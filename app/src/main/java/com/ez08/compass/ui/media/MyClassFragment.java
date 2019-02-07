package com.ez08.compass.ui.media;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.TypedArray;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputFilter;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ez08.compass.CompassApp;
import com.ez08.compass.R;
import com.ez08.compass.entity.ClassEntity;
import com.ez08.compass.entity.RoomEntity;
import com.ez08.compass.net.NetInterface;
import com.ez08.compass.tools.MD5;
import com.ez08.compass.ui.BaseFragment;
import com.ez08.compass.ui.media.banner.ClassLiuHeader;
import com.ez08.support.net.EzMessage;
import com.ez08.support.net.NetResponseHandler2;
import com.ez08.support.util.EzValue;
import com.ez08.tools.IntentTools;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.umeng.analytics.MobclickAgent;

import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import static android.view.View.inflate;

public class MyClassFragment extends BaseFragment {
    private boolean hsLivingHeader = false;
    private RecyclerView mListView;
    private SmartRefreshLayout mListViewFrame;
    private MyClassAdapter adapter;
    private ClassLiuHeader mHeader;
    private List<ClassEntity> mList = new ArrayList<ClassEntity>();
    private List<RoomEntity> mRoomList = new ArrayList<RoomEntity>();
    private List<RoomEntity> mAllRoomList = new ArrayList<RoomEntity>();
    private boolean mIsFirstLoad = true;
    private final int WHAT_LIVE_REFRESH = 1001;
    private static final int WHAT_ADD_LIVING = 1007;

    AlertDialog dialog = null;
    private String mRoomId = "";
    private String mRoonName = "";
    public long mNextTime = 0; // 下个直播开始时间

    private static final int TYPE_CLASS = 0;
    private static final int TYPE_HEADER = 1;
    private static final int TYPE_LIVING = 2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        CompassApp.addStatis(CompassApp.GLOBAL.mgr.LIVE_ROOMLIST, "0", "",
                System.currentTimeMillis());
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

        View view = inflate(getActivity(), R.layout.fragment_my_class,
                null);
        mListView = (RecyclerView) view.findViewById(R.id.my_class_lv);
        mListViewFrame = (SmartRefreshLayout) view.findViewById(R.id.my_class_lv_frame);
        mListViewFrame.autoRefresh();
        mListViewFrame.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                NetInterface.requestLiveList(mHandler, WHAT_LIVE_REFRESH);
            }
        });

        mHeader = (ClassLiuHeader) inflate(getActivity(),
                R.layout.class_liu_header, null);

        adapter = new MyClassAdapter();
        mListView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mListView.setAdapter(adapter);
        return view;
    }

    class MyClassAdapter extends RecyclerView.Adapter {

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == TYPE_CLASS) {
                ClassHolder holder = new ClassHolder(LayoutInflater.from(
                        getActivity()).inflate(R.layout.list_item_style_home, parent,
                        false));
                return holder;
            } else if (viewType == TYPE_HEADER) {
                HeaderHolder holder = new HeaderHolder(mHeader);
                return holder;
            } else if (viewType == TYPE_LIVING) {
                LivingHolder holder = new LivingHolder(LayoutInflater.from(
                        getActivity()).inflate(R.layout.layout_live_first_header, parent,
                        false));
                return holder;
            }
            return null;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if (holder instanceof ClassHolder) {
                ClassHolder classHolder = (ClassHolder) holder;
                int index = position - 2;
                if (index >= 0) {

                } else {
                    index = 0;
                }
                if (mRoomList.size() < 1) {
                    return;
                }
                final RoomEntity entity = mRoomList.get(index);
                classHolder.lTitle.setText(entity.getName());
                if (mHeader.getVisibility() == View.VISIBLE && position == 2) {
                    classHolder.list_title_line.setVisibility(View.VISIBLE);
                } else {
                    classHolder.list_title_line.setVisibility(View.GONE);
                }
                if (entity.getList() != null && entity.getList().size() > 0) {
                    classHolder.lLiving.setVisibility(View.VISIBLE);
                    ClassEntity lClass = entity.getList().get(0);

                    SimpleDateFormat formatter = new SimpleDateFormat("MM-dd");
                    SimpleDateFormat formatter2 = new SimpleDateFormat("HH:mm");
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTimeInMillis(lClass.getTimemillis());

                    Calendar calendar1 = Calendar.getInstance();
                    calendar1.setTimeInMillis(System.currentTimeMillis());

                    String tmome = formatter.format(calendar.getTime());
                    String lmome = formatter.format(calendar1.getTime());
                    long target = lClass.getTimemillis()
                            - System.currentTimeMillis();
                    long ended = lClass.getEndtime() - System.currentTimeMillis();
                    if (ended <= 0) {
                        classHolder.lLiving.setText("直播已结束");
                        classHolder.lLiving.setTextColor(redColor);
                        classHolder.lLiving.setVisibility(View.VISIBLE);
                        String timer = formatter2.format(calendar.getTime());
                        classHolder.lState.setText(timer + "直播");
                    } else if (target <= 0) {
                        classHolder.lLiving.setText("直播中");
                        classHolder.lLiving.setTextColor(redColor);
                        classHolder.lLiving.setVisibility(View.VISIBLE);
                        String timer = formatter2.format(calendar.getTime());
                        classHolder.lState.setText(timer + "直播");
                    } else {
                        if (TextUtils.equals(tmome, lmome)) {
                            classHolder.lLiving.setText("即将直播");
                            classHolder.lLiving.setTextColor(redColor);
                            classHolder.lLiving.setVisibility(View.VISIBLE);
                            String timer = formatter2.format(calendar.getTime());
                            classHolder.lState.setText(timer + "直播");
                        } else {
                            Calendar calendar2 = Calendar.getInstance();
                            calendar2
                                    .setTimeInMillis(System.currentTimeMillis() + 86400000);
                            String amome = formatter.format(calendar2.getTime());
                            if (TextUtils.equals(tmome, amome)) {
                                classHolder.lLiving.setVisibility(View.INVISIBLE);
                                String timer = formatter2
                                        .format(calendar.getTime());
                                classHolder.lState.setText("明日" + timer + " 直播");
                                //
                            } else {
                                classHolder.lLiving.setVisibility(View.INVISIBLE);
                                String timer = formatter2
                                        .format(calendar.getTime());
                                String anim = tmome;
                                anim.replace("-", "月");
                                anim = anim + " ";
                                classHolder.lState.setText(anim + timer + " 直播");
                            }
                        }
                    }

                    classHolder.lContent.setText(lClass.getTitle());
                    if (lClass.isIsfollow()) {
                        classHolder.attention_l.setVisibility(View.VISIBLE);
                    } else {
                        classHolder.attention_l.setVisibility(View.GONE);
                    }
                } else {
                    classHolder.lLiving.setVisibility(View.INVISIBLE);
                    classHolder.lContent.setText("");
                    classHolder.lState.setText("");
                    classHolder.attention_l.setVisibility(View.GONE);
                }

                String imageId = entity.getImageid();
                if (entity.getList() != null && entity.getList().size() > 0) {
                    imageLoader.displayImage(entity.getList().get(0).getImageid(),
                            classHolder.lImage, options);
                } else {
                    imageLoader.displayImage(imageId, classHolder.lImage, options);
                }
                // holder.lContent.setVisibility(View.VISIBLE);
                if (entity.getType() == 0) {
                    classHolder.lNoMoney.setVisibility(View.GONE);
                } else {
                    classHolder.lNoMoney.setVisibility(View.VISIBLE);
                }
                classHolder.itemView.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        mRoomId = entity.getId();
                        mRoonName = entity.getName();
                        // TODO Auto-generated method stub
                        if (!isNetworkAvailble()) {
                            return;
                        }

                        if (entity.getType() == 1) {
                            showSetDialog("没有权限", "", entity.getId(),
                                    1);

                        } else {
                            NetInterface.requestAddLiving(mHandler,
                                    WHAT_ADD_LIVING, entity.getId(), null);
                        }
                    }
                });
            } else if (holder instanceof HeaderHolder) {

            } else if (holder instanceof LivingHolder) {
                LivingHolder livingHolder = (LivingHolder) holder;
                livingHolder.living_title_pro.setText(CompassApp.GLOBAL.mLivingRoomName);
                livingHolder.iv_exit_live.setOnClickListener(livingClick);
                livingHolder.lFragGroup.setOnClickListener(livingClick);
                if (hsLivingHeader) {
                    livingHolder.lFragGroup.setVisibility(View.VISIBLE);
                } else {
                    livingHolder.lFragGroup.setVisibility(View.GONE);
                }
            }
        }

        @Override
        public int getItemViewType(int position) {
            if (position == 0) {
                return TYPE_LIVING;
            }
            if (position == 1) {
                return TYPE_HEADER;
            }
            return TYPE_CLASS;
        }

        @Override
        public int getItemCount() {
            return mRoomList.size() + 2;
        }

        class LivingHolder extends RecyclerView.ViewHolder {

            public RelativeLayout lFragGroup;
            public ImageView iv_exit_live;
            public TextView living_title_pro;

            public LivingHolder(View convertView) {
                super(convertView);
                lFragGroup = (RelativeLayout) convertView
                        .findViewById(R.id.living_frag_group);
                iv_exit_live = (ImageView) convertView
                        .findViewById(R.id.iv_exit_live);
                living_title_pro = (TextView) convertView.findViewById(R.id.living_title_pro);
            }
        }

        class HeaderHolder extends RecyclerView.ViewHolder {
            public HeaderHolder(View itemView) {
                super(itemView);
            }
        }

        class ClassHolder extends RecyclerView.ViewHolder {

            public TextView lTitle;
            public ImageView lImage;
            public TextView lState;
            public ImageView lNoMoney;
            public TextView lContent;
            public View line;
            public TextView lLiving;
            public ImageView attention_l;
            public View list_title_line;

            public ClassHolder(View convertView) {
                super(convertView);
                lTitle = (TextView) convertView
                        .findViewById(R.id.time_name);
                lState = (TextView) convertView
                        .findViewById(R.id.time_date);
                lNoMoney = (ImageView) convertView
                        .findViewById(R.id.class_no_money);
                lContent = (TextView) convertView
                        .findViewById(R.id.time_detail);
                lImage = (ImageView) convertView
                        .findViewById(R.id.zixun_img);
                if (CompassApp.GLOBAL.THEME_STYLE == 0) {
                    lImage.setImageResource(R.drawable.chaogu);
                } else {
                    lImage.setImageResource(R.drawable.chaogu_night);
                }
                line = (View) convertView.findViewById(R.id.shadow);
                lLiving = (TextView) convertView
                        .findViewById(R.id.class_living);
                attention_l = (ImageView) convertView
                        .findViewById(R.id.attention_l);
                list_title_line = convertView.findViewById(R.id.list_title_line);
            }
        }
    }

    OnClickListener livingClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.living_frag_group:
                    for (int i = 0; i < mAllRoomList.size(); i++) {
                        if (mAllRoomList.get(i).getId().equals(CompassApp.GLOBAL.mLivingRoomId)) {
                            RoomEntity entity = mAllRoomList.get(i);
                            mRoomId = entity.getId();
                            mRoonName = entity.getName();
                            if (entity.getList().size() > 1) {
                                mNextTime = entity.getList().get(1).getTimemillis();
                            } else {
                                mNextTime = 0;
                            }
                            // TODO Auto-generated method stub
                            if (!isNetworkAvailble()) {
                                CompassApp.addStatis(CompassApp.GLOBAL.mgr.LIVE_ROOMLIST, "1", CompassApp.GLOBAL.mLivingRoomId,
                                        System.currentTimeMillis());
                                return;
                            }
                            Intent sintent = new Intent(getActivity(),
                                    ClassRoomActivity.class);
                            sintent.putExtra("roomid", CompassApp.GLOBAL.mLivingRoomId);
                            sintent.putExtra("roomname", mRoonName);
                            startActivity(sintent);
                        }
                    }
                    break;
                case R.id.iv_exit_live:
                    hsLivingHeader = false;
                    adapter.notifyDataSetChanged();
                    break;
            }
        }
    };

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        MobclickAgent.onPageStart("myclassfragment");
        NetInterface.requestLiveList(mHandler, WHAT_LIVE_REFRESH);
        if (CompassApp.GLOBAL.mIsInLiving && (mNextTime - System.currentTimeMillis() < 0)) {
//            hsLivingHeader = true;
            hsLivingHeader = false;
        } else {
            hsLivingHeader = false;
        }
    }

    public void refreshData() {
        NetInterface.requestLiveList(mHandler, WHAT_LIVE_REFRESH);
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("myclassfragment");
    }


    @SuppressLint("HandlerLeak")
    private NetResponseHandler2 mHandler = new NetResponseHandler2() {
        @Override
        public void netConnectLost(int arg0) {
            switch (arg0) {
                case WHAT_ADD_LIVING:
                    break;
                case WHAT_LIVE_REFRESH:
                    break;
                default:
                    break;
            }
        }

        @Override
        public void receive(int arg0, boolean arg1, Intent intent) {
            // TODO Auto-generated method stub
            String msg = intent.getStringExtra("msg");
            switch (arg0) {
                case WHAT_LIVE_REFRESH:
                    if (mIsFirstLoad) {
                        mIsFirstLoad = false;
//                        mListView.addHeaderView(mHeader);
                    }
                    mList.clear();
                    mRoomList.clear();
                    if (intent != null) {
                        EzValue value = IntentTools.safeGetEzValueFromIntent(
                                intent, "list");
//                        String aa = value.description();
                        if (value != null) {
                            EzMessage[] msges = value.getMessages();
                            if (msges != null) {
                                if (msges.length > 0) {
                                    for (int i = 0; i < msges.length; i++) {
                                        ClassEntity entity = parser(msges[i]);
                                        if (!TextUtils.isEmpty(entity.getRoomid())) {
                                            mList.add(entity);
                                        }
                                    }
                                }

                            }
                        }
                        EzValue value2 = IntentTools.safeGetEzValueFromIntent(
                                intent, "roomlist");
//                        String bb=value2.description();
                        if (value2 != null) {
                            EzMessage[] msges = value2.getMessages();
                            if (msges != null) {
                                if (msges.length > 0) {
                                    for (int i = 0; i < msges.length; i++) {
                                        RoomEntity entity = parserRoom(msges[i]);
                                        for (int j = 0; j < mList.size(); j++) {
                                            if (TextUtils.equals(mList.get(j)
                                                    .getRoomid(), entity.getId())) {
                                                entity.setList(mList.get(j));
                                            }
                                        }
                                        mRoomList.add(entity);
                                    }
                                }
                                mAllRoomList.clear();
                                mAllRoomList.addAll(mRoomList);
                            }

                        }

                        if (mRoomList.size() > 1) {
                            for (int i = 0; i < mRoomList.size(); i++) {
                                if (i < mRoomList.size())
                                    if (mRoomList.get(i).getList() != null
                                            && mRoomList.get(i).getList().size() > 0) {
                                        RoomEntity item = mRoomList.get(i);
                                        mRoomList.remove(item);
                                        mRoomList.add(0, item);
                                    }
                            }
                            for (int i = 0; i < mRoomList.size() - 1; i++) {
                                for (int j = i + 1; j < mRoomList.size(); j++) {
                                    if (mRoomList.get(i).getList() != null
                                            && mRoomList.get(i).getList().size() > 0
                                            && mRoomList.get(j).getList() != null
                                            && mRoomList.get(j).getList().size() > 0) {
                                        RoomEntity item1 = mRoomList.get(i);
                                        RoomEntity item2 = mRoomList.get(j);
                                        if (item1.getList().get(0).getTimemillis() > item2
                                                .getList().get(0).getTimemillis()) {
                                            RoomEntity t = mRoomList.get(i);
                                            mRoomList.set(i, mRoomList.get(j));
                                            mRoomList.set(j, t);
                                        }
                                    }
                                }
                            }
                        }
                        for (int i = 0; i < mRoomList.size(); i++) {
                            if (mRoomList.get(i).getList() == null
                                    || mRoomList.get(i).getList().size() == 0) {
                                mRoomList.get(i).setNoLiving(true);
                            }
                        }

                        livingTofront(mRoomList);

                        if (mHeader == null) {
                            return;
                        }
                        if (mRoomList.size() > 0) {
                            RoomEntity mHeaderEntity = mRoomList.get(0);
//                            mRoomList.remove(mHeaderEntity);
                            mHeader.setHeader(mHeaderEntity);

                        } else {
                            mHeader.setHeaderVisible(false);
                        }

                        RoomEntity nullentity0 = new RoomEntity();
                        mRoomList.add(0, nullentity0);
                        RoomEntity nullentity1 = new RoomEntity();
                        mRoomList.add(0, nullentity1);
                        adapter.notifyDataSetChanged();
                    }
                    List<RoomEntity> lRoomList = new ArrayList<>();
//                    lRoomList.addAll(mRoomList);
                    for (int i = 0; i < mRoomList.size(); i++) {
                        if (!TextUtils.isEmpty(mRoomList.get(i).getId())) {
                            lRoomList.add(mRoomList.get(i));
                        }
                    }
                    mRoomList.clear();
                    mRoomList.addAll(lRoomList);
                    mListViewFrame.finishRefresh();
                    adapter.notifyDataSetChanged();
                    break;
                case WHAT_ADD_LIVING:
                    if (intent != null) {
                        boolean result = intent.getBooleanExtra("result", false);
                        if (!result) {
                            String tips = intent.getStringExtra("tips");
                            Toast.makeText(getActivity(), tips, Toast.LENGTH_LONG)
                                    .show();
                            CompassApp.addStatis(CompassApp.GLOBAL.mgr.LIVE_ROOMLIST, "1", CompassApp.GLOBAL.mLivingRoomId,
                                    System.currentTimeMillis());
                        } else {
                            Intent sintent = new Intent(getActivity(),
                                    ClassRoomActivity.class);

                            sintent.putExtra("roomid", mRoomId);
                            sintent.putExtra("roomname", mRoonName);
                            startActivity(sintent);
                        }
                    }
                    break;
                default:
                    break;
            }
        }

        @Override
        public void timeout(int arg0) {
            switch (arg0) {
                case WHAT_ADD_LIVING:
//                    Toast.makeText(getActivity(), "网络异常", Toast.LENGTH_LONG).show();
                    break;
                case WHAT_LIVE_REFRESH:
                    adapter.notifyDataSetChanged();
                    mListViewFrame.finishRefresh();
//                    Toast.makeText(getActivity(), "网络异常", Toast.LENGTH_LONG).show();
                    break;
                default:
                    break;
            }

        }

        ;
    };


    private void livingTofront(List<RoomEntity> list) {

        List<RoomEntity> subList = new ArrayList<>();
        for (int i = list.size() - 1; i >= 0; i--) {
            if (list.get(i).getList() == null) {
                continue;
            }

            ClassEntity lClass = list.get(i).getList().get(0);

            SimpleDateFormat formatter = new SimpleDateFormat("MM-dd");
            SimpleDateFormat formatter2 = new SimpleDateFormat("HH:mm");
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(lClass.getTimemillis());

            Calendar calendar1 = Calendar.getInstance();
            calendar1.setTimeInMillis(System.currentTimeMillis());

            String tmome = formatter.format(calendar.getTime());
            String lmome = formatter.format(calendar1.getTime());
            long target = lClass.getTimemillis()
                    - System.currentTimeMillis();
            long ended = lClass.getEndtime() - System.currentTimeMillis();
            if (ended <= 0) {
//
            } else if (target <= 0) {
                subList.add(list.remove(i));
            } else {
                //即将直播
                if (TextUtils.equals(tmome, lmome)) {
                    subList.add(list.remove(i));
                } else {

                }
            }
        }

        Collections.reverse(subList);
        list.addAll(0, subList);
    }

    private RoomEntity parserRoom(EzMessage room) {
        Log.e("", room.description());
        RoomEntity entity = new RoomEntity();
        String id = room.getKVData("id").getStringWithDefault("");

        String name = room.getKVData("name").getStringWithDefault("");
        name = URLDecoder.decode(name);
        String imageid = room.getKVData("imageid").getStringWithDefault("");
        int type = room.getKVData("type").getInt32();
        entity.setId(id);
        entity.setImageid(imageid);
        entity.setName(name);
        entity.setType(type);
        return entity;
    }

    private ClassEntity parser(EzMessage live) {
        ClassEntity classEntity = new ClassEntity();
        String cid = live.getKVData("id").getStringWithDefault("");
        String cprofessor = live.getKVData("professor")
                .getStringWithDefault("");
        String ctitle = live.getKVData("title").getStringWithDefault("");
        String ccontent = live.getKVData("content").getStringWithDefault("");
        String cimageid = live.getKVData("imageid").getStringWithDefault("");
        long ctime = live.getKVData("starttime").getInt64();
        long etime = live.getKVData("endtime").getInt64();
        boolean cisfollow = live.getKVData("isfollow").getBoolean();
        int ctype = live.getKVData("type").getInt32();
        String curl = live.getKVData("url").getStringWithDefault("");
        String roomid = live.getKVData("roomid").getStringWithDefault("");
        classEntity.setRoomid(roomid);
        classEntity.setContent(ccontent);
        classEntity.setId(cid);
        classEntity.setImageid(cimageid);
        classEntity.setIsfollow(cisfollow);
        classEntity.setProfessor(cprofessor);
        classEntity.setTimemillis(ctime);
        classEntity.setEndtime(etime);
        classEntity.setTitle(ctitle);
        classEntity.setType(ctype);
        classEntity.setUrl(curl);
        return classEntity;
    }

    ImageLoader imageLoader = ImageLoader.getInstance();
    DisplayImageOptions options = null;

    private void showSetDialog(String title, String content, final String key,
                               final int what) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(title);
        View view = LayoutInflater.from(getActivity()).inflate(
                R.layout.input_password_view, null);
        final EditText etContent = (EditText) view
                .findViewById(R.id.et_content);

        setTextLimit(what, etContent);
        etContent.setText(content);
        etContent.setSelection(etContent.getText().length());
        builder.setView(view);
        builder.setPositiveButton("取消", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                // TODO Auto-generated method stub
                if (TextUtils.isEmpty(etContent.getText().toString())) {
                    Toast.makeText(getActivity(), "请输入密码", Toast.LENGTH_LONG)
                            .show();
                    return;
                }
                String pass = MD5.mD5Encode(etContent.getText().toString(),
                        "UTF-8");
                NetInterface.requestAddLiving(mHandler, WHAT_ADD_LIVING, key,
                        pass);
            }
        });
        dialog = builder.create();

        dialog.show();
    }

    private void setTextLimit(int what, final EditText et) {
        et.setFilters(new InputFilter[]{new InputFilter.LengthFilter(40)});
    }

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
