package com.ez08.compass.ui.view.indicators;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.ez08.compass.CompassApp;
import com.ez08.compass.R;
import com.ez08.compass.entity.CGPoint;
import com.ez08.compass.entity.ColumnValuesDataModel;
import com.ez08.compass.entity.KChartEntity;
import com.ez08.compass.tools.MathUtils;
import com.ez08.compass.tools.StockUtils;
import com.ez08.compass.tools.UtilTools;
import com.ez08.compass.ui.view.KLineView;
import com.ez08.compass.ui.view.drawutils.EzBarChart;
import com.ez08.compass.ui.view.drawutils.EzBarVolume;
import com.ez08.compass.ui.view.drawutils.EzFixedGapLine;

import java.util.ArrayList;
import java.util.List;

public class MacdViewImp implements Indicator<List<KChartEntity>> {

    Context context;
    // macd
    private EzFixedGapLine macdDifLine;
    private EzFixedGapLine macdDeaLine;
    private EzBarChart macdBarLine;

    int width;
    int height;
    int greenColor;
    int redColor;

    private String mMacdDesHigh; // macd最大值
    private String mMacdDesNormal; // macd中间值
    private String mMacdDesLow; // macd最小值
    private List<KChartEntity> dataModels;

    private float widthAverage = 0;
    private float lineLeftGap = 0;
    float barLeftGap = 2;
    private float interval = 0;
    Paint textPaint;

    public MacdViewImp(Context context) {
        this.context = context;
        greenColor = CompassApp.GLOBAL.GREEN;
        redColor = CompassApp.GLOBAL.RED;
        dataModels = new ArrayList<>();

        macdDifLine = new EzFixedGapLine();
        macdDifLine.setLineColor(context.getResources().getColor(R.color.orange));
        macdDifLine.setLineWidth(2.0f);

        macdDeaLine = new EzFixedGapLine();
        macdDeaLine.setLineColor(greenColor);
        macdDeaLine.setLineWidth(2.0f);

        macdBarLine = new EzBarChart();

        textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setTextSize(UtilTools.dip2px(context, 10));
        textPaint.setColor(context.getResources().getColor(R.color.lable_item_style));
    }

    @Override
    public void setWidth(int width) {
        this.width = width;
    }

    @Override
    public void setHeight(int height) {
        this.height = height;
    }

    @Override
    public void setData(List<KChartEntity> mTotalList) {
        this.dataModels = mTotalList;
    }

    public void setInterval(float widthAverage, float interval, float barLeftGap, float lineLeftGap) {
        this.widthAverage = widthAverage;
        this.interval = interval;
        this.barLeftGap = barLeftGap;
        this.lineLeftGap = lineLeftGap;
    }

    @Override
    public void setRange(int start, int end) {
        List<ColumnValuesDataModel> bars = new ArrayList<ColumnValuesDataModel>();
        List<Float> lDifList = new ArrayList<Float>();
        List<Float> lDeaList = new ArrayList<Float>();
        int SHORT = 12, MID = 9, LONG = 26;
        double shortRate, longRate;
        double MACD_LARGER = 100;
        double DI, EMA_DI_SHORT = 0, EMA_DI_LONG = 0, DEA = 0;
        int resourceColor;
        if (SHORT == 12) {
            shortRate = 0.1538;
        } else {
            shortRate = 2.0 / (SHORT + 1);
        }

        if (LONG == 26) {
            longRate = 0.0741;
        } else {
            longRate = 2.0 / (LONG + 1);
        }

        for (int i = 0; i < dataModels.size(); i++) {
            DI = MACD_LARGER
                    * (dataModels.get(i).getHigh() + dataModels.get(i).getLow() + 2.0 * dataModels
                    .get(i).getClose()) * 0.25;

            if (i == 0) {
                EMA_DI_SHORT = DI;
                EMA_DI_LONG = DI;
            } else {
                EMA_DI_SHORT = EMA_DI_SHORT + (DI - EMA_DI_SHORT) * shortRate;
                EMA_DI_LONG = EMA_DI_LONG + (DI - EMA_DI_LONG) * longRate;
            }
            double dif = ((EMA_DI_SHORT - EMA_DI_LONG) / EMA_DI_LONG)
                    * MACD_LARGER;
            lDifList.add((float) dif);

            if (i == 0) {
                DEA = lDifList.get(0);
            } else {
                DEA = DEA + (lDifList.get(i) - DEA) * (2.0 / MID);
            }
            lDeaList.add((float) DEA);
            float barValue = lDifList.get(i) - lDeaList.get(i);
            if (barValue >= 0) {
                resourceColor = redColor;
            } else {
                resourceColor = greenColor;
            }
            ColumnValuesDataModel bar = new ColumnValuesDataModel(
                    resourceColor, resourceColor, barValue);
            bars.add(bar);
        }

        while (bars.size() < end) {
            start--;
            end--;
        }
        while (lDifList.size() < end) {
            start--;
            end--;
        }
        while (lDeaList.size() < end) {
            start--;
            end--;
        }

        List<ColumnValuesDataModel> macdBars = new ArrayList<>();
        List<Float> macdDifList = new ArrayList<>();
        List<Float> macdDeaList = new ArrayList<>();

        for (int i = start; i < end; i++) {
            macdDifList.add(lDifList.get(i));
        }

        for (int i = start; i < end; i++) {
            macdDeaList.add(lDeaList.get(i));
        }

        for (int i = start; i < end; i++) {
            macdBars.add(bars.get(i));
        }

        double macdHighestData = 0, macdLowestData = 0;

        for (int i = 0; i < macdBars.size(); i++) {
            if (i == 0) {
                macdLowestData = macdDifList.get(i);
            }
            if (macdBars.get(i).getValue() > macdHighestData) {
                macdHighestData = macdBars.get(i).getValue();
            }
            if (macdDeaList.get(i) > macdHighestData) {
                macdHighestData = macdDeaList.get(i);
            }
            if (macdDifList.get(i) > macdHighestData) {
                macdHighestData = macdDifList.get(i);
            }

            if (macdDifList.get(i) < macdLowestData) {
                macdLowestData = macdDifList.get(i);
            }
            if (macdDeaList.get(i) < macdLowestData) {
                macdLowestData = macdDeaList.get(i);
            }
            if (macdBars.get(i).getValue() < macdLowestData) {
                macdLowestData = macdBars.get(i).getValue();
            }
        }

        mMacdDesHigh = MathUtils.formatNum((float) macdHighestData, 5);
        mMacdDesLow = MathUtils.formatNum((float) macdLowestData, 5);
        double normal = (macdHighestData + macdLowestData) / 2;
        mMacdDesNormal = MathUtils.formatNum((float) normal, 5);

        double heightScale = (KLineView.BOTTOM_RECT_PERCENT * height / (macdHighestData - macdLowestData));

        macdDeaLine.setHeightScale(heightScale);
        macdDeaLine.setmLowestData(macdLowestData);
        macdDeaLine.setmHighestData(macdHighestData);
        macdDifLine.setHeightScale(heightScale);
        macdDifLine.setmLowestData(macdLowestData);
        macdDifLine.setmHighestData(macdHighestData);

        macdDeaLine.setInterval(interval);
        macdDifLine.setInterval(interval);

        CGPoint lMacdPosition = new CGPoint(lineLeftGap, height * (KLineView.TOP_RECT_PERCENT + KLineView.MID_RECT_PERCENT));
        Float offest = (float) (macdHighestData * heightScale);
        CGPoint lMacdBarPosition = new CGPoint(barLeftGap + 5 * widthAverage / (10 * 2), height * (KLineView.TOP_RECT_PERCENT + KLineView.MID_RECT_PERCENT) + offest);

        macdDeaLine.setOriginPoint(lMacdPosition);
        macdDifLine.setOriginPoint(lMacdPosition);
        macdDeaLine.setValues(macdDeaList);
        macdDifLine.setValues(macdDifList);
        macdBarLine.setGapWidth(9 * widthAverage / 10);
        macdBarLine.setColumnWidth(1 * widthAverage / 10);
        macdBarLine.setHeightScale(heightScale);
        macdBarLine.setmHighestData(macdHighestData);
        macdBarLine.setValues(macdBars);
        macdBarLine.setOriginPoint(lMacdBarPosition);
    }

    @Override
    public void draw(Canvas canvas) {
        macdBarLine.draw(canvas, macdBarLine.getOriginPoint());
        macdDeaLine.draw(canvas, macdDeaLine.getOriginPoint());
        macdDifLine.draw(canvas, macdDifLine.getOriginPoint());
        canvas.drawText(mMacdDesHigh, 5, height * (KLineView.TOP_RECT_PERCENT + KLineView.MID_RECT_PERCENT) + UtilTools.dip2px(context, 10), textPaint);
        canvas.drawText(mMacdDesNormal, 5, height - height * KLineView.BOTTOM_RECT_PERCENT / 2, textPaint);
        canvas.drawText(mMacdDesLow, 5, height - 2, textPaint);
    }
}
