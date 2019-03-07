package com.ez08.compass.entity;

import com.ez08.compass.entity.FenShiHistoryEntity;

import java.util.List;

public class FenShiAllEntity {

	private List<String> instans;
	private List<FenShiHistoryEntity> history;
	private String code;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public List<String> getInstans() {
		return instans;
	}

	public void setInstans(List<String> instans) {
		this.instans = instans;
	}

	public List<FenShiHistoryEntity> getHistory() {
		return history;
	}

	public void setHistory(List<FenShiHistoryEntity> history) {
		this.history = history;
	}

}
