package com.ez08.compass.ui.market.holder;

import android.view.ViewGroup;
import android.widget.TextView;

import com.ez08.compass.R;
import com.ez08.compass.entity.ChartsHolderEntity;
import com.ez08.compass.ui.base.BaseViewHolder;

public class ChartsLeaderHolder extends BaseViewHolder<ChartsHolderEntity> {

    TextView peopleName;
    TextView stockName;
    TextView stockCode;
    TextView position;
    TextView count;
    TextView reason;

    public ChartsLeaderHolder(ViewGroup itemView) {
        super(itemView, R.layout.holder_leader_hold);
        peopleName = $(R.id.holder_person);
        stockName = $(R.id.holder_name);
        stockCode = $(R.id.holder_code);
        position = $(R.id.holder_position);
        count = $(R.id.holder_number);
        reason = $(R.id.holder_reason);
    }

    @Override
    public void setData(ChartsHolderEntity data) {
        peopleName.setText(data.getPerson());
        stockName.setText(data.getName());
        if(data.getCode() != null && data.getCode().length() > 4) {
            String code = data.getCode().substring(4);
            stockCode.setText(code);
        }
        position.setText(data.getPosition());
        count.setText(data.getNumber() + "");
        reason.setText(data.getReason());
    }
}
