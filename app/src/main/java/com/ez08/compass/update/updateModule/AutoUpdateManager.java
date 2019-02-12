package com.ez08.compass.update.updateModule;


public class AutoUpdateManager {

	private static AutoUpdateManager um = new AutoUpdateManager();

	private AutoDownloaderInterface downloader;
	private static final String aCTION_NET_CHANGE = "android.net.conn.CONNECTIVITY_CHANGE";

	private AutoUpdateManager() {
		downloader = new AutoDownloader3G(this);
	}

	public static AutoUpdateManager getInstance() {
		return um;
	}

	// 外部接口让主Activity调用
	public void checkUpdate(AutoUpdatePacket updatePacket) {
		downloader.start(updatePacket);
	}

}
