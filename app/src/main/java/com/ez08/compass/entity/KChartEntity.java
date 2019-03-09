package com.ez08.compass.entity;

import com.ez08.compass.tools.MathUtils;
import com.ez08.compass.tools.UtilTools;

public class KChartEntity {

	private String time;// 日期
	private float open;// 开盘价
	private float high;// 最高价
	private float low;// 最低价
	private float close;// 收盘价
	private float volume;//
	private float amount;//
	private long lTime;
	private long minute;
	private float dr;
	public float openValue;
	public float highValue;
	public float lowValue;
	public float closeValue;

	public  void turnDr(){
		open = openValue/dr;
		close = closeValue/dr;
		high = highValue/dr;
		low = lowValue/dr;
	}

	public  void turnNormal(){
		open = openValue;
		close = closeValue;
		high = highValue;
		low = lowValue;
	}

	public void setDr(float dr){
		this.dr = dr;
	}

	public long getMinute() {
		return minute;
	}

	public void setMinute(long minute) {
		this.minute = minute;
	}

	public long getlTime() {
		return lTime;
	}

	public void setlTime(long lTime) {
		this.lTime = lTime;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public float getOpen() {
		return open;
	}

	public void setOpen(float open) {
		this.open = open;
	}

	public float getHigh() {
		return high;
	}

	public void setHigh(float high) {
		this.high = high;
	}

	public float getLow() {
		return low;
	}

	public void setLow(float low) {
		this.low = low;
	}

	public float getClose() {
		return close;
	}

	public void setClose(float close) {
		this.close = close;
	}

	public float getVolume() {
		return volume;
	}

	public void setVolume(float volume) {
		this.volume = volume;
	}

	public float getAmount() {
		return amount;
	}

	public void setAmount(float amount) {
		this.amount = amount;
	}

}
