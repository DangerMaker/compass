package com.ez08.compass.ui.view.drawutils;

import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;

import com.ez08.compass.entity.CGPoint;

import java.util.List;

/**
 * 根据固定的水平间隔来绘制
 *
 * @author dingwei
 */
public class EzFixedGapLine extends EzDrawCharBase {

    private int lineColor;// 线条颜色
    private float lineWidth;// 线条宽度
    /**
     * 点间水平距离
     */
    private double interval;// 点间水平距离
    private List<Float> values;// 图形数据
    private float dashLength = 0;// 虚线间隔长度
    private float offsetX = 0;// X轴的偏移量（默认为0）

    @Override
    public void draw(Canvas canvas, CGPoint position) {
        // TODO Auto-generated method stub
        super.draw(canvas, position);
        if (values == null || position == null) {
            return;
        }
        Paint paint = new Paint();

        float startX = originPoint.getX();
        float startY = originPoint.getY();
        if (dashLength != 0) {
            DashPathEffect dash = new DashPathEffect(new float[]{dashLength,
                    dashLength, dashLength, dashLength}, 10);
            paint.setPathEffect(dash);
        }

        paint.setAntiAlias(true);
        paint.setColor(lineColor);
        paint.setStrokeWidth(lineWidth);
        paint.setStyle(Style.STROKE);
        Path path = new Path();
        path.moveTo(startX,
                (float) ((mHighestData - values.get(0)) * heightScale) + startY);
        for (int i = 0; i < values.size() - 1; i++) {
            path.lineTo((float) (startX + (i + 1) * interval * scale),
                    (float) ((mHighestData - values.get(i + 1)) * heightScale)
                            + startY);
        }
        canvas.drawPath(path, paint);

    }

    public int getLineColor() {
        return lineColor;
    }

    public void setLineColor(int lineColor) {
        this.lineColor = lineColor;
    }

    public float getLineWidth() {
        return lineWidth;
    }

    /**
     * 线条宽度
     *
     * @param lineWidth
     */
    public void setLineWidth(float lineWidth) {
        this.lineWidth = lineWidth;
    }

    public double getInterval() {
        return interval;
    }

    /**
     * 点间水平距离
     *
     * @param interval
     */
    public void setInterval(double interval) {
        this.interval = interval;
    }

    public List<Float> getValues() {
        return values;
    }

    public void setValues(List<Float> values) {
        this.values = values;
    }

    public float getDashLength() {
        return dashLength;
    }

    /**
     * 虚线间隔长度
     *
     * @param dashLength
     */
    public void setDashLength(float dashLength) {
        this.dashLength = dashLength;
    }

    public float getOffsetX() {
        return offsetX;
    }

    /**
     * X轴的偏移量（默认为0）
     *
     * @param offsetX
     */
    public void setOffsetX(float offsetX) {
        this.offsetX = offsetX;
    }

}
