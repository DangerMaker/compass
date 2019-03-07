package com.ez08.compass.entity;

import java.io.Serializable;

public class ColumnValuesDataModel implements Serializable {
	private int columnColor;//柱色
	private int columnFillColor;//填充色
	private float value;//数值
	
	public ColumnValuesDataModel(int columnColor, int columnFillColor,
			float value) {
		super();
		this.columnColor = columnColor;
		this.columnFillColor = columnFillColor;
		this.value = value;
	}
	public int getColumnColor() {
		return columnColor;
	}
	public void setColumnColor(int columnColor) {
		this.columnColor = columnColor;
	}
	public int getColumnFillColor() {
		return columnFillColor;
	}
	public void setColumnFillColor(int columnFillColor) {
		this.columnFillColor = columnFillColor;
	}
	public float getValue() {
		return value;
	}
	public void setValue(float value) {
		this.value = value;
	}
	
	
}
