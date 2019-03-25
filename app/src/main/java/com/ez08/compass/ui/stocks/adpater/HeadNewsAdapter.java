package com.ez08.compass.ui.stocks.adpater;

import android.content.Context;
import android.view.ViewGroup;

import com.ez08.compass.entity.InfoEntity;
import com.ez08.compass.ui.base.BaseAdapter;
import com.ez08.compass.ui.base.BaseViewHolder;

public class HeadNewsAdapter extends BaseAdapter<InfoEntity> {
    public HeadNewsAdapter(Context context) {
        super(context);
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new HeadNewsHolder(parent);
    }
}
