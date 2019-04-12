package com.ez08.compass.ui.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.ez08.compass.R;
import com.ez08.compass.entity.StockDetailEntity;
import com.ez08.compass.parser.IndicatorHelper;
import com.ez08.compass.ui.KInterface;
import com.ez08.compass.ui.market.customtab.EasyFragment;
import com.ez08.compass.ui.market.tablayout.SlidingTabLayout;
import com.ez08.compass.ui.stocks.EmptyFragment;
import com.ez08.compass.ui.stocks.FenshiFragment;
import com.ez08.compass.ui.stocks.KLineFragment;
import com.ez08.compass.ui.stocks.view.IndexQuoteView;
import com.ez08.compass.ui.stocks.view.StockTabLayout;

import java.util.ArrayList;

public class StockDetailHeader extends LinearLayout {

    IndexQuoteView indexQuoteView;
    StockTabLayout tabLayout;
    FrameLayout container;

    FenshiFragment fenshiFragment;
    KLineFragment dayFragment;
    KLineFragment weekFragment;
    KLineFragment monthFragment;
    Fragment currentFragment;

    StockDetailEntity detailEntity;
    private boolean isFocus = false;
    private int position = 0;
    private FragmentManager fragmentManager;

    public StockDetailHeader(Context context) {
        super(context);
    }

    public StockDetailHeader(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        indexQuoteView = findViewById(R.id.stock_index_quote);
        tabLayout = findViewById(R.id.tab_layout_header);
        container = findViewById(R.id.k_container);
        fragmentManager = ((AppCompatActivity) getContext()).getSupportFragmentManager();
        currentFragment = new EmptyFragment();
        tabLayout.setHandler(handler);
    }

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            select(msg.what);
        }
    };

    public void select(int position) {
        switch (position) {
            case 0:
                if (fenshiFragment == null) {
                    fenshiFragment = FenshiFragment.newInstance();
                }
                fenshiFragment.setRefreshStock(detailEntity);
                showFragment(fenshiFragment);
                break;
            case 1:
                if (dayFragment == null) {
                    dayFragment = KLineFragment.newInstance("day");
                }
                dayFragment.setRefreshStock(detailEntity);
                showFragment(dayFragment);
                break;
            case 2:
                if (weekFragment == null) {
                    weekFragment = KLineFragment.newInstance("week");
                }
                weekFragment.setRefreshStock(detailEntity);
                showFragment(weekFragment);
                break;
            case 3:
                if (monthFragment == null) {
                    monthFragment = KLineFragment.newInstance("month");
                }
                monthFragment.setRefreshStock(detailEntity);
                showFragment(monthFragment);
                break;
        }
        this.position = position;
    }

    public void setData(StockDetailEntity entity) {
        detailEntity = entity;
        IndicatorHelper helper = new IndicatorHelper(detailEntity);
        indexQuoteView.setData(helper);
        tabLayout.reset(position);
    }

    private void showFragment(Fragment fragment) {
        if (currentFragment != fragment) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.hide(currentFragment);
            currentFragment = fragment;
            if (!fragment.isAdded()) {
                transaction.add(R.id.k_container, fragment).show(fragment).commit();
            } else {
                transaction.show(fragment).commit();
            }
        }
    }

    public boolean getFocus() {
        if (currentFragment == null) {
            return false;
        }
        isFocus = ((KInterface) currentFragment).getFocus();
        return isFocus;
    }
}
