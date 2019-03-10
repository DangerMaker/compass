package com.ez08.compass.ui.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.PathEffect;
import android.graphics.RectF;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ScaleGestureDetector;
import android.view.View;

import com.ez08.compass.CompassApp;
import com.ez08.compass.R;
import com.ez08.compass.entity.CGPoint;
import com.ez08.compass.entity.ColumnValuesDataModel;
import com.ez08.compass.entity.KChartEntity;
import com.ez08.compass.entity.KLineValuesDataModel;
import com.ez08.compass.entity.StockDesEntity;
import com.ez08.compass.entity.StockDetailEntity;
import com.ez08.compass.tools.MathUtils;
import com.ez08.compass.tools.StockUtils;
import com.ez08.compass.tools.UtilTools;
import com.ez08.compass.ui.view.drawutils.EzBarChart;
import com.ez08.compass.ui.view.drawutils.EzBarVolume;
import com.ez08.compass.ui.view.drawutils.EzFixedAreaLine;
import com.ez08.compass.ui.view.drawutils.EzFixedGapLine;
import com.ez08.compass.ui.view.drawutils.EzKLineChart;
import com.ez08.compass.ui.view.indicators.KLineViewImp;
import com.ez08.compass.ui.view.indicators.MacdViewImp;
import com.ez08.compass.ui.view.indicators.VolumeViewImp;

import java.util.ArrayList;
import java.util.List;

public class KLineView extends View {

    public static final int ONE_SCREEN_KLINE_NUM = 52;
    public static final float TOP_RECT_PERCENT = 0.72f;
    public static final float BOTTOM_RECT_PERCENT = 0.18f;
    public static final float MID_RECT_PERCENT = 1f - TOP_RECT_PERCENT - BOTTOM_RECT_PERCENT;

    private Context mContext;
    private Handler handler;
    private ScaleGestureDetector mScaleGestureDetector = null;

    private int redColor;
    private int greenColor;

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
    // --
    private boolean canRefresh = false;
    private boolean isVer = true;

    private int width, height; // 控件宽高
    private float dashedGroup[]; // 虚线数组
    private float[][] uprightGroup; // 矩形数组

    float barLeftGap = 2;
    private int lBeforeIndex = 0;

    private int mBarType = -1;
    private int indexNum = 0;
    private int lengthNum = 0;
    private float widthAverage = 0;
    private float lineLeftGap = 0;
    private float interval = 0;
    private int decm = 4;

    StockDetailEntity detailEntity;
    //    private List<StockDesEntity> mDesEntityList; //详细的数据，原始值+计算后的指标
    private List<KChartEntity> mTotalList; // 传入总的数据源，原始值

    private List<ColumnValuesDataModel> zhuColumn;//主力资金数据集
    private List<ColumnValuesDataModel> dkColumn;//多空资金数据集
    private List<ColumnValuesDataModel> gsdColumn;//敢死队资金数据集


    public KLineView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        mContext = context;
        redColor = CompassApp.GLOBAL.RED;
        greenColor = CompassApp.GLOBAL.GREEN;
//        mScaleGestureDetector = new ScaleGestureDetector(context, listener);
        init();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        width = getWidth();
        height = getHeight();

        widthAverage = (width - barLeftGap) / ONE_SCREEN_KLINE_NUM;
        lineLeftGap = barLeftGap + 7 * widthAverage / (10 * 2);

        float intervalTotal = width - lineLeftGap - widthAverage - 7
                * widthAverage / 10;
        int lAver = ONE_SCREEN_KLINE_NUM - 2;
        interval = intervalTotal / lAver;
        initData();
    }

    //初始化
    private void init() {
        dashedGroup = new float[4];
        uprightGroup = new float[2][4];
        mTotalList = new ArrayList<>();

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
        dashedGroup[0] = height * TOP_RECT_PERCENT /4;
        dashedGroup[1] = dashedGroup[0] * 2;
        dashedGroup[2] = dashedGroup[0] * 3;
        dashedGroup[3] = height - height * BOTTOM_RECT_PERCENT /2;

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

    public void setData(List<KChartEntity> lists) {
        if (lists == null || lists.isEmpty()) {
            invalidate();
            return;
        }
        mTotalList.clear();
        mTotalList.addAll(lists);

        indexNum = lists.size() - ONE_SCREEN_KLINE_NUM > 0 ? lists.size() - ONE_SCREEN_KLINE_NUM : 0;
        lengthNum = lists.size();

        kLineViewImp.setData(mTotalList);
        volumeViewImp.setData(mTotalList);
        macdViewImp.setData(mTotalList);

        refreshUI(indexNum, lists.size());
    }

    private void refreshUI(int start, int end) {
        kLineViewImp.setRange(start,end);
        switch (mBarType) {
            default:
//                volumeViewImp.setRange(start, end);
                macdViewImp.setRange(start,end);
                invalidate();
                break;
        }
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
//            volumeViewImp.draw(canvas);
            macdViewImp.draw(canvas);

        } else {
            float textLength = rectPaint.measureText("数据加载中");
            canvas.drawRect(uprightGroup[0][0], uprightGroup[0][1], uprightGroup[0][2], uprightGroup[1][3], rectPaint);
            canvas.drawText("数据加载中", width / 2 - textLength / 2, height / 2, textPaint);
            canRefresh = true;
        }
    }

    public void setHandler(Handler handler) {
        this.handler = handler;
    }

}
