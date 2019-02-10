package com.ez08.compass.entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/8/23.
 */
public class SimpleEntity implements Serializable {
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
