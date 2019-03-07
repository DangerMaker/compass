package com.ez08.compass.ui.base;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;



public abstract class BasePopupWindows<T> extends PopupWindow {

    public Context context;
    public View container;

    public BasePopupWindows(Context context) {
        super(context);
        this.context = context;
        this.setWindowLayoutMode(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        container = inflater.inflate(getLayoutResource(), null);
        onCreateView(container);
        this.setContentView(container);
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        ColorDrawable dw = new ColorDrawable(0000000000);
        this.setBackgroundDrawable(dw);
    }


    protected abstract void onCreateView(View view);

    protected abstract int getLayoutResource();

    protected abstract void setData(T data);

    public void showPopupWindow(View parent) {
        if (!this.isShowing()) {
            showAsDropDown(parent);
        } else {
            this.dismiss();
        }
    }
}
