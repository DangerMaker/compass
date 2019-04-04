package com.ez08.compass.ui.market.adapter;

import android.content.Context;
import android.view.ViewGroup;

import com.ez08.compass.entity.ItemStock;
import com.ez08.compass.ui.base.BaseAdapter;
import com.ez08.compass.ui.base.BaseViewHolder;
import com.ez08.compass.ui.market.holder.StockOptionalHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/8/20.
 */


public class OptionalAdapter extends BaseAdapter<ItemStock> {

    boolean mSetPriceValue;
    public static ArrayList<String> myCodes;

    public OptionalAdapter(Context context, List<ItemStock> mList,boolean flag) {
        super(context,mList);
        mSetPriceValue = flag;
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new StockOptionalHolder(parent);
    }

    @Override
    public void OnBindViewHolder(BaseViewHolder holder, int position) {
        ((StockOptionalHolder)holder).setPriceValue(mSetPriceValue);
        super.OnBindViewHolder(holder, position);
    }

    public void setPriceValue(boolean flag){
        mSetPriceValue = flag;
    }
}