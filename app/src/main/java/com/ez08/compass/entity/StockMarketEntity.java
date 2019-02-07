package com.ez08.compass.entity;

import java.io.Serializable;

/**
 * 股票信息
 * Created by Administrator on 2016/8/15.
 */
public class StockMarketEntity implements Serializable {

    private String secuname; //股票名称
    private String secucode; //股票代码
    private int state; //状态 0正常，1停牌，2退市
    private int current; //最新价格，价格*1000
    private int lastclose;  //昨收盘价格，价格*1000
    private int exp;
    private int type;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getExp() {
        return exp;
    }

    public void setExp(int exp) {
        this.exp = exp;
    }

    public String getSecuname() {
        return secuname;
    }

    public void setSecuname(String secuname) {
        this.secuname = secuname;
    }

    public String getSecucode() {
        return secucode;
    }

    public void setSecucode(String secucode) {
        this.secucode = secucode;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        this.current = current;
    }

    public int getLastclose() {
        return lastclose;
    }

    public void setLastclose(int lastclose) {
        this.lastclose = lastclose;
    }
}
