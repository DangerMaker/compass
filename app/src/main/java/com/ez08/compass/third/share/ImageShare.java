package com.ez08.compass.third.share;

import android.app.Activity;

import com.ez08.compass.R;
import com.ez08.compass.third.MyShareListener;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;

import java.io.File;

public class ImageShare implements Share {

    String imagePath;

    public ImageShare(String imagePath) {
        this.imagePath = imagePath;
    }

    @Override
    public void invoke(Activity activity, SHARE_MEDIA share_media, String statistics) {
        UMImage image;
        if(imagePath != null){
            if(imagePath.startsWith("http")){
                image = new UMImage(activity,imagePath);
            }else {
                image = new UMImage(activity, new File(imagePath));//本地文件
            }
        }else {
            return;
        }
        image.setThumb(new UMImage(activity, R.drawable.thumb));

        new ShareAction(activity)
                .setPlatform(share_media)
                .setCallback(new MyShareListener(activity,statistics))
                .withText("股海茫茫，为您指南")
                .withMedia(image)
                .share();

    }
}
