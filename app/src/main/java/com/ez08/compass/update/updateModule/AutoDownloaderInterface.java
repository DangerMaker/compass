package com.ez08.compass.update.updateModule;


public interface AutoDownloaderInterface {

	int ALTERNATIVE_UPDATE = 0; // 可选更新类型
	int FORCE_UPDATE = 1; // 强制更新类型

	void start(AutoUpdatePacket updatePacket);

	void showNoticeDialog();

	void download();

	void breadpointDownload();

	void stop();

	void installApk();
	

}
