package com.ez08.compass.entity;

public class BarChartEntity {
	private double net;//主力资金净值
	private String date;//日期

	private double DKnet;//多空资金净值

	private double GSDnet;//敢死队资金净值

	public double getNet() {
		return net;
	}
	public void setNet(double net) {
		this.net = net;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}

	public double getDKnet() {
		return DKnet;
	}

	public void setDKnet(double DKnet) {
		this.DKnet = DKnet;
	}

	public double getGSDnet() {
		return GSDnet;
	}

	public void setGSDnet(double GSDnet) {
		this.GSDnet = GSDnet;
	}
}
