package com.kamikaze.yada;

import android.location.Location;
import android.location.LocationManager;

public class Diary {
    private String title;
    private String description;
    public Diary(String title, String description) {
        this.title = title;
        this.description = description;
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
