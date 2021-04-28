package com.cross.geosearch.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Page implements Serializable {
    @Expose
    @SerializedName("pageid")
    public int pageid;

    @Expose
    @SerializedName("ns")
    public int ns;

    @Expose
    @SerializedName("title")
    public String title;

    @Expose
    @SerializedName("index")
    public int index;

    @Expose
    @SerializedName("coordinates")
    public List<Coordinate> coordinates;

    @Expose
    @SerializedName("thumbnail")
    public Thumbnail thumbnail;

    @Expose
    @SerializedName("pageimage")
    public String pageimage;

    public Page(int pageid, int ns, String title, int index, List<Coordinate> coordinates, Thumbnail thumbnail, String pageimage) {
        this.pageid = pageid;
        this.ns = ns;
        this.title = title;
        this.index = index;
        this.coordinates = coordinates;
        this.thumbnail = thumbnail;
        this.pageimage = pageimage;
    }

    public int getPageid() {
        return pageid;
    }

    public void setPageid(int pageid) {
        this.pageid = pageid;
    }

    public int getNs() {
        return ns;
    }

    public void setNs(int ns) {
        this.ns = ns;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public List<Coordinate> getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(List<Coordinate> coordinates) {
        this.coordinates = coordinates;
    }

    public Thumbnail getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(Thumbnail thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getPageimage() {
        return pageimage;
    }

    public void setPageimage(String pageimage) {
        this.pageimage = pageimage;
    }
}
