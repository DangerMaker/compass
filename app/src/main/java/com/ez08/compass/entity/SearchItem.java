package com.ez08.compass.entity;

import java.io.Serializable;

public class SearchItem implements Serializable {
    private String code;//股票代码 000060
    private String market;//SH,SZ
    private String name;//股票名称
    private int type;//股票类型

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMarket() {
        return market;
    }

    public void setMarket(String market) {
        this.market = market;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

}