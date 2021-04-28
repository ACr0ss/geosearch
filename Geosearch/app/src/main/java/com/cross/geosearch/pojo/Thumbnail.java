package com.cross.geosearch.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Thumbnail implements Serializable {
    @Expose
    @SerializedName("source")
    public String source;

    @Expose
    @SerializedName("width")
    public int width;

    @Expose
    @SerializedName("height")
    public int height;

    public Thumbnail(String source, int width, int height) {
        this.source = source;
        this.width = width;
        this.height = height;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
