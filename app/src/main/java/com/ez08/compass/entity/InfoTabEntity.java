package com.ez08.compass.entity;

/**
 * Created by Administrator on 2016/9/13.
 */
public class InfoTabEntity {
    private String name;
    private String category;
    private int id;

    public InfoTabEntity(String name, String category) {
        this.name = name;
        this.category = category;
    }

    public InfoTabEntity(String name, String category, int id) {
        this.name = name;
        this.category = category;
        this.id=id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
