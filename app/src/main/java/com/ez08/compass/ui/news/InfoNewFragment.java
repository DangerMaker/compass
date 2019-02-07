package com.ez08.compass.ui.news;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ez08.compass.CompassApp;
import com.ez08.compass.R;
import com.ez08.compass.database.IMDBHelper;
import com.ez08.compass.entity.InfoEntity;
import com.ez08.compass.entity.ItemStock;
import com.ez08.compass.entity.NewAdvertEntity;
import com.ez08.compass.net.NetInterface;
import com.ez08.compass.third.UmengShareManager;
import com.ez08.compass.third.share.LinkShare;
import com.ez08.compass.third.share.TextShare;
import com.ez08.compass.tools.AdsManager;
import com.ez08.compass.tools.NetWorkUtils;
import com.ez08.compass.tools.UtilTools;
import com.ez08.compass.ui.BaseFragment;
import com.ez08.compass.ui.WebActivity;
import com.ez08.compass.ui.view.ChildrenViewPager;
import com.ez08.compass.userauth.AuthUserInfo;
import com.ez08.support.net.EzMessage;
import com.ez08.support.net.NetManager;
import com.ez08.support.net.NetResponseHandler2;
import com.ez08.support.util.EzValue;
import com.ez08.tools.IntentTools;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.umeng.analytics.MobclickAgent;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Administrator on 2017/8/23.
 */

public class InfoNewFragment extends BaseFragment implements View.OnClickListener {
    private SmartRefreshLayout mListViewFrame;
    private RecyclerView mListView;
    private RecyclerView.Adapter adapter;
    private List<InfoEntity> mList = new ArrayList<InfoEntity>();
    private List<InfoEntity> mTopList = new ArrayList<InfoEntity>();
    private final int WHAT_REFRESH_VIDEO = 1000; // 下拉刷新
    private final int WHAT_GET_MORE_VIDEO = 1001; // 加载更多
    private final int GET_SHARE_INFO = 1003;
    private String mCategory = "1";
    private String infourl = "";
    private Bitmap mAdvertBmp;

    private LinearLayoutManager linearLayoutManager;
    private String mShareContent = "";
    private boolean pagerScrollStatus = false;
    private IMDBHelper mHelper;
    private List<String> mHistoryList;

    private int currentPosition = 1;
    private int dotPosition = 0;

    private boolean mHasAdvert = false;

    UmengShareManager shareManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub

        if (CompassApp.GLOBAL.THEME_STYLE == 0) {
            options = new DisplayImageOptions.Builder()
                    .showImageForEmptyUri(R.drawable.chaogu)
                    .showImageOnFail(R.drawable.chaogu).cacheInMemory(true)
                    .cacheOnDisk(true).considerExifParams(true).build();
        } else {
            options = new DisplayImageOptions.Builder()
                    .showImageForEmptyUri(R.drawable.chaogu_night)
                    .showImageOnFail(R.drawable.chaogu_night).cacheInMemory(true)
                    .cacheOnDisk(true).considerExifParams(true).build();
        }

        mCategory = getArguments().getString("category");
        View view = View.inflate(getActivity(), R.layout.fragment_news, null);
        mListView = (RecyclerView) view.findViewById(R.id.info_lv);
        mListViewFrame = (SmartRefreshLayout) view.findViewById(R.id.info_lv_frame);
        mListViewFrame.autoRefresh();
        mListViewFrame.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                if(!mList.isEmpty()){
                    InfoEntity msg = mList.get(mList.size() - 1);
                    NetInterface.requestNewInfoList(mHandler, WHAT_GET_MORE_VIDEO, msg.getId(), 10, mCategory);
                }
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                NetInterface.requestNewInfoList(mHandler, WHAT_REFRESH_VIDEO, null, 10, mCategory);
            }
        });

        mListView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {

            @Override
            public void onScrollChanged() {
                int aas = linearLayoutManager.findFirstCompletelyVisibleItemPosition();
                if (aas == 0 && pagerScrollStatus) {
                    pagerScrollStatus = false;
                    mListView.scrollBy(0, 1);
                }
            }
        });
        adapter = new MyInfoAdapter();
        linearLayoutManager = new LinearLayoutManager(getActivity());
        mListView.setLayoutManager(linearLayoutManager);
        mListView.setAdapter(adapter);

        initAdvert();
        shareManager = new UmengShareManager(getActivity());

        return view;
    }

    public void setDBHelper(IMDBHelper mHelper, List<String> mHistoryList) {
        this.mHistoryList = mHistoryList;
        this.mHelper = mHelper;
    }

    private void initAdvert() {
        mAdvertBmp = null;
        NewAdvertEntity entity = AdsManager.getInstance(getActivity()).getAdsAtNews();
        if (entity != null) {
            File tempFile = UtilTools.buildFile(getContext().getFilesDir(), AuthUserInfo.getMyCid() + "_newsAd.png");
            mAdvertBmp = UtilTools.getLoacalBitmap(tempFile);
            infourl = entity.getInfourl();
        }


        if (mAdvertBmp != null && !TextUtils.isEmpty(infourl)) {
            mHasAdvert = true;
            if (mCategory.equals("头条")) {
                CompassApp.addStatis(CompassApp.GLOBAL.mgr.ADVERT_INFO, "0", "", System.currentTimeMillis());
            }
        }
        adapter.notifyDataSetChanged();
    }

    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("infofragment");
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("infofragment");
    }

    private final int TYPE_INFO = 1;
    private final int TYPE_HEADER = 0;
    private final int TYPE_IMG_RIGHT = 2;  //图片在右边
    private final int TYPE_COMPASS = 3;   //指南针
    private final int TYPE_NO_IMG = 4;    //无图片
    private final int TYPE_SHARE = 5; //文字带分享

    class MyInfoAdapter extends RecyclerView.Adapter {

        class AdvertHolder extends RecyclerView.ViewHolder {

            public ImageView img;

            public AdvertHolder(View itemView) {
                super(itemView);
                img = (ImageView) itemView.findViewById(R.id.info_advert_img);
            }
        }

        public class TopLineHolder extends RecyclerView.ViewHolder {

            public ChildrenViewPager mViewPager;
            private LinearLayout linar;
            private TextView lPagetTitle;

            public TopLineHolder(View itemView) {
                super(itemView);
                mViewPager = (ChildrenViewPager) itemView.findViewById(R.id.style_pager);
                linar = (LinearLayout) itemView.findViewById(R.id.style_pager_linear);
                lPagetTitle = (TextView) itemView.findViewById(R.id.style_paget_title);
            }
        }

        public class InfoHolder extends RecyclerView.ViewHolder {
            public TextView lTitle;
            public TextView lDate;
            public TextView lName;
            public ImageView lImg;
//            public View lLine;

            public InfoHolder(View convertView) {
                super(convertView);
                lTitle = (TextView) convertView
                        .findViewById(R.id.time_name);
                lImg = (ImageView) convertView
                        .findViewById(R.id.zixun_img);
                lDate = (TextView) convertView
                        .findViewById(R.id.time_date);
                lName = (TextView) convertView
                        .findViewById(R.id.time_detail);
                if (CompassApp.GLOBAL.THEME_STYLE == 0) {
                    lImg.setImageResource(R.drawable.chaogu);
                } else {
                    lImg.setImageResource(R.drawable.chaogu_night);
                }
            }
        }

        public class CompassHolder extends RecyclerView.ViewHolder {
            public TextView lTitle;
            public TextView lDate;
            public TextView lCategory;
            public TextView lName;

            public CompassHolder(View convertView) {
                super(convertView);
                lTitle = (TextView) convertView
                        .findViewById(R.id.time_name);
                lDate = (TextView) convertView
                        .findViewById(R.id.time_date);
                lCategory = (TextView) convertView
                        .findViewById(R.id.compass_style);
                lName = (TextView) convertView
                        .findViewById(R.id.time_detail);
            }
        }

        public class NoImgHolder extends RecyclerView.ViewHolder {
            public TextView lTitle;
            public TextView lDate;
            public TextView lName;

            public NoImgHolder(View convertView) {
                super(convertView);
                lTitle = (TextView) convertView
                        .findViewById(R.id.time_name);
                lDate = (TextView) convertView
                        .findViewById(R.id.time_date);
                lName = (TextView) convertView
                        .findViewById(R.id.time_detail);
            }
        }

        public class ShareHolder extends RecyclerView.ViewHolder {
            public TextView lTitle;
            public ImageView lImg;
            public TextView lDate;
            public TextView lTime;

            public ShareHolder(View convertView) {
                super(convertView);
                lTitle = (TextView) convertView
                        .findViewById(R.id.time_detail);
                lImg = (ImageView) convertView.findViewById(R.id.style_info_share);
                lDate = (TextView) convertView.findViewById(R.id.style_share_date);
                lTime = (TextView) convertView.findViewById(R.id.style_share_time);
            }
        }

        @Override
        public int getItemViewType(int position) {
            InfoEntity info = mList.get(position);
            if (mCategory.equals("头条") || mCategory.equals("操盘计划")) {
                if (position == 0) {
                    return TYPE_HEADER;
                }
                if (TextUtils.isEmpty(info.getImageid())) {
                    return TYPE_NO_IMG;
                } else {
                    return TYPE_IMG_RIGHT;
                }
            } else if (mCategory.equals("内参")) {
                return TYPE_COMPASS;
            } else if (mCategory.equals("滚动")) {
                return TYPE_SHARE;
            } else if (mCategory.equals("晨会")) {
                if (TextUtils.isEmpty(info.getImageid())) {
                    return TYPE_NO_IMG;
                } else {
                    return TYPE_IMG_RIGHT;
                }
            } else if (mCategory.equals("行业")) {
                if (TextUtils.isEmpty(info.getImageid())) {
                    return TYPE_NO_IMG;
                } else {
                    return TYPE_IMG_RIGHT;
                }
            } else if (mCategory.equals("宏观")) {
                if (TextUtils.isEmpty(info.getImageid())) {
                    return TYPE_NO_IMG;
                } else {
                    return TYPE_IMG_RIGHT;
                }
            } else if (mCategory.equals("公司")) {
                if (TextUtils.isEmpty(info.getImageid())) {
                    return TYPE_NO_IMG;
                } else {
                    return TYPE_IMG_RIGHT;
                }
            } else if (mCategory.equals("全球")) {
                if (TextUtils.isEmpty(info.getImageid())) {
                    return TYPE_NO_IMG;
                } else {
                    return TYPE_IMG_RIGHT;
                }
            } else if (mCategory.equals("热文")) {
                if (TextUtils.isEmpty(info.getImageid())) {
                    return TYPE_NO_IMG;
                } else {
                    return TYPE_IMG_RIGHT;
                }
            } else if (mCategory.equals("港股")) {
                if (TextUtils.isEmpty(info.getImageid())) {
                    return TYPE_NO_IMG;
                } else {
                    return TYPE_IMG_RIGHT;
                }
            } else if (mCategory.equals("新三板")) {
                if (TextUtils.isEmpty(info.getImageid())) {
                    return TYPE_NO_IMG;
                } else {
                    return TYPE_IMG_RIGHT;
                }
            } else if (mCategory.equals("新股")) {
                if (TextUtils.isEmpty(info.getImageid())) {
                    return TYPE_NO_IMG;
                } else {
                    return TYPE_IMG_RIGHT;
                }
            } else if (mCategory.equals("研究")) {
                if (TextUtils.isEmpty(info.getImageid())) {
                    return TYPE_NO_IMG;
                } else {
                    return TYPE_IMG_RIGHT;
                }
            } else if (mCategory.equals("财经")) {
                if (TextUtils.isEmpty(info.getImageid())) {
                    return TYPE_NO_IMG;
                } else {
                    return TYPE_IMG_RIGHT;
                }
            } else if (mCategory.equals("期货")) {
                if (TextUtils.isEmpty(info.getImageid())) {
                    return TYPE_NO_IMG;
                } else {
                    return TYPE_IMG_RIGHT;
                }
//            } else if (mCategory.equals("外汇")) {
//                if (TextUtils.isEmpty(info.getImageid())) {
//                    return TYPE_NO_IMG;
//                } else {
//                    return TYPE_IMG_RIGHT;
//                }
            } else if (mCategory.equals("期权")) {
                if (TextUtils.isEmpty(info.getImageid())) {
                    return TYPE_NO_IMG;
                } else {
                    return TYPE_IMG_RIGHT;
                }
            }
            return TYPE_IMG_RIGHT;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == TYPE_HEADER) {
                MyInfoAdapter.TopLineHolder advert = new MyInfoAdapter.TopLineHolder(LayoutInflater.from(
                        getActivity()).inflate(R.layout.style_viewpager_item, parent,
                        false));
                return advert;
            } else if (viewType == TYPE_INFO) {
                MyInfoAdapter.InfoHolder holder = new MyInfoAdapter.InfoHolder(LayoutInflater.from(
                        getActivity()).inflate(R.layout.list_style_right_item, parent,
                        false));
                return holder;
            } else if (viewType == TYPE_IMG_RIGHT) {
                MyInfoAdapter.InfoHolder holder = new MyInfoAdapter.InfoHolder(LayoutInflater.from(
                        getActivity()).inflate(R.layout.list_style_right_item, parent,
                        false));
                return holder;
            } else if (viewType == TYPE_COMPASS) {
                MyInfoAdapter.CompassHolder holder = new MyInfoAdapter.CompassHolder(LayoutInflater.from(
                        getActivity()).inflate(R.layout.compass_style_item, parent,
                        false));
                return holder;
            } else if (viewType == TYPE_NO_IMG) {
                MyInfoAdapter.NoImgHolder holder = new MyInfoAdapter.NoImgHolder(LayoutInflater.from(
                        getActivity()).inflate(R.layout.style_content_item, parent,
                        false));
                return holder;
            } else if (viewType == TYPE_SHARE) {
                MyInfoAdapter.ShareHolder holder = new MyInfoAdapter.ShareHolder(LayoutInflater.from(
                        getActivity()).inflate(R.layout.style_share_new_layout, parent,
                        false));
                return holder;
            } else {
                MyInfoAdapter.InfoHolder holder = new MyInfoAdapter.InfoHolder(LayoutInflater.from(
                        getActivity()).inflate(R.layout.list_style_item, parent,
                        false));
                return holder;
            }
        }

        private boolean hasInfoId(String id) {
            for (int i = 0; i < mHistoryList.size(); i++) {
                if (mHistoryList.get(i).equals(id)) {
                    return true;
                }
            }
            return false;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder myholder, final int position) {
            if (myholder instanceof MyInfoAdapter.TopLineHolder) {
                final MyInfoAdapter.TopLineHolder advertHolder = (MyInfoAdapter.TopLineHolder) myholder;
                RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) advertHolder.linar.getLayoutParams();
                params.width = CompassApp.GLOBAL.SCREEN_W;
                params.height = CompassApp.GLOBAL.SCREEN_W / 2;
                advertHolder.linar.setLayoutParams(params);


                if (mTopList.size() > 0) {
                    advertHolder.lPagetTitle.setText(mTopList.get(0).getTitle());
                }

                final ArrayList<View> images = new ArrayList<View>();
                for (int i = 0; i < mTopList.size() + 2; i++) {
                    InfoEntity info = null;
                    if (i == 0) {
                        info = mTopList.get(mTopList.size() - 1);
                    } else if (i == mTopList.size() + 1) {
                        info = mTopList.get(0);
                    } else {
                        info = mTopList.get(i - 1);
                    }
                    View itemV = View.inflate(getActivity(), R.layout.info_top_line_view, null);
                    ImageView iv = (ImageView) itemV.findViewById(R.id.info_advert_img);
                    RelativeLayout.LayoutParams imgParams = (RelativeLayout.LayoutParams) iv.getLayoutParams();
                    imgParams.width = CompassApp.GLOBAL.SCREEN_W;
                    imgParams.height = CompassApp.GLOBAL.SCREEN_W / 2;
                    iv.setLayoutParams(imgParams);
                    images.add(itemV);
//                    InfoEntity info = mTopList.get(i);
                    String imageid = info.getImageid();
                    if (!TextUtils.isEmpty(info.getTopDes()) && info.getTopDes().equals("广告")) {
                        iv.setImageBitmap(mAdvertBmp);


                    } else if (!imageid.equals("")) {
                        imageLoader.displayImage(imageid, iv, options);
                    }

                    final InfoEntity finalInfo = info;
                    itemV.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (!TextUtils.isEmpty(finalInfo.getTopDes()) && finalInfo.getTopDes().equals("广告")) {
                                if (!TextUtils.isEmpty(AuthUserInfo.getMyCid()) && !TextUtils.isEmpty(AuthUserInfo.getMyToken())) {
                                    infourl = infourl + "?personid=" + AuthUserInfo.getMyCid() + "&skey=" + AuthUserInfo.getMyToken();
                                    ;
                                    ItemStock entity = new ItemStock();
                                    entity.setUrl(infourl);
                                    Intent intentWeb = new Intent(getActivity(), WebActivity.class);
                                    intentWeb.putExtra("entity", entity);
                                    intentWeb.putExtra("ifFromAd", true);
                                    startActivity(intentWeb);
                                    CompassApp.addStatis(CompassApp.GLOBAL.mgr.ADVERT_INFO, "1", "", System.currentTimeMillis());
                                }
                                return;
                            }

                            String url;
                            if (finalInfo.getUrl().contains("?")) {
                                url = finalInfo.getUrl() + "&"
                                        + UtilTools.getDate(getActivity());
                            } else {
                                url = finalInfo.getUrl() + "?"
                                        + UtilTools.getDate(getActivity());
                            }


                            Intent intent = new Intent(getActivity(), WebActivity.class);
                            ItemStock lEntity = new ItemStock();

                            lEntity.setTitle(finalInfo.getCategory());
                            lEntity.setUrl(url);
//                        lEntity.setUrl("http://pc.znz888.cn/aweb/download/?plant=pc&personid=194784380&company=compass&os=android&osver=HUAWEIGRA-UL00,6.0&personid=194784380&skey=znzapp6ab4c0155a3c23ebeec32c6810c3f6e8&aid=zhinantong_appAAAJir&chid=20000&ver=2.3.4&pmode=HUAWEIGRA-UL00&imei=866696025526961&style=0&company=compass&os=android&osver=HUAWEIGRA-UL00,6.0&personid=194784380&skey=znzapp6ab4c0155a3c23ebeec32c6810c3f6e8&aid=zhinantong_appAAAJir&chid=20000&ver=2.3.4&pmode=HUAWEIGRA-UL00&imei=866696025526961&style=0");
                            lEntity.setName(finalInfo.getTitle());
                            intent.putExtra("type", 0);
                            intent.putExtra("entity", lEntity);
                            intent.putExtra("imgId", finalInfo.getImageid());
                            intent.putExtra("shareurl", finalInfo.getUrl());
                            intent.putExtra("InfoEntity", finalInfo);

                            mHelper.saveInfoId(finalInfo.getId(), AuthUserInfo.getMyCid());
                            mHistoryList.add(finalInfo.getId());
                            adapter.notifyDataSetChanged();
                            getActivity().startActivity(intent);
                        }
                    });
                }
                advertHolder.mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//                    int oldPosition = 0;


                    @Override
                    public void onPageSelected(int position) {

                        if (position == 0) {    //判断当切换到第0个页面时把currentPosition设置为images.length,即倒数第二个位置，小圆点位置为length-1
                            currentPosition = mTopList.size();
                            dotPosition = mTopList.size() - 1;
                        } else if (position == mTopList.size() + 1) {    //当切换到最后一个页面时currentPosition设置为第一个位置，小圆点位置为0
                            currentPosition = 1;
                            dotPosition = 0;
                        } else {
                            currentPosition = position;
                            dotPosition = position - 1;
                        }
                        advertHolder.lPagetTitle.setText(mTopList.get(dotPosition).getTitle());
                    }

                    @Override
                    public void onPageScrolled(int arg0, float arg1, int arg2) {
                        // TODO Auto-generated method stub
                        if (arg1 > 0) {
                            pagerScrollStatus = true;
                        }
                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {
                        // TODO Auto-generated method stub
                        if (state == ViewPager.SCROLL_STATE_IDLE) {
                            advertHolder.mViewPager.setCurrentItem(currentPosition, false);
                        }
                    }
                });
                advertHolder.mViewPager.setAdapter(new PagerAdapter() {
                    @Override
                    public int getCount() {
                        return images.size();
                    }

                    @Override
                    public boolean isViewFromObject(View arg0, Object arg1) {
                        return arg0 == arg1;
                    }

                    @Override
                    public void destroyItem(ViewGroup viewGroup, int position, Object object) {
                        viewGroup.removeView(images.get(position));
                    }

                    @Override
                    public Object instantiateItem(ViewGroup viewGroup, int position) {

                        viewGroup.addView(images.get(position));
                        return images.get(position);
                    }
                });
                advertHolder.mViewPager.setCurrentItem(currentPosition);
            } else if (myholder instanceof MyInfoAdapter.InfoHolder) {
                MyInfoAdapter.InfoHolder holder = (MyInfoAdapter.InfoHolder) myholder;
                final InfoEntity info = mList.get(position);
                holder.lTitle.setText(info.getTitle());
                if (!hasInfoId(mList.get(position).getId())) {
                    holder.lTitle.setTextColor(titleColor);
                } else {
                    holder.lTitle.setTextColor(contentColor);
                }

                if (info.getContent() != null) {
                    holder.lName.setText(info.getContent());
                }
                //holder.lLogo.setVisibility(View.VISIBLE);
                SimpleDateFormat formatter = new SimpleDateFormat("MM-dd HH:mm");
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(info.getTime());
                String timer = formatter.format(calendar.getTime());
                holder.lDate.setText(timer);

                String imageid = info.getImageid();
                if (!imageid.equals("")) {
                    imageLoader.displayImage(imageid, holder.lImg, options);
                }

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String url;
                        if (mList.get(position).getUrl().contains("?")) {
                            url = mList.get(position).getUrl() + "&"
                                    + UtilTools.getDate(getActivity());
                        } else {
                            url = mList.get(position).getUrl() + "?"
                                    + UtilTools.getDate(getActivity());
                        }

                        Intent intent = new Intent(getActivity(), WebActivity.class);
                        ItemStock lEntity = new ItemStock();

                        lEntity.setTitle(mList.get(position).getCategory());
                        lEntity.setUrl(url);
//                        lEntity.setUrl("http://pc.znz888.cn/aweb/download/?plant=pc&personid=194784380&company=compass&os=android&osver=HUAWEIGRA-UL00,6.0&personid=194784380&skey=znzapp6ab4c0155a3c23ebeec32c6810c3f6e8&aid=zhinantong_appAAAJir&chid=20000&ver=2.3.4&pmode=HUAWEIGRA-UL00&imei=866696025526961&style=0&company=compass&os=android&osver=HUAWEIGRA-UL00,6.0&personid=194784380&skey=znzapp6ab4c0155a3c23ebeec32c6810c3f6e8&aid=zhinantong_appAAAJir&chid=20000&ver=2.3.4&pmode=HUAWEIGRA-UL00&imei=866696025526961&style=0");
                        lEntity.setName(mList.get(position).getTitle());
                        intent.putExtra("type", 0);
                        intent.putExtra("entity", lEntity);
                        intent.putExtra("imgId", mList.get(position).getImageid());
                        intent.putExtra("shareurl", mList.get(position).getUrl());
                        intent.putExtra("InfoEntity", mList.get(position));

                        adapter.notifyDataSetChanged();
                        mHelper.saveInfoId(mList.get(position).getId(), AuthUserInfo.getMyCid());
                        mHistoryList.add(mList.get(position).getId());
                        getActivity().startActivity(intent);
                    }
                });
            } else if (myholder instanceof MyInfoAdapter.CompassHolder) {
                MyInfoAdapter.CompassHolder holder = (MyInfoAdapter.CompassHolder) myholder;
                final InfoEntity info = mList.get(position);
                holder.lTitle.setText(info.getTitle());
                if (!hasInfoId(mList.get(position).getId())) {
                    holder.lTitle.setTextColor(titleColor);
                } else {
                    holder.lTitle.setTextColor(contentColor);
                }

                if (info.getContent() != null) {
                    holder.lName.setText(info.getContent());
                }
                //holder.lLogo.setVisibility(View.VISIBLE);
                SimpleDateFormat formatter = new SimpleDateFormat("MM-dd HH:mm");
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(info.getTime());
                String timer = formatter.format(calendar.getTime());
                holder.lDate.setText(timer);
                holder.lCategory.setText(info.getCategory());

                holder.lCategory.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        Intent intent = new Intent(getActivity(), InfoDetailActivity.class);
//                        intent.putExtra("category", info.getCategory());
//                        startActivity(intent);
                    }
                });

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String url;
                        if (mList.get(position).getUrl().contains("?")) {
                            url = mList.get(position).getUrl() + "&"
                                    + UtilTools.getDate(getActivity());
                        } else {
                            url = mList.get(position).getUrl() + "?"
                                    + UtilTools.getDate(getActivity());
                        }
                        Intent intent = new Intent(getActivity(), WebActivity.class);
                        ItemStock lEntity = new ItemStock();

                        lEntity.setTitle(mList.get(position).getCategory());
                        lEntity.setUrl(url);
//                        lEntity.setUrl("http://pc.znz888.cn/aweb/download/?plant=pc&personid=194784380&company=compass&os=android&osver=HUAWEIGRA-UL00,6.0&personid=194784380&skey=znzapp6ab4c0155a3c23ebeec32c6810c3f6e8&aid=zhinantong_appAAAJir&chid=20000&ver=2.3.4&pmode=HUAWEIGRA-UL00&imei=866696025526961&style=0&company=compass&os=android&osver=HUAWEIGRA-UL00,6.0&personid=194784380&skey=znzapp6ab4c0155a3c23ebeec32c6810c3f6e8&aid=zhinantong_appAAAJir&chid=20000&ver=2.3.4&pmode=HUAWEIGRA-UL00&imei=866696025526961&style=0");
                        lEntity.setName(mList.get(position).getTitle());
                        intent.putExtra("type", 0);
                        intent.putExtra("entity", lEntity);
                        intent.putExtra("imgId", mList.get(position).getImageid());
                        intent.putExtra("shareurl", mList.get(position).getUrl());
                        intent.putExtra("InfoEntity", mList.get(position));
                        adapter.notifyDataSetChanged();
                        mHelper.saveInfoId(mList.get(position).getId(), AuthUserInfo.getMyCid());
                        mHistoryList.add(mList.get(position).getId());
                        getActivity().startActivity(intent);
                    }
                });
            } else if (myholder instanceof MyInfoAdapter.NoImgHolder) {
                MyInfoAdapter.NoImgHolder holder = (MyInfoAdapter.NoImgHolder) myholder;
                InfoEntity info = mList.get(position);
                holder.lTitle.setText(info.getTitle());

                if (!hasInfoId(mList.get(position).getId())) {
                    holder.lTitle.setTextColor(titleColor);
                } else {
                    holder.lTitle.setTextColor(contentColor);
                }
                if (info.getContent() != null) {
                    holder.lName.setText(info.getContent());
                }
                //holder.lLogo.setVisibility(View.VISIBLE);
                SimpleDateFormat formatter = new SimpleDateFormat("MM-dd HH:mm");
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(info.getTime());
                String timer = formatter.format(calendar.getTime());
                holder.lDate.setText(timer);

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String url;
                        if (mList.get(position).getUrl().contains("?")) {
                            url = mList.get(position).getUrl() + "&"
                                    + UtilTools.getDate(getActivity());
                        } else {
                            url = mList.get(position).getUrl() + "?"
                                    + UtilTools.getDate(getActivity());
                        }
                        Intent intent = new Intent(getActivity(), WebActivity.class);
                        ItemStock lEntity = new ItemStock();

                        lEntity.setTitle(mList.get(position).getCategory());
                        lEntity.setUrl(url);
//                        lEntity.setUrl("http://pc.znz888.cn/aweb/download/?plant=pc&personid=194784380&company=compass&os=android&osver=HUAWEIGRA-UL00,6.0&personid=194784380&skey=znzapp6ab4c0155a3c23ebeec32c6810c3f6e8&aid=zhinantong_appAAAJir&chid=20000&ver=2.3.4&pmode=HUAWEIGRA-UL00&imei=866696025526961&style=0&company=compass&os=android&osver=HUAWEIGRA-UL00,6.0&personid=194784380&skey=znzapp6ab4c0155a3c23ebeec32c6810c3f6e8&aid=zhinantong_appAAAJir&chid=20000&ver=2.3.4&pmode=HUAWEIGRA-UL00&imei=866696025526961&style=0");
                        lEntity.setName(mList.get(position).getTitle());
                        intent.putExtra("type", 0);
                        intent.putExtra("entity", lEntity);
                        intent.putExtra("imgId", mList.get(position).getImageid());
                        intent.putExtra("shareurl", mList.get(position).getUrl());
                        intent.putExtra("InfoEntity", mList.get(position));
                        adapter.notifyDataSetChanged();
                        mHelper.saveInfoId(mList.get(position).getId(), AuthUserInfo.getMyCid());
                        mHistoryList.add(mList.get(position).getId());
                        getActivity().startActivity(intent);
                    }
                });
            } else if (myholder instanceof MyInfoAdapter.ShareHolder) {

                final MyInfoAdapter.ShareHolder holder = (MyInfoAdapter.ShareHolder) myholder;
                final InfoEntity info = mList.get(position);

                SimpleDateFormat formatter = new SimpleDateFormat("MM-dd HH:mm");
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(info.getTime());
                String timer = formatter.format(calendar.getTime());
                String timers[] = timer.split(" ");
                if (timers != null && timers.length > 1) {
                    if (position == 0) {
                        holder.lDate.setText(timers[0]);
                        holder.lTime.setText(timers[1]);
                        holder.lDate.setTextColor(redColor);
                        holder.lTime.setTextColor(redColor);
                    } else {
                        holder.lDate.setText(timers[1]);
                        holder.lTime.setText("");
                        holder.lDate.setTextColor(contentColor);
                        holder.lTime.setTextColor(contentColor);
                    }

                } else {
                    holder.lDate.setText("");
                    holder.lTime.setText("");
                    holder.lDate.setTextColor(contentColor);
                    holder.lTime.setTextColor(contentColor);
                }

                String title = info.getTitle();

                String content = info.getContent();
                String detail = title + content;
                holder.lTitle.setText(title);
                if (!hasInfoId(mList.get(position).getId())) {
                    holder.lTitle.setTextColor(titleColor);
                } else {
                    holder.lTitle.setTextColor(contentColor);
                }
                if (info.ischeck()) {
                    holder.lTitle.setMaxLines(100);
                    holder.lImg.setVisibility(View.VISIBLE);
                } else {
                    holder.lTitle.setMaxLines(3);
                    holder.lImg.setVisibility(View.GONE);
                }

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        boolean check = info.ischeck();
                        for (int i = 0; i < mList.size(); i++) {
                            mList.get(i).setIscheck(false);
                        }
                        info.setIscheck(!check);

                        mShareContent = holder.lTitle.getText().toString();
                        mHelper.saveInfoId(mList.get(position).getId(), AuthUserInfo.getMyCid());
                        mHistoryList.add(mList.get(position).getId());
                        adapter.notifyDataSetChanged();
                    }
                });

                holder.lImg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (NetWorkUtils.isNetworkAvailable(getActivity())) {
                            NetInterface.getShareInfoList(mHandler, GET_SHARE_INFO);
                        } else {
                            Toast.makeText(getActivity(), "当前网络不可用，请联网后分享", Toast.LENGTH_LONG).show();
                        }
                    }
                });

            }

        }

        @Override
        public int getItemCount() {
            return mList.size();
        }
    }


    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub

    }

    @SuppressLint("HandlerLeak")
    private NetResponseHandler2 mHandler = new NetResponseHandler2() {
        @Override
        public void netConnectLost(int arg0) {
            mListViewFrame.finishRefresh();
        }

        @Override
        public void timeout(int arg0) {
            mListViewFrame.finishRefresh();
        }

        @Override
        public void receive(int arg0, boolean arg1, Intent intent) {
            // TODO Auto-generated method stub
            String msg = intent.getStringExtra("msg");
            switch (arg0) {
                case WHAT_REFRESH_VIDEO:
                    mList.clear();// 清空
                    mTopList.clear();
                    if (intent != null) {
                        EzValue value = IntentTools.safeGetEzValueFromIntent(
                                intent, "list");
                        if (value != null) {
                            EzMessage[] msges = value.getMessages();
                            if (msges != null) {
                                for (int i = 0; i < msges.length; i++) {
                                    String aa = msges[i].description();
                                    InfoEntity entity = parser(msges[i]);
                                    mList.add(entity);
                                }
                                if (mCategory.equals("头条") || mCategory.equals("操盘计划")) {
                                    if (mHasAdvert) {
                                        InfoEntity entity = new InfoEntity();
                                        entity.setCategory("广告");
                                        entity.setContent("广告");
                                        entity.setTopDes("广告");
                                        entity.setBmp(mAdvertBmp);
                                        entity.setUrl(infourl);
                                        mTopList.add(0, entity);
                                    }
                                    if (mTopList.size() < 1) {
                                        mTopList.add(mList.get(0));
                                        mList.remove(0);
                                    }

                                    mList.add(0, null);
                                }
                            }
                        }
                        adapter.notifyDataSetChanged();
                    }
                    mListViewFrame.finishRefresh();
                    mListView.scrollBy(0, 1);
                    break;
                case WHAT_GET_MORE_VIDEO:
                    if (intent != null) {
                        EzValue value = IntentTools.safeGetEzValueFromIntent(
                                intent, "list");
                        if (value != null) {
                            EzMessage[] msges = value.getMessages();
                            if (msges != null && msges.length > 0) {
                                for (int i = 0; i < msges.length; i++) {
                                    InfoEntity entity = parser(msges[i]);
                                    mList.add(entity);
                                }

                                mListViewFrame.finishLoadMore();
                                adapter.notifyDataSetChanged();
                                mListView.scrollBy(0, UtilTools.dip2px(getActivity(), 25));
                            } else {
                                mListViewFrame.finishLoadMore();
                            }
                        } else {
                            mListViewFrame.finishLoadMore();
                        }
                    }
                    break;
                case GET_SHARE_INFO:
                    if (intent != null) {
                        EzValue value = IntentTools.safeGetEzValueFromIntent(
                                intent, "list");
                        if (value != null) {
                            EzMessage[] msges = value.getMessages();
                            if (msges != null) {
                                for (int i = 0; i < msges.length; i++) {
                                    EzMessage ezMessage = msges[0];
                                    if (ezMessage != null) {
                                        int id = ezMessage.getKVData("id").getInt32();
                                        String url = ezMessage.getKVData("url").getStringWithDefault("");
                                        String title = ezMessage.getKVData("title").getStringWithDefault("");
                                        shareManager.share(new LinkShare(title, "", mShareContent, url, String.valueOf(id)), null);
                                    }
                                }
                            } else {
                                shareManager.share(new TextShare(mShareContent), null);
                            }
                        } else {
                            shareManager.share(new TextShare(mShareContent), null);
                        }
                    }
                    break;
                default:
                    break;
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

    ImageLoader imageLoader = ImageLoader.getInstance();
    DisplayImageOptions options = null;


    public void setCategory(String category) {
        if (!mCategory.equals(category)) {
            mCategory = category;
            if (mListViewFrame != null) {
                mListViewFrame.autoRefresh();
            }
        }
        mCategory = category;
    }

    public String getCategory() {
        return mCategory;
    }
}
