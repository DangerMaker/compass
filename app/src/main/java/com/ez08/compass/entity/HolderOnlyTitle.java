package com.ez08.compass.entity;

public class HolderOnlyTitle {//only title

    String title;
    boolean white = false;

    public HolderOnlyTitle(String title) {
        this.title = title;
    }

    public HolderOnlyTitle(String title,boolean white) {
        this.title = title;
        this.white = white;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isWhite() {
        return white;
    }

    public void setWhite(boolean white) {
        this.white = white;
    }
}
