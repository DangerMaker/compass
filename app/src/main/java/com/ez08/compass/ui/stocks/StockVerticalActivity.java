package com.ez08.compass.ui.stocks;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.ez08.compass.R;
import com.ez08.compass.entity.StockDetailEntity;
import com.ez08.compass.net.NetInterface;
import com.ez08.compass.parser.IndicatorHelper;
import com.ez08.compass.parser.StockDetailParser;
import com.ez08.compass.tools.StockUtils;
import com.ez08.compass.ui.base.BaseActivity;
import com.ez08.support.net.EzMessage;
import com.ez08.support.net.NetResponseHandler2;
import com.ez08.support.util.EzValue;
import com.ez08.tools.IntentTools;

import java.util.List;

public class StockVerticalActivity extends BaseActivity implements View.OnClickListener {

    private final int WHAT_GET_STOCK_DETAIL = 10001;    //获取股票详细报价信息
    public static final String TAG = "StockVerticalActivity";
    List<String> stockList;
    String stockCode;
    StockDetailEntity detailEntity;

    TextView singleTextView;
    ImageButton backBtn;
    ImageButton searchBtn;
    RecyclerView recyclerView;

    StockDataAdapter dataAdapter;
    GridLayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ativity_stock_detail_vertical);

        Intent getIntent = getIntent();
        if (getIntent != null) {
            stockList = getIntent.getStringArrayListExtra("stock_list");
            stockCode = getIntent.getStringExtra("stock_code");
        }

        initView();

        if (!TextUtils.isEmpty(stockCode))
            NetInterface.getStockDetailNew(mHandler, WHAT_GET_STOCK_DETAIL, stockCode);

    }

    private void initView() {
        singleTextView = (TextView) findViewById(R.id.page_name);
        backBtn = (ImageButton) findViewById(R.id.back_btn);
        backBtn.setOnClickListener(this);
        searchBtn = (ImageButton) findViewById(R.id.search_btn);
        searchBtn.setOnClickListener(this);

        recyclerView = (RecyclerView)findViewById(R.id.recycler_view);
        layoutManager = new GridLayoutManager(mContext,2);
        recyclerView.setLayoutManager(layoutManager);
        dataAdapter = new StockDataAdapter(mContext);
        recyclerView.setAdapter(dataAdapter);
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
        public void receive(int arg0, boolean b, Intent arg2) {
            switch (arg0) {
                case WHAT_GET_STOCK_DETAIL: //获取股票详细报价信息
                    if (arg2 != null) {
                        EzValue detail = IntentTools.safeGetEzValueFromIntent(
                                arg2, "detail");
                        if (detail != null) {
                            String aa = detail.description();
                            Log.e(TAG, aa);
                            EzMessage message = detail.getMessage();
                            StockDetailParser parser = new StockDetailParser();
                            detailEntity = parser.parse(stockCode, message);

                            if (detailEntity != null) {
                                singleTextView.setText(detailEntity.getSecuname() + "(" + StockUtils.cutStockCode(detailEntity.getSecucode()) + ")");
                            }

                            IndicatorHelper helper = new IndicatorHelper(detailEntity);
                            helper.toPrint();
                            dataAdapter.addAll(helper.getData());

                        }
                    }
            }
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_btn:
                finish();
                break;
            case R.id.search_btn:

                break;
        }
    }
}
