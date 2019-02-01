package com.ez08.compass.entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/3/27.
 */

public class NewAdvertEntity implements Serializable {

    private String imageurl;
    private String infourl;
    private String showplace;
    private String title;


    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public String getInfourl() {
        return infourl;
    }

    public void setInfourl(String infourl) {
        this.infourl = infourl;
    }

    public String getShowplace() {
        return showplace;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setShowplace(String showplace) {
        this.showplace = showplace;
    }
}
