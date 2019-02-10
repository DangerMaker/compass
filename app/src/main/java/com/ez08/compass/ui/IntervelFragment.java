package com.ez08.compass.ui;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;

public abstract class IntervelFragment extends BaseFragment {
    private static final int INTERVEL_POST = 8001;
    private static final int INTERVEL_POST_DELAY = 8002;
    public static final int INTERVEL_TIME = 5000;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

   public Handler intervelHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case INTERVEL_POST:
                    postMethod();
                    break;
                case INTERVEL_POST_DELAY:
                    postMethod();
                    intervelHandler.sendEmptyMessageDelayed(INTERVEL_POST_DELAY, INTERVEL_TIME);
                    break;
            }
        }
    };

    public abstract void postMethod();

    public void postImmediately(){
        intervelHandler.sendEmptyMessage(INTERVEL_POST);
    }

    @Override
    public void onResume() {
        super.onResume();
        postImmediately();
        intervelHandler.sendEmptyMessageDelayed(INTERVEL_POST_DELAY, INTERVEL_TIME);
    }

    @Override
    public void onPause() {
        super.onPause();
        intervelHandler.removeMessages(INTERVEL_POST_DELAY);
    }
}
