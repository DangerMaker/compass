package com.ez08.compass.ui.market.holder;

import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ez08.compass.CompassApp;
import com.ez08.compass.R;
import com.ez08.compass.entity.PlateMarketEntity;
import com.ez08.compass.net.H5Interface;
import com.ez08.compass.ui.WebActivity;
import com.ez08.compass.ui.base.BaseViewHolder;

import java.util.List;

public class DingpanTailHolder extends BaseViewHolder<List<PlateMarketEntity>> {

    TextView title;
    public DingpanTailHolder(ViewGroup itemView) {
        super(itemView, R.layout.item_dingpan_tail);
        title = $(R.id.title);
        title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(getContext(), WebActivity.class);
                intent1.putExtra("title", "趋势活金");
                intent1.putExtra("url", H5Interface.QSHJ.intro(getContext()));
                getContext().startActivity(intent1);
                CompassApp.addStatis(CompassApp.GLOBAL.mgr.STOCK_FEATURE, "7", "",
                        System.currentTimeMillis());
            }
        });
    }
}
