package com.ez08.compass.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/8/30.
 */

public class CxjcPointEntity {
    public int date;
    public int state; //0 - 1 +

    public static List<CxjcPointEntity> findPoint(List<CxjcEntity> data) {
        if(data == null){
            return null;
        }

        int oldState = 0;
        List<CxjcPointEntity> list = new ArrayList<>();
        for (int index = 0; index < data.size(); index++) {
            // 新旧数据针对仓位标志进行比较判断
            if (index > 0) {

                // 确定当前数据仓位的标志位
                int newState = 0;
                if (data.get(index).cw <= 100 && data.get(index).cw >= 1) {
                    newState = 1;
                } else {
                    newState = 0;
                }

                // 开始比较
                if (newState < oldState) {//减号
                    CxjcPointEntity pointEntity = new CxjcPointEntity();
                    pointEntity.date = data.get(index).date;
                    pointEntity.state = 0;
                    list.add(pointEntity);
                } else if (newState > oldState) {//加号
                    CxjcPointEntity pointEntity = new CxjcPointEntity();
                    pointEntity.date = data.get(index).date;
                    pointEntity.state = 1;
                    list.add(pointEntity);
                }
            }

            // 确定上一个旧数据仓位标志
            if (data.get(index).cw <= 100 && data.get(index).cw >= 1) {
                oldState = 1;
            } else {
                oldState = 0;
            }

        }
        return list;
    }

}
