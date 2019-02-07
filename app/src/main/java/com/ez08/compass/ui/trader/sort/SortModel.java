package com.ez08.compass.ui.trader.sort;

import java.io.Serializable;

public class SortModel implements Serializable {

	private String name;   //显示的数据
	private String sortLetters;  //显示数据拼音的首字母
	private String appName;	//下载安装包的名称
	private String packageName;	//包名
	private String url;	//下载地址
	private boolean hasAdd=false;

	public boolean isHasAdd() {
		return hasAdd;
	}

	public void setHasAdd(boolean hasAdd) {
		this.hasAdd = hasAdd;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSortLetters() {
		return sortLetters;
	}
	public void setSortLetters(String sortLetters) {
		this.sortLetters = sortLetters;
	}
}
