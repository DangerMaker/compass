package com.ez08.compass.parser;

import android.util.Log;

import com.ez08.compass.entity.StockDataEntity;
import com.ez08.compass.entity.StockDetailEntity;
import com.ez08.compass.tools.DDSID;
import com.ez08.compass.tools.MathUtils;
import com.ez08.compass.tools.StockUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * 加工原始数据用于显示
 */
public class IndicatorHelper {

    public StockDetailEntity entity;

    public IndicatorHelper(StockDetailEntity entity) {
        this.entity = entity;
    }

    //当前价格
    public String getCurrentPrice() {
        return MathUtils.getFormatPrice(entity.getCurrent(), entity.getDecm());
    }

    //当前波动值
    public String getCurrentWaveValue(){
        return MathUtils.getFormatPrice(entity.getCurrent() - entity.getLastclose(), entity.getDecm());
    }

    //当前波动率
    public String getCurrentWavePercent(){
        return MathUtils.formatNum((entity.getCurrent() - entity.getLastclose()) * 100f / entity.getLastclose(),4) + "%";
    }

    //开
    public String getOpenPrice() {
        return MathUtils.getFormatPrice(entity.getOpen(), entity.getDecm());
    }

    //收
    public String getLastClosePrice() {
        return MathUtils.getFormatPrice(entity.getLastclose(), entity.getDecm());
    }

    //高
    public String getHighPrice() {
        return MathUtils.getFormatPrice(entity.getHigh(), entity.getDecm());
    }

    //低
    public String getLowPrice() {
        return MathUtils.getFormatPrice(entity.getLow(), entity.getDecm());
    }

    //成交额
    public String getDealAmount() {
        return MathUtils.getFormatUnit(entity.getAmount()) + "元";
    }

    //成交量
    public String getDealVolume() {
        return MathUtils.getFormatUnit(entity.getVolume() / 100) + "手"; //股 -> 手
    }

    //股票名称
    public String getName() {
        return entity.getSecuname();
    }

    //股票代码
    public String getCode() {
        return StockUtils.cutStockCode(entity.getSecucode());
    }

    //市值
    public String getMarketValue() {
        return MathUtils.getFormatUnit(entity.getMarketvalue() / 100); //这个不知道为啥除100
    }

    //流动市值
    public String getTmarketvalue() {
        return MathUtils.getFormatUnit(entity.getTmarketvalue() / 100); //这个不知道为啥除100
    }

    //振幅
    public String getWave() {
        return MathUtils.formatNum(((entity.getHigh() - entity.getLow()) * 100f / entity.getLastclose()), 4) + "%";
    }

    //换手率
    public String getHandRate() {
        return MathUtils.formatNum(entity.getToratio() / 10000f, 4) + "%";
    }

    //市盈率
    public String getEarnings() {
        return MathUtils.formatNum(entity.getPeratio() / 10000f, 4);
    }

    //委买
    public String getIndexBuy() {
        return MathUtils.getFormatUnit(entity.getZbidvolume() / 100); //这个不知道为啥除100
    }

    //委卖
    public String getIndexSell() {
        return MathUtils.getFormatUnit(entity.getZaskvolume() / 100); //这个不知道为啥除100
    }

    //委比(指数)
    public String getIndexRate() {
        return MathUtils.getFormatUnit((entity.getZbidvolume() - entity.getZaskvolume()) * 100f / (entity.getZbidvolume() + entity.getZaskvolume())) + "%"; //这个不知道为啥除100
    }

    //内盘
    public String getInnerVolume() {
        return MathUtils.getFormatUnit(entity.getBuyvolume() / 100) + "手"; //股 -> 手
    }

    //外盘
    public String getOuterVolume() {
        return MathUtils.getFormatUnit((entity.getVolume() - entity.getBuyvolume()) / 100) + "手"; //股 -> 手
    }

    //委比（个股）
    public String getStockRate() {
        return MathUtils.getFormatUnit(((entity.getVolume() - entity.getBuyvolume()) - entity.getBuyvolume()) * 100f / entity.getVolume()) + "%"; //这个不知道为啥除100
    }

    //涨家数
    public String getUpCount() {
        return entity.getZup() + "";
    }

    //跌家数
    public String getDownCount() {
        return entity.getZdown() + "";
    }

    //平家数
    public String getEqualCount() {
        return entity.getZequal() + "";
    }

    public List<StockDataEntity> getData() {
        List<StockDataEntity> list = new ArrayList<>();
        list.add(new StockDataEntity("名称", getName()));
        list.add(new StockDataEntity("代码", getCode()));
        list.add(new StockDataEntity("成交量", getDealVolume()));
        list.add(new StockDataEntity("成交额", getDealAmount()));
        list.add(new StockDataEntity("振幅", getWave()));
        list.add(new StockDataEntity("当前价", getCurrentPrice()));
        list.add(new StockDataEntity("当前波动值", getCurrentWaveValue()));
        list.add(new StockDataEntity("当前波动率", getCurrentWavePercent()));
        list.add(new StockDataEntity("开盘价", getOpenPrice()));
        list.add(new StockDataEntity("收盘价", getLastClosePrice()));
        list.add(new StockDataEntity("最高价", getHighPrice()));
        list.add(new StockDataEntity("最低价", getLowPrice()));

        if (DDSID.isZ(entity.getSecucode())) { //指数 板块
            list.add(new StockDataEntity("委买", getIndexBuy()));
            list.add(new StockDataEntity("委卖", getIndexSell()));
            list.add(new StockDataEntity("委比", getIndexRate()));
            list.add(new StockDataEntity("涨家数", getUpCount()));
            list.add(new StockDataEntity("跌家数", getDownCount()));
            list.add(new StockDataEntity("平家数", getEqualCount()));
        } else {
            list.add(new StockDataEntity("内盘", getInnerVolume()));
            list.add(new StockDataEntity("外盘", getOuterVolume()));
            list.add(new StockDataEntity("委比", getStockRate()));
            list.add(new StockDataEntity("市值", getMarketValue()));
            list.add(new StockDataEntity("流动市值", getTmarketvalue()));
            list.add(new StockDataEntity("换手率", getHandRate()));
            list.add(new StockDataEntity("市盈率", getEarnings()));
        }

        return list;
    }


    public String toPrint() {
        String print = "\n" +
                "名称 ：" + getName() + "\n" +
                "代码 ：" + getCode() + "\n" +
                "内盘 ：" + getInnerVolume() + "\n" +
                "外盘 ：" + getOuterVolume() + "\n" +
                "成交额 ：" + getDealAmount() + "\n" +
                "成交量 ：" + getDealVolume() + "\n" +
                "市值 ：" + getMarketValue() + "\n" +
                "流动市值 ：" + getTmarketvalue() + "\n" +
                "振幅 ：" + getWave() + "\n" +
                "换手率 ：" + getHandRate() + "\n" +
                "市盈率 ：" + getEarnings() + "\n" +
                "委买 ：" + getIndexBuy() + "\n" +
                "委卖 ：" + getIndexSell() + "\n" +
                "委比（指数） ：" + getIndexRate() + "\n" +
                "委比（个股） ：" + getStockRate() + "\n" +
                "涨家数 ：" + getUpCount() + "\n" +
                "跌家数 ：" + getDownCount() + "\n" +
                "平家数 ：" + getEqualCount() + "\n" +
                "开盘价 ：" + getOpenPrice() + "\n" +
                "收盘价：" + getLastClosePrice() + "\n" +
                "最高价：" + getHighPrice() + "\n" +
                "最低价：" + getLowPrice() + "\n" +
                "当前价：" + getCurrentPrice();
        Log.e("IndicatorHelper", print);
        return print;
    }
}
