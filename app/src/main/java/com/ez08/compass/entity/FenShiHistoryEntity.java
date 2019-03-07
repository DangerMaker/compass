package com.ez08.compass.entity;

public class FenShiHistoryEntity {

	private double value; //价
	private long column;  //总量
	private double turn;  //总额
	private int date;

	public int getDate() {
		return date;
	}

	public void setDate(int date) {
		this.date = date;
	}

	public double getValue() {
		return value;
	}
	public void setValue(double value) {
		this.value = value;
	}
	public long getColumn() {
		return column;
	}
	public void setColumn(long column) {
		this.column = column;
	}
	public double getTurn() {
		return turn;
	}
	public void setTurn(double turn) {
		this.turn = turn;
	}
	
}
