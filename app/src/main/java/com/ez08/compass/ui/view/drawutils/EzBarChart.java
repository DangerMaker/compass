package com.ez08.compass.ui.view.drawutils;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;

import com.ez08.compass.entity.CGPoint;
import com.ez08.compass.entity.ColumnValuesDataModel;

import java.util.List;

/**
 * 柱状图工具
 * 
 * @author dingwei
 * 
 */
public class EzBarChart extends EzDrawCharBase {
	private List<ColumnValuesDataModel> values;// 图形数据
	private float columnWidth;// 柱宽
	private float gapWidth;// 柱间宽度
	private boolean display;// 底部是否显示数值
	private float offsetX = 0;// x轴的偏移量
	private float textFont = 0;// 数据字体大小

	@Override
	public void draw(Canvas canvas, CGPoint position) {
		// TODO Auto-generated method stub
		super.draw(canvas, position);
		if(originPoint==null){
			return;
		}
		Paint paint = new Paint();
		paint.setStyle(Style.FILL);
		paint.setAntiAlias(true);

		float startX = originPoint.getX() + offsetX;
		float startY = originPoint.getY();
		for (int i = 0; i < values.size(); i++) {
			ColumnValuesDataModel value = values.get(i);
			paint.setColor(value.getColumnColor());
//			Log.i("-i", " getValue = " + value.getValue());
			if (value.getValue() >= 0) {
				canvas.drawRect(startX, (float) (startY - value.getValue()
						* heightScale), startX + columnWidth,
						startY, paint);
			} else {
				canvas.drawRect(startX, startY, (float) (startX + columnWidth), (float) (startY - value.getValue()
						* heightScale), paint);
			}

			if (display) {
				paint.setTextSize(textFont);
				canvas.drawText(String.valueOf(value.getValue()), startX,
						startY, paint);
			}
			startX += columnWidth + gapWidth;
		}
	}

	public List<ColumnValuesDataModel> getValues() {
		return values;
	}

	public void setValues(List<ColumnValuesDataModel> values) {
		this.values = values;
	}

	public float getColumnWidth() {
		return columnWidth;
	}

	public void setColumnWidth(float columnWidth) {
		this.columnWidth = columnWidth;
	}

	public float getGapWidth() {
		return gapWidth;
	}

	public void setGapWidth(float gapWidth) {
		this.gapWidth = gapWidth;
	}

	public boolean isDisplay() {
		return display;
	}

	public void setDisplay(boolean display) {
		this.display = display;
	}

	public float getOffsetX() {
		return offsetX;
	}

	public void setOffsetX(float offsetX) {
		this.offsetX = offsetX;
	}

	public float getTextFont() {
		return textFont;
	}

	public void setTextFont(float textFont) {
		this.textFont = textFont;
	}

}
