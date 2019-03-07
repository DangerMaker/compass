package com.ez08.compass.entity;

public class FenShiDesEntity {

	private String value;
	private String valueRate;
	private String valueAverage;
	private String column;
	private String columnAll;
	private String countAll;
	private double count;
	private double acolumn;
	private boolean isHigh;
	private String time;	//本地补的时间

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public double getAcolumn() {
		return acolumn;
	}

	public void setAcolumn(double acolumn) {
		this.acolumn = acolumn;
	}

	public double getCount() {
		return count;
	}

	public void setCount(double count) {
		this.count = count;
	}

	public boolean isHigh() {
		return isHigh;
	}

	public void setHigh(boolean isHigh) {
		this.isHigh = isHigh;
	}
	/**
	 * 价格
	 * @return
	 */
	public String getValue() {
		return value;
	}
	/**
	 * 价格
	 * @return
	 */
	public void setValue(String value) {
		this.value = value;
	}
	/**
	 * 涨跌
	 * @return
	 */
	public String getValueRate() {
		return valueRate;
	}
	/**
	 * 涨跌
	 * @return
	 */
	public void setValueRate(String valueRate) {
		this.valueRate = valueRate;
	}
	/**
	 * 均价
	 * @return
	 */
	public String getValueAverage() {
		return valueAverage;
	}
	/**
	 * 均价
	 * @return
	 */
	public void setValueAverage(String valueAverage) {
		this.valueAverage = valueAverage;
	}
	/**
	 * 成交量
	 * @return
	 */
	public String getColumn() {
		return column;
	}
	/**
	 * 成交量
	 * @return
	 */
	public void setColumn(String column) {
		this.column = column;
	}
	/**
	 * 总量
	 * @return
	 */
	public String getColumnAll() {
		return columnAll;
	}
	/**
	 * 总量
	 * @return
	 */
	public void setColumnAll(String columnAll) {
		this.columnAll = columnAll;
	}
	/**
	 * 总额
	 * @return
	 */
	public String getCountAll() {
		return countAll;
	}
	/**
	 * 总额
	 * @return
	 */
	public void setCountAll(String countAll) {
		this.countAll = countAll;
	}

}
