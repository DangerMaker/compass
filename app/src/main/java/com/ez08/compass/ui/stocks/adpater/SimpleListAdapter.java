package com.ez08.compass.ui.stocks.adpater;

import android.content.Context;
import android.view.ViewGroup;

import com.ez08.compass.entity.BaseNewsEntity;
import com.ez08.compass.entity.InfoEntity;
import com.ez08.compass.entity.InfoEntity1;
import com.ez08.compass.entity.ItemStock;
import com.ez08.compass.ui.base.BaseAdapter;
import com.ez08.compass.ui.base.BaseViewHolder;
import com.ez08.compass.ui.stocks.EzParser;
import com.ez08.support.net.EzMessage;

public class SimpleListAdapter extends BaseAdapter<Object> {


    public SimpleListAdapter(Context context) {
        super(context);
    }

    @Override
    public int getItemViewType(int position) {
        if (getItem(position) instanceof BaseNewsEntity) {
            return 0;
        }
        return 1;
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 0)
            return new SimpleHolder(parent);
        else
            return  new LoadMoreHolder(parent);
    }
}
