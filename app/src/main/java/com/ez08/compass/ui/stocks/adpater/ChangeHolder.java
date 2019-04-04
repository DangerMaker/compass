package com.ez08.compass.ui.stocks.adpater;

import android.text.TextUtils;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ez08.compass.R;
import com.ez08.compass.entity.ItemStock;
import com.ez08.compass.tools.MyAppCompat;
import com.ez08.compass.tools.TimeTool;
import com.ez08.compass.tools.UtilTools;
import com.ez08.compass.ui.base.BaseViewHolder;

public class ChangeHolder extends BaseViewHolder<ItemStock> {
    public TextView stockName;
    public TextView stockCode;
    public TextView stockValue ;
    public TextView changeValue;

    int shadow0Color;
    int greenColor;
    int redColor;

    public ChangeHolder(ViewGroup itemView) {
        super(itemView, R.layout.market_list_layout);
         stockName = $(R.id.stock_top_name);
         stockCode = $(R.id.stock_top_code);
         stockValue = $(R.id.stock_top_value);
         changeValue = $(R.id.stock_top_increase);
         shadow0Color = MyAppCompat.getColor(getContext(),R.attr.shadow0);
         greenColor = MyAppCompat.getColor(getContext(),R.attr.green_main_color);
         redColor = MyAppCompat.getColor(getContext(),R.attr.red_main_color);
    }

    @Override
    public void setData(ItemStock sc) {
        stockName.setText(sc.getName());
        if(sc.getCode().length()==0){
            changeValue.setText("— —");
            stockValue.setText("— —");
            stockValue.setTextColor(shadow0Color);
            changeValue.setTextColor(MyAppCompat.getColor(getContext(),R.attr.shadow0));
            stockCode.setText("— —");
            stockName.setText("— —");
            return;
        }
        stockCode.setText(sc.getCode().substring(4));
        stockValue.setText(sc.getValue());
        if (TextUtils.isEmpty(sc.getIncrease())) {
            sc.setIncrease("0.0000");
        }
        float increase = Float.parseFloat(sc.getIncrease());
        increase = increase * 100; // 百分数
        increase = UtilTools.getRoundingNum(increase, 2);

        String inside = UtilTools.getFormatNum(increase + "", 2, true);

        if (increase > 0) {
            changeValue.setTextColor(redColor);
            stockValue.setTextColor(redColor);
            inside = "+" + (inside) + "%";
        } else if (increase == 0) {
            inside = (inside) + "%";
            changeValue.setTextColor(shadow0Color);
            stockValue.setTextColor(shadow0Color);
        } else {
            inside = (inside) + "%";
            changeValue.setTextColor(greenColor);
            stockValue.setTextColor(greenColor);
        }
        while (inside.length() < 7) {
            inside = " " + inside;
        }
        changeValue.setText(inside);
        if (TimeTool.isInTotalTrade()) { // 集合竞价阶段，如果股票价格不为0，就显示价格，不区分是否停牌
            changeValue.setText("— —");
            stockValue.setText("— —");
            changeValue.setTextColor(shadow0Color);
            stockValue.setTextColor(shadow0Color);
        } else {
            if (TextUtils.isEmpty(sc.getAmount())
                    || "0".equalsIgnoreCase(sc.getAmount())
                    || "0.0".equalsIgnoreCase(sc.getAmount())
                    || "0.00".equalsIgnoreCase(sc.getAmount())
                    || TextUtils.isEmpty(sc.getValue())
                    || "0".equalsIgnoreCase(sc.getValue())
                    || "0.0".equalsIgnoreCase(sc.getValue())
                    || "0.00".equalsIgnoreCase(sc.getValue())) {
                changeValue.setText("— —");
                stockValue.setText("— —");
                changeValue.setTextColor(shadow0Color);
                stockValue.setTextColor(shadow0Color);
            }

        }
        String value = sc.getValue();
        if ((sc.getState() != 0 && sc.getState() != 2) || (value.equals("0.00") || value.equals("0.000"))) { //不是开盘和临时停牌
            changeValue.setText("— —");
            stockValue.setText("— —");
            changeValue.setTextColor(shadow0Color);
            stockValue.setTextColor(shadow0Color);
        }
    }
}
