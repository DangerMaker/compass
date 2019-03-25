package com.ez08.compass.ui.view.indicators;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.ez08.compass.CompassApp;
import com.ez08.compass.R;
import com.ez08.compass.entity.CGPoint;
import com.ez08.compass.entity.KChartEntity;
import com.ez08.compass.entity.KLineValuesDataModel;
import com.ez08.compass.tools.MathUtils;
import com.ez08.compass.tools.StockUtils;
import com.ez08.compass.tools.UtilTools;
import com.ez08.compass.ui.view.KLineView;
import com.ez08.compass.ui.view.drawutils.EzFixedGapLine;
import com.ez08.compass.ui.view.drawutils.EzKLineChart;

import java.util.ArrayList;
import java.util.List;

public class KLineViewImp implements Indicator<List<KChartEntity>> {
    Context context;
    private EzKLineChart kLineChart; // 画k线
    private EzFixedGapLine ma5Line;
    private EzFixedGapLine ma10Line;
    private EzFixedGapLine ma20Line;
    private EzFixedGapLine ma60Line;

    int width;
    int height;
    int greenColor;
    int redColor;

    private float mHighestData;//最大数
    private float mLowestData;//最小数
    private float mHigher1Data;//最大数下面第一个数
    private float mHigher2Data;//最大数下面第二个数
    private float mHigher3Data;//最大数下面第三个数
    private List<Float> ma5List;
    private List<Float> ma10List;
    private List<Float> ma20List;
    private List<Float> ma60List;
    List<KChartEntity> mTotalList;

    private float widthAverage = 0;
    private float lineLeftGap = 0;
    float barLeftGap = 2;
    private float interval = 0;
    int decm;

    private CGPoint mKPosition; // k线坐标
    private CGPoint mIndexPosition; // 指数坐标
    Paint textPaint;

    public KLineViewImp(Context context) {
        this.context = context;
        greenColor = CompassApp.GLOBAL.GREEN;
        redColor = CompassApp.GLOBAL.RED;
        ma5List = new ArrayList<>();
        ma10List = new ArrayList<>();
        ma20List = new ArrayList<>();
        ma60List = new ArrayList<>();

        kLineChart = new EzKLineChart();
        kLineChart.setRiseColor(redColor);
        kLineChart.setFallColor(greenColor);

        ma5Line = new EzFixedGapLine();
        ma5Line.setLineColor(context.getResources().getColor(R.color.orange));
        ma5Line.setLineWidth(2.0f);

        ma10Line = new EzFixedGapLine();
        ma10Line.setLineColor(context.getResources().getColor(R.color.fuchsia));
        ma10Line.setLineWidth(2.0f);

        ma20Line = new EzFixedGapLine();
        ma20Line.setLineColor(greenColor);
        ma20Line.setLineWidth(2.0f);

        ma60Line = new EzFixedGapLine();
        ma60Line.setLineColor(context.getResources().getColor(R.color.blue));
        ma60Line.setLineWidth(2.0f);

        textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setTextSize(UtilTools.dip2px(context, 10));
        textPaint.setColor(context.getResources().getColor(R.color.lable_item_style));
    }

    @Override
    public void setWidth(int width) {
        this.width = width;
        kLineChart.setWidth(width);

    }

    @Override
    public void setHeight(int height) {
        this.height = height;
    }

    public void setDecm(int decm){
        this.decm = decm;
    }

    public void setInterval(float widthAverage,float interval, float barLeftGap,float lineLeftGap){
        this.widthAverage = widthAverage;
        this.interval = interval;
        this.barLeftGap = barLeftGap;
        this.lineLeftGap = lineLeftGap;
    }

    @Override
    public void setData(List<KChartEntity> mTotalList) {
        this.mTotalList = mTotalList;
        ma5List.clear();
        ma5List.addAll(StockUtils.getMaListByNum(5, mTotalList));
        ma10List.clear();
        ma10List.addAll(StockUtils.getMaListByNum(10, mTotalList));
        ma20List.clear();
        ma20List.addAll(StockUtils.getMaListByNum(20, mTotalList));
        ma60List.clear();
        ma60List.addAll(StockUtils.getMaListByNum(60, mTotalList));
    }

    @Override
    public void setRange(int start, int end) {
        List<KLineValuesDataModel> kvalues = new ArrayList<>();
        List<Float> lma5List = new ArrayList<>();
        List<Float> lma10List = new ArrayList<>();
        List<Float> lma20List = new ArrayList<>();
        List<Float> lma60List = new ArrayList<>();
        for (int i = start; i < end; i++) {
            KLineValuesDataModel value = new KLineValuesDataModel(mTotalList.get(i).getOpen(),
                    mTotalList.get(i).getHigh(), mTotalList.get(i).getLow(),
                    mTotalList.get(i).getClose(), (int) mTotalList.get(i).getlTime());
            kvalues.add(value);
        }

        for (int i = start; i < end; i++) {
            lma5List.add(ma5List.get(i));
            lma10List.add(ma10List.get(i));
            lma20List.add(ma20List.get(i));
            lma60List.add(ma60List.get(i));
        }

        kLineChart.setkLineList(kvalues);
        ma5Line.setValues(lma5List);
        ma10Line.setValues(lma10List);
        ma20Line.setValues(lma20List);
        ma60Line.setValues(lma60List);

        mLowestData = 0;
        mHighestData = 0;
        for (int i = 0; i < kvalues.size(); i++) {
            float max = kvalues.get(i).getMaxValue();
            float min = kvalues.get(i).getMinValue();
            float ma5MaxMin = lma5List.get(i);
            float ma10MaxMin = lma10List.get(i);
            float ma20MaxMin = lma20List.get(i);
            float ma60MaxMin = lma60List.get(i);
            if (i == 0) {
                mLowestData = min;
            }
            if (mHighestData < max) {
                mHighestData = max;
            }
            if (mLowestData > min) {
                mLowestData = min;
            }
            if (mHighestData < ma5MaxMin) {
                mHighestData = ma5MaxMin;
            }
            if (mLowestData > ma5MaxMin) {
                mLowestData = ma5MaxMin;
            }
            if (mHighestData < ma10MaxMin) {
                mHighestData = ma10MaxMin;
            }
            if (mLowestData > ma10MaxMin) {
                mLowestData = ma10MaxMin;
            }
            if (mHighestData < ma20MaxMin) {
                mHighestData = ma20MaxMin;
            }
            if (mLowestData > ma20MaxMin) {
                mLowestData = ma20MaxMin;
            }
            if (mHighestData < ma60MaxMin) {
                mHighestData = ma60MaxMin;
            }
            if (mLowestData > ma60MaxMin) {
                mLowestData = ma60MaxMin;
            }
        }

        float tempSpan = mHighestData - mLowestData;
        mHigher1Data = mLowestData + tempSpan * 3 / 4;
        mHigher2Data = mLowestData + tempSpan * 2 / 4;
        mHigher3Data = mLowestData + tempSpan * 1 / 4;

        float heightScale =  height * KLineView.TOP_RECT_PERCENT / (mHighestData - mLowestData);
        kLineChart.setHeightScale(heightScale);
        kLineChart.setmLowestData(mLowestData);
        kLineChart.setmHighestData(mHighestData);

        kLineChart.setGapWidth(3 * widthAverage / 10);
        kLineChart.setChartwidth(7 * widthAverage / 10);

        ma5Line.setHeightScale(heightScale);
        ma5Line.setmLowestData(mLowestData);
        ma5Line.setmHighestData(mHighestData);

        ma10Line.setHeightScale(heightScale);
        ma10Line.setmLowestData(mLowestData);
        ma10Line.setmHighestData(mHighestData);

        ma20Line.setHeightScale(heightScale);
        ma20Line.setmLowestData(mLowestData);
        ma20Line.setmHighestData(mHighestData);

        ma60Line.setHeightScale(heightScale);
        ma60Line.setmLowestData(mLowestData);
        ma60Line.setmHighestData(mHighestData);

        mKPosition = new CGPoint(barLeftGap, 2);
        mIndexPosition = new CGPoint(lineLeftGap, 0);

        ma5Line.setOriginPoint(mIndexPosition);
        ma10Line.setOriginPoint(mIndexPosition);
        ma20Line.setOriginPoint(mIndexPosition);
        ma60Line.setOriginPoint(mIndexPosition);
        kLineChart.setOriginPoint(mKPosition);


        ma5Line.setInterval(interval);
        ma10Line.setInterval(interval);
        ma20Line.setInterval(interval);
        ma60Line.setInterval(interval);
    }

    @Override
    public void draw(Canvas canvas) {
        kLineChart.draw(canvas, kLineChart.getOriginPoint());
        ma5Line.draw(canvas, ma5Line.getOriginPoint());
        ma10Line.draw(canvas, ma10Line.getOriginPoint());
        ma20Line.draw(canvas, ma20Line.getOriginPoint());
        ma60Line.draw(canvas, ma60Line.getOriginPoint());

        canvas.drawText(MathUtils.formatNum(mHighestData, decm) + "", 5, 25, textPaint);
        canvas.drawText(MathUtils.formatNum(mHigher1Data, decm), 5, height * KLineView.TOP_RECT_PERCENT /4 - 1, textPaint);
        canvas.drawText(MathUtils.formatNum(mHigher2Data, decm), 5, height * KLineView.TOP_RECT_PERCENT /4  * 2  -1, textPaint);
        canvas.drawText(MathUtils.formatNum(mHigher3Data, decm), 5, height * KLineView.TOP_RECT_PERCENT /4 * 3 - 1, textPaint);
        canvas.drawText(MathUtils.formatNum(mLowestData, decm), 5, height * KLineView.TOP_RECT_PERCENT /4 * 4 - 1, textPaint);
    }
}
