package com.ez08.compass.ui.media;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ez08.compass.CompassApp;
import com.ez08.compass.R;
import com.ez08.compass.ui.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;

public class ClassFragment extends BaseFragment implements OnClickListener {
    private ViewPager mViewPager;
    private FragmentAdapter mAdapter;
    private List<Fragment> mFragments = new ArrayList<Fragment>();
    private TextView txtClass;
    private TextView txtVideo;
    private int mCurPosition = 0;
    private MyClassFragment timeFragment1;
    private VideoFragment timeFragment2;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View view = inflater.inflate(R.layout.fragment_media, null);
        txtClass = (TextView) view.findViewById(R.id.txt_class);
        txtVideo = (TextView) view.findViewById(R.id.txt_vedio);
        txtClass.setText("教室");
        txtVideo.setText("往期");
        initTitle(0);
        txtClass.setOnClickListener(this);
        txtVideo.setOnClickListener(this);
        mViewPager = (ViewPager) view.findViewById(R.id.second_pager);

        timeFragment1 = new MyClassFragment();
        mFragments.add(timeFragment1);
        timeFragment2 = new VideoFragment();
        mFragments.add(timeFragment2);

        mAdapter = new FragmentAdapter(getChildFragmentManager(), mFragments);
        mViewPager.setAdapter(mAdapter);
        mViewPager.setOnPageChangeListener(new PageChangeListener());
        return view;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }


    private void initTitle(int curPos) {
        switch (curPos) {
            case 0:
                txtClass.setTextSize(20);
                txtVideo.setTextSize(15);
                break;
            case 1:
                txtClass.setTextSize(15);
                txtVideo.setTextSize(20);
                break;
            default:
                break;
        }
    }

    public class FragmentAdapter extends FragmentPagerAdapter {

        private List<Fragment> mFragments;

        public FragmentAdapter(FragmentManager fm, List<Fragment> mFragments) {
            super(fm);
            this.mFragments = mFragments;
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

    private class PageChangeListener implements OnPageChangeListener {

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageSelected(int arg0) {
            // txtTitle.setText(titles[arg0]);
            initTitle(arg0);
            mCurPosition = arg0;
            if (arg0 == 0) {
                CompassApp.addStatis(CompassApp.GLOBAL.mgr.LIVE_ROOMLIST, "0", "",
                        System.currentTimeMillis());
            } else {
                CompassApp.addStatis(CompassApp.GLOBAL.mgr.LIVE_VIDEOLIST, "0", "",
                        System.currentTimeMillis());
            }
        }
    }

    public int getCurPosition() {
        return mCurPosition;
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.txt_class:
                mViewPager.setCurrentItem(0);
                initTitle(0);
                break;
            case R.id.txt_vedio:
                mViewPager.setCurrentItem(1);
                initTitle(1);
                break;
        }
    }

}
