package com.ez08.compass.parser;

import com.ez08.compass.entity.StockDetailEntity;
import com.ez08.compass.tools.DDSID;
import com.ez08.support.net.EzMessage;

/**
 * Created by Administrator on 2017/1/9.
 */

public class StockDetailParser {

    public StockDetailEntity parse(String code, EzMessage message) {
        StockDetailEntity entity = new StockDetailEntity();
        if (message != null) {
            if (DDSID.isZ(code)) {
                EzMessage index = message.getKVData("indexl1").getMessage();
                if (index != null) {
                    EzMessage base = index.getKVData("base").getMessage();
                    if (base != null) {
                        int idstk = base.getKVData("idstk").getInt32();
                        int date = base.getKVData("date").getInt32();
                        int decm = base.getKVData("decm").getInt32();
                        int volexp = base.getKVData("volexp").getInt32();
                        int volmul = base.getKVData("volmul").getInt32();
                        long time = base.getKVData("time").getInt64();
                        entity.setIdstk(idstk);
                        entity.setDate(date);
                        entity.setDecm(decm);
                        entity.setVolexp(volexp);
                        entity.setVolmul(volmul);
                        entity.setTime(time);
                    }

                    //------
                    int open = index.getKVData("open").getInt32();
                    int high = index.getKVData("high").getInt32();
                    int low = index.getKVData("low").getInt32();
                    int current = index.getKVData("current").getInt32();
                    long volume = index.getKVData("volume").getInt64();
                    long amount = index.getKVData("amount").getInt64();
                    long zbidvolume = index.getKVData("zbidvolume").getInt64();
                    long zaskvolume = index.getKVData("zaskvolume").getInt64();
                    int zup = index.getKVData("zup").getInt32();
                    int zdown = index.getKVData("zdown").getInt32();
                    int zequal = index.getKVData("zequal").getInt32();
                    int lastclose = index.getKVData("lastclose").getInt32();
                    String secuname = index.getKVData("secuname").getStringWithDefault("");
                    String secucode = index.getKVData("secucode").getStringWithDefault("");
                    int exp = index.getKVData("exp").getInt32();

                    entity.setOpen(open);
                    entity.setHigh(high);
                    entity.setLow(low);
                    entity.setCurrent(current);
                    entity.setVolume(volume);
                    entity.setAmount(amount);
                    entity.setZbidvolume(zbidvolume);
                    entity.setZaskvolume(zaskvolume);
                    entity.setZup(zup);
                    entity.setZdown(zdown);
                    entity.setZequal(zequal);
                    entity.setLastclose(lastclose);
                    entity.setSecuname(secuname);
                    entity.setSecucode(secucode);
                    entity.setExp(exp);

                }
            } else {
                EzMessage index = message.getKVData("l1").getMessage();
                if (index != null) {
                    EzMessage base = index.getKVData("base").getMessage();
                    if (base != null) {
                        int idstk = base.getKVData("idstk").getInt32();
                        int date = base.getKVData("date").getInt32();
                        int decm = base.getKVData("decm").getInt32();
                        int volexp = base.getKVData("volexp").getInt32();
                        int volmul = base.getKVData("volmul").getInt32();
                        long time = base.getKVData("time").getInt64();
                        entity.setIdstk(idstk);
                        entity.setDate(date);
                        entity.setDecm(decm);
                        entity.setVolexp(volexp);
                        entity.setVolmul(volmul);
                        entity.setTime(time);
                    }

                    //------
                    int open = index.getKVData("open").getInt32();
                    int high = index.getKVData("high").getInt32();
                    int low = index.getKVData("low").getInt32();
                    int current = index.getKVData("current").getInt32();
                    long volume = index.getKVData("volume").getInt64();
                    long amount = index.getKVData("amount").getInt64();
                    int lastclose = index.getKVData("lastclose").getInt32();
                    String secuname = index.getKVData("secuname").getStringWithDefault("");
                    String secucode = index.getKVData("secucode").getStringWithDefault("");
                    long buyvolume = index.getKVData("buyvolume").getInt64();
                    int[] b5prices = index.getKVData("b5prices").getInt32s();
                    long[] b5volumes = index.getKVData("b5volumes").getInt64s();
                    int[] s5prices = index.getKVData("s5prices").getInt32s();
                    long[] s5volumes = index.getKVData("s5volumes").getInt64s();
                    int state = index.getKVData("state").getInt32();
                    long marketvalue = index.getKVData("marketvalue").getInt64();
                    long tmarketvalue = index.getKVData("tmarketvalue").getInt64();
                    int peratio = index.getKVData("peratio").getInt32();
                    int toratio = index.getKVData("toratio").getInt32();

                    entity.setOpen(open);
                    entity.setHigh(high);
                    entity.setLow(low);
                    entity.setCurrent(current);
                    entity.setVolume(volume);
                    entity.setAmount(amount);
                    entity.setLastclose(lastclose);
                    entity.setSecuname(secuname);
                    entity.setSecucode(secucode);
                    entity.setBuyvolume(buyvolume);
                    entity.setB5prices(b5prices);
                    entity.setB5volumes(b5volumes);
                    entity.setS5prices(s5prices);
                    entity.setS5volumes(s5volumes);
                    entity.setLastclose(lastclose);
                    entity.setSecuname(secuname);
                    entity.setSecucode(secucode);
                    entity.setState(state);
                    entity.setMarketvalue(marketvalue);
                    entity.setTmarketvalue(tmarketvalue);
                    entity.setPeratio(peratio);
                    entity.setToratio(toratio);

                }

            }
        }
        return entity;
    }
}
