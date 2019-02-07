package com.ez08.compass.parser;

import com.ez08.compass.entity.PlateMarketEntity;
import com.ez08.support.net.EzMessage;

/**
 * Created by Administrator on 2016/8/15.
 */
public class PlateMarketParser {
    public PlateMarketEntity parse(EzMessage msg) {
        PlateMarketEntity entity = new PlateMarketEntity();
        String boardcode = msg.getKVData("boardcode").getStringWithDefault("");
        entity.setBoardcode(boardcode);
        String boardname = msg.getKVData("boardname").getStringWithDefault("");
        entity.setBoardname(boardname);
        int current = msg.getKVData("current").getInt32();
        entity.setCurrent(current);
        int lastclose = msg.getKVData("lastclose").getInt32();
        entity.setLastclose(lastclose);
        int hsrate = msg.getKVData("hsrate").getInt32();
        entity.setHsrate(hsrate);
        String firststockcode = msg.getKVData("firststockcode").getStringWithDefault("");
        entity.setFirststockcode(firststockcode);
        String firststockname = msg.getKVData("firststockname").getStringWithDefault("");
        entity.setFirststockname(firststockname);
        int firststockzf = msg.getKVData("firststockzf").getInt32();
        entity.setFirststockzf(firststockzf);
        int exp = msg.getKVData("exp").getInt32();
        entity.setExp(exp);
        return entity;
    }
}
