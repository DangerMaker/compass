package com.ez08.compass.tools;

/**
 * Created by Administrator on 2017/7/5.
 */

public class StockUtils {

    public static String getStockCode(String code){
        String resultCode="";
        String lCode=code.toLowerCase();
        if(lCode.startsWith("shhq")){
            resultCode=lCode.toUpperCase();
        }else if(lCode.startsWith("szhq")){
            resultCode=lCode.toUpperCase();
        }else if(lCode.startsWith("br01")){
            resultCode=lCode.toUpperCase();
        }else if(lCode.startsWith("sh")){
            resultCode=lCode.replace("sh","SHHQ");
        }else if(lCode.startsWith("sz")){
            resultCode=lCode.replace("sz","SZHQ");
        }else {
            resultCode=code.toUpperCase();
        }
        return resultCode;
    }
}
