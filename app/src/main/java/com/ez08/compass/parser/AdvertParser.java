package com.ez08.compass.parser;

import com.ez08.compass.entity.AdvertEntity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/3/27.
 */

public class AdvertParser {

    public ArrayList<AdvertEntity> parse(String result){
        ArrayList<AdvertEntity> list=new ArrayList<>();
        try {
            JSONObject obj=new JSONObject(result);
            JSONArray array=obj.getJSONArray("data");
            if(array!=null){
                for(int i=0;i<array.length();i++){
                    String starttime=array.getJSONObject(i).getString("starttime");
                    String endtime=array.getJSONObject(i).getString("endtime");
                    String level=array.getJSONObject(i).getString("limitlevel");
                    String imageurl=array.getJSONObject(i).getString("imageurl");
                    String infourl=array.getJSONObject(i).getString("infourl");
                    String showplace=array.getJSONObject(i).getString("showplace");
                    String authsid=array.getJSONObject(i).getString("authid");
                    int mt=array.getJSONObject(i).getInt("mt");
                    AdvertEntity entity=new AdvertEntity();
                    entity.setStarttime(starttime);
                    entity.setEndtime(endtime);
                    entity.setLevel(level);
                    entity.setImageurl(imageurl);
                    entity.setInfourl(infourl);
                    entity.setShowplace(showplace);
                    entity.setAhtusId(authsid);
                    entity.setMt(mt);
                    list.add(entity);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }
}
