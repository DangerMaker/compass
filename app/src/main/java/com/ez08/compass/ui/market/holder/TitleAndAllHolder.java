package com.ez08.compass.ui.market.holder;

import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ez08.compass.CompassApp;
import com.ez08.compass.R;
import com.ez08.compass.entity.SimpleEntity;
import com.ez08.compass.entity.HolderTitleAndAll;
import com.ez08.compass.ui.base.BaseViewHolder;
import com.ez08.compass.ui.stocks.StockTopActivity;

public class TitleAndAllHolder extends BaseViewHolder<HolderTitleAndAll> {

    TextView title;
    public TitleAndAllHolder(ViewGroup itemView) {
        super(itemView, R.layout.holder_title_all);
        title = $(R.id.title);
    }

    @Override
    public void setData(final HolderTitleAndAll data) {
        title.setText(data.getTitleName());
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent stockUp1 = new Intent(getContext(), StockTopActivity.class);
                SimpleEntity entity1 = new SimpleEntity();
                entity1.setSorttype(data.getSorttype());
                entity1.setType(data.getType());
                stockUp1.putExtra("seri", entity1);
                entity1.setTitleName(data.getTitleName());
                stockUp1.putExtra("sort",false);
                getContext().startActivity(stockUp1);

                String stats;
                if(data.getType() == 12){
                    stats = "1";
                }else if(data.getType() == 13){
                    stats = "2";
                }else{
                    stats = "3";
                }
                CompassApp.addStatis(CompassApp.GLOBAL.mgr.STOCK_WATCH, stats,"",
                        System.currentTimeMillis());
            }
        });
    }
}
