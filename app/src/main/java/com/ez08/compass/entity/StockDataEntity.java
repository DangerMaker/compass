package com.ez08.compass.entity;

public class StockDataEntity {
    private String title;
    private String content;
    private String contentColor;

    public StockDataEntity(String title, String content) {
        this.title = title;
        this.content = content;
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

    public String getContentColor() {
        return contentColor;
    }

    public void setContentColor(String contentColor) {
        this.contentColor = contentColor;
    }
}
