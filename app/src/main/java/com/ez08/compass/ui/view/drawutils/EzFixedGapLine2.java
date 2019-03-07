package com.ez08.compass.ui.view.drawutils;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.util.Log;

import java.util.List;

public class EzFixedGapLine2 {
    /**
     * 线条颜色
     */
    private int lineColor;
    /**
     * 线条宽度
     */
    private float lineWidth;

    /**
     * 宽度
     */
    private float mWidth;
    /**
     * 高度
     */
    private float mHeight;

    private boolean shadowColor = false;// 是否显示线下阴影
    /**
     *
     */
    private int displayNumber;

    private List<Float> linesData;// 图形数据

    protected double minValue;
    protected double maxValue;

    Path linePath = new Path();
    Path linePath1 = new Path();

    public void draw(Canvas canvas) {
        Paint mPaintShade = new Paint();
        mPaintShade.setColor(lineColor);
        mPaintShade.setAlpha(10);
        mPaintShade.setAntiAlias(true);

        Paint mPaintLine = new Paint();
        mPaintLine.setColor(lineColor);
        mPaintLine.setAntiAlias(true);
        mPaintLine.setStrokeWidth(lineWidth);
        mPaintLine.setStyle(Paint.Style.STROKE);
        mPaintLine.setFakeBoldText(true);

        float lineLength = mWidth / displayNumber;
        float startX = lineLength;
        linePath.reset();
        linePath1.reset();
        for (int j = 0; j < linesData.size(); j++) {

            float value = linesData.get(j);
            float valueY = (float) (mHeight - ((value - minValue) / (maxValue - minValue) * mHeight));
            if (valueY > mHeight) {
                valueY = mHeight - 3;
            } else if (valueY <= 0) {
                valueY = 3;
            }
            if (j == 0) {
                linePath.moveTo(startX, mHeight);
                linePath.lineTo(startX, valueY);

                linePath1.moveTo(startX,valueY);
            } else if (j == linesData.size() - 1) {
                linePath.lineTo(startX, valueY);
                linePath.lineTo(startX, mHeight);

                linePath1.lineTo(startX, valueY);
            } else {
                linePath.lineTo(startX, valueY);

                linePath1.lineTo(startX, valueY);
            }
            startX = startX + lineLength;
        }

        if (linesData.size() > 0) {
            canvas.drawPath(linePath1,mPaintLine);
        }

        linePath.close();
        if (shadowColor) {
            canvas.drawPath(linePath, mPaintShade);
        }

    }

    public void setDisplayUnderLineShadowColor(boolean shadowColor) {
        this.shadowColor = shadowColor;
    }

    public void setLineColor(int lineColor) {
        this.lineColor = lineColor;
    }

    public void setLineWidth(float lineWidth) {
        this.lineWidth = lineWidth;
    }

    public void setWidth(float mWidth) {
        this.mWidth = mWidth;
    }

    public void setHeight(float mHeight) {
        this.mHeight = mHeight;
    }

    public void setDisplayNumber(int displayNumber) {
        this.displayNumber = displayNumber;
    }

    public void setLinesData(List<Float> linesData) {
        this.linesData = linesData;
    }

    public List<Float> getValues() {
        return linesData;
    }

    public void setMinValue(double minValue) {
        this.minValue = minValue;
    }

    public void setMaxValue(double maxValue) {
        this.maxValue = maxValue;
    }

}
