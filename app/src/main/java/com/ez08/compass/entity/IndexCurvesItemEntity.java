package com.ez08.compass.entity;

/**
 * Created by dingwei on 16/6/1.
 */
public class IndexCurvesItemEntity {
    private String name;
    private boolean ifSelected;

    public IndexCurvesItemEntity(String name, boolean ifSelected){
        this.name=name;
        this.ifSelected=ifSelected;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isIfSelected() {
        return ifSelected;
    }

    public void setIfSelected(boolean ifSelected) {
        this.ifSelected = ifSelected;
    }
}
