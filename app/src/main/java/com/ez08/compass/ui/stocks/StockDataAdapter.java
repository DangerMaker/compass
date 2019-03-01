package com.ez08.compass.ui.stocks;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ez08.compass.R;
import com.ez08.compass.entity.StockDataEntity;
import com.ez08.compass.ui.base.BaseAdapter;
import com.ez08.compass.ui.base.BaseViewHolder;

public class StockDataAdapter extends BaseAdapter<StockDataEntity> {


    public StockDataAdapter(Context context) {
        super(context);
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new DataHolder(parent);
    }



    public class DataHolder extends BaseViewHolder<StockDataEntity>{

        TextView dataTitle;
        TextView dataContent;
        public DataHolder(ViewGroup itemView) {
            super(itemView, R.layout.holder_stock_data);
            dataTitle = $(R.id.data_title);
            dataContent = $(R.id.data_content);
        }

        @Override
        public void setData(StockDataEntity data) {
            dataTitle.setText(data.getTitle());
            dataContent.setText(data.getContent());
        }
    }
}



