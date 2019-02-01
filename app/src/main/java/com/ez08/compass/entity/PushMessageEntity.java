package com.ez08.compass.entity;

import java.io.Serializable;

public class PushMessageEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	private boolean ifhasfind;//消息是否已查看
	private String pushid;//消息id
	private String title;//推送标题
	private String description;//推送描述
	private String imgurl;//推送图片地址
	private String uri;//推送目的
	private String pushtype;//推送类型
	private String usertype;//推送用户类型
	private String time;//推送时间
	private String starttime;//推送开始时间
	private String endtime;//推送结束时间
	private String receivertime;//消息接收到的时间
	
	private boolean showCheckBox;//是否展示复选框
    private Boolean isSelected;//CheckBox是否被选中
    
    private String stockCode;
	private String action;
	private String url;
	private String shareUrl;
	private int imgpos;

	public int getImgpos() {
		return imgpos;
	}

	public void setImgpos(int imgpos) {
		this.imgpos = imgpos;
	}

	public String getShareUrl() {
		return shareUrl;
	}

	public void setShareUrl(String shareUrl) {
		this.shareUrl = shareUrl;
	}
	
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	public String getStockCode() {
		return stockCode;
	}

	public void setStockCode(String stockCode) {
		this.stockCode = stockCode;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}
    
	public boolean ishasfind() {
		return ifhasfind;
	}
	public void setIshasfind(boolean ifhasfind) {
		this.ifhasfind = ifhasfind;
	}
	public String getPushid() {
		return pushid;
	}
	public void setPushid(String pushid) {
		this.pushid = pushid;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getImgurl() {
		return imgurl;
	}
	public void setImgurl(String imgurl) {
		this.imgurl = imgurl;
	}
	public String getUri() {
		return uri;
	}
	public void setUri(String uri) {
		this.uri = uri;
	}
	public String getPushtype() {
		return pushtype;
	}
	public void setPushtype(String pushtype) {
		this.pushtype = pushtype;
	}
	public String getUsertype() {
		return usertype;
	}
	public void setUsertype(String usertype) {
		this.usertype = usertype;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getStarttime() {
		return starttime;
	}
	public void setStarttime(String starttime) {
		this.starttime = starttime;
	}
	public String getEndtime() {
		return endtime;
	}
	public void setEndtime(String endtime) {
		this.endtime = endtime;
	}
	public String getReceivertime() {
		return receivertime;
	}
	public void setReceivertime(String receivertime) {
		this.receivertime = receivertime;
	}
	public  boolean isShowCheckBox() {
		return showCheckBox;
	}
	public  void setShowCheckBox(boolean showCheckBox) {
		this.showCheckBox = showCheckBox;
	}
	public Boolean getIsSelected() {
		return isSelected;
	}
	public  void setIsSelected(Boolean isSelected) {
		this.isSelected = isSelected;
	}
	
	
}
