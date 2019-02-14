package com.ez08.compass.entity;

public class HolderTitleAndAll { // title + all

    public HolderTitleAndAll(String titleName, int sorttype, int type) {
        this.titleName = titleName;
        this.sorttype = sorttype;
        this.type = type;
        this.boardcode = boardcode;
    }

    private String titleName;
    private int sorttype;
    private int type;
    private String boardcode;

    public String getTitleName() {
        return titleName;
    }

    public void setTitleName(String titleName) {
        this.titleName = titleName;
    }

    public String getBoardcode() {
        return boardcode;
    }

    public void setBoardcode(String boardcode) {
        this.boardcode = boardcode;
    }

    public int getSorttype() {
        return sorttype;
    }

    public void setSorttype(int sorttype) {
        this.sorttype = sorttype;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
