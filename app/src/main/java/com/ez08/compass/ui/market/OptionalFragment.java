package com.ez08.compass.ui.market;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ez08.compass.R;
import com.ez08.compass.entity.ItemStock;
import com.ez08.compass.event.OptionalPriceModeEvent;
import com.ez08.compass.net.NetInterface;
import com.ez08.compass.parser.StockDetailListParser;
import com.ez08.compass.tools.SelfCodesManager;
import com.ez08.compass.ui.IntervelFragment;
import com.ez08.compass.ui.market.adapter.OptionalAdapter;
import com.ez08.support.net.NetResponseHandler2;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

public class OptionalFragment extends IntervelFragment {
    private final int AUTO_STOCK = 1001;

    private RecyclerView mRecyclerView;
    int listType = 0;//0 normal 1 important
    private List<ItemStock> mStockList = new ArrayList<>();
    OptionalAdapter adapter;

    public static OptionalFragment getInstance(int type) {
        OptionalFragment fragment = new OptionalFragment();
        fragment.listType = type;
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_optional_layout, null);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.optional_share_lv);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        adapter = new OptionalAdapter(mContext, mStockList);
        mRecyclerView.setAdapter(adapter);

        DividerItemDecoration divider = new DividerItemDecoration(
                mContext,
                DividerItemDecoration.VERTICAL
        );
        divider.setDrawable(ContextCompat.getDrawable(mContext, R.drawable.line_light_1px));
        mRecyclerView.addItemDecoration(divider);
        return view;
    }

    public void notifyDataSetChanged() {
        mStockList = SelfCodesManager.getSelfCodes(listType);
        adapter.clearAndAddAll(mStockList);
    }

    @Override
    public void postMethod() {
        String path = SelfCodesManager.getSelfOnlyCode(listType);
        if (!TextUtils.isEmpty(path))
            NetInterface.getStockBrief(mHandler, AUTO_STOCK, path);

    }

    @SuppressLint("HandlerLeak")
    NetResponseHandler2 mHandler = new NetResponseHandler2() {
        @Override
        public void netConnectLost(int arg0) {
        }

        @Override
        public void cancel(int what) {
            super.cancel(what);
        }

        @Override
        public void timeout(int arg0) {
        }

        @Override
        public void receive(int arg0, boolean arg1, Intent intent) {
            switch (arg0) {
                case AUTO_STOCK:
                    if (intent == null) {
                        return;
                    }

                    StockDetailListParser parser = new StockDetailListParser(mStockList);
                    parser.parserResult(intent);
                    notifyDataSetChanged();
                    break;
            }
        }

    };

    //点击自选股后边的块，切换涨跌和涨幅
    @Subscribe(threadMode = ThreadMode.POSTING)
    public void changePriceMode(OptionalPriceModeEvent event){
        adapter.setPriceValue(event.isFlag());
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
