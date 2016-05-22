package com.inuh.vinproject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.inuh.vinproject.api.RestService;
import com.inuh.vinproject.api.requests.CatalogRequest;
import com.inuh.vinproject.api.response.NovelResponse;
import com.inuh.vinproject.model.Novel;
import com.inuh.vinproject.view.EndlessRecyclerViewScrollListener;
import com.inuh.vinproject.util.PrefManager;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;
import com.squareup.picasso.Picasso;

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

        IntentFilter filter = new IntentFilter(PrefManager.PREF_CHANGE_NOTIFICATION);
        getActivity().registerReceiver(mOnPreferenceChange, filter);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.catalog_fragment_layout, container, false);

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
                    CatalogRequest request = new CatalogRequest(totalItemsCount, mWhereClause);
                    mSpiceManager.execute(request, request.hashCode(), DurationInMillis.ONE_DAY, new NovelRequestListener());
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
            if(mNovelList.size() == 0) {
                CatalogRequest request = new CatalogRequest(0, mWhereClause);
                mSpiceManager.execute(request, request.hashCode(), DurationInMillis.ONE_DAY, new NovelRequestListener());
            }
        }else{

            Toast.makeText(getActivity(), "no selected sources", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        mSpiceManager.shouldStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(mOnPreferenceChange);
    }

    private BroadcastReceiver mOnPreferenceChange = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(mNovelList != null && mAdapter != null)
                mNovelList.clear();
                mAdapter.notifyDataSetChanged();
            mWhereClause = getWhereClause();
        }
    };





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

    private class NovelHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private Novel       mNovel;
        private TextView    mNameTextView;
        private ImageView   mImageView;

        public NovelHolder(View itemView){
            super(itemView);
            itemView.setOnClickListener(this);
            mNameTextView = (TextView)itemView.findViewById(R.id.novel_name);
            mImageView = (ImageView)itemView.findViewById(R.id.novel_image);

        }

        public void bindHolder(Novel novel){
            mNovel = novel;
            mNameTextView.setText(novel.getName());
            Picasso.with(getActivity())
                    .load(novel.getImgHref())
                    .placeholder(android.R.drawable.ic_dialog_info)
                    .into(mImageView);
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getActivity(), ReaderActivity.class);
            intent.putExtra(ReaderActivity.EXTRA_NOVELS_OBJECTID, mNovel.getObjectId());
            intent.putExtra(ReaderActivity.EXTRA_TOTAL_PAGE_COUNT, mNovel.getPageTotal());
            startActivity(intent);
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
