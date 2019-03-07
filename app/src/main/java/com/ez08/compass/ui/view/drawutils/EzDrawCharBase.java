package com.ez08.compass.ui.view.drawutils;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;

import com.ez08.compass.entity.CGPoint;

/**
 * 画图工具类基类
 * @author dingwei
 *
 */
public class EzDrawCharBase {
	public static final boolean DEFAULT_DISPLAY_CANVAS_BACKGROUND = Boolean.FALSE;//默认是否显示画布背景
	public static final int DEFAULT_BACKGROUND_COLOR = Color.WHITE;//默认背景色
	public static final boolean DEFAULT_DISPLAY_BORDER = Boolean.FALSE;//默认是否显示边框
	public static final int DEFAULT_BORDER_COLOR = Color.GRAY; //默认边框颜色
	public static final float DEFAULT_BORDER_WIDTH = 1f;//默认边框宽度
	public static final float DEFAULT_TITLE_WIDTH = 12f;//默认标题大小
	public static final int DEFAULT_TITLE_COLOR = Color.BLACK;//默认标题颜色
	public static final CGPoint DEFAULT_TITLE_POSITION = new CGPoint(0,0);//默认标题位置
	
	protected boolean displayCanvasBackground=DEFAULT_DISPLAY_CANVAS_BACKGROUND;//是否显示画布背景
	protected int canvasBackgroundColor=DEFAULT_BACKGROUND_COLOR;//画布背景色
	protected CGPoint originPoint;//坐标原点
	protected float height;//画布高
	protected float width;//画布宽
	protected boolean displayBorder = DEFAULT_DISPLAY_BORDER;//是否显示边框
	protected int borderLineColor = DEFAULT_BORDER_COLOR;//边框颜色
	protected float borderLineWidth = DEFAULT_BORDER_WIDTH;//边框线宽
	protected double scale = 1;//高度比例
	protected String title;//标题内容
	protected int titleColor=DEFAULT_TITLE_COLOR;//标题颜色
	protected float textSize=DEFAULT_TITLE_WIDTH;//标题大小
	protected CGPoint titlePosition=DEFAULT_TITLE_POSITION;//标题位置
	
	protected double heightScale;
	protected double mLowestData;
	/**
	 * 最大值
	 */
	protected double mHighestData;
	/**
	 * 画图方法
	 * @param canvas 画布
	 * @param position 画布位置(左顶点坐标）
	 */
	
	public void draw(Canvas canvas, CGPoint position){

		if(position==null){
			return;
		}
		if(displayCanvasBackground){
//			canvas.drawColor(canvasBackgroundColor);
		}
		if(displayBorder){
			Paint mPaint = new Paint();
			mPaint.setAntiAlias(true);
			mPaint.setColor(borderLineColor);
			mPaint.setStrokeWidth(borderLineWidth);
			mPaint.setStyle(Style.STROKE);
			//画一个矩形
			canvas.drawRect(position.getX(), position.getY(), position.getX()+width, position.getY()+height, mPaint);
		}
		if(title!=null&&title.length()!=0){
			Paint textPaint=new Paint();
			textPaint.setColor(titleColor);
			textPaint.setTextSize(textSize);
			canvas.drawText(title, titlePosition.getX(), titlePosition.getY(), textPaint);
		}
		
	}
	
	/**
	 * 是否显示画布背景
	 * @return
	 */
	public boolean isDisplayCanvasBackground() {
		return displayCanvasBackground;
	}
	/**
	 * 设置是否显示画布背景
	 * @param displayCanvasBackground
	 */
	public void setDisplayCanvasBackground(boolean displayCanvasBackground) {
		this.displayCanvasBackground = displayCanvasBackground;
	}
	
	/**
	 * 获取画布背景色
	 * @return
	 */

	public int getCanvasBackgroundColor() {
		return canvasBackgroundColor;
	}
	/**
	 * 设置画布背景色
	 * @param canvasBackgroundColor
	 */
	public void setCanvasBackgroundColor(int canvasBackgroundColor) {
		this.canvasBackgroundColor = canvasBackgroundColor;
	}
	/**
	 * 获取坐标原点
	 * @return
	 */
	public CGPoint getOriginPoint() {
		return originPoint;
	}
	/**
	 *  设置坐标原点
	 * @param originPoint
	 */
	public void setOriginPoint(CGPoint originPoint) {
		this.originPoint = originPoint;
	}
	
	/**
	 * 是否显示边框
	 * @return
	 */
	public boolean isDisplayBorder() {
		return displayBorder;
	}
	/**
	 * 设置是否显示边框
	 * @param displayBorder
	 */
	public void setDisplayBorder(boolean displayBorder) {
		this.displayBorder = displayBorder;
	}
	/**
	 * 获取边框颜色
	 * @return
	 */
	public int getBorderLineColor() {
		return borderLineColor;
	}
	/**
	 * 设置边框颜色
	 * @param borderLineColor
	 */
	public void setBorderLineColor(int borderLineColor) {
		this.borderLineColor = borderLineColor;
	}
	/**
	 * 获取边框宽度
	 * @return
	 */
	public float getBorderLineWidth() {
		return borderLineWidth;
	}
	/**
	 *设置边框宽度
	 * @param borderLineWidth
	 */
	public void setBorderLineWidth(float borderLineWidth) {
		this.borderLineWidth = borderLineWidth;
	}
	/**
	 * 获取缩放比例
	 * @return
	 */
	public double getScale() {
		return scale;
	}
	/**
	 * 设置缩放比例
	 * @param scale
	 */
	public void setScale(double scale) {
		this.scale = scale;
	}

	
	
	
	/**
	 * 获取标题内容
	 * @return
	 */
	public String getTitle() {
		return title;
	}
	
	/**
	 * 设置标题内容
	 * @param title
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	
	/**
	 * 获取标题颜色
	 * @return
	 */
	public int getTitleColor() {
		return titleColor;
	}
	
	/**
	 * 设置标题颜色
	 * @param titleColor
	 */
	public void setTitleColor(int titleColor) {
		this.titleColor = titleColor;
	}
	
	/**
	 * 获取标题大小
	 * @return
	 */
	public float getTextSize() {
		return textSize;
	}
	
	/**
	 * 设置标题大小
	 * @param textSize
	 */
	public void setTextSize(float textSize) {
		this.textSize = textSize;
	}
	
	/**
	 * 获取标题位置
	 * @return
	 */
	public CGPoint getTitlePosition() {
		return titlePosition;
	}
	
	/**
	 * 设置标题位置
	 * @param titlePosition
	 */
	public void setTitlePosition(CGPoint titlePosition) {
		this.titlePosition = titlePosition;
	}
	
	/**
	 * 获取画布高度
	 * @return
	 */
	public float getHeight() {
		return height;
	}
	/**
	 * 设置画布高度
	 * @param height
	 */
	public void setHeight(float height) {
		this.height = height;
	}
	
	/**
	 * 获取画布宽度
	 * @return
	 */
	public float getWidth() {
		return width;
	}
	
	/**
	 * 设置画布宽度
	 * @param width
	 */
	public void setWidth(float width) {
		this.width = width;
	}

	public double getHeightScale() {
		return heightScale;
	}

	public void setHeightScale(double heightScale) {
		this.heightScale = heightScale;
	}

	public double getmLowestData() {
		return mLowestData;
	}

	public void setmLowestData(double mLowestData) {
		this.mLowestData = mLowestData;
	}
	public double getmHighestData() {
		return mHighestData;
	}

	public void setmHighestData(double mHighestData) {
		this.mHighestData = mHighestData;
	}
	
	
}
