package com.ez08.compass.ui.stocks;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.ez08.compass.R;
import com.ez08.compass.entity.InfoEntity;
import com.ez08.compass.entity.StockDetailEntity;
import com.ez08.compass.net.NetInterface;
import com.ez08.compass.parser.IndicatorHelper;
import com.ez08.compass.parser.StockDetailParser;
import com.ez08.compass.tools.StockUtils;
import com.ez08.compass.ui.base.BaseActivity;
import com.ez08.compass.ui.base.BaseViewHolder;
import com.ez08.compass.ui.market.customtab.EasyFragment;
import com.ez08.compass.ui.market.tablayout.SlidingTabLayout;
import com.ez08.compass.ui.stocks.adpater.HeadNewsHolder;
import com.ez08.compass.ui.stocks.view.IndexQuoteView;
import com.ez08.compass.ui.stocks.view.LoremIpsumAdapter;
import com.ez08.compass.ui.view.EazyFragmentAdpater;
import com.ez08.compass.ui.view.SingleLineAutoResizeTextView;
import com.ez08.compass.ui.view.StockDetailHeader;
import com.ez08.support.net.EzMessage;
import com.ez08.support.net.NetResponseHandler2;
import com.ez08.support.util.EzValue;
import com.ez08.tools.IntentTools;

import java.util.ArrayList;
import java.util.List;

public class StockVerticalActivity extends BaseActivity implements View.OnClickListener {

    private final int WHAT_GET_STOCK_DETAIL = 10001;    //获取股票详细报价信息
    public static final String TAG = "StockVerticalActivity";
    List<String> stockList;
    String stockCode;
    StockDetailEntity detailEntity;

    SingleLineAutoResizeTextView singleTextView;
    ImageButton backBtn;
    ImageButton searchBtn;
    StockPopupWindows stockPopupWindows;

    StockDetailHeader headerView;
    IndexQuoteView indexQuoteView;
    SlidingTabLayout bottomTabLayout;

//    FrameLayout frameLayout;
//    TabLayout tabLayout;
//    ViewPager viewPager;
//    EazyFragmentAdpater mAdapter;
//
//    FenshiFragment fenshiFragment;
//    KLineFragment m30fragment;
//    FragmentManager fragmentManager;
//    StockBottomTabFragment bottomTabFragment;
//    LinearLayout tradeLayout;

    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ativity_stock_detail_vertical1);

        Intent getIntent = getIntent();
        if (getIntent != null) {
            stockList = getIntent.getStringArrayListExtra("stock_list");
            stockCode = getIntent.getStringExtra("stock_code");
        }

        initView();
    }


    private void initView() {
        singleTextView = (SingleLineAutoResizeTextView) findViewById(R.id.page_name);
        backBtn = (ImageButton) findViewById(R.id.back_btn);
        backBtn.setOnClickListener(this);
        searchBtn = (ImageButton) findViewById(R.id.search_btn);
        searchBtn.setOnClickListener(this);

        headerView = (StockDetailHeader)findViewById(R.id.stock_detail_header);
        indexQuoteView = headerView.findViewById(R.id.stock_index_quote);
        indexQuoteView.setOnClickListener(this);

        recyclerView = (RecyclerView) findViewById(R.id.card_recyclerview);
        DividerItemDecoration divider = new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL);
        divider.setDrawable(ContextCompat.getDrawable(mContext, R.drawable.line_light_1px));
        recyclerView.addItemDecoration(divider);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        LoremIpsumAdapter adapter = new LoremIpsumAdapter(this);
        recyclerView.setAdapter(adapter);


        if (!TextUtils.isEmpty(stockCode))
            NetInterface.getStockDetailNew(mHandler, WHAT_GET_STOCK_DETAIL, stockCode);

//        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
//        viewPager = (ViewPager) findViewById(R.id.view_pager);
//        fragmentManager = getSupportFragmentManager();
//
//        tradeLayout = (LinearLayout) findViewById(R.id.stock_security_tv);
//        tradeLayout.setOnClickListener(this);
//
//        frameLayout = (FrameLayout) findViewById(R.id.stock_detail_bottom);
//
//        MyAppCompat.setTextBackgroud(tradeLayout,mContext);

    }


//    int type = 0; //0 head news 1 inner news 2 capital
//    List<InfoEntity> headNewsList = new ArrayList<>();
//
//    public class StockDetailAdapter extends RecyclerView.Adapter {
//
//        public static final int TYPE_HEADER = 0;
//        public static final int TYPE_TAB = 1;
//        public static final int TYPE_HEAD_NEWS = 2;
//        public static final int TYPE_INNER_NEWS = 3;
//        public static final int TYPE_CAPITAL = 4;
//
//        @Override
//        public int getItemViewType(int position) {
//            switch (position) {
//                case 0:
//                    return TYPE_HEADER;
//                case 1:
//                    return TYPE_TAB;
//            }
//
//            switch (type) {
//                case 0:
//                    return TYPE_HEAD_NEWS;
//                case 1:
//                    return TYPE_INNER_NEWS;
//                case 2:
//                    return TYPE_CAPITAL;
//            }
//
//            return 404;
//        }
//
//        @NonNull
//        @Override
//        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
//            switch (viewType) {
//                case TYPE_HEADER:
//                    Log.e("onCreateViewHolder","TYPE_HEADER");
//                    return new StockHeaderHolder(headerView);
//                case TYPE_TAB:
//                    Log.e("onCreateViewHolder","TYPE_TAB");
//                    return new StockHeaderTabHolder(bottomTabLayout);
//                case TYPE_HEAD_NEWS:
//                    Log.e("onCreateViewHolder","TYPE_HEAD_NEWS");
//                    return new HeadNewsHolder(viewGroup);
//            }
//            return null;
//        }
//
//        @Override
//        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
//            switch (getItemViewType(position)) {
//                case TYPE_HEADER:
//                    Log.e("onBindViewHolder","TYPE_HEADER");
//                    return;
//                case TYPE_TAB:
//                    Log.e("onBindViewHolder","TYPE_TAB");
//                    return;
//                case TYPE_HEAD_NEWS:
//                    Log.e("onBindViewHolder","TYPE_HEAD_NEWS");
//                    ((HeadNewsHolder) viewHolder).setData(headNewsList.get(position - 2));
//                    return;
//            }
//        }
//
//        @Override
//        public int getItemCount() {
//            return 2 + headNewsList.size();
//        }
//    }
//
//
//    public class StockHeaderHolder extends RecyclerView.ViewHolder {
//
//        public StockHeaderHolder(View itemView) {
//            super(itemView);
//
//        }
//    }
//
//    public class StockHeaderTabHolder extends RecyclerView.ViewHolder {
//
////        SlidingTabLayout tabLayout;
////        private ViewPager mViewPager;
////        private EazyFragmentAdpater mAdapter;
////        private ArrayList<EasyFragment> mFragmentList = new ArrayList<>();
//
////        HeadNewsFragment fragment2;
//
//        public StockHeaderTabHolder(View itemView) {
//            super(bottomTabLayout);
//
////            mViewPager = $(R.id.view_pager);
////            tabLayout = $(R.id.tab_layout);
////
////            fragment2 = new HeadNewsFragment();
////
////            mFragmentList.clear();
////            mFragmentList.add(new EasyFragment(fragment2, "头条"));
////
////            mViewPager.setOffscreenPageLimit(mFragmentList.size());
////            mAdapter = new EazyFragmentAdpater(((AppCompatActivity) getContext()).getSupportFragmentManager(), mFragmentList);
////            mViewPager.setAdapter(mAdapter);
////            tabLayout.setViewPager(mViewPager);
//
//        }
//    }


    @SuppressLint("HandlerLeak")
    NetResponseHandler2 mHandler = new NetResponseHandler2() {
        @Override
        public void netConnectLost(int arg0) {
        }

        @Override
        public void timeout(int arg0) {
        }

        @Override
        public void receive(int arg0, boolean b, Intent intent) {
            switch (arg0) {
                case WHAT_GET_STOCK_DETAIL: //获取股票详细报价信息
                    if (intent != null) {
                        EzValue detail = IntentTools.safeGetEzValueFromIntent(
                                intent, "detail");
                        if (detail != null) {
//                            String aa = detail.description();
//                            Log.e(TAG, aa);
                            EzMessage message = detail.getMessage();
                            StockDetailParser parser = new StockDetailParser();
                            detailEntity = parser.parse(stockCode, message);

                            if (detailEntity != null) {
                                singleTextView.setTextContent(detailEntity.getSecuname() + "(" + StockUtils.cutStockCode(detailEntity.getSecucode()) + ")");
                                headerView.setData(detailEntity);
//
                                NetInterface.requestNewInfoList(mHandler, 10000, null, 10, "头条");
//                                fenshiFragment = FenshiFragment.newInstance(detailEntity);
//                                m30fragment = KLineFragment.newInstance(detailEntity,"day");
//                                mFragmentList.clear();
//                                mFragmentList.add(new EasyFragment(fenshiFragment, "分时"));
//                                mFragmentList.add(new EasyFragment(m30fragment, "30分"));
////                                mFragmentList.add(new EasyFragment(m60Fragment, "60分"));
////                                mFragmentList.add(new EasyFragment(dayFragment, "日K"));
////                                mFragmentList.add(new EasyFragment(weekFragment, "周K"));
////                                mFragmentList.add(new EasyFragment(monthFragment, "月K"));
////                                mFragmentList.add(new EasyFragment(minFragment, "分钟"));
//
//                                viewPager.setOffscreenPageLimit(mFragmentList.size());
//                                mAdapter = new EazyFragmentAdpater(getSupportFragmentManager(), mFragmentList);
//                                viewPager.setAdapter(mAdapter);
//                                tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
//                                tabLayout.setupWithViewPager(viewPager);
//
//
//                                FragmentTransaction transaction = fragmentManager.beginTransaction();
//                                if(bottomTabFragment == null){
//                                    bottomTabFragment =  StockBottomTabFragment.newInstance();
//                                    transaction.replace(R.id.stock_detail_bottom,bottomTabFragment);
//                                }
//                                transaction.show(bottomTabFragment);
//                                transaction.commitAllowingStateLoss();
//

//                                if (fenshiFragment == null) {
//                                    fenshiFragment = FenshiFragment.newInstance(detailEntity);
//                                    transaction.add(R.id.container, fenshiFragment);
//                                }
//                                transaction.show(fenshiFragment);
//                                transaction.commitAllowingStateLoss();
//
//                                transaction = fragmentManager.beginTransaction();
//                                if (kLineFragment == null) {
//                                    kLineFragment = KLineFragment.newInstance(detailEntity,"day");
//                                    transaction.add(R.id.container, kLineFragment);
//                                }
//                                transaction.show(kLineFragment);
//                                transaction.commitAllowingStateLoss();
                            }

                        }
                    }
//                case 10000:
//                    if (intent != null) {
//                        EzValue value = IntentTools.safeGetEzValueFromIntent(
//                                intent, "list");
//                        if (value != null) {
//                            EzMessage[] messages = value.getMessages();
//                            if (messages != null) {
//                                EzParser<InfoEntity> parser =  new InfoParser();
//                                for (int i = 0; i < messages.length; i++) {
//                                    InfoEntity entity = parser.invoke(messages[i]);
//                                    headNewsList.add(entity);
//                                }
//                            }
//                            adapter.notifyDataSetChanged();
//                        }
//                    }
//                    break;
            }
        }
    };


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_btn:
                finish();
                break;
            case R.id.search_btn:

                break;
            case R.id.stock_index_quote:
                if (stockPopupWindows == null) {
                    if (detailEntity == null) {
                        return;
                    }
                    stockPopupWindows = new StockPopupWindows(this);
                    stockPopupWindows.setData(detailEntity);
                }
                stockPopupWindows.showPopupWindow(indexQuoteView);
                break;
            case R.id.stock_security_tv:

                break;
        }
    }

}
