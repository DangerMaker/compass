package com.ez08.compass.ui.media;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ez08.compass.CompassApp;
import com.ez08.compass.R;
import com.ez08.compass.database.IMDBHelper;
import com.ez08.compass.entity.ItemStock;
import com.ez08.compass.entity.VideoEntity;
import com.ez08.compass.net.NetInterface;
import com.ez08.compass.tools.UtilTools;
import com.ez08.compass.ui.base.BaseFragment;
import com.ez08.compass.ui.WebActivity;
import com.ez08.compass.auth.AuthUserInfo;
import com.ez08.support.net.EzMessage;
import com.ez08.support.net.NetResponseHandler2;
import com.ez08.support.util.EzValue;
import com.ez08.tools.IntentTools;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.umeng.analytics.MobclickAgent;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class VideoFragment extends BaseFragment {

    private RecyclerView mListView;
    private SmartRefreshLayout mListViewFrame;
    private VideoAdapter adapter;
    private List<VideoEntity> mList = new ArrayList<VideoEntity>();
    private final int WHAT_REFRESH_VIDEO = 1000; //
    private final int WHAT_GET_MORE_VIDEO = 1001; //
    private LinearLayoutManager linearLayoutManager;

    private IMDBHelper mHelper;
    private List<String> mHistoryList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
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
        mHistoryList = new ArrayList<>();

        mHelper = IMDBHelper.getInstance(getActivity());
        View view = View.inflate(getActivity(), R.layout.fragment_video_list,
                null);
        mListView = (RecyclerView) view.findViewById(R.id.my_class_lv);
        mListViewFrame = (SmartRefreshLayout) view.findViewById(R.id.my_class_lv_frame);
        mListViewFrame.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                if (mList.size() < 10) {
                    mListViewFrame.finishRefresh();
                } else {
                    VideoEntity msg = mList.get(mList.size() - 1);
                    NetInterface.requestVideoList(mHandler,
                            WHAT_GET_MORE_VIDEO, msg.getId(), 10);
                }
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                NetInterface.requestVideoList(mHandler, WHAT_REFRESH_VIDEO,
                        null, 10);
            }
        });

        mListViewFrame.autoRefresh();

        adapter = new VideoAdapter();
        linearLayoutManager = new LinearLayoutManager(getActivity());
        mListView.setLayoutManager(linearLayoutManager);
        mListView.setAdapter(adapter);

        Cursor cursor = mHelper.getInfoIdList(AuthUserInfo.getMyCid());
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String infoid = cursor.getString(cursor.getColumnIndex("infoid"));
                mHistoryList.add(infoid);
            }
        }
        return view;
    }

    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("videofragment");
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("videofragment");
    }

    ;

    class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoHolder> {

        @Override
        public VideoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            VideoHolder holder = new VideoHolder(LayoutInflater.from(
                    getActivity()).inflate(R.layout.item_media_video, parent,
                    false));
            return holder;
        }

        @Override
        public void onBindViewHolder(VideoHolder holder, final int position) {
            VideoEntity entity = mList.get(position);
            holder.lTitle.setText(entity.getTitle());
            holder.lLogo.setVisibility(View.VISIBLE);
            SimpleDateFormat formatter = new SimpleDateFormat("MM-dd HH:mm");
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(entity.getTime());
            String timer = formatter.format(calendar.getTime());
            holder.lDate.setText("\n" + timer);
            if (entity.getContent() != null) {
                // holder.lName.setText(entity.getContent());
                if (entity.getContent().length() > 22) {
                    String content = entity.getContent();
                    content = content.substring(0, 22);
                    content = content + "...";
                    holder.lName.setText(content);
                } else {
                    holder.lName.setText(entity.getContent());
                }
            }
            String imageid = entity.getImageid();
            if (!imageid.equals("")) {
                imageLoader.displayImage(imageid, holder.lImage, options);
            }

            if (!hasInfoId(mList.get(position).getId())) {
                holder.lTitle.setTextColor(titleColor);
            } else {
                holder.lTitle.setTextColor(contentColor);
            }

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String url = mList.get(position).getUrl() + "&"
                            + UtilTools.getDate(getActivity());
                    Intent intent = new Intent(getActivity(), WebActivity.class);
                    ItemStock lEntity = new ItemStock();
                    lEntity.setTitle("视频");
                    lEntity.setUrl(url);
                    lEntity.setName(mList.get(position).getTitle());
                    intent.putExtra("type", 1);
                    intent.putExtra("imgId", mList.get(position).getImageid());
                    intent.putExtra("shareurl", mList.get(position).getUrl());
                    intent.putExtra("entity", lEntity);
                    mList.get(position).setType(1);
                    mList.get(position).setCategory("视频");
                    intent.putExtra("InfoEntity", mList.get(position));
                    mHelper.saveInfoId(mList.get(position).getId(), AuthUserInfo.getMyCid());
                    mHistoryList.add(mList.get(position).getId());
                    adapter.notifyDataSetChanged();
                    startActivity(intent);
                }
            });
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
        public int getItemCount() {
            return mList.size();
        }

        class VideoHolder extends RecyclerView.ViewHolder {
            public TextView lTitle;
            public ImageView lImage;
            public TextView lDate;
            public TextView lName;
            public ImageView lLogo;

            public VideoHolder(View convertView) {
                super(convertView);
                lTitle = (TextView) convertView
                        .findViewById(R.id.time_name);
                lImage = (ImageView) convertView
                        .findViewById(R.id.zixun_img);
                lDate = (TextView) convertView
                        .findViewById(R.id.time_date);
                lLogo = (ImageView) convertView
                        .findViewById(R.id.video_logo);
                lName = (TextView) convertView
                        .findViewById(R.id.time_detail);
                if (CompassApp.GLOBAL.THEME_STYLE == 0) {
                    lImage.setImageResource(R.drawable.chaogu);
                } else {
                    lImage.setImageResource(R.drawable.chaogu_night);
                }
            }
        }
    }

    @SuppressLint("HandlerLeak")
    private NetResponseHandler2 mHandler = new NetResponseHandler2() {
        @Override
        public void netConnectLost(int arg0) {
            adapter.notifyDataSetChanged();
        }

        @Override
        public void timeout(int arg0) {
            adapter.notifyDataSetChanged();
        }

        @Override
        public void cancel(int what) {
            super.cancel(what);
            adapter.notifyDataSetChanged();
        }

        @Override
        public void receive(int arg0, boolean arg1, Intent intent) {
            // TODO Auto-generated method stub
            switch (arg0) {
                case WHAT_REFRESH_VIDEO:
                    mList.clear();
                    if (intent != null) {
                        EzValue value = IntentTools.safeGetEzValueFromIntent(
                                intent, "list");
                        if (value != null) {
                            EzMessage[] msges = value.getMessages();
                            if (msges != null) {
                                for (int i = 0; i < msges.length; i++) {
                                    VideoEntity entity = parser(msges[i]);
                                    mList.add(entity);
                                }
                            }
                        }
                        adapter.notifyDataSetChanged();
                    }
                    mListViewFrame.finishRefresh();
                    break;
                case WHAT_GET_MORE_VIDEO:
                    if (intent != null) {
                        EzValue value = IntentTools.safeGetEzValueFromIntent(
                                intent, "list");
                        if (value != null) {
                            EzMessage[] msges = value.getMessages();
                            if (msges != null && msges.length > 0) {
                                for (int i = 0; i < msges.length; i++) {
                                    VideoEntity entity = parser(msges[i]);
                                    mList.add(entity);
                                    Log.e("", entity.getUrl());
                                }
                                adapter.notifyDataSetChanged();
                                mListViewFrame.finishLoadMore();
                            } else {
                                mListViewFrame.finishLoadMore();
                            }
                        } else {
                            mListViewFrame.finishLoadMore();
                        }
                    }
                    break;

                default:
                    break;
            }
        }

    };

    private VideoEntity parser(EzMessage msg) {
        VideoEntity entity = new VideoEntity();
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
        return entity;
    }

    ImageLoader imageLoader = ImageLoader.getInstance();
    DisplayImageOptions options = null;

    public boolean isNetworkAvailble() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();
        if (info == null || !info.isAvailable()) {
            Toast.makeText(getActivity(), "没有可用网络", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
