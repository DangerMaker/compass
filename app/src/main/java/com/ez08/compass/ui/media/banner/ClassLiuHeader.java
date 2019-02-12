package com.ez08.compass.ui.media.banner;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.thinkive.framework.util.ScreenUtil;
import com.ez08.compass.CompassApp;
import com.ez08.compass.R;
import com.ez08.compass.entity.ClassEntity;
import com.ez08.compass.entity.ItemStock;
import com.ez08.compass.entity.NewAdvertEntity;
import com.ez08.compass.entity.RoomEntity;
import com.ez08.compass.net.NetInterface;
import com.ez08.compass.tools.AdsManager;
import com.ez08.compass.tools.MD5;
import com.ez08.compass.ui.WebActivity;
import com.ez08.compass.ui.media.ClassRoomActivity;
import com.ez08.compass.auth.AuthUserInfo;
import com.ez08.support.net.EzMessage;
import com.ez08.support.net.NetResponseHandler2;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ClassLiuHeader extends FrameLayout {

    private final int WHAT_ATTENTION_FOLLOW = 1001;

    CustomBanner<RoomEntity> banner;
    private Context mContext;
    private RoomEntity entity;
    RelativeLayout.LayoutParams picContainerLP;

    public ClassLiuHeader(@NonNull Context context) {
        super(context);
        mContext = context;
    }

    public ClassLiuHeader(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        if (CompassApp.GLOBAL.THEME_STYLE == 0) {
            options = new DisplayImageOptions.Builder()
                    .showImageForEmptyUri(R.drawable.chaogu_large)
                    .showImageOnFail(R.drawable.chaogu_large).cacheInMemory(true)
                    .cacheOnDisk(true).considerExifParams(true).build();
        } else {
            options = new DisplayImageOptions.Builder()
                    .showImageForEmptyUri(R.drawable.chaogu_large_night)
                    .showImageOnFail(R.drawable.chaogu_large_night).cacheInMemory(true)
                    .cacheOnDisk(true).considerExifParams(true).build();
        }
    }

    public void setHeaderVisible(boolean visible) {
        banner = (CustomBanner) findViewById(R.id.banner);
        if (visible) {
            banner.setVisibility(View.VISIBLE);
        } else {
            banner.setVisibility(View.GONE);
        }
    }

    public void setHeader(final RoomEntity roomEntity) {
        banner = (CustomBanner) findViewById(R.id.banner);
        if (roomEntity == null) {
            banner.setVisibility(View.GONE);
            return;
        }
        banner.setVisibility(View.VISIBLE);

        FrameLayout.LayoutParams fl = (FrameLayout.LayoutParams) banner
                .getLayoutParams();

        fl.width = CompassApp.GLOBAL.SCREEN_W;
        fl.height = (int) (fl.width * 8 / 16 + ScreenUtil.dpToPx(mContext, 50));
        banner.setLayoutParams(fl);

        entity = roomEntity;

        final List<RoomEntity> list = new ArrayList<>();

        // new ads
        NewAdvertEntity adsEntity = AdsManager.getInstance(mContext).getAdsAtLivingRoom();
        if (adsEntity != null) {
            CompassApp.addStatis(CompassApp.GLOBAL.mgr.ADVERT_LIVE, "0", "", System.currentTimeMillis());
            RoomEntity entity = new RoomEntity();
            entity.infoUrl = adsEntity.getInfourl();
            entity.setImageid(adsEntity.getImageurl());
            entity.setName(adsEntity.getTitle());
            entity.setType(666);
            list.add(entity);
        }

        list.add(entity);


        banner.setPages(new CustomBanner.ViewCreator<RoomEntity>() {
            @Override
            public View createView(Context context, final int position) {
                View view = LayoutInflater.from(mContext).inflate(R.layout.view_banner_living, null);

                return view;
            }

            @Override
            public void updateUI(Context context, View view, int position, RoomEntity data) {

                ImageView imageView = (ImageView) view.findViewById(R.id.farm_pager);
                TextView nameView = (TextView) view.findViewById(R.id.introduce_name);
                TextView countView = (TextView) view.findViewById(R.id.introduce_coutdown);
                picContainerLP = (RelativeLayout.LayoutParams) imageView
                        .getLayoutParams();

                picContainerLP.width = CompassApp.GLOBAL.SCREEN_W;
                picContainerLP.height = picContainerLP.width * 8 / 16;
                imageView.setLayoutParams(picContainerLP);

                ClassEntity classEntity;
                if (data != null && data.getList() != null && data.getList().size() > 0) {
                    classEntity = data.getList().get(0);
                    imageLoader.displayImage(classEntity.getImageid(), (ImageView) imageView, options);
                    nameView.setText(data.getName());
                    countView.setText(getLiveDate(classEntity.getTimemillis(),
                            classEntity.getEndtime()));
                }else{
                    imageLoader.displayImage(roomEntity.getImageid(), (ImageView) imageView, options);
                    nameView.setText(roomEntity.getName());
                    countView.setText("");
                }

                if (data != null && data.getType() == 666) {
                    imageLoader.displayImage(data.getImageid(), (ImageView) imageView, options);
                    nameView.setText(data.getName());
                    countView.setText("");
                }
            }
        }, list);

        banner.setOnPageClickListener(new CustomBanner.OnPageClickListener<RoomEntity>() {
            @Override
            public void onPageClick(int position, RoomEntity roomEntity1) {

                if (roomEntity1.getType() == 666) {
                    ItemStock entity = new ItemStock();
                    entity.setUrl(roomEntity1.infoUrl + "?personid=" + AuthUserInfo.getMyCid() + "&skey=" + AuthUserInfo.getMyToken());
                    Intent intentWeb = new Intent(mContext, WebActivity.class);
                    intentWeb.putExtra("entity", entity);
                    intentWeb.putExtra("ifFromAd", true);
                    mContext.startActivity(intentWeb);
                    CompassApp.addStatis(CompassApp.GLOBAL.mgr.ADVERT_LIVE, "1", "", System.currentTimeMillis());

                    return;
                }


                CompassApp.GLOBAL.mIsInLiving = false;
                if (!isNetworkAvailble()) {
                    return;
                }

                if (roomEntity1.getType() == 1) {
                    showSetDialog("没有权限", "", roomEntity1.getId(),
                            WHAT_REQUEST_SET);
                } else {
                    NetInterface.requestAddLiving(mHandler, WHAT_ADD_LIVING,
                            roomEntity1.getId(), null);
                }

            }
        });

        if (list != null && list.size() > 1) {
            banner.startTurning(5000);
            banner.setIndicatorStyle(CustomBanner.IndicatorStyle.ORDINARY);
        }else{
            banner.setIndicatorStyle(CustomBanner.IndicatorStyle.NONE);
        }

    }

    class MyOnClickListener implements OnClickListener {

        List<RoomEntity> list;
        int position;

        public MyOnClickListener(List<RoomEntity> list, int position) {
            this.list = list;
            this.position = position;
        }

        @Override
        public void onClick(View view) {
            Log.e("onclick", "banner");
            if (TextUtils.equals(list.get(position).getId(), CompassApp.GLOBAL.mLivingRoomId) && CompassApp.GLOBAL.mIsInLiving) {

                CompassApp.GLOBAL.mIsInLiving = false;
                if (!isNetworkAvailble()) {
                    return;
                }

                if (list.get(position).getType() == 666) {
                    ItemStock entity = new ItemStock();
                    entity.setUrl(list.get(position).infoUrl + "?personid=" + AuthUserInfo.getMyCid());
                    Intent intentWeb = new Intent(mContext, WebActivity.class);
                    intentWeb.putExtra("entity", entity);
                    intentWeb.putExtra("ifFromAd", true);
                    mContext.startActivity(intentWeb);
                    CompassApp.addStatis(CompassApp.GLOBAL.mgr.ADVERT_LIVE, "1", "", System.currentTimeMillis());

                    return;
                }

                if (list.get(position).getType() == 1) {
                    showSetDialog("没有权限", "", list.get(position).getId(),
                            WHAT_REQUEST_SET);
                } else {
                    NetInterface.requestAddLiving(mHandler, WHAT_ADD_LIVING,
                            list.get(position).getId(), null);
                }
            }
        }
    }

    public boolean isNetworkAvailble() {
        ConnectivityManager connectivityManager = (ConnectivityManager) mContext
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();
        if (info == null || !info.isAvailable()) {
            Toast.makeText(mContext, "没有可用网络", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private static final int WHAT_REQUEST_SET = 1001;
    private static final int WHAT_ADD_LIVING = 1007;
    AlertDialog dialog = null;

    private void setTextLimit(int what, final EditText et) {

        switch (what) {
            case WHAT_REQUEST_SET:
                et.setFilters(new InputFilter[]{new InputFilter.LengthFilter(40)});
                break;
            default:
                break;
        }
    }

    private void showSetDialog(String title, String content, final String key,
                               final int what) {

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle(title);
        View view = LayoutInflater.from(mContext).inflate(
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
                    Toast.makeText(mContext, "请输入密码", Toast.LENGTH_LONG).show();
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

    NetResponseHandler2 mHandler = new NetResponseHandler2() {

        @Override
        public void receive(int arg0, boolean arg1, Intent arg2) {
            // TODO Auto-generated method stub
            String msg = arg2.getStringExtra("msg");
            switch (arg0) {
                case WHAT_ATTENTION_FOLLOW:
                    if (arg1) {
                        entity.getList().get(0)
                                .setIsfollow(!entity.getList().get(0).isIsfollow());
                        setHeader(entity);
                    } else {
                        Toast.makeText(mContext, "修改失败!", Toast.LENGTH_LONG).show();
                    }
                    break;
                case WHAT_ADD_LIVING:
                    if (arg2 != null) {
                        boolean result = arg2.getBooleanExtra("result", false);
                        if (!result) {
                            String tips = arg2.getStringExtra("tips");
                            Log.e("z", "z4");
                            Toast.makeText(mContext, tips, Toast.LENGTH_LONG)
                                    .show();
                        } else {
                            Intent sintent = new Intent(mContext,
                                    ClassRoomActivity.class);
                            sintent.putExtra("roomid", entity.getId());
                            sintent.putExtra("roomname", entity.getName());
                            mContext.startActivity(sintent);
                        }
                    }
                    break;
                default:
                    break;
            }
        }

    };

    private ClassEntity Classparser(EzMessage live) {
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


    private LinearLayout mGroup;

    // --------------zhangchungang---------------
    private static String getDateStr(long millis) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        Date dt = new Date(millis);
        return sdf.format(dt);
    }

    // ------------------------------------------
    private String getLiveDate(long mills, long endtime) {

        long current = System.currentTimeMillis();
        long target = mills - current;
        long ended = endtime - System.currentTimeMillis();
        if (ended <= 0) {
            return "直播已结束";
        } else if (target < 0) {
            return "直播中";
        } else {
            String content = "";
            SimpleDateFormat formatter = new SimpleDateFormat("MM-dd");
            SimpleDateFormat formatter2 = new SimpleDateFormat("HH:mm");
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(mills);
            Calendar calendar1 = Calendar.getInstance();
            calendar1.setTimeInMillis(current);
            String tmome = formatter.format(calendar.getTime());
            String lmome = formatter.format(calendar1.getTime());
            if (TextUtils.equals(tmome, lmome)) {
                String timer = formatter2.format(calendar.getTime());
                content = timer + "开始";
            } else {
                Calendar calendar2 = Calendar.getInstance();
                calendar2
                        .setTimeInMillis(System.currentTimeMillis() + 86400000);
                String amome = formatter.format(calendar2.getTime());
                if (TextUtils.equals(tmome, amome)) {
                    String timer = formatter2.format(calendar.getTime());
                    content = "明日" + timer + " 开始";
                    //
                } else {
                    String timer = formatter2.format(calendar.getTime());
                    String anim = tmome;
                    anim.replace("-", "月");
                    anim = anim + " ";
                    content = anim + timer + " 开始";
                }
            }

            return content;
        }
    }

}