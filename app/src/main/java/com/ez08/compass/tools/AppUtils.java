package com.ez08.compass.tools;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.ThumbnailUtils;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.view.View;

import com.ez08.compass.CompassApp;
import com.ez08.compass.R;
import com.ez08.compass.auth.AuthUserInfo;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Calendar;
import java.util.List;

public class AppUtils {
    /**
     * 判断当前应用程序是否在前台
     *
     * @param context
     * @return
     */
    public static boolean isAppOnForeground(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getApplicationContext().getSystemService(
                Context.ACTIVITY_SERVICE);
        String packageName = context.getApplicationContext().getPackageName();
        List<RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        if (appProcesses == null)
            return false;
        for (RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals(packageName)
                    && appProcess.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                return true;
            }
        }
        return false;
    }

    public static SharedPreferences getSharedPrefCerences(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("pushlist&" + AuthUserInfo.getMyCid(),
                Activity.MODE_PRIVATE);
        return preferences;
    }

    public static SharedPreferences getIndexSharedPrefCerences(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("index&" + AuthUserInfo.getMyCid(),
                Activity.MODE_PRIVATE);
        return preferences;
    }

    public static boolean isHUAWEI() {
        try {
            if (Build.BRAND.equals("HUAWEI")) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }

    }

    public static String getDeviceInfo() {
        StringBuffer sb = new StringBuffer();
        sb.append("主板：" + Build.BOARD);
        sb.append("\n系统启动程序版本号：" + Build.BOOTLOADER);
        sb.append("\n系统定制商：" + Build.BRAND);
        sb.append("\ncpu指令集：" + Build.CPU_ABI);
        sb.append("\ncpu指令集2：" + Build.CPU_ABI2);
        sb.append("\n设置参数：" + Build.DEVICE);
        sb.append("\n显示屏参数：" + Build.DISPLAY);
        sb.append("\n无线电固件版本：" + Build.getRadioVersion());
        sb.append("\n硬件识别码：" + Build.FINGERPRINT);
        sb.append("\n硬件名称：" + Build.HARDWARE);
        sb.append("\nHOST:" + Build.HOST);
        sb.append("\n修订版本列表：" + Build.ID);
        sb.append("\n硬件制造商：" + Build.MANUFACTURER);
        sb.append("\n版本：" + Build.MODEL);
        sb.append("\n硬件序列号：" + Build.SERIAL);
        sb.append("\n手机制造商：" + Build.PRODUCT);
        sb.append("\n描述Build的标签：" + Build.TAGS);
        sb.append("\nTIME:" + Build.TIME);
        sb.append("\nbuilder类型：" + Build.TYPE);
        sb.append("\nUSER:" + Build.USER);
        return sb.toString();
    }

    public static String viewSaveToImage(View view) {
        view.setDrawingCacheEnabled(true);
        view.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        view.setDrawingCacheBackgroundColor(Color.WHITE);

        // 把一个View转换成图片
        Bitmap cachebmp = loadBitmapFromView(view);

        FileOutputStream fos;
        String imagePath = "";
        try {
            // 判断手机设备是否有SD卡
            boolean isHasSDCard = Environment.getExternalStorageState().equals(
                    android.os.Environment.MEDIA_MOUNTED);
            if (isHasSDCard) {
                // SD卡根目录
                File sdRoot = Environment.getExternalStorageDirectory();
                File file = new File(sdRoot, Calendar.getInstance().getTimeInMillis() + ".png");
                fos = new FileOutputStream(file);
                imagePath = file.getAbsolutePath();
            } else
                throw new Exception("创建文件失败!");

            cachebmp.compress(Bitmap.CompressFormat.PNG, 90, fos);

            fos.flush();
            fos.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        view.destroyDrawingCache();
        return imagePath;
    }

    public static Bitmap viewSaveToBitmap(View view) {
        view.setDrawingCacheEnabled(true);
        view.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        view.setDrawingCacheBackgroundColor(Color.WHITE);
        Bitmap cachebmp = loadBitmapFromView(view);
        view.destroyDrawingCache();
        return cachebmp;
    }

    public static Bitmap loadBitmapFromView(View v) {
        int w = v.getWidth();
//        int h = v.getHeight();

//        Bitmap bmp = Bitmap.createBitmap(w, CompassApp.dividerHeight - getStatusHeight(v.getContext()), Bitmap.Config.ARGB_8888);
//        Canvas c = new Canvas(bmp);
//
//        c.drawColor(Color.WHITE);
//        /** 如果不设置canvas画布为白色，则生成透明 */
//
//        v.layout(0, 0, w, h);
//        v.draw(c);

        if (CompassApp.GLOBAL.SCREEN_H < 1) {
            CompassApp.GLOBAL.SCREEN_H = 1000;
        }

        int tempTop = CompassApp.GLOBAL.dividerHeight - getStatusHeight(v.getContext());
        int tempBottom = w / (785 / 148);

        Bitmap bmp = Bitmap.createBitmap(w, tempTop
                + tempBottom, Bitmap.Config.RGB_565);

        Canvas c = new Canvas(bmp);
        c.drawColor(Color.WHITE);
        /** 如果不设置canvas画布为白色，则生成透明 */

        v.layout(0, 0, w, tempTop);
        v.draw(c);

        //logo print

        Bitmap bmpLogo = BitmapFactory.decodeResource(v.getContext().getResources(), R.drawable.compass_share_f);

        Bitmap bmpNew = ThumbnailUtils.extractThumbnail(bmpLogo, w, tempBottom);

        Log.e("width", w + "");
        Log.e("logo", "width :" + bmpLogo.getWidth() + ",height :" + bmpLogo.getHeight());

        Paint paint = new Paint();
        c.drawBitmap(bmpNew, 0, tempTop, paint);

//        return bmp;
        return ThumbnailUtils.extractThumbnail(bmp, w , (tempTop + tempBottom));
    }

    public static int getStatusHeight(Context context) {
        int statusBarHeight1 = 0;
        //获取status_bar_height资源的ID
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0)

        {
            //根据资源ID获取响应的尺寸值
            statusBarHeight1 = context.getResources().getDimensionPixelSize(resourceId);
        }

        return statusBarHeight1;
    }


}
