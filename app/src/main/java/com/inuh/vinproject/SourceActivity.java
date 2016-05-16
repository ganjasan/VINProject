package com.inuh.vinproject;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.inuh.vinproject.api.response.SourceResponse;
import com.inuh.vinproject.model.Source;
import com.inuh.vinproject.api.requests.SourceRequest;
import com.inuh.vinproject.api.RestService;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;


public class SourceActivity extends Activity {

    private SpiceManager mSpiceManager = new SpiceManager(RestService.class);

    private TextView mTextView;
    private SourceRequest mSourceRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_PROGRESS);
        setContentView(R.layout.activity_source);

        mTextView = (TextView)findViewById(R.id.source_list);

        mSourceRequest = new SourceRequest(0);
    }

    @Override
    protected void onStart() {
        mSpiceManager.start(this);
        super.onStart();
        mSpiceManager.execute(mSourceRequest, "source", DurationInMillis.ONE_MINUTE, new ListSourceRequestListener());
    }

    @Override
    protected void onStop() {
        mSpiceManager.shouldStop();
        super.onStop();
    }

    private void updateSource(final SourceResponse result){
        String originalText = "Source list:";

        StringBuilder builder = new StringBuilder();
        builder.append(originalText);
        builder.append('\n');
        for (Source source : result.getData()){
            builder.append('\t');
            builder.append(source.getName());
            builder.append('\n');
        }
        mTextView.setText(builder.toString());
    }

    public final class ListSourceRequestListener implements RequestListener<SourceResponse>{
        @Override
        public void onRequestFailure(SpiceException spiceException) {
            Toast.makeText(SourceActivity.this, "failure", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onRequestSuccess(SourceResponse sources) {
            Toast.makeText(SourceActivity.this, "success", Toast.LENGTH_SHORT).show();
            updateSource(sources);
        }
    }
}
