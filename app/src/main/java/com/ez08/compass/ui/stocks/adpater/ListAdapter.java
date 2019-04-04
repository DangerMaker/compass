package com.ez08.compass.ui.stocks.adpater;

import android.content.Context;
import android.view.ViewGroup;

import com.ez08.compass.entity.InfoEntity;
import com.ez08.compass.entity.InfoEntity1;
import com.ez08.compass.entity.ItemStock;
import com.ez08.compass.ui.base.BaseAdapter;
import com.ez08.compass.ui.base.BaseViewHolder;
import com.ez08.compass.ui.stocks.EzParser;
import com.ez08.support.net.EzMessage;

public class ListAdapter<T> extends BaseAdapter<T> {

    public static final int TYPE_HEAD = 1;
    public static final int TYPE_INNER = 2;
    public static final int TYPE_CHANGE = 3;

    public ListAdapter(Context context) {
        super(context);
    }

    @Override
    public int getItemViewType(int position) {
        if(getItem(position) instanceof InfoEntity){
           return TYPE_HEAD;
        }else if(getItem(position) instanceof InfoEntity1){
            return TYPE_INNER;
        }else if(getItem(position) instanceof ItemStock){
            return TYPE_CHANGE;
        }
        return 0;
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == TYPE_HEAD) {
            return new HeadNewsHolder(parent);
        }else if(viewType == TYPE_INNER){
            return new InnerNewsHolder(parent);
        }else if(viewType == TYPE_CHANGE){
            return new ChangeHolder(parent);
        }
        return null;
    }

    public void setEzMessage(EzMessage[] messages, EzParser<T> parser){
        if (messages != null) {
            for (int i = 0; i < messages.length; i++) {
                T entity = parser.invoke(messages[i]);
                mObjects.add(entity);
            }
        }
        notifyDataSetChanged();
    }

}
