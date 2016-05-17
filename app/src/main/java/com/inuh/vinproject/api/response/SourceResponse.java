package com.inuh.vinproject.api.response;

import com.inuh.vinproject.spiceadapter.Source;
import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by artimus on 16.05.16.
 */
public class SourceResponse {


    private int offset;
    private String nextPage;
    private int totalObjects;

    @DatabaseField(generatedId =  true)
    private int id;

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
