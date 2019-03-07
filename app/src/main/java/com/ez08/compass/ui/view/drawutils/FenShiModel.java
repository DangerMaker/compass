package com.ez08.compass.ui.view.drawutils;

import org.json.JSONArray;

/**
 * 分时数据模型
 */
public class FenShiModel {

	private boolean index;
	
	private int sNumber;
	
	private JSONArray instant;
	
	private JSONArray history;
	
	private String scode;
	/**
	 * 股票代码
	 * @return
	 */
	public String getScode() {
		return scode;
	}
	/**
	 * 股票代码
	 * @param scode
	 */
	public void setScode(String scode) {
		this.scode = scode;
	}
	/**
	 * 分钟线需要的数据
	 * @return
	 */
	public JSONArray getHistory() {
		return history;
	}
	/**
	 * 分钟线需要的数据
	 * @param history
	 */
	public void setHistory(JSONArray history) {
		this.history = history;
	}
	/**
	 * 瞬时变化的数据
	 */
	public JSONArray getInstant() {
		return instant;
	}
	/**
	 * 瞬时变化的数据
	 */
	public void setInstant(JSONArray instant) {
		this.instant = instant;
	}
	
	public int getsNumber() {
		return sNumber;
	}
	public void setsNumber(int sNumber) {
		this.sNumber = sNumber;
	}
	/**
	 * 是否为指数
	 * True，是指数
	 * False，不是指数，是一般股票
	 * @return
	 */
	public boolean isIndex() {
		return index;
	}
	/**
	 * 是否为指数
	 * True，是指数
	 * False，不是指数，是一般股票
	 */
	public void setIndex(boolean index) {
		this.index = index;
	}
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("index = "+index).append("\r\n");
		sb.append("sNumber = "+sNumber).append("\r\n");
		sb.append("scode = "+scode).append("\r\n");
		sb.append("instant = "+instant.toString()).append("\r\n");
		sb.append("history = "+history.toString()).append("\r\n");
		return sb.toString();
	}
}
