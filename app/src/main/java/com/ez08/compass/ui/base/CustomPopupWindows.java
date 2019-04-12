package com.ez08.compass.ui.base;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;


public class CustomPopupWindows extends PopupWindow {

    Context context;
    public CustomPopupWindows(Context context,View view, int width, int height,boolean flag) {
        super(view,width,height,flag);
        this.context = context;

        ColorDrawable dw = new ColorDrawable(0000000000);
        setBackgroundDrawable(dw);
        setFocusable(true);
        setOutsideTouchable(true);
    }

    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = ((Activity)context).getWindow().getAttributes();
        lp.alpha = bgAlpha; // 0.0~1.0
        ((Activity)context).getWindow().setAttributes(lp); //act 是上下文context

    }

    @Override
    public void showAsDropDown(View anchor) {
        super.showAsDropDown(anchor);
        backgroundAlpha(0.7f);
    }

    @Override
    public void dismiss() {
        super.dismiss();
        backgroundAlpha(1f);
    }
}
