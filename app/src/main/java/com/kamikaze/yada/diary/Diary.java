package com.kamikaze.yada.diary;

import android.widget.EditText;

import com.kamikaze.yada.model.Notes;

public class Diary {
    private String title;
    private String description;
    private String location;
    private String bgImageUrl=null;
    private Notes note=null ;




    public Diary(String title, String description, String location , Notes note) {
        this.title = title;
        this.description = description;
        this.location = location;
        this.note = note;
    }

    public Diary(String title, String description, String location, String bgImageUrl, Notes note) {
        this.title = title;
        this.description = description;
        this.location = location;
        this.bgImageUrl = bgImageUrl;
        this.note = note;
    }

    public Diary(String title, String description, String location) {
        this.title = title;
        this.description = description;
        this.location = location;
    }
    public String getBgImageUrl() {
        return bgImageUrl;
    }

    public void setBgImageUrl(String bgImageUrl) {
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

    public Notes getNote() {
        return note;
    }

    public void setNote(Notes note) {
        this.note = note;
    }
}
