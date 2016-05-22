package com.inuh.vinproject.util;

import android.content.Context;
import android.content.Intent;
import android.preference.PreferenceManager;

import com.inuh.vinproject.model.Source;

/**
 * Created by artimus on 18.05.16.
 */
public class PrefManager {


    public final static String PREF_CHANGE_NOTIFICATION = "PrefChangeNotification";
    public final static String PREF_SELECTED_SOURCE = "SelectedSources";

    private static PrefManager sInstance;

    private Context mContext;

    public static PrefManager getInstance(Context context) {
        if(sInstance == null) {
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

        mContext.sendBroadcast(new Intent(PREF_CHANGE_NOTIFICATION));
    }

    public String getSelectedSource(){

        String selectedSources = PreferenceManager.getDefaultSharedPreferences(mContext).getString("SelectedSources", "");
        return   selectedSources;

    }

    public void setNovelsLastPage(int number, String novelsId){
        PreferenceManager.getDefaultSharedPreferences(mContext)
                .edit()
                .putInt(novelsId, number)
                .commit();
    }

    public int getNovelsLastPage(String novelsId){
        return PreferenceManager.getDefaultSharedPreferences(mContext).getInt(novelsId, 1);
    }
}
