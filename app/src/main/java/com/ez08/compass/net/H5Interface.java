package com.ez08.compass.net;

import android.content.Context;
import android.util.Log;

import com.ez08.compass.autoupdate.tools.AutoPackageUtility;
import com.ez08.compass.tools.UtilTools;
import com.ez08.compass.userauth.AuthUserInfo;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by Administrator on 2018/8/10.
 */

public class H5Interface {
//    "http://cweb.compass.cn/disp_app/?company=znz&personid=xxx&channel=xxx&softver=xxx&func=gsdzj&action=xxx";
    public static String HOST = "http://cweb.compass.cn/disp_app/";
    public static String COMPANY = "company";
    public static String PERSONID = "personid";
    public static String CHANNEL = "channel";
    public static String FUNC = "func";
    public static String ACTION = "action";
    public static String VERSION = "softver";

    public static String COMPANY_DEAFAULT = "znz";
    public static String ACTION_INTRO = "func_intro";
    public static String ACTION_NOAUTH = "func_noauth";
    public static String ACTION_OUTLIMIT = "func_outlimit";

    public static class GanSiDui{
        public static String FUNC_GSDZJ = "gsdzj";

        public static String intro(Context context){
            Map<String,String> map = baseMap(context);
            map.put(FUNC,FUNC_GSDZJ);
            map.put(ACTION,ACTION_INTRO);
            return getRqstUrl(HOST,map);
        }

        public static String noauth(Context context){
            Map<String,String> map = baseMap(context);
            map.put(FUNC,FUNC_GSDZJ);
            map.put(ACTION,ACTION_NOAUTH);
            return getRqstUrl(HOST,map);
        }

        public static String outlimit(Context context){
            Map<String,String> map = baseMap(context);
            map.put(FUNC,FUNC_GSDZJ);
            map.put(ACTION,ACTION_OUTLIMIT);
            return getRqstUrl(HOST,map);
        }
    }

    public static class DuoKong{
        public static String FUNC_DKZJ = "dkzj";

        public static String intro(Context context){
            Map<String,String> map = baseMap(context);
            map.put(FUNC,FUNC_DKZJ);
            map.put(ACTION,ACTION_INTRO);
            return getRqstUrl(HOST,map);
        }

        public static String noauth(Context context){
            Map<String,String> map = baseMap(context);
            map.put(FUNC,FUNC_DKZJ);
            map.put(ACTION,ACTION_NOAUTH);
            return getRqstUrl(HOST,map);
        }

        public static String outlimit(Context context){
            Map<String,String> map = baseMap(context);
            map.put(FUNC,FUNC_DKZJ);
            map.put(ACTION,ACTION_OUTLIMIT);
            return getRqstUrl(HOST,map);
        }
    }

    public static class Feature{
        public static String FUNC_TSGN = "tsgn";

        public static String intro(Context context){
            Map<String,String> map = baseMap(context);
            map.put(FUNC,FUNC_TSGN);
            map.put(ACTION,ACTION_INTRO);
            return getRqstUrl(HOST,map);
        }
    }

    public static class Download{
        public static String FUNC_XZPC= "xzpc";

        public static String intro(Context context){
            Map<String,String> map = baseMap(context);
            map.put(FUNC,FUNC_XZPC);
            map.put(ACTION,ACTION_INTRO);
            return getRqstUrl(HOST,map);
        }
    }

    public static class DHYD{
        public static String FUNC_DHYD = "gjhyd";

        public static String intro(Context context){
            Map<String,String> map = baseMap(context);
            map.put(FUNC,FUNC_DHYD);
            map.put(ACTION,ACTION_INTRO);
            return getRqstUrl(HOST,map);
        }

        public static String outlimite(Context context){
            Map<String,String> map = baseMap(context);
            map.put(FUNC,FUNC_DHYD);
            map.put(ACTION,ACTION_OUTLIMIT);
            return getRqstUrl(HOST,map);
        }
    }

    public static class DKJC{
        public static String FUNC_KJC = "cxjc";

        public static String intro(Context context){
            Map<String,String> map = baseMap(context);
            map.put(FUNC,FUNC_KJC);
            map.put(ACTION,ACTION_INTRO);
            return getRqstUrl(HOST,map);
        }

        public static String outlimite(Context context){
            Map<String,String> map = baseMap(context);
            map.put(FUNC,FUNC_KJC);
            map.put(ACTION,ACTION_OUTLIMIT);
            return getRqstUrl(HOST,map);
        }
    }

    public static class QSHJ{
        public static String FUNC_QSHJ = "qshj";

        public static String intro(Context context){
            Map<String,String> map = baseMap(context);
            map.put(FUNC,FUNC_QSHJ);
            map.put(ACTION,ACTION_INTRO);
            return getRqstUrl(HOST,map);
        }
    }

    public static class TSDP{
        public static String FUNC_TSDP = "tsdp";

        public static String intro(Context context){
            Map<String,String> map = baseMap(context);
            map.put(FUNC,FUNC_TSDP);
            map.put(ACTION,ACTION_OUTLIMIT);
            return getRqstUrl(HOST,map);
        }
    }

    public static Map<String,String> baseMap(Context context){
        Map<String,String> map = new LinkedHashMap<>();
        map.put(COMPANY,COMPANY_DEAFAULT);
        map.put(PERSONID, AuthUserInfo.getMyCid());
        map.put(CHANNEL, UtilTools.getChannel(context));
        try {
            map.put(VERSION, AutoPackageUtility.getVersionName() );
        } catch (Exception e) {
            e.printStackTrace();
            map.put(VERSION, "1.0");
        }
        return map;
    }

    public static String getRqstUrl(String url, Map<String, String> params) {
        StringBuilder builder = new StringBuilder(url);
        boolean isFirst = true;
        for (String key : params.keySet()) {
            if (key != null && params.get(key) != null) {
                if (isFirst) {
                    isFirst = false;
                    builder.append("?");
                } else {
                    builder.append("&");
                }
                builder.append(key)
                        .append("=")
                        .append(params.get(key));
            }
        }
        String result = builder.toString();
        Log.e("getRqstUrl",result);
        return result;
    }
}
