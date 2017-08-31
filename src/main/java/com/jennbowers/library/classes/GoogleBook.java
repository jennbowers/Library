package com.jennbowers.library.classes;

public class GoogleBook {
    private String kind;
    private String id;
    private String etag;
    private String selfLink;
    private Object volumeInfo;
    private Object salesInfo;
    private Object accessInfo;
    private Object searchInfo;

    public GoogleBook() {
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEtag() {
        return etag;
    }

    public void setEtag(String etag) {
        this.etag = etag;
    }

    public String getSelfLink() {
        return selfLink;
    }

    public void setSelfLink(String selfLink) {
        this.selfLink = selfLink;
    }

    public Object getVolumeInfo() {
        return volumeInfo;
    }

    public void setVolumeInfo(Object volumeInfo) {
        this.volumeInfo = volumeInfo;
    }

    public Object getSalesInfo() {
        return salesInfo;
    }

    public void setSalesInfo(Object salesInfo) {
        this.salesInfo = salesInfo;
    }

    public Object getAccessInfo() {
        return accessInfo;
    }

    public void setAccessInfo(Object accessInfo) {
        this.accessInfo = accessInfo;
    }

    public Object getSearchInfo() {
        return searchInfo;
    }

    public void setSearchInfo(Object searchInfo) {
        this.searchInfo = searchInfo;
    }
}
