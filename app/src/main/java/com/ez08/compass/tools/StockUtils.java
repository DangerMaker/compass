package com.ez08.compass.tools;

import android.text.TextUtils;

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
            resultCode = lCode.replace("shhq","");
        } else if (lCode.startsWith("szhq")) {
            resultCode = lCode.replace("szhq","");
        } else if (lCode.startsWith("br01")) {
            resultCode = lCode.replace("br01","");
        } else if (lCode.startsWith("sh")) {
            resultCode = lCode.replace("sh","");
        } else if (lCode.startsWith("sz")) {
            resultCode = lCode.replace("sz","");
        } else {
            resultCode = code.toUpperCase();
        }

        resultCode = resultCode.toUpperCase();
        return resultCode;
    }
}
