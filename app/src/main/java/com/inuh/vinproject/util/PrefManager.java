package com.inuh.vinproject.util;

import android.content.Context;
import android.content.Intent;
import android.preference.PreferenceManager;

import com.inuh.vinproject.model.Source;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by artimus on 18.05.16.
 */
public class PrefManager {


    public final static String PREF_CHANGE_NOTIFICATION = "PrefChangeNotification";
    public final static String PREF_SELECTED_SOURCE = "SelectedSources";

    public final static String PREF_FILTER_STATUS = "status";
    public final static String PREF_FILTER_SORT = "sort";

    public final static String PREF_FAVORITE_NOVELS_LIST = "com.inuh.vinproject.pref_manage.favorite_novels_list";
    public final static String PREF_DOWNLOADED_NOVELS_LIST = "com.inuh.vinptoject.pref_manager.downloaded_novels_list";

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

    public void setSortFilter(String filter){
        PreferenceManager.getDefaultSharedPreferences(mContext)
                .edit()
                .putString(PREF_FILTER_SORT, filter)
                .commit();

        mContext.sendBroadcast(new Intent(PREF_CHANGE_NOTIFICATION));
    }

    public String getSortFilter(){
        return PreferenceManager.getDefaultSharedPreferences(mContext).getString(PREF_FILTER_SORT, "name");
    }

    public void setStatusFilter(String filter){
        PreferenceManager.getDefaultSharedPreferences(mContext)
                .edit()
                .putString(PREF_FILTER_STATUS, filter)
                .commit();

        mContext.sendBroadcast(new Intent(PREF_CHANGE_NOTIFICATION));
    }

    public String getStatusFilter(){
        return PreferenceManager.getDefaultSharedPreferences(mContext).getString(PREF_FILTER_STATUS, "all");
    }

    public void setNovelsFavorite(String novelsId){
        Set<String> favoriteNovels = PreferenceManager.getDefaultSharedPreferences(mContext)
                .getStringSet(PREF_FAVORITE_NOVELS_LIST, new HashSet<String>());
        favoriteNovels.add(novelsId);
        PreferenceManager.getDefaultSharedPreferences(mContext)
                .edit()
                .putStringSet(PREF_FAVORITE_NOVELS_LIST, favoriteNovels)
                .commit();
    }

    public boolean isNovelsFavorite(String novelsId){
        return PreferenceManager.getDefaultSharedPreferences(mContext)
                .getStringSet(PREF_FAVORITE_NOVELS_LIST, new HashSet<String>()).contains(novelsId);
    }

    public boolean isFavoriteNovelsListEmpty(){
        return PreferenceManager.getDefaultSharedPreferences(mContext)
                .getStringSet(PREF_FAVORITE_NOVELS_LIST, new HashSet<String>()).isEmpty();
    }

    public String getFavoritesNovelsString(){
        Set<String> favoritesNovels = PreferenceManager.getDefaultSharedPreferences(mContext)
                .getStringSet(PREF_FAVORITE_NOVELS_LIST, new HashSet<String>());

        StringBuilder stringBuilder = new StringBuilder();
        for (String novelsId: favoritesNovels) {
            stringBuilder.append("'" + novelsId +"',");
        }
        if(stringBuilder.length() != 0){
            stringBuilder.deleteCharAt(stringBuilder.length()-1);
        }

        return stringBuilder.toString();
    }

    public boolean isNovelDownloaded(String novelsId){
        return PreferenceManager.getDefaultSharedPreferences(mContext)
                .getStringSet(PREF_DOWNLOADED_NOVELS_LIST, new HashSet<String>()).contains(novelsId);
    }

    public void setDownloadedNovel(String novelsId){
        Set<String> favoriteNovels = PreferenceManager.getDefaultSharedPreferences(mContext)
                .getStringSet(PREF_DOWNLOADED_NOVELS_LIST, new HashSet<String>());

        favoriteNovels.add(novelsId);
        PreferenceManager.getDefaultSharedPreferences(mContext)
                .edit()
                .putStringSet(PREF_DOWNLOADED_NOVELS_LIST, favoriteNovels)
                .commit();
    }
}
