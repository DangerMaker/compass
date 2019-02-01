package com.ez08.compass.autoupdate.updateModule;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ez08.compass.R;
import com.ez08.compass.autoupdate.tools.AutoFileUtility;
import com.ez08.compass.autoupdate.tools.AutoPackageUtility;
import com.ez08.compass.autoupdate.tools.AutoStringUtility;
import com.ez08.compass.autoupdate.tools.AutoTools;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;

public class AutoDownloader3G implements AutoDownloaderInterface {

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    public void myPermission() {
        int permission = ActivityCompat.checkSelfPermission(AutoUpdateModule.getUpdateActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    AutoUpdateModule.getUpdateActivity(),
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }


    private AutoUpdatePacket auto_updatePacket;
    private ProgressBar auto_progressBar;
    private int auto_progress;
    private Thread auto_downLoadThread;
    private File auto_apkFile;
    private AutoUpdateManager auto_updateManager;

    private boolean auto_breakPointDownload = false;

    Dialog auto_noticeDialog;
    View auto_noticeView;

    private static final int AUTO_DOWN_UPDATE = 10000;
    private static final int AUTO_DOWN_OVER = 10001;
    private static final int AUTO_DOWN_FAIL = 10002;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case AUTO_DOWN_FAIL:
                    if (auto_noticeDialog != null) {
                        auto_noticeDialog.dismiss();
                        if (AutoUpdateModule.getUpdateActivity() != null) {
                            AutoUpdateModule.getUpdateActivity().finish();
                        }
                        // EzApp.showToast("下载失败");
                        Toast.makeText(AutoUpdateModule.getContext(), "下载失败", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case AUTO_DOWN_UPDATE:
                    if (auto_progressBar != null)
                        auto_progressBar.setProgress(auto_progress);
                    break;
                case AUTO_DOWN_OVER:
                    if (auto_noticeDialog != null) {
                        auto_noticeDialog.dismiss();
                        if (AutoUpdateModule.getUpdateActivity() != null) {
                            AutoUpdateModule.getUpdateActivity().finish();
                        }
                    }
                    installApk();
                    break;
                default:
                    break;
            }
        }

        ;
    };

    /**
     * 创建3G下载器同时根据URL创建下载文件，如果存在该文件，则删除后重新创建
     */
    public AutoDownloader3G(AutoUpdateManager um) {
        this.auto_updateManager = um;
    }

    @Override
    public void start(AutoUpdatePacket updatePacket) {

        this.auto_updatePacket = updatePacket;

        try {
            String versionCur = AutoPackageUtility.getVersionName();
            String versionNew = updatePacket.getTver();
            System.out.println("软件接受到更新消息，当前版本号:" + versionCur + ",最新版本号:" + versionNew);
            boolean compareCode = AutoStringUtility.compareStringVersion(versionCur, versionNew);
            if (compareCode) {

                mHandler.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        showNoticeDialog();
                    }
                }, 500);
                System.out.println("弹出更新对话框--------------------------->code:" + compareCode);
            } else {
                // 已是最新版，不需要更新
                System.out.println("当前已是最新版本--------------------------->code:" + compareCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void showNoticeDialog() {
        // TODO Auto-generated method stub

        if (auto_noticeDialog != null && auto_noticeDialog.isShowing()) {
            if (auto_progressBar != null && auto_progressBar.getVisibility() == View.VISIBLE) {
                // 正在下载
                breadpointDownload();
            }
            return;
        }

        // 非断点下载，删除重新下载
        String versionName = auto_updatePacket.getTver();
        auto_noticeDialog = new Dialog(AutoUpdateModule.getUpdateActivity(), R.style.dialog);
        auto_noticeDialog.setOnCancelListener(new OnCancelListener() {

            @Override
            public void onCancel(DialogInterface arg0) {
                if (AutoUpdateModule.getUpdateActivity() != null) {
                    AutoUpdateModule.getUpdateActivity().finish();
                }
            }
        });
        auto_noticeView = LayoutInflater.from(AutoUpdateModule.getContext()).inflate(R.layout.update_progress, null);

        TextView tvTitle = (TextView) auto_noticeView.findViewById(R.id.title_tick);
        TextView tvMsg = (TextView) auto_noticeView.findViewById(R.id.msg);
        View btnCancel = auto_noticeView.findViewById(R.id.cancel_down);
        View btnOk = auto_noticeView.findViewById(R.id.ok_down);

        tvTitle.setText("检测到新版本 V" + versionName);
        tvMsg.setText(auto_updatePacket.getBrief());
        btnCancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (auto_noticeDialog != null) {
                    auto_noticeDialog.dismiss();
                    if (AutoUpdateModule.getUpdateActivity() != null) {
                        AutoUpdateModule.getUpdateActivity().finish();
                    }
                }
            }
        });

        btnOk.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                String url = auto_updatePacket.getCaburl();
                String versionName = auto_updatePacket.getTver();
                String name = url.substring(url.lastIndexOf("/"));
                auto_apkFile = AutoFileUtility.getFile(name);
                if (auto_apkFile != null && auto_apkFile.exists()) {
                    auto_apkFile.delete();
                    try {
                        auto_apkFile.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                if (!auto_apkFile.exists()) {
                    // EzApp.showToast("请检查您的sd卡或内部存储是否可用");
                    Toast.makeText(AutoUpdateModule.getContext(), "请检查您的sd卡或内部存储是否可用。", Toast.LENGTH_SHORT).show();
                    if (auto_noticeDialog != null) {
                        auto_noticeDialog.dismiss();
                        if (AutoUpdateModule.getUpdateActivity() != null) {
                            AutoUpdateModule.getUpdateActivity().finish();
                        }
                    }
                    return;
                }
                download();
            }
        });

        auto_noticeDialog.setContentView(auto_noticeView);
        WindowManager.LayoutParams lp = auto_noticeDialog.getWindow().getAttributes();
        auto_noticeDialog.getWindow().setAttributes(lp);
        lp.width = (int) (AutoTools.getWindowWidth(AutoUpdateModule.getUpdateActivity()) * 8 * 1.0f / 10);
        // noticeDialog.setCancelable(false);
        auto_noticeDialog.setCanceledOnTouchOutside(false);
        auto_noticeDialog.setOnDismissListener(new OnDismissListener() {

            @Override
            public void onDismiss(DialogInterface dialog) {
                // TODO Auto-generated method stub
                stop();
            }
        });
        auto_noticeDialog.show();
        myPermission();
    }

    @Override
    public void download() {
        // TODO Auto-generated method stub
        if (auto_noticeDialog != null) {
            auto_noticeView.findViewById(R.id.btn_container).setVisibility(View.INVISIBLE);
            auto_progressBar = (ProgressBar) auto_noticeView.findViewById(R.id.progress_bar);
            auto_progressBar.setVisibility(View.VISIBLE);
            // 执行下载线程
            this.auto_breakPointDownload = false;
            this.auto_downLoadThread = new DownloadThread();
            auto_downLoadThread.start();
        }

    }

    // 通用下载方案下的断点下载
    @Override
    public void breadpointDownload() {
        // 执行下载线程
        System.out.println("开始执行断点下载-------------------");
        this.auto_breakPointDownload = true;
        this.auto_downLoadThread = new DownloadThread();
        auto_downLoadThread.start();
    }

    @Override
    public void stop() {
        // 线程下载过程中中断下载进程
        if (auto_downLoadThread != null)
            auto_downLoadThread.interrupt();
        mHandler.postDelayed(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                if (auto_updatePacket.getType() == FORCE_UPDATE) {
                    System.exit(0);
                }
            }
        }, 2000);

    }

    @Override
    public void installApk() {
        // TODO Auto-generated method stub
        if (auto_apkFile == null || !auto_apkFile.exists()) {
            return;
        }
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.setDataAndType(Uri.parse("file://" + auto_apkFile.toString()), "application/vnd.android.package-archive");

        if (AutoUpdateModule.getUpdateActivity() != null) {
            AutoUpdateModule.getUpdateActivity().startActivity(i);
        }

        // 判断是否强制安装，强制安装则退出当前应用
        if (auto_updatePacket.getType() == FORCE_UPDATE) {
            // 强制
            System.exit(0);
        }
    }

    class DownloadThread extends Thread {
        boolean downloadCompleteFlag = false;
        InputStream is;
        RandomAccessFile fos;
        long totalLength;
        long downloadSize;

        @Override
        public void run() {
            try {
                URL url = new URL(auto_updatePacket.getCaburl());
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                // conn.setRequestProperty("Connection", "Keep-Alive");
                conn.setConnectTimeout(5000);
                conn.setReadTimeout(5000);

                if (auto_breakPointDownload) {
                    downloadSize = auto_apkFile.length();
                    if (downloadSize > 0) {
                        conn.setRequestProperty("RANGE", "bytes=" + downloadSize + "-");
                        System.out.println("断点下载字节偏移量" + downloadSize);
                    }
                }
                // 链接之前需要执行一些参数设置的方法
                conn.connect();
                totalLength = conn.getContentLength() + downloadSize;

                is = conn.getInputStream();
                is = new BufferedInputStream(is);
                fos = new RandomAccessFile(auto_apkFile, "rw");

                // 下载之前判断是否是断点模式，断点模式则位移读写位置
                if (auto_breakPointDownload) {
                    fos.seek(downloadSize);
                    System.out.println("断点下载偏移指定位置");
                }

                System.out.println("开始下载，downloadsize：" + downloadSize + ",totalsize:" + totalLength);
                byte buf[] = new byte[1024];
                // 下载循环中应该处理网络异常
                // 1.断网 2.网络切换 3.断网->重连
                while (!Thread.currentThread().isInterrupted()) {
                    int numread = is.read(buf);
                    downloadSize += numread;
                    auto_progress = (int) (((float) downloadSize / totalLength) * 100);
                    // 更新进度
                    mHandler.sendEmptyMessage(AUTO_DOWN_UPDATE);
                    if (numread <= 0) {
                        // 下载完成通知安装
                        downloadCompleteFlag = true;
                        break;
                    }
                    fos.write(buf, 0, numread);
                    System.out.println("已下载------------------------------>" + auto_progress);
                }
            } catch (Exception e) {
                e.printStackTrace();

                // 网络超时
                mHandler.sendEmptyMessage(AUTO_DOWN_FAIL);
            } finally {
                try {
                    System.out.println("开始关闭网络流-------------------------------->");
                    if (fos != null)
                        fos.close();
                    if (is != null)
                        is.close();
                    System.out.println("网络流被成功关闭-------------------------------->");
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("网络流被关闭失败-------------------------------->");
                }

                if (downloadCompleteFlag) {
                    mHandler.sendEmptyMessage(AUTO_DOWN_OVER);
                } else {
                    System.out.println("-------------------------------->下载未完成，被终止");
                }

            }

        }
    }

}
