package com.ez08.compass.ui.view.drawutils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;

import com.ez08.compass.R;
import com.ez08.compass.entity.CGPoint;
import com.ez08.compass.entity.CxjcPointEntity;
import com.ez08.compass.entity.KLineValuesDataModel;

import java.util.List;

/**
 * K线图(蜡烛）工具
 * 
 * @author dingwei
 * 
 */
public class EzKLineChart extends EzDrawCharBase {

	private List<KLineValuesDataModel> kLineList; // k线数据对象
	private float chartwidth; // 柱状图的宽度
	private float gapWidth; // 柱状之间的间隔宽度
	private float lineWidth; // 线宽
	private int riseColor; // 涨颜色
	private int fallColor; // 跌颜色
	Bitmap bitPlus;
	Bitmap bitReduce;

	List<CxjcPointEntity> cxjcList;

	public void setCxjcPoint(List<CxjcPointEntity> entities, Context context){
		cxjcList = entities;
		if(cxjcList == null)
			return;
		bitPlus = BitmapFactory.decodeResource(context.getResources(), R.drawable.cxjc_plus);
		bitReduce = BitmapFactory.decodeResource(context.getResources(),R.drawable.cxjc_reduce);

		for (int i = 0; i < entities.size(); i++) {
			CxjcPointEntity entity = entities.get(i);
			for (int j = 0; j < kLineList.size(); j++) {
				if(entity.date == kLineList.get(j).date){
					kLineList.get(j).state = entity.state;
				}
			}
		}
	}
//	private double heightScale;
//	private double mLowestData;
//	private double mHighestData;

	/** 显示的蜡烛数据个数 */
	@Override
	public void draw(Canvas canvas, CGPoint position) {
		// TODO Auto-generated ethod stub
		super.draw(canvas, position);
		// 绘制蜡烛图
		Paint redPaint = new Paint();
		redPaint.setColor(riseColor);
		redPaint.setStyle(Style.STROKE);
		redPaint.setStrokeWidth(2f);

		Paint greenPaint = new Paint();
		greenPaint.setColor(fallColor);
		greenPaint.setStyle(Style.FILL);
		greenPaint.setStrokeWidth(2f);

		Paint whitePaint = new Paint();
		whitePaint.setColor(Color.WHITE);
		whitePaint.setStyle(Style.FILL);

		Paint linePaint = new Paint();
		linePaint.setStrokeWidth(2f);
		float startX = (float) (originPoint.getX() * scale);
		float startY = (float) (originPoint.getY() * scale);
		if (kLineList != null) {
			for (int i = 0; i < kLineList.size(); i++) {
				KLineValuesDataModel entity = kLineList.get(i);

				float open = (float) ((mHighestData - entity.getOpenValue()) * heightScale)
						+ startY;
				float close = (float) ((mHighestData - entity.getCloseValue()) * heightScale)
						+ startY;
				float high = (float) ((mHighestData - entity.getMaxValue()) * heightScale)
						+ startY;
				float low = (float) ((mHighestData - entity.getMinValue()) * heightScale)
						+ startY;
				float left = (float) (startX + (chartwidth * scale + gapWidth
						* scale)
						* i);
				if (left > width) {
					return;
				}
				if (open < close) {
					linePaint.setColor(fallColor);
					canvas.drawLine(left + (float) (chartwidth * scale) / 2,
							high, left + (float) (chartwidth * scale) / 2, low,
							linePaint);
					canvas.drawRect(left, open,
							(float) (left + (float) chartwidth * scale), close,
							greenPaint);
				} else if (open == close) {
					boolean rise=true;
					if(i!=0){
						float lastclose=kLineList.get(i-1).getCloseValue();
						if(entity.getOpenValue()<lastclose){
							rise=false;
						}
					}
					if(rise){
						linePaint.setColor(riseColor);
						canvas.drawLine(left + (float) (chartwidth * scale) / 2,
								high, left + (float) (chartwidth * scale) / 2, low,
								linePaint);
						canvas.drawRect(left, close, (float) (left + chartwidth
								* scale), open, whitePaint);
						canvas.drawRect(left, close, (float) (left + chartwidth
								* scale), open, redPaint);
					}else{
						linePaint.setColor(fallColor);
						canvas.drawLine(left + (float) (chartwidth * scale) / 2,
								high, left + (float) (chartwidth * scale) / 2, low,
								linePaint);
						canvas.drawRect(left, close, (float) (left + chartwidth
								* scale), open, greenPaint);
					}
				} else {
					linePaint.setColor(riseColor);
					canvas.drawLine(left + (float) (chartwidth * scale) / 2,
							high, left + (float) (chartwidth * scale) / 2, low,
							linePaint);
					canvas.drawRect(left, close, (float) (left + chartwidth
							* scale), open, whitePaint);
					canvas.drawRect(left, close, (float) (left + chartwidth
							* scale), open, redPaint);
				}

				if(cxjcList != null) {
					if (kLineList.get(i).state == 0) {
						RectF rect = new RectF(left + (float) (chartwidth * scale) / 2 - 20, high - 40 - 10, left + (float) (chartwidth * scale) / 2 + 20, high- 10);
						canvas.drawBitmap(bitReduce, null, rect, linePaint);
					} else if (kLineList.get(i).state == 1) {
						RectF rect = new RectF(left + (float) (chartwidth * scale) / 2 - 20, low + 10, left + (float) (chartwidth * scale) / 2 + 20, low + 40 + 10);
						canvas.drawBitmap(bitPlus, null, rect, linePaint);
					}
				}
			}

		}
	}

	public List<KLineValuesDataModel> getkLineList() {
		return kLineList;
	}

	public void setkLineList(List<KLineValuesDataModel> kLineList) {
		this.kLineList = kLineList;

		if(cxjcList != null) {
			for (int i = 0; i < cxjcList.size(); i++) {
				CxjcPointEntity entity = cxjcList.get(i);
				for (int j = 0; j < kLineList.size(); j++) {
					if (entity.date == kLineList.get(j).date) {
						kLineList.get(j).state = entity.state;
					}
				}
			}
		}
	}

	public float getGapWidth() {
		return gapWidth;
	}

	public void setGapWidth(float gapWidth) {
		this.gapWidth = gapWidth;
	}

	public float getLineWidth() {
		return lineWidth;
	}

	public void setLineWidth(float lineWidth) {
		this.lineWidth = lineWidth;
	}

	public int getRiseColor() {
		return riseColor;
	}

	public void setRiseColor(int riseColor) {
		this.riseColor = riseColor;
	}

	public int getFallColor() {
		return fallColor;
	}

	public void setFallColor(int fallColor) {
		this.fallColor = fallColor;
	}

	public float getChartwidth() {
		return chartwidth;
	}

	public void setChartwidth(float chartwidth) {
		this.chartwidth = chartwidth;
	}

	public double getHeightScale() {
		return heightScale;
	}

	public void setHeightScale(double heightScale) {
		this.heightScale = heightScale;
	}

	public double getmLowestData() {
		return mLowestData;
	}

	public void setmLowestData(double mLowestData) {
		this.mLowestData = mLowestData;
	}

	public double getmHighestData() {
		return mHighestData;
	}

	public void setmHighestData(double mHighestData) {
		this.mHighestData = mHighestData;
	}

}
