package com.ez08.compass.ui.media;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.ez08.compass.R;
import com.ez08.compass.ui.base.BaseActivity;
import com.ez08.compass.ui.view.ResizeLayout;
import com.ez08.compass.ui.view.input.BottomPanelFragment;

public class ClassRoomActivity extends BaseActivity implements View.OnClickListener {

    public static final String TAG = "ClassRoomActivity";

    private BottomPanelFragment bottomPanel;
    ResizeLayout background;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ativity_class_room);
        bottomPanel = (BottomPanelFragment) getSupportFragmentManager().findFragmentById(R.id.bottom_bar);
        background = findViewById(R.id.background);
        background.setOnClickListener(this);
        listView = findViewById(R.id.list_view);
        listView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                bottomPanel.onBackAction();
                return false;
            }
        });

        background.setOnResizeListener(new ResizeLayout.OnResizeListener() {
            @Override
            public void OnResize(int w, int h, int oldw, int oldh) {
                if (h < oldh) {
                    handler.sendEmptyMessageDelayed(0,200);
                }
            }
        });
    }


    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            bottomPanel.hideEmojiBroad();
        }
    };


    @Override
    public void onBackPressed() {
        if (!bottomPanel.onBackAction()) {
            finish();
            return;
        }
    }

    @Override
    public void onClick(View v) {
        if (v == background || v == listView) {
            bottomPanel.onBackAction();
        }
    }
}
