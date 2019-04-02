package com.ez08.compass.ui.stocks;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ez08.compass.R;
import com.ez08.compass.entity.KChartEntity;
import com.ez08.compass.entity.StockDetailEntity;
import com.ez08.compass.entity.StockDrNewEntity;
import com.ez08.compass.entity.StockDrNewParser;
import com.ez08.compass.net.NetInterface;
import com.ez08.compass.parser.KlineNewParser;
import com.ez08.compass.tools.DDSID;
import com.ez08.compass.tools.StockUtils;
import com.ez08.compass.ui.base.BaseFragment;
import com.ez08.compass.ui.view.KLineView;
import com.ez08.support.net.NetResponseHandler2;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;


public class KLineFragment extends BaseFragment {

    private final int GET_CAPITAL_INFO = 1001;//获取历史资金信息
    private final int GET_CURRENT_CAPITAL_INFO = 1002;//获取当日资金信息
    private final int GET_DR_LIST = 1003; //获得除权信息
    private final int GET_KLINE_LIST = 1004; //获得k线数据
    private final int GET_KLINE_LIST_MORE = 1007; //获得k线数据
    private final int GET_KLINE_LIST_HYD = 1005; //获得股价活跃度数据
    private final int GET_KLINE_LIST_CXJC = 1006; //获得长线数据
    private final int DATA_LENGTH = -KLineView.KLINE_VIEW_NET_ONCE;   //一次性获取多少条k线数据

    TextView maText;
    KLineView kLineView;

    private String mStockPeriod = "day";  //day、week、month、1minute
    private int mKlineRequestDate;  //获取k线数据的mark(日期，年月日)
    private int type = 0;//0 日k， 1 周k，2 月k

    List<StockDrNewEntity> mDrList; //获得除权数组
    StockDetailEntity detailEntity;
    String code;
    List<KChartEntity> mTotalList;

    public static KLineFragment newInstance(StockDetailEntity detailEntity, String period) {
        KLineFragment fragment = new KLineFragment();
        Bundle args = new Bundle();
        args.putSerializable("detail", detailEntity);
        args.putString("mStockPeriod", period);
        fragment.setArguments(args);
        return fragment;
    }

    public boolean getFocus() {
        if (kLineView != null) {
            return kLineView.getFocus();

        }
        return false;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_kline, null);
        maText = (TextView) view.findViewById(R.id.ma_text);
        maText.setText(Html.fromHtml(StockUtils.MA5102060));
        kLineView = (KLineView) view.findViewById(R.id.k_line_view);
        kLineView.setHandler(viewHandler);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String dateNowStr = sdf.format(d);
        mKlineRequestDate = Integer.parseInt(dateNowStr);
        if (getArguments() != null) {
            mStockPeriod = getArguments().getString("mStockPeriod");
            if (mStockPeriod.equals("day")) {
                type = 0;
            } else if (mStockPeriod.equals("week")) {
                type = 1;
            } else if (mStockPeriod.equals("month")) {
                type = 2;
            } else {
                type = -1;
            }

            detailEntity = (StockDetailEntity) getArguments().getSerializable("detail");
            kLineView.setDesEntity(detailEntity);
            code = detailEntity.getSecucode();
            mTotalList = new ArrayList<>();
            NetInterface.getStockDrHistory(mHandler, GET_DR_LIST, code);
        }


    }

    @SuppressLint("HandlerLeak")
    Handler viewHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case KLineView.GET_MORE_LINE:
                    mKlineRequestDate = (int) mTotalList.get(0).getlTime();
                    NetInterface.getStockKlineNew(mHandler, GET_KLINE_LIST_MORE, code, mStockPeriod, mKlineRequestDate, DATA_LENGTH);
                    break;
            }
        }
    };


    @SuppressLint("HandlerLeak")
    NetResponseHandler2 mHandler = new NetResponseHandler2() {

        @Override
        public void receive(int arg0, boolean b, Intent intent) {
            switch (arg0) {
                case GET_DR_LIST:
                    if (intent != null) {
                        List<StockDrNewEntity> lDrList = new StockDrNewParser().parser(intent);
                        mDrList = new ArrayList<>();
                        if (lDrList != null) {
                            mDrList.addAll(lDrList);
                        }
                        NetInterface.getStockKlineNew(mHandler, GET_KLINE_LIST, code, mStockPeriod, mKlineRequestDate, DATA_LENGTH);
                    }
                    break;
                case GET_KLINE_LIST:
                    List<KChartEntity> list = new KlineNewParser().parse(intent, mDrList, detailEntity.getDecm());
                    mTotalList.clear();
                    mTotalList.addAll(list);
                    kLineView.setData(mTotalList);
                    break;
                case GET_KLINE_LIST_MORE:
                    List<KChartEntity> listMore = new KlineNewParser().parse(intent, mDrList, detailEntity.getDecm());
                    if(listMore != null && listMore.size() > 1){
                        listMore.remove(listMore.size() - 1);
                        mTotalList.addAll(0,listMore);
                        kLineView.setMoreData(mTotalList);
                    }
                    break;
            }
        }
    };
}
