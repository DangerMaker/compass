package com.ez08.compass.entity;

import android.content.Intent;

import com.ez08.support.net.EzMessage;
import com.ez08.support.util.EzValue;
import com.ez08.tools.IntentTools;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/1/12.
 */

public class StockDrNewParser {

    public List<StockDrNewEntity> parser(Intent intent){
        List<StockDrNewEntity> list = new ArrayList<StockDrNewEntity>();
        if (intent != null) {
            EzValue value = IntentTools.safeGetEzValueFromIntent(
                    intent, "list");
            if (value != null) {
//                String aa=value.description();
                EzMessage[] msges = value.getMessages();
                for(int i=0;i<msges.length;i++){
                    int date=msges[i].getKVData("date").getInt32();
                    long dr=msges[i].getKVData("dr").getInt64();
                    long tq=msges[i].getKVData("tq").getInt64();
                    StockDrNewEntity entity=new StockDrNewEntity();
                    entity.setDate(date);
                    entity.setTq(tq);
                    entity.setDr(dr);
                    list.add(entity);
                }
            }
        }
        return list;
    }
}
