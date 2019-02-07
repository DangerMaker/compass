package com.ez08.compass.entity;

import java.io.Serializable;

public class ClassEntity implements Serializable {

	private String id;
	private String professor;
	private String title;
	private long timemillis;
	private long endtime;
	private boolean isfollow;
	private int type;
	private String content;
	private String imageid;
	private String url;

	private String roomid;

	private boolean isFirst;
	private String roomName;
	
	public String getRoomName() {
		return roomName;
	}

	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}

	public boolean isFirst() {
		return isFirst;
	}

	public void setFirst(boolean isFirst) {
		this.isFirst = isFirst;
	}

	public String getRoomid() {
		return roomid;
	}

	public void setRoomid(String roomid) {
		this.roomid = roomid;
	}

	public long getEndtime() {
		return endtime;
	}

	public void setEndtime(long endtime) {
		this.endtime = endtime;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
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

	public long getTimemillis() {
		return timemillis;
	}

	public void setTimemillis(long timemillis) {
		this.timemillis = timemillis;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getProfessor() {
		return professor;
	}

	public void setProfessor(String professor) {
		this.professor = professor;
	}

	public boolean isIsfollow() {
		return isfollow;
	}

	public void setIsfollow(boolean isfollow) {
		this.isfollow = isfollow;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

}
