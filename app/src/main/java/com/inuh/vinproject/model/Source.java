package com.inuh.vinproject.model;

import android.preference.PreferenceManager;
import android.provider.BaseColumns;

import com.inuh.vinproject.api.response.SourceResponse;
import com.j256.ormlite.field.DatabaseField;

public class Source {
    @DatabaseField(columnName = BaseColumns._ID, generatedId = true)
    private int     id;
    @DatabaseField(columnName = "objectId")
    private String  objectId;
    @DatabaseField(columnName = "created")
    private long     created;
    @DatabaseField(columnName = "updated")
    private long     updated;
    @DatabaseField(columnName = "name")
    private String  name;
    @DatabaseField(columnName = "description")
    private String  description;
    @DatabaseField(columnName = "href")
    private String  href;



    private boolean isSelect;

    @DatabaseField(foreign = true, foreignAutoRefresh = true)
    private SourceResponse response;

    public Source(){

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

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }
}
