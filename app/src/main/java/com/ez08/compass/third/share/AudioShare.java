package com.ez08.compass.third.share;

import android.app.Activity;

import com.ez08.compass.third.MyShareListener;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMusic;

public class AudioShare implements Share{

    String title;
    String thumb;
    String des;
    String url;

    public AudioShare(String title, String thumb, String des, String url) {
        this.title = title;
        this.thumb = thumb;
        this.des = des;
        this.url = url;
    }

    @Override
    public void invoke(Activity activity, SHARE_MEDIA share_media, String statistics) {
        UMusic music = new UMusic(url);
        UMImage image = new UMImage(activity, thumb);
        music.setTitle(title);//标题
        music.setThumb(image);  //缩略图
        music.setDescription(des);//描述

        new ShareAction(activity)
                .setPlatform(share_media)
                .setCallback(new MyShareListener(activity, statistics))
                .withMedia(music)
                .share();

    }
}
