package com.ez08.compass.tools;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

/**
 * Created by liuxiaoyu on 2018/5/4.
 */

public class ToastUtils {

    private static Toast toast = null;

    public static void show(Context context, String text) {
        if(context != null) {
            if (toast == null) {
                toast = Toast.makeText(context.getApplicationContext(),
                        text, Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
            }

            toast.setText(text);
            toast.show();
        }
    }

    public static void printCurrentThread(){
        Log.i("Thread", Thread.currentThread().getName());
    }

}
