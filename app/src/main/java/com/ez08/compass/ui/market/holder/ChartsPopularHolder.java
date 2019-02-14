package com.ez08.compass.ui.market.holder;

import android.view.ViewGroup;
import android.widget.TextView;

import com.ez08.compass.R;
import com.ez08.compass.entity.HolderChartsNetWord;
import com.ez08.compass.ui.base.BaseViewHolder;

public class ChartsPopularHolder extends BaseViewHolder<HolderChartsNetWord> {

    TextView name;

    public ChartsPopularHolder(ViewGroup itemView) {
        super(itemView, R.layout.holder_charts_industry);
        name = $(R.id.industry_name);
    }

    @Override
    public void setData(HolderChartsNetWord data) {
        name.setText(data.getTitle());
    }
}
