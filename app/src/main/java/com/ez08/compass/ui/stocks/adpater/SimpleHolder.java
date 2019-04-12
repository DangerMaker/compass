package com.ez08.compass.ui.stocks.adpater;

import android.view.ViewGroup;
import android.widget.TextView;

import com.ez08.compass.R;
import com.ez08.compass.entity.BaseNewsEntity;
import com.ez08.compass.entity.InfoEntity1;
import com.ez08.compass.ui.base.BaseViewHolder;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class SimpleHolder extends BaseViewHolder<BaseNewsEntity> {
    public TextView lTitle;
    public TextView lDate;

    public SimpleHolder(ViewGroup itemView) {
        super(itemView, R.layout.stock_main_report_item);
        lTitle = $(R.id.tv_news_list_1_title);
        lDate = $(R.id.tv_news_list_1_date);
    }

    @Override
    public void setData(BaseNewsEntity info) {
        lTitle.setText(info.getTitle());
        lDate.setText(info.getDate());
    }
}
