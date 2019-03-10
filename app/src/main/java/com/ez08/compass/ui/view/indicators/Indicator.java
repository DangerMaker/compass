package com.ez08.compass.ui.view.indicators;

import android.graphics.Canvas;

public interface Indicator<T> {
    void setWidth(int width);
    void setHeight(int height);
    void setData(T t);
    void setRange(int start,int end);
    void draw(Canvas canvas);
}
