package com.ez08.compass.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.ez08.compass.ui.base.BaseFragment;

public abstract class IntervalFragment extends BaseFragment {
    private boolean isPrepared;
    private boolean isLazyLoaded;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        isPrepared = true;
        lazyLoad();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        Log.e("IntervalFragment",getClass().getSimpleName()+"  setUserVisibleHint:" +isVisibleToUser);
        lazyLoad();
    }

    private void lazyLoad(){
        if(getUserVisibleHint() && isPrepared && !isLazyLoaded){
            onLazyLoad();
            isLazyLoaded = true;
        }
    }

    public abstract void onLazyLoad();
}
