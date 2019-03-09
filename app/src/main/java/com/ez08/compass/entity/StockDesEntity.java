package com.ez08.compass.entity;

import java.io.Serializable;

public class StockDesEntity implements Serializable {

	private String date; // 日期
	private String time;	//时分秒
	private float openValue; // 开盘价
	private float highValue; // 最高价
	private float lowValue; // 最低价
	private float closeValue; // 收盘价
	private float lastClose;
	private float MA5;
	private float MA10;
	private float MA20;
	private float MA60;
	// --column
	private float Column;
	private float VollMA5;
	private float VollMA10;
	private float VollMA20;
	// macd
	private float DIF;
	private float DEA;
	private float BAR;
	// kdj
	private float K;
	private float D;
	private float J;
	// rsi
	private float RSI6;
	private float RSI12;
	// bias
	private float BIAS1;
	private float BIAS2;
	private float BIAS3;
	// cci
	private float CCI;
	// roc
	private float ROC;
	private float ROCMA;
	// asi
	private float ASI;
	// psy
	private float PSY12;

	//主力资金
	private float zhu;
	//多空资金
	private float dk;
	//敢死队资金
	private float gsd;

	private String mColumn;
	private String mVollMA5;
	private String mVollMA10;
	private String mVollMA20;

	private float hyd;

	public void setLastClose(float lastClose){
		this.lastClose = lastClose;
	}

	public float getLastClose(){
		return lastClose;
	}

	public float getHyd(){
		return hyd;
	}

	public void setHyd(float hyd){
		this.hyd = hyd;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public float getZhu() {
		return zhu;
	}

	public void setZhu(float zhu) {
		this.zhu = zhu;
	}

	public float getDk() {
		return dk;
	}

	public void setDk(float dk) {
		this.dk = dk;
	}

	public float getGsd() {
		return gsd;
	}

	public void setGsd(float gsd) {
		this.gsd = gsd;
	}

	public String getmColumn() {
		return mColumn;
	}

	public void setmColumn(String mColumn) {
		this.mColumn = mColumn;
	}

	public String getmVollMA5() {
		return mVollMA5;
	}

	public void setmVollMA5(String mVollMA5) {
		this.mVollMA5 = mVollMA5;
	}

	public String getmVollMA10() {
		return mVollMA10;
	}

	public void setmVollMA10(String mVollMA10) {
		this.mVollMA10 = mVollMA10;
	}

	public String getmVollMA20() {
		return mVollMA20;
	}

	public void setmVollMA20(String mVollMA20) {
		this.mVollMA20 = mVollMA20;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public float getOpenValue() {
		return openValue;
	}

	public void setOpenValue(float openValue) {
		this.openValue = openValue;
	}

	public float getHighValue() {
		return highValue;
	}

	public void setHighValue(float highValue) {
		this.highValue = highValue;
	}

	public float getLowValue() {
		return lowValue;
	}

	public void setLowValue(float lowValue) {
		this.lowValue = lowValue;
	}

	public float getCloseValue() {
		return closeValue;
	}

	public void setCloseValue(float closeValue) {
		this.closeValue = closeValue;
	}

	public float getMA5() {
		return MA5;
	}

	public void setMA5(float mA5) {
		MA5 = mA5;
	}

	public float getMA10() {
		return MA10;
	}

	public void setMA10(float mA10) {
		MA10 = mA10;
	}

	public float getMA20() {
		return MA20;
	}

	public void setMA20(float mA20) {
		MA20 = mA20;
	}

	public float getMA60() {
		return MA60;
	}

	public void setMA60(float mA60) {
		MA60 = mA60;
	}

	public float getColumn() {
		return Column;
	}

	public void setColumn(float column) {
		Column = column;
	}

	public float getVollMA5() {
		return VollMA5;
	}

	public void setVollMA5(float vollMA5) {
		VollMA5 = vollMA5;
	}

	public float getVollMA10() {
		return VollMA10;
	}

	public void setVollMA10(float vollMA10) {
		VollMA10 = vollMA10;
	}

	public float getVollMA20() {
		return VollMA20;
	}

	public void setVollMA20(float vollMA20) {
		VollMA20 = vollMA20;
	}

	public float getDIF() {
		return DIF;
	}

	public void setDIF(float dIF) {
		DIF = dIF;
	}

	public float getDEA() {
		return DEA;
	}

	public void setDEA(float dEA) {
		DEA = dEA;
	}

	public float getBAR() {
		return BAR;
	}

	public void setBAR(float bAR) {
		BAR = bAR;
	}

	public float getK() {
		return K;
	}

	public void setK(float k) {
		K = k;
	}

	public float getD() {
		return D;
	}

	public void setD(float d) {
		D = d;
	}

	public float getJ() {
		return J;
	}

	public void setJ(float j) {
		J = j;
	}

	public float getRSI6() {
		return RSI6;
	}

	public void setRSI6(float rSI6) {
		RSI6 = rSI6;
	}

	public float getRSI12() {
		return RSI12;
	}

	public void setRSI12(float rSI12) {
		RSI12 = rSI12;
	}

	public float getBIAS1() {
		return BIAS1;
	}

	public void setBIAS1(float bIAS1) {
		BIAS1 = bIAS1;
	}

	public float getBIAS2() {
		return BIAS2;
	}

	public void setBIAS2(float bIAS2) {
		BIAS2 = bIAS2;
	}

	public float getBIAS3() {
		return BIAS3;
	}

	public void setBIAS3(float bIAS3) {
		BIAS3 = bIAS3;
	}

	public float getCCI() {
		return CCI;
	}

	public void setCCI(float cCI) {
		CCI = cCI;
	}

	public float getROC() {
		return ROC;
	}

	public void setROC(float rOC) {
		ROC = rOC;
	}

	public float getROCMA() {
		return ROCMA;
	}

	public void setROCMA(float rOCMA) {
		ROCMA = rOCMA;
	}

	public float getASI() {
		return ASI;
	}

	public void setASI(float aSI) {
		ASI = aSI;
	}

	public float getPSY12() {
		return PSY12;
	}

	public void setPSY12(float pSY12) {
		PSY12 = pSY12;
	}

}
