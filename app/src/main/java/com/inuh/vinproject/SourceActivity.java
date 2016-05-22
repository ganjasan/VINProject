package com.inuh.vinproject;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.inuh.vinproject.api.RestService;
import com.inuh.vinproject.api.response.SourceResponse;
import com.inuh.vinproject.model.Source;
import com.inuh.vinproject.api.requests.SourceRequest;
import com.inuh.vinproject.util.PrefManager;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import java.util.ArrayList;
import java.util.Collection;


public class SourceActivity extends AppCompatActivity {

    private String TAG = this.getClass().getSimpleName();
    private long cacheExpireDuration = DurationInMillis.ONE_WEEK;

    private SpiceManager mSpiceManager = new SpiceManager(RestService.class);

    private RecyclerView mRecyclerView;
    private SourceAdapter mAdapter;
    private SourceRequest mSourceRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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

            Collection<Source> sourceList = sources.getData();
            mAdapter = new SourceAdapter(sourceList);
            mRecyclerView.setAdapter(mAdapter);

        }
    }


    private class SourceHolder extends RecyclerView.ViewHolder {

        private Source mSource;

        private TextView mTitleTextView;
        private CheckBox mSelectChecbox;


        public SourceHolder(View itemView) {
            super(itemView);
            mTitleTextView = (TextView) itemView.findViewById(R.id.source_name);
            mSelectChecbox = (CheckBox) itemView.findViewById(R.id.source_select_checbox);
            mSelectChecbox.setOnClickListener(new CheckBoxClickListener());
        }

        public void bindSource(Source source){
            mSource = source;
            mTitleTextView.setText(source.getName());
            mSelectChecbox.setChecked(PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).
                    getBoolean(source.getObjectId(), false));
        }

        private class CheckBoxClickListener implements View.OnClickListener{
            @Override
            public void onClick(View v) {
                PrefManager.getInstance(getApplicationContext()).setSourceSelected(mSource, mSelectChecbox.isChecked());
            }
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
            holder.bindSource(source);
        }

        @Override
        public SourceHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(SourceActivity.this);
            View view = layoutInflater.inflate(R.layout.source_item_layout, parent, false);
            return new SourceHolder(view);
        }

        @Override
        public int getItemCount() {
            return mSources.size();
        }
    }
}
