package com.ez08.compass.ui.market;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ez08.compass.R;
import com.ez08.compass.entity.Mix3Entity;
import com.ez08.compass.entity.StockMarketEntity;
import com.ez08.compass.entity.HolderTitleAndAll;
import com.ez08.compass.entity.HolderWatchMixTitle;
import com.ez08.compass.entity.HolderOnlyTitle;
import com.ez08.compass.net.NetInterface;
import com.ez08.compass.parser.StockMarketParser;
import com.ez08.compass.ui.Interval;
import com.ez08.compass.ui.IntervalFragment;
import com.ez08.compass.ui.base.BaseFragment;
import com.ez08.compass.ui.market.adapter.WatchAdapter;
import com.ez08.support.net.EzMessage;
import com.ez08.support.net.NetResponseHandler2;
import com.ez08.support.util.EzValue;
import com.ez08.tools.IntentTools;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class WatchFragment extends BaseFragment implements Interval {

    private final int WHAT_REFRESH_WATCH = 1000; //行情数据
    private final int WHAT_GET_ALL_DETAIL = 1001; //个股详细

    private SmartRefreshLayout mListViewFrame;
    private RecyclerView mRecyclerView;
    private WatchAdapter adapter;

    private String top1;
    private String top2;
    private String top3;
    String target;

    String topmix;
    List<Object> mix3List;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = View.inflate(getActivity(), R.layout.fragment_watch, null);
        mListViewFrame = (SmartRefreshLayout) view.findViewById(R.id.watch_lv_frame);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        adapter = new WatchAdapter(mContext);
        mRecyclerView.setAdapter(adapter);
//        mListViewFrame.autoRefresh();

        DividerItemDecoration divider = new DividerItemDecoration(
                mContext,
                DividerItemDecoration.VERTICAL
        );
        divider.setDrawable(ContextCompat.getDrawable(mContext, R.drawable.line_light_1px));
        mRecyclerView.addItemDecoration(divider);

        mListViewFrame.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                NetInterface.getStock3(mHandler, WHAT_REFRESH_WATCH);
            }
        });

        return view;
    }


    @SuppressLint("HandlerLeak")
    NetResponseHandler2 mHandler = new NetResponseHandler2() {

        @Override
        public void netConnectLost(int what) {
            super.netConnectLost(what);
            mListViewFrame.finishRefresh();
        }

        @Override
        public void timeout(int what) {
            super.timeout(what);
            mListViewFrame.finishRefresh();
        }

        @Override
        public void receive(int arg0, boolean b, Intent intent) {
            switch (arg0) {
                case WHAT_REFRESH_WATCH:
                    mListViewFrame.finishRefresh();
                    if (intent != null) {
                        if (mix3List == null) {
                            mix3List = new ArrayList<>();
                        } else {
                            mix3List.clear();
                        }

                        top1 = intent.getStringExtra("top1"); //people
                        top2 = intent.getStringExtra("top2"); //media
                        top3 = intent.getStringExtra("top3"); //capital
                        topmix = intent.getStringExtra("topmix");

                        mix3List.add(new HolderTitleAndAll("资金关注", 0, 14));
                        String[] capitalCodes = top3.split(",");
                        for (int i = 0; i < capitalCodes.length; i++) {
                            StockMarketEntity entity = new StockMarketEntity();
                            entity.setSecucode(capitalCodes[i]);
                            mix3List.add(entity);
                        }

                        mix3List.add(new HolderTitleAndAll("股民关注", 0, 12));
                        String[] peopelCodes = top1.split(",");
                        for (int i = 0; i < peopelCodes.length; i++) {
                            StockMarketEntity entity = new StockMarketEntity();
                            entity.setSecucode(peopelCodes[i]);
                            mix3List.add(entity);
                        }

                        mix3List.add(new HolderTitleAndAll("媒体关注", 0, 13));
                        String[] mediaCodes = top2.split(",");
                        for (int i = 0; i < mediaCodes.length; i++) {
                            StockMarketEntity entity = new StockMarketEntity();
                            entity.setSecucode(mediaCodes[i]);
                            mix3List.add(entity);
                        }

                        target = top1 + top2 + top3;
                        NetInterface.getStockBrief(mHandler, WHAT_GET_ALL_DETAIL, target);

                        try {
                            mix3List.add(new HolderOnlyTitle("交叉匹配", false));
                            mix3List.add(new HolderWatchMixTitle());
                            JSONArray jsonArray = new JSONArray(topmix);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONArray jsonArray1 = (JSONArray) jsonArray.get(i);
                                Mix3Entity entity = new Mix3Entity();
                                entity.setStockCode(jsonArray1.getString(0));
                                entity.setStockName(jsonArray1.getString(1));
                                entity.setStockPeopleCount(jsonArray1.getInt(2));
                                entity.setStockNewsCount(jsonArray1.getInt(3));
                                entity.setStockCapitalCount(jsonArray1.getInt(4));
                                mix3List.add(entity);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

//                        adapter.clearAndAddAll(mix3List);
                    }
                    break;

                case WHAT_GET_ALL_DETAIL:
                    //匹配详细内容
                    EzValue vaule = IntentTools.safeGetEzValueFromIntent(
                            intent, "list");
                    if (vaule != null) {
                        EzMessage[] msg = vaule.getMessages();
                        if (msg != null) {
                            StockMarketParser parser = new StockMarketParser();
                            for (int i = 0; i < msg.length; i++) {
                                StockMarketEntity entity = parser.parse(msg[i]);
                                for (int j = 0; j < mix3List.size(); j++) {
                                    Object temp = mix3List.get(j);
                                    if (temp instanceof StockMarketEntity &&
                                            ((StockMarketEntity) temp).getSecucode().equals(entity.getSecucode())) {
                                        StockMarketEntity marketEntity = ((StockMarketEntity) temp);
                                        marketEntity.setCurrent(entity.getCurrent());
                                        marketEntity.setExp(entity.getExp());
                                        marketEntity.setLastclose(entity.getLastclose());
                                        marketEntity.setSecuname(entity.getSecuname());
                                        marketEntity.setState(entity.getState());
                                        marketEntity.setType(entity.getType());
                                    }
                                }
                            }
                            adapter.clearAndAddAll(mix3List);
                        }
                    }


                    break;
            }
        }
    };

    @Override
    public void OnPost() {
        NetInterface.getStock3(mHandler, WHAT_REFRESH_WATCH);
    }
}