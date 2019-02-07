package com.ez08.compass.entity;

import java.io.Serializable;

/**
 * 板块信息
 * Created by Administrator on 2016/8/15.
 */
public class PlateMarketEntity implements Serializable {

    private String boardcode; //板块id
    private String boardname; //板块名称
    private int current; //最新价格，价格*1000
    private int lastclose;  //昨收盘价格，价格*1000
    private int hsrate;  //换手率，*10000
    private String firststockcode;  //首股代码，为空表示没有
    private String firststockname;  //
    private int firststockzf;  //首股涨跌幅，*10000
    private int exp;

    public int getExp() {
        return exp;
    }

    public void setExp(int exp) {
        this.exp = exp;
    }

    public String getBoardcode() {
        return boardcode;
    }

    public void setBoardcode(String boardcode) {
        this.boardcode = boardcode;
    }

    public String getBoardname() {
        return boardname;
    }

    public void setBoardname(String boardname) {
        this.boardname = boardname;
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

    public int getHsrate() {
        return hsrate;
    }

    public void setHsrate(int hsrate) {
        this.hsrate = hsrate;
    }

    public String getFirststockcode() {
        return firststockcode;
    }

    public void setFirststockcode(String firststockcode) {
        this.firststockcode = firststockcode;
    }

    public String getFirststockname() {
        return firststockname;
    }

    public void setFirststockname(String firststockname) {
        this.firststockname = firststockname;
    }

    public int getFirststockzf() {
        return firststockzf;
    }

    public void setFirststockzf(int firststockzf) {
        this.firststockzf = firststockzf;
    }
}
