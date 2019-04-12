package com.ez08.compass.ui.stocks.view;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ez08.compass.CompassApp;
import com.ez08.compass.R;
import com.ez08.compass.ui.base.CustomPopupWindows;

public class StockTabLayout extends LinearLayout implements View.OnClickListener {

    Context context;
    TextView timeshareTV;
    TextView dayTV;
    TextView weekTV;
    TextView monthTV;
    TextView minuteTV;

    View timeshareLine;
    View dayLine;
    View weekLine;
    View monthLine;
    View minuteLine;
    int mPopType;
    int gray;

    public StockTabLayout(Context context) {
        super(context);
        this.context = context;
    }

    public StockTabLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        gray = Color.parseColor("#666666");
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        timeshareTV = findViewById(R.id.timeshare);
        dayTV = findViewById(R.id.day);
        weekTV = findViewById(R.id.week);
        monthTV = findViewById(R.id.month);
        minuteTV = findViewById(R.id.minute);

        timeshareLine = findViewById(R.id.timeshare_line);
        dayLine = findViewById(R.id.day_line);
        weekLine = findViewById(R.id.week_line);
        monthLine = findViewById(R.id.month_line);
        minuteLine = findViewById(R.id.minute_line);

        for (int i = 0; i < getChildCount(); i++) {
            getChildAt(i).setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        if (v == getChildAt(0)) {
            reset(0);
        } else if (v == getChildAt(1)) {
            reset(1);
        } else if (v == getChildAt(2)) {
            reset(2);
        } else if (v == getChildAt(3)) {
            reset(3);
        } else if (v == getChildAt(4)) {
            reset(4);
        }
    }


    public void reset(int i) {
        switch (i) {
            case 0:
                timeshareTV.setTextColor(Color.RED);
                dayTV.setTextColor(gray);
                weekTV.setTextColor(gray);
                monthTV.setTextColor(gray);
                minuteTV.setTextColor(gray);

                timeshareLine.setVisibility(VISIBLE);
                dayLine.setVisibility(INVISIBLE);
                weekLine.setVisibility(INVISIBLE);
                monthLine.setVisibility(INVISIBLE);
                minuteLine.setVisibility(INVISIBLE);
                mPopType = 0;
                handler.sendEmptyMessage(mPopType);
                break;
            case 1:
                timeshareTV.setTextColor(gray);
                dayTV.setTextColor(Color.RED);
                weekTV.setTextColor(gray);
                monthTV.setTextColor(gray);
                minuteTV.setTextColor(gray);

                timeshareLine.setVisibility(INVISIBLE);
                dayLine.setVisibility(VISIBLE);
                weekLine.setVisibility(INVISIBLE);
                monthLine.setVisibility(INVISIBLE);
                minuteLine.setVisibility(INVISIBLE);
                mPopType = 1;
                handler.sendEmptyMessage(mPopType);
                break;
            case 2:
                timeshareTV.setTextColor(gray);
                dayTV.setTextColor(gray);
                weekTV.setTextColor(Color.RED);
                monthTV.setTextColor(gray);
                minuteTV.setTextColor(gray);

                timeshareLine.setVisibility(INVISIBLE);
                dayLine.setVisibility(INVISIBLE);
                weekLine.setVisibility(VISIBLE);
                monthLine.setVisibility(INVISIBLE);
                minuteLine.setVisibility(INVISIBLE);
                mPopType = 2;
                handler.sendEmptyMessage(mPopType);
                break;
            case 3:
                timeshareTV.setTextColor(gray);
                dayTV.setTextColor(gray);
                weekTV.setTextColor(gray);
                monthTV.setTextColor(Color.RED);
                minuteTV.setTextColor(gray);

                timeshareLine.setVisibility(INVISIBLE);
                dayLine.setVisibility(INVISIBLE);
                weekLine.setVisibility(INVISIBLE);
                monthLine.setVisibility(VISIBLE);
                minuteLine.setVisibility(INVISIBLE);
                mPopType = 3;
                handler.sendEmptyMessage(mPopType);
                break;
            case 4:
                showMinPop(getChildAt(4));
                break;
        }
    }


    Handler handler;

    public void setHandler(Handler handler) {  // handler what == {0,fenshi},{1,day},{2,week},{3,month},{4,1min},{5,3min},{6,5min},{7,10min},{8,15min},{9,30min},{10,60min}
        this.handler = handler;
    }

    CustomPopupWindows popupWindow;

    private void showMinPop(View view) {
        View popupView = View.inflate(context, R.layout.stock_hor_select_layout, null);
        LinearLayout layout = (LinearLayout) popupView.findViewById(R.id.stock_popup_layout);
        if (CompassApp.GLOBAL.THEME_STYLE == 0) {
            layout.setBackgroundResource(R.drawable.stock_hor_select_bg);
        } else {
            layout.setBackgroundResource(R.drawable.stock_hor_bar_bg_night);
        }
        popupWindow = new CustomPopupWindows(context, popupView, view.getWidth(),
                ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setWidth(view.getWidth());
        popupWindow.showAsDropDown(view);

        TextView item0 = (TextView) popupView.findViewById(R.id.stock_hor_min0);
        TextView item1 = (TextView) popupView.findViewById(R.id.stock_hor_min1);
        TextView item2 = (TextView) popupView.findViewById(R.id.stock_hor_min2);
        TextView item3 = (TextView) popupView.findViewById(R.id.stock_hor_min3);
        TextView item4 = (TextView) popupView.findViewById(R.id.stock_hor_min4);
        TextView item5 = (TextView) popupView.findViewById(R.id.stock_hor_min5);
        TextView item6 = (TextView) popupView.findViewById(R.id.stock_hor_min6);
        item0.setOnClickListener(popClickListener);
        item1.setOnClickListener(popClickListener);
        item2.setOnClickListener(popClickListener);
        item3.setOnClickListener(popClickListener);
        item4.setOnClickListener(popClickListener);
        item5.setOnClickListener(popClickListener);
        item6.setOnClickListener(popClickListener);
    }

    private View.OnClickListener popClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.stock_hor_min0:
                    mPopType = 4;
                    minuteTV.setText("1分");
                    break;
                case R.id.stock_hor_min1:
                    mPopType = 5;
                    minuteTV.setText("3分");
                    break;
                case R.id.stock_hor_min2:
                    mPopType = 6;
                    minuteTV.setText("5分");
                    break;
                case R.id.stock_hor_min3:
                    mPopType = 7;
                    minuteTV.setText("10分");
                    break;
                case R.id.stock_hor_min4:
                    mPopType = 8;
                    minuteTV.setText("15分");
                    break;
                case R.id.stock_hor_min5:
                    mPopType = 9;
                    minuteTV.setText("30分");
                    break;
                case R.id.stock_hor_min6:
                    mPopType = 10;
                    minuteTV.setText("60分");
                    break;
            }

            handler.sendEmptyMessage(mPopType);
            popupWindow.dismiss();
            timeshareTV.setTextColor(gray);
            dayTV.setTextColor(gray);
            weekTV.setTextColor(gray);
            monthTV.setTextColor(gray);
            minuteTV.setTextColor(Color.RED);

            timeshareLine.setVisibility(INVISIBLE);
            dayLine.setVisibility(INVISIBLE);
            weekLine.setVisibility(INVISIBLE);
            monthLine.setVisibility(INVISIBLE);
            minuteLine.setVisibility(VISIBLE);
        }
    };


}
