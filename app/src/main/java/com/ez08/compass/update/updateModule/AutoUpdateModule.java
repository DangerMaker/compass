package com.ez08.compass.update.updateModule;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;

import com.ez08.support.net.EzMessage;
import com.ez08.support.net.EzNet;
import com.ez08.support.net.NetResponseHandler;
import com.ez08.tools.IntentTools;

public class AutoUpdateModule {
	private static String AUTO_TAG = "AutoUpdate";
	private static Context auto_context;
	private static SharedPreferences auto_spf;
	private static Activity updateActivity;
	
	private static Context mContext;
	
	private static final Handler handler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			Intent intent1=(Intent)msg.obj;
			auto_context.startActivity(intent1);
		};
	};
	
	public static void init(Context context) {
		mContext=context;
		setContetx(context);
		auto_spf = PreferenceManager.getDefaultSharedPreferences(context);
		registerUpdateReciver();
	}

	/**
	 * 注册升级广播
	 */
	private static void registerUpdateReciver() {
		Log.e(AUTO_TAG, "注册升级广播");
		/**
		 * 升级
		 */
		IntentFilter filter = new IntentFilter();
		filter.addAction("ez08.sys.softupdate");
		filter.setPriority(Integer.MAX_VALUE - 2);
		EzNet.regMessageHandler(updateReceiver, filter);
		
		IntentFilter myIntent = new IntentFilter();
		myIntent.addAction("ez08.sys.softupdate");
		mContext.registerReceiver(mBroadcastReceiver, myIntent);
		
	}
	private static BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action.equals("ez08.sys.softupdate")) {
		 		final int type = intent.getIntExtra("type", 0);
				final String caburl = intent.getStringExtra("caburl");
				final String brief = intent.getStringExtra("brief");
				final String tver = intent.getStringExtra("tver");
				final AutoUpdatePacket up = new AutoUpdatePacket();
				up.setType(type);
				up.setCaburl(caburl);
				up.setBrief(brief);
				up.setTver(tver);
				SharedPreferences.Editor editor = auto_spf.edit();
				editor.putString("caburl", caburl);
				editor.putInt("type", type);
				editor.putString("brief", brief);
				editor.putString("tver", tver);
				editor.commit();
				Intent intent1 = new Intent(auto_context, AutoUpdateActivity.class);
				intent1.putExtra("up", up);
				intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				
				Message msg=Message.obtain();
				msg.obj=intent1;
				handler.sendMessageDelayed(msg, 10000);
			}
		}

	};
	/**
	 * 设置句柄
	 * 
	 * @param app_context
	 */
	private static void setContetx(Context app_context) {
		Log.e(AUTO_TAG, "设置自动升级句柄");
		auto_context = app_context;
	}

	/**
	 * 获取句柄
	 * 
	 * @return
	 */
	public static Context getContext() {

		return auto_context;
	}

	/**
	 * 升级推送
	 */
	private static NetResponseHandler updateReceiver = new NetResponseHandler() {

		@Override
		public void receive(EzMessage message) {
			Log.e(AUTO_TAG,
					"=========================自动升级================================");
			if ("ez08.sys.softupdate".equalsIgnoreCase(message.getKVData(
					"action").getString())) {

				Intent intent = IntentTools.messageToIntent(message);
				
				
				final int type = intent.getIntExtra("type", 0);
				final String caburl = intent.getStringExtra("caburl");
				final String brief = intent.getStringExtra("brief");
				final String tver = intent.getStringExtra("tver");
				final AutoUpdatePacket up = new AutoUpdatePacket();
				up.setType(type);
				up.setCaburl(caburl);
				up.setBrief(brief);
				up.setTver(tver);
				SharedPreferences.Editor editor = auto_spf.edit();
				editor.putString("caburl", caburl);
				editor.putInt("type", type);
				editor.putString("brief", brief);
				editor.putString("tver", tver);
				editor.commit();
				Intent intent1 = new Intent(auto_context, AutoUpdateActivity.class);
				intent1.putExtra("up", up);
				intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				
				Message msg=Message.obtain();
				msg.obj=intent1;
				handler.sendMessageDelayed(msg, 10000);
				// if (auto_context != null) {
				// ((Activity) auto_context).runOnUiThread(new Runnable() {
				// @Override
				// public void run() {
				// UpdateManager.getInstance().checkUpdate(up);
				// }
				// });
				//
				// }

			}
		}
	};

	public static void setUpdateActivity(Activity curActivity) {
			updateActivity = curActivity;
	}

	public static Activity getUpdateActivity() {
		return updateActivity;
	}
}
