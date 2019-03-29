package com.ez08.compass.ui.stocks.adpater;

import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ez08.compass.CompassApp;
import com.ez08.compass.R;
import com.ez08.compass.entity.InfoEntity;
import com.ez08.compass.entity.InfoEntity1;
import com.ez08.compass.ui.base.BaseViewHolder;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class InnerNewsHolder extends BaseViewHolder<InfoEntity1> {
    public TextView lTitle;
    public TextView lDate;
    public TextView lCategory;
    public TextView lName;

    public InnerNewsHolder(ViewGroup itemView) {
        super(itemView, R.layout.compass_style_item);
        lTitle = $(R.id.time_name);
        lDate = $(R.id.time_date);
        lCategory = $(R.id.compass_style);
        lName = $(R.id.time_detail);
    }

    @Override
    public void setData(InfoEntity1 info) {
        lTitle.setText(info.getTitle());
        lName.setText(info.getContent());

        SimpleDateFormat formatter = new SimpleDateFormat("MM-dd HH:mm");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(info.getTime());
        String timer = formatter.format(calendar.getTime());
        lDate.setText(timer);
        lCategory.setText(info.getCategory());
    }
}
