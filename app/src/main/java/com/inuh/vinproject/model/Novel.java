package com.inuh.vinproject.model;

import android.content.ContentValues;

import com.inuh.vinproject.api.response.NovelResponse;
import com.inuh.vinproject.provider.TableContracts.TableNovels;
import com.j256.ormlite.field.DatabaseField;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by artimus on 16.05.16.
 */
public class Novel {

    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField(columnName = "objectId")
    private String objectId;
    @DatabaseField(columnName = "created")
    private long created;
    @DatabaseField(columnName = "updated")
    private long updated;
    @DatabaseField(columnName = "name")
    private String name;
    @DatabaseField(columnName = "description")
    private String description;
    @DatabaseField(columnName = "totalPageCount")
    private int pageTotal;
    @DatabaseField(columnName = "imgHref")
    private String imgHref;

    @DatabaseField(foreign  = true, foreignAutoRefresh = true)
    private NovelResponse response;

    public Novel(){

    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public long getCreated() {
        return created;
    }

    public void setCreated(long created) {
        this.created = created;
    }

    public long getUpdated() {
        return updated;
    }

    public void setUpdated(long updated) {
        this.updated = updated;
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

    public String getImgHref() {
        return imgHref;
    }

    public void setImgHref(String imgHref) {
        this.imgHref = imgHref;
    }

    public NovelResponse getResponse() {
        return response;
    }

    public void setResponse(NovelResponse response) {
        this.response = response;
    }

    public int getPageTotal() {
        return pageTotal;
    }

    public void setPageTotal(int pageTotal) {
        this.pageTotal = pageTotal;
    }
}
