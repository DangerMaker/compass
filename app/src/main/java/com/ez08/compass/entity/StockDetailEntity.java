package com.ez08.compass.entity;

public class StockDetailEntity {
//    private int type;  //是否是板块
    private int idstk;  //品种ID
    private int date;   //数据日期
    private int decm; //价格转换成整数时需要乘的幂数(0～7)，实际乘数应该是 10 exp (wDecm-2)。0:乘0.01  1:乘0.1  2:乘1 3:乘10 ……
                        //我认为就是价格 乘以 10的（2 - decm）次幂
    private int volexp; //计算均价时，成交量需要乘的幂数(0～3)，实际乘数应该是 (10 exp yVolExp)，总的来说成交量的乘数应该是 vol * (10 exp yVolExp) * (yVolMul+1)
    private int volmul; //计算均价时，成交量需要乘的数字(0～127)，实际乘数应该是 (yVolMul+1)，总的来说成交量的乘数应该是 vol * (10 exp yVolExp) * (yVolMul+1)
    private long time;

    private int open;  //开
    private int high;   //高
    private int low;    //低
    private int current;    //当前
    private long volume;     //成交量
    private long amount;     //成交额

    //个股
    private long buyvolume; //买量（内盘，委托以买方价格成交）
    private int[] b5prices;   //5档买价
    private long[] b5volumes;  //5档买量
    private int[] s5prices;   //5档卖价
    private long[] s5volumes;  //5档卖量
    private int lastclose;  //收
    private String secuname;   //股票名称
    private String secucode;   //股票代码
    private int state;  //0 正常 1停牌 3退市
    private long marketvalue;    //  市值
    private long tmarketvalue;   //  流动市值
    private int peratio;    //  市盈率
    private int toratio;    //  换手率

    //指数
    private long zbidvolume;    //委买总量
    private long zaskvolume;    //委卖总量
    private int zup;    //  涨家数
    private int zdown;    //  跌家数
    private int zequal;    //  平家数
    private int exp;    //

//    public int getType() {
//        return type;
//    }
//
//    public void setType(int type) {
//        this.type = type;
//    }

    public int getIdstk() {
        return idstk;
    }

    public void setIdstk(int idstk) {
        this.idstk = idstk;
    }

    public int getDate() {
        return date;
    }

    public void setDate(int date) {
        this.date = date;
    }

    public int getDecm() {
        return decm;
    }

    public void setDecm(int decm) {
        this.decm = decm;
    }

    public int getVolexp() {
        return volexp;
    }

    public void setVolexp(int volexp) {
        this.volexp = volexp;
    }

    public int getVolmul() {
        return volmul;
    }

    public void setVolmul(int volmul) {
        this.volmul = volmul;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getOpen() {
        return open;
    }

    public void setOpen(int open) {
        this.open = open;
    }

    public int getHigh() {
        return high;
    }

    public void setHigh(int high) {
        this.high = high;
    }

    public int getLow() {
        return low;
    }

    public void setLow(int low) {
        this.low = low;
    }

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        this.current = current;
    }

    public long getVolume() {
        return volume;
    }

    public void setVolume(long volume) {
        this.volume = volume;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public long getBuyvolume() {
        return buyvolume;
    }

    public void setBuyvolume(long buyvolume) {
        this.buyvolume = buyvolume;
    }

    public int[] getB5prices() {
        return b5prices;
    }

    public void setB5prices(int[] b5prices) {
        this.b5prices = b5prices;
    }

    public long[] getB5volumes() {
        return b5volumes;
    }

    public void setB5volumes(long[] b5volumes) {
        this.b5volumes = b5volumes;
    }

    public int[] getS5prices() {
        return s5prices;
    }

    public void setS5prices(int[] s5prices) {
        this.s5prices = s5prices;
    }

    public long[] getS5volumes() {
        return s5volumes;
    }

    public void setS5volumes(long[] s5volumes) {
        this.s5volumes = s5volumes;
    }

    public int getLastclose() {
        return lastclose;
    }

    public void setLastclose(int lastclose) {
        this.lastclose = lastclose;
    }

    public String getSecuname() {
        return secuname;
    }

    public void setSecuname(String secuname) {
        this.secuname = secuname;
    }

    public String getSecucode() {
        return secucode;
    }

    public void setSecucode(String secucode) {
        this.secucode = secucode;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public long getMarketvalue() {
        return marketvalue;
    }

    public void setMarketvalue(long marketvalue) {
        this.marketvalue = marketvalue;
    }

    public long getTmarketvalue() {
        return tmarketvalue;
    }

    public void setTmarketvalue(long tmarketvalue) {
        this.tmarketvalue = tmarketvalue;
    }

    public int getPeratio() {
        return peratio;
    }

    public void setPeratio(int peratio) {
        this.peratio = peratio;
    }

    public int getToratio() {
        return toratio;
    }

    public void setToratio(int toratio) {
        this.toratio = toratio;
    }

    public long getZbidvolume() {
        return zbidvolume;
    }

    public void setZbidvolume(long zbidvolume) {
        this.zbidvolume = zbidvolume;
    }

    public long getZaskvolume() {
        return zaskvolume;
    }

    public void setZaskvolume(long zaskvolume) {
        this.zaskvolume = zaskvolume;
    }

    public int getZup() {
        return zup;
    }

    public void setZup(int zup) {
        this.zup = zup;
    }

    public int getZdown() {
        return zdown;
    }

    public void setZdown(int zdown) {
        this.zdown = zdown;
    }

    public int getZequal() {
        return zequal;
    }

    public void setZequal(int zequal) {
        this.zequal = zequal;
    }

    public int getExp() {
        return exp;
    }

    public void setExp(int exp) {
        this.exp = exp;
    }
}
