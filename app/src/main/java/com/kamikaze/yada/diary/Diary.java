package com.kamikaze.yada.diary;

public class Diary {
    private String title;
    private String description;
    private String location;
    private String bgImageUrl=null;

    public String getBgImageUrl() {
        return bgImageUrl;
    }

    public void setBgImageUrl(String bgImageUrl) {
        this.bgImageUrl = bgImageUrl;
    }

    public Diary(String title, String description, String location) {
        this.title = title;
        this.description = description;
        this.location = location;
    }

    public Diary(String title, String description, String location, String bgImageUrl) {
        this.title = title;
        this.description = description;
        this.location = location;
        this.bgImageUrl = bgImageUrl;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
