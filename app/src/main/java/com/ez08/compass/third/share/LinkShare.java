package com.ez08.compass.third.share;

import android.app.Activity;

import com.ez08.compass.R;
import com.ez08.compass.third.MyShareListener;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

public class LinkShare implements Share{

    String title;
    String thumb;
    String des;
    String url;
    public String id;

    public LinkShare(String title, String thumb, String des, String url, String id) {
        this.title = title;
        this.thumb = thumb;
        this.des = des;
        this.url = url;
        this.id = id;
    }

    @Override
    public void invoke(Activity activity, SHARE_MEDIA share_media, String statistics) {
        UMWeb web = new UMWeb(url);
        UMImage image;
        if(thumb == null || thumb.equals("")){
            image = new UMImage(activity, R.drawable.logo_);
        }else {
            image = new UMImage(activity, thumb);
        }
        web.setTitle(title);//标题
        web.setThumb(image);  //缩略图
        web.setDescription(des);//描述

        new ShareAction(activity)
                .setPlatform(share_media)
                .setCallback(new MyShareListener(activity, statistics))
                .withMedia(web)
                .share();

    }
}
