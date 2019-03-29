package com.ez08.compass.ui.stocks;


import com.ez08.compass.net.NetInterface;
import com.ez08.support.net.EzMessage;

public class InnerNewsFragment extends ListFragment {

    @Override
    public void request() {
        NetInterface.requestNewInfoList(mHandler, 10000, null, 10, "内参");

    }

    @Override
    public void response(EzMessage[] messages) {
        adapter.setEzMessage(messages, new Info1Parser());
    }

}
