package com.ez08.compass.tools;

/**
 *  根据用户level判断显示哪些内容
 *  改版后只分免费和付费
 */

public class AuthTool {
    //免费普通 0 免费实验 1 付费普通 2 付费实验 3
    public static int authType = -1;

    public static void initType(int level,String auths){
//        if(level < 0){
//            if(auths.contains("5633")){
//                authType = 1;
//            }else{
//                authType = 0;
//            }
//        }else if(level == 0 && auths.contains("5634")){
//                authType = 3;
//        }else {
//            authType = 2;
//        }
        if(level < 0){
            authType = 1;
        }else{
            authType = 3;
        }
    }

    //盯盘五日以上
    public static boolean dingpanFiveData(){
        if(authType == 2 || authType == 3){
            return true;
        }else{
            return false;
        }
    }

    //盯盘底部广告位
    public static boolean dingpanBottomAd(){
        if(authType == 0 || authType == 1){
            return true;
        }else{
            return false;
        }
    }

    //诊股堂
    public static boolean zhengutang(){
        if(authType == 1){
            return true;
        }else{
            return false;
        }
    }

    //操盘计划
    public static boolean caopanPlan(){
        if(authType == 1){
            return true;
        }else{
            return false;
        }
    }

    //特色功能
    public static boolean feature(){
        if(authType == 1){
            return true;
        }else{
            return false;
        }
    }

    //下载PC
    public static boolean downloadPC(){
        if(authType == 0 || authType == 1){
            return true;
        }else{
            return false;
        }
    }

    //敢死队 多空 长线 股价活跃
    public static boolean needUnlock(){
        if(authType == 0){
            return false;
        }else{
            return true;
        }
    }
}
