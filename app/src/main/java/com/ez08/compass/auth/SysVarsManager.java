package com.ez08.compass.auth;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;

import com.ez08.compass.entity.KefuInfoEntity;
import com.ez08.support.util.CodedInputStream;
import com.ez08.support.util.CodedOutputStream;
import com.ez08.support.util.EzValue;
import com.ez08.tools.IntentTools;

public class SysVarsManager {

	private static Context mContext;

	public static void setContext(Context context) {
		mContext = context;
	}

	public static Context getContext() {
		return mContext;
	}

	/**
	 * 获取系统变量references
	 */
	public static SharedPreferences getPreferences() {
		if(getContext()!=null){
			return getContext().getSharedPreferences("EZ08_SYS",
					Context.MODE_PRIVATE);
		}else{
			return null;
		}

	}

	/**
	 * 存储boolean数据
	 * 
	 * @param key
	 * @param value
	 */
	public static void putBool(String key, boolean value) {
		Editor edit = getPreferences().edit();
		edit.putBoolean(key, value);
		edit.commit();
	}

	/**
	 * 获取存储的boolean数据
	 * 
	 * @param key
	 * @param defValue
	 */
	public static boolean getBool(String key, boolean defValue) {
		return getPreferences().getBoolean(key, defValue);
	}

	/**
	 * 存储int数据
	 * 
	 * @param key
	 * @param value
	 */
	public static void putInt(String key, int value) {
		Editor edit = getPreferences().edit();
		edit.putInt(key, value);
		edit.commit();
	}

	/**
	 * 获取存储的int数据
	 * 
	 * @param key
	 * @param defValue
	 */
	public static int getInt(String key, int defValue) {
		return getPreferences().getInt(key, defValue);
	}

	/**
	 * 存储String数据
	 * 
	 * @param key
	 * @param value
	 */
	public static void putString(String key, String value) {
		Editor edit = getPreferences().edit();
		edit.putString(key, value);
		edit.commit();
	}

	/**
	 * 获取存储的String数据
	 * 
	 * @param key
	 * @param defValue
	 */
	public static String getString(String key, String defValue) {
		if(getPreferences()==null){
			return "";
		}
		return getPreferences().getString(key, defValue);
	}

	public static void putKefuInfo(String cid, String pid, String sign,
                                   String name) {
		Editor edit = getPreferences().edit();
		edit.putString("kefu_name", name);
		edit.putString("kefu_cid", cid);
		edit.putString("kefu_pid", pid);
		edit.putString("kefu_sign", sign);
		edit.commit();
	}

	private static KefuInfoEntity kefu_info;
	public static KefuInfoEntity getKefuInfo() {
		if (kefu_info == null) {
			kefu_info = new KefuInfoEntity();
			String name=getPreferences().getString("kefu_name", "");
			String cid=getPreferences().getString("kefu_cid", "");
			String pid=getPreferences().getString("kefu_pid", "");
			String sign=getPreferences().getString("kefu_sign", "");
			kefu_info.setCid(cid);
			kefu_info.setName(name);
			kefu_info.setPid(pid);
			kefu_info.setSign(sign);
		}
		return kefu_info;
	}

	/**
	 * 存储long数据
	 * 
	 * @param key
	 * @param value
	 */
	public static void putLong(String key, long value) {
		Editor edit = getPreferences().edit();
		edit.putLong(key, value);

		edit.commit();
	}

	/**
	 * 获取存储的long数据
	 * 
	 * @param key
	 * @param defValue
	 */
	public static long getLong(String key, long defValue) {
		return getPreferences().getLong(key, defValue);
	}

	public static Bundle getBundle(String key) {
		Bundle bd = null;
		CodedInputStream io = StorageTools.openForRead(AuthModule.authContext,
				AuthModule.packgeName, "SYS_VAR_" + key);
		if (io != null) {
			EzValue value = new EzValue();
			value.readValueFrom(io);
			if (value != null)
				bd = IntentTools.keyValue2Bundle(value.getKeyValues());

		}

		return bd;
	}

	public static void setBundle(String key, Bundle bd) {
		if (bd == null)
			return;
		deleteBundle(key);
		CodedOutputStream os = null;
		try {
			os = StorageTools.openForWrite(AuthModule.authContext,
					AuthModule.packgeName, "SYS_VAR_" + key);
			StorageTools.writeAsEzValue(os, bd);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				os.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static void deleteBundle(String key) {
		StorageTools.delete(AuthModule.authContext, AuthModule.packgeName,
				"SYS_VAR_" + key);
	}
}
