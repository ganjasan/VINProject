package com.inuh.vinproject.spiceadapter;

import android.provider.BaseColumns;

import com.inuh.vinproject.api.response.SourceResponse;
import com.j256.ormlite.field.DatabaseField;



public class Source {
    @DatabaseField(columnName = BaseColumns._ID, generatedId = true)
    private int id;

    @DatabaseField(columnName = "name")
    private String name;

    @DatabaseField(foreign = true, foreignAutoRefresh = true)
    private SourceResponse response;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public SourceResponse getResponse() {
        return response;
    }

    public void setResponse(SourceResponse response) {
        this.response = response;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + id;

        return result;
    }

    @Override
    public String toString() {
        return "Source " + name;
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj){
            return true;
        }
        if(obj == null){
            return  false;
        }
        if(getClass() != obj.getClass()){
            return false;
        }

        Source other = (Source)obj;
        if(id != other.id && name != other.name){
            return false;
        }

        return true;
    }
}
