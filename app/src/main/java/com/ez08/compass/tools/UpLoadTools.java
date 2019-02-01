package com.ez08.compass.tools;

import android.util.Log;

import com.ez08.compass.entity.UpLoadEntity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;

/**
 * 上传文件类
 */
public class UpLoadTools {
    //	private final String URL="http://202.108.13.252:8080/upload";
    public static String URL = "";
    private final int TIME_OUT = 60 * 1000;   //超时时间
    private final String CHARSET = "utf-8"; //设置编码

    public UpLoadEntity uploadFile(File file) {
        UpLoadEntity load = uploadFile(file, URL);
        return load;
    }

    public UpLoadEntity uploadFile(File file, String RequestURL) {
        String result = null;
        String BOUNDARY = UUID.randomUUID().toString();  //边界标识   随机生成
        String PREFIX = "--", LINE_END = "\r\n";
        String CONTENT_TYPE = "multipart/form-data";   //内容类型
        UpLoadEntity entity = null;
        try {
            java.net.URL url = new URL(RequestURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(TIME_OUT);
            conn.setConnectTimeout(TIME_OUT);
            conn.setDoInput(true);  //允许输入�?
            conn.setDoOutput(true); //允许输出�?
            conn.setUseCaches(false);  //不允许使用缓�?
            conn.setRequestMethod("POST");  //请求方式
            conn.setRequestProperty("Charset", CHARSET);  //设置编码
            conn.setRequestProperty("connection", "keep-alive");
            conn.setRequestProperty("Content-Type", CONTENT_TYPE + ";boundary=" + BOUNDARY);
            if (file != null) {
//                OutputStream oo=conn.getOutputStream();
                /**
                 * 当文件不为空，把文件包装并且上传
                 */
                DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
                StringBuffer sb = new StringBuffer();
                sb.append(PREFIX);
                sb.append(BOUNDARY);
                sb.append(LINE_END);
                /**
                 * name里面的�?为服务器端需要key   只有这个key 才可以得到对应的文件
                 * filename是文件的名字，包含后�?���?  比如:abc.png  
                 */

                sb.append("Content-Disposition: form-data; name=\"img\"; filename=\"" + file.getName() + "\"" + LINE_END);
                sb.append("Content-Type: application/octet-stream; charset=" + CHARSET + LINE_END);
                sb.append(LINE_END);
                dos.write(sb.toString().getBytes());
                InputStream is = new FileInputStream(file);
                byte[] bytes = new byte[1024];
                int len = 0;
                while ((len = is.read(bytes)) != -1) {
                    dos.write(bytes, 0, len);
                }
                is.close();
                dos.write(LINE_END.getBytes());
                byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINE_END).getBytes();
                dos.write(end_data);
                dos.flush();
                /**
                 * 获取响应�? 200=成功
                 * 当响应成功，获取响应的流  
                 */
                int res = conn.getResponseCode();
                Log.e("TAG", "response code:" + res);
                if (res == 200) {
                    Log.e("TAG", "request success");
                    InputStream input = conn.getInputStream();
                    StringBuffer sb1 = new StringBuffer();
                    int ss;
                    while ((ss = input.read()) != -1) {
                        sb1.append((char) ss);
                    }
                    result = sb1.toString();
                    Log.e("TAG", "result : " + result);
                    entity = parserResult(result);
                } else {
                    Log.e("TAG", "request error");
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return entity;
    }

    private UpLoadEntity parserResult(String result) {
        JSONObject data = null;
        UpLoadEntity entity = new UpLoadEntity();
        try {
            data = new JSONObject(result);
            if (!data.isNull("timestamp")) {
                entity.setTimestamp(data.getString("timestamp"));
            }
            if (!data.isNull("data")) {
                JSONObject data1 = data.getJSONObject("data");
                if (!data1.isNull("imageid")) {
                    entity.setImageid(data1.getString("imageid"));
                }
                if (!data1.isNull("errmsg")) {
                    entity.setErrmsg(data1.getString("errmsg"));
                }
                if (!data1.isNull("code")) {
                    entity.setCode(data1.getInt("code"));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return entity;
    }
}
