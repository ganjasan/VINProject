package com.inuh.vinproject.api.response;

import com.inuh.vinproject.model.Chapter;

import java.util.ArrayList;

/**
 * Created by artimus on 16.05.16.
 */
public class ChapterResponse {

    private int offset;
    private String nextPage;
    private int totalObjects;

    private ArrayList<Chapter> data;

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

    public ArrayList<Chapter> getData() {
        return data;
    }

    public void setData(ArrayList<Chapter> data) {
        this.data = data;
    }
}
