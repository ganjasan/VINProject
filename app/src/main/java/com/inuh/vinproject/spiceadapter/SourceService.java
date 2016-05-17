package com.inuh.vinproject.spiceadapter;

import android.app.Application;

import com.inuh.vinproject.api.response.SourceResponse;
import com.octo.android.robospice.persistence.CacheManager;
import com.octo.android.robospice.persistence.exception.CacheCreationException;
import com.octo.android.robospice.persistence.ormlite.InDatabaseObjectPersister;
import com.octo.android.robospice.persistence.ormlite.InDatabaseObjectPersisterFactory;
import com.octo.android.robospice.persistence.ormlite.RoboSpiceDatabaseHelper;
import com.octo.android.robospice.retrofit.RetrofitGsonSpiceService;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by artimus on 17.05.16.
 */
public class SourceService extends RetrofitGsonSpiceService {

    public static final String PREF_SOURCE_IN_CACHE = "source_in_cache";

    public static final String DATABASE_NAME = "vinproject_database";
    public static final int DATABASE_VERSION = 1;

    public static final int WEBSERVICE_TIMEOUT = 10000;

    private final static String BASE_URL = "https://api.backendless.com/";

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    protected String getServerUrl() {
        return BASE_URL;
    }

    @Override
    public CacheManager createCacheManager(Application application) throws CacheCreationException {

        CacheManager cacheManager = new CacheManager();
        List<Class<?>> classCollection = new ArrayList<Class<?>>();

        classCollection.add(Source.class);
        classCollection.add(SourceResponse.class);

        RoboSpiceDatabaseHelper databaseHelper = new RoboSpiceDatabaseHelper(application, DATABASE_NAME, DATABASE_VERSION);
        InDatabaseObjectPersisterFactory inDatabaseObjectPersisterFactory = new InDatabaseObjectPersisterFactory(application, databaseHelper, classCollection);
        cacheManager.addPersister(inDatabaseObjectPersisterFactory);
        return cacheManager;

    }


}
