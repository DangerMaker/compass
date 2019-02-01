package com.ez08.compass.entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/3/27.
 */

public class AdvertEntity implements Serializable {

    private String starttime;
    private String endtime;
    private String level;
    private String imageurl;
    private String infourl;
    private String showplace;
    private String ahtusId;
    private int mt;

    public int getMt() {
        return mt;
    }

    public void setMt(int mt) {
        this.mt = mt;
    }

    public String getAhtusId() {
        return ahtusId;
    }

    public void setAhtusId(String ahtusId) {
        this.ahtusId = ahtusId;
    }

    public String getStarttime() {
        return starttime;
    }

    public void setStarttime(String starttime) {
        this.starttime = starttime;
    }

    public String getEndtime() {
        return endtime;
    }

    public void setEndtime(String endtime) {
        this.endtime = endtime;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

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

    public void setShowplace(String showplace) {
        this.showplace = showplace;
    }
}
