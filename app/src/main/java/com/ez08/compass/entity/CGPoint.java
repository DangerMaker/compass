package com.ez08.compass.entity;

public class CGPoint {
	public float x;
    public float y;

    public CGPoint() {}

    public CGPoint(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public CGPoint(CGPoint src) {
        this.x = src.x;
        this.y = src.y;
    }
    /**
     * 设置点的横坐标和纵坐标
     */
    public void set(int x, int y) {
        this.x = x;
        this.y = y;
    }

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}
    
    
    
}
