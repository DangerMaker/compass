package com.ez08.compass.ui.view;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.ez08.compass.R;
import com.ez08.compass.entity.StockDetailEntity;
import com.ez08.compass.parser.IndicatorHelper;
import com.ez08.compass.ui.market.customtab.EasyFragment;
import com.ez08.compass.ui.market.tablayout.SlidingTabLayout;
import com.ez08.compass.ui.stocks.FenshiFragment;
import com.ez08.compass.ui.stocks.KLineFragment;
import com.ez08.compass.ui.stocks.view.IndexQuoteView;

import java.util.ArrayList;

public class StockDetailHeader extends LinearLayout {

    IndexQuoteView indexQuoteView;
    SlidingTabLayout tabLayout;
    UnScrollViewPager viewPager;

    EasyFragmentAdapter1 mAdapter;
    FenshiFragment fenshiFragment;
    KLineFragment dayFragment;
    KLineFragment weekFragment;
    KLineFragment monthFragment;

    StockDetailEntity detailEntity;
    private ArrayList<EasyFragment> mFragmentList = new ArrayList<>();
    private boolean isFocus = false;

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
        viewPager = findViewById(R.id.view_pager);
    }

    public void setData(StockDetailEntity entity) {
        detailEntity = entity;
        IndicatorHelper helper = new IndicatorHelper(detailEntity);
        indexQuoteView.setData(helper);

        fenshiFragment = FenshiFragment.newInstance(detailEntity);
        dayFragment = KLineFragment.newInstance(detailEntity, "day");
        weekFragment = KLineFragment.newInstance(detailEntity, "week");
        monthFragment = KLineFragment.newInstance(detailEntity, "month");

        mFragmentList.clear();
        mFragmentList.add(new EasyFragment(fenshiFragment, "分时"));
//        mFragmentList.add(new EasyFragment(m30fragment, "日K"));
//        mFragmentList.add(new EasyFragment(m60Fragment, "60分"));
        mFragmentList.add(new EasyFragment(dayFragment, "日K"));
        mFragmentList.add(new EasyFragment(weekFragment, "周K"));
        mFragmentList.add(new EasyFragment(monthFragment, "月K"));
//        mFragmentList.add(new EasyFragment(minFragment, "分钟"));

        viewPager.setOffscreenPageLimit(mFragmentList.size());
        mAdapter = new EasyFragmentAdapter1(((AppCompatActivity) getContext()).getSupportFragmentManager(), mFragmentList);
        viewPager.setAdapter(mAdapter);
        tabLayout.setViewPager(viewPager);
    }

    public boolean getFocus() {
        if (viewPager == null)
            return false;

        switch (viewPager.getCurrentItem()) {
            case 0:
                if (fenshiFragment != null) {
                    isFocus = fenshiFragment.getFocus();
                }
                break;
            case 1:
                if (dayFragment != null) {
                    isFocus = dayFragment.getFocus();
                }
                break;
            case 2:
                if (weekFragment != null) {
                    isFocus = weekFragment.getFocus();
                }
                break;
            case 3:
                if (monthFragment != null) {
                    isFocus = monthFragment.getFocus();
                }
                break;
        }

        return isFocus;
    }
}
