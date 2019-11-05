package com.aruna.mvvmexample.models;

public class NicePlace {

    private String imageUrl;
    private String title;
    private String video_url;

    public NicePlace(String imageUrl, String title, String video_url) {
        this.imageUrl = imageUrl;
        this.title = title;
        this.video_url = video_url;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getVideo_url() {
        return video_url;
    }

    public void setVideo_url(String video_url) {
        this.video_url = video_url;
    }
}
