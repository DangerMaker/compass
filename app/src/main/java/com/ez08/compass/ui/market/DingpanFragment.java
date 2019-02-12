package com.ez08.compass.ui.market;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.ez08.compass.CompassApp;
import com.ez08.compass.R;
import com.ez08.compass.entity.PlateMarketEntity;
import com.ez08.compass.net.H5Interface;
import com.ez08.compass.net.NetInterface;
import com.ez08.compass.parser.PlateMarketParser;
import com.ez08.compass.tools.AuthTool;
import com.ez08.compass.tools.ToastUtils;
import com.ez08.compass.ui.IntervelFragment;
import com.ez08.compass.ui.WebActivity;
import com.ez08.compass.ui.market.adapter.DingpanAdapter;
import com.ez08.support.net.EzMessage;
import com.ez08.support.net.NetResponseHandler2;
import com.ez08.support.util.EzValue;
import com.ez08.tools.IntentTools;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/7/27.
 * 行情fragment
 */
public class DingpanFragment extends IntervelFragment implements View.OnClickListener {

    private final int WHAT_REFRESH_MARKLIST = 1000; //行情数据
    int day = 0;

    private List<PlateMarketEntity> boardlist0;
    private List<PlateMarketEntity> boardlist1;
    private List<PlateMarketEntity> boardlist2;
    private List<PlateMarketEntity> boardlist3;
    private List<PlateMarketEntity> boardlist4;
    private List<PlateMarketEntity> boardlist5;
    private List<List<PlateMarketEntity>> bigList;
    private SmartRefreshLayout mListViewFrame;
    private RecyclerView mRecyclerView;

    private PopupWindow popupWindow;
    private TextView choose;
    private ImageView highLowIv;
    private LinearLayout popupIndex;
    private ImageView jiantou;

    boolean state = true; // true up,false down
    private DingpanAdapter adapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        boardlist0 = new ArrayList<>();
        boardlist1 = new ArrayList<>();
        boardlist2 = new ArrayList<>();
        boardlist3 = new ArrayList<>();
        boardlist4 = new ArrayList<>();
        boardlist5 = new ArrayList<>();
        bigList = new ArrayList<>();

        View view = View.inflate(getActivity(), R.layout.fragment_dingpan, null);
        choose = (TextView) view.findViewById(R.id.date_choose);
        popupIndex = (LinearLayout) view.findViewById(R.id.popup_index);
        popupIndex.setOnClickListener(this);
        highLowIv = (ImageView) view.findViewById(R.id._selector);
        highLowIv.setOnClickListener(this);
        highLowIv.setSelected(state);
        jiantou = (ImageView) view.findViewById(R.id.iv_k_minchart);
        mListViewFrame = (SmartRefreshLayout) view.findViewById(R.id.market_lv_frame);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        adapter = new DingpanAdapter(mContext);
        mRecyclerView.setAdapter(adapter);
        mListViewFrame.autoRefresh();

        mListViewFrame.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                postImmediately();
            }
        });


        if (CompassApp.GLOBAL.THEME_STYLE == 0) {
            jiantou.setColorFilter(Color.BLACK);
//            highLowIv.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.highs_and_lows_selector));
        } else {
            jiantou.setColorFilter(Color.WHITE);
//            highLowIv.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.highs_and_lows_selector_night));
        }

        return view;
    }

    private void setPopSelect(int day1) {
        if (!AuthTool.dingpanFiveData()) {
            if (day1 == 5 || day1 == 10 || day1 == 20) {
                Intent intent1 = new Intent(getContext(), WebActivity.class);
                intent1.putExtra("title", "特色盯盘");
                intent1.putExtra("url", H5Interface.TSDP.intro(getContext()));
                getContext().startActivity(intent1);
                return;
            }
        }
        if (day1 != day) {
            String text = "";
            if (day1 == 0) {
                text = "每日";
            } else if (day1 == 10) {
                text = "10日";
            } else if (day1 == 20) {
                text = "20日";
            } else if (day1 == 5) {
                text = "5日";
            }
            day = day1;
            choose.setText(text);
            mListViewFrame.autoRefresh();
        }
    }

    @Override
    public void postMethod() {
        NetInterface.getDingPanData(mHandler, WHAT_REFRESH_MARKLIST, day);
    }

    private void showMinPop(View view) {
        View popupView = View.inflate(mContext, R.layout.dingpan_select_layout, null);
        LinearLayout layout = (LinearLayout) popupView.findViewById(R.id.stock_popup_layout);
        if (CompassApp.GLOBAL.THEME_STYLE == 0) {
            layout.setBackgroundResource(R.drawable.stock_hor_select_bg);
        } else {
            layout.setBackgroundResource(R.drawable.stock_hor_bar_bg_night);
        }
        popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        final int popupWidth = view.getMeasuredWidth();
        final int popupHeight = view.getMeasuredHeight();
        popupWindow.setWidth(popupWidth);
        final int[] location = new int[2];
        view.getLocationOnScreen(location);
        popupWindow.showAsDropDown(view);
        TextView item0 = (TextView) popupView.findViewById(R.id.stock_hor_min0);
        TextView item1 = (TextView) popupView.findViewById(R.id.stock_hor_min1);
        TextView item2 = (TextView) popupView.findViewById(R.id.stock_hor_min2);
        TextView item3 = (TextView) popupView.findViewById(R.id.stock_hor_min3);
        item0.setOnClickListener(this);
        item1.setOnClickListener(this);
        item2.setOnClickListener(this);
        item3.setOnClickListener(this);
    }

    @SuppressLint("HandlerLeak")
    NetResponseHandler2 mHandler = new NetResponseHandler2() {

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
                    mListViewFrame.finishRefresh();

                    boardlist0.clear();
                    boardlist1.clear();
                    boardlist2.clear();
                    boardlist3.clear();
                    boardlist4.clear();
                    boardlist5.clear();
                    bigList.clear();

                    EzValue vaule0 = IntentTools.safeGetEzValueFromIntent(
                            intent, "dingpan1");
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
                            intent, "dingpan2");
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
                            intent, "dingpan3");
                    if (vaule2 != null) {
                        EzMessage[] msges1 = vaule2.getMessages();
                        if (msges1 != null) {
                            PlateMarketParser parser1 = new PlateMarketParser();
                            for (int i = 0; i < msges1.length; i++) {
                                PlateMarketEntity entity1 = parser1.parse(msges1[i]);
                                boardlist2.add(entity1);
                            }
                        }
                    }

                    EzValue vaule3 = IntentTools.safeGetEzValueFromIntent(
                            intent, "dingpan4");
                    if (vaule3 != null) {
                        EzMessage[] msges1 = vaule3.getMessages();
                        if (msges1 != null) {
                            PlateMarketParser parser1 = new PlateMarketParser();
                            for (int i = 0; i < msges1.length; i++) {
                                PlateMarketEntity entity1 = parser1.parse(msges1[i]);
                                boardlist3.add(entity1);
                            }
                        }
                    }

                    EzValue vaule4 = IntentTools.safeGetEzValueFromIntent(
                            intent, "dingpan5");
                    if (vaule4 != null) {
                        EzMessage[] msges1 = vaule4.getMessages();
                        if (msges1 != null) {
                            PlateMarketParser parser1 = new PlateMarketParser();
                            for (int i = 0; i < msges1.length; i++) {
                                PlateMarketEntity entity1 = parser1.parse(msges1[i]);
                                boardlist4.add(entity1);
                            }
                        }
                    }

                    EzValue vaule5 = IntentTools.safeGetEzValueFromIntent(
                            intent, "dingpan6");
                    if (vaule5 != null) {
                        EzMessage[] msges1 = vaule5.getMessages();
                        if (msges1 != null) {
                            PlateMarketParser parser1 = new PlateMarketParser();
                            for (int i = 0; i < msges1.length; i++) {
                                PlateMarketEntity entity1 = parser1.parse(msges1[i]);
                                boardlist5.add(entity1);
                            }
                        }
                    }

                    String[] valueHighs = intent.getStringArrayExtra("highs");
                    String[] valueLows = intent.getStringArrayExtra("lows");
                    bigList.add(boardlist0);
                    bigList.add(boardlist1);
                    bigList.add(boardlist2);
                    bigList.add(boardlist3);
                    bigList.add(boardlist4);
                    bigList.add(boardlist5);

                    if (AuthTool.dingpanBottomAd() && !bigList.isEmpty()) {
                        List<PlateMarketEntity> footer = new ArrayList<>();
                        bigList.add(footer);
                    }

                    adapter.addHighAndLow(valueHighs, valueLows);
                    adapter.clearAndAddAll(bigList);

                    break;
            }
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.popup_index:
                if (!bigList.isEmpty()) {
                    showMinPop(popupIndex);
                }
                break;
            case R.id._selector:
                if (bigList.isEmpty()) {
                    ToastUtils.show(mContext, "暂无数据");
                    return;
                }

                if (state) {
                    CompassApp.addStatis(CompassApp.GLOBAL.mgr.STOCK_FEATURE, "2", "",
                            System.currentTimeMillis());
                } else {
                    CompassApp.addStatis(CompassApp.GLOBAL.mgr.STOCK_FEATURE, "1", "",
                            System.currentTimeMillis());
                }

                state = !state;
                highLowIv.setSelected(state);
                adapter.changeState(state);
                adapter.notifyDataSetChanged();
                break;
            case R.id.stock_hor_min0:
                if (popupWindow != null && popupWindow.isShowing()) {
                    popupWindow.dismiss();
                }
                setPopSelect(0);
                CompassApp.addStatis(CompassApp.GLOBAL.mgr.STOCK_FEATURE, "3", "",
                        System.currentTimeMillis());
                break;
            case R.id.stock_hor_min1:
                if (popupWindow != null && popupWindow.isShowing()) {
                    popupWindow.dismiss();
                }
                setPopSelect(5);
                CompassApp.addStatis(CompassApp.GLOBAL.mgr.STOCK_FEATURE, "4", "",
                        System.currentTimeMillis());

                break;
            case R.id.stock_hor_min2:
                if (popupWindow != null && popupWindow.isShowing()) {
                    popupWindow.dismiss();
                }
                setPopSelect(10);
                CompassApp.addStatis(CompassApp.GLOBAL.mgr.STOCK_FEATURE, "5", "",
                        System.currentTimeMillis());
                break;
            case R.id.stock_hor_min3:
                if (popupWindow != null && popupWindow.isShowing()) {
                    popupWindow.dismiss();
                }
                setPopSelect(20);
                CompassApp.addStatis(CompassApp.GLOBAL.mgr.STOCK_FEATURE, "6", "",
                        System.currentTimeMillis());

                break;
        }
    }
}
