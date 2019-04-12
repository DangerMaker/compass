package com.ez08.compass.ui.stocks.adpater;

import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ez08.compass.CompassApp;
import com.ez08.compass.R;
import com.ez08.compass.entity.InfoEntity;
import com.ez08.compass.entity.ItemStock;
import com.ez08.compass.tools.UtilTools;
import com.ez08.compass.ui.base.BaseViewHolder;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class HeadNewsHolder extends BaseViewHolder<InfoEntity> {
    public TextView lTitle;
    public TextView lDate;
    public TextView lName;
    public ImageView lImg;

    public HeadNewsHolder(ViewGroup itemView) {
        super(itemView, R.layout.list_style_right_item);
        lTitle = $(R.id.time_name);
        lImg = $(R.id.zixun_img);
        lDate = $(R.id.time_date);
        lName = $(R.id.time_detail);
        if (CompassApp.GLOBAL.THEME_STYLE == 0) {
            lImg.setImageResource(R.drawable.chaogu);
        } else {
            lImg.setImageResource(R.drawable.chaogu_night);
        }
    }

    @Override
    public void setData(InfoEntity data) {
        final InfoEntity info = data;
        lTitle.setText(info.getTitle());
        if (info.getContent() != null) {
            lName.setText(info.getContent());
        }
        SimpleDateFormat formatter = new SimpleDateFormat("MM-dd HH:mm");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(info.getTime());
        String timer = formatter.format(calendar.getTime());
        lDate.setText(timer);

        String imageid = info.getImageid();
        if (!imageid.equals("")) {
            Glide.with(itemView).load(imageid).into(lImg);
        }
    }
}
