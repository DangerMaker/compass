package com.ez08.compass.update.tools;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.ez08.compass.update.updateModule.AutoUpdateModule;

public class AutoPackageUtility {

	public static String getVersionName() throws Exception {
		// 获取packagemanager的实例
		PackageManager packageManager = AutoUpdateModule.getContext()
				.getPackageManager();
		// getPackageName()是你当前类的包名，0代表是获取版本信息
		PackageInfo packInfo = packageManager.getPackageInfo(AutoUpdateModule
				.getContext().getPackageName(), 0);
		String version = packInfo.versionName;
		return version;
	}
}
