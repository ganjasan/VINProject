package com.inuh.vinproject.api.requests;

import android.util.Log;

import com.inuh.vinproject.api.response.SourceResponse;
import com.inuh.vinproject.api.rest.VinRest;
import com.octo.android.robospice.request.retrofit.RetrofitSpiceRequest;

/**
 * Created by artimus on 16.05.16.
 */
public class SourceRequest extends RetrofitSpiceRequest<SourceResponse, VinRest> {

    private int offset;

    public SourceRequest(int offset){
        super(SourceResponse.class, VinRest.class);
        this.offset = offset;
    }

    @Override
    public SourceResponse loadDataFromNetwork() throws Exception {
        Log.d("RestRequset", "Call web service");
        return getService().getSources(offset);
    }
}
