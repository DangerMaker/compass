package com.ez08.compass.ui.stocks;

public class PieChartEntity {
	private double mainBuyAmount = 0;// 主力流入量
	private double mainSellAmount = 0;// 主力流出量
	private double retailBuyAmount = 0;//散户流入量
	private double retailSellAmount = 0;//散户流出量
	private double sgBuyAmount = 0;	//敢死队买入量
	private double sgSellAmount = 0;	//敢死队卖出量
	private double dkBuyMoney = 0;	//多方资金
	private double dkSellMoney = 0;	//空方资金

	public double getDkBuyMoney() {
		return dkBuyMoney;
	}

	public void setDkBuyMoney(double dkBuyMoney) {
		this.dkBuyMoney = dkBuyMoney;
	}

	public double getDkSellMoney() {
		return dkSellMoney;
	}

	public void setDkSellMoney(double dkSellMoney) {
		this.dkSellMoney = dkSellMoney;
	}

	public double getSgBuyAmount() {
		return sgBuyAmount;
	}

	public void setSgBuyAmount(double sgBuyAmount) {
		this.sgBuyAmount = sgBuyAmount;
	}

	public double getSgSellAmount() {
		return sgSellAmount;
	}

	public void setSgSellAmount(double sgSellAmount) {
		this.sgSellAmount = sgSellAmount;
	}

	public double getMainBuyAmount() {
		return mainBuyAmount;
	}
	public void setMainBuyAmount(double mainBuyAmount) {
		this.mainBuyAmount = mainBuyAmount;
	}
	public double getMainSellAmount() {
		return mainSellAmount;
	}
	public void setMainSellAmount(double mainSellAmount) {
		this.mainSellAmount = mainSellAmount;
	}
	public double getRetailBuyAmount() {
		return retailBuyAmount;
	}
	public void setRetailBuyAmount(double retailBuyAmount) {
		this.retailBuyAmount = retailBuyAmount;
	}
	public double getRetailSellAmount() {
		return retailSellAmount;
	}
	public void setRetailSellAmount(double retailSellAmount) {
		this.retailSellAmount = retailSellAmount;
	}
	
	

}
