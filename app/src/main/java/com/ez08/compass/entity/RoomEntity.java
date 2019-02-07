package com.ez08.compass.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class RoomEntity implements Serializable {
	private String id; // 房间id
	private String name; // 房间名称
	private String imageid; // 房间图片url
	private int type;
	List<ClassEntity> list;

	public String infoUrl;

	private boolean noLiving=false;
	
	public boolean isNoLiving() {
		return noLiving;
	}

	public void setNoLiving(boolean noLiving) {
		this.noLiving = noLiving;
	}

	public List<ClassEntity> getList() {
		return list;
	}

	public void setList(ClassEntity entity) {
		if (list == null) {
			list = new ArrayList<ClassEntity>();
		}
		if (entity != null) {
			list.add(entity);
		}
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getImageid() {
		return imageid;
	}

	public void setImageid(String imageid) {
		this.imageid = imageid;
	}

}
