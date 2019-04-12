package com.ez08.compass.entity;

import java.io.Serializable;
import java.util.List;

public class StockNewsEntity implements Serializable {
	private List<StockNews> news;
	private List<StockReport> report;
	private List<StockNotice> notice;
	public List<StockNews> getNews() {
		return news;
	}
	public void setNews(List<StockNews> news) {
		this.news = news;
	}
	public List<StockReport> getReport() {
		return report;
	}
	public void setReport(List<StockReport> report) {
		this.report = report;
	}
	public List<StockNotice> getNotice() {
		return notice;
	}
	public void setNotice(List<StockNotice> notice) {
		this.notice = notice;
	}
	
}
