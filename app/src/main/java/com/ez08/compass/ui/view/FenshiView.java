package com.ez08.compass.ui.view;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PathEffect;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.ez08.compass.CompassApp;
import com.ez08.compass.R;
import com.ez08.compass.entity.CGPoint;
import com.ez08.compass.entity.ColumnValuesDataModel;
import com.ez08.compass.entity.FenShiDesEntity;
import com.ez08.compass.entity.StockDetailEntity;
import com.ez08.compass.tools.MathUtils;
import com.ez08.compass.tools.UtilTools;
import com.ez08.compass.ui.view.drawutils.ChartHelper;
import com.ez08.compass.ui.view.drawutils.EzBarChart;
import com.ez08.compass.ui.view.drawutils.EzFixedGapLine;
import com.ez08.compass.ui.view.drawutils.EzFixedGapLine2;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

public class FenShiView extends View {

    private Context mContext;
    private EzBarChart ezbarChart;
    private EzFixedGapLine2 realTimePriceLine2;
    private EzFixedGapLine averageLine;

    private int redColor;
    private int greenColor;

    double columnHeightScale;
    float columnHighestData = 0;
    double heightScale;
    private float mMaxValue;
    private float mMinValue;
    private float lastClosePrice;
    private int width, height;
    private int decm;
    private float[][] uprightGroup;
    private float priceFontSize = 20;
    private String span;
    private String mTurnDes = "";
    private float scrollOrignX = 0; //
    private float scrollX = 0; //
    private float realScrollX = 0;
    private float scrollY = 0; //
    private boolean canRefresh = false;
    private float topRectHeight;
    private double interval = 0.0;
    private float mDesX = 0; //
    private int mDesIndex; //
    private float widthAverage;
    private float fenshiCurrentMaxWidth = 0;    //
    RectF rectF;
    RectF drRealRectF;

    private Paint redPaint; //
    private Paint greenPaint; //
    private Paint loadPaint; //
    private Paint realTimePricePaint;
    private Paint timeScalePaint;
    private Paint desPaint;
    private Paint rectPaint;
    private Paint massPaint;
    private Paint scrollPaint;
    private Paint numPaint;
    private Paint cursorPaint;
    private Paint blackPaint;
    private Paint shiziPaint;
    private boolean isScrollDetail = false;
    private Paint dashedPaint;

    private List<Float> value;
    private List<FenShiDesEntity> mDesEntityList;

    Bitmap bitmapScale;
    boolean isDataIn = false;

    public FenShiView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        mContext = context;
        init();
    }

    public void setDesEntityList(List<FenShiDesEntity> mDesEntityList) {
        this.mDesEntityList = mDesEntityList;
        String beginTime = "201702270930";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
        SimpleDateFormat sf2 = new SimpleDateFormat("yyyy-MM-dd-HH:mm");
        long lSeconds = 0;
        try {
            lSeconds = sdf.parse(beginTime).getTime();//毫秒
        } catch (ParseException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < mDesEntityList.size(); i++) {
            lSeconds = lSeconds + 60 * 1000;
            String time = sf2.format(lSeconds).substring(11);
            if (time.equals("11:30")) {
                lSeconds = lSeconds + 90 * 60 * 1000;
            }
            mDesEntityList.get(i).setTime(time);
        }
        isDataIn = true;
    }

    public void setBarData(List<ColumnValuesDataModel> list) {
        ezbarChart.setValues(list);
        for (int i = 0; i < list.size(); i++) {
            float max = list.get(i).getValue();
            if (columnHighestData < max) {
                columnHighestData = max;
            }
        }
        columnHeightScale = (height / 4) / columnHighestData;
        widthAverage = width / 240f;
        ezbarChart.setGapWidth(3 * widthAverage / 10);
        ezbarChart.setColumnWidth(7 * widthAverage / 10);
        ezbarChart.setHeightScale(columnHeightScale);
        ezbarChart.setmHighestData(columnHighestData);

        CGPoint mColumnposition = new CGPoint(1, height);
        ezbarChart.setOriginPoint(mColumnposition);
    }


    public void setRealTimePriceData(List<Float> list) {
        if (list == null) {
            return;
        }
        value = list;
        realTimePriceLine2.setLinesData(value);
        interval = width / 239f;
        fenshiCurrentMaxWidth = (float) (interval * (list.size() - 1));
        heightScale = 4 * height / ((mMaxValue - mMinValue) * 6);
    }


    public void setTurnDes(String mTurnDes) {
        this.mTurnDes = mTurnDes;
    }

    public void setAverageLineData(List<Float> list) {
        if (list == null || averageLine == null) {
            return;
        }
        if (list.size() - 1 > 0) {
            averageLine.setInterval(interval);
            averageLine.setLineWidth(3f);
            averageLine.setHeightScale(heightScale);
            averageLine.setmHighestData(mMaxValue);
            averageLine.setValues(list);
        }
    }


    public void setPrice(StockDetailEntity entity) {
        this.lastClosePrice = MathUtils.getPriceInt2Float(entity.getLastclose(), entity.getDecm());
        float mTempMaxValue = MathUtils.getPriceInt2Float(entity.getHigh(), entity.getDecm());
        float mTempMinValue = MathUtils.getPriceInt2Float(entity.getLow(), entity.getDecm());
        decm = entity.getDecm();

        float cha = mTempMaxValue - this.lastClosePrice;
        float upSpan = Math.abs(cha);
        float downSpan = Math.abs(this.lastClosePrice - mTempMinValue);
        float tmpSpan = Math.max(upSpan, downSpan);
        span = MathUtils.formatNum(tmpSpan * 100f / lastClosePrice, 4);

        this.mMaxValue = lastClosePrice + tmpSpan;
        this.mMinValue = lastClosePrice - tmpSpan;
        realTimePriceLine2.setMaxValue(this.mMaxValue);
        realTimePriceLine2.setMinValue(this.mMinValue);
    }



    public float getTopRectHeight() {
        return topRectHeight;
    }

    private boolean scalePress() {
        if (drRealRectF.contains(scrollX, scrollY)) {
            getContext().sendBroadcast(new Intent("KScale"));
            return true;
        }
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!isDataIn) {
            return false;
        }

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                CompassApp.GLOBAL.isTouchFenshiView = true;
                // ---初始化原始点和滑动的位置
                scrollOrignX = event.getX();
                //跟据间隔自动调整x
                scrollX = event.getX();
                scrollY = event.getY();

                if (isVer && scalePress()) {
                    return false;
                }

                if (isScrollDetail) {
                    if (scrollOrignX != 0) {
                        for (int i = 0; i < realTimePriceLine2.getValues().size(); i++) {
                            mDesX = (float) (interval * i);
                            if (mDesX >= scrollX) {
                                scrollX = mDesX;
                                mDesIndex = i;
                                break;
                            }
                        }

                        FenShiDesEntity entity = mDesEntityList.get(mDesIndex);
                        if (isVer) {
                            Intent intent = new Intent("lock_move");
                            intent.putExtra("type", 0);
                            intent.putExtra("time", entity.getTime());
                            intent.putExtra("price", entity.getValue());
                            intent.putExtra("average", entity.getValueAverage());
                            intent.putExtra("rate", entity.getValueRate());
                            intent.putExtra("isHigh", entity.isHigh());
                            getContext().sendBroadcast(intent);
                        } else {
                            Message message = Message.obtain();
                            message.what = 3;
                            message.obj = entity;
                            handler.sendMessage(message);
                        }

                    }
                }

                break;
            case MotionEvent.ACTION_MOVE:
                Log.e("FenshiView", "ACTION_MOVE");
                CompassApp.GLOBAL.isTouchFenshiView = true;
                scrollX = event.getX();
                scrollY = event.getY();

                if (isScrollDetail) {
                    if (scrollX >= fenshiCurrentMaxWidth) {    //到达临界值
                        scrollX = mDesX = fenshiCurrentMaxWidth;
                        mDesIndex = realTimePriceLine2.getValues().size() - 1;
                    } else {
                        for (int i = 0; i < realTimePriceLine2.getValues().size(); i++) {
                            mDesX = (float) (interval * i);
                            if (mDesX >= scrollX) {
                                scrollX = mDesX;
                                mDesIndex = i;
                                break;
                            }
                        }
                    }

                    FenShiDesEntity entity = mDesEntityList.get(mDesIndex);
                    if (isVer) {
                        Intent intent1 = new Intent("lock_move");
                        //type 分时 0 kline 1 mline 2
                        intent1.putExtra("type", 0);
                        intent1.putExtra("time", entity.getTime());
                        intent1.putExtra("price", entity.getValue());
                        intent1.putExtra("average", entity.getValueAverage());
                        intent1.putExtra("rate", entity.getValueRate());
                        intent1.putExtra("isHigh", entity.isHigh());
                        getContext().sendBroadcast(intent1);
                    } else {
                        Message message = Message.obtain();
                        message.what = 3;
                        message.obj = entity;
                        handler.sendMessage(message);
                    }

                    canRefresh = true;
                    invalidate();

                    if (isScrollDetail) {
                        removeCallbacks(goneRunnable);
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                Log.e("FenshiView", "ACTION_UP");
                realScrollX = event.getX();
                CompassApp.GLOBAL.isTouchFenshiView = false;
                // --------点击事件消费
                if (scrollOrignX != 0) {
                    if (Math.abs(scrollOrignX - realScrollX) < 10 && mDesEntityList.size() > 0) {
                        Log.e("FenshiView", "ACTION_UP1");
                        isScrollDetail = !isScrollDetail;
                        CompassApp.GLOBAL.isScroll = isScrollDetail;


                        if (scrollX >= fenshiCurrentMaxWidth) {    //到达临界值
                            scrollX = mDesX = fenshiCurrentMaxWidth;
                            mDesIndex = realTimePriceLine2.getValues().size() - 1;
                        } else {
                            for (int i = 0; i < realTimePriceLine2.getValues().size(); i++) {
                                mDesX = (float) (interval * i);
                                if (mDesX >= scrollX) {
                                    scrollX = mDesX;
                                    mDesIndex = i;
                                    break;
                                }
                            }
                        }

                        FenShiDesEntity entity = mDesEntityList.get(mDesIndex);
                        if (isVer) {
                            Intent intent = new Intent("lock_move");
                            intent.putExtra("type", 0);
                            intent.putExtra("time", entity.getTime());
                            intent.putExtra("price", entity.getValue());
                            intent.putExtra("average", entity.getValueAverage());
                            intent.putExtra("rate", entity.getValueRate());
                            intent.putExtra("isHigh", entity.isHigh());
                            getContext().sendBroadcast(intent);
                        } else {
                            Message message = Message.obtain();
                            message.what = 3;
                            message.obj = entity;
                            handler.sendMessage(message);
                        }

                        if (isScrollDetail) {
                            shiziVisible();
                            //3秒消失
                        } else {
                            shiziGone();
                        }
                        canRefresh = true;
                        invalidate();
                    }
                }

                if (isScrollDetail) {
                    postDelayed(goneRunnable, 3000);
                }

                break;
        }
        return true;

    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        removeCallbacks(goneRunnable);
    }

    Runnable goneRunnable = new Runnable() {
        @Override
        public void run() {
            if (isScrollDetail) {
                Log.e("goneRunnable", "gone");
                isScrollDetail = false;
                CompassApp.GLOBAL.isScroll = isScrollDetail;
                shiziGone();
                canRefresh = true;
                invalidate();
            }
        }
    };

    private void shiziVisible() {
        if (isVer) {
            Intent intent2 = new Intent("lock_scroll");
            intent2.putExtra("type", 0);
            intent2.putExtra("locked", true);
            getContext().sendBroadcast(intent2);
        } else {
            handler.sendEmptyMessage(2);
        }
    }

    private void shiziGone() {
        if (isVer) {
            Intent intent2 = new Intent("lock_scroll");
            intent2.putExtra("type", 0);
            intent2.putExtra("locked", false);
            getContext().sendBroadcast(intent2);
        } else {
            handler.sendEmptyMessage(1);
        }
    }


    public boolean isCanRefresh() {
        return canRefresh;
    }

    public void setCanRefresh(boolean canRefresh) {
        this.canRefresh = canRefresh;
        invalidate();
    }

    /**
     * 初始化数据
     *
     * @param
     * @param height
     */
    public void initData(int lWidth, int height) {
        if (lWidth <= 0) {
            return;
        }
        this.width = lWidth;
        this.height = height;
        ezbarChart.setWidth(width);
        ezbarChart.setHeight(height);

        uprightGroup[0][0] = 0;
        uprightGroup[0][1] = 0;
        uprightGroup[0][2] = width;
        uprightGroup[0][3] = 4 * height / 6;

        uprightGroup[1][0] = 0;
        uprightGroup[1][1] = height / 12 + 4 * height / 6;
        uprightGroup[1][2] = width;
        uprightGroup[1][3] = height;

        if (CompassApp.GLOBAL.THEME_STYLE == 0) {
            realTimePriceLine2.setLineColor(getResources()
                    .getColor(R.color.darkBlue));
        } else {
            realTimePriceLine2.setLineColor(getResources()
                    .getColor(R.color.fenshi_line_night));
        }

        realTimePriceLine2.setWidth(uprightGroup[0][2]);
        realTimePriceLine2.setHeight(uprightGroup[0][3]);
        realTimePriceLine2.setDisplayNumber(240);
        realTimePriceLine2.setLineWidth(3f);
        realTimePriceLine2.setDisplayUnderLineShadowColor(Boolean.TRUE);

        if (CompassApp.GLOBAL.THEME_STYLE == 0) {
            averageLine.setLineColor(getResources().getColor(R.color.orange));
        } else {
            averageLine.setLineColor(getResources().getColor(R.color.average_line_night));
        }

        averageLine.setDisplayBorder(false);
        averageLine.setDisplayCanvasBackground(false);
        topRectHeight = uprightGroup[0][3];
        averageLine.setOffsetX(0);
        averageLine.setWidth(uprightGroup[0][2]);
        averageLine.setHeight(uprightGroup[0][3]);
        averageLine.setOriginPoint(new CGPoint(0, 0));

        rectF = new RectF(uprightGroup[0][2] - UtilTools.dip2px(mContext, 22) - 10,
                uprightGroup[0][3]
                        - UtilTools.dip2px(mContext, 22) - 20 - ChartHelper.getFontHeight(priceFontSize) / 2,
                uprightGroup[0][2] - 10,
                uprightGroup[0][3]
                        - 20 - ChartHelper.getFontHeight(priceFontSize) / 2);
        drRealRectF = new RectF(rectF.left - 20, rectF.top - 20,
                rectF.right + 20, rectF.bottom + 20);

    }


    private void init() {
        TypedArray a = mContext.obtainStyledAttributes(null, R.styleable.main_attrs, 0, 0);
        redColor = getResources().getColor(a.getResourceId(R.styleable.main_attrs_red_main_color, 0));
        greenColor = getResources().getColor(a.getResourceId(R.styleable.main_attrs_green_main_color, 0));

        redPaint = new Paint();
        redPaint.setTextSize(UtilTools.dip2px(mContext, 10));
        redPaint.setAntiAlias(true);
        redPaint.setColor(redColor);

        greenPaint = new Paint();
        greenPaint.setTextSize(UtilTools.dip2px(mContext, 10));
        greenPaint.setAntiAlias(true);
        greenPaint.setColor(greenColor);

        numPaint = new Paint();
        numPaint.setTextSize(UtilTools.dip2px(mContext, 10));
        numPaint.setAntiAlias(true);
        numPaint.setColor(getResources().getColor(R.color.shadow0));

        scrollPaint = new Paint();
        if (CompassApp.GLOBAL.THEME_STYLE == 0) {
            scrollPaint.setColor(mContext.getResources().getColor(R.color.black));
        } else {
            scrollPaint.setColor(mContext.getResources().getColor(R.color.stock_equal_white));
        }

        bitmapScale = BitmapFactory.decodeResource(getResources(), R.drawable.switchover);
        uprightGroup = new float[2][4];

        ezbarChart = new EzBarChart();
        ezbarChart.setDisplayBorder(false);
        ezbarChart.setDisplayCanvasBackground(false);
        ezbarChart.setOffsetX(0);

        rectPaint = new Paint();
        if (CompassApp.GLOBAL.THEME_STYLE == 0) {
            rectPaint.setColor(getResources().getColor(R.color.shadow1));
        } else {
            rectPaint.setColor(getResources().getColor(R.color.shadow_line_new));

        }
        rectPaint.setStyle(Style.STROKE);
        rectPaint.setStrokeWidth(3);

        timeScalePaint = new Paint();
        timeScalePaint.setColor(getResources().getColor(R.color.gray));
        timeScalePaint.setTextSize(UtilTools.dip2px(mContext, 10));
        timeScalePaint.setAntiAlias(true);

        desPaint = new Paint();
        desPaint.setColor(getResources().getColor(R.color.gray));
        desPaint.setTextSize(priceFontSize);
        desPaint.setAntiAlias(true);
        desPaint.setTextSize(UtilTools.dip2px(mContext, 10));

        realTimePricePaint = new Paint();
        realTimePricePaint.setColor(getResources().getColor(R.color.shadow0));

        cursorPaint = new Paint();
        cursorPaint.setColor(Color.parseColor("#147EFE"));
        cursorPaint.setStyle(Style.FILL);

        blackPaint = new Paint();
        blackPaint.setColor(Color.WHITE);
        blackPaint.setStrokeWidth(2);
        blackPaint.setTextSize(UtilTools.dip2px(mContext, 10));


        shiziPaint = new Paint();
        if (CompassApp.GLOBAL.THEME_STYLE == 0) {
            shiziPaint.setColor(Color.BLACK);
        } else {
            shiziPaint.setColor(Color.WHITE);
        }

        massPaint = new Paint();
        massPaint.setStrokeWidth(3);
        if (CompassApp.GLOBAL.THEME_STYLE == 0) {
            massPaint.setColor(getResources().getColor(R.color.shadow1));
        } else {
            massPaint.setColor(getResources().getColor(R.color.shadow_line_new));

        }

        loadPaint = new Paint();
        loadPaint.setTextSize(50);
        loadPaint.setColor(mContext.getResources().getColor(R.color.gray));

        dashedPaint = new Paint();
        dashedPaint.setStrokeWidth(3);
        if (CompassApp.GLOBAL.THEME_STYLE == 0) {
            dashedPaint.setColor(getResources().getColor(R.color.shadow1));
        } else {
            dashedPaint.setColor(getResources().getColor(R.color.shadow_line_new));

        }
        PathEffect effects = new DashPathEffect(new float[]{5, 5, 5, 5}, 1);
        dashedPaint.setStyle(Paint.Style.STROKE);
        dashedPaint.setPathEffect(effects);
        dashedPaint.setAntiAlias(true);
        realTimePriceLine2 = new EzFixedGapLine2();
        averageLine = new EzFixedGapLine();

    }

    @Override
    protected void onDraw(Canvas canvas) {

        super.onDraw(canvas);
        if (canRefresh) {
            canvas.drawRect(uprightGroup[0][0], uprightGroup[0][1],
                    uprightGroup[0][2], uprightGroup[0][3], rectPaint);
            canvas.drawRect(uprightGroup[1][0], uprightGroup[1][1],
                    uprightGroup[1][2], uprightGroup[1][3], rectPaint);
            timeScalePaint.setColor(getResources().getColor(R.color.gray));
            canvas.drawText(mTurnDes, uprightGroup[1][0], uprightGroup[1][1]
                            + ChartHelper.getFontHeight(priceFontSize) / 2 + UtilTools.dip2px(mContext, 4),
                    timeScalePaint);
            // 绘制竖线
            canvas.drawLine(width / 4, uprightGroup[0][1], width / 4,
                    uprightGroup[0][3], massPaint);
            canvas.drawLine(width / 2, uprightGroup[0][1], width / 2,
                    uprightGroup[0][3], massPaint);
            canvas.drawLine(width / 2 + width / 4, uprightGroup[0][1], width
                    / 2 + width / 4, uprightGroup[0][3], massPaint);
            //底部竖线
            canvas.drawLine(width / 4, uprightGroup[0][3] + height / 12, width / 4,
                    height, massPaint);
            canvas.drawLine(width / 2, uprightGroup[0][3] + height / 12, width / 2,
                    height, massPaint);
            canvas.drawLine(width / 2 + width / 4, uprightGroup[0][3] + height / 12, width
                    / 2 + width / 4, height, massPaint);
            // 绘制横线
            canvas.drawLine(uprightGroup[0][0], uprightGroup[0][3] / 4,
                    uprightGroup[0][2], uprightGroup[0][3] / 4, massPaint);
            canvas.drawLine(uprightGroup[0][0], uprightGroup[0][3] / 2,
                    uprightGroup[0][2], uprightGroup[0][3] / 2, dashedPaint);
            canvas.drawLine(uprightGroup[0][0], uprightGroup[0][3] / 2
                            + uprightGroup[0][3] / 4, uprightGroup[0][2],
                    uprightGroup[0][3] / 2 + uprightGroup[0][3] / 4, massPaint);
            // 绘制价格刻度
            timeScalePaint.setColor(redColor);
            float lVari;
            canvas.drawText(MathUtils.formatNum(mMaxValue, decm), uprightGroup[0][0],
                    ChartHelper.getFontHeight(priceFontSize) / 2 + UtilTools.dip2px(mContext, 4),
                    timeScalePaint);
            String des1 = span + "%";
            lVari = timeScalePaint.measureText(des1);
            canvas.drawText(
                    des1,
                    uprightGroup[0][2] - lVari,
                    uprightGroup[0][1]
                            + ChartHelper.getFontHeight(priceFontSize) / 2 + UtilTools.dip2px(mContext, 4),
                    timeScalePaint);
            timeScalePaint.setColor(getResources().getColor(R.color.gray));
            canvas.drawText(
                    lastClosePrice + "",
                    uprightGroup[0][0],
                    uprightGroup[0][3] / 2
                            + ChartHelper.getFontHeight(priceFontSize) / 4,
                    timeScalePaint);
            lVari = timeScalePaint.measureText("0.00%");
            canvas.drawText(
                    "0.00%",
                    uprightGroup[0][2] - lVari,
                    uprightGroup[0][3] / 2
                            + ChartHelper.getFontHeight(priceFontSize) / 4,
                    timeScalePaint);
            timeScalePaint.setColor(greenColor);
            canvas.drawText(MathUtils.formatNum(mMinValue, decm), uprightGroup[0][0],
                    uprightGroup[0][3] - 2, timeScalePaint);

            String des2 = "-" + span + "%";

            lVari = timeScalePaint.measureText(des2);
            canvas.drawText(des2, uprightGroup[0][2] - lVari,
                    uprightGroup[0][3] - UtilTools.dip2px(mContext, 2), timeScalePaint);
            realTimePriceLine2.draw(canvas);

            //realTimePriceLine.draw(canvas, realTimePriceLine.getOriginPoint());
            averageLine.draw(canvas, averageLine.getOriginPoint());
            ezbarChart.draw(canvas, ezbarChart.getOriginPoint());
            if (isVer) {
                canvas.drawBitmap(bitmapScale, null, rectF, timeScalePaint);
            }

            float textHeight = height / 12 + 4 * height / 6 - UtilTools.dip2px(mContext, 7);
            float textOffset = 30;
            if (isScrollDetail) {
                //vertical line
                canvas.drawRect(scrollX, 0, scrollX + 2, uprightGroup[0][3], shiziPaint);
                canvas.drawRect(scrollX, uprightGroup[1][1], scrollX + 2, uprightGroup[1][3], shiziPaint);

                if (scrollY < 0 || scrollY > height) {
                    return;
                }

                //cursor  y分为三段  (0~uprightGroup[0][3]) (uprightGroup[0][3]~uprightGroup[1][1]) (uprightGroup[1][1],height)
                if (scrollY < uprightGroup[0][3]) {
                    //horizontal line
                    canvas.drawRect(0, scrollY, width, scrollY + 2, shiziPaint);
                    float price = mMaxValue - (3 * scrollY * (mMaxValue - mMinValue) / (2 * height));
                    String priceString = UtilTools.getFormatNum(price + "", 2);
                    Rect rect = new Rect();
                    blackPaint.getTextBounds(priceString, 0, priceString.length(), rect);
                    int tw1 = rect.width();//文本的宽度
                    int th1 = rect.height();//文本的高度

                    if (uprightGroup[0][3] - scrollY < th1) {
                        if (scrollX < width / 2) {
                            canvas.drawRect(width - tw1 - 20, uprightGroup[0][3] - 2 * th1, width, uprightGroup[0][3], cursorPaint);
                            canvas.drawText(priceString, width - tw1 - 10, uprightGroup[0][3] - 10, blackPaint);
                        } else {
                            canvas.drawRect(0, uprightGroup[0][3] - 2 * th1, tw1 + 20, uprightGroup[0][3], cursorPaint);
                            canvas.drawText(priceString, 5, uprightGroup[0][3] - 10, blackPaint);
                        }
                    } else if (scrollY < th1) {
                        if (scrollX < width / 2) {
                            canvas.drawRect(width - tw1 - 20, 0, width, 2 * th1, cursorPaint);
                            canvas.drawText(priceString, width - tw1 - 10, 2 * th1 - 10, blackPaint);
                        } else {
                            canvas.drawRect(0, 0, tw1 + 20, 2 * th1, cursorPaint);
                            canvas.drawText(priceString, 5, 2 * th1 - 10, blackPaint);
                        }
                    } else {
                        if (scrollX < width / 2) {
                            canvas.drawRect(width - tw1 - 20, scrollY - th1, width, scrollY + th1, cursorPaint);
                            canvas.drawText(priceString, width - tw1 - 10, scrollY + th1 - 10, blackPaint);
                        } else {
                            canvas.drawRect(0, scrollY - th1, tw1 + 20, scrollY + th1, cursorPaint);
                            canvas.drawText(priceString, 5, scrollY + th1 - 10, blackPaint);
                        }
                    }


                } else if (scrollY < uprightGroup[1][1]) {

                } else {
                    //horizontal line
                    canvas.drawRect(0, scrollY, width, scrollY + 2, shiziPaint);
                    int chart = (int) ((height - scrollY) / (float) columnHeightScale);
                    String priceString = UtilTools.getTransFormNum1(chart) + "手";
                    Rect rect = new Rect();
                    blackPaint.getTextBounds(priceString, 0, priceString.length(), rect);
                    int tw1 = rect.width();//文本的宽度
                    int th1 = rect.height();//文本的高度

                    if (height - scrollY < th1) {
                        if (scrollX < width / 2) {
                            canvas.drawRect(width - tw1 - 20, height - 2 * th1, width, height, cursorPaint);
                            canvas.drawText(priceString, width - tw1 - 10, height - 10, blackPaint);
                        } else {
                            canvas.drawRect(0, height - 2 * th1, tw1 + 20, height, cursorPaint);
                            canvas.drawText(priceString, 5, height - 10, blackPaint);
                        }
                    } else if (scrollY - uprightGroup[1][1] < th1) {
                        if (scrollX < width / 2) {
                            canvas.drawRect(width - tw1 - 20, uprightGroup[1][1], width, uprightGroup[1][1] + 2 * th1, cursorPaint);
                            canvas.drawText(priceString, width - tw1 - 10, uprightGroup[1][1] + 2 * th1 - 10, blackPaint);
                        } else {
                            canvas.drawRect(0, uprightGroup[1][1], tw1 + 20, uprightGroup[1][1] + 2 * th1, cursorPaint);
                            canvas.drawText(priceString, 5, uprightGroup[1][1] + 2 * th1 - 10, blackPaint);
                        }
                    } else {
                        if (scrollX < width / 2) {
                            canvas.drawRect(width - tw1 - 20, scrollY - th1, width, scrollY + th1, cursorPaint);
                            canvas.drawText(priceString, width - tw1 - 10, scrollY + th1 - 10, blackPaint);
                        } else {
                            canvas.drawRect(0, scrollY - th1, tw1 + 20, scrollY + th1, cursorPaint);
                            canvas.drawText(priceString, 5, scrollY + th1 - 10, blackPaint);
                        }
                    }
                }

                FenShiDesEntity entity = mDesEntityList.get(mDesIndex);
                float textLength = numPaint.measureText("0");
                int offset = 10;
                canvas.drawText("成交量", offset, textHeight, desPaint);
                textLength = desPaint.measureText("成交量");

                offset += textLength + 10;
                canvas.drawText(entity.getColumn() + "", offset, textHeight,
                        desPaint);
                textLength = desPaint.measureText(entity.getColumn() + "");

                offset += textLength + 50;
                canvas.drawText("总量", offset, textHeight, desPaint);
                textLength = desPaint.measureText("总量");

                offset += textLength + 10;
                canvas.drawText(entity.getColumnAll() + "", offset, textHeight,
                        desPaint);
                textLength = desPaint.measureText(entity.getColumnAll() + "");

                offset += textLength + 50;
                canvas.drawText("总额", offset, textHeight, desPaint);
                textLength = desPaint.measureText("总额");

                offset += textLength + 10;
                canvas.drawText(entity.getCountAll() + "", offset, textHeight,
                        desPaint);
            } else {
                // 绘制时间刻度
                timeScalePaint.setColor(getResources().getColor(R.color.gray));
                canvas.drawText("9:30", 2, textHeight, timeScalePaint);
                canvas.drawText("", width / 4 - textOffset, textHeight,
                        timeScalePaint);
                //zhangchungang
                canvas.drawText("11:30", width / 2 - timeScalePaint.measureText("11:30") / 2, textHeight,
                        timeScalePaint);
                canvas.drawText("", width / 2 + width / 4 - textOffset,
                        textHeight, timeScalePaint);
                lVari = timeScalePaint.measureText("15:00");
                canvas.drawText("15:00", width - lVari - 2, textHeight,
                        timeScalePaint);

            }
            canRefresh = false;
        } else {

            float textLength = loadPaint.measureText("数据加载中");
            canvas.drawRect(uprightGroup[0][0], uprightGroup[0][1],
                    uprightGroup[0][2], uprightGroup[0][3], rectPaint);
            canvas.drawRect(uprightGroup[1][0], uprightGroup[1][1],
                    uprightGroup[1][2], uprightGroup[1][3], rectPaint);
            if (width == 0) {
                canvas.drawText("数据加载中", getMeasuredWidth() / 2 - textLength / 2, getMeasuredHeight() / 2,
                        loadPaint);
            } else {
                canvas.drawText("数据加载中", width / 2 - textLength / 2, height / 2,
                        loadPaint);
            }

        }
    }

    private boolean isVer = true;

    public void setVer(boolean isver) {
        isVer = isver;
    }

    Handler handler;

    public void setHandler(Handler handler) {
        this.handler = handler;
    }
}
