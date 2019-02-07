package com.ez08.tools;

import java.util.UUID;

import android.app.Activity;
import android.app.Application;
import android.content.SharedPreferences;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.text.TextUtils;

public class MacAddrManager {

	static Application app;

	public static String getMacAddr(Application application) {
		app = application;
		String macAddress = "";

		//-------------------
		boolean addrUseful=false;	//mac地址是否可用
		WifiManager wifi11 = (WifiManager) application.getSystemService("wifi");
		WifiInfo info = wifi11.getConnectionInfo();
		if (info != null && info.getMacAddress() != null) {
			addrUseful=true;
			if (info.getMacAddress().equals("02:00:00:00:00:00") || info.getMacAddress().equals("")
					|| info.getMacAddress().equals("00:00:00:00:00:00")
					|| info.getMacAddress().equals("ff:ff:ff:ff:ff:ff")) {
				addrUseful=false;
			}
		}
		//------------------

		SharedPreferences mSharedPreferences = application.getSharedPreferences("wifi", Activity.MODE_PRIVATE);
		String wifiMacAddr = mSharedPreferences.getString("wifiMacAddr", "");
		if (!TextUtils.isEmpty(wifiMacAddr)) {
			if(wifiMacAddr.length()>=36) {
				macAddress = wifiMacAddr;
			}else{
				if(addrUseful){
					macAddress = wifiMacAddr;
				}else{
					macAddress=getUUID();
					SharedPreferences.Editor mEditor = mSharedPreferences.edit();
					mEditor.putString("wifiMacAddr", macAddress);
					mEditor.commit();
				}
			}
		} else {
			if(addrUseful){
				macAddress = info.getMacAddress();
			}else{
				macAddress = getUUID();
			}
			SharedPreferences.Editor mEditor = mSharedPreferences.edit();
			mEditor.putString("wifiMacAddr", macAddress);
			mEditor.commit();
		}
		return macAddress;
	}

	/**
	 * �õ�ȫ��ΨһUUID
	 */
	private static String getUUID() {
		String uuid = UUID.randomUUID().toString();
		return uuid;
	}
}
