package com.ez08.compass.ui.stocks;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ez08.compass.R;
import com.ez08.compass.net.NetInterface;
import com.ez08.compass.ui.base.BaseFragment;
import com.ez08.compass.ui.stocks.adpater.ListAdapter;
import com.ez08.support.net.EzMessage;
import com.ez08.support.net.NetResponseHandler2;
import com.ez08.support.util.EzValue;
import com.ez08.tools.IntentTools;

public abstract class ListFragment extends BaseFragment {

    RecyclerView recyclerView;
    ListAdapter adapter;
    EzParser parser;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recyclerview, container, false);
        recyclerView = view.findViewById(R.id.recycler_view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        DividerItemDecoration divider = new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL);
        divider.setDrawable(ContextCompat.getDrawable(mContext, R.drawable.line_light_1px));
        recyclerView.addItemDecoration(divider);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        adapter = new ListAdapter(mContext);
        recyclerView.setAdapter(adapter);
        }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        request();
    }

    public abstract void request();

    public abstract void response(EzMessage[] messages);

    public void setParser(EzParser parser) {
        this.parser = parser;
    }

    @SuppressLint("HandlerLeak")
    NetResponseHandler2 mHandler = new NetResponseHandler2() {

        @Override
        public void receive(int arg0, boolean b, Intent intent) {
            if (intent != null) {
                EzValue value = IntentTools.safeGetEzValueFromIntent(
                        intent, "list");
                if (value != null) {
                    EzMessage[] msgs = value.getMessages();
                    response(msgs);
                }
            }
        }
    };

}
