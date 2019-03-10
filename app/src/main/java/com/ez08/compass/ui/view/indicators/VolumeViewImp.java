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
import com.ez08.compass.ui.view.drawutils.EzBarVolume;
import com.ez08.compass.ui.view.drawutils.EzFixedGapLine;

import java.util.ArrayList;
import java.util.List;

public class VolumeViewImp implements Indicator<List<KChartEntity>> {

    public static final int VOLUME_UNIT = 10000;  //kChat 返回量单位是万，显示转换成亿
    Context context;
    private EzBarVolume ezBarChart; // 画柱状图
    // 成交量
    private EzFixedGapLine amountMa5Line;
    private EzFixedGapLine amountMa10Line;
    private EzFixedGapLine amountMa20Line;

    int width;
    int height;
    int greenColor;
    int redColor;

    private String mAmountDes;
    private float widthAverage = 0;
    private float lineLeftGap = 0;
    float barLeftGap = 2;
    private float interval = 0;
    private CGPoint mColumnPosition; // 成交量坐标
    Paint textPaint;

    private List<ColumnValuesDataModel> dataModels;


    public VolumeViewImp(Context context) {
        this.context = context;
        greenColor = CompassApp.GLOBAL.GREEN;
        redColor = CompassApp.GLOBAL.RED;
        dataModels = new ArrayList<>();

        ezBarChart = new EzBarVolume();
        amountMa5Line = new EzFixedGapLine();
        amountMa5Line.setLineColor(context.getResources().getColor(R.color.orange));
        amountMa5Line.setLineWidth(2.0f);

        amountMa10Line = new EzFixedGapLine();
        amountMa10Line.setLineColor(context.getResources().getColor(R.color.fuchsia));
        amountMa10Line.setLineWidth(2.0f);

        amountMa20Line = new EzFixedGapLine();
        amountMa20Line.setLineColor(CompassApp.GLOBAL.GREEN);
        amountMa20Line.setLineWidth(2.0f);

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
        for (int i = 0; i < mTotalList.size(); i++) {
            KChartEntity entity = mTotalList.get(i);
            ColumnValuesDataModel data;
            if (entity.getOpen() > entity.getClose()) {
                data = new ColumnValuesDataModel(greenColor, greenColor,
                        entity.getVolume());
            } else if (entity.getOpen() < entity.getClose()) {
                data = new ColumnValuesDataModel(redColor, redColor,
                        entity.getVolume());
            } else {
                //没看懂
                if (i != 0) {
                    float lastClose = mTotalList.get(i - 1).getClose();
                    if (entity.getOpen() < lastClose) {
                        data = new ColumnValuesDataModel(greenColor, greenColor,
                                entity.getVolume());
                    } else {
                        data = new ColumnValuesDataModel(redColor, redColor,
                                entity.getVolume());
                    }
                } else {
                    data = new ColumnValuesDataModel(redColor, redColor,
                            entity.getVolume());
                }
            }
            dataModels.add(data);
        }
    }

    public void setInterval(float widthAverage, float interval, float barLeftGap, float lineLeftGap) {
        this.widthAverage = widthAverage;
        this.interval = interval;
        this.barLeftGap = barLeftGap;
        this.lineLeftGap = lineLeftGap;
    }

    @Override
    public void setRange(int start, int end) {
        double columnHighestData = 0;
        double columnLowestData = 0;
        List<ColumnValuesDataModel> columns = new ArrayList<ColumnValuesDataModel>();
        for (int i = start; i < end; i++) {
            columns.add(dataModels.get(i));
        }
        // -------------柱状图

        List<Float> vollMa5List = StockUtils.getVolMaListByNum(5, dataModels);
        List<Float> vollMa10List = StockUtils.getVolMaListByNum(10, dataModels);
        List<Float> vollMa20List = StockUtils.getVolMaListByNum(20, dataModels);

        List<Float> svollMa5List = new ArrayList<Float>();
        List<Float> svollMa10List = new ArrayList<Float>();
        List<Float> svollMa20List = new ArrayList<Float>();

        for (int i = start; i < end; i++) {
            svollMa5List.add(vollMa5List.get(i));
        }

        for (int i = start; i < end; i++) {
            svollMa10List.add(vollMa10List.get(i));
        }

        for (int i = start; i < end; i++) {
            svollMa20List.add(vollMa20List.get(i));
        }

        for (int i = 0; i < columns.size(); i++) {
            if (i == 0) {
                columnLowestData = columns.get(i).getValue();
            }
            float max = columns.get(i).getValue();
            if (columnHighestData < max) {
                columnHighestData = max;
            }
            if (columnLowestData > max) {
                columnLowestData = max;
            }
            float max5 = svollMa5List.get(i);
            if (columnHighestData < max5) {
                columnHighestData = max5;
            }
            if (columnLowestData > max5) {
                columnLowestData = max5;
            }

            float max10 = svollMa10List.get(i);
            if (columnHighestData < max10) {
                columnHighestData = max10;
            }
            if (columnLowestData > max10) {
                columnLowestData = max10;
            }

            float max20 = svollMa20List.get(i);
            if (columnHighestData < max20) {
                columnHighestData = max20;
            }
            if (columnLowestData > max20) {
                columnLowestData = max20;
            }
        }

        mAmountDes = MathUtils.formatNum((float) columnHighestData / VOLUME_UNIT, 4) + "亿"; //保留两位小数

        double columnHeightScale = height * KLineView.BOTTOM_RECT_PERCENT / columnHighestData;
        ezBarChart.setGapWidth(3 * widthAverage / 10);
        ezBarChart.setColumnWidth(7 * widthAverage / 10);
        ezBarChart.setHeightScale(columnHeightScale);
        ezBarChart.setmHighestData(columnHighestData);
        ezBarChart.setValues(columns);

        mColumnPosition = new CGPoint(barLeftGap, height);
        ezBarChart.setOriginPoint(mColumnPosition);

        amountMa5Line.setValues(svollMa5List);
        amountMa10Line.setValues(svollMa10List);
        amountMa20Line.setValues(svollMa20List);

        amountMa5Line.setHeightScale(columnHeightScale);
        amountMa5Line.setmLowestData(columnLowestData);
        amountMa5Line.setmHighestData(columnHighestData);
        amountMa10Line.setHeightScale(columnHeightScale);
        amountMa10Line.setmLowestData(columnLowestData);
        amountMa10Line.setmHighestData(columnHighestData);
        amountMa20Line.setHeightScale(columnHeightScale);
        amountMa20Line.setmLowestData(columnLowestData);
        amountMa20Line.setmHighestData(columnHighestData);

        amountMa5Line.setInterval(interval);
        amountMa10Line.setInterval(interval);
        amountMa20Line.setInterval(interval);

        CGPoint lAmountPosition = new CGPoint(lineLeftGap, height * (KLineView.TOP_RECT_PERCENT + KLineView.MID_RECT_PERCENT));

        amountMa5Line.setOriginPoint(lAmountPosition);
        amountMa10Line.setOriginPoint(lAmountPosition);
        amountMa20Line.setOriginPoint(lAmountPosition);
    }

    @Override
    public void draw(Canvas canvas) {
        ezBarChart.draw(canvas, ezBarChart.getOriginPoint());
        amountMa5Line.draw(canvas, amountMa5Line.getOriginPoint());
        amountMa10Line.draw(canvas, amountMa10Line.getOriginPoint());
        amountMa20Line.draw(canvas, amountMa20Line.getOriginPoint());
        canvas.drawText(mAmountDes, 5, height * (KLineView.TOP_RECT_PERCENT + KLineView.MID_RECT_PERCENT) + UtilTools.dip2px(context, 10) + 5,
                textPaint);
    }
}
