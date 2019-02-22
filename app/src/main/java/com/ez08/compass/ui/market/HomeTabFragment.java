package com.ez08.compass.ui.market;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ez08.compass.CompassApp;
import com.ez08.compass.R;
import com.ez08.compass.ui.MainActivity;
import com.ez08.compass.ui.market.customtab.EasyFragment;
import com.ez08.compass.ui.market.customtab.SlidingTabLayout;
import com.ez08.compass.ui.stocks.SearchStockActivity;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/9/18.
 */
public class HomeTabFragment extends Fragment implements View.OnClickListener {

    private ViewPager mViewPager;
    private FragmentAdapter mAdapter;
    private ArrayList<EasyFragment> mFragmentList = new ArrayList<>();
    private int mIndex = 0;
    private MarketFragment marketFragment;
    private OptionalContainerFragment stockFragment;
    private ChartsFragment chartsFragment;
    private WatchFragment watchFragment;
    private DingpanFragment dingPanFragment;
    private Button add_stock;
    private Button theme_style;

    SlidingTabLayout sliding_tabs;

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
//        super.onSaveInstanceState(outState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = View.inflate(getActivity(), R.layout.home_tab_layout, null);
        mViewPager = (ViewPager) view.findViewById(R.id.info_tab_pager);
//        mViewPager.setOffscreenPageLimit(5);
        sliding_tabs = (SlidingTabLayout) view.findViewById(R.id.sliding_tabs);

        mFragmentList = new ArrayList<>();
        marketFragment = new MarketFragment();
        stockFragment = new OptionalContainerFragment();
        chartsFragment = new ChartsFragment();
        dingPanFragment = new DingpanFragment();

        mFragmentList.add(new EasyFragment(marketFragment, "行情"));
        mFragmentList.add(new EasyFragment(stockFragment, "自选股"));
        mFragmentList.add(new EasyFragment(dingPanFragment, "特色盯盘"));
        mFragmentList.add(new EasyFragment(chartsFragment, "消息榜"));

        if (CompassApp.GLOBAL.CUSTOMER_LEVEL >= 0) {
            watchFragment = new WatchFragment();
            mFragmentList.add(new EasyFragment(watchFragment, "三看榜"));
        }

        add_stock = (Button) view.findViewById(R.id.add_stock);
        add_stock.setOnClickListener(this);
        mAdapter = new FragmentAdapter(getChildFragmentManager(), mFragmentList);
        mViewPager.setAdapter(mAdapter);
        sliding_tabs.setViewPager(mViewPager);
        mViewPager.addOnPageChangeListener(new PageChangeListener());

        theme_style = (Button) view.findViewById(R.id.theme_style);
        theme_style.setOnClickListener(this);
        if (CompassApp.GLOBAL.THEME_STYLE == 0) {
            theme_style.setBackgroundResource(R.drawable.night_3x);
        } else {
            theme_style.setBackgroundResource(R.drawable.day_3x);
        }
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_stock:
                Intent intent = new Intent(getActivity(), SearchStockActivity.class);
                startActivity(intent);
                break;
            case R.id.theme_style:
                if (CompassApp.GLOBAL.THEME_STYLE == 0) {
                    theme_style.setBackgroundResource(R.drawable.night_3x);
                    CompassApp.GLOBAL.THEME_STYLE = 1;
                    CompassApp.addStatis(CompassApp.GLOBAL.mgr.NIGHT_MODE, "1", "", System.currentTimeMillis());
                } else {
                    theme_style.setBackgroundResource(R.drawable.day_3x);
                    CompassApp.GLOBAL.THEME_STYLE = 0;
                    CompassApp.addStatis(CompassApp.GLOBAL.mgr.NIGHT_MODE, "0", "", System.currentTimeMillis());
                }
                SharedPreferences mySharedPreferences = getActivity().getSharedPreferences("kefu", Activity.MODE_PRIVATE);
                SharedPreferences.Editor editor = mySharedPreferences
                        .edit();
                editor.putInt("theme_style", CompassApp.GLOBAL.THEME_STYLE);
                editor.commit();
                getActivity().finish();
                Intent main = new Intent(getActivity(), MainActivity.class);
                main.putExtra("theme", true);
                getActivity().startActivity(main);
                break;
        }
    }

    public void setHomeLevel() {
        if (CompassApp.GLOBAL.CUSTOMER_LEVEL >= 0) {
            if (mFragmentList.size() == 4) {
                watchFragment = new WatchFragment();
                mFragmentList.add(new EasyFragment(watchFragment, "三看榜"));
                mAdapter.notifyDataSetChanged();
                sliding_tabs.setViewPager(mViewPager);
            }
        } else {
            if (mFragmentList.size() > 4) {
                mFragmentList.remove(mFragmentList.size() - 1);
                mAdapter.notifyDataSetChanged();
                sliding_tabs.setViewPager(mViewPager);
            }

        }
    }

    public class FragmentAdapter extends FragmentPagerAdapter {

        private List<EasyFragment> mFragments;

        public FragmentAdapter(FragmentManager fm, List<EasyFragment> mFragments) {
            super(fm);
            this.mFragments = mFragments;
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position).getFragment();
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragments.get(position).getName();
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
//            super.destroyItem(container, position, object);
        }
    }

    private class PageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageSelected(int arg0) {
            initTitle(arg0);
            switch (arg0) {
                case 0:
                    CompassApp.addStatis(CompassApp.GLOBAL.mgr.STOCK_MARKET, "0", "",
                            System.currentTimeMillis());
                    break;
                case 1:
//                    CompassApp.addStatis(CompassApp.mgr.STOCK_MYSTOCK, "0", "",
//                            System.currentTimeMillis());
                    break;
                case 3:
                    CompassApp.addStatis(CompassApp.GLOBAL.mgr.STOCK_HOTWORDS, "0", "",
                            System.currentTimeMillis());
                    break;
                case 4:
                    CompassApp.addStatis(CompassApp.GLOBAL.mgr.STOCK_WATCH, "0", "",
                            System.currentTimeMillis());
                    break;
                case 2:
                    CompassApp.addStatis(CompassApp.GLOBAL.mgr.STOCK_STARE, "0", "",
                            System.currentTimeMillis());
                    break;
            }
        }
    }

    public void setSortStatus() {
//        stockFragment.setSortStatus();
    }

    private void initTitle(int curPos) {
        mIndex = curPos;
        setVisible(true);
    }

    //非得这么统计
    public void setCurrentPage() {
        switch (mIndex) {
            case 0:
                CompassApp.addStatis(CompassApp.GLOBAL.mgr.STOCK_MARKET, "0", "",
                        System.currentTimeMillis());
                break;
            case 1:
//                    CompassApp.addStatis(CompassApp.mgr.STOCK_MYSTOCK, "0", "",
//                            System.currentTimeMillis());
                break;
            case 3:
                CompassApp.addStatis(CompassApp.GLOBAL.mgr.STOCK_HOTWORDS, "0", "",
                        System.currentTimeMillis());
                break;
            case 4:
                CompassApp.addStatis(CompassApp.GLOBAL.mgr.STOCK_WATCH, "0", "",
                        System.currentTimeMillis());
                break;
            case 2:
                CompassApp.addStatis(CompassApp.GLOBAL.mgr.STOCK_STARE, "0", "",
                        System.currentTimeMillis());
                break;
        }
    }

    public boolean getSortStatus() {
//        return stockFragment.getSortStatus();
        return false;
    }

    public void setVisible(boolean isVisible) {
        if (marketFragment != null) {
            marketFragment.intervelStop();
        }
//        if (stockFragment != null) {
//            stockFragment.intervelStop();
//        }
        if (chartsFragment != null) {
            chartsFragment.intervelStop();
        }
        if (watchFragment != null) {
            watchFragment.intervelStop();
        }

        if (dingPanFragment != null) {
            dingPanFragment.intervelStop();
        }

        if (isVisible) {
            switch (mIndex) {
                case 0:
                    if (marketFragment != null) {
                        marketFragment.intervelBegin();
                    }
                    break;
//                case 1:
//                    if (stockFragment != null) {
//                        stockFragment.intervelBegin();
//                    }
//                    break;
                case 2:
                    if (dingPanFragment != null) {
                        dingPanFragment.intervelBegin();
                    }
                    break;
                case 3:
                    if (chartsFragment != null) {
                        chartsFragment.intervelBegin();
                    }
                    break;
                case 4:
                    if (watchFragment != null) {
                        watchFragment.intervelBegin();
                    }
                    break;
            }
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(getActivity());
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(getActivity());
    }
}
