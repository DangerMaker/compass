package com.ez08.compass.ui.market;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.android.thinkive.framework.util.ScreenUtil;
import com.ez08.compass.CompassApp;
import com.ez08.compass.R;
import com.ez08.compass.entity.NewAdvertEntity;
import com.ez08.compass.entity.PlateMarketEntity;
import com.ez08.compass.entity.StockMarketEntity;
import com.ez08.compass.net.NetInterface;
import com.ez08.compass.parser.PlateMarketParser;
import com.ez08.compass.parser.StockDetailListParser;
import com.ez08.compass.parser.StockMarketParser;
import com.ez08.compass.tools.AdsManager;
import com.ez08.compass.ui.IntervelFragment;
import com.ez08.compass.ui.market.view.MarketHomeHeader;
import com.ez08.compass.ui.view.PopupAdView;
import com.ez08.support.net.EzMessage;
import com.ez08.support.net.NetResponseHandler2;
import com.ez08.support.util.EzValue;
import com.ez08.tools.IntentTools;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/7/27.
 * 行情fragment
 */
public class MarketFragment extends IntervelFragment {

    private final int WHAT_REFRESH_MARKLIST = 1000; //行情数据
    private final int AUTO_TASK_STOCK = 1005;

    private ArrayList<StockMarketEntity> stocklist1;
    private ArrayList<StockMarketEntity> stocklist2;
    private ArrayList<PlateMarketEntity> boardlist0;
    private ArrayList<PlateMarketEntity> boardlist1;
    private ArrayList<PlateMarketEntity> boardlist2;

    private MarketHomeHeader mMarketHeader;
    private SmartRefreshLayout mListViewFrame;

    PopupAdView popupAds;
    private LinearLayout mOptionalGroup;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = getActivity();

        boardlist0 = new ArrayList<>();
        boardlist1 = new ArrayList<>();
        boardlist2 = new ArrayList<>();
        stocklist1 = new ArrayList<>();
        stocklist2 = new ArrayList<>();
        View view = View.inflate(getActivity(), R.layout.fragment_market, null);
        mMarketHeader = (MarketHomeHeader) view.findViewById(R.id.market_home_header);
        mOptionalGroup = (LinearLayout) view.findViewById(R.id.market_group);
        popupAds = (PopupAdView) view.findViewById(R.id.popup_ad);

        mListViewFrame = (SmartRefreshLayout) view.findViewById(R.id.market_lv_frame);
        mListViewFrame.autoRefresh();
        mListViewFrame.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                postImmediately();
            }
        });

        IntentFilter adFilter = new IntentFilter();
        adFilter.addAction(AdsManager.ADS_FINISH);
        getActivity().registerReceiver(receiver, adFilter);
        CompassApp.addStatis(CompassApp.GLOBAL.mgr.STOCK_MARKET, "0", "",
                System.currentTimeMillis());

        return view;
    }

    @Override
    public void onDestroyView() {
        if (mContext != null) {
            mContext.unregisterReceiver(receiver);
        }
        super.onDestroyView();
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            getAds();
        }
    };

    private void getAds() {
        NewAdvertEntity entity = AdsManager.getInstance(getActivity()).getAdsAtOptional();
        if (entity != null) {
            popupAds.setVisibility(View.VISIBLE);
            RelativeLayout.LayoutParams groupLayoutParams = (RelativeLayout.LayoutParams) mOptionalGroup.getLayoutParams();
            int height = (int) ScreenUtil.getScreenWidth(mContext) / 5;
            groupLayoutParams.bottomMargin = height;
            mOptionalGroup.setLayoutParams(groupLayoutParams);
            popupAds.setAdVisible(entity.getImageurl(), entity.getInfourl());
            CompassApp.addStatis(CompassApp.GLOBAL.mgr.ADVERT_STOCK, "0", "", System.currentTimeMillis());
            popupAds.setCloseListener(new PopupAdView.CloseListener() {
                @Override
                public void invoke() {
                    RelativeLayout.LayoutParams groupLayoutParams = (RelativeLayout.LayoutParams) mOptionalGroup.getLayoutParams();
                    groupLayoutParams.bottomMargin = 0;
                    mOptionalGroup.setLayoutParams(groupLayoutParams);
                    popupAds.setAdGone();

                    CompassApp.addStatis(CompassApp.GLOBAL.mgr.ADVERT_STOCK, "2", "", System.currentTimeMillis());

                }
            });
        } else {
            popupAds.setVisibility(View.GONE);
        }
    }

    @SuppressLint("HandlerLeak")
    private NetResponseHandler2 mHandler = new NetResponseHandler2() {

        @Override
        public void netConnectLost(int what) {
            super.netConnectLost(what);
        }

        @Override
        public void timeout(int what) {
            super.timeout(what);
        }

        @Override
        public void receive(int arg0, boolean b, Intent intent) {
            switch (arg0) {
                case WHAT_REFRESH_MARKLIST:
                    if (mListViewFrame == null) {
                        return;
                    }
                    mListViewFrame.finishRefresh();
                    boardlist0.clear();
                    boardlist1.clear();
                    boardlist2.clear();
                    stocklist1.clear();
                    stocklist2.clear();
                    if (intent != null) {

                        EzValue vaule0 = IntentTools.safeGetEzValueFromIntent(
                                intent, "boardlist0");
                        if (vaule0 != null) {
                            EzMessage[] msges1 = vaule0.getMessages();
                            if (msges1 != null) {
                                PlateMarketParser parser1 = new PlateMarketParser();
                                for (int i = 0; i < msges1.length; i++) {
                                    PlateMarketEntity entity1 = parser1.parse(msges1[i]);
                                    boardlist0.add(entity1);
                                }
                            }
                        }

                        EzValue vaule1 = IntentTools.safeGetEzValueFromIntent(
                                intent, "boardlist1");
                        if (vaule1 != null) {
                            EzMessage[] msges1 = vaule1.getMessages();
                            if (msges1 != null) {
                                PlateMarketParser parser1 = new PlateMarketParser();
                                for (int i = 0; i < msges1.length; i++) {
                                    PlateMarketEntity entity1 = parser1.parse(msges1[i]);
                                    boardlist1.add(entity1);
                                }
                            }
                        }

                        EzValue vaule2 = IntentTools.safeGetEzValueFromIntent(
                                intent, "boardlist2");
                        if (vaule2 != null) {
//                            String aa=vaule2.description();
                            EzMessage[] msges2 = vaule2.getMessages();
                            if (msges2 != null) {
                                PlateMarketParser parser1 = new PlateMarketParser();
                                for (int i = 0; i < msges2.length; i++) {
                                    PlateMarketEntity entity2 = parser1.parse(msges2[i]);
                                    boardlist2.add(entity2);
                                }
                            }
                        }

                        EzValue vaule3 = IntentTools.safeGetEzValueFromIntent(
                                intent, "stocklist1");
                        if (vaule3 != null) {
                            EzMessage[] msges3 = vaule3.getMessages();
                            if (msges3 != null) {
                                StockMarketParser parser3 = new StockMarketParser();
                                for (int i = 0; i < msges3.length; i++) {
                                    StockMarketEntity entity3 = parser3.parse(msges3[i]);
                                    stocklist1.add(entity3);
                                }
                            }
                        }

                        EzValue vaule4 = IntentTools.safeGetEzValueFromIntent(
                                intent, "stocklist2");
                        if (vaule4 != null) {
                            EzMessage[] msges4 = vaule4.getMessages();
                            if (msges4 != null) {
                                StockMarketParser parser4 = new StockMarketParser();
                                for (int i = 0; i < msges4.length; i++) {
                                    StockMarketEntity entity4 = parser4.parse(msges4[i]);
                                    stocklist2.add(entity4);
                                }
                            }
                        }
                    }
                    mMarketHeader.setData(stocklist1, stocklist2, boardlist0, boardlist1, boardlist2);
                    break;
                case AUTO_TASK_STOCK:
                    if (mMarketHeader != null) {
                        StockDetailListParser parserq1 = new StockDetailListParser(mMarketHeader.getStockList());
                        parserq1.parserResult(intent);
                        mMarketHeader.setHeader();
                    }
                    break;
            }
        }
    };

    @Override
    public void postMethod() {
        Log.e("postMethod","MarketFragment");
        NetInterface.getMarketData(mHandler, WHAT_REFRESH_MARKLIST);
        NetInterface.getStockBrief(mHandler, AUTO_TASK_STOCK, CompassApp.Constants.STOCK_VALUE_CODE);
    }
}
