package com.ez08.compass.ui.market.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.ViewGroup;

import com.ez08.compass.entity.PlateMarketEntity;
import com.ez08.compass.ui.base.BaseAdapter;
import com.ez08.compass.ui.base.BaseViewHolder;
import com.ez08.compass.ui.market.holder.DingPanItemHolder;
import com.ez08.compass.ui.market.holder.DingpanTailHolder;

import java.security.InvalidParameterException;
import java.util.List;

public class DingpanAdapter extends BaseAdapter<List<PlateMarketEntity>> {

    public static final int CELL = 1;
    public static final int TAIL = 2;

    String[] valueHighs;
    String[] valueLows;
    boolean state = true;

    public DingpanAdapter(Context context) {
        super(context);
    }

    public void addHighAndLow(String[] valueHighs,String[] valueLows){
        this.valueHighs = valueHighs;
        this.valueLows = valueLows;
    }

    public void changeState(boolean flag){
        state = flag;
    }

    @Override
    public int getViewType(int position) {
        if(getItem(position).isEmpty()){
            return TAIL;
        }
        return CELL;
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case CELL:
                return new DingPanItemHolder(parent);
            case TAIL:
                return new DingpanTailHolder(parent);
            default:
                throw new InvalidParameterException();
        }
    }

    @Override
    public void OnBindViewHolder(BaseViewHolder holder, int position) {
        if(holder instanceof DingPanItemHolder) {
            ((DingPanItemHolder) holder).setHighsAndLows(valueHighs, valueLows);
            ((DingPanItemHolder) holder).setState(state);

        }
        super.OnBindViewHolder(holder, position);
    }
}
