package com.ez08.compass.tools;

import android.content.Context;
import android.content.Intent;

import com.ez08.compass.ui.stocks.StockHorizontalActivity;
import com.ez08.compass.ui.stocks.StockVerticalActivity;

import java.util.ArrayList;
import java.util.List;

public class JumpHelper {

    public static void startStockVerticalActivity(Context context, String code, ArrayList<String> list) {
        Intent intent = new Intent(context, StockVerticalActivity.class);
        intent.putExtra("stock_code", code);
        if (list != null)
            intent.putStringArrayListExtra("stock_list", list);
        context.startActivity(intent);
    }

    public static void startStockHorizontalActivity(Context context, String code, ArrayList<String> list) {
        Intent intent = new Intent(context, StockHorizontalActivity.class);
        intent.putExtra("stock_code", code);
        if (list != null)
            intent.putStringArrayListExtra("stock_list", list);
        context.startActivity(intent);
    }
}
