package com.ez08.compass.tools;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.thinkive.framework.util.ScreenUtil;
import com.ez08.compass.userauth.AuthUserInfo;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.content.Context.TELEPHONY_SERVICE;

public class UtilTools {

    public static boolean equalsMonth(String eTime1, String eTime2) {
        if (eTime1.length() != 8 || eTime2.length() != 8) {
            return false;
        }
        Calendar calendar1 = Calendar.getInstance();
        Calendar calendar2 = Calendar.getInstance();
        calendar1.set(Integer.parseInt(eTime1.substring(0, 4)), Integer.parseInt(eTime1.substring(4, 6)), Integer.parseInt(eTime1.substring(6)));
        calendar2.set(Integer.parseInt(eTime2.substring(0, 4)), Integer.parseInt(eTime2.substring(4, 6)), Integer.parseInt(eTime2.substring(6)));
        int year1 = calendar1.get(Calendar.YEAR);
        int year2 = calendar2.get(Calendar.YEAR);
        int month1 = calendar1.get(Calendar.MONTH);
        int month2 = calendar2.get(Calendar.MONTH);
        System.out.println(year1 + "  " + month1);
        System.out.println(year2 + "  " + month2);
        return calendar1.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR) && calendar1.get(Calendar.MONTH) == calendar2.get(Calendar.MONTH);

    }

    public static boolean isSameWeekWithToday(String eTime1, String eTime2) {
        if (eTime1.length() != 8 || eTime2.length() != 8) {
            return false;
        }
        // 0.先把Date类型的对象转换Calendar类型的对象
        Calendar todayCal = Calendar.getInstance();
        Calendar dateCal = Calendar.getInstance();
//        todayCal.setFirstDayOfWeek(Calendar.MONDAY);//西方周日为一周的第一天，咱得将周一设为一周第一天
//        dateCal.setFirstDayOfWeek(Calendar.MONDAY);
        todayCal.set(Integer.parseInt(eTime1.substring(0, 4)), Integer.parseInt(eTime1.substring(4, 6)), Integer.parseInt(eTime1.substring(6)));
        dateCal.set(Integer.parseInt(eTime2.substring(0, 4)), Integer.parseInt(eTime2.substring(4, 6)), Integer.parseInt(eTime2.substring(6)));
        // 1.比较当前日期在年份中的周数是否相同
        if (todayCal.get(Calendar.WEEK_OF_YEAR) == dateCal.get(Calendar.WEEK_OF_YEAR)) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isSameDate(String date1, String date2) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date d1 = null;
        Date d2 = null;
        try {
            d1 = format.parse(date1);
            d2 = format.parse(date2);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTime(d1);
        cal2.setTime(d2);
        int subYear = cal1.get(Calendar.YEAR) - cal2.get(Calendar.YEAR);
        // subYear==0,说明是同一年
        if (subYear == 0) {
            if (cal1.get(Calendar.WEEK_OF_YEAR) == cal2
                    .get(Calendar.WEEK_OF_YEAR))
                return true;
        }
        // 例子:cal1是"2005-1-1"，cal2是"2004-12-25"
        // java对"2004-12-25"处理成第52周
        // "2004-12-26"它处理成了第1周，和"2005-1-1"相同了
        // 大家可以查一下自己的日历
        // 处理的比较好
        // 说明:java的一月用"0"标识，那么12月用"11"
        else if (subYear == 1 && cal2.get(Calendar.MONTH) == 11) {
            if (cal1.get(Calendar.WEEK_OF_YEAR) == cal2
                    .get(Calendar.WEEK_OF_YEAR))
                return true;
        }
        // 例子:cal1是"2004-12-31"，cal2是"2005-1-1"
        else if (subYear == -1 && cal1.get(Calendar.MONTH) == 11) {
            if (cal1.get(Calendar.WEEK_OF_YEAR) == cal2
                    .get(Calendar.WEEK_OF_YEAR))
                return true;

        }
        return false;
    }

    //	static DecimalFormat df = new DecimalFormat("###.00");
//	static DecimalFormat df1 = new DecimalFormat("###.000"); 
    public static float getRoundingNum(float num, int type) {
        String target = "";
        float slar = 0;
        switch (type) {
            case 2:
//			target=df.format(num);
                slar = (float) ((Math.round(num * 100)) / 100.0);
                break;
            case 3:
//			target=df1.format(num);
                slar = (float) ((Math.round(num * 1000)) / 1000.0);
                break;
            default:
                break;
        }
//		float tar=0;
//		try {
//			tar=Float.parseFloat(target);
//		} catch (NumberFormatException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			tar=0;
//		}
        return slar;
    }

//    public static String getFormatNum(String num, int index) {
//
////        String result = "";
////            if (num != null) {
////                String nu = num.replace(" ","");
////                nu = nu.replace("　","");
////                float numf = Float.parseFloat(nu);
////                if (index == 2) {
////                    result = format2Num(numf);
////                } else if (index == 3) {
////                    result =  format3Num(numf);
////                }
////            }
////        return re;
//    }

    public static String getFormatNum(String num, int index) {
        if (num == null || num.length() < 1){
            return "";
        }
        String ltext[] = num.split("\\.");
        if (ltext != null && ltext.length > 1) {
            if (ltext[1].length() < index) {
                String anim = "";
                for (int i = 0; i < index - ltext[1].length(); i++) {
                    anim = anim + "0";
                }
                ltext[1] = ltext[1] + anim;

            } else if (ltext[1].length() > index) {
                ltext[1] = ltext[1].substring(0, 2);
            }
            return ltext[0] + "." + ltext[1];
        }
        return "";
    }


    public static String getFormatNum(String num, int index, boolean range) {
        String ltext[] = num.split("\\.");
        if (ltext != null && ltext.length > 1) {
            if (ltext[1].length() < index) {
                String anim = "";
                for (int i = 0; i < index - ltext[1].length(); i++) {
                    anim = anim + "0";
                }
                ltext[1] = ltext[1] + anim;

            }
            if (ltext[1].length() > index) {
                ltext[1] = ltext[1].substring(0, index);

            }
            return ltext[0] + "." + ltext[1];
        }
        return "";
    }

    public static boolean func(String str) {
        Pattern p1 = Pattern.compile("\\d{10}");
        Matcher m1 = p1.matcher(str);
        String result1 = m1.find() ? m1.group() : null;

        Pattern p2 = Pattern.compile("\\d{11}");
        Matcher m2 = p2.matcher(str);
        String result2 = m2.find() ? m2.group() : null;
        if (TextUtils.equals("15652306465", result2)
                || TextUtils.equals("13262238132", result2)) {
            result2 = null;
        }
        if (TextUtils.isEmpty(result1) && TextUtils.isEmpty(result2)) {
            return false;
        }
        return true;
    }

    public static boolean isSpecialCharer(String str) {
        int n = 0;
        int y = 0;
        for (int i = 0; i < str.length(); i++) {
            n = (int) str.charAt(i);
            /*
             * if (!(19968 <= n && n <
             * 40623)||!(48<=n&&n<=57)||!(65<=n&&n<=122)){ return true; }
             */
            if (48 <= n && n <= 57) {
                y = 0;
            } else if (65 <= n && n <= 90) {
                y = 0;
            } else if (n == 95) {
                y = 0;
            } else if (97 <= n && n <= 122) {
                y = 0;
            } else if (19968 <= n && n < 40623) {
                y = 0;
            } else {
                y = 1;
                return true;
            }
        }
        return false;
    }

    public static int dip2px(Context context, float dipValue) {
        if (context == null) {
            return 0;
        }
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    public static boolean isSpecialCharerPassword(String str) {
        int n = 0;
        for (int i = 0; i < str.length(); i++) {
            n = (int) str.charAt(i);
            if (19968 <= n && n < 40623) {
                return true;
            }
        }
        return false;
    }

    public static String getStime(Long date) {
        String sTIme = "";
        String[] lTime = (date + "")
                .split("");
        if (lTime != null && lTime.length > 8) {
            sTIme = lTime[1] + lTime[2] + lTime[3] + lTime[4] + "-"
                    + lTime[5] + lTime[6] + "-" + lTime[7] + lTime[8];
        }
        return sTIme;
    }

    public static String getPushUrlDate(Context context) {
        String content = "";
        // ---------------
        content = content + "company=compass";
        content = content + "&os=android";
        content = content + "&osver=" + android.os.Build.MODEL + ","
                + android.os.Build.VERSION.RELEASE;

        // ---------------
        if (!TextUtils.isEmpty(AuthUserInfo.getMyCid())) {
            content = content + "&personid=" + AuthUserInfo.getMyCid();
        }
        // ---------------
        content = content + "&chid=" + getChannel(context);

        // ---------------
        String version = "";
        PackageManager pm = context.getPackageManager();
        try {
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(),
                    PackageManager.GET_ACTIVITIES);
            version = pi.versionName;
            content = content + "&ver=" + version;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        // ---------------
        content = content + "&pmode=" + android.os.Build.MODEL;
        content = content.replaceAll(" ", "");
        return content;
    }

    //这个不是只有date，我被误导了
    public static String getDate(Context context) {
        String content = "";
        // ---------------
        content = content + "company=compass";
        content = content + "&os=android";
        content = content + "&osver=" + android.os.Build.MODEL + ","
                + android.os.Build.VERSION.RELEASE;

        // ---------------
        if (!TextUtils.isEmpty(AuthUserInfo.getMyCid())) {
            content = content + "&personid=" + AuthUserInfo.getMyCid();
        }
        // ---------------
        if (!TextUtils.isEmpty(AuthUserInfo.getMyToken())) {
            content = content + "&skey=" + AuthUserInfo.getMyToken();
        }
        // ---------------
        if (!TextUtils.isEmpty(AuthUserInfo.getMyTid())) {
            content = content + "&aid=" + AuthUserInfo.getMyTid();
        }
        // ---------------
        content = content + "&chid=" + getChannel(context);

        // ---------------
        String version = "";
        PackageManager pm = context.getPackageManager();
        try {
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(),
                    PackageManager.GET_ACTIVITIES);
            version = pi.versionName;
            content = content + "&ver=" + version;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        // ---------------
        content = content + "&pmode=" + android.os.Build.MODEL;
        try {
            String imei = ((TelephonyManager) context.getSystemService(TELEPHONY_SERVICE)).getDeviceId();

            if (!TextUtils.isEmpty(imei)) {
                content = content + "&imei=" + imei;
            }
        } catch (Exception e) {
            Log.e("", "");
        }
        content = content.replaceAll(" ", "");
        return content;
    }

    public static String getChannel(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                "chid", Activity.MODE_PRIVATE);
        String chid = sharedPreferences.getString("chid", "");
        if (TextUtils.isEmpty(chid)) {
//            chid="30120";
            chid = getChannelFromApp(context);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            // 用putString的方法保存数据
            editor.putString("chid", chid);
            // 提交当前数据
            editor.commit();
        }
        return chid;
    }

    private static String getChannelFromApp(Context context) {
        try {
            PackageManager pm = context.getPackageManager();
            ApplicationInfo appInfo = pm.getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            String result = appInfo.metaData.getString("UMENG_CHANNEL");
            if (result.contains("compass")) {
                result = result.replace("compass", "");
            }
            return result;
//            return appInfo.metaData.getString("UMENG_CHANNEL");
        } catch (PackageManager.NameNotFoundException ignored) {
        }
        return "";
    }

    //将int的时间转化为显示时间
    public static String getMiniuteTime(int time) {
        if (time == 0) {
            return "--";
        }
        String miniute = "";
        String lTime = time > 99999 ? time + "" : "0" + time;
        miniute = lTime.substring(0, 2) + ":" + lTime.substring(2, 4) + ":" + lTime.substring(4, 6);
        return miniute;
    }


    public static int getStockType(int stockType) {
        if ((stockType >= 200 && stockType <= 203) || (stockType >= 400 && stockType <= 403) || stockType == 600) {   //指数
            return 3;
        }
        return 0;
    }

    /*
    decm=5价格除以1000，code为SH9开头保留三位小数
     */
    public static String getDecmPrice(double target, int decm, String code) {
        double lStager = (double) target;
//        double value = getDecmValue(lStager, decm, code);
        float value = getPriceInt2Float((int) target, decm);
        String result = "";
        if (decm == 5) {
            result = getFormatNum(value + "", 3);
        } else {
            result = getFormatNum(value + "", 2);
        }
        return result;
    }

    public static String getDecmPrice1(int target, int decm, String code) {
        return getPriceInt2Float(target, decm) + "";
    }

    public static float getPriceInt2Float(int price, int exp) {
        switch (exp) {
            case 0:
                return (float) (price / 0.01);
            case 1:
                return (float) (price / 0.1);
            case 2:
                return (float) (price / 1.0);
            case 3:
                return (float) (price / 10.0);
            case 4:
                return (float) (price / 100.0);
            case 5:
                return (float) (price / 1000.0);
            case 6:
                return (float) (price / 10000.0);
            case 7:
                return (float) (price / 100000.0);
            default:
                return 0;
        }
    }


    /*
    decm=5价格除以1000，code为SH9开头保留三位小数
     */
    public static double getDecmValue(double target, int decm, String code) {
        double value = 0;
        if (decm == 5) {
            value = target / 1000;
        } else {
            value = target / 100;
        }
        if (decm == 4) {
            value = getRoundingNum(value, 2);
        } else {
            value = getRoundingNum(value, 3);
        }
//        if (code.toUpperCase().contains("SH9") || code.toUpperCase().contains("SHHQ9")) {
//            value = getRoundingNum(value, 3);
//        } else {
//            value = getRoundingNum(value, 2);
//        }
        return value;
    }

    private static double getRoundingNum(double num, int type) {
        String target = "";
        double slar = 0;
        switch (type) {
            case 2:
                slar = (double) ((Math.round(num * 100)) / 100.0);
                break;
            case 3:
                slar = (double) ((Math.round(num * 1000)) / 1000.0);
                break;
            default:
                break;
        }
        return slar;
    }

    public static File buildFile(File dir, String fileName) {
        StringBuilder fileNameBuilder = new StringBuilder();
        fileNameBuilder.append(dir.getPath());
        fileNameBuilder.append(File.separator);
        fileNameBuilder.append(fileName);
        Log.e("fileName", fileNameBuilder.toString());
        return new File(fileNameBuilder.toString());
    }

    public static Bitmap getLoacalBitmap(File tempFile) {
        try {
            FileInputStream fis = new FileInputStream(tempFile);
            return BitmapFactory.decodeStream(fis);  ///把流转化为Bitmap图片

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    /*
    获得资产目录下的文件
     */
    public static String getFromAssets(Context context, String fileName) {
        try {
            InputStreamReader inputReader = new InputStreamReader(context.getResources().getAssets().open(fileName));
            BufferedReader bufReader = new BufferedReader(inputReader);
            String line = "";
            String Result = "";
            while ((line = bufReader.readLine()) != null)
                Result += line;
            return Result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static boolean isInSameWeek(String date1, String date2) {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        Date d1 = null;
        Date d2 = null;
        try {
            d1 = format.parse(date1);
            d2 = format.parse(date2);
        } catch (Exception e) {
            e.printStackTrace();
//            return true;
        }
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setFirstDayOfWeek(Calendar.MONDAY);//西方周日为一周的第一天，咱得将周一设为一周第一天
        cal2.setFirstDayOfWeek(Calendar.MONDAY);
        cal1.setTime(d1);
        cal2.setTime(d2);
        int subYear = cal1.get(Calendar.YEAR) - cal2.get(Calendar.YEAR);
        if (subYear == 0)// subYear==0,说明是同一年
        {
            if (cal1.get(Calendar.WEEK_OF_YEAR) == cal2.get(Calendar.WEEK_OF_YEAR))
                return true;
        } else if (subYear == 1 && cal2.get(Calendar.MONTH) == 11) //subYear==1,说明cal比cal2大一年;java的一月用"0"标识，那么12月用"11"
        {
            if (cal1.get(Calendar.WEEK_OF_YEAR) == cal2.get(Calendar.WEEK_OF_YEAR))
                return true;
        } else if (subYear == -1 && cal1.get(Calendar.MONTH) == 11)//subYear==-1,说明cal比cal2小一年
        {
            if (cal1.get(Calendar.WEEK_OF_YEAR) == cal2.get(Calendar.WEEK_OF_YEAR))
                return true;
        }
        return false;
    }

    public static String formatDouble(double num) {
        String end;
        double abs_profit = Math.abs(num);
        if (abs_profit > 100000000) {
            end = "亿";
            num = num / 100000000D;
        } else if (abs_profit > 10000) {
            end = "万";
            num = num / 10000D;
        } else {
            end = "万";
            num = num / 10000D;
        }

        if (num == 0) {
            return 0 + "万";
        }

        DecimalFormat df1 = new DecimalFormat("0.00");
        String result = df1.format(num) + end;
        return result;
    }

    // value num 进位
    public static String format3Num(float value) {
        DecimalFormat df1 = new DecimalFormat("0.000");
        String result = df1.format(value);
        return result;
    }

    // value num 进位
    public static String format2Num(float value) {
        DecimalFormat df1 = new DecimalFormat("0.00");
        String result = df1.format(value);
        return result;
    }

    //获得转换后的数据
    //type:0,String 1,double,2 int,3 long
    //decimal:保留几位小数
    public static String getTransFormNum(String target, int type, int decimal) {
        if (TextUtils.isEmpty(target) || target.equals("∞")) {
            return "";
        }
        String result = "";
        String unit = "";
        switch (type) {
            case 0:

                break;
            case 1:
                double tarD = Double.parseDouble(target);
                String end;
                double net_profit = tarD;
                double abs_profit = Math.abs(tarD);

                if (abs_profit > 100000000) {
                    end = "亿";
                    net_profit = net_profit / 100000000D;
                } else if (abs_profit > 10000) {
                    end = "万";
                    net_profit = net_profit / 10000D;
                } else {
                    end = "";
                }

                DecimalFormat df1;
                if (abs_profit < 0.01) {
                    df1 = new DecimalFormat("0.000");
                } else {
                    df1 = new DecimalFormat("0.00");
                }

                result = df1.format(net_profit) + end;

//                double tarD = Double.parseDouble(target);
//
//                long tars = (long) tarD;
//                String tarDS = tars + "";
//                int lengths = tarDS.length();
//                if (lengths <= 4) {
//                    result = target;
//                } else if (lengths > 4 && lengths <= 8) {
//                    double df = (double) tars / 10000;
//                    String dfN = df + "";
//                    unit = "万";
//                    String tarN[] = dfN.split("\\.");
//                    String txt0, txt1;
//                    txt0 = tarN[0];
//                    if (tarN[1].length() > decimal) {
//                        txt1 = tarN[1].substring(0, decimal);
//                    } else {
//                        txt1 = tarN[1];
//                    }
//                    result = txt0 + "." + txt1 + unit;
//                } else {
//                    unit = "亿";
//                    double df = (double) tars / 100000000;
//                    String dfN = df + "";
//                    String tarN[] = dfN.split("\\.");
//                    String txt0, txt1;
//                    txt0 = tarN[0];
//                    if (tarN[1].length() > 2) {
//                        txt1 = tarN[1].substring(0, 2);
//                    } else {
//                        txt1 = tarN[1];
//                    }
//                    result = txt0 + "." + txt1 + unit;
//                }

                break;
            case 2:

                break;
            case 3:
                long tar = Long.parseLong(target);

                String end1;
                double net_profit1 = tar;
                double abs_profit1 = Math.abs(tar);

                if (abs_profit1 > 100000000) {
                    end1 = "亿";
                    net_profit1 = net_profit1 / 100000000D;
                } else if (abs_profit1 > 10000) {
                    end1 = "万";
                    net_profit1 = net_profit1 / 10000D;
                } else {
                    end1 = "";
                }

                DecimalFormat df2 = new DecimalFormat("0.00");
                result = df2.format(net_profit1) + end1;

//                long tar=Long.parseLong(target);
//                int length=target.length();
//                if(length<=4){
//                    result=target;
//                }else if(length>4&&length<=8){
//                    double df=(double)tar/10000;
//                    String dfN=df+"";
//                    unit="万";
//                    String tarN[]=dfN.split("\\.");
//                    String txt0,txt1;
//                    txt0=tarN[0];
//                    if(tarN[1].length()>decimal){
//                        txt1=tarN[1].substring(0,decimal);
//                    }else{
//                        txt1=tarN[1];
//                    }
//                    result=txt0+"."+txt1+unit;
//                }else{
//                    unit="亿";
//                    double df=(double)tar/100000000;
//                    String dfN=df+"";
//                    String tarN[]=dfN.split("\\.");
//                    String txt0,txt1;
//                    txt0=tarN[0];
//                    if(tarN[1].length()>2){
//                        txt1=tarN[1].substring(0,2);
//                    }else{
//                        txt1=tarN[1];
//                    }
//                    result=txt0+"."+txt1+unit;
//                }
                break;
        }
        return result;
    }

    public static String getTransFormNum1(int target) {

        String result;
        String end = "";
        float t2;

        if (target > 100000000) {
            end = "亿";
            t2 = target / 100000000f;

            DecimalFormat df2 = new DecimalFormat("0.00");
            result = df2.format(t2) + end;
        } else if (target > 10000) {
            end = "万";
            t2 = target / 10000f;
            DecimalFormat df2 = new DecimalFormat("0.00");
            result = df2.format(t2) + end;
        } else {
            end = "";
            result = target + end;
        }

        return result;
    }

    private String removeEmptyData(String data) {
        if (data.equals("0") || data.equals("0.0") || data.equals("0.00")) {
            return "--";
        }
        return data;
    }

    public static void saveBitmap(final Context context, final String uri) {
        Log.e("", "保存图片");

        new Thread() {
            @Override
            public void run() {
                super.run();
                Bitmap bmp = null;
                try {
                    bmp = loadImageFromUrl(uri);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (bmp == null) {
                    Activity cc = (Activity) context;
                    cc.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(context, "保存本地失败", Toast.LENGTH_LONG).show();
                        }
                    });
                    return;
                }
                File f = new File("/sdcard/compass/compass/", "compass=" + System.currentTimeMillis() + ".png");
                if (!f.getParentFile().exists()) {
                    f.getParentFile().mkdirs();
                }
                try {
                    FileOutputStream out = new FileOutputStream(f);
                    bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
                    out.flush();
                    out.close();
                    Log.e("", "已经保存");
                    Activity cc = (Activity) context;
                    cc.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(context, "保存成功", Toast.LENGTH_LONG).show();
                        }
                    });
                    Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                    Uri uri = Uri.fromFile(f);
                    intent.setData(uri);
                    context.sendBroadcast(intent);//这个广播的目的就是更新图库
                } catch (FileNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }.start();

    }

    public static Bitmap loadImageFromUrl(String url) throws Exception {
        final DefaultHttpClient client = new DefaultHttpClient();
        final HttpGet getRequest = new HttpGet(url);

        HttpResponse response = client.execute(getRequest);
        int statusCode = response.getStatusLine().getStatusCode();
        if (statusCode != HttpStatus.SC_OK) {
            Log.e("PicShow", "Request URL failed, error code =" + statusCode);
        }

        HttpEntity entity = response.getEntity();
        if (entity == null) {
            Log.e("PicShow", "HttpEntity is null");
        }
        InputStream is = null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            is = entity.getContent();
            byte[] buf = new byte[1024];
            int readBytes = -1;
            while ((readBytes = is.read(buf)) != -1) {
                baos.write(buf, 0, readBytes);
            }
        } finally {
            if (baos != null) {
                baos.close();
            }
            if (is != null) {
                is.close();
            }
        }
        byte[] imageArray = baos.toByteArray();
        return BitmapFactory.decodeByteArray(
                imageArray, 0, imageArray.length);
    }

    public static int[] calculatePopWindowPos(final View anchorView, final View contentView) {
        final int windowPos[] = new int[2];
        final int anchorLoc[] = new int[2];
        anchorView.getLocationOnScreen(anchorLoc);
        final int anchorHeight = anchorView.getHeight();
        // 获取屏幕的高宽
        final int screenHeight = (int) ScreenUtil.getScreenHeight(anchorView.getContext());
        final int screenWidth = (int) ScreenUtil.getScreenWidth(anchorView.getContext());
        contentView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        // 计算contentView的高宽
        final int windowHeight = contentView.getMeasuredHeight();
        final int windowWidth = contentView.getMeasuredWidth();
        // 判断需要向上弹出还是向下弹出显示
        final boolean isNeedShowUp = (screenHeight - ScreenUtil.dpToPx(anchorView.getContext(), 110) - anchorLoc[1] - anchorHeight < windowHeight);
        if (isNeedShowUp) {
            windowPos[0] = screenWidth - windowWidth;
            windowPos[1] = anchorLoc[1] - windowHeight;
        } else {
            windowPos[0] = screenWidth - windowWidth;
            windowPos[1] = anchorLoc[1] + anchorHeight;
        }
        return windowPos;
    }
}
// personid=xxx 卡号
// skey=xxxx session
// company=xxxx 公司
// aid=xxxx 设备唯一id
// chid=xxx 渠道号
// ver=xxx 应用版本号
// os=xxx 系统操作系统(ios,android,windows)
// osver=xxx 系统版本号
// pmode=xxx 机型
