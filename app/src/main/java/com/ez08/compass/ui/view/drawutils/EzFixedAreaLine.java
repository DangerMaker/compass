package com.ez08.compass.ui.view.drawutils;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;

import com.ez08.compass.entity.CGPoint;

import java.util.ArrayList;
import java.util.List;

/**
 * 根据固定的水平间隔来绘制
 * 
 * @author dingwei
 * 
 */
public class EzFixedAreaLine extends EzDrawCharBase {

	private float lineWidth;// 线条宽度
	private int underLineShadowColor;// 线下阴影颜色
	/**
	 * 点间水平距离
	 */
	private double interval;// 点间水平距离
	private List<Float> values;// 图形数据
	private float dashLength = 0;// 虚线间隔长度
	private float offsetX = 0;// X轴的偏移量（默认为0）
	private boolean displayUnderLineShadowColor = false;// 是否显示线下阴影
	Paint paint;

	float startX;
	float startY;
	@Override
	public void draw(Canvas canvas, CGPoint position) {
		// TODO Auto-generated method stub
		super.draw(canvas, position);
		if(values==null||position==null){
			return;
		}

		 startX = (float) (originPoint.getX() * scale);
		 startY = (float) (originPoint.getY() * scale);

		y12 = (float) ((mHighestData * heightScale) + startY);
		makeDHYDPoint(startX,startY);

		DashPathEffect pathEffect =new DashPathEffect(new float[]{5, 5, 5, 5}, 1);

		Paint paint1 = newPaint(Color.parseColor("#F43731")); //red
		Paint paint2 =  newPaint(Color.parseColor("#E89712")); //yellow
		Paint paint3 =  newPaint(Color.parseColor("#008000")); //green
		Paint paint4 = newPaint(Color.GRAY);
		paint4.setPathEffect(pathEffect);

		paint = paint1;
		Path path = new Path();

		for (int i = 0; i < points.size() - 1; i++) {
			CGPoint p1 = points.get(i);
			CGPoint p2 = points.get(i + 1);
			if (p1.y <= y12 && p2.y <= y12) {
				paint = paint1;
			}  else if(p1.y >= y12 && p2.y >= y12) {
				paint = paint3;
			}
			path.reset();
			path.moveTo(p1.x,p1.y);
			path.lineTo(p2.x,p2.y);
			path.lineTo(p2.x,y12);
			path.lineTo(p1.x,y12);
			path.close();

			canvas.drawPath(path,paint);
		}

	}
	private Paint newPaint(int lineColor){
		Paint paint = new Paint();
		paint.setAntiAlias(true);
		paint.setColor(lineColor);
		paint.setStrokeWidth(lineWidth);
		paint.setStyle(Style.FILL_AND_STROKE);
		return paint;
	}

	/*
 * 根据对角正玄值相等的原理
 * y1/x1 = y2/x2;
 * x1 = x2*y1/y2;
 * x2 = x1*y2/y1;
 * x1 + x2 = interval;
 */

	CGPoint cGPointZero;
	private CGPoint get12WithPrevious(CGPoint previous,CGPoint current,float axis){

		CGPoint result = cGPointZero;
		if (previous.y != current.y) {
			float y1 = previous.y - axis;
			float y12 = previous.y - current.y;//
			float x1 = (float) interval * y1 / y12;
			result = new CGPoint(previous.x + x1, axis);
		} else {
			result = current;
		}
		return result;
	}

	List<CGPoint> points;
	float y12 = 0;

	private void makeDHYDPoint(float startX,float startY){
		cGPointZero = new CGPoint(startX,startY);
		points = new ArrayList<>();
		float x = startX;
		float y = 0;

		float previousValue = 0;
		CGPoint previousPoint = cGPointZero;

		for (int i = 0; i < values.size(); i++) {
			float value = values.get(i);
			y = (float) ((mHighestData - value) * heightScale) + startY;
			CGPoint point = new CGPoint(x, y);
			if (i == 0) {
				points.add(point);
			} else {
				if (previousValue >= 0) {
					if (value >= 0) {
						points.add(point);
					} else {
						CGPoint point12 = get12WithPrevious(previousPoint,point,y12);
						points.add(point12);
						points.add(point);
					}
				} else{
					if (value >= 0) {
						CGPoint point4 = get12WithPrevious(previousPoint,point,y12);
						points.add(point4);
						points.add(point);
					} else {
						points.add(point);
					}

				}
			}
			previousValue = value;
			previousPoint = point;
			x = x + (float)interval * (float)scale;
		}


	}

	Paint paintText;
	public void setNumColor(Paint numText){
		paintText = numText;
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

	public int getUnderLineShadowColor() {
		return underLineShadowColor;
	}

	/**
	 * 阴影颜色
	 * 
	 * @param underLineShadowColor
	 */
	public void setUnderLineShadowColor(int underLineShadowColor) {
		this.underLineShadowColor = underLineShadowColor;
	}

	public boolean isDisplayUnderLineShadowColor() {
		return displayUnderLineShadowColor;
	}

	public void setDisplayUnderLineShadowColor(
			boolean displayUnderLineShadowColor) {
		this.displayUnderLineShadowColor = displayUnderLineShadowColor;
	}
}
