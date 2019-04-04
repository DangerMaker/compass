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
import com.ez08.compass.entity.ItemStock;
import com.ez08.compass.net.NetInterface;
import com.ez08.compass.parser.StockDetailListParser;
import com.ez08.compass.tools.StockUtils;
import com.ez08.compass.ui.base.BaseFragment;
import com.ez08.compass.ui.stocks.adpater.ListAdapter;
import com.ez08.support.net.NetResponseHandler2;

import java.util.ArrayList;

public class ChangeListFragment extends BaseFragment {

    public static ChangeListFragment newInstance(String code) {
        Bundle args = new Bundle();
        args.putString("code",code);
        ChangeListFragment fragment = new ChangeListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    RecyclerView recyclerView;
    ListAdapter adapter;
    private final int WHAT_REFRESH_STOCK_LIST = 1002;
    private final int WHAT_REFRESH_STOCK_DETAIL = 1001;

    private ArrayList<ItemStock> mTempList;
    private ArrayList<ItemStock> stocklistAll;

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

        mTempList=new ArrayList<>();
        stocklistAll=new ArrayList<>();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        NetInterface.getSortStockList(mHandler, WHAT_REFRESH_STOCK_LIST, 0, 1,(String)getArguments().get("code") , 0, 10);

    }

    @SuppressLint("HandlerLeak")
    NetResponseHandler2 mHandler = new NetResponseHandler2() {

        @Override
        public void receive(int arg0, boolean b, Intent intent) {
            switch (arg0){
                case WHAT_REFRESH_STOCK_LIST:
                    mTempList.clear();
                    String[] list = intent.getStringArrayExtra("list");
                    if (list != null && list.length > 0) {
                        for (int i = 0; i < list.length; i++) {
                            ItemStock entity = new ItemStock();
                            entity.setCode(StockUtils.getStockCode(list[i]));
                            mTempList.add(entity);
                        }
                        String secucodes = "";
                        for (int i = 0; i < list.length; i++) {
                            secucodes = secucodes + StockUtils.getStockCode(list[i]);
                            if (i != list.length - 1) {
                                secucodes = secucodes + ",";
                            }
                        }
                        NetInterface.getStockBrief(mHandler, WHAT_REFRESH_STOCK_DETAIL, secucodes);
                    }
                    break;
                case WHAT_REFRESH_STOCK_DETAIL:
                    StockDetailListParser parser = new StockDetailListParser(mTempList);
                    parser.parserResult(intent);
                    stocklistAll.clear();
                    stocklistAll.addAll(mTempList);
                    adapter.addAll(stocklistAll);
                    break;
            }
        }
    };




}
