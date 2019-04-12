package com.ez08.compass.tools;

import android.text.TextUtils;
import android.util.Log;

import com.ez08.compass.entity.ColumnValuesDataModel;
import com.ez08.compass.entity.FenShiDesEntity;
import com.ez08.compass.entity.FenShiHistoryEntity;
import com.ez08.compass.entity.KChartEntity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Administrator on 2017/7/5.
 */

public class StockUtils {

    public static final String MA5102060 = "<font color='#FFA500'>MA5&thinsp </font> <font color='#FF00FF'>10&thinsp</font> <font color='#006400'>20&thinsp</font> <font color='#0000FF'>60&thinsp</font>";

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

    public static String cutShortStockCode(String code) {
        if (TextUtils.isEmpty(code)) {
            return "";
        }

        String resultCode = "";
        String lCode = code.toLowerCase();
        if (lCode.startsWith("shhq")) {
            resultCode = lCode.replace("shhq","sh");
        } else if (lCode.startsWith("szhq")) {
            resultCode = lCode.replace("szhq","sz");
        } else if (lCode.startsWith("br01")) {
            resultCode = lCode.toUpperCase();
        } else if (lCode.startsWith("sh")) {
            resultCode = lCode;
        } else if (lCode.startsWith("sz")) {
            resultCode = lCode;
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

    /**
     * 分时图计算平均线
     *
     * @param code
     * @param tempFenshiList
     * @param tempEntityList
     * @return
     */
    public static List<Float> computeAverageLine(String code, List<Float> tempFenshiList, List<FenShiDesEntity> tempEntityList) {
        if (DDSID.isZ(code)) {
            return drawIndexAvg(tempFenshiList, tempEntityList);
        } else {
            return drawNonIndexAvg(tempFenshiList, tempEntityList);
        }
    }

    private static List<Float> drawIndexAvg(List<Float> tempFenshiList, List<FenShiDesEntity> tempEntityList) {
        List<Float> list = new ArrayList<Float>();
        if (tempEntityList == null || tempFenshiList == null) {
            return list;
        }

        if (tempFenshiList.isEmpty() || tempEntityList.isEmpty()) {
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
        if (tempEntityList == null || tempFenshiList == null) {
            return list;
        }

        if (tempFenshiList.isEmpty() || tempEntityList.isEmpty()) {
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

    public static List<Float> getMaListByNum(int num, List<KChartEntity> mTotalList) {
        List<Float> maList = new ArrayList<>();
        for (int i = 0; i < mTotalList.size(); i++) {
            if (i < num) {
                if (i == 0) {
                    float temp = mTotalList.get(0).getClose();
                    maList.add(temp);
                } else {
                    float tempTotal = 0;
                    for (int j = 0; j < i + 1; j++) {
                        tempTotal = tempTotal + mTotalList.get(j).getClose();
                    }
                    float temp = tempTotal / (i + 1);
                    maList.add(temp);
                }
            } else {
                float tempTotal = 0;
                for (int j = i - num + 1; j < i + 1; j++) {
                    tempTotal = tempTotal + mTotalList.get(j).getClose();
                }
                float temp = tempTotal / num;
                maList.add(temp);
            }
        }

        return maList;
    }

    public static List<Float> getVolMaListByNum(int num, List<ColumnValuesDataModel> column) {
        List<Float> volList = new ArrayList<>();
        for (int i = 0; i < column.size(); i++) {
            if (i < num) {
                if (i == 0) {
                    float temp = column.get(0).getValue();
                    volList.add(temp);
                } else {
                    float tempTotal = 0;
                    for (int j = 0; j < i + 1; j++) {
                        tempTotal = tempTotal + column.get(j).getValue();
                    }
                    float temp = tempTotal / (i + 1);
                    volList.add(temp);
                }
            } else {
                float tempTotal = 0;
                for (int j = i - num + 1; j < i + 1; j++) {
                    tempTotal = tempTotal + column.get(j).getValue();
                }
                float temp = tempTotal / num;
                volList.add(temp);
            }
        }
        return volList;
    }

//    public static int getCodePositionInList(String code,List<String> codes){
//        int result = -1;
//        if(codes.contains(con))
//    }
}
