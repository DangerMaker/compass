package com.ez08.compass.tools;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.annotation.AttrRes;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

public class MyAppCompat {

    public static int getColor(Context context, @AttrRes int attr){
        TypedArray typedArray = context.obtainStyledAttributes(new int[]{attr});
        int color = typedArray.getColor(0,-1);
        return color;
    }

    public static void setTextBackgroud(View view, Context context){
        // If we're running on Honeycomb or newer, then we can use the Theme's
        // selectableItemBackground to ensure that the View has a pressed state
        TypedValue outValue = new TypedValue();
        context.getTheme().resolveAttribute(android.R.attr.selectableItemBackground,
                outValue, true);
        view.setBackgroundResource(outValue.resourceId);
    }
}
