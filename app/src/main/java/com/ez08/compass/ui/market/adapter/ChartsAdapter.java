package com.ez08.compass.ui.market.adapter;

import android.content.Context;
import android.view.ViewGroup;

import com.ez08.compass.entity.ChartsHolderEntity;
import com.ez08.compass.entity.ChartsLeaderTitleEntity;
import com.ez08.compass.entity.HotModel;
import com.ez08.compass.entity.StockMarketEntity;
import com.ez08.compass.entity.HolderTitleAndAll;
import com.ez08.compass.entity.HolderOnlyTitle;
import com.ez08.compass.entity.HolderChartsNetWord;
import com.ez08.compass.ui.base.BaseAdapter;
import com.ez08.compass.ui.base.BaseViewHolder;
import com.ez08.compass.ui.market.holder.ChartsIndustryHolder;
import com.ez08.compass.ui.market.holder.SingleTitleHolder;
import com.ez08.compass.ui.market.holder.ChartsLeaderHolder;
import com.ez08.compass.ui.market.holder.ChartsLeaderTitleHolder;
import com.ez08.compass.ui.market.holder.ChartsPopularHolder;
import com.ez08.compass.ui.market.holder.WatchMarketHolder;
import com.ez08.compass.ui.market.holder.TitleAndAllHolder;

import java.security.InvalidParameterException;

public class ChartsAdapter extends BaseAdapter<Object> {

    public static final int MARKET_TITLE = 1;
    public static final int MARKET = 2; //股票
    public static final int LEADER_TITLE = 3;
    public static final int LEADER = 4;
    public static final int INDUSTRY = 5;
    public static final int INDUSTRY_TITLE = 6;
    public static final int POPULAR = 7;

    public ChartsAdapter(Context context) {
        super(context);
    }

    @Override
    public int getViewType(int position) {
        if(getItem(position) instanceof HolderTitleAndAll){
            return MARKET_TITLE;
        }else if(getItem(position) instanceof ChartsLeaderTitleEntity){
            return LEADER_TITLE;
        }else if(getItem(position) instanceof StockMarketEntity){
            return MARKET;
        }else if(getItem(position) instanceof ChartsHolderEntity){
            return LEADER;
        }else if (getItem(position) instanceof HotModel){
            return INDUSTRY;
        }else if (getItem(position) instanceof HolderOnlyTitle){
            return INDUSTRY_TITLE;
        }else if (getItem(position) instanceof HolderChartsNetWord){
            return POPULAR;
        }
        else{
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
            case LEADER_TITLE:
                return new ChartsLeaderTitleHolder(parent);
            case LEADER:
                return new ChartsLeaderHolder(parent);
            case INDUSTRY:
                return new ChartsIndustryHolder(parent);
            case INDUSTRY_TITLE:
                return new SingleTitleHolder(parent);
            case POPULAR:
                return new ChartsPopularHolder(parent);
            default:
                throw new InvalidParameterException();
        }
    }

}
