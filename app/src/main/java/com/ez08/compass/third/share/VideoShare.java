package com.ez08.compass.third.share;

import android.app.Activity;

import com.ez08.compass.third.MyShareListener;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMVideo;

public class VideoShare implements Share{

    String title;
    String thumb;
    String des;
    String url;

    public VideoShare(String title, String thumb, String des, String url) {
        this.title = title;
        this.thumb = thumb;
        this.des = des;
        this.url = url;
    }

    @Override
    public void invoke(Activity activity, SHARE_MEDIA share_media, String statistics) {
        UMVideo video = new UMVideo(url);
        UMImage image = new UMImage(activity, thumb);
        video.setTitle(title);//标题
        video.setThumb(image);  //缩略图
        video.setDescription(des);//描述

        new ShareAction(activity)
                .setPlatform(share_media)
                .setCallback(new MyShareListener(activity, statistics))
                .withMedia(video)
                .share();

    }
}
