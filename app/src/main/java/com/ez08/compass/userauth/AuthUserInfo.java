package com.ez08.compass.userauth;

import android.content.Intent;
import android.os.Bundle;

public class AuthUserInfo {

	public static Bundle mBundle;
	
	public static Intent getLoginIntent(String mobile, String pwd) {
		Intent localIntent = new Intent("ez08.auth.login");
		localIntent.putExtra("mobile", mobile);
		localIntent.putExtra("pwd", pwd);
		return localIntent;
	}

	public static void setUserInfoBundle(Bundle bd){
		mBundle = bd;		
		SysVarsManager.setBundle("AuthUserInfo", bd);

	}
	public static void modifyUserInfo(Bundle bd){
		if(mBundle!=null && bd!=null){
			mBundle.putAll(bd);
			SysVarsManager.setBundle("AuthUserInfo", mBundle);
		}
	}
	public static Bundle restoreUserInfoBundle(){
		mBundle = SysVarsManager.getBundle("AuthUserInfo");
		if(mBundle == null)
			mBundle = new Bundle();
		return mBundle;
	}
	
	
	public static boolean isLogined() {
		return mBundle.getBoolean("login");
	}

	public static void setLogined(boolean isLogined) {
		mBundle.putBoolean("login", isLogined);
		setUserInfoBundle(mBundle);
	}

	public static String getMyName() {
		return mBundle.getString("name");
	}
	public static String getMyPicid() {
		return mBundle.getString("imageid")+"";	
	}
	
	public static String getMyPhone() {
		return mBundle.getString("mobile");
	}
	

	public static String getMyCid() {
		if(mBundle==null){
			return "";
		}
		try {
			return mBundle.getString("cid");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}
	}


	public static String getMyTid() {
		if(mBundle==null){
			return null;
		}
		try {
			String tid=mBundle.getString("tid","");
			return tid;
		}catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}
	}


	public static int getMySex() {
		return mBundle.getInt("sex");
	}

	public static String getEmail() {
		return mBundle.getString("email");
	}
	
	public static String getMyToken() {
		if(mBundle==null){
			return null;
		}
		return mBundle.getString("token");
	}

	public static String getMyRealName(){
		return mBundle.getString("realname");
	}

	public static int getLockInfo(){
		int lockinfo2 = mBundle.getInt("lockinfo2",-1);
		if(lockinfo2 > 0){
			return lockinfo2;
		}else {
			return 0;
		}
	}

	public static void clearUserInfo() {
		SysVarsManager.deleteBundle("AuthUserInfo");
		mBundle = new Bundle();
	}
}
