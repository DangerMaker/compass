package com.ez08.compass.third.share;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;

import com.ez08.compass.third.MyShareListener;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.bean.SHARE_MEDIA;

public class TextShare implements Share {

    String text;

    public TextShare(String text) {
        this.text = text;
    }

    @Override
    public void invoke(Activity activity, SHARE_MEDIA share_media, String statistics) {

        if(share_media.equals(SHARE_MEDIA.QQ)){
            Intent intent = new Intent("android.intent.action.SEND");
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_SUBJECT, text + "  来自指南针股票");
            intent.putExtra(Intent.EXTRA_TEXT,text + "  来自指南针股票");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setComponent(new ComponentName("com.tencent.mobileqq","com.tencent.mobileqq.activity.JumpActivity"));
            activity.startActivity(intent);
        }else {
            new ShareAction(activity)
                    .setPlatform(share_media)
                    .setCallback(new MyShareListener(activity, statistics))
                    .withText(text)
                    .share();
        }

    }
}
