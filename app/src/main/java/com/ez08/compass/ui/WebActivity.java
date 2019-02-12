package com.ez08.compass.ui;

import android.os.Bundle;
import android.view.View;

import com.ez08.compass.ui.base.BaseActivity;

public class WebActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new View(this));
    }
}
