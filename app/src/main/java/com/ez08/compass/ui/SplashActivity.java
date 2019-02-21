package com.ez08.compass.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Looper;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.ez08.compass.CompassApp;
import com.ez08.compass.R;
import com.ez08.compass.entity.NewAdvertEntity;
import com.ez08.compass.tools.AdsManager;
import com.ez08.compass.tools.LoadBalancingManager;
import com.ez08.compass.tools.UtilTools;
import com.ez08.compass.ui.base.BaseActivity;
import com.ez08.compass.ui.personal.LoginActivity;
import com.umeng.analytics.AnalyticsConfig;
import com.umeng.analytics.MobclickAgent;

import java.io.File;

import static com.ez08.compass.tools.LoadBalancingManager.LOAD_BALANCING_FINISH;

public class SplashActivity extends BaseActivity {

    public static final String SOCKET_CONNECT_SUCCESS = "ez08.net.connect.judge.broadcast";
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
    ProgressDialog pDialog;

    protected void dismissBusyDialog() {
        if (pDialog != null && pDialog.isShowing()) {
            pDialog.dismiss();
        }
    }

    protected void showBusyDialog() {
        if (pDialog == null) {
            pDialog = new ProgressDialog(this);
            pDialog.setMessage("请稍候...");
            pDialog.setCancelable(true);
            pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        }

        if (pDialog.isShowing()) {
            return;
        }

        if (Thread.currentThread() == Looper.getMainLooper().getThread()) {
            pDialog.show();
        } else {
            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    pDialog.show();
                }
            });
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_splash);

        mAddStatus = false;
        mContext = this;
        MobclickAgent.openActivityDurationTrack(false);
        AnalyticsConfig.enableEncrypt(false); // 不加密

        IntentFilter filter = new IntentFilter();
        filter.addAction(SOCKET_CONNECT_SUCCESS); //socket connect
        filter.addAction(LOAD_BALANCING_FINISH); //http request finish
        registerReceiver(connectReceiver, filter);

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
                isTimeOver = true;
                if (!isNetworkAvailble()) {
                    showNetDialog();
                } else {
                    startNetWork();
                }

            }
        }, timer);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(connectReceiver);
        CompassApp.GLOBAL.JUMP = 3;
    }

    private void showNetDialog() {
        new AlertDialog.Builder(mContext)
                .setTitle("指南针提示")
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton("重试", new OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        if (!isNetworkAvailble()) {
                            showNetDialog();
                        } else {
                            //todo 加载Loading 加个
                            showBusyDialog();
                            LoadBalancingManager.getInstance(mContext).setUrl(CompassApp.Constants.REQUEST_URL);
                        }
                    }
                }).setNegativeButton("退出", new OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                finish();
            }
        }).show();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    ;

    boolean isTimeOver = false;
    int jump = 0; //0 hold 1 login 2 main 3 failure pls retry
    String message = "请检查网络";

    private void startNetWork() {
        if(mContext == null){
            return;
        }

        if (CompassApp.GLOBAL.APP_IS_NEW) {
            CompassApp.GLOBAL.JUMP = jump;
        }
        if (CompassApp.GLOBAL.JUMP == 0) {
            showBusyDialog();
        } else if (CompassApp.GLOBAL.JUMP == 1) {
            Intent i = new Intent(mContext, LoginActivity.class);
            i.putExtra("fromstart", true);
            i.putExtra("install", mInstall);
            startActivity(i);
            finish();
        } else if (CompassApp.GLOBAL.JUMP == 2) {
            Intent i = new Intent(mContext, MainActivity.class);
            i.putExtra("infourl", infourl);
            startActivity(i);
            finish();
            CompassApp.addStatis(CompassApp.GLOBAL.mgr.START_APP, "0", mInstall ? "1" : "0", System.currentTimeMillis());
            if (addAdvertStatis) {
                CompassApp.addStatis(CompassApp.GLOBAL.mgr.ADVERT_START, "0", "", System.currentTimeMillis());
            }
        } else if (jump == 3) {
            showNetDialog();
        }
    }


    private BroadcastReceiver connectReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null && intent.getAction() != null) {
                dismissBusyDialog();
                if (intent.getAction().equals(LOAD_BALANCING_FINISH)) {
                    if (!intent.getBooleanExtra("success", false)) {
                        jump = 3;
                        message = intent.getStringExtra("message");
                    } else {
                        return;
                    }
                } else if (intent.getAction().equals(SOCKET_CONNECT_SUCCESS)) {
                    String cid = intent.getStringExtra("cid");
                    if (!TextUtils.isEmpty(cid) && cid.contains("T-")) {
                        jump = 1;
                    } else {
                        jump = 2;
                    }
                }

                if (isTimeOver) {
                    startNetWork();
                }
            }
        }
    };

}
