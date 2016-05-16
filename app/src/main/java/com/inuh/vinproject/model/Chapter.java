package com.inuh.vinproject.model;

import android.content.ContentValues;

import com.inuh.vinproject.provider.TableContracts.TableChapters;

/**
 * Created by artimus on 16.05.16.
 */
public class Chapter extends Model{

    private String name;
    private String description;
    private String firstPage;
    private String novelObjectId;

    @Override
    public ContentValues toContentValues() {
        ContentValues cv = new ContentValues();
        cv.put(TableChapters._OBJECTID, objectId);
        cv.put(TableChapters._CREATED, created);
        cv.put(TableChapters._UPDATED, updated);
        cv.put(TableChapters.NAME, name);
        cv.put(TableChapters.DESCRIPTION, description);
        cv.put(TableChapters.FIRSTPAGE, firstPage);
        cv.put(TableChapters.NOVELS_ID, novelObjectId);

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

    public String getFirstPage() {
        return firstPage;
    }

    public void setFirstPage(String firstPage) {
        this.firstPage = firstPage;
    }

    public String getNovelObjectId() {
        return novelObjectId;
    }

    public void setNovelObjectId(String novelObjectId) {
        this.novelObjectId = novelObjectId;
    }
}
