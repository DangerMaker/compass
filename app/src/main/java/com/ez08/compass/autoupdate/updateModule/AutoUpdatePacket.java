package com.ez08.compass.autoupdate.updateModule;

import java.io.Serializable;

public class AutoUpdatePacket implements Serializable {

	public String getAppuid() {
		return appuid;
	}

	public void setAppuid(String appuid) {
		this.appuid = appuid;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getWeburl() {
		return weburl;
	}

	public void setWeburl(String weburl) {
		this.weburl = weburl;
	}

	public String getCaburl() {
		return caburl;
	}

	public void setCaburl(String caburl) {
		this.caburl = caburl;
	}

	public String getBrief() {
		return brief;
	}

	public void setBrief(String brief) {
		this.brief = brief;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getTver() {
		return tver;
	}

	public void setTver(String tver) {
		this.tver = tver;
	}

	private String appuid;
	private String appName;
	private String weburl;
	private String caburl;
	private String brief;
	private String tver;
	private long time;
	private int type;// 0可选更新 1强制更新
}
