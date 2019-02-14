package com.ez08.compass.ui.market.holder;

import android.support.v4.content.ContextCompat;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ez08.compass.R;
import com.ez08.compass.entity.HolderOnlyTitle;
import com.ez08.compass.ui.base.BaseViewHolder;

public class SingleTitleHolder extends BaseViewHolder<HolderOnlyTitle> {

    TextView name;

    public SingleTitleHolder(ViewGroup itemView) {
        super(itemView, R.layout.holder_single_title);
        name = $(R.id.name);
    }

    @Override
    public void setData(HolderOnlyTitle data) {
        name.setText(data.getTitle());
        if(!data.isWhite()){
            itemView.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.shadow1));
        }

    }
}
