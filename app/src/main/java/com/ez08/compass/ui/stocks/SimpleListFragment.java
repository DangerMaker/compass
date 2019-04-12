package com.ez08.compass.ui.stocks;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ez08.compass.R;
import com.ez08.compass.entity.BaseNewsEntity;
import com.ez08.compass.entity.StockNews;
import com.ez08.compass.entity.StockNewsEntity;
import com.ez08.compass.entity.StockNotice;
import com.ez08.compass.entity.StockReport;
import com.ez08.compass.net.HttpUtils;
import com.ez08.compass.parser.StockNewsParser;
import com.ez08.compass.tools.StockUtils;
import com.ez08.compass.tools.ToastUtils;
import com.ez08.compass.ui.base.BaseFragment;
import com.ez08.compass.ui.stocks.adpater.ListAdapter;
import com.ez08.compass.ui.stocks.adpater.SimpleListAdapter;

import java.util.ArrayList;
import java.util.List;

public class SimpleListFragment extends BaseFragment {

    public static SimpleListFragment newInstance(String code, int type) {
        Bundle args = new Bundle();
        SimpleListFragment fragment = new SimpleListFragment();
        args.putString("code", code);
        args.putInt("type", type);
        fragment.setArguments(args);
        return fragment;
    }

    protected View rootView;
    protected RecyclerView recyclerView;
    protected TextView tipsTextView;

    SimpleListAdapter adapter;
    int type = -1; // 0新闻 2研报 1公告
    String typeName;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_simple_list, null);
        recyclerView = rootView.findViewById(R.id.list_view);
        tipsTextView = rootView.findViewById(R.id.tips);

        DividerItemDecoration divider = new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL);
        divider.setDrawable(ContextCompat.getDrawable(mContext, R.drawable.line_light_1px));
        recyclerView.addItemDecoration(divider);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        adapter = new SimpleListAdapter(mContext);
        recyclerView.setAdapter(adapter);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getArguments() != null && !TextUtils.isEmpty(getArguments().getString("code"))) {
            type = getArguments().getInt("type");

            new NewsTask(new CallBack() {
                @Override
                public void setError() {
                    tipsTextView.setText("加载失败");
                    tipsTextView.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                }

                @Override
                public void setData(StockNewsEntity stockNewsEntity) {
                    List<Object> lList = new ArrayList<>();
                    switch (type) {
                        case 0: //新闻
                            typeName = "新闻";
                            final List<StockNews> stockNewsList = stockNewsEntity
                                    .getNews();
                            if (stockNewsList != null && !stockNewsList.isEmpty()) {
                                for (int i = 0; i < stockNewsList.size(); i++) {
                                    lList.add(stockNewsEntity.getNews().get(i));
                                }
                            }

                            break;
                        case 1: //公告
                            typeName = "公告";
                            final List<StockNotice> stockNoticeList = stockNewsEntity
                                    .getNotice();
                            if (stockNoticeList != null && !stockNoticeList.isEmpty()) {
                                for (int i = 0; i < stockNoticeList.size(); i++) {
                                    lList.add(stockNewsEntity.getNotice().get(i));
                                }
                            }
                            break;
                        case 2: //研报
                            typeName = "研报";
                            final List<StockReport> stockReportList = stockNewsEntity
                                    .getReport();
                            if (stockReportList != null && !stockReportList.isEmpty()) {
                                for (int i = 0; i < stockReportList.size(); i++) {
                                    lList.add(stockNewsEntity.getReport().get(i));
                                }
                            }
                            break;
                    }

                    if(lList.isEmpty()){
                        tipsTextView.setText("暂无" + typeName);
                        tipsTextView.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                    }else{
                        tipsTextView.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                        if(lList.size() == 10){
                            lList.add(Integer.valueOf(type));
                        }
                        adapter.addAll(lList);

                    }

                }
            }).execute(StockUtils.cutShortStockCode(getArguments().getString("code")));
        }
    }

    private static class NewsTask extends AsyncTask<String, Integer, String> {

        CallBack callBack;

        public NewsTask(CallBack callBack) {
            this.callBack = callBack;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (!TextUtils.isEmpty(result)) {
                StockNewsParser stockNewsParser = new StockNewsParser();
                StockNewsEntity stockNewsEntity = stockNewsParser.parserResult(result);

                if (stockNewsEntity == null) {
                    callBack.setError();
                    return;
                }
                callBack.setData(stockNewsEntity);
            } else {
                callBack.setError();
            }
        }

        @Override
        protected String doInBackground(String... arg0) {
            String url = "http://app.compass.cn/stock/data_infolist_preview.php?code=" + arg0[0] + "&num=" + 10;
            Log.e("url",url);
            String jsonString = HttpUtils
                    .getJsonContent(url);
            return jsonString;
        }

    }

    private interface CallBack {
        void setError();

        void setData(StockNewsEntity entity);
    }
}
