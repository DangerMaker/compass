//package com.ez08.compass.ui.view;
//
//import android.content.Context;
//import android.support.design.widget.TabLayout;
//import android.support.v7.app.AppCompatActivity;
//import android.util.AttributeSet;
//import android.widget.LinearLayout;
//import android.widget.TableLayout;
//
//import com.ez08.compass.R;
//import com.ez08.compass.entity.StockDetailEntity;
//import com.ez08.compass.ui.market.customtab.EasyFragment;
//import com.ez08.compass.ui.stocks.HeadNewsFragment;
//
//import java.util.ArrayList;
//
//public class StockDetailFooter extends LinearLayout {
//
//    TabLayout tabLayout;
//    UnScrollViewPager viewPager;
//    EasyFragmentAdapter mAdapter;
//    StockDetailEntity detailEntity;
//
//    HeadNewsFragment fragment2;
//
//    private ArrayList<EasyFragment> mFragmentList = new ArrayList<>();
//
//    public StockDetailFooter(Context context) {
//        super(context);
//    }
//
//    public StockDetailFooter(Context context, AttributeSet attrs) {
//        super(context, attrs);
//    }
//
//    @Override
//    protected void onFinishInflate() {
//        super.onFinishInflate();
//        tabLayout = findViewById(R.id.tab_layout_footer);
//        viewPager = findViewById(R.id.view_pager_footer);
//    }
//
//    public void setData(StockDetailEntity entity) {
//        detailEntity = entity;
//        fragment2 =  HeadNewsFragment.newInstance();
//
//        mFragmentList.clear();
//        mFragmentList.add(new EasyFragment(fragment2, "头条"));
//        viewPager.setOffscreenPageLimit(mFragmentList.size());
//        mAdapter = new EasyFragmentAdapter(((AppCompatActivity) getContext()).getSupportFragmentManager(), mFragmentList);
//        viewPager.setAdapter(mAdapter);
//        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
//        tabLayout.setupWithViewPager(viewPager);    }
//
//}
