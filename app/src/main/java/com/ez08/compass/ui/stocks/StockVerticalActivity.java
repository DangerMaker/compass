package com.ez08.compass.ui.stocks;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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
    RelativeLayout stockHeader;
    StockPopupWindows stockPopupWindows;

    TextView stockBigTv;
    TextView stockWaveTv;
    TextView stockOpenTv;
    TextView stockCloseTv;
    TextView stockVolumeTv;
    TextView stockChangeTv;

    LinearLayout tabLayout;

    FenshiFragment fenshiFragment;
    private FragmentManager fragmentManager;
    FragmentTransaction transaction;


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
        stockHeader = (RelativeLayout) findViewById(R.id.stock_detail_header);
        stockHeader.setOnClickListener(this);

        stockBigTv = (TextView) findViewById(R.id.stock_big);
        stockWaveTv = (TextView) findViewById(R.id.stock_main_percent);
        stockOpenTv = (TextView) findViewById(R.id.stock_open);
        stockCloseTv = (TextView) findViewById(R.id.stock_close);
        stockVolumeTv = (TextView) findViewById(R.id.stock_volume);
        stockChangeTv = (TextView) findViewById(R.id.stock_change);
        tabLayout = (LinearLayout) findViewById(R.id.tab_layout);
        for (int i = 0; i < tabLayout.getChildCount(); i++) {
            tabLayout.getChildAt(i).setOnClickListener(this);
        }

        fragmentManager = getSupportFragmentManager();
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
                                singleTextView.setText(detailEntity.getSecuname() + "(" + StockUtils.cutStockCode(detailEntity.getSecucode()) + ")");
                                IndicatorHelper helper = new IndicatorHelper(detailEntity);
                                setStockData(stockBigTv, helper.getCurrentPriceEntity());
                                setStockData(stockWaveTv, helper.getCurrentWaveEntity());
                                setStockData(stockOpenTv, helper.getOpenPriceEntity());
                                setStockData(stockCloseTv, helper.getClosePriceEntity());
                                setStockData(stockVolumeTv, helper.getVolumeEntity());
                                setStockData(stockChangeTv, helper.getChangeEntity());

                                transaction = fragmentManager.beginTransaction();
                                if (fenshiFragment == null) {
                                    fenshiFragment = FenshiFragment.newInstance(detailEntity);
                                    transaction.add(R.id.container, fenshiFragment);
                                }
                                transaction.show(fenshiFragment);
                                transaction.commitAllowingStateLoss();
                            }

                        }
                    }
            }
        }
    };

    private void setStockData(TextView textView, StockDataEntity entity) {
        String title = "";
        if (!TextUtils.isEmpty(entity.getTitle())) {
            title = entity.getTitle() + "  ";
        }

        textView.setText(title + entity.getContent());
        if (entity.getContentColor() != 0) {
            textView.setTextColor(entity.getContentColor());
        }
    }

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
            case R.id.minute:

                break;
        }
    }
}
