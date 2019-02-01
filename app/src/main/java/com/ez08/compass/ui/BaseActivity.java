package com.ez08.compass.ui;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.res.TypedArray;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.ez08.compass.CompassApp;
import com.ez08.compass.R;
import com.ez08.compass.tools.ToastUtils;
import com.umeng.analytics.MobclickAgent;

import java.util.List;

public class BaseActivity extends AppCompatActivity {
    protected int screenWidth;
    protected int screenHeigh;
    protected int shadow0Color, textContentColor, redColor, greenColor, mainColor;
    public boolean mAddStatus = true;    //是否在appcallback判断的activitys里

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        if (CompassApp.GLOBAL.THEME_STYLE == 0) {
            setTheme(R.style.DayTheme);
        } else {
            setTheme(R.style.NightTheme);
        }
        super.onCreate(savedInstanceState);
        TypedArray a = this.obtainStyledAttributes(null, R.styleable.main_attrs, 0, 0);
        shadow0Color = getResources().getColor(a.getResourceId(R.styleable.main_attrs_shadow0, 0));
        textContentColor = getResources().getColor(a.getResourceId(R.styleable.main_attrs_lable_list_style, 0));
        redColor = getResources().getColor(a.getResourceId(R.styleable.main_attrs_red_main_color, 0));
        greenColor = getResources().getColor(a.getResourceId(R.styleable.main_attrs_green_main_color, 0));
        mainColor = getResources().getColor(a.getResourceId(R.styleable.main_attrs_main_main_color, 0));

        screenWidth = CompassApp.GLOBAL.SCREEN_W;
        screenHeigh = CompassApp.GLOBAL.SCREEN_H;
    }

    public static void hideSoftInput(View view) {
        InputMethodManager imm = (InputMethodManager) view.getContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void showSoftInput(View view) {
        InputMethodManager imm = (InputMethodManager) view.getContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(view, 0);
    }

    protected void onDestroy() {
        super.onDestroy();
        Log.i("-zhang", "onDestroy");
    }

    public boolean isNetworkAvailble() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();
        if (info == null || !info.isAvailable()) {
            ToastUtils.show(this,"没有可用网络");
            return false;
        }
        return true;
    }

    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAddStatus) {
            if (!isAppOnForeground()) {
                //app 进入后台
                //全局变量isActive = false 记录当前已经进入后台
                CompassApp.GLOBAL.isActive = false;
                CompassApp.addStatis(CompassApp.GLOBAL.mgr.APP_STATUS, "1", "", System.currentTimeMillis());
            }
        }
    }

    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);

        //----------------app 从后台唤醒，进入前台
        if (mAddStatus) {
            if (!CompassApp.GLOBAL.isActive) {
                CompassApp.GLOBAL.isActive = true;
                CompassApp.addStatis(CompassApp.GLOBAL.mgr.APP_STATUS, "0", "", System.currentTimeMillis());
            }
        }
    }

    /**
     * 程序是否在前台运行
     *
     * @return
     */
    private boolean isAppOnForeground() {
        // Returns a list of application processes that are running on the
        // device

        ActivityManager activityManager = (ActivityManager) getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        String packageName = getApplicationContext().getPackageName();

        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager
                .getRunningAppProcesses();
        if (appProcesses == null)
            return false;

        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            // The name of the process that this object is associated with.
            if (appProcess.processName.equals(packageName)
                    && appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                return true;
            }
        }

        return false;
    }

}
