package com.ez08.compass.entity;

import java.io.Serializable;

public class InitEntity implements Serializable {

	private String server; // socket服务器的ip:port;
	private String imageupload; // 图片上传服务器地址
	private String url;
	private String date;
	private String version;
	private String info;
	private String adurl;
	private String adurl2;

    public String getAdurl2() {
        return adurl2;
    }

    public void setAdurl2(String adurl2) {
        this.adurl2 = adurl2;
    }

    public String getAdurl() {
		return adurl;
	}

	public void setAdurl(String adurl) {
		this.adurl = adurl;
	}

	public String getServer() {
		return server;
	}

	public void setServer(String server) {
		this.server = server;
	}

	public String getImageupload() {
		return imageupload;
	}

	public void setImageupload(String imageupload) {
		this.imageupload = imageupload;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

}
