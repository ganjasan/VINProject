package com.inuh.vinproject;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.inuh.vinproject.api.response.SourceResponse;
import com.inuh.vinproject.spiceadapter.Source;
import com.inuh.vinproject.api.requests.SourceRequest;
import com.inuh.vinproject.api.RestService;
import com.inuh.vinproject.spiceadapter.SourceService;
import com.j256.ormlite.dao.ForeignCollection;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class SourceActivity extends Activity {

    private Context mContext;

    private String TAG = this.getClass().getSimpleName();
    private long cacheExpireDuration = DurationInMillis.ONE_WEEK;

    private SpiceManager mSpiceManager = new SpiceManager(SourceService.class);

    private RecyclerView mRecyclerView;
    private SourceAdapter mAdapter;
    private SourceRequest mSourceRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = this;

        requestWindowFeature(Window.FEATURE_PROGRESS);
        setContentView(R.layout.activity_source);
        mRecyclerView = (RecyclerView) findViewById(R.id.source_list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mSourceRequest = new SourceRequest(0);
    }

    @Override
    protected void onStart() {
        mSpiceManager.start(this);
        super.onStart();
        mSpiceManager.execute(mSourceRequest, "Source", cacheExpireDuration, new ListSourceRequestListener());

//        boolean sourceInCache = PreferenceManager.getDefaultSharedPreferences(mContext)
//                .getBoolean(RestService.PREF_SOURCE_IN_CACHE, false);
//
//        if (sourceInCache){
//            mSpiceManager.getFromCache(SourceResponse.class, "Source", cacheExpireDuration, new ListSourceRequestListener());
//        }else {
//            mSpiceManager.execute(mSourceRequest, "Source", cacheExpireDuration, new ListSourceRequestListener());
//        }

    }

    @Override
    protected void onStop() {
        mSpiceManager.shouldStop();
        super.onStop();
    }


    public final class ListSourceRequestListener implements RequestListener<SourceResponse>{
        @Override
        public void onRequestFailure(SpiceException spiceException) {
            Toast.makeText(SourceActivity.this, "failure", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onRequestSuccess(SourceResponse sources) {
            Toast.makeText(SourceActivity.this, "success", Toast.LENGTH_SHORT).show();
//            if(sources == null){
//                PreferenceManager.getDefaultSharedPreferences(mContext).
//                        edit()
//                        .putBoolean(RestService.PREF_SOURCE_IN_CACHE, false)
//                        .commit();
//                mSpiceManager.execute(mSourceRequest, "Source", cacheExpireDuration, new ListSourceRequestListener());
//            }else {
//                Collection<Source> sourceList = sources.getData();
//                mAdapter = new SourceAdapter(sourceList);
//                mRecyclerView.setAdapter(mAdapter);
//
//                //объявить, что данные в кэше
//                PreferenceManager.getDefaultSharedPreferences(mContext)
//                        .edit()
//                        .putBoolean(RestService.PREF_SOURCE_IN_CACHE, true)
//                        .commit();
//
//            }

            Collection<Source> sourceList = sources.getData();
            mAdapter = new SourceAdapter(sourceList);
            mRecyclerView.setAdapter(mAdapter);

        }
    }


    private class SourceHolder extends RecyclerView.ViewHolder {

        public TextView mTitleTextView;

        public SourceHolder(View itemView) {
            super(itemView);

            mTitleTextView = (TextView) itemView;
        }
    }

    private class SourceAdapter extends RecyclerView.Adapter<SourceHolder>{

        private ArrayList<Source> mSources;

        public SourceAdapter(Collection<Source> sourceList){
            mSources = new ArrayList<Source>(sourceList);
        }

        @Override
        public void onBindViewHolder(SourceHolder holder, int position) {

            Source source = mSources.get(position);
            holder.mTitleTextView.setText(source.getName());
        }

        @Override
        public SourceHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(SourceActivity.this);
            View view = layoutInflater.inflate(android.R.layout.simple_list_item_1, parent, false);
            return new SourceHolder(view);
        }

        @Override
        public int getItemCount() {
            return mSources.size();
        }
    }
}
