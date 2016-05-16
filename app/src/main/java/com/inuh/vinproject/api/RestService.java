package com.inuh.vinproject.api;

import com.octo.android.robospice.retrofit.RetrofitGsonSpiceService;

/**
 * Created by artimus on 16.05.16.
 */
public class RestService extends RetrofitGsonSpiceService {

    private final static String BASE_URL = "https://api.backendless.com/";

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    protected String getServerUrl() {
        return BASE_URL;
    }

}
