package com.ez08.compass.ui.view;

import android.content.Context;
import android.graphics.PointF;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by Administrator on 2017/8/28.
 */

public class ChildrenViewPager extends ViewPager {

    PointF downP = new PointF();
    /** 触摸时当前的点 **/
    PointF curP = new PointF();
    InsideViewPager.OnSingleTouchListener onSingleTouchListener;

    public ChildrenViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return false;
    }
}
