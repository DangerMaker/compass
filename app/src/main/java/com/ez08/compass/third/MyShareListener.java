package com.ez08.compass.third;

import android.app.Activity;

import com.ez08.compass.CompassApp;
import com.ez08.compass.tools.ToastUtils;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.lang.ref.WeakReference;

public class MyShareListener implements UMShareListener {

    WeakReference<Activity> mReference;
    String statistics;

    public MyShareListener(Activity activity, String statistics) {
        mReference = new WeakReference<>(activity);
        this.statistics = statistics;
    }

    @Override
    public void onStart(SHARE_MEDIA share_media) {

    }

    @Override
    public void onResult(SHARE_MEDIA share_media) {
        ToastUtils.show(mReference.get(), "成功");
        if (statistics != null) {
            String arg1 = statistics;
            String arg2 = "";
            if(statistics.contains("@")){
                String[] params = statistics.split("@");
                arg1 = params[0];
                arg2 = params[1];
            }
            CompassApp.addStatis(arg1, "6", arg2,
                    System.currentTimeMillis());

        }
    }

    @Override
    public void onError(SHARE_MEDIA share_media, Throwable throwable) {
        ToastUtils.show(mReference.get(), "失败");
        if (statistics != null) {
            String arg1 = statistics;
            String arg2 = "";
            if(statistics.contains("@")){
                String[] params = statistics.split("@");
                arg1 = params[0];
                arg2 = params[1];
            }
            CompassApp.addStatis(arg1, "7", arg2,
                    System.currentTimeMillis());

        }
    }

    @Override
    public void onCancel(SHARE_MEDIA share_media) {
        ToastUtils.show(mReference.get(), "取消");
        if (statistics != null) {
            String arg1 = statistics;
            String arg2 = "";
            if(statistics.contains("@")){
                String[] params = statistics.split("@");
                arg1 = params[0];
                arg2 = params[1];
            }
            CompassApp.addStatis(arg1, "7", arg2,
                    System.currentTimeMillis());

        }
    }
}