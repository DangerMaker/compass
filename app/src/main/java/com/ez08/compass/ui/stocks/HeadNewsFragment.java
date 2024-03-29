package com.ez08.compass.ui.stocks;


import com.ez08.compass.net.NetInterface;
import com.ez08.support.net.EzMessage;

public class HeadNewsFragment extends ListFragment {

    @Override
    public void request() {
        NetInterface.requestNewInfoList(mHandler, 10000, null, 10, "头条");

    }

    @Override
    public void response(EzMessage[] messages) {
        adapter.setEzMessage(messages, new InfoParser());
    }

}
