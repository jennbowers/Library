package com.jennbowers.library.classes;

import com.google.api.client.util.Key;

import java.util.List;

public class GoogleBook {
    @Key
    private String id;
    @Key
    private String selfLink;
    @Key("volumeInfo")
    private List<VolumeInfo> volumeInformation;
    @Key("searchInfo")
    private List<SearchInfo> searchInformation;

    public GoogleBook() {
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSelfLink() {
        return selfLink;
    }

    public void setSelfLink(String selfLink) {
        this.selfLink = selfLink;
    }

    public List<VolumeInfo> getVolumeInformation() {
        return volumeInformation;
    }

    public void setVolumeInformation(List<VolumeInfo> volumeInformation) {
        this.volumeInformation = volumeInformation;
    }

    public List<SearchInfo> getSearchInformation() {
        return searchInformation;
    }

    public void setSearchInformation(List<SearchInfo> searchInformation) {
        this.searchInformation = searchInformation;
    }
}
