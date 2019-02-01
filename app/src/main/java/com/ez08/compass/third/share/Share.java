package com.ez08.compass.third.share;

import android.app.Activity;

import com.umeng.socialize.bean.SHARE_MEDIA;

public interface Share {
    void invoke(Activity activity, SHARE_MEDIA share_media, String statistics);
}
