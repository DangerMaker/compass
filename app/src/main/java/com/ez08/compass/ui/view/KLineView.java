package com.ez08.compass.ui.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.PathEffect;
import android.graphics.RectF;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

import com.ez08.compass.CompassApp;
import com.ez08.compass.R;
import com.ez08.compass.entity.ColumnValuesDataModel;
import com.ez08.compass.entity.KChartEntity;
import com.ez08.compass.entity.StockDetailEntity;
import com.ez08.compass.tools.JumpHelper;
import com.ez08.compass.tools.UtilTools;
import com.ez08.compass.ui.view.indicators.Indicator;
import com.ez08.compass.ui.view.indicators.KLineViewImp;
import com.ez08.compass.ui.view.indicators.MacdViewImp;
import com.ez08.compass.ui.view.indicators.VolumeViewImp;

import java.util.ArrayList;
import java.util.List;

public class KLineView extends View {

    public static final String TAG = "KLineView";
    public static final int ONE_SCREEN_KLINE_NUM = 52;
    public static final int ONE_SCREEN_KLINE_NUM_MIX = 36;
    public static final int ONE_SCREEN_KLINE_NUM_MAX = 104;
    public static final int KLINE_VIEW_NET_ONCE = 200;
    public static final int GET_MORE_LINE = 2001;
    public static final float TOP_RECT_PERCENT = 0.72f;
    public static final float BOTTOM_RECT_PERCENT = 0.18f;
    public static final float MID_RECT_PERCENT = 1f - TOP_RECT_PERCENT - BOTTOM_RECT_PERCENT;

    private Context mContext;
    private Handler handler;
    private GestureDetector gestureDetector;
    ScaleGestureDetector scaleGestureDetector;

    private int redColor;
    private int greenColor;

    private Indicator indicator;
    private KLineViewImp kLineViewImp;
    private VolumeViewImp volumeViewImp;
    private MacdViewImp macdViewImp;

    RectF chartRectF; //指标区域
    RectF scaleRectF; //切换到竖屏区域
    RectF scaleRealRectF; //上边的画图用，点击区域要大一些
    RectF r2rRectF; //资金展示切换区域
    RectF zjRectF;//资金切换区域

    Paint rectPaint;
    Paint dashedPaint;
    Paint textPaint;
    Paint blackPaint;
    // --
    private boolean canRefresh = false;
    private boolean isVer = true;

    private int width, height; // 控件宽高
    private float dashedGroup[]; // 虚线数组
    private float[][] uprightGroup; // 矩形数组

    float barLeftGap = 2;
    private int lBeforeIndex = 0;

    private int mBarType = 0;
    private float widthAverage = 0;
    private float lineLeftGap = 0;
    private float interval = 0;
    private int decm = 4;
    int screenStart = 0;
    int screenEnd = 0;
    public boolean tenLineState = false; //十字线状态
    int currentLineNum = 52;
    float currentX = 0;
    float currentY = 0;
    float formatX = 0;
    float lastFormatX = 0;
    int formatIndex = 0;
    float scrollDistanceX = 0;
    int scrollStartIndex = 0;
    int scrollEndIndex = 0;

    StockDetailEntity detailEntity;
    //    private List<StockDesEntity> mDesEntityList; //详细的数据，原始值+计算后的指标
    private List<KChartEntity> mTotalList; // 传入总的数据源，原始值

    private List<ColumnValuesDataModel> zhuColumn;//主力资金数据集
    private List<ColumnValuesDataModel> dkColumn;//多空资金数据集
    private List<ColumnValuesDataModel> gsdColumn;//敢死队资金数据集

    int MODE = 0; //0 拖拽模式 1 缩放模式 2 十字模式

    public KLineView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        mContext = context;
        redColor = CompassApp.GLOBAL.RED;
        greenColor = CompassApp.GLOBAL.GREEN;
        gestureDetector = new GestureDetector(new MySimpleGesture());

        scaleGestureDetector = new ScaleGestureDetector(context, new ScaleGestureDetector.OnScaleGestureListener() {
            @Override
            public boolean onScale(ScaleGestureDetector detector) {

                int tempLine = (int) (currentLineNum / detector.getScaleFactor());
                if (tempLine > ONE_SCREEN_KLINE_NUM_MAX) {
                    tempLine = ONE_SCREEN_KLINE_NUM_MAX;
                } else if (tempLine < ONE_SCREEN_KLINE_NUM_MIX) {
                    tempLine = ONE_SCREEN_KLINE_NUM_MIX;
                }

                int start = screenEnd - tempLine;
                if (start < 0) {
                    if (checkIfNeedLoadMore) {
                        handler.sendEmptyMessage(GET_MORE_LINE);
                        checkIfNeedLoadMore = false;
                    }
                    return true;
                }

                currentLineNum = tempLine;
                Log.e(TAG, "onScale line=" + currentLineNum);
                widthAverage = (width - barLeftGap) / currentLineNum;
                lineLeftGap = barLeftGap + 7 * widthAverage / (10 * 2);

                float intervalTotal = width - lineLeftGap - widthAverage - 7
                        * widthAverage / 10;
                int lAver = currentLineNum - 2;
                interval = intervalTotal / lAver;
                kLineViewImp.setInterval(widthAverage, interval, barLeftGap, lineLeftGap);
                volumeViewImp.setInterval(widthAverage, interval, barLeftGap, lineLeftGap);
                macdViewImp.setInterval(widthAverage, interval, barLeftGap, lineLeftGap);

                refreshUI(start, screenEnd);
                return false;
            }

            @Override
            public boolean onScaleBegin(ScaleGestureDetector detector) {
                Log.e(TAG, "onScaleBegin");
                MODE = 1;
                return true;
            }

            @Override
            public void onScaleEnd(ScaleGestureDetector detector) {
                Log.e(TAG, "onScaleEnd");
            }
        });
        init();
    }

    private void chartPress() {
        mBarType = mBarType + 1;
        setIndexType(mBarType);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        width = getWidth();
        height = getHeight();

        widthAverage = (width - barLeftGap) / currentLineNum;
        lineLeftGap = barLeftGap + 7 * widthAverage / (10 * 2);

        float intervalTotal = width - lineLeftGap - widthAverage - 7
                * widthAverage / 10;
        int lAver = currentLineNum - 2;
        interval = intervalTotal / lAver;
        initData();
    }

    //初始化
    private void init() {
        dashedGroup = new float[4];
        uprightGroup = new float[2][4];
        mTotalList = new ArrayList<>();
        currentLineNum = ONE_SCREEN_KLINE_NUM;

        rectPaint = new Paint();
        rectPaint.setStrokeWidth(3);
        rectPaint.setColor(getResources().getColor(R.color.shadow1));
        rectPaint.setStyle(Paint.Style.STROKE);

        dashedPaint = new Paint();//虚线画笔
        dashedPaint.setStrokeWidth(3);
        dashedPaint.setColor(getResources().getColor(R.color.shadow1));

        PathEffect effects = new DashPathEffect(new float[]{5, 5, 5, 5}, 1);
        dashedPaint.setStyle(Paint.Style.STROKE);
        dashedPaint.setPathEffect(effects);
        dashedPaint.setAntiAlias(true);

        textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setTextSize(UtilTools.dip2px(mContext, 15));
        textPaint.setColor(mContext.getResources().getColor(R.color.lable_item_style));
//        mDesEntityList = new ArrayList<>();
        blackPaint = new Paint();
        blackPaint.setAntiAlias(true);
        blackPaint.setColor(Color.BLACK);

        //k
        kLineViewImp = new KLineViewImp(mContext);
        //成交量
        volumeViewImp = new VolumeViewImp(mContext);
        macdViewImp = new MacdViewImp(mContext);
    }

    //设置具体位置
    private void initData() {
        kLineViewImp.setWidth(width);
        kLineViewImp.setHeight(height);
        kLineViewImp.setInterval(widthAverage, interval, barLeftGap, lineLeftGap);

        volumeViewImp.setWidth(width);
        volumeViewImp.setHeight(height);
        volumeViewImp.setInterval(widthAverage, interval, barLeftGap, lineLeftGap);

        macdViewImp.setWidth(width);
        macdViewImp.setHeight(height);
        macdViewImp.setInterval(widthAverage, interval, barLeftGap, lineLeftGap);

        //画布分为三个部分
        dashedGroup[0] = height * TOP_RECT_PERCENT / 4;
        dashedGroup[1] = dashedGroup[0] * 2;
        dashedGroup[2] = dashedGroup[0] * 3;
        dashedGroup[3] = height - height * BOTTOM_RECT_PERCENT / 2;

        uprightGroup[0][0] = 1;
        uprightGroup[0][1] = 1;
        uprightGroup[0][2] = width - 1;
        uprightGroup[0][3] = height * TOP_RECT_PERCENT;

        uprightGroup[1][0] = 1;
        uprightGroup[1][1] = height * (TOP_RECT_PERCENT + MID_RECT_PERCENT);
        uprightGroup[1][2] = width - 1;
        uprightGroup[1][3] = height - 1;

        scaleRectF = new RectF(
                uprightGroup[0][2] - UtilTools.dip2px(mContext, 22) - 10,
                uprightGroup[0][3] - UtilTools.dip2px(mContext, 22) - 10,
                uprightGroup[0][2] - 10,
                uprightGroup[0][3] - 10);
        scaleRealRectF = new RectF(
                scaleRectF.left - 20,
                scaleRectF.top - 20,
                scaleRectF.right + 20,
                scaleRectF.bottom + 20);

        chartRectF = new RectF(uprightGroup[1][0], uprightGroup[1][1], uprightGroup[1][2], uprightGroup[1][3]);
    }


    /**
     * 上级传入参数,findView便可调用
     *
     * @param entity
     */
    public void setDesEntity(StockDetailEntity entity) {
        this.detailEntity = entity;
        decm = detailEntity.getDecm();
        kLineViewImp.setDecm(decm);
    }

    boolean checkIfNeedLoadMore = true;

    public void setData(List<KChartEntity> lists) {
        if (lists == null || lists.isEmpty()) {
            invalidate();
            checkIfNeedLoadMore = false;
            return;
        }

        canRefresh = true;
        checkIfNeedLoadMore = true;
        mTotalList.clear();
        mTotalList.addAll(lists);

        int start;
        int end;
        //第一次
        if (screenStart == 0 && screenEnd == 0) {
            if (lists.size() < ONE_SCREEN_KLINE_NUM) {
                start = 0;
                end = mTotalList.size();
                checkIfNeedLoadMore = false;
            } else {
                end = mTotalList.size();
                start = end - ONE_SCREEN_KLINE_NUM;
            }
        } else {
            start = screenStart;
            end = screenEnd;
        }


        kLineViewImp.setData(mTotalList);
        volumeViewImp.setData(mTotalList);
        macdViewImp.setData(mTotalList);
        refreshUI(start, end);
    }

    public void setMoreData(List<KChartEntity> lists) {
        if (lists == null || lists.isEmpty()) {
            invalidate();
            checkIfNeedLoadMore = false;
            return;
        }

        int start;
        int end;
        int increment = lists.size() - mTotalList.size();
        if (increment < KLINE_VIEW_NET_ONCE - 1) {
            checkIfNeedLoadMore = false;
        } else {
            checkIfNeedLoadMore = true;
        }

        start = increment + screenStart;
        end = start + currentLineNum;

        mTotalList.clear();
        mTotalList.addAll(lists);

        kLineViewImp.setData(mTotalList);
        volumeViewImp.setData(mTotalList);
        macdViewImp.setData(mTotalList);
        refreshUI(start, end);
    }


    public void setIndexType(int indexType) {
        this.mBarType = indexType;
        refreshUI(screenStart, screenEnd);
    }

    private void refreshUI(int start, int end) {
        if (start < 0 || end > mTotalList.size()) {
            return;
        }

        this.screenStart = start;
        this.screenEnd = end;

        kLineViewImp.setRange(start, end);
        switch (mBarType) {
            case 0:
                indicator = volumeViewImp;
                break;
            case 1:
                indicator = macdViewImp;
                break;
            default:
                break;
        }
        Log.e(TAG, "indicator=" + "start=" + start + ",end=" + end);
        indicator.setRange(start, end);
        invalidate();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (canRefresh) {
            canvas.drawRect(uprightGroup[0][0], uprightGroup[0][1], uprightGroup[0][2], uprightGroup[0][3], rectPaint);
            canvas.drawRect(uprightGroup[1][0], uprightGroup[1][1], uprightGroup[1][2], uprightGroup[1][3], rectPaint);

            for (int i = 0; i < dashedGroup.length; i++) {
                canvas.drawLine(barLeftGap, dashedGroup[i], width, dashedGroup[i], dashedPaint);
            }

            kLineViewImp.draw(canvas);
            indicator.draw(canvas);

            if (tenLineState && formatIndex >= 0) {
                canvas.drawRect(lastFormatX, 0, lastFormatX + 2, uprightGroup[0][3], blackPaint);
                canvas.drawRect(lastFormatX, uprightGroup[1][1], lastFormatX + 2, uprightGroup[1][3], blackPaint);

                if (currentY < uprightGroup[0][3]) {
                    canvas.drawRect(0, currentY, width, currentY + 2, blackPaint);
                }
            }
        } else {
            float textLength = textPaint.measureText("数据加载中");
            canvas.drawRect(uprightGroup[0][0], uprightGroup[0][1], uprightGroup[0][2], uprightGroup[1][3], rectPaint);
            canvas.drawText("数据加载中", width / 2 - textLength / 2, height / 2, textPaint);
        }
    }

    boolean touchUp = false;
    int pointCount = 1;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        pointCount = event.getPointerCount();
        Log.e(TAG, "pointCount=" + pointCount);
        if (pointCount == 1) {
            if (!tenLineState) {
                return gestureDetector.onTouchEvent(event);
            }
        } else {
            return scaleGestureDetector.onTouchEvent(event);
        }

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                if (touchUp) {
                    tenLineState = false;
                    invalidate();
                    return true;
                }

                break;
            case MotionEvent.ACTION_POINTER_DOWN:

                break;
            case MotionEvent.ACTION_MOVE:
                currentX = event.getX();
                currentY = event.getY();

                for (int i = 0; i < screenEnd - screenStart; i++) {
                    formatX = lineLeftGap + interval * i;
                    if (formatX >= currentX) {
                        lastFormatX = formatX;
                        formatIndex = i;
                        break;
                    }
                }

                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                touchUp = true;
                removeCallbacks(delayRunnable);
                postDelayed(delayRunnable, 3 * 1000);
                pointCount = 1;
                break;
        }
        return true;
    }

    public Runnable delayRunnable = new Runnable() {
        @Override
        public void run() {
            tenLineState = false;
            invalidate();
        }
    };

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        removeCallbacks(delayRunnable);
    }

    public void setHandler(Handler handler) {
        this.handler = handler;
    }


    public boolean getFocus() {
        return tenLineState || pointCount == 2;
    }

    public class MySimpleGesture extends GestureDetector.SimpleOnGestureListener{
        @Override
        public boolean onDown(MotionEvent e) {
            Log.e(TAG, "onDown");
            scrollDistanceX = 0;
            scrollStartIndex = screenStart;
            scrollEndIndex = screenEnd;
            MODE = 0;
            return true;
        }

        @Override
        public void onShowPress(MotionEvent e) {
            Log.e(TAG, "onShowPress");
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            Log.e(TAG, "onSingleTapUp");
            if (chartRectF.contains(e.getX(), e.getY())) {
                chartPress();
                return true;
            }
            return false;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            if (MODE == 1) {
                return false;
            }

            scrollDistanceX = scrollDistanceX + distanceX;
            Log.e(TAG, "onScroll scrollDistanceX=" + scrollDistanceX);
            int num = (int) (scrollDistanceX / widthAverage);
            int start = scrollStartIndex + num;
            int end = scrollEndIndex + num;

            if (start < 0) {
                start = 0;
                end = scrollEndIndex - scrollStartIndex;
                if (checkIfNeedLoadMore) {
                    handler.sendEmptyMessage(GET_MORE_LINE);
                    checkIfNeedLoadMore = false;
                }
            }

            if (end > mTotalList.size()) {
                end = mTotalList.size();
                start = scrollStartIndex + mTotalList.size() - scrollEndIndex;
            }

            refreshUI(start, end);
            return true;
        }

        @Override
        public void onLongPress(MotionEvent e) {
            Log.e(TAG, "onLongPress");
            if (pointCount > 1) {
                return;
            }
            tenLineState = true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            Log.e(TAG, "onFling");
            return false;
        }


        @Override
        public boolean onDoubleTap(MotionEvent e) {
            Log.e(TAG, "onDoubleTap");
            JumpHelper.startStockHorizontalActivity(mContext,detailEntity.getSecucode(),null);
            return true;
        }
    }


    public void setCanRefresh(boolean canRefresh) {
        this.canRefresh = canRefresh;
        invalidate();
    }
}
