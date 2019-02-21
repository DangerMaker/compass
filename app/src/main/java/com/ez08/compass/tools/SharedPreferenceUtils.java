package com.ez08.compass.tools;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferenceUtils {

    public static void saveServerConfig(Context context, String result){
        SharedPreferences mySharedPreferences = context.getSharedPreferences("net_config",
                Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = mySharedPreferences
                .edit();
        editor.putString("update_result", result);
        editor.apply();
    }

    public static String setServerConfig(Context context){
        SharedPreferences netPreferences = context.getSharedPreferences("net_config",
                Activity.MODE_PRIVATE);
        String data = netPreferences.getString("update_result",
                "");
        return data;
    }

}
