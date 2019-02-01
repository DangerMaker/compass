package com.ez08.compass.tools;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;
import com.ez08.compass.entity.ItemStock;
import com.ez08.compass.entity.PushMessageEntity;
import com.ez08.compass.ui.WebActivity;
import com.ez08.compass.ui.personal.PushMessageDialog;
import com.ez08.support.net.NetResponseHandler2;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

@SuppressLint("NewApi")
public class PushManager {

	private final static int WHAT_PUSH_RESULT = 30;

	static NetResponseHandler2 mHandler = new NetResponseHandler2() {
		@Override
		public void receive(int arg0, boolean arg1, Intent arg2) {
			if (arg2 != null) {
				// Toast.makeText(CompassApp.getmContext(),"反馈成功", 0).show();
			}

		}
	};

	public static void pushManager(final Activity context, boolean home) {
		SharedPreferences pushInfoPreferences=null;
		if(home){
			pushInfoPreferences = context.getSharedPreferences(
					"homepageInfo", 0);
		}else{
			pushInfoPreferences = context.getSharedPreferences(
					"pushInfo", 0);
		}

		// 处理推送
		final String pushid = pushInfoPreferences.getString("pushid", "");
		final String title = pushInfoPreferences.getString("title", "");
		final String description = pushInfoPreferences.getString("description",
				"");
		final String imgurl = pushInfoPreferences.getString("imgurl", "");
		final String uri = pushInfoPreferences.getString("uri", "");
		final String action = pushInfoPreferences.getString("action", "");
		String usertype = pushInfoPreferences.getString("usertype", "");// 资金分享时用到
		String time = pushInfoPreferences.getString("time", "");// 不知什么时候用到
		String starttime = pushInfoPreferences.getString("starttime", "");
		String endtime = pushInfoPreferences.getString("endtime", "");
		String pushType = pushInfoPreferences.getString("pushtype", "");
		String stockcode = pushInfoPreferences.getString("stockcode", "");
		String url=pushInfoPreferences.getString("url", "");
		Long curtime = Long.parseLong(new SimpleDateFormat("yyyyMMddHHmmss")
				.format(new Date()));

		if (!home) {
			PushMessageEntity homeEntity = new PushMessageEntity();
			homeEntity.setTitle(title);
			homeEntity.setDescription(description);
			homeEntity.setImgurl(imgurl);
			homeEntity.setUrl(url);
			homeEntity.setPushid(pushid);
			homeEntity.setUri(uri);
			homeEntity.setTime(time);
			homeEntity.setStarttime(starttime);
			homeEntity.setEndtime(endtime);
			homeEntity.setPushtype(pushType);
			homeEntity.setAction(action);
			homeEntity.setStockCode(stockcode);
			pushInfoPreferences.edit().putBoolean("ifShowNotification", false)
					.commit();

			if(!TextUtils.isEmpty(action)&&(action.equals("gotoshare")||action.equals("gotourl")||action.equals("cancel"))){
				new PushMessageDialog(context, homeEntity).show();
				return;
			}
			
			Intent pushIntent = new Intent();
			pushIntent.setClassName("com.ez08.compass",
					"com.ez08.compass.activity.HandleMessageActivity");
			pushIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			pushIntent.putExtra("pushentity", homeEntity);
			pushIntent.setAction(action);
			context.startActivity(pushIntent);
			return;
		}

		if (curtime >= Long.parseLong(starttime)
				&& curtime <= Long.parseLong(endtime)) {
			PushMessageEntity homeEntity = new PushMessageEntity();
			homeEntity.setTitle(title);
			homeEntity.setDescription(description);
			homeEntity.setImgurl(imgurl);
			homeEntity.setUrl(url);
			homeEntity.setPushid(pushid);
			homeEntity.setUri(uri);
			homeEntity.setTime(time);
			homeEntity.setStarttime(starttime);
			homeEntity.setEndtime(endtime);
			homeEntity.setPushtype(pushType);
			homeEntity.setAction(action);
			homeEntity.setStockCode(stockcode);
			pushInfoPreferences.edit().putBoolean("ifShowNotification", false)
					.commit();
			new PushMessageDialog(context, homeEntity).show();
		} else if (curtime > Long.parseLong(endtime)) {
			pushInfoPreferences.edit().putBoolean("ifShowNotification", false)
					.commit();
		} else {
			return;
		}
	}

	protected static void startWebActivity(Context context, Intent intent, String name,
                                           String url, String title, int type) {
		intent = new Intent(context, WebActivity.class);
		ItemStock lEntity = new ItemStock();
		lEntity.setUrl(url);
		lEntity.setTitle(title);
		lEntity.setName(name);
		intent.putExtra("entity", lEntity);
		intent.putExtra("type", type);
		context.startActivity(intent);

	}

	/**
	 * 将推送消息保存到本地
	 * 
	 * @param context
	 * @param pushMessage
	 */
	public static void savePushInfoToLocal(Context context,
                                           PushMessageEntity pushMessage, JSONArray array) {
		SharedPreferences preferences = AppUtils.getSharedPrefCerences(context);
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("pushid", pushMessage.getPushid());
			jsonObject.put("title", pushMessage.getTitle());
			jsonObject.put("description", pushMessage.getDescription());
			jsonObject.put("imgurl", pushMessage.getImgurl());
			jsonObject.put("uri", pushMessage.getUri());
			jsonObject.put("pushtype", pushMessage.getPushtype());
			jsonObject.put("usertype", pushMessage.getUsertype());
			jsonObject.put("time", pushMessage.getTime());
			jsonObject.put("starttime", pushMessage.getStarttime());
			jsonObject.put("endtime", pushMessage.getEndtime());
			jsonObject.put("ifhasfind", pushMessage.ishasfind());
			jsonObject.put("receivertime",
					new SimpleDateFormat("MM-dd").format(new Date()));
			if (array.length() < 10) {
				array.put(jsonObject);
			} else {
				array.remove(0);
				array.put(jsonObject);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		Editor editor = preferences.edit();
		editor.putString("list", array.toString());
		editor.commit();
	};

	public static void updatePushInfoToLocal(Context context, int position,
                                             boolean ishasfind) {
		JSONArray array = null;
		SharedPreferences preferences = AppUtils.getSharedPrefCerences(context);
		String str = preferences.getString("list", "");
		if (TextUtils.isEmpty(str)) {
			array = new JSONArray();
		} else {
			try {
				array = new JSONArray(str);
				JSONObject jsonObject = (JSONObject) array.get(position);
				jsonObject.put("ifhasfind", ishasfind);
				Editor editor = preferences.edit();
				editor.putString("list", array.toString());
				editor.commit();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public static void updatePushInfoToLocal(Context context, String pushid, boolean ishasfind){
		JSONArray array=null;
		SharedPreferences preferences = AppUtils.getSharedPrefCerences(context);
		String str = preferences.getString("list", "");
		if(TextUtils.isEmpty(str)){
			array=new JSONArray();
		}else{
			try {
				array=new JSONArray(str);
				for(int j=0;j<array.length();j++){
					JSONObject jsonObject=(JSONObject) array.get(j);
					if(jsonObject.getString("pushid").equals(pushid)){
						jsonObject.put("ifhasfind",ishasfind);
						Editor editor = preferences.edit();
						editor.putString("list", array.toString());
						editor.commit();
					}
				}
			}catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		Intent intentReceiver=new Intent();
		intentReceiver.setAction("dataChange");
		context.sendBroadcast(intentReceiver);
	}
	
}
