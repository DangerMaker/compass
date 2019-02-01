package com.ez08.compass.entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/4/11.
 */

public class LevelAuthsEntity implements Serializable {
    private String authsID;
    private String imeiID;
    private String name;
    private String state;
    private String days;
    private String start;
    private String end;

    public String getImeiID() {
        return imeiID;
    }

    public void setImeiID(String imeiID) {
        this.imeiID = imeiID;
    }

    public String getAuthsID() {
        return authsID;
    }

    public void setAuthsID(String authsID) {
        this.authsID = authsID;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }
}
