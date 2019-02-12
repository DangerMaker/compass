package com.ez08.compass.ui.news;

import android.app.Activity;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ez08.compass.CompassApp;
import com.ez08.compass.R;
import com.ez08.compass.database.IMDBHelper;
import com.ez08.compass.entity.InfoTabEntity;
import com.ez08.compass.tools.AuthTool;
import com.ez08.compass.ui.base.BaseFragment;
import com.ez08.compass.ui.news.tab.InfoTabBarView;
import com.ez08.compass.ui.news.tab.ParentViewPager;
import com.ez08.compass.auth.AuthUserInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/8/22.
 */

public class InfoNewTabFragment extends BaseFragment implements View.OnClickListener {

    private ArrayList<InfoTabEntity> mCategoryList;
    private LinearLayout mTabMain;
    private ImageView mTabImg;
    private InfoTabBarView mBarView;
    private TextView mTabDesTv;
    private HorizontalScrollView mHorScroll;
    private RelativeLayout mInfoBarTitle;
    private boolean mBarStatus = false;   //收起
    private callBack mCallBack;
    private SharedPreferences mySharedPreferences;
    private String mCurrentTitle = "操盘计划";
    private List<TextView> mTabTvList;

    private ParentViewPager mViewPager;
    private FragmentAdapter mAdapter;
    private ArrayList<Fragment> mFragmentList = new ArrayList<>();
    private int mIndex = 0;
    private int scrollViewWidth = 0;
    //初始化偏移量
    private int offset = 0;
    public IMDBHelper mHelper;
    private List<String> mHistoryList;
    private int mTabMeasureWidth;    //每一个tab的宽度
    private TextView mFinishTv;
    int whiteColor;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = View.inflate(getActivity(), R.layout.info_new_tab_layout, null);
        whiteColor = Color.WHITE;
        mInfoBarTitle = (RelativeLayout) view.findViewById(R.id.info_bartitle);
        mTabMain = (LinearLayout) view.findViewById(R.id.info_tab_main);
        mTabImg = (ImageView) view.findViewById(R.id.info_tab_img);
        mTabImg.setOnClickListener(this);
        mBarView = (InfoTabBarView) view.findViewById(R.id.info_tab_bar_view);
        mTabDesTv = (TextView) view.findViewById(R.id.info_tab_des);
        mFinishTv = (TextView) view.findViewById(R.id.info_tab_finish_tv);
        mFinishTv.setOnClickListener(this);
        mFinishTv.setVisibility(View.GONE);
        mHorScroll = (HorizontalScrollView) view.findViewById(R.id.info_tab_scroll);
        mHorScroll.setSmoothScrollingEnabled(true);
        mTabDesTv.setVisibility(View.GONE);
        mHorScroll.setVisibility(View.VISIBLE);
        mBarStatus = false;
        mBarView.setVisibility(View.GONE);

        mViewPager = (ParentViewPager) view.findViewById(R.id.info_tab_pager);
//        mViewPager.setOffscreenPageLimit(16);
        mViewPager.setOffscreenPageLimit(3);
        mFragmentList = new ArrayList<>();

        mTabTvList = new ArrayList<>();
        mCategoryList = new ArrayList<>();
        mySharedPreferences = getActivity().getSharedPreferences(
                "infotab5", Activity.MODE_PRIVATE);
        String change = mySharedPreferences.getString(AuthUserInfo.getMyCid(), "");

        if (!TextUtils.isEmpty(change)) {
            parseTab(change);
        } else {
            initTab();
        }
        initTabView();
        mBarView.setDataList(mCategoryList);
        mBarView.setEditeCallBack(new InfoTabBarView.CallBack() {
            @Override
            public void getCurrengPosition(String category) {
                mCurrentTitle = category;
                mTabImg.performClick();
                int arg0 = 0;
                for (int i = 0; i < mCategoryList.size(); i++) {
                    if (mCategoryList.get(i).getName().equals(category)) {
                        arg0 = i;
                        break;
                    }
                }
                mTabMeasureWidth = mTabMain.getMeasuredWidth() / mCategoryList.size();
                int right = mTabMeasureWidth * (arg0 + 1);
                int left = mTabMeasureWidth * (arg0);
                mHorScroll.scrollTo(0, 0);
                offset = 0;

                if ((scrollViewWidth + offset) < right) {//需要向右移动
                    mHorScroll.smoothScrollBy(right - (scrollViewWidth + offset), 0);
                    offset += right - (scrollViewWidth + offset);
                }

                if (offset >= left) {//需要向左移动
                    mHorScroll.smoothScrollBy(left - offset, 0);
                    offset += left - offset;
                }

            }

            @Override
            public void isEdite(boolean edite) {
                if (edite) {
                    mTabDesTv.setText("拖动排序");
                    mFinishTv.setVisibility(View.VISIBLE);
                    mTabImg.setVisibility(View.GONE);
                } else {
                    mTabDesTv.setText("切换栏目");
                    mFinishTv.setVisibility(View.GONE);
                    mTabImg.setVisibility(View.VISIBLE);
                }
            }
        });

        mHelper = IMDBHelper.getInstance(getActivity());
        mHistoryList = new ArrayList<>();
        Cursor cursor = mHelper.getInfoIdList(AuthUserInfo.getMyCid());
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String infoid = cursor.getString(cursor.getColumnIndex("infoid"));
                mHistoryList.add(infoid);
            }
        }

        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        for (int i = 0; i < mCategoryList.size(); i++) {
            InfoNewFragment fragment = new InfoNewFragment();
            fragment.setDBHelper(mHelper, mHistoryList);
            Bundle bundle = new Bundle();
            bundle.putString("category", mCategoryList.get(i).getName());
            fragment.setArguments(bundle);
            mFragmentList.add(fragment);
            if (mCategoryList.get(i).getCategory().equals("0")) {
                transaction.show(fragment);
            } else {
                transaction.hide(fragment);
            }
        }
        mAdapter = new FragmentAdapter(getChildFragmentManager(), mFragmentList);
        mViewPager.setAdapter(mAdapter);
        mViewPager.setOnPageChangeListener(new PageChangeListener());

        if (AuthTool.caopanPlan()) {
            CompassApp.addStatis(CompassApp.GLOBAL.mgr.INFO_LIST, "14", "",
                    System.currentTimeMillis());
        } else {
            CompassApp.addStatis(CompassApp.GLOBAL.mgr.INFO_LIST, "0", "",
                    System.currentTimeMillis());
        }
        return view;
    }

    private void initTabView() {
        if (mAdapter != null) {
            mAdapter.setFragmentList(mFragmentList);
        }


        mTabMain.removeAllViews();
        mTabTvList.clear();
        boolean hasTab = false;    //是否在展示tab上
        for (int i = 0; i < mCategoryList.size(); i++) {
            final InfoTabEntity entity = mCategoryList.get(i);
            if (entity.getCategory().equals("0")) {
                final View item = View.inflate(getActivity(), R.layout.info_new_tab_item, null);
                mTabMain.addView(item);
                TextView lName = (TextView) item.findViewById(R.id.info_tab_item_tv);
                lName.setText(entity.getName());
                if (mCurrentTitle.equals(entity.getName())) {
                    lName.setTextColor(whiteColor);
                    lName.setTextSize(20);
                    hasTab = true;
                    mIndex = i;
//                    mViewPager.setCurrentItem(i);
                }
                mTabTvList.add(lName);
                item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mCurrentTitle = entity.getName();
                        for (int i = 0; i < mTabTvList.size(); i++) {
                            TextView tvi = mTabTvList.get(i);
                            if (mTabTvList.get(i).getText().toString().equals(mCurrentTitle)) {
                                mIndex = i;
                                mViewPager.setCurrentItem(i);
                                tvi.setTextColor(whiteColor);
                                tvi.setTextSize(20);
                            } else {
                                tvi.setTextColor(whiteColor);
                                tvi.setTextSize(15);
                            }
                        }
//                        mTitleNameTv.setText(mCurrentTitle);
                        mAdapter.notifyDataSetChanged();
                    }
                });
                mHorScroll.post(new Runnable() {
                    @Override
                    public void run() {
                        mTabMeasureWidth = item.getMeasuredHeight();
                    }
                });

            }
        }

        if (!hasTab) {
            TextView lName = (TextView) mTabMain.getChildAt(0).findViewById(R.id.info_tab_item_tv);
            lName.setTextColor(whiteColor);
            lName.setTextSize(20);
            mCurrentTitle = lName.getText().toString();
            mIndex = 0;
            mViewPager.setCurrentItem(0);
        } else {
            mViewPager.setCurrentItem(mIndex);
        }

//        mTitleNameTv.setText(mCurrentTitle);


    }

    /*
    添加模块
     */
    private void initTab() {
        if (AuthTool.caopanPlan()) {
            mCategoryList.add(new InfoTabEntity("操盘计划", "0"));
        }
        mCategoryList.add(new InfoTabEntity("头条", "0"));
        mCategoryList.add(new InfoTabEntity("内参", "0"));
        mCategoryList.add(new InfoTabEntity("滚动", "0"));
        mCategoryList.add(new InfoTabEntity("晨会", "0"));
        mCategoryList.add(new InfoTabEntity("宏观", "0"));
        mCategoryList.add(new InfoTabEntity("行业", "0"));
        mCategoryList.add(new InfoTabEntity("公司", "0"));
        mCategoryList.add(new InfoTabEntity("全球", "0"));
        mCategoryList.add(new InfoTabEntity("热文", "0"));
        mCategoryList.add(new InfoTabEntity("新股", "0"));
        mCategoryList.add(new InfoTabEntity("港股", "0"));
//        mCategoryList.add(new InfoTabEntity("日历", "0"));
//        mCategoryList.add(new InfoTabEntity("日历", "0"));
        mCategoryList.add(new InfoTabEntity("新三板", "0"));
        mCategoryList.add(new InfoTabEntity("期货", "0"));
        mCategoryList.add(new InfoTabEntity("期权", "0"));
//        mCategoryList.add(new InfoTabEntity("外汇", "0"));

    }

    private void parseTab(String change) {
        try {
            JSONArray array = new JSONArray(change);
            for (int i = 0; i < array.length(); i++) {
                JSONObject obj = (JSONObject) array.get(i);
                String name = obj.getString("name");
                String state = obj.getString("state");
                mCategoryList.add(new InfoTabEntity(name, state));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.info_tab_finish_tv:
                mTabDesTv.setText("切换栏目");
                mFinishTv.setVisibility(View.GONE);
                mTabImg.setVisibility(View.VISIBLE);
                //----
                mBarView.setEdite(false);
                //----
                break;
            case R.id.info_tab_img:
                CompassApp.addStatis(CompassApp.GLOBAL.mgr.INFO_SELECT, "0", "",
                        System.currentTimeMillis());
                mFinishTv.setVisibility(View.GONE);
                mTabImg.setVisibility(View.VISIBLE);
                if (mBarStatus) { //收起
                    mTabImg.setBackgroundResource(R.drawable.info_tab_select_normal);
                    mBarView.setVisibility(View.GONE);
                    mBarView.setEdite(false);
                    mViewPager.setVisibility(View.VISIBLE);
                    mTabDesTv.setVisibility(View.GONE);
                    mHorScroll.setVisibility(View.VISIBLE);
//                    mInfoBarTitle.setBackgroundColor();
                    //-------------收起的时候要更新本地排序缓存
                    SharedPreferences.Editor editor = mySharedPreferences
                            .edit();
                    editor.putString(AuthUserInfo.getMyCid(), mBarView.getData());
                    editor.commit();
                    initTabView();
                    for (int i = 0; i < mFragmentList.size(); i++) {
                        InfoNewFragment fragment = (InfoNewFragment) mFragmentList.get(i);
                        if (((InfoNewFragment) mFragmentList.get(i)).getCategory().equals(mCurrentTitle)) {
//                            fragment.refreshData();
                        }
                    }
                    mViewPager.setScrollble(true);
                } else {    //展开
                    if (CompassApp.GLOBAL.THEME_STYLE == 0) {
                        mTabImg.setBackgroundResource(R.drawable.info_tab_select_selected);
                    } else {
                        mTabImg.setBackgroundResource(R.drawable.info_tab_select_selected2);
                    }
                    //----

                    //----
                    mBarView.setVisibility(View.VISIBLE);
                    mViewPager.setVisibility(View.GONE);
                    mTabDesTv.setVisibility(View.VISIBLE);
                    mTabDesTv.setText("切换栏目");
                    mHorScroll.setVisibility(View.GONE);
//                    mInfoBarTitle.setBackgroundColor(m);

                }
                mCallBack.setInfoCallBack(mBarStatus);
                mBarStatus = !mBarStatus;
                break;
        }
    }

    public boolean setBarStatus() {
        if (mBarStatus && mTabImg != null) {
            mTabImg.performClick();
            return true;
        }
        return false;
    }

    public void setCallBack(callBack mCallBack) {
        this.mCallBack = mCallBack;
    }

    public interface callBack {
        public void setInfoCallBack(boolean mBarStatus);
    }

    public class FragmentAdapter extends FragmentPagerAdapter {
        Fragment currentFragment;

        @Override
        public void setPrimaryItem(ViewGroup container, int position, Object object) {
            currentFragment = (InfoNewFragment) object;
            super.setPrimaryItem(container, position, object);
        }

        private List<Fragment> mFragments;
        FragmentManager fm;

        public FragmentAdapter(FragmentManager fm, List<Fragment> lFragments) {
            super(fm);
            mFragments = new ArrayList<>();
            mFragments.addAll(lFragments);
//            this.mFragments = mFragments;
            this.fm = fm;
        }

        public void setFragmentList(List<Fragment> lFragments) {
            for (int i = 0; i < mFragments.size(); i++) {
                InfoNewFragment fragment = (InfoNewFragment) mFragments.get(i);
                FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
                InfoTabEntity entity = mCategoryList.get(i);
                if (entity.getCategory().equals("1")) {
                    transaction.hide(fragment);
                } else {
                    transaction.show(fragment);
                }
                fragment.setCategory(mCategoryList.get(i).getName());
            }
        }

        @Override
        public Fragment getItem(int arg0) {
            // TODO Auto-generated method stub

            return mFragments.get(arg0);
        }

        @Override
        public int getCount() {
            return mFragments.size();
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
            if (mTabTvList.size() < arg0 + 2) {
                mViewPager.setScrollble(false);
            } else {
                mViewPager.setScrollble(true);
            }
            if (mTabTvList.size() < arg0 + 1) {
                return;
            }
            mIndex = arg0;
            for (int j = 0; j < mTabTvList.size(); j++) {
                mTabTvList.get(j).setTextColor(whiteColor);
                mTabTvList.get(j).setTextSize(15);
            }
            mTabTvList.get(arg0).setTextColor(whiteColor);
            mTabTvList.get(arg0).setTextSize(20);
            mCurrentTitle = mTabTvList.get(arg0).getText().toString();

            InfoNewFragment fragment = (InfoNewFragment) mAdapter.instantiateItem(mViewPager, mViewPager.getCurrentItem());
            fragment.setCategory(mCurrentTitle);
            mAdapter.notifyDataSetChanged();

            setScrollTabPosition(arg0);
            statistic();
        }
    }

    private void statistic(){
        switch (mCurrentTitle) {
            case "头条":
                CompassApp.addStatis(CompassApp.GLOBAL.mgr.INFO_LIST, "0", "",
                        System.currentTimeMillis());
                break;
            case "内参":
                CompassApp.addStatis(CompassApp.GLOBAL.mgr.INFO_LIST, "1", "",
                        System.currentTimeMillis());
                break;
            case "滚动":
                CompassApp.addStatis(CompassApp.GLOBAL.mgr.INFO_LIST, "2", "",
                        System.currentTimeMillis());
                break;
            case "晨会":
                CompassApp.addStatis(CompassApp.GLOBAL.mgr.INFO_LIST, "3", "",
                        System.currentTimeMillis());
                break;
            case "宏观":
                CompassApp.addStatis(CompassApp.GLOBAL.mgr.INFO_LIST, "4", "",
                        System.currentTimeMillis());
                break;
            case "行业":
                CompassApp.addStatis(CompassApp.GLOBAL.mgr.INFO_LIST, "5", "",
                        System.currentTimeMillis());
                break;
            case "公司":
                CompassApp.addStatis(CompassApp.GLOBAL.mgr.INFO_LIST, "6", "",
                        System.currentTimeMillis());
                break;
            case "全球":
                CompassApp.addStatis(CompassApp.GLOBAL.mgr.INFO_LIST, "7", "",
                        System.currentTimeMillis());
                break;
            case "热文":
                CompassApp.addStatis(CompassApp.GLOBAL.mgr.INFO_LIST, "8", "",
                        System.currentTimeMillis());
                break;
            case "新股":
                CompassApp.addStatis(CompassApp.GLOBAL.mgr.INFO_LIST, "9", "",
                        System.currentTimeMillis());
                break;
            case "港股":
                CompassApp.addStatis(CompassApp.GLOBAL.mgr.INFO_LIST, "10", "",
                        System.currentTimeMillis());
                break;
            case "新三板":
                CompassApp.addStatis(CompassApp.GLOBAL.mgr.INFO_LIST, "11", "",
                        System.currentTimeMillis());
                break;
            case "期货":
                CompassApp.addStatis(CompassApp.GLOBAL.mgr.INFO_LIST, "12", "",
                        System.currentTimeMillis());
                break;
            case "期权":
                CompassApp.addStatis(CompassApp.GLOBAL.mgr.INFO_LIST, "13", "",
                        System.currentTimeMillis());
                break;
            case "操盘计划":
                CompassApp.addStatis(CompassApp.GLOBAL.mgr.INFO_LIST, "14", "",
                        System.currentTimeMillis());
                break;
        }
    }

    public void getCurrentName(){
        statistic();
    }

    private void setScrollTabPosition(int arg0) {
        scrollViewWidth = mHorScroll.getWidth();

        int right = mTabMain.getChildAt(arg0).getRight();
        int left = mTabMain.getChildAt(arg0).getLeft();
        if ((scrollViewWidth + offset) < right) {//需要向右移动
            mHorScroll.smoothScrollBy(right - (scrollViewWidth + offset), 0);
            offset += right - (scrollViewWidth + offset);
        }

        if (offset >= left) {//需要向左移动
            mHorScroll.smoothScrollBy(left - offset, 0);
            offset += left - offset;
        }
    }
}
