package com.ez08.compass.tools;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import com.ez08.compass.entity.NewAdvertEntity;
import com.ez08.compass.entity.NewAdvertRespone;
import com.ez08.compass.net.HttpUtils;
import com.ez08.compass.auth.AuthUserInfo;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdsManager {

    public static String ADS_FINISH = "compass.ads.finish";
    private static AdsManager adsManager;

    public static AdsManager getInstance(Context context) {
        if (adsManager == null) {
            adsManager = new AdsManager(context);
        }
        return adsManager;
    }

    private Context context;


    public AdsManager(Context context) {
        this.context = context.getApplicationContext();

    }

    public void setUrl(String path) {
        AdsAsyncTask task = new AdsAsyncTask(context);
        task.execute(path);
    }

    public NewAdvertEntity getAdsAtOptional() {
        return getEntity("4");
    }

    public NewAdvertEntity getAdsAtLivingRoom() {
        return getEntity("3");
    }

    public NewAdvertEntity getAdsAtNews() {
        return getEntity("1");
    }

    public NewAdvertEntity getAdsAtSplash() {
        return getEntity("0");
    }

    public String getCid(){
        SharedPreferences sharedPreferences = context.getSharedPreferences("ads",
                Activity.MODE_PRIVATE);
        String cid = sharedPreferences.getString("cid", "");
        return cid;
    }

    public void clearCid(){
        SharedPreferences sharedPreferences = context.getSharedPreferences("ads",
                Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("cid","");
        editor.commit();
    }

    public void close() {

    }


    private NewAdvertEntity getEntity(String showPlace) {
        String tempJson;
        SharedPreferences sharedPreferences = context.getSharedPreferences("ads",
                Activity.MODE_PRIVATE);
        String sJson = sharedPreferences.getString("json", "");
        if (!sJson.equals("")) {
            tempJson = sJson;
        } else {
            return null;
        }


        Gson gson = new Gson();
        NewAdvertRespone response = gson.fromJson(tempJson, NewAdvertRespone.class);
        List<NewAdvertEntity> list = response.data;
        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getShowplace().equals(showPlace)) {
                    return list.get(i);
                }
            }
        }

        return null;
    }

    private static class AdsAsyncTask extends AsyncTask<String, Void, Void> {

        Context context;

        public AdsAsyncTask(Context context) {
            this.context = context;
        }

        @Override
        protected Void doInBackground(String... strings) {
            HttpUtils httpUtils = new HttpUtils();
            Log.e("AdsManager",strings[0]);
            String result = httpUtils.getJsonContent(strings[0]);

            if (TextUtils.isEmpty(result)) {
                return null;
            }

            Gson gson = new Gson();
            NewAdvertRespone response = gson.fromJson(result, NewAdvertRespone.class);
            if (response != null) {
                Map<String, NewAdvertEntity> map = new HashMap<>();
                List<NewAdvertEntity> list = response.data;
                if (list != null) {
                    for (int i = 0; i < list.size(); i++) {
                        map.put(list.get(i).getShowplace(), list.get(i));
                    }
                }

                //保存图片
                Log.i("AdsManager", "保存图片");
                if (map.containsKey("0")) {
                    saveBitmap(map.get("0").getImageurl(), AuthUserInfo.getMyCid()+ "_splashAd.png");
                }

                if (map.containsKey("1")) {
                    saveBitmap(map.get("1").getImageurl(), AuthUserInfo.getMyCid()+ "_newsAd.png");
                }

                //存json
                Log.i("AdsManager", "存json");
                SharedPreferences sharedPreferences = context.getSharedPreferences("ads",
                        Activity.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                Log.e("saveJson", result);
                editor.putString("json", result);
                editor.putString("cid",AuthUserInfo.getMyCid());
                editor.commit();

                Intent intent = new Intent();
                intent.setAction(ADS_FINISH);
                context.sendBroadcast(intent);

            }
            return null;
        }

        private void saveBitmap(String imgUrl, String name) {
            try {
                final File cacheDir = context.getFilesDir();
                File tempFile = UtilTools.buildFile(cacheDir, name);
                URL url = new URL(imgUrl);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setConnectTimeout(3000);
                urlConnection.setReadTimeout(3000);
                urlConnection.connect();
                FileOutputStream fileOutputStream = new FileOutputStream(tempFile);

                if (urlConnection.getResponseCode() == 200) {
                    InputStream inputStream = urlConnection.getInputStream();
                    byte[] bytes = new byte[1024];
                    int len;
                    while ((len = inputStream.read(bytes)) != -1) {
                        fileOutputStream.write(bytes, 0, len);
                    }
                    //将缓冲刷入文件
                    fileOutputStream.flush();
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
