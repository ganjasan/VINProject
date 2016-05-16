package com.inuh.vinproject.model;

import android.content.ContentValues;

import com.inuh.vinproject.provider.TableContracts.TableNovels;

/**
 * Created by artimus on 16.05.16.
 */
public class Novel extends Model {

    private String name;
    private String description;
    private String sourceObjectId;

    @Override
    public ContentValues toContentValues() {
        ContentValues cv = new ContentValues();
        cv.put(TableNovels._OBJECTID, objectId);
        cv.put(TableNovels._CREATED, created);
        cv.put(TableNovels._UPDATED, updated);
        cv.put(TableNovels.NAME, name);
        cv.put(TableNovels.DESCRIPTION, description);
        cv.put(TableNovels.SOURCE_ID, sourceObjectId);

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

    public String getSourceObjectId() {
        return sourceObjectId;
    }

    public void setSourceObjectId(String sourceObjectId) {
        this.sourceObjectId = sourceObjectId;
    }
}
