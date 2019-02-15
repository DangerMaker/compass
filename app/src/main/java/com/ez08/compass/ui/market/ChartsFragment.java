package com.ez08.compass.ui.market;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ez08.compass.CompassApp;
import com.ez08.compass.R;
import com.ez08.compass.entity.ChartsLeaderTitleEntity;
import com.ez08.compass.entity.HotModel;
import com.ez08.compass.entity.StockMarketEntity;
import com.ez08.compass.entity.HolderTitleAndAll;
import com.ez08.compass.entity.HolderOnlyTitle;
import com.ez08.compass.entity.HolderChartsNetWord;
import com.ez08.compass.net.NetInterface;
import com.ez08.compass.parser.ChartsHolderParser;
import com.ez08.compass.parser.StockMarketParser;
import com.ez08.compass.ui.IntervelFragment;
import com.ez08.compass.ui.market.adapter.ChartsAdapter;
import com.ez08.compass.ui.market.view.CustomGridItemDecoration;
import com.ez08.support.net.EzMessage;
import com.ez08.support.net.NetResponseHandler2;
import com.ez08.support.util.EzValue;
import com.ez08.tools.IntentTools;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.json.JSONArray;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ChartsFragment extends IntervelFragment implements View.OnClickListener {

    private final int WHAT_REFRESH_CHARTS = 1000; //行情数据
    private final int WHAT_REFRESH_STOCK_DETAIL = 1001;

    private SmartRefreshLayout mListViewFrame;
    private RecyclerView mRecyclerView;
    GridLayoutManager manager;
    private ChartsAdapter adapter;

    List<Object> mList;
    String hotstocks;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Log.e("BaseFragment",this.getClass().getSimpleName());

        View view = View.inflate(getActivity(), R.layout.fragment_watch, null);
        mListViewFrame = (SmartRefreshLayout) view.findViewById(R.id.watch_lv_frame);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        manager = new GridLayoutManager(mContext, 4);
        mRecyclerView.setLayoutManager(manager);
        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int i) {
                int type = mRecyclerView.getAdapter().getItemViewType(i);
                switch (type) {
                    case ChartsAdapter.INDUSTRY:
                    case ChartsAdapter.POPULAR:
                        return 1;
                }
                return 4;
            }
        });
        adapter = new ChartsAdapter(mContext);
        mRecyclerView.setAdapter(adapter);
        mListViewFrame.autoRefresh();


//        DividerItemDecoration divider = new DividerItemDecoration(
//                mContext,
//                DividerItemDecoration.VERTICAL
//        );
//        divider.setDrawable(ContextCompat.getDrawable(mContext, R.drawable.line_light_1px));
        CustomGridItemDecoration divider = new CustomGridItemDecoration(mContext);
        mRecyclerView.addItemDecoration(divider);

        mListViewFrame.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                NetInterface.getStockCharts(mHandler, WHAT_REFRESH_CHARTS);

            }
        });

        return view;
    }

    @Override
    public void postMethod() {
        if (TextUtils.isEmpty(hotstocks)) {
            NetInterface.getStockBrief(mHandler, WHAT_REFRESH_STOCK_DETAIL, hotstocks);
        }
    }

    @SuppressLint("HandlerLeak")
    NetResponseHandler2 mHandler = new NetResponseHandler2() {

        @Override
        public void netConnectLost(int what) {
            super.netConnectLost(what);
        }

        @Override
        public void timeout(int what) {
            super.timeout(what);
        }

        @Override
        public void receive(int arg0, boolean b, Intent intent) {
            switch (arg0) {
                case WHAT_REFRESH_CHARTS:
                    mListViewFrame.finishRefresh();
                    if (intent != null) {
                        if (mList == null) {
                            mList = new ArrayList<>();
                        } else {
                            mList.clear();
                        }

                        String list0 = intent.getStringExtra("0");
                        List<Object> listArray0 = parseNetWords(list0);

                        String list1 = intent.getStringExtra("1");
                        List<Object> listArray1 = parse(list1);

                        String list2 = intent.getStringExtra("2");
                        List<Object> listArray2 = parse(list2);

                        mList.add(new HolderOnlyTitle("热词关注"));

                        if (listArray2 != null) {
                            mList.add(new HolderOnlyTitle("行业板块",true));
                            mList.addAll(listArray2);
                        }

                        if (listArray1 != null) {
                            mList.add(new HolderOnlyTitle("概念板块",true));
                            mList.addAll(listArray1);
                        }

                        if (listArray0 != null) {
                            mList.add(new HolderOnlyTitle("网络热词",true));
                            mList.addAll(listArray0);
                        }


                        String hotstocks = intent.getStringExtra("hotstocks");
                        if(hotstocks != null) {
                            mList.add(new HolderTitleAndAll("个股关注",0,11));
                            String[] mediaCodes = hotstocks.split(",");
                            for (int i = 0; i < mediaCodes.length; i++) {
                                StockMarketEntity entity = new StockMarketEntity();
                                entity.setSecucode(mediaCodes[i]);
                                mList.add(entity);
                            }
                        }

                        String holderchange1 = intent.getStringExtra("holderchange1");
                        String holderchange2 = intent.getStringExtra("holderchange2");
                        ChartsHolderParser parser1 = new ChartsHolderParser();
                        List<Object> list3 = parser1.parse(holderchange1);
                        ChartsHolderParser parser2 = new ChartsHolderParser();
                        List<Object> list4 = parser2.parse(holderchange2);

                        if(list3 != null) {
                            mList.add(new HolderOnlyTitle("高管增持"));
                            mList.add(new ChartsLeaderTitleEntity());
                            mList.addAll(list3);
                        }

                        if(list4 != null) {
                            mList.add(new HolderOnlyTitle("高管减持"));
                            mList.add(new ChartsLeaderTitleEntity());
                            mList.addAll(list4);
                        }

//                        if (mList != null)
//                            adapter.clearAndAddAll(mList);

                        NetInterface.getStockBrief(mHandler, WHAT_REFRESH_STOCK_DETAIL, hotstocks);
                    }
                    break;

                case WHAT_REFRESH_STOCK_DETAIL:
                    //匹配详细内容
                    EzValue vaule = IntentTools.safeGetEzValueFromIntent(
                            intent, "list");
                    if (vaule != null) {
                        EzMessage[] msg = vaule.getMessages();
                        if (msg != null) {
                            StockMarketParser parser = new StockMarketParser();
                            for (int i = 0; i < msg.length; i++) {
                                StockMarketEntity entity = parser.parse(msg[i]);
                                for (int j = 0; j < mList.size(); j++) {
                                    Object temp = mList.get(j);
                                    if (temp instanceof StockMarketEntity &&
                                            ((StockMarketEntity) temp).getSecucode().equals(entity.getSecucode())) {
                                        StockMarketEntity marketEntity = ((StockMarketEntity)temp);
                                        marketEntity.setCurrent(entity.getCurrent());
                                        marketEntity.setExp(entity.getExp());
                                        marketEntity.setLastclose(entity.getLastclose());
                                        marketEntity.setSecuname(entity.getSecuname());
                                        marketEntity.setState(entity.getState());
                                        marketEntity.setType(entity.getType());
                                    }
                                }
                            }
                            adapter.clearAndAddAll(mList);
                        }
                    }


                    break;
            }
        }
    };

    private List<Object> parseNetWords(String intent) {
        if (intent != null) {
            List<Object> netWords = new ArrayList<>();

            try {
                intent = URLDecoder.decode(intent, "UTF-8");
                JSONArray jsonArray = new JSONArray(intent);
                if (jsonArray != null && jsonArray.length() > 0) {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONArray jsonArray1 = new JSONArray(jsonArray.get(i).toString());
                        if(jsonArray1.length() < 1){
                            continue;
                        }

                        String name = jsonArray1.getString(0);
                        netWords.add(new HolderChartsNetWord(name));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return netWords;

        }
        return null;
    }

    private List<Object> parse(String intent) {
        List<Object> list = new ArrayList<>();
        if (intent != null) {
            try {
                intent = URLDecoder.decode(intent, "UTF-8");
                JSONArray jsonArray = new JSONArray(intent);
                if (jsonArray != null) {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        String item = jsonArray.get(i).toString();
                        JSONArray jsonArray1 = new JSONArray(item);
                        if (jsonArray1 != null) {
                            HotModel model = new HotModel();
                            model.name = jsonArray1.getString(1);
                            if (jsonArray1.optInt(2, 0) == -2 || jsonArray1.optInt(2, 0) == 0) {
                                model.isUseful = false;
                            } else {
                                model.isUseful = true;
                            }

                            if (model.isUseful) {
                                model.hotValue = jsonArray1.getInt(3);
                                JSONArray jsonArray2 = jsonArray1.getJSONArray(4);
                                if (jsonArray2 != null) {
                                    for (int j = 0; j < jsonArray2.length(); j++) {
                                        if (jsonArray2.get(j) instanceof JSONArray) {
                                            model.MostValue = ((JSONArray) jsonArray2.get(j)).getDouble(2);
                                        }
                                    }

                                }
                            } else {
                                model.hotValue = 0;
                                model.MostValue = 0;
                            }
                            list.add(model);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                return list;
            }

        }

        List<HotModel> posList = new ArrayList<>();
        List<HotModel> negList = new ArrayList<>();
        List<HotModel> zeroList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            HotModel model = (HotModel) list.get(i);
            if (model.hotValue > 0) {
                posList.add(model);
            } else if (model.hotValue < 0) {
                negList.add(model);
            } else {
                zeroList.add(model);
            }
        }

        if (CompassApp.GLOBAL.CUSTOMER_LEVEL < 0) {
            Collections.sort(posList);
            for (int i = 0; i < negList.size(); i++) {
                negList.get(i).hotValue = 0;
                negList.get(i).MostValue = 0;
            }
            zeroList.addAll(negList);
            Collections.sort(zeroList);
            list.clear();
            list.addAll(posList);
            list.addAll(zeroList);
        } else {
            Collections.sort(posList);
            Collections.sort(negList);
            Collections.sort(zeroList);
            list.clear();
            list.addAll(posList);
            list.addAll(negList);
            list.addAll(zeroList);
        }


        return list;
    }


    @Override
    public void onClick(View v) {
    }
}