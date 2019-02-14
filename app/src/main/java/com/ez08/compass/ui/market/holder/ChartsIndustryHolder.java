package com.ez08.compass.ui.market.holder;

import android.view.ViewGroup;
import android.widget.TextView;

import com.ez08.compass.CompassApp;
import com.ez08.compass.R;
import com.ez08.compass.entity.HotModel;
import com.ez08.compass.ui.base.BaseViewHolder;

public class ChartsIndustryHolder extends BaseViewHolder<HotModel> {

    TextView name;
    TextView days;

    public ChartsIndustryHolder(ViewGroup itemView) {
        super(itemView, R.layout.holder_charts_industry);
        name = $(R.id.industry_name);
        days = $(R.id.last_days);
    }

    @Override
    public void setData(HotModel data) {
        name.setText(data.name);
        int num = data.hotValue;
        if (num > 0) {
            name.setTextColor(CompassApp.GLOBAL.RED);
            days.setTextColor(CompassApp.GLOBAL.RED);
        } else if (num < 0) {
            name.setTextColor(CompassApp.GLOBAL.GREEN);
            days.setTextColor(CompassApp.GLOBAL.GREEN);
        }else{
            name.setTextColor(CompassApp.GLOBAL.BLACK);
            days.setTextColor(CompassApp.GLOBAL.BLACK);
        }

        int abs = Math.abs(num);

        String day;
        if (abs == 3) {
            day = "③";
        } else if (abs == 5) {
            day = "⑤";
        } else if (abs == 2) {
            day = "②";
        } else if (abs == 4) {
            day = "④";
        } else if (abs == 1) {
            day = "①";
        } else {
            day = "";
        }
        days.setText(day);
    }
}
