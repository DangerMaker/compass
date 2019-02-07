//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.ez08.support.net;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;

import com.ez08.support.util.EzCoder;
import com.ez08.tools.EzLog;
import com.ez08.tools.IntentTools;
import com.ez08.tools.MacAddrManager;

import java.net.InetSocketAddress;
import java.util.Timer;
import java.util.TimerTask;

public class NetManager {
	private static final String NetMangerVersion = "1.0";
	private static final boolean D = true;
	private static final String tag = "NetManager";
	public static String mDefaultIp = "211.151.60.5";
	public static int mDefailtPort = 6930;
	public static Boolean NET_DEBUG = Boolean.valueOf(true);
	public static Boolean NET_DEBUG_DETAIL = Boolean.valueOf(false);
	static Application application;
	private static long mLastTimeConnectNet = 0L;
	private static NetTimeTask mTimerTask;
	public static final String EZ_NET_STATE_CHANGE_BROADCAST = "ez08.net.state.change.broadcast";
	static String mAppName = null;
	static String mAppId = null;
	static Handler mNoHandlerPakagersHandler = null;
	private static int mReconnectCounter = 0;
	static int mEncType;
	static String[] mEncNames = null;
	public static String mTid = null;
	public static String mToken = null;
	public static String mCid = null;
	public static String mChid = null;
	private static boolean NetStarted = false;
	public static final String ACTION_AUTH_CONNECT = "ez08.auth.connect";
	public static final String ACTION_AUTH_LOGIN = "ez08.auth.login";
	public static final String ACTION_AUTH_LOGOUT = "ez08.auth.logout";
	public static final String ACTION_AUTH_LOGIN_RESPONSE = "ez08.auth.login.response";
	public static final String ACTION_AUTH_LOGOUT_RESPONSE = "ez08.auth.logout.response";
	public static final String ACTION_AUTH_PRE_REGISTER = "ez08.auth.preregister";
	public static final String ACTION_AUTH_PRE_REGISTER_RESPONSE = "ez08.auth.preregister.response";
	public static final String ACTION_AUTH_REG_EDIT = "ez08.auth.regedit";
	public static final String ACTION_HEART_BEAT = "ez08.auth.heartbeat";
	public static final String ACTION_HEART_BEAT_RESPONSE = "ez08.auth.heartbeat.response";
	public static final String ACTION_DATA_RESPONSE = "ez08.data.response";
	public static final String ACTION_GET_VCODE_RESPONSE = "ez08.auth.getvcode.response";
	public static final String ACTION_GET_VCODE = "ez08.auth.getvcode";
	public static final String ACTION_LOGON_BY_SMS = "ez08.auth.logonBySMS";
	public static final String ACTION_AUTH_RECONNECT = "ez08.auth.reconnect";
	private static long mLastReceiveTime = 0L;
	private static long mLastOperateTime = 0L;
	public static final long TIMEOUT = 100000L;
	public static final long NET_RECONNECT_TIMEOUT = 300000L;
	public static int mState = 0;
	public static final int STATE_NONE = 0;
	public static final int STATE_CONNECTING = 1;
	public static final int STATE_CONNECT = 2;
	private static EzSocket mEzSocket;
	public static EzCoder mEzCoder = new EzCoder(0);
	private static BroadcastReceiver netBroadcastReceiver = new BroadcastReceiver() {
		public synchronized void onReceive(Context context, Intent intent) {
			if ("android.net.conn.CONNECTIVITY_CHANGE".equalsIgnoreCase(intent.getAction()) && NetManager.NetStarted) {
				NetManager.wirelessStateChange();
			}

		}
	};
	private static NetworkInfo currentNet = null;
	private static Timer mTimer;

	public NetManager() {
	}

	public static void Init(Application app, String appname, String appid) {
		EzMessageFactory.initProto();
		mAppName = appname;
		mAppId = appid;
		application = app;
		IntentFilter filter = new IntentFilter();
		filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
		filter.setPriority(2147483647);
		application.registerReceiver(netBroadcastReceiver, filter);
		mEzCoder.generateLocalKey();
	}

	public static void setChid(String chid) {
		mChid = chid;
	}

	public static String getAppID() {
		return mAppId;
	}

	public static void setIpAndPort(String ip, int port) {
		mDefaultIp = ip;
		mDefailtPort = port;
	}

	public static void regNoHandlerPakagersHandler(Handler handler) {
		mNoHandlerPakagersHandler = handler;
	}

	public static boolean startNet(String tid, String cid, String token) {
		if (application != null && mDefaultIp != null && mDefailtPort != 0) {
			NetStarted = true;
			if (mTimerTask != null) {
				mTimerTask.cancel();
			}

			if(mTimer == null){
				mTimer = new Timer();
			}

			mTimerTask = new NetTimeTask();
			mTimer.scheduleAtFixedRate(mTimerTask, 1000L, 5000L);
			mTid = tid;
			mCid = cid;
			mToken = token;
			NetStarted = true;
			return tryConnect();
		} else {
			Log.i("NetManager", "������δ��ʼ�����޷������������ӣ�");
			return false;
		}
	}

	public static boolean startNet() {
		return tryConnect();
	}

	static void restartNet() {
		if (NetStarted) {
			tryConnect();
		}

	}

	public static void stopNet() {
		Log.i("NetManager", "��ʼ�ر�����....");
		NetStarted = false;
		mTimer.cancel();
		if (mEzSocket != null) {
			mEzSocket.stop();
		}
		mTimer = null;
		setState(0);
	}

	private static boolean tryConnect() {
		boolean ret = false;
		ConnectivityManager conMan = (ConnectivityManager) application.getSystemService("connectivity");
		NetworkInfo info = conMan.getActiveNetworkInfo();
		if (info == null) {
			Log.e("NetManager", "û�п��õ����磬�޷���������");
			if (mEzSocket != null) {
				mEzSocket.stop();
			}

			setState(0);
		} else {
			showNetworkInfo("connect() show netWork Info", info);
			currentNet = info;
			if (mEzSocket == null) {
				mEzSocket = new EzSocket();
			} else {
				mEzSocket.stop();
			}

			mEzSocket.connect(new InetSocketAddress(mDefaultIp, mDefailtPort));
			mLastTimeConnectNet = System.currentTimeMillis();
			setState(1);
			ret = true;
		}

		return ret;
	}

	static EzMessage getConnectMessage() {
		Intent intent = new Intent("ez08.auth.connect");
		String versionName = null;

		try {
			String wifi1 = application.getPackageName();
			versionName = application.getPackageManager().getPackageInfo(wifi1, 0).versionName;
		} catch (Exception var4) {
			;
		}

		intent.putExtra("version", versionName);
		intent.putExtra("os", "android");
		intent.putExtra("osVersion", Build.MODEL + "," + VERSION.RELEASE);
		intent.putExtra("NetMangerVersion", "1.0");
		intent.putExtra("appName", mAppName);
		intent.putExtra("appuid", mAppId);
		if (!TextUtils.isEmpty(mChid)) {
			intent.putExtra("chid", mChid);
		}

		if (mTid != null) {
			intent.putExtra("tid", mTid);
			intent.putExtra("cid", mCid);
			intent.putExtra("token", mToken);
		}

		intent.putExtra("wifiMacAddr", MacAddrManager.getMacAddr(application));

		intent.putExtra("pkeya", mEzCoder.getPublicKeyA());
		return IntentTools.intentToMessage(intent);
	}

	private static void setState(int state) {
		Intent bintent = new Intent("ez08.net.state.change.broadcast");
		bintent.putExtra("oldState", mState);
		bintent.putExtra("newState", state);
		if (mState != state) {
			mState = state;
			mLastTimeConnectNet = 0L;
			application.sendBroadcast(bintent);
		}
	}

	static void connectFail(String msg) {
		Log.i("NetManager", "�����������ӳ��ִ���" + msg);
		setState(0);
		EzNet.connectLost();
	}

	static void connectLost() {
		EzLog.e(true, "NetManager", "�������Ӷ�ʧ");
		setState(0);
		EzNet.connectLost();
	}

	static void resetLastNetOperateTime() {
		mLastOperateTime = System.currentTimeMillis();
	}

	static void resetLastReceiveTime() {
		mLastReceiveTime = System.currentTimeMillis();
		resetLastNetOperateTime();
	}

	static void connectMessageHandle(EzMessage message) {
		Intent intent = IntentTools.messageToIntent(message);
		String action = intent.getAction();
		if ("ez08.auth.connect".equalsIgnoreCase(action)) {
			EzLog.e(true, "NetManager", "�յ��������ְ�������������� action=" + action);
			
	        Intent bintent = new Intent("ez08.net.connect.judge.broadcast");
	        bintent.putExtra("cid", intent.getStringExtra("cid"));
	        application.sendBroadcast(bintent);
			
			connected(intent);
		}

		boolean result = intent.getBooleanExtra("result", false);
		if (result) {
			if ("ez08.auth.login.response".equalsIgnoreCase(action)) {
				EzLog.e(true, "NetManager", "�յ���¼Ӧ����������趨tid����Ϣ action = " + action);
				connected(intent);
			} else if ("ez08.auth.logout.response".equalsIgnoreCase(action)) {
				EzLog.e(true, "NetManager", "�յ��˳���¼Ӧ����������趨tid����Ϣ action = " + action);
				connected(intent);
			}
		}

	}

	static void connected(Intent intent) {
		mReconnectCounter = 0;
		mTid = intent.getStringExtra("tid");
		mCid = intent.getStringExtra("cid");
		mToken = intent.getStringExtra("token");
		EzLog.e(true, "NetManager", "��NetManger�����¼���ĵط��趨cid��Ϣtid= " + mTid + "   cid=" + mCid + "  token=" + mToken);
		byte[] pkeybytes = intent.getByteArrayExtra("pkeyb");
		String str = intent.getStringExtra("encnames");
		mEncNames = null;
		if (str != null) {
			mEncNames = str.split(",");
		}

		mEncType = intent.getIntExtra("enctype", 0);
		byte[] bytes = intent.getByteArrayExtra("rc4key");
		if (bytes != null) {
			try {
				byte[] e = mEzCoder.decodeRc4Key(pkeybytes, bytes);
				mEzCoder.setInfomation(mTid, (String) null, (String) null, pkeybytes, e, mEncNames);
			} catch (Exception var5) {
				var5.printStackTrace();
			}
		}

		if ("ez08.auth.connect".equalsIgnoreCase(intent.getAction())) {
			mEzSocket.startWriter();
		}

		setState(2);
	}

	public static boolean isNetworkAvilable() {
		ConnectivityManager conMan = (ConnectivityManager) application.getSystemService("connectivity");
		NetworkInfo info = conMan.getActiveNetworkInfo();
		return info != null;
	}

	private static void wirelessStateChange() {
		Log.i("NetManager", "����״̬�����仯");
		ConnectivityManager conMan = (ConnectivityManager) application.getSystemService("connectivity");
		NetworkInfo info = conMan.getActiveNetworkInfo();
		showNetworkInfo("wirelessStateChange() show netWork Info", info);
		mReconnectCounter = 0;
		if (info != null && info.isAvailable()) {
			if (mState == 0) {
				restartNet();
			}
		} else if (mState != 0 && mEzSocket != null) {
			Log.i("NetManager", "�Ѿ����������ӵ����������ò����ã��ر��Ѿ����ӵ�socket");
			mEzSocket.stop();
			setState(0);
		}

	}

	private static boolean isSameNetWork(NetworkInfo info) {
		return currentNet == info;
	}

	private static void showNetworkInfo(String text, NetworkInfo info) {
		if (NET_DEBUG.booleanValue() && NET_DEBUG_DETAIL.booleanValue()) {
			Log.i("NetManager", "==============" + text + "====================");
			if (info != null) {
				Log.i("NetManager", "info.describeContents() = " + info.describeContents());
				Log.i("NetManager", "info.getReason() = " + info.getReason());
				Log.i("NetManager", "info.getSubtypeName() = " + info.getSubtypeName());
				Log.i("NetManager", "info.getTypeName() = " + info.getTypeName());
				Log.i("NetManager", "info.toString() = " + info.toString());
				Log.i("NetManager", "info.getExtraInfo() = " + info.getExtraInfo());
				Log.i("NetManager", "info.getDetailedState() = " + info.getDetailedState());
				Log.i("NetManager", "info.getSubtype() = " + info.getSubtype());
				Log.i("NetManager", "info.getSubtype() = " + info.getType());
				Log.i("NetManager", "info.getSubtype() = " + info.getSubtype());
				Log.i("NetManager", "info.getState() = " + info.getState());
				Log.i("NetManager", "info.hashCode()= " + info.hashCode());
				Log.i("NetManager", "info.isAvailable() = " + info.isAvailable());
				Log.i("NetManager", "info.isConnected() = " + info.isConnected());
				Log.i("NetManager", "info.isConnectedOrConnecting() = " + info.isConnectedOrConnecting());
				Log.i("NetManager", "info.isFailover() = " + info.isFailover());
				Log.i("NetManager", "info.isRoaming() = " + info.isRoaming());
			}
		}
	}

	private static void checkNetConnect() {
		if (System.currentTimeMillis() - mLastReceiveTime > 300000L) {
			restartNet();
		}

	}

	static class NetTimeTask extends TimerTask {
		NetTimeTask() {
		}

		public void run() {
			EzNet.checkTimeOut();
			NetManager.checkNetConnect();
		}
	}
}
