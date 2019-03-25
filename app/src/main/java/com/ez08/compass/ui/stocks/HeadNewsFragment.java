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
import com.ez08.compass.entity.InfoEntity;
import com.ez08.compass.net.NetInterface;
import com.ez08.compass.ui.base.BaseFragment;
import com.ez08.compass.ui.stocks.adpater.HeadNewsAdapter;
import com.ez08.support.net.EzMessage;
import com.ez08.support.net.NetResponseHandler2;
import com.ez08.support.util.EzValue;
import com.ez08.tools.IntentTools;

import java.util.ArrayList;
import java.util.List;

public class HeadNewsFragment extends BaseFragment {

    RecyclerView recyclerView;
    HeadNewsAdapter adapter;

    private final int WHAT_REFRESH = 1000; // 请求数据
    private List<InfoEntity> mList = new ArrayList<InfoEntity>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recyclerview,container,false);
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        adapter = new HeadNewsAdapter(mContext);
        recyclerView.setAdapter(adapter);

        DividerItemDecoration divider = new DividerItemDecoration(
                mContext,
                DividerItemDecoration.VERTICAL
        );
        divider.setDrawable(ContextCompat.getDrawable(mContext, R.drawable.line_light_1px));
        recyclerView.addItemDecoration(divider);

        NetInterface.requestNewInfoList(mHandler, WHAT_REFRESH, null, 10, "头条");
        return view;
    }

    @SuppressLint("HandlerLeak")
    NetResponseHandler2 mHandler = new NetResponseHandler2() {

        @Override
        public void receive(int arg0, boolean b, Intent intent) {
            if (intent != null) {
                EzValue value = IntentTools.safeGetEzValueFromIntent(
                        intent, "list");
                if (value != null) {
                    EzMessage[] msges = value.getMessages();
                    if (msges != null) {
                        for (int i = 0; i < msges.length; i++) {
                            InfoEntity entity = parser(msges[i]);
                            mList.add(entity);
                        }
                        adapter.addAll(mList);
                    }
                }
            }
        }
    };

    private InfoEntity parser(EzMessage msg) {
        InfoEntity entity = new InfoEntity();
        String id = msg.getKVData("id").getStringWithDefault("");
        entity.setId(id);
        String title = msg.getKVData("title").getStringWithDefault("");
        entity.setTitle(title);
        String url = msg.getKVData("url").getStringWithDefault("");
        entity.setUrl(url);
        String imageid = msg.getKVData("imageid").getStringWithDefault("");
        entity.setImageid(imageid);
        long time = msg.getKVData("time").getInt64();
        entity.setTime(time);
        String content = msg.getKVData("content").getStringWithDefault("");
        entity.setContent(content);
        String category = msg.getKVData("category").getStringWithDefault("");
        entity.setCategory(category);
        return entity;
    }

}
