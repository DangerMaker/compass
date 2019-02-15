package com.ez08.compass.tools;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.AttrRes;

public class MyAppCompat {

    public static int getColor(Context context, @AttrRes int attr){
        TypedArray typedArray = context.obtainStyledAttributes(new int[]{attr});
        int color = typedArray.getColor(0,-1);
        return color;
    }
}
