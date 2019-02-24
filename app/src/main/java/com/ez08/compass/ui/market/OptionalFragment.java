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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ez08.compass.R;
import com.ez08.compass.entity.ItemStock;
import com.ez08.compass.event.OptionalPriceModeEvent;
import com.ez08.compass.net.NetInterface;
import com.ez08.compass.parser.StockDetailListParser;
import com.ez08.compass.tools.SelfCodesManager;
import com.ez08.compass.tools.StockSortManager;
import com.ez08.compass.ui.IntervalFragment;
import com.ez08.compass.ui.base.BaseFragment;
import com.ez08.compass.ui.market.adapter.OptionalAdapter;
import com.ez08.support.net.NetResponseHandler2;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

public class OptionalFragment extends BaseFragment implements View.OnClickListener {
    private final int AUTO_STOCK = 1001;

    public String className = getClass().getSimpleName();
    private RecyclerView mRecyclerView;
    LinearLayout editLayout;
    LinearLayout lastPriceLayout;
    LinearLayout lastIncreaseLayout;
    LinearLayout returnLayout;

    TextView increaseTitle;
    ImageView priceNav;
    ImageView increaseNav;

    int listType = 0;//0 normal 1 important
    boolean mSetPriceValue = false;
    private List<ItemStock> mStockList = new ArrayList<>();
    OptionalAdapter adapter;

    public static OptionalFragment getInstance(int type) {
        OptionalFragment fragment = new OptionalFragment();
        fragment.listType = type;
        return fragment;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("listType", listType);
        outState.putBoolean("mSetPriceValue", mSetPriceValue);
        Log.e(className, "onSaveInstanceState");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_optional_layout, null);
        Log.e(className, "onCreateView");

        mRecyclerView = (RecyclerView) view.findViewById(R.id.optional_share_lv);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        adapter = new OptionalAdapter(mContext, mStockList, mSetPriceValue);
        mRecyclerView.setAdapter(adapter);
        editLayout = (LinearLayout) view.findViewById(R.id.stock_edit);
        lastPriceLayout = (LinearLayout) view.findViewById(R.id.stock_last);
        lastIncreaseLayout = (LinearLayout) view.findViewById(R.id.stock_increase);
        returnLayout = (LinearLayout) view.findViewById(R.id.stock_return);
        returnLayout.setOnClickListener(this);
        editLayout.setOnClickListener(this);
        lastPriceLayout.setOnClickListener(this);
        lastIncreaseLayout.setOnClickListener(this);
        increaseTitle = (TextView) view.findViewById(R.id.txt_increase);
        priceNav = (ImageView) view.findViewById(R.id.stock_price_nav);
        increaseNav = (ImageView) view.findViewById(R.id.stock_increase_nav);

        DividerItemDecoration divider = new DividerItemDecoration(
                mContext,
                DividerItemDecoration.VERTICAL
        );
        divider.setDrawable(ContextCompat.getDrawable(mContext, R.drawable.line_light_1px));
        mRecyclerView.addItemDecoration(divider);
        return view;
    }

    public void setFragmentVisible(boolean visible) {
        if (visible) {
            onLazyLoad();
        }
    }


    private void notifyDataSetChanged() {
        if (adapter == null) {
            Log.e(className, "notifyDataSetChanged");
            return;
        }
        sortMode = 0;
        notifyHeaderMode();
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
    public void changePriceMode(OptionalPriceModeEvent event) {
        mSetPriceValue = event.isFlag();
        notifyHeaderMode();
        adapter.setPriceValue(mSetPriceValue);
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

    int sortMode = 0; // 0 default ,1 price asc ,2 price desc ,3 increase asc ,4 increase desc

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.stock_edit:

                return;
            case R.id.stock_return:
                sortMode = 0;
                break;
            case R.id.stock_last:
                if (sortMode == 1) {
                    sortMode = 2;
                } else {
                    sortMode = 1;
                }
                break;
            case R.id.stock_increase:
                if (sortMode == 3) {
                    sortMode = 4;
                } else {
                    sortMode = 3;
                }
                break;
        }
        notifyHeaderMode();
    }

    private void notifyHeaderMode() {
        switch (sortMode) {
            case 0:
                mStockList.clear();
                mStockList = SelfCodesManager.getSelfCodes(listType);
                adapter.clearAndAddAll(mStockList);
                priceNav.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.option_vertal_img));
                increaseNav.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.option_vertal_img));
                returnLayout.setVisibility(View.GONE);
                editLayout.setVisibility(View.VISIBLE);
                break;
            case 1:
                mStockList.clear();
                mStockList = StockSortManager.sortPriceAsc(SelfCodesManager.getSelfCodes(listType));
                adapter.clearAndAddAll(mStockList);
                priceNav.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.sort_down));
                increaseNav.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.option_vertal_img));
                returnLayout.setVisibility(View.VISIBLE);
                editLayout.setVisibility(View.GONE);
                break;
            case 2:
                mStockList.clear();
                mStockList = StockSortManager.sortPriceDesc(SelfCodesManager.getSelfCodes(listType));
                adapter.clearAndAddAll(mStockList);
                priceNav.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.sort_up));
                increaseNav.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.option_vertal_img));
                returnLayout.setVisibility(View.VISIBLE);
                editLayout.setVisibility(View.GONE);
                break;
            case 3:
                mStockList.clear();
                mStockList = StockSortManager.sortIncreaseAsc(SelfCodesManager.getSelfCodes(listType), mSetPriceValue);
                adapter.clearAndAddAll(mStockList);
                increaseNav.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.sort_up));
                increaseNav.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.sort_down));
                priceNav.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.option_vertal_img));
                returnLayout.setVisibility(View.VISIBLE);
                editLayout.setVisibility(View.GONE);
                break;
            case 4:
                mStockList.clear();
                mStockList = StockSortManager.sortIncreaseDesc(SelfCodesManager.getSelfCodes(listType), mSetPriceValue);
                adapter.clearAndAddAll(mStockList);
                increaseNav.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.sort_up));
                priceNav.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.option_vertal_img));
                returnLayout.setVisibility(View.VISIBLE);
                editLayout.setVisibility(View.GONE);
                break;
        }

        if (mSetPriceValue) {
            increaseTitle.setText("涨跌");
        } else {
            increaseTitle.setText("涨幅");
        }
    }

    public void onLazyLoad() {
        String path = SelfCodesManager.getSelfOnlyCode(listType);
        mStockList = SelfCodesManager.getSelfCodes(listType);
        if (!TextUtils.isEmpty(path))
            NetInterface.getStockBrief(mHandler, AUTO_STOCK, path);
    }
}
