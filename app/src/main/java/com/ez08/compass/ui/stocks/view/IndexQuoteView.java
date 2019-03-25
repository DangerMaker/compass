package com.ez08.compass.ui.stocks.view;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.ez08.compass.R;
import com.ez08.compass.entity.StockDataEntity;
import com.ez08.compass.parser.IndicatorHelper;

public class IndexQuoteView extends FrameLayout {

    TextView stockBigTv;
    TextView stockWaveTv;
    TextView stockOpenTv;
    TextView stockCloseTv;
    TextView stockVolumeTv;
    TextView stockChangeTv;

    public IndexQuoteView(Context context) {
        super(context);
    }

    public IndexQuoteView(Context context, AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    public void init(){
        LayoutInflater.from(getContext()).inflate(R.layout.view_index_quote,this);
        stockBigTv = (TextView) findViewById(R.id.stock_big);
        stockWaveTv = (TextView) findViewById(R.id.stock_main_percent);
        stockOpenTv = (TextView) findViewById(R.id.stock_open);
        stockCloseTv = (TextView) findViewById(R.id.stock_close);
        stockVolumeTv = (TextView) findViewById(R.id.stock_volume);
        stockChangeTv = (TextView) findViewById(R.id.stock_change);
    }

    public void setData(IndicatorHelper helper){
        setStockData(stockBigTv, helper.getCurrentPriceEntity());
        setStockData(stockWaveTv, helper.getCurrentWaveEntity());
        setStockData(stockOpenTv, helper.getOpenPriceEntity());
        setStockData(stockCloseTv, helper.getClosePriceEntity());
        setStockData(stockVolumeTv, helper.getVolumeEntity());
        setStockData(stockChangeTv, helper.getChangeEntity());
    }


    private void setStockData(TextView textView, StockDataEntity entity) {
        String title = "";
        if (!TextUtils.isEmpty(entity.getTitle())) {
            title = entity.getTitle() + "  ";
        }

        textView.setText(title + entity.getContent());
        if (entity.getContentColor() != 0) {
            textView.setTextColor(entity.getContentColor());
        }
    }

}
