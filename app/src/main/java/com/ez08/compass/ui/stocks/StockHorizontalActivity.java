package com.ez08.compass.ui.stocks;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.ez08.compass.R;
import com.ez08.compass.entity.StockDetailEntity;
import com.ez08.compass.net.NetInterface;
import com.ez08.compass.parser.StockDetailParser;
import com.ez08.compass.tools.StockUtils;
import com.ez08.compass.ui.base.BaseActivity;
import com.ez08.support.net.EzMessage;
import com.ez08.support.net.NetResponseHandler2;
import com.ez08.support.util.EzValue;
import com.ez08.tools.IntentTools;

import java.util.List;

public class StockHorizontalActivity extends BaseActivity implements View.OnClickListener {

    private final int WHAT_GET_STOCK_DETAIL = 10001;    //获取股票详细报价信息

    RelativeLayout nameLayout;
    ImageView indexChipsView;
    RecyclerView stocksListView;
    RecyclerView indexListView;
    RelativeLayout chipsView;

    boolean state = true; // true up,false down
    boolean stockListShowing = false;
    List<String> stockList;
    String stockCode;
    StockDetailEntity detailEntity;

    FragmentManager fragmentManager;
    KLineFragment kLineFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_stock_horizontal);

        Intent getIntent = getIntent();
        if (getIntent != null) {
            stockList = getIntent.getStringArrayListExtra("stock_list");
            stockCode = getIntent.getStringExtra("stock_code");
        }


        initView();
        initData();
    }

    private void initView() {
        nameLayout = findViewById(R.id.name_layout);
        nameLayout.setOnClickListener(this);
        indexChipsView = findViewById(R.id.index_chips_select);
        indexChipsView.setOnClickListener(this);

        stocksListView = findViewById(R.id.stock_list_view);
        indexListView = findViewById(R.id.index_list_view);
        chipsView = findViewById(R.id.chips_view);
    }

    public void initData() {
        fragmentManager = getSupportFragmentManager();
        indexChipsView.setSelected(state);
        setIndexChipsState();
        setStockListViewState();

        if (!TextUtils.isEmpty(stockCode))
            NetInterface.getStockDetailNew(mHandler, WHAT_GET_STOCK_DETAIL, stockCode);
    }

    @SuppressLint("HandlerLeak")
    NetResponseHandler2 mHandler = new NetResponseHandler2() {
        @Override
        public void netConnectLost(int arg0) {
        }

        @Override
        public void timeout(int arg0) {
        }

        @Override
        public void receive(int arg0, boolean b, Intent intent) {
            switch (arg0) {
                case WHAT_GET_STOCK_DETAIL: //获取股票详细报价信息
                    if (intent != null) {
                        EzValue detail = IntentTools.safeGetEzValueFromIntent(
                                intent, "detail");
                        if (detail != null) {
                            EzMessage message = detail.getMessage();
                            StockDetailParser parser = new StockDetailParser();
                            detailEntity = parser.parse(stockCode, message);

                            if (detailEntity != null) {

                                FragmentTransaction transaction = fragmentManager.beginTransaction();
                                if (kLineFragment == null) {
                                    kLineFragment = KLineFragment.newInstance(detailEntity, "day");
                                    transaction.replace(R.id.container, kLineFragment);
                                }
                                transaction.show(kLineFragment);
                                transaction.commitAllowingStateLoss();
                            }

                        }
                    }
            }
        }
    };


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.name_layout: //弹出stockList
                stockListShowing = !stockListShowing;
                setStockListViewState();
                break;
            case R.id.index_chips_select:
                state = !state;
                indexChipsView.setSelected(state);
                setIndexChipsState();
                break;
        }
    }

    private void setStockListViewState() {
        if (stockListShowing) {
            stocksListView.setVisibility(View.VISIBLE);
        } else {
            stocksListView.setVisibility(View.GONE);
        }
    }

    private void setIndexChipsState() {
        if (state) {
            indexListView.setVisibility(View.VISIBLE);
            chipsView.setVisibility(View.GONE);
        } else {
            indexListView.setVisibility(View.GONE);
            chipsView.setVisibility(View.VISIBLE);
        }
    }

}
