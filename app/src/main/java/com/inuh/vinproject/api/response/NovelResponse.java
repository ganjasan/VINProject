package com.inuh.vinproject.api.response;

import com.inuh.vinproject.model.Novel;

import java.util.ArrayList;

/**
 * Created by artimus on 16.05.16.
 */
public class NovelResponse {

    private int offset;
    private String nextPage;
    private int totalObjects;

    private ArrayList<Novel> data;

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

    public ArrayList<Novel> getData() {
        return data;
    }

    public void setData(ArrayList<Novel> data) {
        this.data = data;
    }
}
