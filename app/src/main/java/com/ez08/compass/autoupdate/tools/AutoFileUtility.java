package com.ez08.compass.autoupdate.tools;

import com.ez08.compass.autoupdate.updateModule.AutoUpdateModule;

import java.io.File;
import java.io.IOException;

public class AutoFileUtility {
	/* 下载包安装路径 */
	private static final String savePath = "/sdcard/farm/";

	/**
	 * 返回指定文件，文件位于当前程序的内存中
	 * 
	 * @param fileName
	 * @return
	 */
	private static File getFileInMemory(String fileName) {
		File dataDir = AutoUpdateModule.getContext().getFilesDir();
		File apkFile = new File(dataDir, fileName);
		if (!apkFile.exists()) {
			try {
				apkFile.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return apkFile;
	}

	/**
	 * 返回指定文件，文件位于sdcard中
	 * 
	 * @param fileName
	 * @return
	 */
	private static File getFileFromSDCard(String fileName) {
		File appDir = new File(savePath);
		if (!appDir.exists()) {
			appDir.mkdirs();
		}
		File apkFile = new File(appDir, fileName);
		if (!apkFile.exists()) {
			try {
				apkFile.createNewFile();
			} catch (IOException e) {e.printStackTrace();
			}
		}
		return apkFile;

	}

	/**
	 * 获取指定文件
	 * 
	 * @param fileName
	 * @return
	 */
	public static File getFile(String fileName) {
		if (checkSDCard()) {
			// 返回sdcard指定目录中的文件
			return getFileFromSDCard(fileName);
		} else {
			// 返回内存中的指定文件
			return getFileInMemory(fileName);
		}

	}

	private static boolean checkSDCard() {
		if (android.os.Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED))
			return true;
		else
			return false;
	}

}
