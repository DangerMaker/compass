package com.ez08.compass.ui.view.drawutils;

import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;

public class ChartHelper {
	/**
	 * 获取文字高度
	 * @param fontSize
	 * @return
	 */
	public static int getFontHeight(float fontSize) {
		Paint paint = new Paint();
		paint.setTextSize(fontSize);
		FontMetrics fm = paint.getFontMetrics();
		return (int) Math.ceil(fm.descent - fm.top) + 2;
	}
}
