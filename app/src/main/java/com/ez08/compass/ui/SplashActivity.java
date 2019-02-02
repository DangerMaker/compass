package com.ez08.compass.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.ez08.compass.CompassApp;
import com.ez08.compass.R;
import com.ez08.compass.autoupdate.updateModule.AutoUpdateActivity;
import com.ez08.compass.autoupdate.updateModule.AutoUpdatePacket;
import com.ez08.compass.entity.InitEntity;
import com.ez08.compass.entity.NewAdvertEntity;
import com.ez08.compass.net.HttpUtils;
import com.ez08.compass.tools.AdsManager;
import com.ez08.compass.tools.AppStatusConstant;
import com.ez08.compass.tools.AppStatusManager;
import com.ez08.compass.tools.AuthTool;
import com.ez08.compass.tools.UpLoadTools;
import com.ez08.compass.tools.UtilTools;
import com.ez08.compass.ui.personal.LoginActivity;
import com.ez08.compass.userauth.AuthModule;
import com.ez08.compass.userauth.AuthUserInfo;
import com.umeng.analytics.AnalyticsConfig;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

public class SplashActivity extends BaseActivity {

    Context mContext;
    CompassApp mCompassApp;
    private String infourl = "";
    private boolean addAdvertStatis = false;
    Bitmap bmp = null;
    int timer = 1000;
    int tempTimer = 0;
    boolean mInstall;

    private SharedPreferences mySharedPreferences;
    private ImageView mLogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        mAddStatus = false;
        mContext = this;
        AppStatusManager.getInstance().setAppStatus(AppStatusConstant.STATUS_NORMAL);
        if (!this.isTaskRoot()) {
            Intent intent = getIntent();
            if (intent != null) {
                String action = intent.getAction();
                if (intent.hasCategory(Intent.CATEGORY_LAUNCHER) && Intent.ACTION_MAIN.equals(action)) {
                    finish();
                    return;
                }
            }
        }

        MobclickAgent.openActivityDurationTrack(false);
        AnalyticsConfig.enableEncrypt(false); // 不加密

        //获取mac地址
        SharedPreferences mSharedPreferences = getSharedPreferences("wifi", 0);
        String wifiMacAddr = mSharedPreferences.getString("wifiMacAddr", "");

        mySharedPreferences = getSharedPreferences("viewpager",
                Activity.MODE_PRIVATE);
        boolean mInstallSwitch = mySharedPreferences.getBoolean("install_switch", false);

        if (TextUtils.isEmpty(wifiMacAddr)) {
            mInstall = true;
        } else {
            mInstall = false;
        }

        if (mInstall || mInstallSwitch) {
            CompassApp.GLOBAL.mFirstInstall = true;
        } else {
            CompassApp.GLOBAL.mFirstInstall = false;
        }

        mCompassApp = (CompassApp) this.getApplication();
        //取消状态栏

        SharedPreferences sp = getSharedPreferences(
                "kefu", Activity.MODE_PRIVATE);

        CompassApp.GLOBAL.CUSTOMER_LEVEL = sp.getInt("compass_level", -1);
        CompassApp.GLOBAL.CUSTOMER_AUTHS = sp.getString("auths", "");
        CompassApp.GLOBAL.THEME_STYLE = sp.getInt("theme_style", 0);
        CompassApp.GLOBAL.DEVELOPER_MODE = sp.getInt("developer_mode", 0);
        //确认auth
        AuthTool.initType(CompassApp.GLOBAL.CUSTOMER_LEVEL, CompassApp.GLOBAL.CUSTOMER_AUTHS);

        setContentView(R.layout.activity_splash);
        MobclickAgent.openActivityDurationTrack(false);
        AnalyticsConfig.enableEncrypt(false); // 不加密
        mLogo = (ImageView) findViewById(R.id.start_logo);
        if (CompassApp.GLOBAL.THEME_STYLE == 0) {
            mLogo.setBackgroundResource(R.drawable.biaoti);
            findViewById(R.id.start_logo1).setBackgroundResource(R.drawable.zhinanzhen_bottom);
            findViewById(R.id.start_logo2).setBackgroundResource(R.drawable.zhinanzhen_bottom);
            findViewById(R.id.start_logo0).setBackgroundResource(R.drawable.zhinanzhen_bottom);
            findViewById(R.id.start_layout).setBackgroundResource(R.color.white);
        } else {
            mLogo.setBackgroundResource(R.drawable.biaoti_night);
            findViewById(R.id.start_logo1).setBackgroundResource(R.drawable.zhinanzhen_bottom_night);
            findViewById(R.id.start_logo2).setBackgroundResource(R.drawable.zhinanzhen_bottom_night);
            findViewById(R.id.start_logo0).setBackgroundResource(R.drawable.zhinanzhen_bottom_night);
            findViewById(R.id.start_layout).setBackgroundResource(R.color.black_background);
        }

        if (!TextUtils.isEmpty(AdsManager.getInstance(this).getCid())) {
            timer = 3000;
            tempTimer = 2000;

            final NewAdvertEntity entity = AdsManager.getInstance(this).getAdsAtSplash();
            if (entity != null) {
                mLogo.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        addAdvertStatis = true;
                        File tempFile = UtilTools.buildFile(getFilesDir(), AdsManager.getInstance(SplashActivity.this).getCid() + "_splashAd.png");
                        bmp = UtilTools.getLoacalBitmap(tempFile);
                        ImageView advertImg = (ImageView) findViewById(R.id.advert_img);
                        advertImg.setImageBitmap(bmp);
                        findViewById(R.id.advert_layout).setVisibility(View.VISIBLE);
                        mLogo.setVisibility(View.GONE);
                        findViewById(R.id.start_logo1).setVisibility(View.GONE);
                        findViewById(R.id.start_logo2).setVisibility(View.GONE);

                        findViewById(R.id.time_view).setVisibility(View.VISIBLE);
                        final String lInfoUrl = entity.getInfourl();
                        advertImg.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                infourl = lInfoUrl;
                            }
                        });
                    }
                }, 1000);

            }
        }

        if (tempTimer != 0) {
            mLogo.postDelayed(new Runnable() {

                @Override
                public void run() {
                    // TODO 1s
                    ((TextView) findViewById(R.id.time_view)).setText("2");
                }
            }, tempTimer);
        }

        mLogo.postDelayed(new Runnable() {

            @Override
            public void run() {
                ((TextView) findViewById(R.id.time_view)).setText("1");
                // TODO Auto-generated method stub
                if (!isNetworkAvailble()) {
                    showNetDialog();
                } else {
                    startNetWork();
                }

            }
        }, timer);

    }

    private void showNetDialog() {
        new AlertDialog.Builder(mContext)
                .setTitle("指南针提示")
                .setMessage("没有网络可用，请检查网络!")
                .setPositiveButton("重试", new OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        // TODO Auto-generated method stub
                        if (!isNetworkAvailble()) {
                            showNetDialog();
                        } else {
                            startNetWork();
                        }
                    }
                }).setNegativeButton("退出", new OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                // TODO Auto-generated method stub
                finish();
            }
        }).show();
    }

    private void showNetDialog2() {
        new AlertDialog.Builder(mContext)
                .setTitle("指南针提示")
                .setMessage("获取分发地址失败,请检查网络!")
                .setPositiveButton("重试", new OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        // TODO Auto-generated method stub
                        if (!isNetworkAvailble()) {
                            showNetDialog();
                        } else {
                            startNetWork();
                        }
                    }
                }).setNegativeButton("退出", new OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                // TODO Auto-generated method stub
                finish();
            }
        }).show();
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();

    }

    private void startNetWork() {
        final HttpUtils u = new HttpUtils();
        new Thread() {
            public void run() {
                String result = u.getJsonContent(CompassApp.Constants.REQUEST_URL);
                InitEntity entity = parserResult(result);
                if (entity != null) {
                    Log.e("", "success+" + result);
                    SharedPreferences.Editor editor = mySharedPreferences
                            .edit();
                    editor.putString("updateresult", result);
                    editor.commit();
                    String ipport = entity.getServer();
                    int index = ipport.indexOf(":");
                    String t_ip = ipport.substring(0, index);
                    CompassApp.GLOBAL.IP = getIP(t_ip);
//                    CompassApp.IP="172.17.241.132";
                    CompassApp.GLOBAL.PORT = Integer.parseInt(ipport
                            .substring(index + 1));
                    UpLoadTools.URL = entity.getImageupload();
                    CompassApp.GLOBAL.ADVERT_URL = entity.getAdurl2();

                    mHandler.sendEmptyMessage(0);
                    Message mesg = Message.obtain();
                    mesg.what = 1;
                    mesg.obj = entity;
                    mHandler.sendMessage(mesg);

                } else {
                    String data = mySharedPreferences.getString("updateresult",
                            "");
                    Log.e("", "error+" + data);
                    if (!TextUtils.isEmpty(data)) {
                        setNet(data);
                    } else {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                showNetDialog2();
                            }
                        });
                    }
                }
            }
        }.start();
    }

    public String getIP(String name) {
        InetAddress address = null;
        try {
            address = InetAddress.getByName(name);
        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            System.out.println("获取失败");
        }
        return address.getHostAddress();
    }

    private void setUpDate(InitEntity entity) {
        String local_version = "";
        PackageManager pm = getPackageManager();
        try {
            PackageInfo pi = pm.getPackageInfo(getPackageName(),
                    PackageManager.GET_ACTIVITIES);
            local_version = pi.versionName;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        String tatget_version = entity.getVersion();
        String[] target = tatget_version.split("\\.");
        String[] local = local_version.split("\\.");
        boolean setVersion = false;
        for (int i = 0; i < local.length; i++) {
            int a = Integer.parseInt(local[i]);
            int b = Integer.parseInt(target[i]);
            if (a < b) {
                setVersion = true;
                break;
            } else if (a > b) {
                setVersion = false;
                break;
            } else {
                setVersion = false;
            }
        }
        if (!setVersion) {
            return;
        }
        final AutoUpdatePacket up = new AutoUpdatePacket();
        up.setType(0);
        up.setCaburl(entity.getUrl());
        up.setBrief(entity.getInfo());
        up.setTver(entity.getVersion());
        Intent intent1 = new Intent(this, AutoUpdateActivity.class);
        intent1.putExtra("up", up);
        intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Message msg = Message.obtain();
        msg.what = 2;
        msg.obj = intent1;
        mHandler.sendMessageDelayed(msg, 1000);

    }

    private void setNet(String result) {
        InitEntity entity = parserResult(result);
        String ipport = entity.getServer();
        int index = ipport.indexOf(":");
        //set socket address
        CompassApp.GLOBAL.IP = ipport.substring(0, index);
        CompassApp.GLOBAL.PORT = Integer.parseInt(ipport.substring(index + 1));
        //set upload http host
        UpLoadTools.URL = entity.getImageupload();
        mHandler.sendEmptyMessage(0);
        Message mesg = Message.obtain();
        mesg.what = 1;
        mesg.obj = entity;
        mHandler.sendMessage(mesg);
    }

    private InitEntity parserResult(String result) {
        JSONObject data = null;
        InitEntity entity = new InitEntity();
        try {
            data = new JSONObject(result);
            if (!data.isNull("server")) {
                entity.setServer(data.getString("server"));
            }
            if (!data.isNull("imageupload")) {
                entity.setImageupload(data.getString("imageupload"));
            }
            if (!data.isNull("date")) {
                entity.setDate(data.getString("date"));
            }
            if (!data.isNull("info")) {
                entity.setInfo(data.getString("info"));
            }
            if (!data.isNull("url")) {
                entity.setUrl(data.getString("url"));
            }
            if (!data.isNull("version")) {
                entity.setVersion(data.getString("version"));
            }
            if (!data.isNull("adurl")) {
                entity.setAdurl(data.getString("adurl"));
            }

            if (!data.isNull("adurl2")) {
                entity.setAdurl2(data.getString("adurl2"));
            }
            if (!data.isNull("authsmap")) {
                JSONObject authMap = data.getJSONObject("authsmap");
                if (authMap != null) {
                    String level2 = authMap.getString("level2");
                    String level2s[] = level2.split(",");
                    CompassApp.GLOBAL.level2ID = level2s;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return entity;
    }


    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    mCompassApp.init();
                    AuthModule.startNet();
                    if (AuthUserInfo.isLogined()) {
                        Intent i = new Intent(mContext, MainActivity.class);
                        i.putExtra("infourl", infourl);
                        startActivity(i);
                    } else {
                        Intent i = new Intent(mContext, LoginActivity.class);
                        i.putExtra("fromstart", true);
                        i.putExtra("install", mInstall);
                        startActivity(i);
                    }
                    finish();
                    CompassApp.addStatis(CompassApp.GLOBAL.mgr.START_APP, "0", mInstall ? "1" : "0", System.currentTimeMillis());
                    if (addAdvertStatis) {
                        CompassApp.addStatis(CompassApp.GLOBAL.mgr.ADVERT_START, "0", "", System.currentTimeMillis());
                    }
                    break;
                case 1:
                    InitEntity result = (InitEntity) msg.obj;
                    if (result != null) {
                        setUpDate(result);
                    }
                    break;
                case 2:
                    Intent intent1 = (Intent) msg.obj;
                    startActivity(intent1);
                    break;
                default:
                    break;
            }

        }

    };

}
