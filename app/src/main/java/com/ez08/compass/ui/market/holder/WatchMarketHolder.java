package com.ez08.compass.ui.market.holder;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ez08.compass.CompassApp;
import com.ez08.compass.R;
import com.ez08.compass.entity.StockCodeEntity;
import com.ez08.compass.entity.StockMarketEntity;
import com.ez08.compass.tools.TimeTool;
import com.ez08.compass.tools.UtilTools;
import com.ez08.compass.ui.base.BaseViewHolder;
import com.ez08.compass.ui.stocks.StockVertcialTabActivity;

import java.util.ArrayList;
import java.util.List;

public class WatchMarketHolder extends BaseViewHolder<StockMarketEntity> {

    TextView name;
    TextView codeTv;
    TextView value;
    TextView percent;

    public WatchMarketHolder(ViewGroup itemView) {
        super(itemView, R.layout.holder_market_simple);
        name = $(R.id.tv_stock_name);
        codeTv = $(R.id.tv_stock_code);
        value = $(R.id.tv_stock_value);
        percent = $(R.id.tv_stock_percent);
    }

    @Override
    public void setData(final StockMarketEntity entity) {
        if (TextUtils.isEmpty(entity.getSecuname())) {
            name.setText("--");
        } else {
            name.setText(entity.getSecuname());
        }
        if (TextUtils.isEmpty(entity.getSecucode())) {
            codeTv.setText("--");
        } else {
            String code = entity.getSecucode();
            if (code.contains("SZHQ") || code.contains("SHHQ")) {
                code = code.substring(4);
            }
            codeTv.setText(code);
        }
        if (entity.getLastclose() == 0 || entity.getCurrent() == 0 || entity.getState() != 0) {
            value.setText("— —");
            value.setTextColor(CompassApp.GLOBAL.LIGHT_GRAY);
            percent.setText("— —");
            percent.setTextColor(CompassApp.GLOBAL.LIGHT_GRAY);
        } else {
            double p = entity.getCurrent();
            int exp = entity.getExp();
            int deno = 100;
            if (exp == 4) {
                deno = 100;
            } else if (exp == 5) {
                deno = 1000;
            }
            double price = p / deno;
            String priced = UtilTools.getFormatNum(price + "", 2, true);
            value.setText(priced);
//            value.setTextColor(colorNormal);
            double in1 = entity.getCurrent() - entity.getLastclose();
            double in2 = entity.getLastclose();
            double increase = in1 / in2;
            increase = increase * 100; // 百分数
            increase = (double) (Math.round(increase * 100)) / 100;
            String inside = UtilTools.getFormatNum(increase + "", 2, true);
            if (increase > 0) {
                percent.setTextColor(CompassApp.GLOBAL.RED);
                value.setTextColor(CompassApp.GLOBAL.RED);
                inside = "+" + (inside) + "%";
            } else if (increase == 0) {
                inside = (inside) + "%";
                percent.setTextColor(CompassApp.GLOBAL.LIGHT_GRAY);
                value.setTextColor(CompassApp.GLOBAL.LIGHT_GRAY);
            } else {
                inside = (inside) + "%";
                percent.setTextColor(CompassApp.GLOBAL.GREEN);
                value.setTextColor(CompassApp.GLOBAL.GREEN);
            }
            while (inside.length() < 7) {
                inside = " " + inside;
            }
            percent.setText(inside);
        }
        if (TimeTool.isInTotalTrade()) { // 集合竞价阶段，如果股票价格不为0，就显示价格，不区分是否停牌
            //-----
            percent.setText("— —");
            percent.setTextColor(CompassApp.GLOBAL.LIGHT_GRAY);
            value.setText("— —");
            value.setTextColor(CompassApp.GLOBAL.LIGHT_GRAY);
        }

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CompassApp.GLOBAL.mStockList.clear();
                String code = entity.getSecucode();
                StockCodeEntity codeEntity = new StockCodeEntity();
                codeEntity.code=code;
                List<String> codes = new ArrayList<String>();
//                for(int i=0;i<mStock_capatal_list.size();i++){
//                    codes.add(mStock_capatal_list.get(i).getSecucode());
//                }
                codeEntity.codes = codes;
                CompassApp.GLOBAL.mStockList.add(codeEntity);
                Intent intent = new Intent(getContext(),
                        StockVertcialTabActivity.class);
                getContext().startActivity(intent);

            }
        });
    }
}
