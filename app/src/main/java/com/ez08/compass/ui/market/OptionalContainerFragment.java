package com.ez08.compass.ui.market;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ez08.compass.R;
import com.ez08.compass.net.NetInterface;
import com.ez08.compass.tools.SelfCodesManager;
import com.ez08.compass.ui.IntervalFragment;
import com.ez08.compass.ui.base.BaseFragment;
import com.ez08.compass.ui.market.customtab.EasyFragment;
import com.ez08.compass.ui.market.tablayout.SlidingTabLayout;
import com.ez08.support.net.NetResponseHandler2;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/8/16.
 */

public class OptionalContainerFragment extends IntervalFragment {
    private final int GET_MYSTOCK = 1000;

    private SmartRefreshLayout mListViewFrame;
    private SlidingTabLayout tabLayout;
    private ViewPager viewPager;
    List<EasyFragment> tabFragments;
    OptionalFragment fragment1;
    OptionalFragment fragment2;
    ContentPagerAdapter adapter;
    Context context;

    int current = 0;

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("current", current);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        context = getActivity();
        View view = View.inflate(getActivity(), R.layout.fragment_optional_container, null);
        tabLayout = (SlidingTabLayout) view.findViewById(R.id.tab_content);
        viewPager = (ViewPager) view.findViewById(R.id.vp_content);
        mListViewFrame = (SmartRefreshLayout) view.findViewById(R.id.info_lv_frame);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        tabFragments = new ArrayList<>();
        if (savedInstanceState != null) {
            if (getChildFragmentManager().findFragmentByTag("android:switcher:" + R.id.vp_content + ":" + 0) != null)
                fragment1 = (OptionalFragment) getChildFragmentManager().findFragmentByTag("android:switcher:" + R.id.vp_content + ":" + 0);

            if (getChildFragmentManager().findFragmentByTag("android:switcher:" + R.id.vp_content + ":" + 1) != null)
                fragment2 = (OptionalFragment) getChildFragmentManager().findFragmentByTag("android:switcher:" + R.id.vp_content + ":" + 1);
        }else {
            fragment1 = OptionalFragment.getInstance(0);
            fragment2 = OptionalFragment.getInstance(1);
        }
        tabFragments.add(new EasyFragment(fragment1, "普通关注"));
        tabFragments.add(new EasyFragment(fragment2, "重点关注"));
        initTabAndVP();

        mListViewFrame.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                onLazyLoad();
            }
        });
    }

    @Override
    public void onLazyLoad() {
        NetInterface.requestGetMyStockList(mHandler, GET_MYSTOCK);
    }

    @SuppressLint("HandlerLeak")
    NetResponseHandler2 mHandler = new NetResponseHandler2() {
        @Override
        public void netConnectLost(int arg0) {
            mListViewFrame.finishRefresh();
        }

        @Override
        public void cancel(int what) {
            super.cancel(what);
            mListViewFrame.finishRefresh();
        }

        @Override
        public void timeout(int arg0) {
            mListViewFrame.finishRefresh();
        }

        @Override
        public void receive(int arg0, boolean arg1, Intent intent) {
            switch (arg0) {
                case GET_MYSTOCK:
                    mListViewFrame.finishRefresh();
                    if (intent == null) {
                        return;
                    }
                    SelfCodesManager.finalSelfCodesList.clear();

                    String[] strs = null;
                    try {
                        strs = intent.getStringArrayExtra("list");
                    } catch (ClassCastException e) {
                        strs = null;
                    }

                    SelfCodesManager.setData(strs);

                    if(viewPager.getCurrentItem() == 0) {
                        fragment1.setFragmentVisible(true);
                        fragment2.setFragmentVisible(false);
                    }

                    if(viewPager.getCurrentItem() == 1){
                        fragment1.setFragmentVisible(false);
                        fragment2.setFragmentVisible(true);
                    }
                    break;
            }
        }

    };

    private void initTabAndVP() {
        adapter = new ContentPagerAdapter(getChildFragmentManager());
        viewPager.setAdapter(adapter);
        tabLayout.setViewPager(viewPager);
        tabLayout.setOnPageChangeListener(new PageChangeListener());
        viewPager.setCurrentItem(current);
    }

    class ContentPagerAdapter extends FragmentPagerAdapter {

        public ContentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return tabFragments.get(position).getFragment();
        }

        @Override
        public int getCount() {
            return tabFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabFragments.get(position).getName();
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

            switch (arg0) {
                case 0:
                    fragment1.setFragmentVisible(true);
                    fragment2.setFragmentVisible(false);
                    break;
                case 1:
                    fragment1.setFragmentVisible(false);
                    fragment2.setFragmentVisible(true);
                    break;
            }
        }
    }

}

