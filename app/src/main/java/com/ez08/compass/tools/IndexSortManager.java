package com.ez08.compass.tools;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.ez08.compass.entity.IndexCurvesItemEntity;
import com.ez08.compass.userauth.AuthUserInfo;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Administrator on 2018/9/17.
 */

public class IndexSortManager {

    public static volatile List<String> indexList = new ArrayList<>();

    public static void init(Context context) {
            synchronized (indexList) {
                indexList.clear();
                indexList.add("主力资金");
                indexList.add("敢死队资金");
                indexList.add("多空资金");
                indexList.add("股价活跃度");
                indexList.add("成交量");
                indexList.add("MACD");
                indexList.add("KDJ");
                indexList.add("RSI");
                indexList.add("BIAS");
                indexList.add("CCI");
                indexList.add("ROC");
                indexList.add("ASI");
                indexList.add("PSY");
        }
    }

    public static void alter(Context context, List<IndexCurvesItemEntity> list) {
        List<String> tempList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            tempList.add(list.get(i).getName());
        }

        SharedPreferences mPreferences = AppUtils.getIndexSharedPrefCerences(context);
        Gson gson = new Gson();
        String jsonStr = gson.toJson(tempList);
        mPreferences.edit().putString("indexSort1" + AuthUserInfo.getMyCid(), jsonStr).apply();
        synchronized (indexList) {
            indexList.clear();
            indexList.addAll(tempList);
        }
    }

    public static List<IndexCurvesItemEntity> getAllList() {
        return toResult(indexList);
    }

    public static void removeCapital(List<String> list, String szCode) {
        if (TextUtils.isEmpty(szCode)) {
            return;
        }
        if (!DDSID.isExistsCapital(szCode)) {
            if (list.contains("主力资金")) {
                list.remove("主力资金");
            }
            if (list.contains("敢死队资金")) {
                list.remove("敢死队资金");
            }
            if (list.contains("多空资金")) {
                list.remove("多空资金");
            }
            if (list.contains("股价活跃度")) {
                list.remove("股价活跃度");
            }
        }

    }

    //0 day 1 week 2 month else min
    public static List<IndexCurvesItemEntity> getStockList(int type, String szCode) {
        List<String> temp = new ArrayList<>();
        //day
        synchronized (indexList) {
            if (type == 0) {
                temp.addAll(indexList);
                removeCapital(temp, szCode);
                return toResult(temp);
            } else if (type == 1) { //week
                temp.addAll(indexList);
                removeCapital(temp, szCode);
                if (temp.contains("股价活跃度")) {
                    temp.remove("股价活跃度");
                }
                return toResult(temp);
            } else if (type == 2) { //month;
                temp.addAll(indexList);
                removeCapital(temp, szCode);
                if (temp.contains("股价活跃度")) {
                    temp.remove("股价活跃度");
                }
                return toResult(temp);
            } else {
                temp.addAll(indexList);
                if (temp.contains("主力资金")) {
                    temp.remove("主力资金");
                }
                if (temp.contains("敢死队资金")) {
                    temp.remove("敢死队资金");
                }
                if (temp.contains("多空资金")) {
                    temp.remove("多空资金");
                }
                if (temp.contains("股价活跃度")) {
                    temp.remove("股价活跃度");
                }
                return toResultMin(temp);
            }
        }

    }

    public static String currentIndex = "";
    public static String currentIndexMin = "";

    public static List<IndexCurvesItemEntity> toResult(List<String> list) {

        if (TextUtils.isEmpty(currentIndex)) {
            currentIndex = list.get(0);
        }

        if (!list.contains(currentIndex)) {
            currentIndex = list.get(0);
        }

        List<IndexCurvesItemEntity> entities = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).equals(currentIndex)) {
                entities.add(new IndexCurvesItemEntity(list.get(i), true));
            } else {
                entities.add(new IndexCurvesItemEntity(list.get(i), false));
            }
        }
        return entities;
    }

    public static List<IndexCurvesItemEntity> toResultMin(List<String> list) {

        if (TextUtils.isEmpty(currentIndexMin)) {
            currentIndexMin = list.get(0);
        }

        if (!list.contains(currentIndexMin)) {
            currentIndexMin = list.get(0);
        }

        List<IndexCurvesItemEntity> entities = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).equals(currentIndexMin)) {
                entities.add(new IndexCurvesItemEntity(list.get(i), true));
            } else {
                entities.add(new IndexCurvesItemEntity(list.get(i), false));
            }
        }
        return entities;
    }
}
