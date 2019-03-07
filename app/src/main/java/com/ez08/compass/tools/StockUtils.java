package com.ez08.compass.tools;

import android.text.TextUtils;
import android.util.Log;

import com.ez08.compass.entity.FenShiDesEntity;
import com.ez08.compass.entity.FenShiHistoryEntity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Administrator on 2017/7/5.
 */

public class StockUtils {

    public static String getStockCode(String code) {
        if (TextUtils.isEmpty(code)) {
            return "";
        }

        String resultCode = "";
        String lCode = code.toLowerCase();
        if (lCode.startsWith("shhq")) {
            resultCode = lCode.toUpperCase();
        } else if (lCode.startsWith("szhq")) {
            resultCode = lCode.toUpperCase();
        } else if (lCode.startsWith("br01")) {
            resultCode = lCode.toUpperCase();
        } else if (lCode.startsWith("sh")) {
            resultCode = lCode.replace("sh", "SHHQ");
        } else if (lCode.startsWith("sz")) {
            resultCode = lCode.replace("sz", "SZHQ");
        } else {
            resultCode = code.toUpperCase();
        }
        return resultCode;
    }

    public static String cutStockCode(String code) {
        if (TextUtils.isEmpty(code)) {
            return "";
        }

        String resultCode;
        String lCode = code.toLowerCase();
        if (lCode.startsWith("shhq")) {
            resultCode = lCode.replace("shhq", "");
        } else if (lCode.startsWith("szhq")) {
            resultCode = lCode.replace("szhq", "");
        } else if (lCode.startsWith("br01")) {
            resultCode = lCode.replace("br01", "");
        } else if (lCode.startsWith("sh")) {
            resultCode = lCode.replace("sh", "");
        } else if (lCode.startsWith("sz")) {
            resultCode = lCode.replace("sz", "");
        } else {
            resultCode = code.toUpperCase();
        }

        resultCode = resultCode.toUpperCase();
        return resultCode;
    }

    public static List<Float> computeAverageLine(String code, List<Float> tempFenshiList, List<FenShiDesEntity> tempEntityList) {
        if (DDSID.isZ(code)) {
            return drawIndexAvg(tempFenshiList, tempEntityList);
        } else {
            return drawNonIndexAvg(tempFenshiList, tempEntityList);
        }
    }

    private static List<Float> drawIndexAvg(List<Float> tempFenshiList, List<FenShiDesEntity> tempEntityList) {
        List<Float> list = new ArrayList<Float>();
        if(tempEntityList == null || tempFenshiList == null){
            return list;
        }

        if(tempFenshiList.isEmpty() || tempEntityList.isEmpty()){
            return list;
        }

        list.add(tempFenshiList.get(0));
        for (int i = 1; i < tempFenshiList.size(); i++) {
            // 当前价
            float curPrice = tempFenshiList.get(i);
            // 当前交易量
            long curVolume = (long) tempEntityList.get(i).getAcolumn();
            // 上一个的交易量
            long frontVolume = (long) tempEntityList.get(i - 1)
                    .getAcolumn();
            long rVolume = curVolume - frontVolume;
            float result = ((curPrice * rVolume + frontVolume
                    * list.get(i - 1)) / curVolume);
            list.add(result);
        }
        return list;
    }

    private static List<Float> drawNonIndexAvg(List<Float> tempFenshiList, List<FenShiDesEntity> tempEntityList) {
        List<Float> list = new ArrayList<Float>();
        if(tempEntityList == null || tempFenshiList == null){
            return list;
        }

        if(tempFenshiList.isEmpty() || tempEntityList.isEmpty()){
            return list;
        }
        list.add(tempFenshiList.get(0));
        for (int i = 1; i < tempEntityList.size(); i++) {
            float volune = (float) tempEntityList.get(i).getAcolumn();
            float amount = (float) tempEntityList.get(i).getCount();
            float result = (amount / volune);
            list.add(result);
        }

        return list;
    }
}
