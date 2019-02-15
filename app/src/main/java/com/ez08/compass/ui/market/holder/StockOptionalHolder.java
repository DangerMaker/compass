package com.ez08.compass.ui.market.holder;

import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ez08.compass.CompassApp;
import com.ez08.compass.R;
import com.ez08.compass.entity.ItemStock;
import com.ez08.compass.event.OptionalPriceModeEvent;
import com.ez08.compass.tools.MyAppCompat;
import com.ez08.compass.tools.TimeTool;
import com.ez08.compass.tools.UtilTools;
import com.ez08.compass.ui.base.BaseViewHolder;

import org.greenrobot.eventbus.EventBus;

public class StockOptionalHolder extends BaseViewHolder<ItemStock> {

    TextView nameTV;
    TextView codeTV;
    TextView valueTV;
    TextView percentTV;

    boolean mSetPriceValue = false;

    public StockOptionalHolder(ViewGroup itemView) {
        super(itemView, R.layout.holder_optional_stock);
        nameTV = $(R.id.txt_name);
        codeTV = $(R.id.txt_code);
        valueTV = $(R.id.txt_value);
        percentTV = $(R.id.txt_increase);
    }

    public void setPriceValue(boolean flag){
        mSetPriceValue = flag;
    }

    @Override
    public void setData(ItemStock sc) {
        if (sc.getCode().length() == 0) {
            codeTV.setText("— —");
            nameTV.setText("— —");
            setValueInvalid();
            return;
        }

        nameTV.setText(sc.getName());
        codeTV.setText(sc.getCode().substring(4));
        valueTV.setText(sc.getValue());
        if (TextUtils.isEmpty(sc.getIncrease())) {
            sc.setIncrease("0.0000");
        }
        float increase = Float.parseFloat(sc.getIncrease());
        increase = increase * 100; // 百分数
        increase = UtilTools.getRoundingNum(increase, 2);
        String inside = UtilTools.getFormatNum(increase + "", 2, true);

        if (increase > 0) {
            percentTV.setBackgroundColor(CompassApp.GLOBAL.RED);
            valueTV.setTextColor(CompassApp.GLOBAL.RED);
            inside = "+" + (inside) + "%";
        } else if (increase == 0) {
            inside = (inside) + "%";
            percentTV.setBackgroundColor(MyAppCompat.getColor(getContext(), R.attr.stock_white_color));
            valueTV.setTextColor(MyAppCompat.getColor(getContext(), R.attr.shadow0));
        } else {
            inside = (inside) + "%";
            percentTV.setBackgroundColor(CompassApp.GLOBAL.GREEN);
            valueTV.setTextColor(CompassApp.GLOBAL.GREEN);
        }
        while (inside.length() < 7) {
            inside = " " + inside;
        }

        if (mSetPriceValue) {
            percentTV.setText(sc.getIncresePrice() + "  ");
        } else {
            percentTV.setText(inside);
        }

        percentTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //通知主界面 percent to price immediately and notify
                mSetPriceValue = !mSetPriceValue;
                EventBus.getDefault().post(new OptionalPriceModeEvent(mSetPriceValue));
            }
        });

        if (TimeTool.isInTotalTrade()) { // 集合竞价阶段，如果股票价格不为0，就显示价格，不区分是否停牌
            setValueInvalid();
        } else {
            if (TextUtils.isEmpty(sc.getAmount())
                    || "0".equalsIgnoreCase(sc.getAmount())
                    || "0.0".equalsIgnoreCase(sc.getAmount())
                    || "0.00".equalsIgnoreCase(sc.getAmount())
                    || TextUtils.isEmpty(sc.getValue())
                    || "0".equalsIgnoreCase(sc.getValue())
                    || "0.0".equalsIgnoreCase(sc.getValue())
                    || "0.00".equalsIgnoreCase(sc.getValue())) {
                setValueInvalid();
            }

        }
        String value = sc.getValue();
        if ((sc.getState() != 0 && sc.getState() != 2) || (value.equals("0.00") || value.equals("0.000"))) { //不是开盘和临时停牌
            setValueInvalid();
        }
//        itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                CompassApp.GLOBAL.mStockList.clear();
//                StockCodeEntity entity = new StockCodeEntity();
//                entity.code = arr.get(position).getCode();
//                List<String> codes = new ArrayList<String>();
//                for (int i = 0; i < arr.size(); i++) {
//                    codes.add(arr.get(i).getCode());
//                }
//                entity.codes = codes;
//                CompassApp.mStockList.add(entity);
//                Intent intent = new Intent(context,
//                        StockVertcialTabActivity.class);
//                context.startActivity(intent);
//            }
//        });

//        if (mOnItemLongClickListener != null) {
//            ((OptionListHolder) holder).rl_stock_info_item.setOnLongClickListener(new View.OnLongClickListener() {
//                @Override
//                public boolean onLongClick(View v) {
//                    if (status != 0) {
//                        return true;
//                    }
//
//                    int position = holder.getLayoutPosition();
//                    mOnItemLongClickListener.onItemLongClick(((OptionListHolder) holder).rl_stock_info_item, position);
//                    //返回true 表示消耗了事件 事件不会继续传递
//                    return true;
//                }
//            });
//        }
    }

    private void setValueInvalid(){
        valueTV.setText("— —");
        valueTV.setTextColor(MyAppCompat.getColor(getContext(), R.attr.stock_white_color));
        percentTV.setText("— —");
        percentTV.setBackgroundColor(MyAppCompat.getColor(getContext(), R.attr.stock_white_color));
    }
}
