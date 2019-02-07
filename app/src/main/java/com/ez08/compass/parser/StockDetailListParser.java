package com.ez08.compass.parser;

import android.content.Intent;
import android.text.TextUtils;

import com.ez08.compass.entity.ItemStock;
import com.ez08.compass.entity.StockMarketEntity;
import com.ez08.compass.tools.StockUtils;
import com.ez08.compass.tools.UtilTools;
import com.ez08.support.net.EzMessage;
import com.ez08.support.util.EzValue;
import com.ez08.tools.IntentTools;

import java.util.List;

/**
 * Created by Administrator on 2017/3/13.
 */

public class StockDetailListParser {
    private List<ItemStock> mStockList;
    private String REQUESTURL = "http://app.compass.cn/stock/s.php?code=";

    public StockDetailListParser(List<ItemStock> mStockList) {
        super();
        this.mStockList = mStockList;
    }

    public List<ItemStock> parserResult(Intent intent) {
        EzValue vaulee = IntentTools.safeGetEzValueFromIntent(
                intent, "list");
        if (vaulee != null) {
            String aa=vaulee.description();
            EzMessage[] msges = vaulee.getMessages();
            if (msges != null) {
                StockMarketParser parser = new StockMarketParser();
                for (int i = 0; i < msges.length; i++) {
                    StockMarketEntity entity = parser.parse(msges[i]);
                    if (entity != null) {
                        String code = entity.getSecucode();
                        code= StockUtils.getStockCode(code);
                        for (int j = 0; j < mStockList.size(); j++) {
                            ItemStock stock = mStockList.get(j);
                            if (TextUtils.equals(stock.getCode(), code)) {
                                stock.setState(entity.getState());
                                if(entity.getLastclose()!=0){
                                    stock.setIncrease(String.valueOf((double)(entity.getCurrent() - entity.getLastclose())
                                            / entity.getLastclose()));
                                }else{
                                    stock.setIncrease("0");
                                }

                                int exp = entity.getExp();

                                double newPriceV= UtilTools.getDecmValue(entity.getCurrent(),exp,stock.getCode());
                                double lastPriceV= UtilTools.getDecmValue(entity.getLastclose(),exp,stock.getCode());
                                stock.setValue(UtilTools.getDecmPrice(entity.getCurrent(),exp,stock.getCode()));
                                stock.setOld(UtilTools.getDecmPrice(entity.getLastclose(),exp,stock.getCode()));

                                float increaseP = (float) (newPriceV - lastPriceV);

                                String increasePrice = "";
                                if (increaseP > 0) {
                                    increasePrice = "+" + increaseP;
                                } else {
                                    increasePrice = "" + increaseP;
                                }
                                if(increaseP==0) {
                                    if (exp == 5 && (code.contains("SH9")||code.contains("SHHQ9"))) {
                                        increasePrice = "0.000";
                                    } else {
                                        increasePrice = "0.00";
                                    }
                                }
//                                increasePrice = UtilTools.getFormatNum(increasePrice + "", stock.getIsLongPrice()==0?2:3, true);
                                stock.setIncresePrice(increasePrice);
                                stock.setUrl(REQUESTURL + stock.getCode());
                                stock.setName(entity.getSecuname());
                                stock.setAmount("1");
                                int type= UtilTools.getStockType(entity.getType());
                                stock.setType(type);
                            }
                        }
                    }
                }
            }
        }
        return mStockList;
    }
}
