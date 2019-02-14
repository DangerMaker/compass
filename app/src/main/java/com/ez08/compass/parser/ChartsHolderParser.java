package com.ez08.compass.parser;

import android.text.TextUtils;

import com.ez08.compass.entity.ChartsHolderEntity;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/9/19.
 */
public class ChartsHolderParser {
    private List<Object> list;

    public List<Object> parse(String data) {
        list = new ArrayList<>();
        if(TextUtils.isEmpty(data)){
            return null;
        }
        try {
            JSONArray array = new JSONArray(data);
            if (array != null) {
                for (int i = 0; i < array.length(); i++) {
                    JSONArray item = (JSONArray) array.get(i);
                    ChartsHolderEntity entity = new ChartsHolderEntity();
                    list.add(entity);
                    if (item != null && item.length() == 7) {
                        if (item.getString(0) != null) {
                            String aa = (String) item.get(0);
                            String bb = URLDecoder.decode(aa, "utf-8");
                            entity.setPerson(bb);
                        }
                        if (item.getString(1) != null) {
                            String code = (String) item.get(1);
                            entity.setCode(code);
                        }
                        if (item.getString(2) != null) {
                            String cc = (String) item.get(2);
                            String dd = URLDecoder.decode(cc, "utf-8");
                            entity.setName(dd);
                        }
                        long num = item.getLong(3);
                        entity.setNumber(num);
                        if (item.getString(4) != null) {
                            String ee = (String) item.get(4);
                            String ff = URLDecoder.decode(ee, "utf-8");
                            entity.setPosition(ff);
                        }
                            int time = (Integer) item.getInt(5);
                            entity.setTime(time+"");
                        if (item.getString(6) != null) {
                            String gg = (String) item.get(6);
                            String hh = URLDecoder.decode(gg, "utf-8");
                            entity.setReason(hh);
                        }
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return list;
    }
}
