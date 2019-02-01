package com.ez08.compass.database;

import android.content.Context;
import android.content.Intent;

import com.ez08.compass.CompassApp;
import com.ez08.compass.entity.StatisEntity;
import com.ez08.compass.net.NetInterface;
import com.ez08.support.net.NetResponseHandler2;

import java.util.List;

/**
 * Created by Administrator on 2017/12/18.
 */

public class NetStatisInterFace {

    private Context mContext;
    private StatisticsFace callBack;
    private final int WHAT_REQUEST_CODE=1001;
    private long mCurrentTime;

    public void requestStatis(Context context, List<StatisEntity> list){
        long sendTime=System.currentTimeMillis();
        //上一次数据正在上传没有返回，且两次上传间隔小于30秒，则不上传
        if(!CompassApp.GLOBAL.mUpLoadSwitch&&(sendTime-CompassApp.GLOBAL.mStatisTimes<30*1000)){
            return;
        }
        CompassApp.GLOBAL.mUpLoadSwitch=false;
        CompassApp.GLOBAL.mStatisTimes=System.currentTimeMillis();
        mContext=context;
        if(list==null||list.size()==0){
            return;
        }
        String results[]=new String[list.size()];
        for(int i=0;i<list.size();i++){
            StatisEntity entity=list.get(i);
            String result=entity.getAction()+","+entity.getType()+","+entity.getParams()+","+entity.getTimes()+","+entity.getCid();
            results[i]=result;
        }
        NetInterface.setControlStatis(mHandler,WHAT_REQUEST_CODE,results);
        mCurrentTime=list.get(list.size()-1).getTimes();
    }

    private NetResponseHandler2 mHandler = new NetResponseHandler2() {

        @Override
        public void receive(int i, boolean b, Intent intent) {
            if(b){
                callBack.StatisticsCall(true,mCurrentTime);
            }
        }
    };

    public void setStatis(StatisticsFace statis){
        callBack=statis;
    }

    public interface StatisticsFace{
        public void StatisticsCall(boolean success, long time);
    }
}
