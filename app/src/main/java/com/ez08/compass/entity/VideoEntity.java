package com.ez08.compass.entity;

import java.io.Serializable;

public class VideoEntity implements Serializable {
	private String id; // id
	private String content; // 内容
	private String imageid; // 图片id
	private String title; // 标题
	private String url; // 地址
	private long time; // 发布时间
	private int type;

	private String category;

	public int getType(){
		return type;
	}
	public void setType(int mType){
		type = mType;
	}
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getImageid() {
		return imageid;
	}

	public void setImageid(String imageid) {
		this.imageid = imageid;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

}
