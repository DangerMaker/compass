package com.ez08.compass.ui.stocks;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ez08.compass.R;
import com.ez08.compass.entity.StockDataEntity;
import com.ez08.compass.entity.StockDetailEntity;
import com.ez08.compass.net.NetInterface;
import com.ez08.compass.parser.IndicatorHelper;
import com.ez08.compass.parser.StockDetailParser;
import com.ez08.compass.tools.MyAppCompat;
import com.ez08.compass.tools.StockUtils;
import com.ez08.compass.ui.base.BaseActivity;
import com.ez08.compass.ui.market.customtab.EasyFragment;
import com.ez08.compass.ui.stocks.view.IndexQuoteView;
import com.ez08.compass.ui.view.EazyFragmentAdpater;
import com.ez08.compass.ui.view.SingleLineAutoResizeTextView;
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
    IndexQuoteView stockHeader;
    StockPopupWindows stockPopupWindows;

    TabLayout tabLayout;
    ViewPager viewPager;
    EazyFragmentAdpater mAdapter;

    FenshiFragment fenshiFragment;
    KLineFragment m30fragment;
    FragmentManager fragmentManager;
    StockBottomTabFragment bottomTabFragment;
    LinearLayout tradeLayout;

    private ArrayList<EasyFragment> mFragmentList = new ArrayList<>();


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
        singleTextView = (SingleLineAutoResizeTextView) findViewById(R.id.page_name);
        backBtn = (ImageButton) findViewById(R.id.back_btn);
        backBtn.setOnClickListener(this);
        searchBtn = (ImageButton) findViewById(R.id.search_btn);
        searchBtn.setOnClickListener(this);
        stockHeader = (IndexQuoteView) findViewById(R.id.stock_detail_header);
        stockHeader.setOnClickListener(this);

        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        fragmentManager = getSupportFragmentManager();

        tradeLayout = (LinearLayout) findViewById(R.id.stock_security_tv);
        tradeLayout.setOnClickListener(this);
        MyAppCompat.setTextBackgroud(tradeLayout,mContext);
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
//                            String aa = detail.description();
//                            Log.e(TAG, aa);
                            EzMessage message = detail.getMessage();
                            StockDetailParser parser = new StockDetailParser();
                            detailEntity = parser.parse(stockCode, message);

                            if (detailEntity != null) {
                                singleTextView.setTextContent(detailEntity.getSecuname() + "(" + StockUtils.cutStockCode(detailEntity.getSecucode()) + ")");
                                IndicatorHelper helper = new IndicatorHelper(detailEntity);
                                stockHeader.setData(helper);

                                fenshiFragment = FenshiFragment.newInstance(detailEntity);
                                m30fragment = KLineFragment.newInstance(detailEntity,"day");
                                mFragmentList.clear();
                                mFragmentList.add(new EasyFragment(fenshiFragment, "分时"));
                                mFragmentList.add(new EasyFragment(m30fragment, "30分"));
//                                mFragmentList.add(new EasyFragment(m60Fragment, "60分"));
//                                mFragmentList.add(new EasyFragment(dayFragment, "日K"));
//                                mFragmentList.add(new EasyFragment(weekFragment, "周K"));
//                                mFragmentList.add(new EasyFragment(monthFragment, "月K"));
//                                mFragmentList.add(new EasyFragment(minFragment, "分钟"));

                                viewPager.setOffscreenPageLimit(mFragmentList.size());
                                mAdapter = new EazyFragmentAdpater(getSupportFragmentManager(), mFragmentList);
                                viewPager.setAdapter(mAdapter);
                                tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
                                tabLayout.setupWithViewPager(viewPager);


                                FragmentTransaction transaction = fragmentManager.beginTransaction();
                                if(bottomTabFragment == null){
                                    bottomTabFragment = new StockBottomTabFragment();
                                    transaction.replace(R.id.stock_detail_bottom,bottomTabFragment);
                                }
                                transaction.show(bottomTabFragment);
                                transaction.commitAllowingStateLoss();
//

//                                if (fenshiFragment == null) {
//                                    fenshiFragment = FenshiFragment.newInstance(detailEntity);
//                                    transaction.add(R.id.container, fenshiFragment);
//                                }
//                                transaction.show(fenshiFragment);
//                                transaction.commitAllowingStateLoss();
//
//                                transaction = fragmentManager.beginTransaction();
//                                if (kLineFragment == null) {
//                                    kLineFragment = KLineFragment.newInstance(detailEntity,"day");
//                                    transaction.add(R.id.container, kLineFragment);
//                                }
//                                transaction.show(kLineFragment);
//                                transaction.commitAllowingStateLoss();
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
            case R.id.stock_detail_header:
                if (stockPopupWindows == null) {
                    if (detailEntity == null) {
                        return;
                    }
                    stockPopupWindows = new StockPopupWindows(this);
                    stockPopupWindows.setData(detailEntity);
                }
                stockPopupWindows.showPopupWindow(stockHeader);
                break;
            case R.id.stock_security_tv:

                break;
        }
    }

}
