package com.ez08.compass.entity;

/**
 * Created by Administrator on 2018/2/2.
 */

public class ColumnValuesBarListModel extends ColumnValuesDataModel {
    private int type;
    private boolean pre;
    public ColumnValuesBarListModel(int columnColor, int columnFillColor, float value) {
        super(columnColor, columnFillColor, value);
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean isPre() {
        return pre;
    }

    public void setPre(boolean pre) {
        this.pre = pre;
    }
}
