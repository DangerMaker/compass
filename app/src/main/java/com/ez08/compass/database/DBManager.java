package com.ez08.compass.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.ez08.compass.CompassApp;
import com.ez08.compass.entity.StatisEntity;
import com.ez08.compass.userauth.AuthUserInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/12/13.
 */

public class DBManager implements NetStatisInterFace.StatisticsFace {

    public final String START_APP = "startapp";
    public final String ADVERT_START = "advert.start";
    public final String ADVERT_INFO = "advert.info";
    public final String ADVERT_STOCK = "stock.advert";
    public final String ADVERT_LIVE = "live.advert";
    public final String PAYMENT = "payment";
    public final String REGIST_IDENTIFY = "regist.identify";
    public final String REGIST_SETPASS = "regist.setpass";
    public final String LOGIN = "login";
    public final String FORGETPASS = "forgetpass";
    public final String INFO_LIST = "info.list";
    public final String INFO_SELECT = "info.select";
    public final String INFO_DETAIL = "info.detail";
    //    public String PUSH_HOME="push.home";
//    public String PUSH_BAR="push.bar";
    public final String TRADE_LIST = "trade.list";
    public final String TRADE_TRADERS = "trade.traders";
    public final String PERSONAL_MAIN = "personal.main";
    public final String PERSONAL_COMMON = "personal.common";
    public final String PERSONAL_RESETPASS = "personal.resetpass";
    public final String KEFU_MAIN = "kefu.main";
    public final String KEFU_MESSAGE = "kefu.message";
    public final String KEFU_COLLECT = "kefu.collect";
    public final String LIVE_ROOMLIST = "live.roomlist";
    public final String LIVE_VIDEOLIST = "live.videolist";
    public final String LIVE_ROOMDETAIL = "live.roomdetail";
    public final String LIVE_ADVANCE = "live.advance";
    public final String STOCK_MYSTOCK = "stock.mystock";
    public final String STOCK_IMPORT = "stock.important";
    public final String STOCK_FEATURE = "stock.stare";
    public final String STOCK_MYSTOCK_EDIT = "stock.ordinaryEdit";
    public final String STOCK_IMPORT_EDIT = "stock.ImportantEdit";
    public final String STOCK_MARKET = "stock.market";
    public final String STOCK_HOTWORDS = "stock.hotwords";
    public final String STOCK_WATCH = "stock.watch";
    public final String STOCK_STARE = "stock.stare";
    public final String STOCK_SEARCH = "stock.search";
    public final String DETAIL_MAIN = "detail.main";
    public final String DETAIL_BUYSELL = "detail.buysell";
    public final String DETAIL_CHART = "detail.chart";
    public final String DETAIL_HOZIROTAL_CHART = "detail.CrossScreenChart";
    public final String DETAIL_HORIZOTAL_INDEX = "detail.index";
    public final String DETAIL_INDEX = "detail.index";
    public final String DETAIL_VERTICAL_INDEX = "detail.CrossScreenChartIndex";
    public final String DETAIL_CAPITALALL = "detail.capitalall";
    public final String DETAIL_VERTICALCAPITALALL = "detail.verticalcapitalall";
    public final String DETAIL_BASIC = "detail.basic";
    public final String DETAIL_WEB = "detail.web";
    public final String DETAIL_COMPANY = "detail.company";
    public final String DETAIL_HOLDER = "detail.holder";
    public final String DETAIL_INDICATOR = "detail.indicator";
    public final String APP_STATUS = "appstatus";
    public final String NIGHT_MODE = "nightmode";
    public final String DETAIL_CHIP = "detail.chip";
    public final String DETAIL_CHIP_VERTICAL = "detail.chipvertical";
    public final String DETAIL_SHARE = "detail.share";
    public final String DETAIL_FORECAST = "detail.forecast";
    public final String SHARE_PLAN = "plan.share";
    public final String SHARE_EXHIBITION = "plan.exhibition";
    public final String DETAIL_GRAPHIC = "detail.graphic ";

    private DBHelper helper;
    private SQLiteDatabase db;
    private NetStatisInterFace mNetStatisInterFace;
    private Context mContext;

    public DBManager(Context context) {
        mContext = context;
        helper = new DBHelper(context);
        //因为getWritableDatabase内部调用了mContext.openOrCreateDatabase(mName, 0, mFactory);
        //所以要确保context已初始化,我们可以把实例化DBManager的步骤放在Activity的onCreate里
        db = helper.getWritableDatabase();
        mNetStatisInterFace = new NetStatisInterFace();
        mNetStatisInterFace.setStatis(this);
    }

    public void addData(String action, String type, String params, long times) {
        db.beginTransaction();  //开始事务
        params=getFullParams(action,type,params);
        Log.e("appLog","action = " + action + " ,type = " + type + " ,params = " + params );
        try {
            db.execSQL("INSERT INTO statis VALUES(null, ?, ?, ?,?,?)", new Object[]{action, type, params, times, AuthUserInfo.getMyCid()});
            db.setTransactionSuccessful();  //设置事务成功完成
        } finally {
            db.endTransaction();    //结束事务
        }
        if (CompassApp.GLOBAL.mFirstInstall) {
            List<StatisEntity> statisList = CompassApp.GLOBAL.mgr.query(times);
            mNetStatisInterFace.requestStatis(mContext, statisList);
        }else {
            List<StatisEntity> statisList=CompassApp.GLOBAL.mgr.query(System.currentTimeMillis());
            if(statisList!=null&&statisList.size()!=0){
                long startTime=statisList.get(0).getTimes();
                long mLastStatisTime=System.currentTimeMillis();
                if(mLastStatisTime-startTime>60*60*1000||statisList.size()>100){    //距上次统计超过一小时或超过100条
                    mNetStatisInterFace.requestStatis(mContext,statisList);
                }
            }
        }
    }

    @Override
    public void StatisticsCall(boolean success, long time) {
        if (success) {
            CompassApp.GLOBAL.mUpLoadSwitch=true;
            CompassApp.GLOBAL.mgr.deleteOldStatis(time);
        }
    }

    /**
     * delete old statis
     *
     * @param time
     */
    public void deleteOldStatis(long time) {
        db.delete("statis", "times <= ?", new String[]{String.valueOf(time)});
    }

    /**
     * query all persons, return list
     *
     * @return List<StatisEntity>
     */
    public List<StatisEntity> query(long times) {
        ArrayList<StatisEntity> list = new ArrayList<StatisEntity>();
        Cursor c = queryCursorByTime(times);
        while (c.moveToNext()) {
            StatisEntity statis = new StatisEntity();
            statis.setAction(c.getString(c.getColumnIndex("action")));
            statis.setType(c.getString(c.getColumnIndex("type")));
            statis.setParams(c.getString(c.getColumnIndex("params")));
            statis.setTimes(c.getLong(c.getColumnIndex("times")));
            statis.setCid(c.getString(c.getColumnIndex("cid")));
            list.add(statis);
        }
        c.close();
        return list;
    }

    public Cursor queryCursorByTime(long times) {
        String sql = "SELECT * FROM statis where times<=?";
        String[] selectionArgs = new String[]{times + ""};
        Cursor cursor = db.rawQuery(sql, selectionArgs);
        return cursor;
    }
    /**
     * close database
     */
    public void closeDB() {
        db.close();
    }

    public String getFullParams(String action, String type, String params) {
        String full = "";
        switch (action) {
            case START_APP:
                full = "install=" + params;
                break;
            case LOGIN:
//                full = "cid=" + params;
                break;
            case TRADE_LIST:
                if (type.equals("2") || type.equals("3") || type.equals("4")) {
                    full = "trader=" + params;
                }
                break;
            case TRADE_TRADERS:
                if (type.equals("1") || type.equals("2")) {
                    full = "trader=" + params;
                }
                break;
            case KEFU_MAIN:
                if (type.equals("6")) {
                    full = "level=" + params;
                }
                break;
            case KEFU_MESSAGE:
                if (type.equals("1")) {
                    full = "msgid=" + params;
                }
                break;
            case KEFU_COLLECT:
                if (type.equals("1")) {
                    full = "collectid=" + params;
                }
                break;
            case LIVE_ROOMLIST:
                if (type.equals("1")) {
                    full = "roomid=" + params;
                }
                break;
            case LIVE_ROOMDETAIL:
                if (type.equals("0")||type.equals("1") || type.equals("2")) {
                    String paramses[] = params.split("&");
                    if (paramses.length == 2) {
                        full = "roomid=" + paramses[0]+ "&" + "classid=" + paramses[1];
                    }
                }
                break;
            case LIVE_ADVANCE:
                if (type.equals("0")) {
                    full = "roomid=" + params;
                }
                if (type.equals("1") || type.equals("2")) {
                    String paramses[] = params.split("&");
                    if (paramses.length == 2) {
                        full = "roomid=" + paramses[0]+ "&" + "classid=" + paramses[1];
                    }
                }
                break;
            case STOCK_HOTWORDS:
                if (type.equals("1") || type.equals("2") || type.equals("3")) {
                    full = "hotword=" + params;
                }else{

                }
                break;
            case STOCK_SEARCH:
                if (type.equals("1") || type.equals("2")) {
                    full = "code=" + params;
                }
                break;
            case DETAIL_MAIN:
                full = "code=" + params;
                break;
            case DETAIL_BUYSELL:
                if (type.equals("2") || type.equals("3")) {
                    full = "trader=" + params;
                }
                break;
            case DETAIL_CHART:
                full = "code=" + params;
                break;
            case DETAIL_HORIZOTAL_INDEX:
            case DETAIL_VERTICAL_INDEX:
                full = "code=" + params;
                break;
            case DETAIL_CAPITALALL:
            case DETAIL_VERTICALCAPITALALL:
                full = "code=" + params;
                break;
            case DETAIL_BASIC:
                full = "code=" + params;
                break;
            case DETAIL_WEB:
                String paramses[] = params.split("&");
                if (paramses.length == 2) {
                    full = "code=" + paramses[0] + "&" + "type=" + paramses[1];
                }
                break;
            case DETAIL_COMPANY:
                full = "code=" + params;
                break;
            case DETAIL_HOLDER:
                full = "code=" + params;
                break;
            case DETAIL_INDICATOR:
                String paramsez[] = params.split("&");
                if (paramsez.length == 2) {
                    full = "code=" + paramsez[0] + "&" + "style=" + paramsez[1];
                }
                break;
            case SHARE_PLAN:
            case SHARE_EXHIBITION:
               full = params;
                break;
            case INFO_DETAIL:
                if (type.equals("0")) {
                    full = "source=" + params;
                }
                break;
        }
        return full;
    }

}
