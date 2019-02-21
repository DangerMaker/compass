package com.ez08.compass.net;

import android.content.Context;
import android.util.Log;

import com.ez08.compass.tools.MyHostnameVerifier;
import com.ez08.compass.tools.MyX509TrustManager;
import com.ez08.compass.tools.UtilTools;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.GeneralSecurityException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.X509TrustManager;

/**
 * 简单http请求
 */
public class HttpUtils {

    /**
     * 意见反馈
     *
     * @param content
     * @return
     */
    public String connect(String content, Context context) {
        String result = "";
        try {
            content = URLEncoder.encode(content, "utf8");
        } catch (UnsupportedEncodingException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        String url = "http://www.compass.cn/about/feedback.php?"
                + UtilTools.getDate(context) + "&content=" + content;
        Log.e("URL", url);
        // 生成请求对象
        HttpGet httpGet = new HttpGet(url);
        HttpClient httpClient = new DefaultHttpClient();
        HttpParams params = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(params, 10000); // 设置连接超时
        HttpConnectionParams.setSoTimeout(params, 10000); // 设置请求超时
        httpGet.setParams(params);
        // 发送请求
        try {

            HttpResponse response = httpClient.execute(httpGet);

            // 显示响应
            result = showResponseResult(response);// 一个私有方法，将响应结果显示出来

        } catch (Exception e) {
            result = "";
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 分发服务器
     *
     * @param url
     * @return
     */
    public String distribute(String url) {
        String result = "";
        // url ="http://192.168.3.6/update.php";
        HttpGet httpGet = new HttpGet(url);
        HttpClient httpClient = new DefaultHttpClient();
        HttpParams params = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(params, 10000); // 设置连接超时
        HttpConnectionParams.setSoTimeout(params, 10000); // 设置请求超时
        httpGet.setParams(params);
        // 发送请求
        try {

            HttpResponse response = httpClient.execute(httpGet);

            // 显示响应
            result = showResponseResult(response);// 一个私有方法，将响应结果显示出来

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 显示响应结果到命令行和TextView
     *
     * @param response
     */
    private String showResponseResult(HttpResponse response) {
        String result = "";
        if (null == response) {
            return "";
        }

        HttpEntity httpEntity = response.getEntity();
        try {
            InputStream inputStream = httpEntity.getContent();
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    inputStream));

            String line = "";
            while (null != (line = reader.readLine())) {
                result += line;

            }

            System.out.println(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String getJsonContent(String url_path) {
        try {
            URL url = new URL(url_path);
            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();
            connection.setConnectTimeout(10 * 1000);
            connection.setReadTimeout(10 * 1000);
            connection.setRequestMethod("GET");
            connection.setDoInput(true);

            int code = connection.getResponseCode();
            if (code == 200) {
                return changeInputStream(connection.getInputStream());
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return "";
    }

    public static String getJsonContent1(String url_path) throws IOException, NoSuchAlgorithmException, KeyManagementException {
//        MyX509TrustManager xtm = new MyX509TrustManager();
//        MyHostnameVerifier hnv = new MyHostnameVerifier();
//
//        SSLContext sslContext = null;
//        sslContext = SSLContext.getInstance("TLS"); //或SSL
//        X509TrustManager[] xtmArray = new X509TrustManager[]{xtm};
//        sslContext.init(null, xtmArray, new java.security.SecureRandom());
//        if (sslContext != null) {
//            HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
//        }
//        HttpsURLConnection.setDefaultHostnameVerifier(hnv);

        URL url = new URL(url_path);
        HttpURLConnection connection = (HttpURLConnection) url
                .openConnection();
        connection.setConnectTimeout(10 * 1000);
        connection.setReadTimeout(10 * 1000);
        connection.setRequestMethod("GET");
        connection.setDoInput(true);
        int code = connection.getResponseCode();
        if (code == 200) {
            return changeInputStream(connection.getInputStream());
        }
        return "";
    }

    private static String changeInputStream(InputStream inputStream) {
        // TODO Auto-generated method stub
        String jsonString = "";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        int len = 0;
        byte[] data = new byte[1024];
        try {
            while ((len = inputStream.read(data)) != -1) {
                outputStream.write(data, 0, len);
            }
            jsonString = new String(outputStream.toByteArray());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return jsonString;
    }
}
