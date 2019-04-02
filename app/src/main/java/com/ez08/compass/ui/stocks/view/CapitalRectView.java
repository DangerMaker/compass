package com.ez08.compass.ui.stocks.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.ez08.compass.CompassApp;
import com.ez08.compass.R;

/**
 * Created by Administrator on 2018/9/5.
 */

public class CapitalRectView extends View {

    Context mContext;
    private int mMainColor, textContentColor, redColor, greenColor;
    int width;
    int height;

    RectF redBgRect;
    RectF greenBgRect;
    RectF redRect;
    RectF greenRect;

    Paint redPaint;
    Paint greenPaint;
    Paint grayPaint;

    float textHeight;
    float redTextWidth;
    float redTextX;
    float redTextY;
    String redText;
    float greenTextWidth;
    float greenTextX;
    float greenTextY;
    String greenText;

    int tempW;
    int tempH;

    public CapitalRectView(Context context) {
        super(context);
        init();
    }

    public CapitalRectView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public void init() {
        mContext = getContext();
        TypedArray a = mContext.obtainStyledAttributes(null, R.styleable.main_attrs, 0, 0);
        mMainColor = getResources().getColor(a.getResourceId(R.styleable.main_attrs_main_main_color, 0));
        textContentColor = getResources().getColor(a.getResourceId(R.styleable.main_attrs_lable_item_style, 0));
        redColor = getResources().getColor(a.getResourceId(R.styleable.main_attrs_red_main_color, 0));
        greenColor = getResources().getColor(a.getResourceId(R.styleable.main_attrs_green_main_color, 0));

//        TypedValue outValue = new TypedValue();
//        getResources().getValue(R.dimen.capital_rect_percent_text, outValue, true);
//        float value = outValue.getFloat();

        redPaint = new Paint();
        redPaint.setColor(redColor);
        redPaint.setTextSize(25);
        redPaint.setAntiAlias(true);

        Rect rect = new Rect();
        redPaint.getTextBounds("88.88%", 0, "88.88%".length(), rect);
         tempW = rect.width();//文字宽
         tempH = rect.height();//文字高

        greenPaint = new Paint();
        greenPaint.setColor(greenColor);
        greenPaint.setTextSize(25);
        greenPaint.setAntiAlias(true);

        grayPaint = new Paint();
        grayPaint.setAntiAlias(true);

        if(CompassApp.GLOBAL.THEME_STYLE == 0){
            grayPaint.setColor(Color.parseColor("#F5F5F5"));
        }else{
            grayPaint.setColor(Color.parseColor("#0c1a1d"));

        }

    }

    private void setPosition() {
        //measure
        int cell = width / 8;

        int textSize = 2 * cell * tempH/tempW;
        redPaint.setTextSize(textSize + 5);
        greenPaint.setTextSize(textSize + 5);

        redText = (float) (Math.round(redPercent * 100 * 100)) / 100 + "%";
        greenText = (float) (Math.round(greenPercent * 100 * 100)) / 100 + "%";

        Rect rect = new Rect();
        redPaint.getTextBounds(redText, 0, redText.length(), rect);
        redTextWidth = rect.width();//文字宽
        textHeight = rect.height();//文字高

        greenPaint.getTextBounds(greenText, 0, greenText.length(), rect);
        greenTextWidth = rect.width();

        float startY = textHeight + 5;

        //compute
        redBgRect = new RectF(cell, startY, 3 * cell, height);
        greenBgRect = new RectF(5 * cell, startY, 7 * cell, height);

        redRect = new RectF(cell, startY + (height - startY) * (1 - redPercent), 3 * cell, height);
        greenRect = new RectF(5 * cell, startY + (height - startY) * (1 - greenPercent), 7 * cell, height);

        redTextX = (redRect.right + redRect.left) / 2 - redTextWidth / 2;
        redTextY = redRect.top - 5;

        greenTextX = (greenRect.right + greenRect.left) / 2 - greenTextWidth / 2;
        greenTextY = greenRect.top - 5;

    }

    float redPercent = 0.0f;
    float greenPercent = 1.0f;

    public void setData(double redPercent, double greenPercent) {
        this.setData((float) redPercent, (float) greenPercent);
    }

    public void setData(float redPercent, float greenPercent) {
        this.redPercent = redPercent;
        this.greenPercent = greenPercent;
        invalidate();
    }

    boolean percentVisible = true;

    public void setPercentVisible(boolean visible) {
        this.percentVisible = visible;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = getMeasuredWidth();
        height = getMeasuredHeight();

        Log.e("onMeasure", "");
        setPosition();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //draw background
        canvas.drawRect(redBgRect, grayPaint);
        canvas.drawRect(greenBgRect, grayPaint);

        //draw line
        canvas.drawLine(0,redBgRect.bottom -1,width,redBgRect.bottom - 1,grayPaint);

        //draw percent

        canvas.drawRect(redRect, redPaint);
        canvas.drawRect(greenRect, greenPaint);

        //draw text
        if (percentVisible) {
            canvas.drawText(redText, redTextX, redTextY, redPaint);
            canvas.drawText(greenText, greenTextX, greenTextY, greenPaint);
        }
    }
}
