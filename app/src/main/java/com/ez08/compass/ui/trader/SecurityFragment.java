package com.ez08.compass.ui.trader;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ez08.compass.CompassApp;
import com.ez08.compass.R;
import com.ez08.compass.net.NetInterface;
import com.ez08.compass.ui.BaseFragment;
import com.ez08.compass.ui.trader.sort.SortModel;
import com.ez08.compass.userauth.AuthUserInfo;
import com.ez08.support.net.NetResponseHandler2;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/10/17.
 */

public class SecurityFragment extends BaseFragment implements View.OnClickListener {

    private List<SortModel> mList;
    private List<SortModel> oGtraderlist;
    private TextView mEditeTv;
    private TextView mAddSecTv;
    private boolean isEdite = false;
    private SecurityAdapter mAdapter;
    private final int WHAT_REQUEST_TRADE_LIST = 1001;
    private String[] traderList;
    private String[] recommendList;
    private String[] mylist;
    private SharedPreferences mySharedPreferences;
    private String url = "http://app.compass.cn/traderlogo/";
    ImageLoader imageLoader = ImageLoader.getInstance();
    DisplayImageOptions options = null;

    private RecyclerView mScrollView;
    private SmartRefreshLayout mListViewFrame;
    private boolean scrollToBottom = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

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

        View view = View.inflate(getActivity(), R.layout.fragment_security, null);
        mEditeTv = (TextView) view.findViewById(R.id.security_edite);
        mScrollView = (RecyclerView) view.findViewById(R.id.security_list);
        mAddSecTv = (TextView) view.findViewById(R.id.tv_add_security);
        view.findViewById(R.id.security_back).setOnClickListener(this);
        mEditeTv.setOnClickListener(this);
        mAddSecTv.setOnClickListener(this);

        mList = new ArrayList<>();
        oGtraderlist = new ArrayList<>();
        mAdapter = new SecurityAdapter();
        mScrollView.setAdapter(mAdapter);
        mScrollView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mListViewFrame = (SmartRefreshLayout) view.findViewById(R.id.stock_charts_lv_frame);

        mListViewFrame.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
//                refreshlayout.finishRefresh(2000/*,false*/);//传入false表示刷新失败
                NetInterface.setMyTrade(mHandler, WHAT_REQUEST_TRADE_LIST, "=", "");
            }
        });

        mListViewFrame.autoRefresh();
        mySharedPreferences = getActivity().getSharedPreferences(AuthUserInfo.getMyCid() + "kefu", Activity.MODE_PRIVATE);
        int size = mySharedPreferences.getInt("Status_size", 0);
        if (size != 0) {
            initData();
        }

        return view;
    }

    private void initData() {
        mList.clear();
        int size = mySharedPreferences.getInt("Status_size", 0);
        String[] lData = new String[size];
        for (int i = 0; i < size; i++) {
            lData[i] = mySharedPreferences.getString("Status_" + i, null);
        }
        List list = new ArrayList();
        for (int i = 0; i < lData.length; i++) {
            String msg = lData[i];
            SortModel sortModel = new SortModel();
            String[] item = msg.split(";");
            sortModel.setName(item[0]);
            sortModel.setUrl(item[1]);
            sortModel.setPackageName(item[2]);
            list.add(sortModel);
        }
        for (int i = 0; i < list.size(); i++) {
            mList.add((SortModel) list.get(i));
        }
        if (mList.size() != 0) {
            mEditeTv.setVisibility(View.VISIBLE);
            mAddSecTv.setVisibility(View.GONE);
            mListViewFrame.setVisibility(View.VISIBLE);
            mAdapter.notifyDataSetChanged();
        } else {
            isEdite = false;
            mEditeTv.setText("编辑");
            mEditeTv.setVisibility(View.GONE);
            mAddSecTv.setVisibility(View.VISIBLE);
            mListViewFrame.setVisibility(View.GONE);
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        SharedPreferences.Editor editor = mySharedPreferences
                .edit();

        editor.putInt("Status_size", mList.size());

        for (int i = 0; i < mList.size(); i++) {
            SortModel model = mList.get(i);
            editor.remove("Status_" + i);
            editor.putString("Status_" + i, model.getName() + ";" + model.getUrl() + ";" + model.getPackageName());
        }
        editor.commit();
    }


    @SuppressLint("HandlerLeak")
    private NetResponseHandler2 mHandler = new NetResponseHandler2() {

        @Override
        public void receive(int i, boolean b, Intent intent) {
            switch (i) {
                case WHAT_REQUEST_TRADE_LIST:
                    mListViewFrame.finishRefresh();
                    mList.clear();
                    oGtraderlist.clear();
                    mylist = intent.getStringArrayExtra("mylist");
                    traderList = intent.getStringArrayExtra("traderlist");
                    recommendList = intent.getStringArrayExtra("recommendlist");
                    if (recommendList != null) {
                        for (int m = 0; m < recommendList.length; m++) {
                            String content = recommendList[m];
                            content = "#" + content;
                            recommendList[m] = content;
                        }
                    }

                    if (traderList == null) {
                        return;
                    }
                    for (int m = 0; m < traderList.length; m++) {
                        SortModel sortModel = new SortModel();
                        String[] item = traderList[m].split(";");
                        sortModel.setName(item[0]);
                        sortModel.setUrl(item[1]);
                        sortModel.setPackageName(item[2]);
                        oGtraderlist.add(sortModel);
                    }
                    if (mylist == null || mylist.length == 0) {
                        isEdite = false;
                        mEditeTv.setText("编辑");
                        mEditeTv.setVisibility(View.GONE);
                        mAddSecTv.setVisibility(View.VISIBLE);
                        mListViewFrame.setVisibility(View.GONE);
                        mAdapter.notifyDataSetChanged();
                        return;
                    }

                    for (int m = 0; m < mylist.length; m++) {
                        SortModel sortModel = new SortModel();
                        sortModel.setName(mylist[m]);
                        for (int j = 0; j < oGtraderlist.size(); j++) {
                            if (oGtraderlist.get(j).getName().equals(mylist[m])) {
                                sortModel.setUrl(oGtraderlist.get(j).getUrl());
                                sortModel.setPackageName(oGtraderlist.get(j).getPackageName());
                                sortModel.setHasAdd(true);
                            }
                        }
                        mList.add(sortModel);
                    }
                    if (mList.size() != 0) {
                        mEditeTv.setVisibility(View.VISIBLE);
                        mAddSecTv.setVisibility(View.GONE);
                        mListViewFrame.setVisibility(View.VISIBLE);
                        mAdapter.notifyDataSetChanged();
                    } else {
                        isEdite = false;
                        mEditeTv.setText("编辑");
                        mEditeTv.setVisibility(View.GONE);
                        mAddSecTv.setVisibility(View.VISIBLE);
                        mListViewFrame.setVisibility(View.GONE);
                        mAdapter.notifyDataSetChanged();
                    }
                    if (scrollToBottom && mList.size() > 0) {
                        mScrollView.scrollToPosition(mList.size());
                        scrollToBottom = false;
                    }
                    break;
            }
        }
    };

    private class SecurityAdapter extends RecyclerView.Adapter {

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            ContentHolder holder = new ContentHolder(View.inflate(getActivity(), R.layout.item_security, null));
            return holder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holderz, final int position) {
            ContentHolder holder = (ContentHolder) holderz;
            if (position == mList.size()) {
                holder.lAdd.setVisibility(View.VISIBLE);
                holder.rly.setVisibility(View.INVISIBLE);
                holder.tv.setVisibility(View.INVISIBLE);
                holder.lImg.setVisibility(View.INVISIBLE);
            } else {
                if (isEdite) {
                    holder.rly.setVisibility(View.VISIBLE);
                } else {
                    holder.rly.setVisibility(View.INVISIBLE);
                }
                String lUrl = url + mList.get(position).getName() + ".png" + "?t=10";
                imageLoader.displayImage(lUrl, holder.lImg, options);

                holder.lAdd.setVisibility(View.INVISIBLE);
                holder.tv.setVisibility(View.VISIBLE);
                holder.lImg.setVisibility(View.VISIBLE);
                holder.tv.setText(mList.get(position).getName());
                holder.lBox.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showDialog(mList.get(position).getName());
                    }
                });
            }
            holder.lGroup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isEdite) {
                        return;
                    }
                    if (position != mList.size()) {
                        String packageName = mList.get(position).getPackageName();
                        String[] packages = packageName.split(",");
                        boolean hasApp = false;
                        if (packages != null && packages.length > 0) {
                            for (int i = 0; i < packages.length; i++) {
                                Intent intentz = getActivity().getPackageManager().getLaunchIntentForPackage(packages[i]);
                                if (!hasApp && intentz != null) {
                                    hasApp = true;
                                    startActivity(intentz);
                                    CompassApp.addStatis(CompassApp.GLOBAL.mgr.TRADE_LIST, "3", mList.get(position).getName(),
                                            System.currentTimeMillis());
                                }
                            }
                        } else {
                            hasApp = false;
                        }
                        if (!hasApp) {
                            showPackageDialog(mList.get(position).getUrl(), mList.get(position).getName());
                        }
                    } else {
                        Intent intent = new Intent(getActivity(), SecurityListActivity.class);
                        intent.putExtra("list", traderList);
                        intent.putExtra("mylist", mylist);
                        intent.putExtra("recommendList", recommendList);
                        startActivityForResult(intent, 1002);
                        CompassApp.addStatis(CompassApp.GLOBAL.mgr.TRADE_LIST, "1", "",
                                System.currentTimeMillis());
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return mList.size() + 1;
        }

        class ContentHolder extends RecyclerView.ViewHolder {

            public RelativeLayout rly;
            public ImageView lBox;
            public TextView tv;
            public LinearLayout lAdd;
            public ImageView lImg;
            public LinearLayout lGroup;

            public ContentHolder(View itemView) {
                super(itemView);
                rly = (RelativeLayout) itemView.findViewById(R.id.rl_cb_selcet);
                lBox = (ImageView) itemView.findViewById(R.id.cb_all_select);
                tv = (TextView) itemView.findViewById(R.id.sec_name_tv);
                lAdd = (LinearLayout) itemView.findViewById(R.id.sec_add_group);
                lImg = (ImageView) itemView.findViewById(R.id.sec_name_img);
                lGroup = (LinearLayout) itemView.findViewById(R.id.sec_item_group);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.security_edite:
                isEdite = !isEdite;
                if (isEdite) {
                    mEditeTv.setText("完成");
                } else {
                    mEditeTv.setText("编辑");
                }
                mAdapter.notifyDataSetChanged();
                break;
            case R.id.tv_add_security:
                Intent intent = new Intent(getActivity(), SecurityListActivity.class);
                intent.putExtra("list", traderList);
                intent.putExtra("mylist", mylist);
                intent.putExtra("recommendList", recommendList);
                startActivityForResult(intent, 1002);
                CompassApp.addStatis(CompassApp.GLOBAL.mgr.TRADE_LIST, "1", "",
                        System.currentTimeMillis());
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 1002) {
            scrollToBottom = true;
            NetInterface.setMyTrade(mHandler, WHAT_REQUEST_TRADE_LIST, "=", "");
        }
    }

    private void showDialog(final String name) {
        AlertDialog.Builder normalDialog =
                new AlertDialog.Builder(getActivity());
        normalDialog.setTitle("提示");
        normalDialog.setMessage("是否删除该券商?");
        normalDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        CompassApp.addStatis(CompassApp.GLOBAL.mgr.TRADE_LIST, "2", name,
                                System.currentTimeMillis());
                        NetInterface.setMyTrade(mHandler, WHAT_REQUEST_TRADE_LIST, "-", name);
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        // 显示
        AlertDialog mlDialog = normalDialog.create();
//        mlDialog.setCancelable(false);
        mlDialog.show();
    }

    private void showPackageDialog(final String name, final String pName) {
        String message = "";
        if (name.contains(".qq.com")) {
            message = "是否前往下载该交易软件?\r\n您将前往应用宝软件市场，请注意选择［普通下载］模式。";
        } else {
            message = "是否前往下载该交易软件?";
        }
        AlertDialog.Builder normalDialog =
                new AlertDialog.Builder(getActivity());
        normalDialog.setTitle("提示");
        normalDialog.setMessage(message);
        normalDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent();
                        intent.setAction("android.intent.action.VIEW");
                        Uri content_url = Uri.parse(name);
                        intent.setData(content_url);
                        startActivity(intent);
                        CompassApp.addStatis(CompassApp.GLOBAL.mgr.TRADE_LIST, "4", pName,
                                System.currentTimeMillis());

                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        // 显示
        AlertDialog mlDialog = normalDialog.create();
//        mlDialog.setCancelable(false);
        mlDialog.show();
    }
}
