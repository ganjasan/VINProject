package com.inuh.vinproject.api.response;

import com.inuh.vinproject.model.Source;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;

import java.util.Collection;

public class SourceResponse {

    @DatabaseField(generatedId =  true)
    private int id;

    @DatabaseField(columnName = "offset")
    private int offset;
    @DatabaseField(columnName = "nextPage")
    private String nextPage;
    @DatabaseField(columnName = "totalObjects")
    private int totalObjects;

    @ForeignCollectionField (eager = true)
    private Collection<Source> data;

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public String getNextPage() {
        return nextPage;
    }

    public void setNextPage(String nextPage) {
        this.nextPage = nextPage;
    }

    public int getTotalObjects() {
        return totalObjects;
    }

    public void setTotalObjects(int totalObjects) {
        this.totalObjects = totalObjects;
    }

    public Collection<Source> getData() {
        return data;
    }

    public void setData(Collection<Source> data) {
        this.data = data;
    }
}
