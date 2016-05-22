package com.inuh.vinproject.api.response;

import com.inuh.vinproject.model.Page;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by artimus on 16.05.16.
 */
public class PageResponse {

    private int offset;
    private String nextPage;
    private int totalObjects;

    private Collection<Page> data;

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

    public Collection<Page> getData() {
        return data;
    }

    public void setData(Collection<Page> data) {
        this.data = data;
    }
}
