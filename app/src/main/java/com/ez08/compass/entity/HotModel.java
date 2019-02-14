package com.ez08.compass.entity;

import android.support.annotation.NonNull;

import java.text.Collator;
import java.util.Comparator;

public  class HotModel implements Comparable<HotModel> {
    private final static Comparator<Object> CHINA_COMPARE = Collator.getInstance(java.util.Locale.CHINA);

    public String name;
    public boolean isUseful = false;  //-2 没有计算值 0 非热点 1热点
    public int hotValue = 0; //天数
    public double MostValue = 0; //name 下的股票热度值

    @Override
    public int compareTo(@NonNull HotModel hotModel) {
        if (Math.abs(this.hotValue) > Math.abs(hotModel.hotValue)) {
            return 1;
        } else if (Math.abs(this.hotValue) < Math.abs(hotModel.hotValue)) {
            return -1;
        } else {
            if (Math.abs(this.MostValue) > Math.abs(hotModel.MostValue)) {
                return -1;
            } else if (this.MostValue < hotModel.MostValue) {
                return 1;
            } else {
                return CHINA_COMPARE.compare(name, hotModel.name);
            }
        }

    }
}