package com.inuh.vinproject.model;


import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "loadNovel")
public class LoadNovel {

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField private String objectId;
    @DatabaseField private String name;
    @DatabaseField private int pageTotal;
    @DatabaseField private String description;
    @DatabaseField(dataType = DataType.BYTE_ARRAY)
    private byte[] imgBytes;

    public LoadNovel(){

    }

    public LoadNovel(Novel novel){
        this.objectId = novel.getObjectId();
        this.description = novel.getDescription();
        this.name = novel.getName();
        this.pageTotal = novel.getPageTotal();
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPageTotal() {
        return pageTotal;
    }

    public void setPageTotal(int pageTotal) {
        this.pageTotal = pageTotal;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public byte[] getImgBytes() {
        return imgBytes;
    }

    public void setImgBytes(byte[] imgBytes) {
        this.imgBytes = imgBytes;
    }
}
