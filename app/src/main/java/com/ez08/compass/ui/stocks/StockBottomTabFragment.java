package com.ez08.compass.ui.stocks;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ez08.compass.R;
import com.ez08.compass.ui.base.BaseFragment;
import com.ez08.compass.ui.market.customtab.EasyFragment;
import com.ez08.compass.ui.market.tablayout.SlidingTabLayout;
import com.ez08.compass.ui.view.EasyFragmentAdapter;

import java.util.ArrayList;

public class StockBottomTabFragment extends BaseFragment {

    public static StockBottomTabFragment newInstance() {
        Bundle args = new Bundle();
        StockBottomTabFragment fragment = new StockBottomTabFragment();
        fragment.setArguments(args);
        return fragment;
    }

    SlidingTabLayout tabLayout;
    private ViewPager mViewPager;
    private EasyFragmentAdapter mAdapter;
    private ArrayList<EasyFragment> mFragmentList = new ArrayList<>();
    private int mIndex = 0;
    EmptyFragment fragment1;
    HeadNewsFragment fragment2;
    InnerNewsFragment fragment3;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stock_botttom_tab, container, false);
        mViewPager = (ViewPager) view.findViewById(R.id.view_pager);
        tabLayout = (SlidingTabLayout) view.findViewById(R.id.tab_layout);

        fragment1 = new EmptyFragment();
        fragment2 = new HeadNewsFragment();
        fragment3 = new InnerNewsFragment();

        mFragmentList.clear();
        mFragmentList.add(new EasyFragment(fragment1, "当日资金"));
        mFragmentList.add(new EasyFragment(fragment2, "头条"));
        mFragmentList.add(new EasyFragment(fragment3, "内参"));

        mViewPager.setOffscreenPageLimit(mFragmentList.size());
        mAdapter = new EasyFragmentAdapter(getChildFragmentManager(), mFragmentList);
        mViewPager.setAdapter(mAdapter);
        tabLayout.setViewPager(mViewPager);
        return view;
    }
}
