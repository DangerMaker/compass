package com.ez08.compass.ui.stocks;

import android.os.Bundle;
import android.view.WindowManager;

import com.ez08.compass.R;
import com.ez08.compass.ui.base.BaseActivity;

public class StockHorizontalActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_stock_horizontal);
    }
}
