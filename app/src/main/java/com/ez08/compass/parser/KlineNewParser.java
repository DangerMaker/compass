package com.ez08.compass.parser;

import android.content.Intent;
import android.util.Log;

import com.ez08.compass.CompassApp;
import com.ez08.compass.entity.CxjcEntity;
import com.ez08.compass.entity.HydEntity;
import com.ez08.compass.entity.KChartEntity;
import com.ez08.compass.entity.StockDrNewEntity;
import com.ez08.compass.tools.UtilTools;
import com.ez08.support.net.EzMessage;
import com.ez08.support.util.EzValue;
import com.ez08.tools.IntentTools;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/1/16.
 */

public class KlineNewParser {
    public List<KChartEntity> parse(Intent kLineIntent, List<StockDrNewEntity> drList, int DECM) {

        List<KChartEntity> swhList = new ArrayList<>();
        if (kLineIntent != null) {
            EzValue kline = IntentTools.safeGetEzValueFromIntent(
                    kLineIntent, "list");
            if (kline != null) {
                String bc = kline.description();
                Log.e("KChart",bc);
                EzMessage[] msges = kline.getMessages();
                if (msges != null) {
                    for (int i = msges.length - 1; i >= 0; i--) {
                        EzMessage msge = msges[i];
                        int date = msge.getKVData("date").getInt32();
                        int time = msge.getKVData("time").getInt32();
                        int open = msge.getKVData("open").getInt32();
                        int close = msge.getKVData("close").getInt32();
                        int high = msge.getKVData("high").getInt32();
                        int low = msge.getKVData("low").getInt32();
                        long volume = msge.getKVData("volume").getInt64();
                        long amount = msge.getKVData("amount").getInt64();
                        float fdr = 1.0f;
                        if(drList != null && !drList.isEmpty()) {
                            for (int drindex = drList.size() - 1; drindex >= 0; drindex--) {
                                if (drList.get(drindex).getDate() > date) {
                                    fdr = fdr * (drList.get(drindex).getDr() / 10000.0f);
                                }
                            }
                        }

                        KChartEntity entity = new KChartEntity();
                        entity.setAmount(amount / 10000f);
                        entity.setVolume(volume / 10000f);
                        entity.setlTime((long) date);
                        entity.setMinute((long) time);
                        entity.setTime(UtilTools.getStime((long) date));
                        entity.openValue = UtilTools.getPriceInt2Float(open,DECM);
                        entity.closeValue = UtilTools.getPriceInt2Float(close,DECM);
                        entity.highValue = UtilTools.getPriceInt2Float(high,DECM);
                        entity.lowValue = UtilTools.getPriceInt2Float(low,DECM);
                        entity.setDr(fdr);
                        if(CompassApp.GLOBAL.isDr){
                            entity.turnDr();
                        }else{
                            entity.turnNormal();
                        }
                        swhList.add(entity);
                    }

                }
            }
        }
        return swhList;
    }


    public List<HydEntity> parseHyd(Intent kLineIntent) {
        List<HydEntity> swhList = new ArrayList<>();
        if (kLineIntent != null) {
            EzValue kline = IntentTools.safeGetEzValueFromIntent(
                    kLineIntent, "list");
            if (kline != null) {
                String bc = kline.description();
                EzMessage[] msges = kline.getMessages();
                if (msges != null) {
                    for (int i = msges.length - 1; i >= 0; i--) {
                        EzMessage msge = msges[i];
                        int date = msge.getKVData("date").getInt32();
                        int time = msge.getKVData("time").getInt32();
                        int d13 = msge.getKVData("dhyd13").getInt32();
                        int d34 = msge.getKVData("dhyd34").getInt32();

                        HydEntity entity = new HydEntity();
                        entity.date = date;
                        entity.time = time;
                        entity.dhyd13 = d13 / 1000f;
                        entity.dhyd34 = d34 / 1000f;
                        swhList.add(entity);
                    }
                }
            }
        }
        return swhList;
    }

    public List<CxjcEntity> parseCxjc(Intent kLineIntent) {
        List<CxjcEntity> swhList = new ArrayList<>();
        if (kLineIntent != null) {
            EzValue kline = IntentTools.safeGetEzValueFromIntent(
                    kLineIntent, "list");
            if (kline != null) {
                String bc = kline.description();
                EzMessage[] msges = kline.getMessages();
                if (msges != null) {
                    for (int i = msges.length - 1; i >= 0; i--) {
                        EzMessage msge = msges[i];
                        int date = msge.getKVData("date").getInt32();
                        int time = msge.getKVData("time").getInt32();
                        int cw = msge.getKVData("cw").getInt32();

                        CxjcEntity entity = new CxjcEntity();
                        entity.date = date;
                        entity.time = time;
                        entity.cw = cw;
                        swhList.add(entity);
                    }
                }
            }
        }
        return swhList;
    }
}
