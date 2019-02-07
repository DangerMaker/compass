package com.ez08.compass.parser;

import com.ez08.compass.entity.StockMarketEntity;
import com.ez08.compass.tools.StockUtils;
import com.ez08.support.net.EzMessage;

/**
 * Created by Administrator on 2016/8/15.
 */
public class StockMarketParser {
    public StockMarketEntity parse(EzMessage msg) {
        if(msg==null){
            return null;
        }
        StockMarketEntity entity = new StockMarketEntity();
        if(msg.getKVData("secuname")!=null) {
            String name = msg.getKVData("secuname").getStringWithDefault("");
            entity.setSecuname(name);
        }else{
            entity.setSecuname("--");
        }
        if(msg.getKVData("secucode")!=null) {
            String code = msg.getKVData("secucode").getStringWithDefault("");
            code= StockUtils.getStockCode(code);
            entity.setSecucode(code);
        }else{
            entity.setSecucode("--");
        }

        if(msg.getKVData("state")!=null) {
            int state = msg.getKVData("state").getInt32();
            entity.setState(state);
        }else{
            entity.setState(0);
        }

        if(msg.getKVData("type") != null) {
            int type = msg.getKVData("type").getInt32();
            entity.setType(type);
        }else{
            entity.setType(0);
        }

        if(msg.getKVData("current") != null) {
            int current = msg.getKVData("current").getInt32();
            entity.setCurrent(current);
        }else{
            entity.setCurrent(0);
        }

        if(msg.getKVData("lastclose") != null) {
            int lastclose = msg.getKVData("lastclose").getInt32();
            entity.setLastclose(lastclose);
        }else{
            entity.setLastclose(0);
        }

        if(msg.getKVData("exp") != null) {
            int exp = msg.getKVData("exp").getInt32();
            entity.setExp(exp);
        }else{
            entity.setExp(0);
        }
        return entity;
    }
}
