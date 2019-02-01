package com.ez08.compass.entity;

/**
 * Created by dingwei on 16/7/6.
 */
public class BaseCapitalEntity {
    private int date;//日期
    private int zhu;//主力资金
    private int gan;//敢死队资金
    private int duo;//多空资金

    public int getDate() {
        return date;
    }

    public void setDate(int date) {
        this.date = date;
    }

    public int getZhu() {
        return zhu;
    }

    public void setZhu(int zhu) {
        this.zhu = zhu;
    }

    public int getGan() {
        return gan;
    }

    public void setGan(int gan) {
        this.gan = gan;
    }

    public int getDuo() {
        return duo;
    }

    public void setDuo(int duo) {
        this.duo = duo;
    }
}
