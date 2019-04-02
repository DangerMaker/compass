package com.ez08.compass.ui.stocks;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.ez08.compass.R;
import com.ez08.compass.net.NetInterface;
import com.ez08.compass.parser.StockCapitalDetailNewParser;
import com.ez08.compass.ui.base.BaseFragment;
import com.ez08.compass.ui.stocks.view.LineChartView;
import com.ez08.compass.ui.stocks.view.SegmentedGroup;
import com.ez08.support.net.EzMessage;
import com.ez08.support.net.NetResponseHandler2;
import com.ez08.support.util.EzValue;
import com.ez08.tools.IntentTools;

public class CapitalFragment extends BaseFragment {

    private final static int WHAT_CAPITAL_DETAIL = 10006;  //获取当日资金数据
    LineChartView chartView;
    SegmentedGroup group;
    private PieChartEntity pieChartEntity;
    private int flag = LineChartView.Main_Capital;
    String code;

    public static CapitalFragment newInstance(String code) {
        Bundle args = new Bundle();
        args.putString("code", code);
        CapitalFragment fragment = new CapitalFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_capital, null);
        group = view.findViewById(R.id.segmented_group);
        chartView = view.findViewById(R.id.line_chart_view);
        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int id = group.getCheckedRadioButtonId();
                if (id == R.id.zl_radio) {
                    flag = LineChartView.Main_Capital;
                } else if (id == R.id.gsd_radio) {
                    flag = LineChartView.GSD_Capital;
                } else {
                    flag = LineChartView.DK_Capital;
                }
                chartView.setData(pieChartEntity, flag);
            }
        });
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getArguments() != null) {
            code = getArguments().getString("code");
            NetInterface.getStockCapitalNew(mHandler, WHAT_CAPITAL_DETAIL, code);
        }
    }


    @SuppressLint("HandlerLeak")
    NetResponseHandler2 mHandler = new NetResponseHandler2() {
        @Override
        public void netConnectLost(int arg0) {
        }

        @Override
        public void timeout(int arg0) {
        }

        @Override
        public void receive(int arg0, boolean b, Intent arg2) {
            switch (arg0) {
                case WHAT_CAPITAL_DETAIL:
                    if (arg2 != null) {
                        EzValue detail = IntentTools.safeGetEzValueFromIntent(
                                arg2, "detail");
                        if (detail != null) {
                            EzMessage msges = detail.getMessage();
                            StockCapitalDetailNewParser parser = new StockCapitalDetailNewParser();
                            pieChartEntity = parser.parse(msges);
                            ((RadioButton)group.getChildAt(0)).setChecked(true);
                        }
                    }
                    break;
            }
        }
    };
}
