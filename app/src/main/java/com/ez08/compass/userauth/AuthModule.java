package com.ez08.compass.userauth;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;

import com.ez08.support.net.EzMessage;
import com.ez08.support.net.EzNet;
import com.ez08.support.net.NetManager;
import com.ez08.support.net.NetResponseHandler;
import com.ez08.tools.EzLog;
import com.ez08.tools.IntentTools;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

public class AuthModule {
    private static String tag = "AuthModule";
    public static Boolean D = true;
    public static Context authContext;
    // 主程序包�?
    public static String packgeName;

    public static void startNet() {
        AuthUserInfo.restoreUserInfoBundle();
        final String tid = AuthUserInfo.getMyTid();
        final String cid = AuthUserInfo.getMyCid();
        final String token = AuthUserInfo.getMyToken();
        Log.e("token", "trouble=" + token);
        if (cid != null && !"".equalsIgnoreCase(cid)) {
            EzLog.i(D, tag, "已经登录成功过，使用登录成功的信息连接网�?...");
            EzLog.i(D, tag, "系统保存的用户信息：cid:" + cid + ", tid:" + tid
                    + ", token:" + token);
            new Thread() {
                public void run() {
                    NetManager.startNet(tid, cid, token);
                }

                ;
            }.start();
//			NetManager.startNet(tid, cid, token);
        } else {
            EzLog.e(D, tag, "从来没有连接成功过，使用手机wifi信息连接网络....");
            NetManager.startNet(null, null, null);
        }
    }

    public static void logout() {
        NetManager.mTid = null;
        NetManager.mToken = null;
        NetManager.mCid = null;
        AuthUserInfo.clearUserInfo();
        AuthUserInfo.setLogined(false);
        EzNet.Request(IntentTools.intentToMessage(new Intent(
                NetManager.ACTION_AUTH_LOGOUT)), null, 0, 2, 0);

    }

    /**
     * 用户认证模块初始�?
     *
     * @param context  主程序上下文
     * @param pageName 主程序包�?
     */
    public static void init(Context context, String pageName) {
        authContext = context;
        packgeName = pageName;
        SysVarsManager.setContext(context);
        AuthUserInfo.restoreUserInfoBundle();
        // 监听网络状�?的变�?
        IntentFilter filter = new IntentFilter(
                NetManager.EZ_NET_STATE_CHANGE_BROADCAST);
        authContext.registerReceiver(receiver, filter);

        // 监听用户状�?的变化以便当前用户信息进行处�?
        IntentFilter filter2 = new IntentFilter(NetManager.ACTION_AUTH_CONNECT);
        filter.addAction(NetManager.ACTION_AUTH_LOGIN_RESPONSE);
        filter.addAction(NetManager.ACTION_AUTH_LOGOUT_RESPONSE);
        EzNet.regMessageHandler(mNetResponseHandler, filter2);
    }

    public static void initImageLoader(Context context) {
        // This configuration tuning is custom. You can tune every option, you
        // may tune some of them,
        // or you can create default configuration by
        // ImageLoaderConfiguration.createDefault(this);
        // method.
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                context).threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.LIFO).build();

        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config);
    }

    private static NetResponseHandler mNetResponseHandler = new NetResponseHandler() {

        @Override
        public void receive(EzMessage message) {
            if (message == null)
                return;
            Intent intent = IntentTools.messageToIntent(message);
            String action = intent.getAction();
            boolean result = intent.getBooleanExtra("result", false);
            EzLog.i(D,
                    tag,
                    "用户信息发生变化。action:" + action + "  CID:"
                            + intent.getStringExtra("cid"));

            // 将用户信息保存在本地，不�?��在NetManger中保存，系统已经自动做过�?
            if (!AuthUserInfo.isLogined()) {
                UserAuthLoginSuccess(intent);
                /* 用户登陆成功之后，清空本地用户信息数据缓�?*/
                AuthDBHelper.recreateDB(authContext);
            }

        }

    };

    public static void UserAuthLoginSuccess(Intent intent) {
        EzLog.i(D, tag, "result=TRUE,保存用户信息");
        ContentValues cv = AuthUserService.getContentValues(intent);
        String cid = intent.getStringExtra("cid");
        String tid = intent.getStringExtra("tid");
        String token = intent.getStringExtra("token");

        boolean islogin = intent.getBooleanExtra("login", false);
        String action = intent.getAction();
        if (NetManager.ACTION_AUTH_LOGIN_RESPONSE.equalsIgnoreCase(action)) {
            islogin = true;
        } else if (NetManager.ACTION_AUTH_LOGOUT_RESPONSE
                .equalsIgnoreCase(action)) {
            islogin = false;
        }
        EzLog.i(D, tag, "用户登录状�?:" + islogin);
        Bundle bd = intent.getBundleExtra("config");
        cv.put("realname", intent.getStringExtra("realname"));
        if (bd != null) {

            cv.put("sex", bd.getInt("sex"));
//            cv.put("email", bd.getString("email"));
            cv.put("cfg_descript", bd.getString("cfg_descript"));
            AuthUserService.saveUserInfo(cid, cv);
        }
        Bundle info = intent.getExtras();
        if (bd != null) {
            info.putAll(bd);
        }
        info.putBoolean("login", islogin);
        info.putString("tid", tid);
        info.putString("realname", intent.getStringExtra("realname"));
        AuthUserInfo.setUserInfoBundle(info);

    }

    private static BroadcastReceiver receiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            switch (NetManager.mState) {
                case NetManager.STATE_CONNECT:

                    break;
            }
        }
    };
}
