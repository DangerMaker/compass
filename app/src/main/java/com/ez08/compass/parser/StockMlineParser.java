package com.ez08.compass.parser;

import android.content.Intent;

import com.ez08.compass.entity.FenShiHistoryEntity;
import com.ez08.compass.entity.StockDetailEntity;
import com.ez08.compass.entity.FenShiAllEntity;
import com.ez08.compass.tools.MathUtils;
import com.ez08.support.net.EzMessage;
import com.ez08.support.util.EzValue;
import com.ez08.tools.IntentTools;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/1/11.
 */

public class StockMlineParser {

    private FenShiHistoryEntity mLastEntity;


    public FenShiAllEntity parse(Intent intent, StockDetailEntity entity, boolean addData) {

        if (entity == null) {
            return null;
        }

        mLastEntity = new FenShiHistoryEntity();
        FenShiAllEntity fenshi = new FenShiAllEntity();
        List<FenShiHistoryEntity> history = new ArrayList<FenShiHistoryEntity>();
        mLastEntity.setDate(93100);
        int openPrice;
        if (entity.getOpen() != 0) {
            openPrice = entity.getOpen();
        } else {
            openPrice = entity.getLastclose();
        }

        mLastEntity.setValue(MathUtils.getPriceInt2Float(openPrice, entity.getDecm()));
        mLastEntity.setTurn(entity.getAmount());
        mLastEntity.setColumn(entity.getVolume());

        if (intent != null) {
            EzValue mline = IntentTools.safeGetEzValueFromIntent(
                    intent, "list");
            if (mline != null) {
                EzMessage[] msgs = mline.getMessages();
                int lBeforeDate = 93100;
                for (int i = 0; i < msgs.length; i++) {
                    EzMessage msg = msgs[i];
                    if (msg != null) {
                        int date = msg.getKVData("date").getInt32();
                        int current = msg.getKVData("current").getInt32();
                        long volume = msg.getKVData("volume").getInt64();
                        long amount = msg.getKVData("amount").getInt64();
                        if (current == 0 && entity.getState() == 0) {
                            //现价为0，是个股，没有停牌的股票，用昨收的价格
                            if (entity.getOpen() != 0) {
                                current = entity.getOpen();
                            } else {
                                current = entity.getLastclose();
                            }
                        }

                        if (addData) {
                            String str = "20170227" + (date < 100000 ? "0" + date : date);
                            String str1 = "20170227" + (lBeforeDate < 100000 ? "0" + lBeforeDate : lBeforeDate);
                            if (i == msgs.length - 1 && Long.parseLong(str) <= Long.parseLong("20170227" + getCurrentDate())) {   //最后一条数据不是最后时间的数据，将这条数据的时间换成最后时间
                                setListHistory(str, str1, date, lBeforeDate, history);
                                //---------------
                                String strMid = "20170227" + getCurrentDate();
                                if (Long.parseLong(strMid) > Long.parseLong("20170227150000")) {    //已经收盘了，但最后一条不是收盘时间
                                    strMid = "20170227150000";
                                    FenShiHistoryEntity his = new FenShiHistoryEntity();
                                    his.setDate(date);
                                    his.setValue(MathUtils.getPriceInt2Float(current, entity.getDecm()));
                                    his.setTurn(amount);
                                    his.setColumn(volume);
                                    history.add(his);
                                    setListHistory(strMid, str, 150000, date, history);
                                }
                            } else {
                                setListHistory(str, str1, date, lBeforeDate, history);
                            }
                        }

                        FenShiHistoryEntity his = new FenShiHistoryEntity();
                        his.setDate(date);
                        his.setValue(MathUtils.getPriceInt2Float(current, entity.getDecm()));
                        his.setTurn(amount);
                        his.setColumn(volume);
                        history.add(his);
                        lBeforeDate = date;
                    }
                }
            }
        }
        fenshi.setHistory(history);
        if (history.size() > 0) {
            int time = history.get(history.size() - 1).getDate();
            int dateCurrent = 0;
            double currentValue = 0;

            dateCurrent = (int) entity.getTime();
            currentValue = entity.getCurrent();
            if (Math.abs(dateCurrent - time) < 100) {
                history.get(history.size() - 1).setValue(MathUtils.getPriceInt2Float((int)currentValue, entity.getDecm()));
            }
        }
        return fenshi;
    }

    private void setListHistory(String str, String str1, int date, int lBeforeDate, List<FenShiHistoryEntity> history) {
        long dValue = 0;
        String beforeMiddle = "20170227113000";
        String afterMiddle = "20170227130000";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        try {
            long lSeconds = sdf.parse(str).getTime();//毫秒
            long mSeconds = sdf.parse(str1).getTime();//毫秒
            dValue = (lSeconds - mSeconds) / (1000 * 60);
            if (Long.parseLong(str1) <= Long.parseLong(beforeMiddle) && Long.parseLong(str) >= Long.parseLong(afterMiddle)) {
                dValue = dValue - 90;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //------------------
        if (dValue > 1) {
            dValue--;
            if (date <= 113000) {
                for (int j = 0; j < dValue; j++) {
                    addSameDate(history);
                }
            } else if (date > 130000 && lBeforeDate > 130000) {
                for (int j = 0; j < dValue; j++) {
                    addSameDate(history);
                }
            } else if (date > 130000 && lBeforeDate < 130000) {
                for (int j = 0; j < dValue; j++) {
                    addSameDate(history);
                }
            }
        }
    }

    private String getCurrentDate() {
        long l = System.currentTimeMillis();
        //new日期对象
        Date dateCurrent = new Date(l);
        //转换提日期输出格式
        SimpleDateFormat dateFormat = new SimpleDateFormat("HHmmss");
        System.out.println(dateFormat.format(dateCurrent));
        String sDate = dateFormat.format(dateCurrent);
        if (Long.parseLong(sDate) > Long.parseLong("112900") && Long.parseLong(sDate) < Long.parseLong("130000")) {
            sDate = "112900";
        }
        return sDate;
    }

    /*
    添加相同的数据
     */
    private void addSameDate(List<FenShiHistoryEntity> history) {
        FenShiHistoryEntity lBeforeEntity;
        if (history.size() > 0) {
            lBeforeEntity = history.get(history.size() - 1);
        } else {
            lBeforeEntity = mLastEntity;
        }
        FenShiHistoryEntity his = new FenShiHistoryEntity();
        int lBeforeDate = lBeforeEntity.getDate();
        int date = 0;
        if (lBeforeDate <= 95900) {
            date = lBeforeDate + 100;
            if (date == 96000) {
                date = 100000;
            }
        } else if (lBeforeDate <= 105900) {
            date = lBeforeDate + 100;
            if (date == 106000) {
                date = 110000;
            }
        } else if (lBeforeDate <= 112900) {
            date = lBeforeDate + 100;
            if (date == 113000) {
                date = 130000;
            }
        } else if (lBeforeDate <= 135900) {
            date = lBeforeDate + 100;
            if (date == 136000) {
                date = 140000;
            }
        } else if (lBeforeDate <= 145900) {
            date = lBeforeDate + 100;
            if (date == 145900) {
                date = 150000;
            }
        } else {
            date = lBeforeDate + 100;
        }

        his.setDate(date);
        his.setValue(lBeforeEntity.getValue());
        his.setTurn(lBeforeEntity.getTurn());
        his.setColumn(lBeforeEntity.getColumn());
        history.add(his);
    }
}
