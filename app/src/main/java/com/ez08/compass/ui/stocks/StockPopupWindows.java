package com.ez08.compass.ui.stocks;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;

import com.ez08.compass.R;
import com.ez08.compass.entity.StockDetailEntity;
import com.ez08.compass.parser.IndicatorHelper;
import com.ez08.compass.ui.base.BasePopupWindows;

public class StockPopupWindows extends BasePopupWindows<StockDetailEntity> implements View.OnClickListener {

    RecyclerView recyclerView;
    StockDataAdapter dataAdapter;
    GridLayoutManager layoutManager;
    RelativeLayout shadow;


    public StockPopupWindows(Context context) {
        super(context);
    }

    @Override
    protected void onCreateView(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        shadow = (RelativeLayout) view.findViewById(R.id.shadow);
        shadow.setOnClickListener(this);
        layoutManager = new GridLayoutManager(context, 2);
        recyclerView.setLayoutManager(layoutManager);
        dataAdapter = new StockDataAdapter(context);
        recyclerView.setAdapter(dataAdapter);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.popup_stock_detail;
    }

    @Override
    protected void setData(StockDetailEntity data) {
        if (data == null) {
            return;
        }

        IndicatorHelper helper = new IndicatorHelper(data);
        helper.toPrint();
        dataAdapter.addAll(helper.getStockDetailShow());

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.shadow:
                this.dismiss();
                break;
        }
    }
}
