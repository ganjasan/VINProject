package com.inuh.vinproject;

import android.app.Application;

import com.inuh.vinproject.provider.HelperFactory;

/**
 * Created by artimus on 23.05.16.
 */
public class MyApplcation extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        HelperFactory.setHelper(getApplicationContext());
    }
    @Override
    public void onTerminate() {
        HelperFactory.releaseHelper();
        super.onTerminate();
    }

}
