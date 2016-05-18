package com.inuh.vinproject.api.response;

import com.inuh.vinproject.model.Novel;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by artimus on 16.05.16.
 */
public class NovelResponse {

    @DatabaseField(generatedId =  true)
    private int id;

    @DatabaseField(columnName = "offset")
    private int offset;
    @DatabaseField(columnName = "nextPage")
    private String nextPage;
    @DatabaseField(columnName = "totalObjects")
    private int totalObjects;

    @ForeignCollectionField(eager = true)
    private Collection<Novel> data;

    public NovelResponse(){

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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

    public Collection<Novel> getData() {
        return data;
    }

    public void setData(Collection<Novel> data) {
        this.data = data;
    }
}
