package com.ez08.compass.entity;

/**
 * k线对象
 * @author Administrator
 *
 */
public class KLineValuesDataModel {

	private float openValue; // 开盘价
	private float closeValue; // 收盘价
	private float maxValue; // 最高价
	private float minValue; // 最低价

	public int date;
	public int state = -1;

	public float getOpenValue() {
		return openValue;
	}


	public void setOpenValue(float openValue) {
		this.openValue = openValue;
	}


	public float getCloseValue() {
		return closeValue;
	}


	public void setCloseValue(float closeValue) {
		this.closeValue = closeValue;
	}


	public float getMaxValue() {
		return maxValue;
	}


	public void setMaxValue(float maxValue) {
		this.maxValue = maxValue;
	}


	public float getMinValue() {
		return minValue;
	}


	public void setMinValue(float minValue) {
		this.minValue = minValue;
	}



	public KLineValuesDataModel(float openValue, float maxValue,
                                float minValue, float closeValue, int date) {
		super();
		this.openValue = openValue;
		this.closeValue = closeValue;
		this.maxValue = maxValue;
		this.minValue = minValue;
		this.date = date;
	}
	
}
