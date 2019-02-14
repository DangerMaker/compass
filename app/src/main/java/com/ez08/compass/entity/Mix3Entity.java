package com.ez08.compass.entity;

import java.io.Serializable;

/**
 * Created by dingwei on 16/9/20.
 * 交叉匹配实体类
 */
public class Mix3Entity implements Serializable {
    private String stockName;//股票名称
    private String stockCode;//股票代码
    private int stockPeopleCount;//股民关注数
    private int stockNewsCount;//媒体关注数
    private int stockCapitalCount;//资金关注数

    public String getStockName() {
        return stockName;
    }

    public void setStockName(String stockName) {
        this.stockName = stockName;
    }

    public String getStockCode() {
        return stockCode;
    }

    public void setStockCode(String stockCode) {
        this.stockCode = stockCode;
    }

    public int getStockPeopleCount() {
        return stockPeopleCount;
    }

    public void setStockPeopleCount(int stockPeopleCount) {
        this.stockPeopleCount = stockPeopleCount;
    }

    public int getStockNewsCount() {
        return stockNewsCount;
    }

    public void setStockNewsCount(int stockNewsCount) {
        this.stockNewsCount = stockNewsCount;
    }

    public int getStockCapitalCount() {
        return stockCapitalCount;
    }

    public void setStockCapitalCount(int stockCapitalCount) {
        this.stockCapitalCount = stockCapitalCount;
    }
}
