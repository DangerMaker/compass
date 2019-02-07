package com.ez08.compass.ui.media.banner;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.AppCompatTextView;
import android.widget.TextView;

import com.ez08.compass.R;

public class NumberIndicator extends AppCompatTextView {

    public NumberIndicator(Context context) {
        super(context);
        setTextColor(Color.WHITE);
        setTextSize(14);
        setBackgroundResource(R.drawable.text_indicator_bg);
        int padding = DensityUtils.dp2px(context, 5);
        setPadding(padding,padding,padding,padding);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //保证TextIndicator的宽高一致(正方形)
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }
}
