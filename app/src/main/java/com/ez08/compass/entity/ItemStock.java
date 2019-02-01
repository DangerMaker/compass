package com.ez08.compass.entity;

import com.ez08.compass.tools.StockUtils;

public class ItemStock extends BaseWebEntity {
    private int isLongPrice;
    private String name = "--";
    private String code = "--";
    private String increase = "0.0"; // 涨幅
    private String value = "0.0"; // 价格
    private String old = "";
    private boolean isMyStock;//是否是自选股
    private String amount;
    private int type; // 股票类型：1，A， 2 B股，3指数
    private boolean isMarket; // 是否三大行情
    private int pageNum; // 加载的是第几级页面

    public int category = 0; // 0custom 1 important
    public int order = 0;
    public int extra1 = 0;
    public int extra2 = 0;
    public double hotValue = 0;
    public int hotFlag = 0;

    public static String splitChar = "^";

    public void setExtra(String code_extra) {
        if (code_extra != null) {
            String code_[] = code_extra.split("\\^");
            if (code_.length == 5) {
                code = StockUtils.getStockCode(code_[0]);
                category = Integer.parseInt(code_[1]);
                order = Integer.parseInt(code_[2]);
                extra1 = Integer.parseInt(code_[3]);
                extra2 = Integer.parseInt(code_[4]);
            }
        }
    }

    public String getExtra(int order1) {
        return code + splitChar + category + splitChar + order1 + splitChar + extra1 + splitChar + extra2;
    }

    private int state;

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getIncresePrice() {
        return incresePrice;
    }

    public void setIncresePrice(String incresePrice) {
        this.incresePrice = incresePrice;
    }

    private String incresePrice;


    public boolean isMarket() {
        return isMarket;
    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public void setMarket(boolean isMarket) {
        this.isMarket = isMarket;
    }

    public String getOld() {
        return old;
    }

    public void setOld(String old) {
        this.old = old;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean isMyStock() {
        return isMyStock;
    }

    public void setMyStock(boolean isMyStock) {
        this.isMyStock = isMyStock;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getIncrease() {
        return increase;
    }

    public void setIncrease(String increase) {
        this.increase = increase;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getIsLongPrice() {
        return isLongPrice;
    }

    public void setIsLongPrice(int isLongPrice) {
        this.isLongPrice = isLongPrice;
    }

}
