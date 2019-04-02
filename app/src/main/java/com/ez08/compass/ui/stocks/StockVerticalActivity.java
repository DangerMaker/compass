package com.ez08.compass.ui.stocks;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;

import com.ez08.compass.R;
import com.ez08.compass.entity.StockDetailEntity;
import com.ez08.compass.net.NetInterface;
import com.ez08.compass.parser.StockDetailParser;
import com.ez08.compass.tools.StockUtils;
import com.ez08.compass.ui.base.BaseActivity;
import com.ez08.compass.ui.market.customtab.EasyFragment;
import com.ez08.compass.ui.market.tablayout.SlidingTabLayout;
import com.ez08.compass.ui.stocks.view.IndexQuoteView;
import com.ez08.compass.ui.stocks.view.MaxHeightViewPager;
import com.ez08.compass.ui.view.EazyFragmentAdpater;
import com.ez08.compass.ui.view.SingleLineAutoResizeTextView;
import com.ez08.compass.ui.view.StockDetailHeader;
import com.ez08.support.net.EzMessage;
import com.ez08.support.net.NetResponseHandler2;
import com.ez08.support.util.EzValue;
import com.ez08.tools.IntentTools;

import java.util.ArrayList;
import java.util.List;

public class StockVerticalActivity extends BaseActivity implements View.OnClickListener {

    private final int WHAT_GET_STOCK_DETAIL = 10001;    //获取股票详细报价信息
    public static final String TAG = "StockVerticalActivity";
    List<String> stockList;
    String stockCode;
    StockDetailEntity detailEntity;

    SingleLineAutoResizeTextView singleTextView;
    ImageButton backBtn;
    ImageButton searchBtn;
    StockPopupWindows stockPopupWindows;

    StockDetailHeader headerView;
    IndexQuoteView indexQuoteView;
    SlidingTabLayout bottomTabLayout;
    MaxHeightViewPager scrollViewPager;

    Fragment capitalFragment;
    Fragment headNewsFragment;
    Fragment innerNewsFragment;
    private EazyFragmentAdpater mAdapter;
    private ArrayList<EasyFragment> mFragmentList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ativity_stock_detail_vertical1);

        Intent getIntent = getIntent();
        if (getIntent != null) {
            stockList = getIntent.getStringArrayListExtra("stock_list");
            stockCode = getIntent.getStringExtra("stock_code");
        }

        initView();
    }


    private void initView() {
        singleTextView = (SingleLineAutoResizeTextView) findViewById(R.id.page_name);
        backBtn = (ImageButton) findViewById(R.id.back_btn);
        backBtn.setOnClickListener(this);
        searchBtn = (ImageButton) findViewById(R.id.search_btn);
        searchBtn.setOnClickListener(this);
        headerView = (StockDetailHeader)findViewById(R.id.stock_detail_header);
        indexQuoteView = headerView.findViewById(R.id.stock_index_quote);
        indexQuoteView.setOnClickListener(this);
        bottomTabLayout = (SlidingTabLayout)findViewById(R.id.tab_layout_bottom);
        scrollViewPager = (MaxHeightViewPager) findViewById(R.id.view_pager_bottom);

        capitalFragment = CapitalFragment.newInstance(stockCode);
        headNewsFragment = new HeadNewsFragment();
        innerNewsFragment = new InnerNewsFragment();

        mFragmentList.clear();
        mFragmentList.add(new EasyFragment(capitalFragment, "当日资金"));
        mFragmentList.add(new EasyFragment(headNewsFragment, "头条"));
        mFragmentList.add(new EasyFragment(innerNewsFragment, "内参"));
        scrollViewPager.setOffscreenPageLimit(mFragmentList.size());
        mAdapter = new EazyFragmentAdpater(getSupportFragmentManager(), mFragmentList);
        scrollViewPager.setAdapter(mAdapter);
        bottomTabLayout.setViewPager(scrollViewPager);

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
                                singleTextView.setTextContent(detailEntity.getSecuname() + "(" + StockUtils.cutStockCode(detailEntity.getSecucode()) + ")");
                                headerView.setData(detailEntity);
                            }

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
            case R.id.stock_index_quote:
                if (stockPopupWindows == null) {
                    if (detailEntity == null) {
                        return;
                    }
                    stockPopupWindows = new StockPopupWindows(this);
                    stockPopupWindows.setData(detailEntity);
                }
                stockPopupWindows.showPopupWindow(indexQuoteView);
                break;
            case R.id.stock_security_tv:

                break;
        }
    }

}
