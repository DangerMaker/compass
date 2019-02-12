package com.ez08.compass;

import android.app.ActivityManager;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.multidex.MultiDex;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.android.thinkive.framework.util.ScreenUtil;
import com.ez08.compass.update.updateModule.AutoUpdateModule;
import com.ez08.compass.database.DBManager;
import com.ez08.compass.entity.ItemStock;
import com.ez08.compass.entity.StockCodeEntity;
import com.ez08.compass.net.protocol.Protocol_UserInfo;
import com.ez08.compass.net.protocol.Protocol_ZhiNanTong;
import com.ez08.compass.net.protocol.StockFinMessageProtocol;
import com.ez08.compass.tools.UtilTools;
import com.ez08.compass.auth.AuthModule;
import com.ez08.support.net.EzMessageFactory;
import com.ez08.support.net.NetManager;
import com.ez08.tools.EzLog;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.socialize.PlatformConfig;

import java.util.ArrayList;
import java.util.List;

public class CompassApp extends Application {

    //常量
    public static class Constants {
        public static final String REQUEST_URL = "https://imapp.compass.cn/update.php?type=0&testmode=1"; //testmode = 0正式* 1测试* type = 0android 1ios
        public static final String MAINAPP_NAME = "zhinantong_app";
        public static final String APPUID = "zhinantong_android";
        //首页行情代码  上证 深证 创业板 中小板
        public static final String STOCK_VALUE_CODE = "SHHQ000001,SZHQ399001,SZHQ399006,SZHQ399005";
        public static final String VERSION = BuildConfig.VERSION_NAME;

    }

    //全局变量
    public static class GLOBAL {
        public static int SCREEN_W = 0;
        public static int SCREEN_H = 0;
        public static int THEME_STYLE = 0;    //0日间，1夜间
        public static int DEVELOPER_MODE = 0;    //0正常；1开发者模式
        public static boolean isActive = false; //app是否处于活跃状态

        //网络配置
        public static String IP = "";
        public static int PORT = 0;
        public static String ADVERT_URL = ""; //所有广告请求地址

        //用户信息
        public static String KEFU_ID = ""; //分配客服ID
        public static int CUSTOMER_LEVEL = -1; //用户等级
        public static String CUSTOMER_PAYED_LEVEL = "";
        public static String level2ID[];    //所有产品level2的id
        public static boolean HAS_LEVEL2 = false; //是否是level2的用户
        public static String CUSTOMER_AUTHS = "";  //用户已购买的产品
        public static String PAY_PERSONID = ""; //支付用户id
        public static String PAY_PWD = ""; //支付用户密码

        //直播
        public static boolean mIsInLiving = false;    //是否在直播
        public static String mLivingRoomId = "";    //直播中的房间id
        public static String mLivingRoomName = "";    //直播中的房间name

        //股票详情
        public static ArrayList<ItemStock> mTempList = new ArrayList<>();    //自选股列表
        public static List<StockCodeEntity> mStockList = new ArrayList<>();    //详情页的股票列表
        public static boolean isScroll = false;  //解决股票详情十字线与上下滑动冲突问题
        public static boolean isTouchFenshiView = false; //同上
        public static int mStockDetailType = -1;   //0，分时十档;1，日k；2，周k；3，月k；4-10：分钟k;
        public static int mStockFenshiType = 0;   //0，买卖档;1，成交明细
        public static int dividerHeight = 0; //股票详情中走势图与下边新闻的分界线高度，用于分享股票时拼接二维码
        public static boolean isDr = true; //默认除权

        //统计
        public static DBManager mgr;
        public static boolean mFirstInstall = true;  //首次安装，要实时传所有得操作
        public static boolean mUpLoadSwitch = false;   //上传统计的开关，默认打开，传输中关闭
        public static long mStatisTimes;  //上传传数据的时间

        public static int RED;
        public static int GREEN;
        public static int LIGHT_GRAY;
    }

    public static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
//        ThinkiveInitializer.getInstance().initialze(this); //第一创业sdk
        UMConfigure.init(this, "55a8a80f67e58ec0420006ef", "umeng", UMConfigure.DEVICE_TYPE_PHONE, "");
        PlatformConfig.setWeixin("wxae22025f248bd50f", "a11764469f0743a2474b4b3e12710909");
        PlatformConfig.setSinaWeibo("1679428760", "e36e4fe521da287ad503fa30d8c5439b", "http://sns.whalecloud.com");
        PlatformConfig.setQQZone("1104947532", "oIBgb6hWY6BoyhEx");
        GLOBAL.mgr = new DBManager(this);
        GLOBAL.SCREEN_W = (int)ScreenUtil.getScreenWidth(mContext);
        GLOBAL.SCREEN_H = (int)ScreenUtil.getScreenHeight(mContext);

        GLOBAL.RED = ContextCompat.getColor(this,R.color.red);
        GLOBAL.GREEN = ContextCompat.getColor(this,R.color.green);
        GLOBAL.LIGHT_GRAY = ContextCompat.getColor(this,R.color.shadow0);
    }

    public static Context getmContext() {
        return mContext;
    }

    public void init() {
        String str = getCurProcessName(this);
        if (str == null || !str.contains("com.ez08.compass")) {
            Log.e("tag", "进入 compassApp--》onCreate().....不需要初始化");
            return;
        }

        Log.e("", GLOBAL.IP + "=" + GLOBAL.PORT + "===");
        NetManager.NET_DEBUG = false;
        NetManager.NET_DEBUG_DETAIL = false;
        Log.e("nettt", "cid=" + NetManager.mCid + ";tid=" + NetManager.mTid + ";token=" + NetManager.mToken);
        String chid = UtilTools.getChannel(mContext);
        NetManager.setChid(chid);
        NetManager.Init(this, Constants.MAINAPP_NAME, Constants.APPUID);
        EzMessageFactory.AddMessageProto(Protocol_ZhiNanTong.proto_zhinantong);
        EzMessageFactory.AddMessageProto(Protocol_UserInfo.proto_userinfo);
        EzMessageFactory.AddMessageProto(StockFinMessageProtocol.proto_znz_stockfindata);
        NetManager.setIpAndPort(GLOBAL.IP, GLOBAL.PORT);
        AuthModule.initImageLoader(getApplicationContext());
        AuthModule.init(this, this.getPackageName());
        AutoUpdateModule.init(this);
        EzLog.TO_FILE = false;// 关闭日志

        IntentFilter filter = new IntentFilter();
        filter.addAction("ez08.net.connect.judge.broadcast");
        registerReceiver(connectReceiver, filter);
    }

    //加入埋点
    public static void addStatis(String action, String type, String params, long times) {
        if (GLOBAL.mgr == null) {
            return;
        }
        Log.e("statistics", action + "|" + type + "|" + params + "|" + times);
        GLOBAL.mgr.addData(action, type, params, times);
    }


    /**
     * @param context 获得程序名
     * @return
     */
    private String getCurProcessName(Context context) {
        int pid = android.os.Process.myPid();
        ActivityManager mActivityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo appProcess : mActivityManager
                .getRunningAppProcesses()) {
            if (appProcess.pid == pid) {

                return appProcess.processName;
            }
        }

        return null;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    private BroadcastReceiver connectReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
//                String action = intent.getStringExtra("ez08.net.connect.judge.broadcast");
//                String cid = intent.getStringExtra("cid");
//                if (!TextUtils.isEmpty(cid) && cid.contains("T-")) {
//                    Intent i = new Intent(getApplicationContext(), LoginActivity.class);
//                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    startActivity(i);
//                }
//                unregisterReceiver(this);
            }
        }
    };
}
