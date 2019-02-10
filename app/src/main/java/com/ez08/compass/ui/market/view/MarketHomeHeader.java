package com.ez08.compass.ui.market.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ez08.compass.CompassApp;
import com.ez08.compass.R;
import com.ez08.compass.entity.ItemStock;
import com.ez08.compass.entity.PlateMarketEntity;
import com.ez08.compass.entity.SimpleEntity;
import com.ez08.compass.entity.StockCodeEntity;
import com.ez08.compass.entity.StockMarketEntity;
import com.ez08.compass.tools.StockUtils;
import com.ez08.compass.tools.TimeTool;
import com.ez08.compass.tools.UtilTools;
import com.ez08.compass.ui.market.IndexListActivity;
import com.ez08.compass.ui.market.PlateLListActivity;
import com.ez08.compass.ui.market.StockListActivity;
import com.ez08.compass.ui.stocks.StockVertcialTabActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/8/24.
 * 行情
 */
public class MarketHomeHeader extends LinearLayout implements View.OnClickListener {
    private ArrayList<StockMarketEntity> stocklist1;
    private ArrayList<PlateMarketEntity> boardlist0;
    private ArrayList<StockMarketEntity> stocklist2;
    private ArrayList<PlateMarketEntity> boardlist1;
    private ArrayList<PlateMarketEntity> boardlist2;
    private LinearLayout marketIndex;
    private LinearLayout marketUp;
    private LinearLayout marketDown;
    private LinearLayout marketListUp;
    private LinearLayout marketListDown;
    private Context mContext;
    private int colorShadowTv, redColor, greenColor;

    StockHeader stockHeader;

    public MarketHomeHeader(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        TypedArray a = context.obtainStyledAttributes(null, R.styleable.main_attrs, 0, 0);
        colorShadowTv = getResources().getColor(a.getResourceId(R.styleable.main_attrs_lable_list_style, 0));
        redColor = getResources().getColor(a.getResourceId(R.styleable.main_attrs_red_main_color, 0));
        greenColor = getResources().getColor(a.getResourceId(R.styleable.main_attrs_green_main_color, 0));
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        init();
    }

    public void setData(ArrayList<StockMarketEntity> stocklist1, ArrayList<StockMarketEntity> stocklist2, ArrayList<PlateMarketEntity> boardlist0, ArrayList<PlateMarketEntity> boardlist1, ArrayList<PlateMarketEntity> boardlist2) {
        this.stocklist1 = stocklist1;
        this.stocklist2 = stocklist2;
        this.boardlist1 = boardlist1;
        this.boardlist2 = boardlist2;
        this.boardlist0 = boardlist0;
        initData();
    }

    public void init() {
        findViewById(R.id.market_up_btn).setOnClickListener(this);
        findViewById(R.id.market_down_btn).setOnClickListener(this);
        findViewById(R.id.stock_up_btn).setOnClickListener(this);
        findViewById(R.id.market_index_btn).setOnClickListener(this);
        findViewById(R.id.stock_down_btn).setOnClickListener(this);
        LinearLayout.LayoutParams params;

        stockHeader = (StockHeader) findViewById(R.id.stock_header);
        stockHeader.init();
        marketIndex = (LinearLayout) findViewById(R.id.market_index);
        for (int i = 0; i < 3; i++) {
            View item = View.inflate(mContext, R.layout.matket_index_layout, null);
            params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1);
            item.setLayoutParams(params);
            marketIndex.addView(item);
            final int index = i;
            item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(boardlist0 == null){
                        return;
                    }
                    if (boardlist0.size() <= index) {
                        return;
                    }

                    CompassApp.GLOBAL.mStockList.clear();
                    StockCodeEntity entity = new StockCodeEntity();
                    String code = boardlist0.get(index).getBoardcode();
                    entity.code = StockUtils.getStockCode(code);
                    List<String> codes = new ArrayList<String>();
                    for (int i = 0; i < boardlist0.size(); i++) {
                        codes.add(boardlist0.get(i).getBoardcode());
                    }
                    entity.codes = codes;
                    CompassApp.GLOBAL.mStockList.add(entity);
                    Intent intent = new Intent(mContext, StockVertcialTabActivity.class);
                    mContext.startActivity(intent);

                }
            });
        }


        marketUp = (LinearLayout) findViewById(R.id.market_up);

        for (int i = 0; i < 3; i++) {
            View item = View.inflate(mContext, R.layout.matket_plate_layout, null);
            params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1);
            item.setLayoutParams(params);
            marketUp.addView(item);
            final int index = i;
            item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(boardlist1 == null){
                        return;
                    }
                    if (boardlist1.size() <= index || TextUtils.isEmpty(boardlist1.get(index).getFirststockcode())) {
                        return;
                    }

                    CompassApp.GLOBAL.mStockList.clear();
                    StockCodeEntity entity = new StockCodeEntity();
                    String code = boardlist1.get(index).getBoardcode();
                    entity.code = StockUtils.getStockCode(code);
                    List<String> codes = new ArrayList<String>();
                    for (int i = 0; i < boardlist1.size(); i++) {
                        codes.add(boardlist1.get(i).getBoardcode());
                    }
                    entity.codes = codes;
                    CompassApp.GLOBAL.mStockList.add(entity);

                    Intent intent = new Intent(mContext, StockVertcialTabActivity.class);
                    mContext.startActivity(intent);

                }
            });
        }
        marketDown = (LinearLayout) findViewById(R.id.market_down);
        for (int i = 0; i < 3; i++) {
            View item = View.inflate(mContext, R.layout.matket_plate_layout, null);
            params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1);
            item.setLayoutParams(params);
            marketDown.addView(item);
            final int index = i;
            item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(boardlist2 == null){
                        return;
                    }
                    if (boardlist2.size() <= index || TextUtils.isEmpty(boardlist2.get(index).getFirststockcode())) {
//                        Toast.makeText(mContext, "此版块暂无股票", Toast.LENGTH_LONG).show();
                        return;
                    }

                    CompassApp.GLOBAL.mStockList.clear();
                    StockCodeEntity entity = new StockCodeEntity();
                    String code = boardlist2.get(index).getBoardcode();
                    entity.code = StockUtils.getStockCode(code);
                    List<String> codes = new ArrayList<String>();
                    for (int i = 0; i < boardlist2.size(); i++) {
                        codes.add(boardlist2.get(i).getBoardcode());
                    }
                    entity.codes = codes;
                    CompassApp.GLOBAL.mStockList.add(entity);

                    Intent intent = new Intent(mContext, StockVertcialTabActivity.class);
                    mContext.startActivity(intent);

                }
            });
        }
        marketListUp = (LinearLayout) findViewById(R.id.market_up_list);
        for (int i = 0; i < 5; i++) {
            View item = View.inflate(mContext, R.layout.market_list_layout, null);
            params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            item.setLayoutParams(params);
            marketListUp.addView(item);
            if (i == 4) {
                item.findViewById(R.id.watch_line).setVisibility(GONE);
            }
            final int index = i;
            item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(stocklist1 == null){
                        return;
                    }
                    if (index >= stocklist1.size()) {
                        return;
                    }

                    CompassApp.GLOBAL.mStockList.clear();
                    StockCodeEntity entity = new StockCodeEntity();
                    String code = stocklist1.get(index).getSecucode();
                    entity.code = StockUtils.getStockCode(code);
                    List<String> codes = new ArrayList<String>();
                    for (int i = 0; i < stocklist1.size(); i++) {
                        codes.add(stocklist1.get(i).getSecucode());
                    }
                    entity.codes = codes;
                    CompassApp.GLOBAL.mStockList.add(entity);

                    if (TextUtils.isEmpty(code)) {
                        return;
                    }
                    Intent intent = new Intent(mContext,
                            StockVertcialTabActivity.class);
                    mContext.startActivity(intent);

                }
            });
        }
        marketListDown = (LinearLayout) findViewById(R.id.market_down_list);
        for (int i = 0; i < 5; i++) {
            View item = View.inflate(mContext, R.layout.market_list_layout, null);
            params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            item.setLayoutParams(params);
            marketListDown.addView(item);

            final int index = i;
            item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(stocklist2 == null){
                        return;
                    }

                    if (index >= stocklist2.size()) {
                        return;
                    }

                    CompassApp.GLOBAL.mStockList.clear();
                    StockCodeEntity entity = new StockCodeEntity();
                    String code = stocklist2.get(index).getSecucode();
                    entity.code = StockUtils.getStockCode(code);
                    List<String> codes = new ArrayList<String>();
                    for (int i = 0; i < stocklist2.size(); i++) {
                        codes.add(stocklist2.get(i).getSecucode());
                    }
                    entity.codes = codes;
                    CompassApp.GLOBAL.mStockList.add(entity);

                    if (TextUtils.isEmpty(code)) {
                        return;
                    }
                    Intent intent = new Intent(mContext,
                            StockVertcialTabActivity.class);
                    mContext.startActivity(intent);

                }
            });
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.market_index_btn:
                Intent marketIndex = new Intent(mContext, IndexListActivity.class);
                SimpleEntity entityIn = new SimpleEntity();
                entityIn.setSorttype(0);
                marketIndex.putExtra("seri", entityIn);
                mContext.startActivity(marketIndex);
                CompassApp.addStatis(CompassApp.GLOBAL.mgr.STOCK_MARKET, "1", "",
                        System.currentTimeMillis());
                break;
            case R.id.market_up_btn:
                Intent marketUp = new Intent(mContext, PlateLListActivity.class);
                SimpleEntity entity0 = new SimpleEntity();
                entity0.setSorttype(0);
                marketUp.putExtra("seri", entity0);
                mContext.startActivity(marketUp);
                CompassApp.addStatis(CompassApp.GLOBAL.mgr.STOCK_MARKET, "2", "",
                        System.currentTimeMillis());
                break;
            case R.id.market_down_btn:
                Intent marketDown = new Intent(mContext, PlateLListActivity.class);
                SimpleEntity entity1 = new SimpleEntity();
                entity1.setSorttype(1);
                marketDown.putExtra("seri", entity1);
                mContext.startActivity(marketDown);
                CompassApp.addStatis(CompassApp.GLOBAL.mgr.STOCK_MARKET, "3", "",
                        System.currentTimeMillis());
                break;
            case R.id.stock_up_btn:
                Intent stockUp = new Intent(mContext, StockListActivity.class);
                SimpleEntity entity2 = new SimpleEntity();
                entity2.setSorttype(0);
                entity2.setType(0);
                stockUp.putExtra("seri", entity2);
                entity2.setTitleName("全部股票");
                mContext.startActivity(stockUp);
                CompassApp.addStatis(CompassApp.GLOBAL.mgr.STOCK_MARKET, "4", "",
                        System.currentTimeMillis());
                break;
            case R.id.stock_down_btn:
                Intent stockDown = new Intent(mContext, StockListActivity.class);
                SimpleEntity entity3 = new SimpleEntity();
                entity3.setSorttype(1);
                entity3.setType(0);
                stockDown.putExtra("seri", entity3);
                entity3.setTitleName("全部股票");
                mContext.startActivity(stockDown);
                CompassApp.addStatis(CompassApp.GLOBAL.mgr.STOCK_MARKET, "5", "",
                        System.currentTimeMillis());
                break;
        }
    }

    private void setIncrease(TextView view, double increase) {
        String inside = UtilTools.getFormatNum(increase + "", 2, true);
        if (increase > 0) {
            view
                    .setTextColor(redColor);
            inside = "+" + (inside) + "%";
        } else if (increase == 0) {
            inside = (inside) + "%";
            view.setTextColor(getResources().getColor(R.color.shadow0));
        } else {
            inside = (inside) + "%";
            view.setTextColor(greenColor);
        }
        while (inside.length() < 7) {
            inside = " " + inside;
        }
        view.setText(inside);
        if (TimeTool.isInTotalTrade()) {
            view.setText("— —");
            view.setTextColor(getResources().getColor(R.color.shadow0));
        } else if (TimeTool.isInTotalTrade()) { // 集合竞价阶段，如果股票价格不为0，就显示价格，不区分是否停牌
            //
        }
    }

    private void setIncrease(TextView view, double current, double last) {
        double in1 = current - last;
        double in2 = last;
        double increase = in2 == 0 ? 0 : in1 / in2;
        increase = increase * 100; // 百分数
        increase = (double) (Math.round(increase * 100)) / 100;
        String inside = UtilTools.getFormatNum(increase + "", 2, true);
        double mIncrease = Double.parseDouble(inside);
        setIncrease(view, mIncrease);
    }

    private void initData() {

        for (int i = 0; i < marketIndex.getChildCount(); i++) {
            View item = marketIndex.getChildAt(i);
            if (boardlist0.size() > i) {
                TextView lBoardName = (TextView) item.findViewById(R.id.market_top_name);
                TextView lBoardIncrease = (TextView) item.findViewById(R.id.market_index_increase);
                TextView lBoardPrice = (TextView) item.findViewById(R.id.market_index_price);
                PlateMarketEntity entity = boardlist0.get(i);
                lBoardName.setText(entity.getBoardname());

                setIncrease(lBoardIncrease, entity.getCurrent(), entity.getLastclose());

                if (entity.getCurrent() == 0) {
                    lBoardPrice.setText("— —");
                    lBoardPrice.setTextColor(getResources().getColor(R.color.shadow0));
                    lBoardIncrease.setText("— —");
                    lBoardIncrease.setTextColor(getResources().getColor(R.color.shadow0));
                } else {
                    double in = entity.getCurrent();
                    int exp = entity.getExp();
                    int deno = 100;
                    if (exp == 4) {
                        deno = 100;
                    } else if (exp == 5) {
                        deno = 1000;
                    }
                    double current = in / deno;
                    String currentd = UtilTools.getFormatNum(current + "", 3, true);
                    if (entity.getCurrent() >= entity.getLastclose()) {
                        lBoardPrice.setTextColor(redColor);
                    } else {
                        lBoardPrice.setTextColor(greenColor);
                    }

                    lBoardPrice.setText(currentd);
                }

                if (TimeTool.isInTotalTradePlate()) { // 早八点到集合竞价阶段，所有股票都显示"- -"
                    lBoardName.setText("— —");
                    lBoardName.setTextColor(getResources().getColor(R.color.shadow0));
                    lBoardIncrease.setText("— —");
                    lBoardPrice.setText("— —");
                    lBoardIncrease.setTextColor(getResources().getColor(R.color.shadow0));
                    lBoardPrice.setTextColor(getResources().getColor(R.color.shadow0));
                }

            } else {
                item.setVisibility(View.VISIBLE);
            }
        }

        for (int i = 0; i < marketUp.getChildCount(); i++) {
            View item = marketUp.getChildAt(i);
            TextView lStockName = (TextView) item.findViewById(R.id.market_top_stock_name);
            if (boardlist1.size() > i) {
                TextView lBoardName = (TextView) item.findViewById(R.id.market_top_name);
                TextView lBoardIncrease = (TextView) item.findViewById(R.id.market_top_increase);
                TextView lStockIncrease = (TextView) item.findViewById(R.id.market_top_stock_increase);
                PlateMarketEntity entity = boardlist1.get(i);
                lBoardName.setText(entity.getBoardname());
                setIncrease(lBoardIncrease, entity.getCurrent(), entity.getLastclose());
                double in = entity.getFirststockzf();
                double zf = in / 100;
                setIncrease(lStockIncrease, zf);
                if (TimeTool.isInTotalTradePlate()) { // 早八点到集合竞价阶段，所有股票都显示"- -"
                    lStockName.setText("— —");
                    lBoardName.setText("— —");
                    lStockName.setTextColor(getResources().getColor(R.color.shadow0));
                    lBoardName.setTextColor(getResources().getColor(R.color.shadow0));
                    lBoardIncrease.setText("— —");
                    lStockIncrease.setText("— —");
                    lBoardIncrease.setTextColor(getResources().getColor(R.color.shadow0));
                    lStockIncrease.setTextColor(getResources().getColor(R.color.shadow0));
                } else {
                    lStockName.setText(entity.getFirststockname());
                }

            } else {
                item.setVisibility(View.VISIBLE);
                lStockName.setText("— —");
            }
        }

        for (int i = 0; i < marketDown.getChildCount(); i++) {
            View item = marketDown.getChildAt(i);
            TextView lStockName = (TextView) item.findViewById(R.id.market_top_stock_name);
            if (boardlist2.size() > i) {
                TextView lBoardName = (TextView) item.findViewById(R.id.market_top_name);
                TextView lBoardIncrease = (TextView) item.findViewById(R.id.market_top_increase);
                TextView lStockIncrease = (TextView) item.findViewById(R.id.market_top_stock_increase);
                PlateMarketEntity entity = boardlist2.get(i);
                lBoardName.setText(entity.getBoardname());
                setIncrease(lBoardIncrease, entity.getCurrent(), entity.getLastclose());
                lStockName.setText(entity.getFirststockname());
                double in = entity.getFirststockzf();
                double zf = in / 100;
                setIncrease(lStockIncrease, zf);
                if (TimeTool.isInTotalTradePlate()) { // 早八点到集合竞价阶段，所有股票都显示"- -"
                    lStockName.setText("— —");
                    lBoardName.setText("— —");
                    lStockName.setTextColor(getResources().getColor(R.color.shadow0));
                    lBoardName.setTextColor(getResources().getColor(R.color.shadow0));
                    lBoardIncrease.setText("— —");
                    lStockIncrease.setText("— —");
                    lBoardIncrease.setTextColor(getResources().getColor(R.color.shadow0));
                    lStockIncrease.setTextColor(getResources().getColor(R.color.shadow0));
                } else {
                    lStockName.setText(entity.getFirststockname());
                }
            } else {
                item.setVisibility(View.VISIBLE);
                lStockName.setText("— —");
            }
        }

        for (int i = 0; i < marketListUp.getChildCount(); i++) {
            View item = marketListUp.getChildAt(i);
            if (stocklist1.size() > i) {
                StockMarketEntity entity = stocklist1.get(i);
                if (entity != null) {
                    TextView lStockName = (TextView) item.findViewById(R.id.stock_top_name);
                    TextView lStockCode = (TextView) item.findViewById(R.id.stock_top_code);
                    TextView lStockValue = (TextView) item.findViewById(R.id.stock_top_value);
                    TextView lStockIncrease = (TextView) item.findViewById(R.id.stock_top_increase);
                    lStockName.setText(entity.getSecuname());
                    String code = entity.getSecucode();
                    if (code.contains("SZHQ") || code.contains("SHHQ")) {
                        code = code.substring(4);
                    }
                    lStockCode.setText(code);
                    if (entity.getState() != 0 && entity.getCurrent() == 0) {
                        lStockValue.setText("— —");
                        lStockIncrease.setText("— —");
                        lStockValue.setTextColor(colorShadowTv);
                        lStockIncrease.setTextColor(colorShadowTv);
                    } else {
                        double in = entity.getCurrent();
                        int exp = entity.getExp();
                        int deno = 100;
                        if (exp == 4) {
                            deno = 100;
                        } else if (exp == 5) {
                            deno = 1000;
                        }
                        double current = in / deno;
                        String currentd = UtilTools.getFormatNum(current + "", 2, true);
                        int ind = entity.getCurrent() - entity.getLastclose();
                        if (ind > 0) {
                            lStockValue.setTextColor(redColor);
                        } else if (ind == 0) {
                            lStockValue.setTextColor(colorShadowTv);
                        } else {
                            lStockValue.setTextColor(greenColor);
                        }
                        if (TimeTool.isInTotalTrade()) { // 早八点到集合竞价阶段，所有股票都显示"- -"
                            lStockValue.setTextColor(colorShadowTv);
                            lStockValue.setText("— —");
                        } else {
                            lStockValue.setText(currentd);
                        }

                        setIncrease(lStockIncrease, entity.getCurrent(), entity.getLastclose());
                    }

                } else {
                    item.setVisibility(View.VISIBLE);
                }
            }
        }

        for (int i = 0; i < marketListDown.getChildCount(); i++) {
            View item = marketListDown.getChildAt(i);
            if (stocklist2.size() == 0 || i >= stocklist2.size()) {
                return;
            }
            StockMarketEntity entity = stocklist2.get(i);
            if (entity != null) {
                if (stocklist2.size() > i) {
                    TextView lStockName = (TextView) item.findViewById(R.id.stock_top_name);
                    TextView lStockCode = (TextView) item.findViewById(R.id.stock_top_code);
                    TextView lStockValue = (TextView) item.findViewById(R.id.stock_top_value);
                    TextView lStockIncrease = (TextView) item.findViewById(R.id.stock_top_increase);

                    lStockName.setText(entity.getSecuname());
                    String code = entity.getSecucode();
                    if (code.contains("SZHQ") || code.contains("SHHQ")) {
                        code = code.substring(4);
                    }
                    lStockCode.setText(code);
                    if (entity.getState() != 0 && entity.getCurrent() == 0) {
                        lStockValue.setText("— —");
                        lStockIncrease.setText("— —");
                        lStockValue.setTextColor(colorShadowTv);
                        lStockIncrease.setTextColor(colorShadowTv);
                    } else {
                        double in = entity.getCurrent();
                        int exp = entity.getExp();
                        int deno = 100;
                        if (exp == 4) {
                            deno = 100;
                        } else if (exp == 5) {
                            deno = 1000;
                        }
                        double current = in / deno;
                        String currentd = UtilTools.getFormatNum(current + "", 2, true);
                        int ind = entity.getCurrent() - entity.getLastclose();
                        if (ind > 0) {
                            lStockValue.setTextColor(redColor);
                        } else if (ind == 0) {
                            lStockValue.setTextColor(colorShadowTv);
                        } else {
                            lStockValue.setTextColor(greenColor);
                        }

                        if (TimeTool.isInTotalTrade()) { // 早八点到集合竞价阶段，所有股票都显示"- -"
                            lStockValue.setTextColor(colorShadowTv);
                            lStockValue.setText("— —");
                        } else {
                            lStockValue.setText(currentd);
                        }
                        setIncrease(lStockIncrease, entity.getCurrent(), entity.getLastclose());
                    }
                } else {
                    item.setVisibility(View.VISIBLE);
                }
            }
        }
    }


    public ArrayList<ItemStock> getStockList() {

        if (stockHeader != null) {
            return stockHeader.getStockList();
        }
        return null;
    }

    public void setHeader() {
        if (stockHeader != null) {
            stockHeader.setHeader();
        }
    }


}
