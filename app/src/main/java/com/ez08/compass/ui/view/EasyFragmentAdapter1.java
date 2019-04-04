package com.ez08.compass.ui.view;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.ez08.compass.ui.market.customtab.EasyFragment;

import java.util.List;

public class EasyFragmentAdapter1 extends FragmentStatePagerAdapter {

    private List<EasyFragment> mFragments;

    public EasyFragmentAdapter1(FragmentManager fm, List<EasyFragment> mFragments) {
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
}