package com.inuh.vinproject;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.TextView;
import android.widget.Toast;

import com.inuh.vinproject.api.RestService;
import com.inuh.vinproject.api.requests.CatalogRequest;
import com.inuh.vinproject.api.response.NovelResponse;
import com.inuh.vinproject.api.response.SourceResponse;
import com.inuh.vinproject.model.Novel;
import com.inuh.vinproject.model.Source;
import com.inuh.vinproject.util.EndlessRecyclerViewScrollListener;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by artimus on 18.05.16.
 */
public class CatalogFragment extends Fragment {

    private RecyclerView mCatalogRecyclerView;
    private SpiceManager mSpiceManager = new SpiceManager(RestService.class);
    private NovelsAdapter mAdapter;
    private List<Novel> mNovelList = new ArrayList<Novel>();
    private LinearLayoutManager mLinearLayoutManager;
    private String mWhereClause;

    private int totalCount;


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.catalog_fragment_layout, container, false);

        //mWhereClause = getWhereClause();

        mCatalogRecyclerView = (RecyclerView)view.findViewById(R.id.catalog_list);
        mNovelList = new ArrayList<Novel>();
        mAdapter = new NovelsAdapter(mNovelList);
        mCatalogRecyclerView.setAdapter(mAdapter);
        mLinearLayoutManager = new LinearLayoutManager(getActivity());
        mCatalogRecyclerView.setLayoutManager(mLinearLayoutManager);
        mCatalogRecyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(mLinearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                if(totalItemsCount < totalCount) {
                    mSpiceManager.execute(new CatalogRequest(totalItemsCount, mWhereClause), "Novels", DurationInMillis.ONE_DAY, new NovelRequestListener());
                }
            }
        });


        return view;
    }

    @Override
    public void onStart() {
        mSpiceManager.start(getContext());
        super.onStart();
        if (PrefManager.getInstance(getActivity()).getSelectedSource().length() != 0) {
            mWhereClause = getWhereClause();
            mSpiceManager.execute(new CatalogRequest(0, mWhereClause), "Novels", DurationInMillis.ONE_DAY, new NovelRequestListener());
        }else{
            Toast.makeText(getActivity(), "no selected sources", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onStop() {
        mSpiceManager.shouldStop();
        super.onStop();
    }

    public final class NovelRequestListener implements RequestListener<NovelResponse>{

        @Override
        public void onRequestFailure(SpiceException spiceException) {
            Toast.makeText(getActivity(), "failure", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onRequestSuccess(NovelResponse response) {
            totalCount = response.getTotalObjects();
            int curSize = mAdapter.getItemCount();
            Collection<Novel> novelsList = response.getData();
            mNovelList.addAll(novelsList);
            mAdapter.notifyItemRangeInserted(curSize, mNovelList.size() - 1);
        }
    }

    private class NovelHolder extends RecyclerView.ViewHolder{

        private Novel mNovel;
        private TextView mNameTextView;

        public NovelHolder(View itemView){
            super(itemView);
            mNameTextView = (TextView)itemView.findViewById(R.id.novel_name);
        }

        public void bindHolder(Novel novel){
            mNovel = novel;
            mNameTextView.setText(novel.getName());
        }

    }

    private class NovelsAdapter extends RecyclerView.Adapter<NovelHolder>{
        private ArrayList<Novel> novelList;

        public NovelsAdapter(List<Novel> novels){
            novelList = (ArrayList<Novel>)novels;
        }

        @Override
        public void onBindViewHolder(NovelHolder holder, int position) {
            Novel novel = novelList.get(position);
            holder.bindHolder(novel);
        }

        @Override
        public NovelHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.novel_item_layout, parent, false);
            return new NovelHolder(view);
        }

        @Override
        public int getItemCount() {
            return  novelList.size();
        }
    }


    private String getWhereClause(){
        String whereSourceInClause = "Sources[novels].objectId in (" + PrefManager.getInstance(getContext()).getSelectedSource() + ")";

        String whereClause = whereSourceInClause;

        return whereClause;

    }



}
