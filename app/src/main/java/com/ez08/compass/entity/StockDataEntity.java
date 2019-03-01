package com.ez08.compass.entity;

public class StockDataEntity {
    private String title;
    private String content;
    private int contentColor;

    public StockDataEntity(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public StockDataEntity(String title, String content,int contentColor) {
        this.title = title;
        this.content = content;
        this.contentColor = contentColor;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getContentColor() {
        return contentColor;
    }

    public void setContentColor(int contentColor) {
        this.contentColor = contentColor;
    }
}
