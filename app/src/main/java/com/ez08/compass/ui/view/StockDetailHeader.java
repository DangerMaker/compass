package com.ez08.compass.ui.view;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.ez08.compass.R;
import com.ez08.compass.entity.StockDetailEntity;
import com.ez08.compass.parser.IndicatorHelper;
import com.ez08.compass.ui.market.customtab.EasyFragment;
import com.ez08.compass.ui.stocks.FenshiFragment;
import com.ez08.compass.ui.stocks.KLineFragment;
import com.ez08.compass.ui.stocks.view.IndexQuoteView;

import java.util.ArrayList;

public class StockDetailHeader extends LinearLayout {

    IndexQuoteView indexQuoteView;
    TabLayout tabLayout;
    UnScrollViewPager viewPager;

    EazyFragmentAdpater mAdapter;
    FenshiFragment fenshiFragment;
    KLineFragment m30fragment;

    StockDetailEntity detailEntity;
    private ArrayList<EasyFragment> mFragmentList = new ArrayList<>();


    public StockDetailHeader(Context context) {
        super(context);
    }

    public StockDetailHeader(Context context,AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        indexQuoteView = findViewById(R.id.stock_index_quote);
        tabLayout = findViewById(R.id.tab_layout_header);
        viewPager = findViewById(R.id.view_pager);
    }

    public void setData(StockDetailEntity entity){
        detailEntity = entity;

        IndicatorHelper helper = new IndicatorHelper(detailEntity);
        indexQuoteView.setData(helper);

        fenshiFragment = FenshiFragment.newInstance(detailEntity);
        m30fragment = KLineFragment.newInstance(detailEntity, "day");
        mFragmentList.clear();
        mFragmentList.add(new EasyFragment(fenshiFragment, "分时"));
        mFragmentList.add(new EasyFragment(m30fragment, "30分"));
//                                mFragmentList.add(new EasyFragment(m60Fragment, "60分"));
//                                mFragmentList.add(new EasyFragment(dayFragment, "日K"));
//                                mFragmentList.add(new EasyFragment(weekFragment, "周K"));
//                                mFragmentList.add(new EasyFragment(monthFragment, "月K"));
//                                mFragmentList.add(new EasyFragment(minFragment, "分钟"));

        viewPager.setOffscreenPageLimit(mFragmentList.size());
        mAdapter = new EazyFragmentAdpater(((AppCompatActivity)getContext()).getSupportFragmentManager(), mFragmentList);
        viewPager.setAdapter(mAdapter);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        tabLayout.setupWithViewPager(viewPager);
    }

}
