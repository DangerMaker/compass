package com.ez08.compass.ui.market.adapter;

import android.content.Context;
import android.view.ViewGroup;

import com.ez08.compass.entity.Mix3Entity;
import com.ez08.compass.entity.StockMarketEntity;
import com.ez08.compass.entity.HolderTitleAndAll;
import com.ez08.compass.entity.HolderWatchMixTitle;
import com.ez08.compass.entity.HolderOnlyTitle;
import com.ez08.compass.ui.base.BaseAdapter;
import com.ez08.compass.ui.base.BaseViewHolder;
import com.ez08.compass.ui.market.holder.SingleTitleHolder;
import com.ez08.compass.ui.market.holder.WatchMarketHolder;
import com.ez08.compass.ui.market.holder.TitleAndAllHolder;
import com.ez08.compass.ui.market.holder.WatchMixHolder;
import com.ez08.compass.ui.market.holder.WatchMixTitleHolder;

import java.security.InvalidParameterException;

public class WatchAdapter extends BaseAdapter<Object> {

    public static final int MARKET_TITLE = 1;
    public static final int MARKET = 2; //股票
    public static final int MIX_TITLE = 3;
    public static final int MIX = 4; //交叉
    public static final int MIX_SUB_TITLE = 5;

    public WatchAdapter(Context context) {
        super(context);
    }

    @Override
    public int getViewType(int position) {
        if(getItem(position) instanceof HolderTitleAndAll){
            return MARKET_TITLE;
        } else if(getItem(position) instanceof StockMarketEntity){
            return MARKET;
        }else if(getItem(position) instanceof HolderOnlyTitle){
            return MIX_TITLE;
        } else if(getItem(position) instanceof HolderWatchMixTitle){
            return MIX_SUB_TITLE;
        }else if(getItem(position) instanceof Mix3Entity){
            return MIX;
        } else{
            return 0;
        }
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case MARKET_TITLE:
                return new TitleAndAllHolder(parent);
            case MARKET:
                return new WatchMarketHolder(parent);
            case MIX_TITLE:
                return new SingleTitleHolder(parent);
            case MIX_SUB_TITLE:
                return new WatchMixTitleHolder(parent);
            case MIX:
                return new WatchMixHolder(parent);
            default:
                throw new InvalidParameterException();
        }
    }

}
