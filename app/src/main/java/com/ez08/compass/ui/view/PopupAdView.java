package com.ez08.compass.ui.view;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.android.thinkive.framework.util.ScreenUtil;
import com.ez08.compass.CompassApp;
import com.ez08.compass.R;
import com.ez08.compass.entity.ItemStock;
import com.ez08.compass.ui.WebActivity;
import com.ez08.compass.auth.AuthUserInfo;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class PopupAdView extends RelativeLayout {

    String imageUrl;
    String infoUrl;
    Context mContext;
    ImageView adImageView;
    ImageView deleteView;

    CloseListener listener;
    int sw;

    public PopupAdView(Context context) {
        super(context);
    }

    public PopupAdView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        initView();
    }

    private void initView() {
        sw = (int) ScreenUtil.getScreenWidth(mContext);
        setVisibility(GONE);
    }

    public void setAdVisible(String imageUrl, String infoUrl) {
        setVisibility(VISIBLE);
        this.imageUrl = imageUrl;
        this.infoUrl = infoUrl;
        adImageView = new ImageView(mContext);
        adImageView.setScaleType(ImageView.ScaleType.FIT_XY);
        RelativeLayout.LayoutParams imageParams =  new RelativeLayout.LayoutParams(sw, sw / 5);
        addView(adImageView,imageParams);

        deleteView = new ImageView(mContext);
        deleteView.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ads_shut));
        RelativeLayout.LayoutParams deleteParams = new RelativeLayout.LayoutParams(sw/5/3,sw/5/3);
        deleteParams.addRule(RelativeLayout.ALIGN_PARENT_TOP,10);
        deleteParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT,10);
        deleteParams.topMargin = 10;
        deleteParams.rightMargin = 10;
        addView(deleteView,deleteParams);

        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageForEmptyUri(R.drawable.chaogu)
                .showImageOnFail(R.drawable.chaogu).cacheInMemory(true)
                .cacheOnDisk(true).considerExifParams(true).build();
        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.displayImage(imageUrl, adImageView, options);

        adImageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                startWebActivity();
                CompassApp.addStatis(CompassApp.GLOBAL.mgr.ADVERT_STOCK, "1", "", System.currentTimeMillis());

            }
        });

        deleteView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.invoke();
            }
        });
    }

    public void setAdGone() {
        setVisibility(GONE);
    }

    private void startWebActivity(){
        ItemStock entity = new ItemStock();
        entity.setUrl(this.infoUrl + "?personid="+ AuthUserInfo.getMyCid()  + "&skey=" + AuthUserInfo.getMyToken());
        Intent intentWeb = new Intent(mContext, WebActivity.class);
        intentWeb.putExtra("entity", entity);
        intentWeb.putExtra("ifFromAd", true);
        mContext.startActivity(intentWeb);
    }

    public void setCloseListener(CloseListener listener){
        this.listener = listener;
    }

    public interface CloseListener{
        void invoke();
    }
}
