package com.ez08.compass.entity;

import android.graphics.Bitmap;

public class InfoEntity1 extends VideoEntity {

    private boolean ischeck=false;

    public boolean ischeck() {
        return ischeck;
    }

    public void setIscheck(boolean ischeck) {
        this.ischeck = ischeck;
    }
    private Bitmap bmp;

    private String topDes;

    public Bitmap getBmp() {
        return bmp;
    }

    public void setBmp(Bitmap bmp) {
        this.bmp = bmp;
    }

    public String getTopDes() {
        return topDes;
    }

    public void setTopDes(String topDes) {
        this.topDes = topDes;
    }
}
