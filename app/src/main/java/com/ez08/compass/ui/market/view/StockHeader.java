package com.ez08.compass.ui.market.view;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ez08.compass.CompassApp;
import com.ez08.compass.R;
import com.ez08.compass.entity.ItemStock;
import com.ez08.compass.entity.StockCodeEntity;
import com.ez08.compass.tools.StockUtils;
import com.ez08.compass.ui.stocks.StockVertcialTabActivity;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class StockHeader extends LinearLayout implements OnClickListener {
    private Context mContext;
    private LinearLayout mShLayout;
    private LinearLayout mSzLayout;
    private LinearLayout mCyLayout;
    private LinearLayout mZxbzLayout;
    private TextView mShPriceTv;
    private TextView mSzPriceTv;
    private TextView mCyPriceTv;
    private TextView mZxbzPriceTv;
    private TextView mShIncreaseTv;
    private TextView mSzIncreaseTv;
    private TextView mCyIncreaseTv;
    private TextView mZxbzIncreaseTv;
    private List<ItemStock> mTempList = new ArrayList<ItemStock>();
    private int colorNormal,redColor,greenColor;
    public StockHeader(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
        mContext = context;
        TypedArray a = mContext.obtainStyledAttributes(null, R.styleable.main_attrs,0, 0);
        colorNormal = getResources().getColor(a.getResourceId(R.styleable.main_attrs_shadow_white_color,0));
        redColor = getResources().getColor(a.getResourceId(R.styleable.main_attrs_red_main_color,0));
        greenColor=getResources().getColor(a.getResourceId(R.styleable.main_attrs_green_main_color,0));
    }

    public void init() {
        mShLayout = (LinearLayout) findViewById(R.id.sh_layout);
        mSzLayout = (LinearLayout) findViewById(R.id.sz_layout);
        mCyLayout = (LinearLayout) findViewById(R.id.cy_layout);
        mZxbzLayout = (LinearLayout) findViewById(R.id.zxbz_layout);


        mShLayout.setOnClickListener(this);
        mSzLayout.setOnClickListener(this);
        mCyLayout.setOnClickListener(this);
        mZxbzLayout.setOnClickListener(this);

        mShPriceTv = (TextView) findViewById(R.id.sh_price);
        mSzPriceTv = (TextView) findViewById(R.id.sz_price);
        mCyPriceTv = (TextView) findViewById(R.id.cy_price);
        mZxbzPriceTv = (TextView) findViewById(R.id.zxbz_price);

        mShIncreaseTv = (TextView) findViewById(R.id.sh_increase);
        mSzIncreaseTv = (TextView) findViewById(R.id.sz_increase);
        mCyIncreaseTv = (TextView) findViewById(R.id.cy_increase);
        mZxbzIncreaseTv = (TextView) findViewById(R.id.zxbz_increase);

        ItemStock lShItem = new ItemStock();
        ItemStock lSzItem = new ItemStock();
        // 鍒涗笟鏉�
        ItemStock lCyItem = new ItemStock();
        ItemStock lZxbzItem = new ItemStock();
        lShItem.setCode("SHHQ000001");
        lShItem.setName("上证指数");
        lShItem.setType(3);

        lSzItem.setCode("SZHQ399001");
        lSzItem.setName("深证成指");
        lSzItem.setType(3);

        lCyItem.setCode("SZHQ399006");
        lCyItem.setName("创业板指");
        lCyItem.setType(3);

        lZxbzItem.setCode("SZHQ399005");
        lZxbzItem.setName("中小板指");
        lZxbzItem.setType(3);


        mTempList.add(lShItem);
        mTempList.add(lSzItem);
        mTempList.add(lCyItem);
        mTempList.add(lZxbzItem);

    }

    public ArrayList<ItemStock> getStockList() {

        return (ArrayList<ItemStock>) mTempList;
    }

    public void setHeader() {
        if (mTempList != null && mTempList.size() > 3) {
            ItemStock lShItem = mTempList.get(0);
            ItemStock lSzItem = mTempList.get(1);
            ItemStock lCyItem = mTempList.get(2);

            ItemStock lZxbzItem = mTempList.get(3);
            setData(lShItem, mShPriceTv, mShIncreaseTv);
            setData(lSzItem, mSzPriceTv, mSzIncreaseTv);
            setData(lCyItem, mCyPriceTv, mCyIncreaseTv);
            setData(lZxbzItem, mZxbzPriceTv, mZxbzIncreaseTv);
        }
    }

    private void setData(ItemStock item, TextView lPriceTv, TextView lIncreaseTv) {
        if (TextUtils.isEmpty(item.getValue())
                || item.getValue().equals("0.00")
                || item.getValue().equals("0.0")) {
            // ------------zhangchungang-----------------
            lPriceTv.setText(item.getOld());
            lIncreaseTv.setText(item.getOld());
            lPriceTv.setTextColor(colorNormal);
            lIncreaseTv.setTextColor(colorNormal);
        }
        if (TextUtils.isEmpty(item.getIncrease())) {
            item.setIncrease("0.0000");
        }
        float value = 0;
        float old = 0;
        try {
            value = Float.parseFloat(item.getValue());
            old = Float.parseFloat(item.getOld());
        } catch (NumberFormatException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        String result;
        if(value > 1) {
            DecimalFormat df1 = new DecimalFormat("0.00");
            result = df1.format(value);
        }else{
            result = "0.00";
        }

        lPriceTv.setText(result);
        float increase = Float.parseFloat(item.getIncrease());
        increase = increase * 100; // 鐧惧垎鏁�
        int type = item.getIsLongPrice() == 0 ? 100 : 1000;
        increase = (float) (Math.round(increase * type)) / type;
        float increaseValue = value - old;
        increaseValue = (float) (Math.round(increaseValue * type)) / type;

        String inside = "";
        inside = "" + increase;
        String drag[] = inside.split("\\.");
        if (drag != null && drag.length > 1) {
            String lDrag = drag[1];
            if (lDrag.length() < 2) {
                if (type == 100) {
                    lDrag = lDrag + "0";
                } else {
                    lDrag = lDrag + "00";
                }
                inside = drag[0] + "." + lDrag;
            }
        }
        String valueSide = "";
        valueSide = "" + increaseValue;
        String dragvalue[] = valueSide.split("\\.");
        if (dragvalue != null && dragvalue.length > 1) {
            String lDragValue = dragvalue[1];
            if (lDragValue.length() < 2) {
                if (type == 100) {
                    lDragValue = lDragValue + "0";
                } else {
                    lDragValue = lDragValue + "00";
                }
                valueSide = dragvalue[0] + "." + lDragValue;
            }
        }

        if (value - old > 0) {
            lPriceTv.setTextColor(redColor);
            inside = "+" + (inside) + "%";
            valueSide = "+" + (valueSide);
            lIncreaseTv.setTextColor(redColor);
        } else if (value - old < 0) {
            inside = (inside) + "%";
            lPriceTv.setTextColor(greenColor);
            lIncreaseTv.setTextColor(greenColor);
        } else {
            inside = (inside) + "%";
            lPriceTv.setTextColor(colorNormal);
            lIncreaseTv.setTextColor(colorNormal);
        }
        while (inside.length() < 7) {
            inside = " " + inside;
        }
        lIncreaseTv.setText(valueSide +" " +inside + "");

        if ("0".equalsIgnoreCase(item.getValue())
                || "0.0".equalsIgnoreCase(item.getValue())
                || "0.00".equalsIgnoreCase(item.getValue())
                || "0".equalsIgnoreCase(item.getAmount())
                || "0.0".equalsIgnoreCase(item.getAmount())
                || "0.00".equalsIgnoreCase(item.getAmount())) {

            lPriceTv.setText(item.getOld());
            lIncreaseTv.setText("0.00 0.00%");
            lPriceTv.setTextColor(colorNormal);
            lIncreaseTv.setTextColor(colorNormal);
        }

        int curTime;
        String time = new SimpleDateFormat("HH:mm:ss").format(new Date());
        time = time.substring(0, 2) + time.substring(3, 5) + time.substring(6);
        curTime = Integer.parseInt(time);
        if (curTime > 90000 && curTime < 92500) {
            lIncreaseTv.setText("-- --");
            lPriceTv.setTextColor(colorNormal);
            lIncreaseTv.setTextColor(colorNormal);
        }
    }

    @Override
    public void onClick(View arg0) {
        // TODO Auto-generated method stub
        ItemStock item = null;
        Intent intent = new Intent(mContext, StockVertcialTabActivity.class);
        switch (arg0.getId()) {
            case R.id.sh_layout:
                item = mTempList.get(0);

                break;
            case R.id.sz_layout:
                item = mTempList.get(1);

                break;
            case R.id.cy_layout:
                item = mTempList.get(2);

                break;
            case R.id.zxbz_layout:
                item = mTempList.get(3);

                break;


            default:
                break;
        }

        CompassApp.GLOBAL.mStockList.clear();
        String code = item.getCode();
        code = StockUtils.getStockCode(code);
        StockCodeEntity entitye = new StockCodeEntity();
        entitye.code=code;
        List<String> codes = new ArrayList<String>();
        entitye.codes = codes;
        CompassApp.GLOBAL.mStockList.add(entitye);
        intent.putExtra("plate_index", true);
        mContext.startActivity(intent);

    }



}
