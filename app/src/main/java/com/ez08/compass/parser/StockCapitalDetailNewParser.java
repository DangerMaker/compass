package com.ez08.compass.parser;

import com.ez08.compass.ui.stocks.PieChartEntity;
import com.ez08.support.net.EzMessage;

/**
 * Created by Administrator on 2017/1/11.
 */

public class StockCapitalDetailNewParser {
    public PieChartEntity parse(EzMessage msge){
        PieChartEntity pieChartEntity=new PieChartEntity();
        double lamount1=0,lamount2=0,lamount3=0,lamount4=0,lamount5=0,lamount6=0,lamount7=0,lamount8=0,lamount9=0,lamount10=0,lamount11=0,lamount12=0;
        if(msge!=null){
            EzMessage zhu_z=msge.getKVData("zhu_z").getMessage();
            if(zhu_z!=null){
                lamount1=zhu_z.getKVData("bamount").getInt64();
                lamount3=zhu_z.getKVData("samount").getInt64();
            }
            EzMessage zhu_b=msge.getKVData("zhu_b").getMessage();
            if(zhu_b!=null){
                lamount2=zhu_b.getKVData("bamount").getInt64();
                lamount4=zhu_b.getKVData("samount").getInt64();
            }
            EzMessage san_b=msge.getKVData("san_b").getMessage();
            if(san_b!=null){
                lamount5=san_b.getKVData("bamount").getInt64();
                lamount6=san_b.getKVData("samount").getInt64();
            }
            EzMessage san_z=msge.getKVData("san_z").getMessage();
            if(san_z!=null){
                lamount7=san_z.getKVData("bamount").getInt64();
                lamount8=san_z.getKVData("samount").getInt64();
            }
            EzMessage sg_z=msge.getKVData("sg_z").getMessage();
            if(sg_z!=null){
                lamount9=sg_z.getKVData("bamount").getInt64();
                lamount10=sg_z.getKVData("samount").getInt64();
            }
            pieChartEntity.setMainBuyAmount(lamount1+lamount2);
            pieChartEntity.setMainSellAmount(lamount3+lamount4);
            pieChartEntity.setRetailBuyAmount(lamount5+lamount7);
            pieChartEntity.setRetailSellAmount(lamount6+lamount8);
            pieChartEntity.setSgBuyAmount(lamount9);
            pieChartEntity.setSgSellAmount(lamount10);
            pieChartEntity.setDkBuyMoney(lamount1+lamount2+lamount9);
            pieChartEntity.setDkSellMoney(lamount3+lamount4+lamount10);
        }
        return pieChartEntity;
    }

}
