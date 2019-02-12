package com.ez08.compass.auth;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

public class AuthUserService {
	
	/**
	 * 获取非当前用户信�?
	 * 
	 * @param cid
	 * @return
	 */
	public static Bundle getFriendInfo(String cid) {
		SQLiteDatabase db =AuthDBHelper.getInstance(AuthModule.authContext).getWritableDatabase();
		try {
			Cursor c = db.query("friend_info", null, "cid=?",
					new String[] { cid }, null, null, null);
			if (c.moveToFirst()) {
				Bundle bundle = new Bundle();
				int cidIndex = c.getColumnIndex("cid");
				int realNameIndex=c.getColumnIndex("realname");
				int nameIndex = c.getColumnIndex("name");
				int mobileIndex = c.getColumnIndex("mobile");
				int imageidIndex = c.getColumnIndex("imageid");
				int briefIndex = c.getColumnIndex("brief");
				bundle.putString("cid", c.getString(cidIndex));
				bundle.putString("name", c.getString(nameIndex));
				bundle.putString("realname", c.getString(realNameIndex));
				bundle.putString("mobile", c.getString(mobileIndex));
				bundle.putString("imageid", c.getString(imageidIndex));
				bundle.putString("brief", c.getString(briefIndex));
				return bundle;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.close();
		}
		return null;
	}

	/**
	 * 存储非当前用户信�?
	 * 
	 * @param cid
	 * @return
	 */
	public static void saveFriendInfo(String cid, String name, String realName, String mobile,
                                      String imageid, String brief) {
		SQLiteDatabase db =AuthDBHelper.getInstance(AuthModule.authContext).getWritableDatabase();
		db.beginTransaction();
		try {
			ContentValues cv = new ContentValues();
			cv.put("cid", cid);
			cv.put("name", name);
			cv.put("mobile", mobile);
			cv.put("imageid", imageid);
			cv.put("realname", realName);
			cv.put("brief", brief);
			db.insertWithOnConflict("friend_info", null, cv,
					SQLiteDatabase.CONFLICT_REPLACE);
			db.setTransactionSuccessful();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.endTransaction();
			db.close();
		}
	}

	/**
	 * 获取当前登录用户的信息，该信息存储在数据表auth_user_info�?
	 * 
	 * @param cid
	 * @return
	 */
	public static Bundle getUserInfo(String cid) {
		SQLiteDatabase db = AuthDBHelper.getInstance(AuthModule.authContext)
				.getWritableDatabase();
		try {
			Cursor c = db.query("auth_user_info", null, "cid=?",
					new String[] { cid }, null, null, null);
			if (c.moveToFirst()) {
				Bundle bundle = new Bundle();
				int cidIndex = c.getColumnIndex("cid");
				int nameIndex = c.getColumnIndex("name");
				int realNameIndex = c.getColumnIndex("realname");
				int mobileIndex = c.getColumnIndex("mobile");
				int imageidIndex = c.getColumnIndex("imageid");
				int briefIndex = c.getColumnIndex("brief");
				int sexIndex = c.getColumnIndex("sex");
				int countryIndex = c.getColumnIndex("country");
				int provinceIndex = c.getColumnIndex("province");
				int cityIndex = c.getColumnIndex("city");
				int stateIndex = c.getColumnIndex("state");
				int cfg_addFriendIndex = c.getColumnIndex("cfg_addFriend");
				int cfg_descriptIndex = c.getColumnIndex("cfg_descript");//个人签名信息
				int loginIndex = c.getColumnIndex("login");
				bundle.putString("cid", c.getString(cidIndex));
				bundle.putString("name", c.getString(nameIndex));
				bundle.putString("mobile", c.getString(mobileIndex));
				bundle.putString("imageid", c.getString(imageidIndex));
				bundle.putString("brief", c.getString(briefIndex));
				bundle.putString("country", c.getString(countryIndex));
				bundle.putString("province", c.getString(provinceIndex));
				bundle.putString("city", c.getString(cityIndex));
				bundle.putString("cfg_descript", c.getString(cfg_descriptIndex));
				bundle.putInt("sex", c.getInt(sexIndex));
				bundle.putString("realname", c.getString(realNameIndex));
				bundle.putInt("state", c.getInt(stateIndex));
				bundle.putInt("cfg_addFriend", c.getInt(cfg_addFriendIndex));
				bundle.putInt("login", c.getInt(loginIndex));
				return bundle;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.close();
		}
		return null;
	}

	/**
	 * 保存当前登录用户的信息，该信息存储在数据表auth_user_info�?
	 * 
	 * @return
	 */
	public static void saveUserInfo(String cid, ContentValues cv) {
		SQLiteDatabase db = AuthDBHelper.getInstance(AuthModule.authContext)
				.getWritableDatabase();
		db.beginTransaction();
		try {

			Cursor c = db.query("auth_user_info", null, "cid=?",
					new String[] { cid }, null, null, null);
			if (c != null && c.moveToFirst()) {
				// update
				db.updateWithOnConflict("auth_user_info", cv, "cid=?",
						new String[] { cid }, SQLiteDatabase.CONFLICT_NONE);
			} else {
				// insert
				db.insertWithOnConflict("auth_user_info", null, cv,
						SQLiteDatabase.CONFLICT_NONE);
			}
			db.setTransactionSuccessful();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.endTransaction();
			db.close();
		}
	}

	/**
	 * 根据服务器返回实体结构生成可操作数据库的ContentValues
	 * 
	 * @param intent
	 * @return
	 */
	public static ContentValues getContentValues(Intent intent) {
		Bundle bundle = intent.getExtras();
		ContentValues cv = new ContentValues();
		try {
			Object o = bundle.get("cid");
			if (o!=null && o instanceof String)
				cv.put("cid", (String)o);
			
			o = bundle.get("mobile");
			if (o!=null && o instanceof String)
				cv.put("mobile", (String)o);
			
			o = bundle.get("name");
			if (o!=null && o instanceof String)
				cv.put("name", (String)o);

			o = bundle.get("imageid");
			if (o!=null && o instanceof String)
				cv.put("imageid", (String)o);

			o = bundle.get("brief");
			if (o!=null && o instanceof String)
				cv.put("brief", (String)o);
			o = bundle.get("cfg_descript");
			if (o!=null && o instanceof String)
				cv.put("cfg_descript", (String)o);

			if (bundle.containsKey("sex"))
				cv.put("sex", intent.getIntExtra("sex", 0));
			if (bundle.containsKey("login"))
				if (intent.getBooleanExtra("login", false) == false) {
					cv.put("login", 0);
				} else {
					cv.put("login", 1);
				}
			if (bundle.containsKey("cfg_addFriend"))
				cv.put("cfg_addFriend", intent.getIntExtra("cfg_addFriend", 0));
			if (bundle.containsKey("state"))
				cv.put("state", intent.getIntExtra("state", 0));
			
			o = bundle.get("city");
			if (o!=null && o instanceof String)
				cv.put("city", (String)o);

			o = bundle.get("province");
			if (o!=null && o instanceof String)
				cv.put("province", (String)o);

			o = bundle.get("country");
			if (o!=null && o instanceof String)
				cv.put("country", (String)o);

			return cv;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return cv;
		}
		

	}
}
