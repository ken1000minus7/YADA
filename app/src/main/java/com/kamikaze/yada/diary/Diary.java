package com.kamikaze.yada.diary;

import android.widget.EditText;

import com.kamikaze.yada.model.Notes;

import java.util.ArrayList;
import java.util.List;

public class Diary {
    private String title;
    private String description;
    private String location;
    private String bgImageUrl=null;
    private Notes note=null ;
    private int color=-1;
    private  int fabcolor=-1;
    private List<String> images=new ArrayList<>();

    public Diary(String title, String description, String location, String bgImageUrl, Notes note, List<String> images) {
        this.title = title;
        this.description = description;
        this.location = location;
        this.bgImageUrl = bgImageUrl;
        this.note = note;
        this.images = images;
    }

    public Diary(String dtitle, String ddescription, String dlocation, String dbgImageUrl, Notes note, List<String> dimages, int color , int fabcolor) {
        this.title = title;
        this.description = description;
        this.location = location;
        this.bgImageUrl = bgImageUrl;
        this.note = note;
        this.images = images;
        this.color = color;
        this.fabcolor = fabcolor;
    }

    public int  getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getFabcolor() {
        return fabcolor;
    }

    public void setFabcolor(int fabcolor) {
        this.fabcolor = fabcolor;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

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
