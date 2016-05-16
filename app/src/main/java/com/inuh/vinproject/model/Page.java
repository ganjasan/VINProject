package com.inuh.vinproject.model;

import android.content.ContentValues;

import com.inuh.vinproject.provider.TableContracts;

/**
 * Created by artimus on 16.05.16.
 */
public class Page extends Model {

    private String name;
    private String novelsId;
    private int    number;

    @Override
    public ContentValues toContentValues() {
        ContentValues cv = new ContentValues();

        return cv;
    }


}
