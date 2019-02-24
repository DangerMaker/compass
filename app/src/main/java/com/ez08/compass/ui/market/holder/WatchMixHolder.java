package com.ez08.compass.ui.market.holder;

import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ez08.compass.CompassApp;
import com.ez08.compass.R;
import com.ez08.compass.entity.Mix3Entity;
import com.ez08.compass.entity.StockCodeEntity;
import com.ez08.compass.ui.base.BaseViewHolder;
import com.ez08.compass.ui.stocks.StockVerticalActivity;

import java.util.ArrayList;
import java.util.List;

public class WatchMixHolder extends BaseViewHolder<Mix3Entity> {

    TextView name;
    TextView code;
    TextView people;
    TextView media;
    TextView capital;

    public WatchMixHolder(ViewGroup itemView) {
        super(itemView, R.layout.holder_mix_list);
        name = $(R.id.tv_stock_name);
        code = $(R.id.tv_stock_code);
        people = $(R.id.tv_stock_people_notice_count);
        media = $(R.id.tv_stock_news_notice_count);
        capital = $(R.id.tv_stock_capital_notice_count);
    }

    @Override
    public void setData(final Mix3Entity entity) {
        code.setText(entity.getStockCode().substring(4));
        name.setText(entity.getStockName());
        people.setText(entity.getStockPeopleCount() + "");
        media.setText(entity.getStockNewsCount() + "");
        capital.setText(entity.getStockCapitalCount() + "");
        if (entity.getStockCapitalCount() == -1) {
            capital.setText("--");
        }
        if (entity.getStockNewsCount() == -1) {
            media.setText("--");
        }
        if (entity.getStockPeopleCount() == -1) {
            people.setText("--");
        }

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CompassApp.GLOBAL.mStockList.clear();
                StockCodeEntity codeEntity = new StockCodeEntity();
                String code = entity.getStockCode();
                code = code.toUpperCase();
                codeEntity.code = code;
                List<String> codes = new ArrayList<String>();
//                for (int i = 0; i < mStock_fix_3_list.size(); i++) {
//                    codes.add(mStock_fix_3_list.get(i).getStockCode().replaceAll("HQ", "").toUpperCase());
//                }
                codeEntity.codes = codes;
                CompassApp.GLOBAL.mStockList.add(codeEntity);
                Intent intent = new Intent(getContext(),
                        StockVerticalActivity.class);
                getContext().startActivity(intent);
            }
        });
    }
}
