package com.ez08.compass.tools;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;

import com.ez08.compass.CompassApp;
import com.ez08.compass.database.IMDBHelper;
import com.ez08.support.net.EzMessage;
import com.ez08.support.net.EzNet;
import com.ez08.support.net.NetResponseHandler;
import com.ez08.tools.IntentTools;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MessageService extends Service {
	public static final String ACTION_SERVICE_STOP = "ez08.im.service.stop";
	public static final String ACTION_NOTIFY_UI = "im.action.message.received";
	public static final String ACTION_NOTIFY_KEFU = "im.action.kefu.received";
	public static final String ACTION_NOTIFY_ROOM = "im.action.message.received.room";
	// 回复
	public static final String ACTION_CS_MESSAGE = "ez08.znt.message";
	
	private IMDBHelper helper;


	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		helper = IMDBHelper.getInstance(MessageService.this);
		IntentFilter filter = new IntentFilter();
		filter.addAction(ACTION_CS_MESSAGE);
		EzNet.regMessageHandler(responseReceiver, filter);

		IntentFilter recFilter = new IntentFilter();
		recFilter.addAction(ACTION_SERVICE_STOP);
		registerReceiver(mReceiver, recFilter);

		helper = IMDBHelper.getInstance(getApplicationContext());
		Cursor mCursor = CreateCursor();
		if (mCursor != null && mCursor.getCount() < 1) {
			long time1 = toServerFormat(System.currentTimeMillis());
			Intent intent1 = createNewMessage(CompassApp.GLOBAL.KEFU_ID, "您好，我是您的专属客服，有什么问题欢迎咨询我哦～", time1);
			saveMessage(intent1, false);

		}
	}

	private static String former = ""; // 之前的一条消息

	private NetResponseHandler responseReceiver = new NetResponseHandler() {

		@Override
		public void receive(EzMessage ezMsg) {
			// TODO Auto-generated method stub
			Intent intent = IntentTools.messageToIntent(ezMsg);
			if (intent == null) {
				return;
			}
			String from = intent.getStringExtra("from");
			String text = intent.getStringExtra("text");
			long timeq=intent.getLongExtra("time", 0);
			
			try {
				DateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
				Calendar calendar = Calendar.getInstance();
				calendar.setTimeInMillis(timeq);
				timeq=Long.parseLong(formatter.format(calendar.getTime()));
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				timeq=intent.getLongExtra("time", 0);
			}
	        intent.putExtra("time", timeq);

			String current = text +timeq;
			Log.e("", current+"=======sss");
			if (TextUtils.equals(former, current)) {
				return;
			}
			former = current;
			//没看懂，一会儿改
//			if (ACTION_CS_MESSAGE.equalsIgnoreCase(intent.getAction())) {
//				saveMessage(intent, false);
//
//				Log.d("new message", from);
//				String roomid = intent.getStringExtra("roomid");
//				if (TextUtils.isEmpty(roomid)
//						&& !TextUtils.equals(from, AuthUserInfo.getMyCid())
//						&& !KefuTalkActivity.istalk && MainActivity.IS_ALIVE) {
//					// 单个人的未读记录+1
//
//					NotificationManager nm = (NotificationManager) MessageService.this
//							.getSystemService(Context.NOTIFICATION_SERVICE);
//
//					NotificationCompat.Builder builder = new NotificationCompat.Builder(
//							MessageService.this)
//							.setSmallIcon(R.drawable.logo_)
//							.setContentTitle("新消息").setTicker("新消息");
//					builder.setAutoCancel(true);
//
//					builder.setDefaults(Notification.DEFAULT_SOUND);
//
//					Bundle bundle = AuthUserService.getFriendInfo(from);
//					if (bundle != null) {
//						builder.setContentText("收到来自"
//								+ bundle.getString("name") + "的消息");
//
//					} else {
//						builder.setContentText("收到新消息");
//					}
//
//					Intent talkIntent = new Intent();
//
//					talkIntent.setAction("my_message");
//					talkIntent.putExtra("targetCid",
//							intent.getStringExtra("from"));
//					talkIntent.putExtra("targcid",
//							intent.getStringExtra("target"));
//					talkIntent.setClassName("com.ez08.compass",
//							"com.ez08.compass.activity.HandleMessageActivity");
//
//					talkIntent.putExtra("isInBackground",
//							SysVarsManager.getBool("isInBackground", true));
//
//					PendingIntent pi = PendingIntent.getActivity(
//							MessageService.this, 0, talkIntent,
//							PendingIntent.FLAG_UPDATE_CURRENT);
//					builder.setContentIntent(pi);
//
//					nm.notify(0, builder.build());
//
//				} else if (!TextUtils.isEmpty(roomid)
//						&& !TextUtils.equals(from, AuthUserInfo.getMyCid())
//						&& !ClassRoomActivity.istalk && MainActivity.IS_ALIVE) {
//					// 单个人的未读记录+1
//
//					NotificationManager nm = (NotificationManager) MessageService.this
//							.getSystemService(Context.NOTIFICATION_SERVICE);
//
//					NotificationCompat.Builder builder = new NotificationCompat.Builder(
//							MessageService.this)
//							.setSmallIcon(R.drawable.logo_)
//							.setContentTitle("新消息").setTicker("新消息");
//					builder.setAutoCancel(true);
//
//					builder.setDefaults(Notification.DEFAULT_SOUND);
//
//					Bundle bundle = AuthUserService.getFriendInfo(from);
//					builder.setContentText("收到房间消息");
//
//					Intent talkIntent = new Intent();
//
//					talkIntent.setAction("room_message");
//					talkIntent.putExtra("targetCid",
//							intent.getStringExtra("from"));
//					talkIntent.putExtra("targcid",
//							intent.getStringExtra("target"));
//					talkIntent.setClassName("com.ez08.compass",
//							"com.ez08.compass.activity.HandleMessageActivity");
//
//					talkIntent.putExtra("isInBackground",
//							SysVarsManager.getBool("isInBackground", true));
//
//					PendingIntent pi = PendingIntent.getActivity(
//							MessageService.this, 0, talkIntent,
//							PendingIntent.FLAG_UPDATE_CURRENT);
//					builder.setContentIntent(pi);
//
//					nm.notify(1, builder.build());
//
//				}
//			}
		}

	};
	
	/**
	 * 推送消息处理
	 */

	private Cursor CreateCursor() {
		return helper.getConversationTableCursor(CompassApp.GLOBAL.KEFU_ID);
	}

	BroadcastReceiver mReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context arg0, Intent intent) {
			// TODO Auto-generated method stub
			String action = intent.getAction();
			if (ACTION_SERVICE_STOP.equalsIgnoreCase(action)) {
				MessageService.this.stopSelf();
			} /*
			 * else if (ACTION_SERVICE_TEST.equalsIgnoreCase(action)) { //
			 * Intent intent2=new Intent("ez08.cs.message"); long time =
			 * toServerFormat(System.currentTimeMillis()); Intent intent2 =
			 * createNewMessage(CompassApp.KEFU_ID, "您好", time); //
			 * intent2.putExtra("text", "您好"); saveMessage(intent2, false); }
			 */
		}
	};

	public static Intent createNewMessage(String target, String msg, long time) {
		String action;
		action = "ez08.znt.message";
		Intent intent = new Intent(action);
		// intent.putExtra("time", time);
		intent.putExtra("text", msg);
		intent.putExtra("from", target);
		return intent;
	}

	public static Intent createNewMessage(String target, String msg, long time,
                                          String roomid) {
		String action;
		action = "ez08.znt.message";
		Intent intent = new Intent(action);
		// intent.putExtra("time", time);
		intent.putExtra("text", msg);
		intent.putExtra("roomid", roomid);
		intent.putExtra("from", target);
		return intent;
	}

	public static long toServerFormat(long t) {
		Date d = new Date(t);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String str = sdf.format(d);
		long t2 = Long.parseLong(str);
		return t2;
	}

	private void saveMessage(Intent intent, boolean send) {
		long lTime=intent.getLongExtra("time", 0);
		long time;
		if(lTime!=0){
			time=lTime;
		}else{
			time = toServerFormat(System.currentTimeMillis());
		}
		if (helper == null) {
			helper = IMDBHelper.getInstance(MessageService.this);
		}

		String text = intent.getStringExtra("text");
		Log.e("text", text);
		if (!TextUtils.isEmpty(text)) { // 替换表情
			for (int i = 0; i < FileUtils.SEND_ALI.length; i++) {
				if (text.contains(FileUtils.SEND_ALI[i])) {
					text = text.replace(FileUtils.SEND_ALI[i], "["
							+ FileUtils.EMOS_ALI[i] + "]");
				}
			}
		}

		if (text != null && text.contains("znz}")) { // 证明有图片
			List<ImageEntity> list = new ArrayList<ImageEntity>();
			while (text.contains("znz}")) { //
				int startindex = text.indexOf("{znz");
				int endindex = text.indexOf("znz}") + 4;
				String turl = text.substring(startindex, endindex);
				String utext = text.substring(0, startindex);
				String etext = text.substring(endindex, text.length());
				ImageEntity entity1 = new ImageEntity();
				ImageEntity entity2 = new ImageEntity();
				ImageEntity entity3 = new ImageEntity();
				entity1.content = utext;
				entity1.isImage = false;
				entity2.content = turl;
				entity2.isImage = true;
				entity3.content = etext;
				entity3.isImage = false;
				if (!entity1.content.equals("")) {
					list.add(entity1);
				}
				list.add(entity2);
				if (!entity3.content.equals("")) {
					list.add(entity3);
				}
				text = text.substring(endindex);
			}
			for (int i = 0; i < list.size(); i++) {
				Log.e("", list.get(i).content + "==" + list.get(i).isImage);
			}
			for (int i = 0; i < list.size(); i++) {
				ImageEntity entity = list.get(i);
				if (!entity.isImage) {
					intent.putExtra("text", entity.content);
					intent.putExtra("imageurl", "");
				} else {
					intent.putExtra("text", "");
					intent.putExtra("imageurl", setToUrl(entity.content));

					// intent.putExtra("imageurl", entity.content);
				}
				helper.saveMessage(intent, send, time);
			}

		} else {
			helper.saveMessage(intent, send, time);
		}

		// helper.saveMessage(intent, send, time);
		String roomid = intent.getStringExtra("roomid");
		if (TextUtils.isEmpty(roomid)) { // 来自客服
			sendNotifyUIBroadcase();
		} else { // 来自房间直播
			sendNotifyROOMBroadcase();
		}

	}

	/**
	 * 将字符串转换成url
	 * 
	 * @return
	 */
	private String setToUrl(String content) {
		int startindex = content.indexOf("{znz") + 4;
		int endindex = content.indexOf("znz}");
		String url = content.substring(startindex, endindex);
		Log.e("ss", "url=" + url);
		return url;
	}

	class ImageEntity {
		boolean isImage;
		String content;
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		unregisterReceiver(mReceiver);
	}

	/**
	 * 数据保存到数据库后，发送一个广播，通知更新ui
	 */
	private void sendNotifyUIBroadcase() {
		Intent intent = new Intent();
		intent.setAction(ACTION_NOTIFY_UI);
		sendBroadcast(intent);
		Intent intent2 = new Intent();
		intent2.setAction(ACTION_NOTIFY_KEFU);
		sendBroadcast(intent2);
	}

	private void sendNotifyROOMBroadcase() {
		Intent intent = new Intent();
		intent.setAction(ACTION_NOTIFY_ROOM);
		sendBroadcast(intent);
	}
}
