package com.inuh.vinproject;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.*;
import android.preference.PreferenceManager;

import com.inuh.vinproject.model.Source;

/**
 * Created by artimus on 18.05.16.
 */
public class PrefManager {

    private final static String PREF_SELECTED_SOURCE = "SelectedSources";

    private static PrefManager sInstance;

    private Context mContext;

    public static PrefManager getInstance(Context context) {
        if(sInstance == null){
            sInstance = new PrefManager(context);
        }

        return sInstance;

    }

    private PrefManager(Context context) {
        mContext = context.getApplicationContext();
    }

    public void setSourceSelected(Source source, boolean select){

        if(select) {
            PreferenceManager.getDefaultSharedPreferences(mContext)
                    .edit()
                    .putBoolean(source.getObjectId(), true)
                    .commit();

            StringBuilder builder = new StringBuilder(PreferenceManager.getDefaultSharedPreferences(mContext)
                    .getString(PREF_SELECTED_SOURCE, ""));
            builder.append("'" + source.getObjectId() + "',");
            builder.deleteCharAt(builder.length()-1);
            PreferenceManager.getDefaultSharedPreferences(mContext)
                    .edit()
                    .putString(PREF_SELECTED_SOURCE, builder.toString())
                    .commit();
        } else{

            PreferenceManager.getDefaultSharedPreferences(mContext)
                    .edit()
                    .putBoolean(source.getObjectId(), false)
                    .commit();

            StringBuilder builder = new StringBuilder(PreferenceManager.getDefaultSharedPreferences(mContext)
                    .getString(PREF_SELECTED_SOURCE,""));
            int startIndex = builder.indexOf(source.getObjectId());
            builder.replace(startIndex-1, startIndex + 38, "");
            if (builder.length() != 0) {
                builder.deleteCharAt(builder.length() - 1);
            }
            PreferenceManager.getDefaultSharedPreferences(mContext)
                    .edit()
                    .putString(PREF_SELECTED_SOURCE, builder.toString())
                    .commit();

        }
    }

    public String getSelectedSource(){

        String selectedSources = PreferenceManager.getDefaultSharedPreferences(mContext).getString("SelectedSources", "");
        return   selectedSources;

    }
}
