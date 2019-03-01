package com.ez08.compass.ui.market.holder;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ez08.compass.CompassApp;
import com.ez08.compass.R;
import com.ez08.compass.entity.PlateMarketEntity;
import com.ez08.compass.entity.StockCodeEntity;
import com.ez08.compass.tools.JumpHelper;
import com.ez08.compass.tools.StockUtils;
import com.ez08.compass.tools.TimeTool;
import com.ez08.compass.tools.UtilTools;
import com.ez08.compass.ui.base.BaseViewHolder;
import com.ez08.compass.ui.stocks.StockVerticalActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DingPanItemHolder extends BaseViewHolder<List<PlateMarketEntity>> {

    TextView name1;
    TextView name2;
    TextView name3;
    TextView price1;
    TextView price2;
    TextView price3;
    TextView title;
    RelativeLayout bg1;
    RelativeLayout bg2;
    RelativeLayout bg3;

    boolean state = true; // true up,false down
    String[] highs = {};
    String[] lows = {};

    int[] highsBg = {R.color.hotpink, R.color.darkorange, R.color.gold};
    int[] lowsBg = {R.color.lightgreen, R.color.lightskyblue, R.color.skyblue};

    String[] titleList = {"流通盘维度", "业绩维度", "价格维度", "上市日期维度", "控盘度维度", "基金维度"};
    String[] codesList = {"BR01B04001", "BR01B04002", "BR01B04003",
            "BR01B04004", "BR01B04005", "BR01B04006",
            "BR01B04007", "BR01B04008", "BR01B04009",
            "BR01B04010", "BR01B04011", "BR01B04012",
            "BR01B04013", "BR01B04014", "BR01B04015",
            "BR01B04016", "BR01B04017", "BR01B04018"};

    public DingPanItemHolder(ViewGroup itemView) {
        super(itemView, R.layout.item_dingpan);
        title = $(R.id.title);
        name1 = $(R.id.market_top_name1);
        name2 = $(R.id.market_top_name2);
        name3 = $(R.id.market_top_name3);
        price1 = $(R.id.market_index_price1);
        price2 = $(R.id.market_index_price2);
        price3 = $(R.id.market_index_price3);
        bg1 = $(R.id.background1);
        bg2 = $(R.id.background2);
        bg3 = $(R.id.background3);
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public void setHighsAndLows(String arg1[], String arg2[]) {
        this.highs = arg1;
        this.lows = arg2;
    }

    @Override
    public void setData(final List<PlateMarketEntity> data) {
        title.setText(titleList[getDataPosition() / titleList.length]);
        if (data.size() >= 3) {
            final PlateMarketEntity entity1 = data.get(0);
            PlateMarketEntity entity2 = data.get(1);
            PlateMarketEntity entity3 = data.get(2);

            name1.setText(entity1.getBoardname());
            name2.setText(entity2.getBoardname());
            name3.setText(entity3.getBoardname());
            setIncrease(price1, entity1);
            setIncrease(price2, entity2);
            setIncrease(price3, entity3);

            bg1.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.transparent)); //
            bg2.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.transparent)); //
            bg3.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.transparent)); //

            setListener(bg1, entity1);
            setListener(bg2, entity2);
            setListener(bg3, entity1);

            if (!TimeTool.isInTotalTradePlate()) {

                if (state) {
                    for (int k = 0; k < highs.length; k++) {
                        if (highs[k].equals(entity1.getBoardcode())) {
                            bg1.setBackgroundColor(ContextCompat.getColor(getContext(), highsBg[k]));
                        }

                        if (highs[k].equals(entity2.getBoardcode())) {
                            bg2.setBackgroundColor(ContextCompat.getColor(getContext(), highsBg[k]));
                        }

                        if (highs[k].equals(entity3.getBoardcode())) {
                            bg3.setBackgroundColor(ContextCompat.getColor(getContext(), highsBg[k]));
                        }
                    }
                } else {
                    for (int k = 0; k < lows.length; k++) {
                        if (lows[k].equals(entity1.getBoardcode())) {
                            bg1.setBackgroundColor(ContextCompat.getColor(getContext(), lowsBg[k]));
                        }

                        if (lows[k].equals(entity2.getBoardcode())) {
                            bg2.setBackgroundColor(ContextCompat.getColor(getContext(), lowsBg[k]));
                        }

                        if (lows[k].equals(entity3.getBoardcode())) {
                            bg3.setBackgroundColor(ContextCompat.getColor(getContext(), lowsBg[k]));
                        }
                    }
                }

            }


        }
    }


    private void setListener(RelativeLayout bg, final PlateMarketEntity marketEntity) {
        bg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> temp = new ArrayList<>();
                for (int i = 0; i < codesList.length; i++) {
                    temp.add(codesList[i]);
                }

                JumpHelper.startStockVerticalActivity(getContext(), marketEntity.getBoardcode(), temp);
            }
        });

    }

    private void setIncrease(TextView view, PlateMarketEntity entity) {
        double in1 = entity.getCurrent() - entity.getLastclose();
        double in2 = entity.getLastclose();
        double increase = in2 == 0 ? 0 : in1 / in2;
        increase = increase * 100; // 百分数
        increase = (double) (Math.round(increase * 100)) / 100;
        String inside = UtilTools.getFormatNum(increase + "", 2, true);
        if (increase > 0) {
            view.setTextColor(CompassApp.GLOBAL.RED);
            inside = "+" + (inside) + "%";
        } else if (increase == 0) {
            inside = (inside) + "%";
            view.setTextColor(CompassApp.GLOBAL.LIGHT_GRAY);
        } else {
            inside = (inside) + "%";
            view.setTextColor(CompassApp.GLOBAL.GREEN);
        }
        while (inside.length() < 7) {
            inside = " " + inside;
        }
        view.setText(inside);
        if (TimeTool.isInTotalTradePlate()) {
            view.setText("— —");
            view.setTextColor(CompassApp.GLOBAL.LIGHT_GRAY);
        }

    }
}
