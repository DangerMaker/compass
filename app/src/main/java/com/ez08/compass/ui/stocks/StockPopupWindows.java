package com.ez08.compass.ui.stocks;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;
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

    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = ((Activity)context).getWindow().getAttributes();
        lp.alpha = bgAlpha; // 0.0~1.0
        ((Activity)context).getWindow().setAttributes(lp); //act 是上下文context

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.shadow:
                this.dismiss();
                break;
        }
    }


    @Override
    public void showPopupWindow(View parent) {
        super.showPopupWindow(parent);
        backgroundAlpha(0.7f);
    }

    @Override
    public void dismiss() {
        super.dismiss();
        backgroundAlpha(1f);
    }

}
