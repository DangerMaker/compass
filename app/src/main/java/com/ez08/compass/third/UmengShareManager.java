package com.ez08.compass.third;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

import com.ez08.compass.CompassApp;
import com.ez08.compass.net.NetInterface;
import com.ez08.compass.third.share.AudioShare;
import com.ez08.compass.third.share.ImageShare;
import com.ez08.compass.third.share.LinkShare;
import com.ez08.compass.third.share.Share;
import com.ez08.compass.third.share.TextShare;
import com.ez08.compass.third.share.VideoShare;
import com.ez08.compass.tools.ToastUtils;
import com.ez08.support.net.NetResponseHandler2;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.shareboard.ShareBoardConfig;
import com.umeng.socialize.shareboard.SnsPlatform;
import com.umeng.socialize.utils.ShareBoardlistener;

import java.lang.ref.WeakReference;

public class UmengShareManager {

    private Activity activity;
    public static String TEXT = "text";
    public static String IMAGE = "image";
    public static String WEB = "web";
    public static String VIDEO = "video";
    public static String AUDIO = "audio";

    public UmengShareManager(Activity activity) {
        this.activity = activity;
    }

    public void shareFactory(String schema, String statisticsName){
        Uri uri = Uri.parse(schema);
        String shareType = uri.getQueryParameter("sharetype");
        String title = uri.getQueryParameter("title");
        String content = uri.getQueryParameter("content");
        String image = uri.getQueryParameter("image");
        String url = uri.getQueryParameter("url");

        if(shareType.equals(TEXT)){
            share(new TextShare(content),statisticsName);
        }else if(shareType.equals(IMAGE)){
            share(new ImageShare(image),statisticsName);
        }else if(shareType.equals(WEB)){
            share(new LinkShare(title,image,content,url,"0"),statisticsName);
        }else if(shareType.equals(VIDEO)){
            share(new VideoShare(title,image,content,url),statisticsName);
        }else if(shareType.equals(AUDIO)){
            share(new AudioShare(title,image,content,url),statisticsName);
        }else{
            ToastUtils.show(activity,"无效的shareType");
        }
    }

    public void share(Share model, String statisticsName) {
        ShareBoardConfig config = new ShareBoardConfig();
        config.setMenuItemBackgroundShape(ShareBoardConfig.BG_SHAPE_CIRCULAR);
        config.setCancelButtonVisibility(false);
        config.setTitleVisibility(false);
        config.setIndicatorVisibility(false);

        new ShareAction(activity)
                .setDisplayList(SHARE_MEDIA.QQ,SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE,  SHARE_MEDIA.QZONE, SHARE_MEDIA.SINA)
                .setShareboardclickCallback(new MyBoardListener(activity, model, statisticsName))
                .open(config);
    }

    public static class MyBoardListener implements ShareBoardlistener {

        WeakReference<Activity> mReference;
        Share model;
        String statistics;

        public MyBoardListener(Activity activity, Share model, String statistics) {
            mReference = new WeakReference<>(activity);
            this.model = model;
            this.statistics = statistics;
        }

        @Override
        public void onclick(SnsPlatform snsPlatform, SHARE_MEDIA share_media) {
            model.invoke(mReference.get(), share_media, statistics);

            //统计
            if(statistics != null) {
                int type = -1;
                switch (share_media) {
                    case WEIXIN:
                        type = 1;
                        break;
                    case WEIXIN_CIRCLE:
                        type = 3;
                        break;
                    case QQ:
                        type = 2;
                        break;
                    case QZONE:
                        type = 4;
                        break;
                    case SINA:
                        type = 5;
                        break;
                }

                String arg1 = statistics;
                String arg2 = "";
                if(statistics.contains("@")){
                    String[] params = statistics.split("@");
                    arg1 = params[0];
                    arg2 = params[1];
                }

                CompassApp.addStatis(arg1, Integer.toString(type), arg2,
                        System.currentTimeMillis());
            }else{
                //历史原因，我也不知道能不能去
                if(model instanceof LinkShare) {
                    NetInterface.shareResultNotify(mHandler, 100, ((LinkShare) model).id, "info");
                }
            }
        }
    }

    static NetResponseHandler2 mHandler = new NetResponseHandler2() {
        @Override
        public void receive(int arg0, boolean arg1, Intent arg2) {

        }
    };


}
