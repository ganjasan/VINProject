package com.inuh.vinproject.model;

import android.content.ContentValues;

import com.inuh.vinproject.provider.TableContracts.TableSource;

/**
 * Created by artimus on 16.05.16.
 */
public class Source extends Model {


    private String name;
    private String description;
    private String href;

    @Override
    public ContentValues toContentValues() {

        ContentValues cv = new ContentValues();
        cv.put(TableSource._OBJECTID, objectId);
        cv.put(TableSource._CREATED, created);
        cv.put(TableSource._UPDATED, updated);
        cv.put(TableSource.NAME, name);
        cv.put(TableSource.DESCRIPTION, description);
        cv.put(TableSource.HREF, href);

        return cv;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }
}
