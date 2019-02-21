package com.ez08.compass.tools;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import com.ez08.compass.CompassApp;
import com.ez08.compass.auth.AuthModule;
import com.ez08.compass.auth.AuthUserInfo;
import com.ez08.compass.entity.InitEntity;
import com.ez08.compass.entity.NewAdvertEntity;
import com.ez08.compass.entity.NewAdvertRespone;
import com.ez08.compass.net.HttpUtils;
import com.ez08.compass.ui.SplashActivity;
import com.ez08.support.net.NetManager;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.ez08.compass.tools.AdsManager.ADS_FINISH;

public class LoadBalancingManager {

    public static String LOAD_BALANCING_START = "compass.load.balancing.start";
    public static String LOAD_BALANCING_FINISH = "compass.load.balancing.finish"; //success true false message
    private static LoadBalancingManager loadManager;

    public static LoadBalancingManager getInstance(Context context) {
        if (loadManager == null) {
            loadManager = new LoadBalancingManager(context);
        }
        return loadManager;
    }

    private Context context;

    public LoadBalancingManager(Context context) {
        this.context = context.getApplicationContext();

    }

    public void setUrl(String path) {
        AdsAsyncTask task = new AdsAsyncTask(context);
        task.execute(path);
    }

    private static class AdsAsyncTask extends AsyncTask<String, Void, Void> {

        Context context;
        private boolean success = true;
        private String message = "未知错误";

        public AdsAsyncTask(Context context) {
            this.context = context;
        }

        @Override
        protected Void doInBackground(String... strings) {
            Log.e("LoadBalancingManager", strings[0]);

            try {
                String result = HttpUtils.getJsonContent1(strings[0]);
                if (TextUtils.isEmpty(result)) {
                    throw new IOException();
                }
                //todo 本地存储未加
                InitEntity entity = parserResult(result);
                if (entity != null) {
                    String server = entity.getServer();
                    int index = server.indexOf(":");
                    String t_ip = server.substring(0, index);
                    CompassApp.GLOBAL.IP = getIP(t_ip);
                    CompassApp.GLOBAL.PORT = Integer.parseInt(server
                            .substring(index + 1));
                    UpLoadTools.URL = entity.getImageupload();
                    CompassApp.GLOBAL.ADVERT_URL = entity.getAdurl2();
                    success = true;
                    message = "连接成功";
                }

            } catch (UnknownHostException e) {
                e.printStackTrace();
                success = false;
                message = "未知的地址";
            } catch (IOException e) {
                e.printStackTrace();
                success = false;
                message = "请检查网路";
            } catch (JSONException e) {
                e.printStackTrace();
                success = false;
                message = "数据解析错误";
            }catch (Exception e){
                e.printStackTrace();
                success = false;
                message = "其他错误";
            } finally {
                Intent intent = new Intent();
                intent.setAction(LOAD_BALANCING_FINISH);
                intent.putExtra("success", success);
                intent.putExtra("message",message);
                context.sendBroadcast(intent);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
             if(success){
                 NetManager.setIpAndPort(CompassApp.GLOBAL.IP, CompassApp.GLOBAL.PORT);
                 AuthModule.startNet();
            }
        }
    }

    public static String getIP(String name) throws UnknownHostException {
        InetAddress address = null;
        address = InetAddress.getByName(name);
        return address.getHostAddress();
    }

    private static InitEntity parserResult(String result) throws JSONException {
        JSONObject data;
        InitEntity entity = new InitEntity();

        data = new JSONObject(result);
        if (!data.isNull("server")) {
            entity.setServer(data.getString("server"));
        }
        if (!data.isNull("imageupload")) {
            entity.setImageupload(data.getString("imageupload"));
        }
        if (!data.isNull("date")) {
            entity.setDate(data.getString("date"));
        }
        if (!data.isNull("info")) {
            entity.setInfo(data.getString("info"));
        }
        if (!data.isNull("url")) {
            entity.setUrl(data.getString("url"));
        }
        if (!data.isNull("version")) {
            entity.setVersion(data.getString("version"));
        }
        if (!data.isNull("adurl")) {
            entity.setAdurl(data.getString("adurl"));
        }

        if (!data.isNull("adurl2")) {
            entity.setAdurl2(data.getString("adurl2"));
        }
        if (!data.isNull("authsmap")) {
            JSONObject authMap = data.getJSONObject("authsmap");
            if (authMap != null) {
                String level2 = authMap.getString("level2");
                String level2s[] = level2.split(",");
                CompassApp.GLOBAL.level2ID = level2s;
            }
        }
        return entity;
    }

}
