package com.ez08.compass.ui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ez08.compass.CompassApp;
import com.ez08.compass.R;
import com.ez08.compass.database.IMDBHelper;
import com.ez08.compass.database.NetStatisInterFace;
import com.ez08.compass.entity.ItemStock;
import com.ez08.compass.entity.PushMessageEntity;
import com.ez08.compass.net.NetInterface;
import com.ez08.compass.tools.AdsManager;
import com.ez08.compass.tools.AppStatusConstant;
import com.ez08.compass.tools.AppStatusManager;
import com.ez08.compass.tools.AuthTool;
import com.ez08.compass.tools.DataCleanManager;
import com.ez08.compass.tools.IndexSortManager;
import com.ez08.compass.tools.MessageService;
import com.ez08.compass.tools.PushManager;
import com.ez08.compass.tools.PushService;
import com.ez08.compass.tools.ToastUtils;
import com.ez08.compass.ui.kefu.MyKefuFragment;
import com.ez08.compass.ui.market.HomeTabFragment;
import com.ez08.compass.ui.media.ClassFragment;
import com.ez08.compass.ui.news.InfoNewTabFragment;
import com.ez08.compass.ui.personal.BindMobileDialog;
import com.ez08.compass.ui.personal.LoginActivity;
import com.ez08.compass.ui.trader.SecurityFragment;
import com.ez08.compass.userauth.AuthModule;
import com.ez08.compass.userauth.AuthUserInfo;
import com.ez08.support.net.EzMessage;
import com.ez08.support.net.EzNet;
import com.ez08.support.net.NetManager;
import com.ez08.support.net.NetResponseHandler;
import com.ez08.support.net.NetResponseHandler2;
import com.ez08.tools.IntentTools;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.UMShareAPI;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity implements OnClickListener, NetStatisInterFace.StatisticsFace {
    private FragmentManager fragmentManager;
    private HomeTabFragment mHomeFragment;
    private MyKefuFragment mSeekFragment;
    private InfoNewTabFragment mInfoTabFragment;
    private ClassFragment mClassFragment;
    private SecurityFragment mSecurityFragment;
    private TextView txtHome, txtClass, txtTrade, txtService, txtSeek;
    private ImageView imgHome, imgClass, imgTrade, imgService, imgSeek;
    private int colorSelected, colorNormal;
    private int mCurrentSelect = -1;
    private LinearLayout layout_01;
    private LinearLayout layout_02;
    private LinearLayout layout_03;
    private RelativeLayout layout_04;
    private LinearLayout layout_06;   //交易
    private LinearLayout mTabBar;
    private static final int WHAT_GET_KEFU_INFO = 1008; // 获得客服资料
    private static final int WHAT_DEL_LIVING = 1009;
    private static final int WHAT_REQUEST_GET_MY_INFO = 1007;
    public static final int GET_KH_SEND_CODE = 1012;
    public static final int GET_KH_VERIFY_CODE = 1013;

    public static final String GOTO_LIVE = "com.ez08.gotolive";
    public static final String GOTO_SHARE = "com.ez08.gotoshare";
    public static final String REFRESH_FRAGMENT = "com.ez08.refresh_fragment";

    private Context mContext;
    public static boolean IS_ALIVE = false;
    public static boolean canShow = false;
    private List<PushMessageEntity> list;// 数据
    private List<PushMessageEntity> unselectlist;// 未被选中的数据源

    private ImageView iv_red_dot;
    private SharedPreferences mySharedPreferences;
    private SharedPreferences dataSharedPreferences;

    private SharedPreferences advertSharedPreferences;

    private final static int WHAT_PUSH_RESULT = 20;

    AlertDialog mlDialog;
    public static boolean forceLogout = false;    //是否被强制踢出
    public static String forceMsg = "";
    private boolean logout = false;

    private NetStatisInterFace mNetStatisInterFace;
    private long mLastStatisTime;
    public boolean mAddStatus = true;    //是否在appcallback判断的activitys里

    BindMobileDialog.Bind bindDialogInovke;
    BindMobileDialog.VerifyCode verifyDialogInovke;
    BindMobileDialog bindDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (AppStatusManager.getInstance().getAppStatus() == AppStatusConstant.STATUS_FORCE_KILLED) {
            finish();
            Intent main = new Intent(MainActivity.this,
                    SplashActivity.class);
            startActivity(main);
        }

        myPermission();
        AdsManager.getInstance(MainActivity.this).setUrl(CompassApp.GLOBAL.ADVERT_URL + "?personid=" + AuthUserInfo.getMyCid());

        IS_ALIVE = true;
        mContext = this;
        setContentView(R.layout.activity_main);
        initViews();
        fragmentManager = getSupportFragmentManager();
        NetInterface.getMyInfo(mHandler, WHAT_REQUEST_GET_MY_INFO);

        IntentFilter filter1 = new IntentFilter();
        filter1.addAction("del_living");
        registerReceiver(delLiveReceiver, filter1);

        //重新打开MainActivity
        IntentFilter rehreshFilter = new IntentFilter();
        rehreshFilter.addAction(REFRESH_FRAGMENT);
        registerReceiver(refreshReceiver, rehreshFilter);

        //获取客服信息、开启直播间和客户会话
        getKefuService();

        //开启推送会话
        Intent pushIntent = new Intent(MainActivity.this, PushService.class);
        pushIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startService(pushIntent);

        setTabSelection(0);

        mySharedPreferences = getSharedPreferences(
                "kefu", Activity.MODE_PRIVATE);

        dataSharedPreferences = getSharedPreferences(AuthUserInfo.getMyCid() + "kefu",
                Activity.MODE_PRIVATE);
        advertSharedPreferences = getSharedPreferences("viewpager",
                Activity.MODE_PRIVATE);

        //判断是否点击过广告
        Intent intent = getIntent();
        String infourl = intent.getStringExtra("infourl");
        if (!TextUtils.isEmpty(infourl)) {
            if (!TextUtils.isEmpty(AuthUserInfo.getMyCid()) && !TextUtils.isEmpty(AuthUserInfo.getMyToken())) {
                infourl = infourl + "?personid=" + AuthUserInfo.getMyCid() + "&skey=" + AuthUserInfo.getMyToken();
                ItemStock entity = new ItemStock();
                entity.setUrl(infourl);
                Intent intentWeb = new Intent(MainActivity.this, WebActivity.class);
                intentWeb.putExtra("entity", entity);
                intentWeb.putExtra("ifFromAd", true);
                startActivity(intentWeb);
                CompassApp.addStatis(CompassApp.GLOBAL.mgr.ADVERT_START, "1", "", System.currentTimeMillis());
            }

        }

        //用户提出
        IntentFilter filterLogout = new IntentFilter();
        filterLogout.addAction("ez08.auth.forcelogout");
        EzNet.regMessageHandler(responseReceiver, filterLogout);

        //客服消息，有新消息刷新dot
        IntentFilter filter2 = new IntentFilter();
        filter2.addAction(MessageService.ACTION_NOTIFY_KEFU);
        registerReceiver(dataChangeReceiver, filter2);

        //捕获异常并上传
        SharedPreferences sp = mContext.getSharedPreferences("exceptionsp", 0);
        boolean exceptionSwitch = sp.getBoolean("switch", false);
        long time = sp.getLong("time", 0);
        if (exceptionSwitch && System.currentTimeMillis() - time > 1000 * 60 * 60 * 24 * 7) {
            SharedPreferences.Editor editor = sp
                    .edit();
            editor.putBoolean("switch", false);
            editor.putLong("time", 0);
            editor.commit();
        }
        String exception = sp.getString("exception", "");
        if (!TextUtils.isEmpty(exception)) {
            NetInterface.setLogException(mHandler, 102, exception);
            SharedPreferences.Editor editor = sp
                    .edit();
            editor.putString("exception", "");
            editor.commit();
        }


        String newsUrl = intent.getStringExtra("news_url");
        if (newsUrl != null) {
            Intent newsIntent = new Intent(mContext, WebActivity.class);
            ItemStock itemStock = new ItemStock();
            itemStock.setTitle("来自浏览器");
            itemStock.setName("来自新闻");
            itemStock.setUrl(newsUrl);
            newsIntent.putExtra("entity", itemStock);
            startActivity(newsIntent);
        }
    }

    private IMDBHelper helper;
    private Cursor mCursor;

    private void refreshDot() {
        boolean hasDot = false;
        helper = IMDBHelper.getInstance(this);
        mCursor = CreateCursor();
        mCursor.moveToLast();
        if (mCursor.getCount() > 0) {
            String lastText = mCursor.getString(mCursor.getColumnIndex("msg"));
            String imageurl = mCursor.getString(mCursor.getColumnIndex("imageurl"));
            String lastTime = mCursor.getString(mCursor.getColumnIndex("localTime"));
            String lLastData = dataSharedPreferences.getString("lastData", "");
            if (!TextUtils.equals(lastText + lastTime + imageurl, lLastData)) {
                hasDot = true;
            }
            List<PushMessageEntity> list = initMessageData();
            int count = 0;
            for (int i = 0; i < list.size(); i++) {
                PushMessageEntity pushMessage = list.get(i);
                if (!pushMessage.ishasfind()) {
                    count++;
                }
            }
            if (count != 0) {
                hasDot = true;
            }
            if (hasDot) {
                iv_red_dot.setVisibility(View.VISIBLE);
            } else {
                iv_red_dot.setVisibility(View.GONE);
            }
        } else {
            iv_red_dot.setVisibility(View.GONE);
        }
    }

    private List<PushMessageEntity> initMessageData() {
        JSONArray array = null;
        List list = new ArrayList<PushMessageEntity>();
        SharedPreferences preferences = getSharedPreferences("pushlist&" + AuthUserInfo.getMyCid(),
                Activity.MODE_PRIVATE);
        String str = preferences.getString("list", "");
        if (TextUtils.isEmpty(str)) {
            array = new JSONArray();
        } else {
            try {
                array = new JSONArray(str);
                if (array != null && array.length() > 0) {
                    for (int i = array.length() - 1; i >= 0; i--) {
                        PushMessageEntity pushMessage = new PushMessageEntity();
                        JSONObject jsonObject = (JSONObject) array.get(i);
                        pushMessage.setPushid(jsonObject.getString("pushid"));
                        pushMessage.setTitle(jsonObject.getString("title"));
                        pushMessage.setDescription(jsonObject.getString("description"));
                        pushMessage.setImgurl(jsonObject.getString("imgurl"));
                        pushMessage.setUri(jsonObject.getString("uri"));
                        pushMessage.setPushtype(jsonObject.getString("pushtype"));
                        pushMessage.setUsertype(jsonObject.getString("usertype"));
                        pushMessage.setTime(jsonObject.getString("time"));
                        pushMessage.setStarttime(jsonObject.getString("starttime"));
                        pushMessage.setEndtime(jsonObject.getString("endtime"));
                        pushMessage.setIshasfind(jsonObject.getBoolean("ifhasfind"));
                        pushMessage.setReceivertime(jsonObject.getString("receivertime"));
                        list.add(pushMessage);
                    }
                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return list;
    }

    private Cursor CreateCursor() {
        return helper.getConversationTableCursor(CompassApp.GLOBAL.KEFU_ID);
    }

    private BroadcastReceiver refreshReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            finish();
            Intent main = new Intent(MainActivity.this,
                    MainActivity.class);
            startActivity(main);
        }
    };

//    /**
//     * 模拟数据
//     */
//    private void initData() {
//        JSONArray array = null;
//        list = new ArrayList<PushMessageEntity>();
//        unselectlist = new ArrayList<PushMessageEntity>();
//        SharedPreferences preferences = AppUtils.getSharedPrefCerences(this);
//        String str = preferences.getString("list", "");
//        if (TextUtils.isEmpty(str)) {
//            array = new JSONArray();
//        } else {
//            try {
//                array = new JSONArray(str);
//                if (array != null && array.length() > 0) {
//                    for (int i = array.length() - 1; i >= 0; i--) {
//                        PushMessageEntity pushMessage = new PushMessageEntity();
//                        JSONObject jsonObject = (JSONObject) array.get(i);
//                        pushMessage.setPushid(jsonObject.getString("pushid"));
//                        pushMessage.setTitle(jsonObject.getString("title"));
//                        pushMessage.setDescription(jsonObject
//                                .getString("description"));
//                        pushMessage.setImgurl(jsonObject.getString("imgurl"));
//                        pushMessage.setUri(jsonObject.getString("uri"));
//                        pushMessage.setPushtype(jsonObject
//                                .getString("pushtype"));
//                        pushMessage.setUsertype(jsonObject
//                                .getString("usertype"));
//                        pushMessage.setTime(jsonObject.getString("time"));
//                        pushMessage.setStarttime(jsonObject
//                                .getString("starttime"));
//                        pushMessage.setEndtime(jsonObject.getString("endtime"));
//                        pushMessage.setIshasfind(jsonObject
//                                .getBoolean("ifhasfind"));
//                        pushMessage.setReceivertime(jsonObject
//                                .getString("receivertime"));
//                        list.add(pushMessage);
//                    }
//                }
//            } catch (JSONException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
//        }
//    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
//        if (mCurrentSelect == 0) {
//            mHomeFragment.setVisible(true);
//        }
//        if (mCurrentSelect == 3) {
//            mSeekFragment.refreshTalk();
//        }
//        if (forceLogout) {
//            forceLogout = false;
//            new LogoutDialog().showDialog(MainActivity.this, forceMsg);
//        }
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        unregisterReceiver(dataChangeReceiver);
        unregisterReceiver(refreshReceiver);
        unregisterReceiver(delLiveReceiver);
//
//        IS_ALIVE = false;
        Intent intent = new Intent();
        intent.setAction(MessageService.ACTION_SERVICE_STOP);
        this.sendBroadcast(intent);
        Intent intent1 = new Intent(this, MessageService.class);
        this.stopService(intent1);

        new Thread(new Runnable() {
            @Override
            public void run() {
                AuthModule.stopNet();
            }
        }).start();
//        unregisterReceiver(delLiveReceiver);
//        unregisterReceiver(finishReceiver);
    }

    private NetResponseHandler responseReceiver = new NetResponseHandler() {
        @Override
        public void receive(EzMessage ezMessage) {
            Intent intent = IntentTools.messageToIntent(ezMessage);
            String msg = intent.getStringExtra("msg");
            Message message = Message.obtain();
            message.what = 0;
            message.obj = msg;
            msgHandler.sendMessage(message);
        }
    };

    private void getKefuService() {
        NetInterface.requestKefuInfo(mHandler, WHAT_GET_KEFU_INFO);
    }


    @SuppressLint("HandlerLeak")
    private NetResponseHandler2 mHandler = new NetResponseHandler2() {
        @Override
        public void netConnectLost(int arg0) {
        }

        @Override
        public void timeout(int arg0) {
        }


        @Override
        public void receive(int arg0, boolean arg1, Intent arg2) {
            // TODO Auto-generated method stub
            switch (arg0) {
                case WHAT_REQUEST_GET_MY_INFO:
                    if (arg2 != null) {
                        String cid = arg2.getStringExtra("cid");
                        if (TextUtils.isEmpty(cid) || cid.contains("T-")) {
                            return;
                        }

                        AuthUserInfo.modifyUserInfo(arg2.getExtras());
                        int sl = arg2.getIntExtra("level", -1);
                        SharedPreferences.Editor editor = mySharedPreferences
                                .edit();
                        editor.putInt("compass_level", sl);
                        String auths = arg2.getStringExtra("auths");
                        if (TextUtils.isEmpty(CompassApp.GLOBAL.CUSTOMER_PAYED_LEVEL)) {
                            CompassApp.GLOBAL.CUSTOMER_LEVEL = sl;
                        }
                        CompassApp.GLOBAL.CUSTOMER_AUTHS = auths;
                        //确认auth
                        AuthTool.initType(CompassApp.GLOBAL.CUSTOMER_LEVEL, CompassApp.GLOBAL.CUSTOMER_AUTHS);
                        IndexSortManager.init(mContext);

                        CompassApp.GLOBAL.HAS_LEVEL2 = false;
                        if (!TextUtils.isEmpty(auths) && CompassApp.GLOBAL.level2ID != null) {
                            for (int i = 0; i < CompassApp.GLOBAL.level2ID.length; i++) {
                                if (auths.contains(CompassApp.GLOBAL.level2ID[i])) {
                                    CompassApp.GLOBAL.HAS_LEVEL2 = true;
                                    break;
                                }
                            }
                        }
                        editor.putString("auths", auths);
                        editor.commit();
                        mHomeFragment.setHomeLevel(); //设置3看榜visible

                        if (getIntent() != null && getIntent().getBooleanExtra("theme", false)) {
                            return;
                        }

                        int bindType = arg2.getIntExtra("needbindmobile", 0);
                        String content = arg2.getStringExtra("bindmobiletips");
                        if (bindType != 0) {
                            bindDialog = new BindMobileDialog(mContext, bindType, content);
                            bindDialog.setBindListener(new BindMobileDialog.BindListener() {
                                @Override
                                public void sendVerifyCode(String mobile, BindMobileDialog.VerifyCode verifyCode) {
                                    verifyDialogInovke = verifyCode;
                                    NetInterface.bindSendCode(mHandler, GET_KH_SEND_CODE, mobile);
                                }

                                @Override
                                public void bindMobile(String mobile, String verity, BindMobileDialog.Bind bind) {
                                    bindDialogInovke = bind;
                                    NetInterface.bindVerifyCode(mHandler, GET_KH_VERIFY_CODE, mobile, verity);
                                }

                                @Override
                                public void cancel(boolean isForce) {
                                    if (!isForce) {
                                        bindDialog.dismiss();
                                    } else {
                                        finish();
                                    }
                                }
                            });
                            bindDialog.show();
                        }
                    }
                    break;
                case WHAT_GET_KEFU_INFO:
                    if (arg2 != null) {
                        String uid = arg2.getStringExtra("uid");
                        String name = arg2.getStringExtra("name");
                        String qq = arg2.getStringExtra("qq");
                        if (name != null) {
                            name = URLDecoder.decode(name);
                        }
                        String praiserate = arg2.getStringExtra("praiserate");
                        String level = arg2.getStringExtra("level");
                        String phone = arg2.getStringExtra("phone");

                        SharedPreferences.Editor editor = mySharedPreferences
                                .edit();
                        editor.putString("uid", uid);
                        editor.putString("name", name);
                        editor.putString("praiserate", praiserate);
                        editor.putString("level", level);
                        editor.putString("phone", phone);
                        editor.putString("qq", qq);
                        editor.commit();
                    }

                    CompassApp.GLOBAL.KEFU_ID = mySharedPreferences.getString("uid", "");
                    //客服 直播room start
                    Intent intent = new Intent(MainActivity.this,
                            MessageService.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startService(intent);

                    SharedPreferences pushHomePreferences = getSharedPreferences(
                            "homepageInfo", 0);
                    Boolean ifShowNotification = pushHomePreferences.getBoolean(
                            "ifShowNotification", false);
                    if (ifShowNotification) {
                        PushManager.pushManager(MainActivity.this, true);
                    }

                    SharedPreferences pushMessagePreferences = getSharedPreferences(
                            "pushInfo", 0);
                    Boolean ifGoto = pushMessagePreferences.getBoolean(
                            "ifShowNotification", false);
                    if (ifGoto) {
                        PushManager.pushManager(MainActivity.this, false);
                    }
                    refreshDot();
                    break;
                case WHAT_DEL_LIVING:
                    break;
                    //exp upload
                case 102:
                    break;
                case GET_KH_SEND_CODE:
                    if (arg2 != null) {
                        if (arg2.getBooleanExtra("result", false)) {
                            ToastUtils.show(MainActivity.this, "验证码已发送");
                            if (bindDialog != null && bindDialog.isShowing() && verifyDialogInovke != null) {
                                verifyDialogInovke.finish();
                            }
                        } else {
                            String msg = "网络异常";
                            if (arg2.getStringExtra("msg") != null) {
                                msg = arg2.getStringExtra("msg");
                            }
                            ToastUtils.show(MainActivity.this, msg);
                        }
                    }
                    break;
                case GET_KH_VERIFY_CODE:
                    if (arg2 != null) {
                        if (arg2.getBooleanExtra("result", false)) {
                            ToastUtils.show(MainActivity.this, "绑定成功");
                            if (bindDialog != null && bindDialog.isShowing() && bindDialogInovke != null) {
                                bindDialogInovke.finish();
                            }
                        } else {
                            String msg = "网络异常";
                            if (arg2.getStringExtra("msg") != null) {
                                msg = arg2.getStringExtra("msg");
                            }
                            ToastUtils.show(MainActivity.this, msg);
                        }
                    }
                    break;
                default:
                    break;

            }
        }

    };

    private BroadcastReceiver dataChangeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            refreshDot();
        }
    };

    private void initViews() {

        if (CompassApp.GLOBAL.THEME_STYLE == 0) {
            tab1 = R.drawable.tab1_day_normal;
            tab2 = R.drawable.tab2_day_normal;
            tab3 = R.drawable.tab3_day_normal;
            tab4 = R.drawable.tab4_day_normal;
            tab5 = R.drawable.tab5_day_normal;
            colorNormal = Color.parseColor("#282828");
        } else {
            tab1 = R.drawable.tab1_night_normal;
            tab2 = R.drawable.tab2_night_normal;
            tab3 = R.drawable.tab3_night_normal;
            tab4 = R.drawable.tab4_night_normal;
            tab5 = R.drawable.tab5_night_normal;
            colorNormal = Color.parseColor("#BFBFBF");
        }
        colorSelected = Color.parseColor("#FB0E1B");
        layout_01 = (LinearLayout) findViewById(R.id.home_page);
        layout_02 = (LinearLayout) findViewById(R.id.my_service);
        layout_03 = (LinearLayout) findViewById(R.id.my_class);
        layout_04 = (RelativeLayout) findViewById(R.id.seek);
        layout_06 = (LinearLayout) findViewById(R.id.trade);

        layout_01.setOnClickListener(this);
        layout_02.setOnClickListener(this);
        layout_03.setOnClickListener(this);
        layout_04.setOnClickListener(this);
        layout_06.setOnClickListener(this);

        mTabBar = (LinearLayout) findViewById(R.id.main_tab_bar);
        txtHome = (TextView) findViewById(R.id.textView1);
        txtService = (TextView) findViewById(R.id.textView2);
        txtClass = (TextView) findViewById(R.id.textView3);
        txtSeek = (TextView) findViewById(R.id.textView4);
        txtTrade = (TextView) findViewById(R.id.textView5);

        imgHome = (ImageView) findViewById(R.id.img_home_page);
        imgClass = (ImageView) findViewById(R.id.img_class);
        imgService = (ImageView) findViewById(R.id.img_my_service);
        imgSeek = (ImageView) findViewById(R.id.img_seek);
        imgTrade = (ImageView) findViewById(R.id.img_trade);
        iv_red_dot = (ImageView) findViewById(R.id.iv_red_dot);

        clearSelection();

        txtHome.setTextColor(colorSelected);
        txtService.setTextColor(colorNormal);
        txtClass.setTextColor(colorNormal);
        txtSeek.setTextColor(colorNormal);
        txtTrade.setTextColor(colorNormal);

        imgHome.setImageResource(R.drawable.tab1_select);
    }

    public void setTabSelection(int index) {
        if (index == mCurrentSelect) {
            return;
        }
        clearSelection();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        hideFragments(transaction);
        // transaction.commit();
        switch (index) {
            case 0:
                imgHome.setImageResource(R.drawable.tab1_select);
                txtHome.setTextColor(colorSelected);
                if (mHomeFragment == null) {
                    mHomeFragment = new HomeTabFragment();
                    transaction.add(R.id.tab_content, mHomeFragment);
                } else {
                    mHomeFragment.setCurrentPage();
                }
//                mHomeFragment.setVisible(true);
                transaction.show(mHomeFragment);

                break;
            case 1:
//                mHomeFragment.setVisible(false);
                imgService.setImageResource(R.drawable.tab2_select);
                txtService.setTextColor(colorSelected);
                if (mInfoTabFragment == null) {
                    mInfoTabFragment = new InfoNewTabFragment();
                    mInfoTabFragment.setCallBack(new InfoNewTabFragment.callBack() {
                        @Override
                        public void setInfoCallBack(boolean mBarStatus) {
                            if (!mBarStatus) {
                                mTabBar.setVisibility(View.GONE);
                            } else {
                                mTabBar.setVisibility(View.VISIBLE);
                            }
                        }
                    });
                    transaction.add(R.id.tab_content, mInfoTabFragment);
                } else {
                    mInfoTabFragment.getCurrentName();
                }
                transaction.show(mInfoTabFragment);

                break;
            case 2:
//                mHomeFragment.setVisible(false);
                imgClass.setImageResource(R.drawable.tab3_select);
                txtClass.setTextColor(colorSelected);
                if (mClassFragment == null) {
                    mClassFragment = new ClassFragment();
                    transaction.add(R.id.tab_content, mClassFragment);
                } else {
                    if (mClassFragment.getCurPosition() == 0) {
                        CompassApp.addStatis(CompassApp.GLOBAL.mgr.LIVE_ROOMLIST, "0", "",
                                System.currentTimeMillis());
                    } else {
                        CompassApp.addStatis(CompassApp.GLOBAL.mgr.LIVE_VIDEOLIST, "0", "",
                                System.currentTimeMillis());
                    }
                }
                transaction.show(mClassFragment);
                mClassFragment.refreshClassData();

                break;
            case 3:
                CompassApp.addStatis(CompassApp.GLOBAL.mgr.KEFU_MAIN, "0", "",
                        System.currentTimeMillis());
//                mHomeFragment.setVisible(false);
                imgSeek.setImageResource(R.drawable.tab5_select);
                txtSeek.setTextColor(colorSelected);
                if (mSeekFragment == null) {
                    mSeekFragment = new MyKefuFragment();
                    transaction.add(R.id.tab_content, mSeekFragment);
                    mSeekFragment.setCallBack(new MyKefuFragment.CallBack() {
                        @Override
                        public void hasRedDot(boolean dot) {
                            if (dot) {
                                iv_red_dot.setVisibility(View.VISIBLE);
                            } else {
                                iv_red_dot.setVisibility(View.GONE);
                            }
                        }
                    });
                }
                transaction.show(mSeekFragment);
//                mSeekFragment.refreshTalk();
                break;
            case 4:
                CompassApp.addStatis(CompassApp.GLOBAL.mgr.TRADE_LIST, "0", "", System.currentTimeMillis());
//                mHomeFragment.setVisible(false);
                imgTrade.setImageResource(R.drawable.tab4_select);
                txtTrade.setTextColor(colorSelected);
                if (mSecurityFragment == null) {
                    mSecurityFragment = new SecurityFragment();
                    transaction.add(R.id.tab_content, mSecurityFragment);
                }
                transaction.show(mSecurityFragment);
                break;
        }
        transaction.commitAllowingStateLoss();
        mCurrentSelect = index;
    }

    /**
     * ???????е????????
     */
    int tab1;
    int tab2;
    int tab3;
    int tab4;
    int tab5;

    private void clearSelection() {

        imgHome.setImageResource(tab1);
        imgService.setImageResource(tab2);
        imgClass.setImageResource(tab3);
        imgTrade.setImageResource(tab4);
        imgSeek.setImageResource(tab5);


        txtHome.setTextColor(colorNormal);
        txtClass.setTextColor(colorNormal);
        txtTrade.setTextColor(colorNormal);
        txtService.setTextColor(colorNormal);
        txtSeek.setTextColor(colorNormal);
    }

    private void hideFragments(FragmentTransaction transaction) {
        if (mHomeFragment != null) {
            transaction.hide(mHomeFragment);
        }
        if (mSeekFragment != null) {
            transaction.hide(mSeekFragment);
        }
        if (mInfoTabFragment != null) {
            transaction.hide(mInfoTabFragment);
        }
        if (mClassFragment != null) {
            transaction.hide(mClassFragment);
        }
        if (mSecurityFragment != null) {
            transaction.hide(mSecurityFragment);
        }
    }


    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.home_page:
                setTabSelection(0);
                break;
            case R.id.my_service:
                setTabSelection(1);
                break;
            case R.id.my_class:
                setTabSelection(2);
                break;
            case R.id.seek:
                setTabSelection(3);
                break;
            case R.id.trade:
                setTabSelection(4);
                break;
            default:
                break;
        }
    }

    private BroadcastReceiver delLiveReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context arg0, Intent arg1) {
            // TODO Auto-generated method stub
            String id = arg1.getStringExtra("id");
            NetInterface.requestDelLiving(mHandler, WHAT_DEL_LIVING, id);
        }

    };

    private long exitTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN) {

            //自选股恢复默认
            if (mHomeFragment.getSortStatus()) {
                mHomeFragment.setSortStatus();
                return true;
            }
            //新闻收回菜单
            if (mInfoTabFragment != null) {
                boolean status = mInfoTabFragment.setBarStatus();
                if (status) {
                    return true;
                }
            }
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(getApplicationContext(), "再按一次退出程序",
                        Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                MobclickAgent.onKillProcess(MainActivity.this);
                File[] files = getCacheDir().listFiles();
                for (File f : files) {
                    f.delete();
                }
                DataCleanManager.cleanInternalCache(mContext);
                DataCleanManager.cleanExternalCache(mContext);
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /** attention to this below ,must add this **/
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    //统计的回调
    @Override
    public void StatisticsCall(boolean success, long time) {
        if (success) {
            CompassApp.GLOBAL.mUpLoadSwitch = true;
            CompassApp.GLOBAL.mgr.deleteOldStatis(time);
        }
    }

    @SuppressLint("HandlerLeak")
    private Handler msgHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    //登出处理
//                    if (logout) {
//                        return;
//                    }
//                    logout = true;
//                    String message = (String) msg.obj;
//                    if (BaseActivity.IS_ONRESUME
//                            || MainActivity.IS_ONRESUME || BaseFragmentLActivity.IS_ONRESUME) {
//                        Intent intent = new Intent(MainActivity.this, MainActivity.class);
//                        intent.putExtra("force_message", message);
//                        startActivity(intent);
//                    } else {
//                        forceLogout = true;
//                        forceMsg = message;
//                        NotificationManager nm = (NotificationManager) MainActivity.this
//                                .getSystemService(Context.NOTIFICATION_SERVICE);
//
//                        NotificationCompat.Builder builder = new NotificationCompat.Builder(
//                                MainActivity.this)
//                                .setSmallIcon(R.drawable.logo_)
//                                .setContentTitle("登出提示").setTicker("登出提示");
//                        builder.setAutoCancel(true);
//
//                        builder.setDefaults(Notification.DEFAULT_SOUND);
//                        builder.setContentText(message);
//
//                        Intent talkIntent = new Intent();
//                        talkIntent.setAction("force_logout");
//                        talkIntent.setClassName("com.ez08.compass",
//                                "com.ez08.compass.activity.HandleMessageActivity");
//                        PendingIntent pi = PendingIntent.getActivity(
//                                MainActivity.this, 0, talkIntent,
//                                PendingIntent.FLAG_UPDATE_CURRENT);
//                        builder.setContentIntent(pi);
//                        nm.notify(11, builder.build());
//                    }
                    break;
            }
        }
    };

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (mlDialog != null && mlDialog.isShowing()) {
            return;
        }
        String message = intent.getStringExtra("force_message");
        if (TextUtils.isEmpty(message)) {
            return;
        }
        AlertDialog.Builder normalDialog =
                new AlertDialog.Builder(MainActivity.this);
        normalDialog.setTitle("登出提示");
        normalDialog.setMessage(message);
        normalDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        AuthUserInfo.clearUserInfo();
                        AuthModule.logout();
                        AdsManager.getInstance(mContext).clearCid();
                        Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(i);
                    }
                });
        // 显示
        mlDialog = normalDialog.create();
        mlDialog.setCancelable(false);
        mlDialog.show();
    }

    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    private static final int REQUEST_EXTERNAL_STORAGE = 1;

    public void myPermission() {
        int permission = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    this,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }
}
