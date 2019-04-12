package com.ez08.compass.ui;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ez08.compass.CompassApp;
import com.ez08.compass.Constants;
import com.ez08.compass.R;
import com.ez08.compass.third.UmengShareManager;
import com.ez08.compass.ui.base.BaseActivity;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.umeng.socialize.UMShareAPI;

import java.util.Map;

public class WebActivity extends BaseActivity implements View.OnClickListener {

    ProgressBar progressBar;
    TextView txTitle;
    WebView webView;

    String title;
    String url;
    String action;

    UmengShareManager shareManager;
    Context mContext;
    MyWebChromeClient chromeClient;
    MyWebViewClientent clientent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webmy_layout);
        mContext = this;
        Intent intent = getIntent();
        if (intent != null) {
            title = intent.getStringExtra("title");
            url = intent.getStringExtra("url");
            action = intent.getStringExtra("action");
        }

        progressBar = (ProgressBar) findViewById(R.id.progress);
        findViewById(R.id.img_back).setOnClickListener(this);
        txTitle = (TextView) findViewById(R.id.page_name);
        webView = (WebView) findViewById(R.id.wv);

        shareManager = new UmengShareManager(this);
        chromeClient = new MyWebChromeClient();
        clientent = new MyWebViewClientent();

        initwidget();

        if(url != null && url.contains("func=")) {
            // 策划统计
            int index = url.indexOf("func=");
            String temp = url.substring(index, url.length());

            CompassApp.addStatis(CompassApp.GLOBAL.mgr.SHARE_EXHIBITION, "0", temp,
                    System.currentTimeMillis());
            //
        }
        txTitle.setText(title);
        webView.loadUrl(url);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                finish();
                break;
        }
    }


    private void initwidget() {
        // TODO Auto-generated method stub
        webView.setBackgroundColor(mainColor);
        webView.getSettings().setDefaultTextEncodingName("gb2312");
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setBuiltInZoomControls(false);
        webView.getSettings().setAppCacheEnabled(true);
        webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);// 允许DCOM
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.getSettings().setAllowFileAccess(true);
        webView.getSettings().setAppCacheEnabled(true);
        webView.getSettings().setSaveFormData(false);
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.setWebViewClient(clientent);
        webView.setWebChromeClient(chromeClient);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
    }

    public class MyWebChromeClient extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            Log.e("progress", newProgress + "");
            progressBar.setProgress(newProgress);
            if (newProgress >= 90) {
                progressBar.setVisibility(View.GONE);
            }
        }
    }

    public class MyWebViewClientent extends WebViewClient {

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            Log.e("finish", url);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String overrideUrl) {
//            if (TextUtils.isEmpty(overrideUrl)) {
//                overrideUrl = "";
//            }
//            if (overrideUrl.startsWith("znzapp://")) {
//                if(url.contains("func=")){
//                    int index = url.indexOf("func=");
//                    String temp = url.substring(index,url.length());
//                    CompassApp.addStatis( CompassApp.GLOBAL.mgr.SHARE_PLAN, "0", temp,
//                            System.currentTimeMillis());
//                    shareManager.shareFactory(overrideUrl, CompassApp.GLOBAL.mgr.SHARE_PLAN + "@" + temp);
//                }else {
//                    shareManager.shareFactory(overrideUrl, null);
//                }
//
//                Log.e("znzapp", overrideUrl);
//                return true;
//            }
//
//            if (overrideUrl.contains("paytype=wxpay")) {//调用微信支付
//                IWXAPI mWxApi = WXAPIFactory.createWXAPI(WebActivity.this,Constants.WX_APP_ID, true);
//                // 将该app注册到微信
//                mWxApi.registerApp(Constants.WX_APP_ID);
//                // 判断是否安装客户端
//                if (!mWxApi.isWXAppInstalled() && !mWxApi.isWXAppSupportAPI()) {
//                    Toast.makeText(WebActivity.this, "请您先安装微信客户端!", Toast.LENGTH_SHORT);
//                    return true;
//                }
//                PayReq payReq = new PayReq();
//                payReq.appId = Uri.parse(overrideUrl).getQueryParameter("appid");
//                payReq.partnerId = Uri.parse(overrideUrl).getQueryParameter("partnerid");
//                payReq.prepayId = Uri.parse(overrideUrl).getQueryParameter("prepayid");
//                payReq.packageValue = Uri.parse(overrideUrl).getQueryParameter("package");
//                payReq.nonceStr = Uri.parse(overrideUrl).getQueryParameter("noncestr");
//                payReq.timeStamp = Uri.parse(overrideUrl).getQueryParameter("timestamp");
//                payReq.sign = Uri.parse(overrideUrl).getQueryParameter("sign");
//                mWxApi.sendReq(payReq);
//                CompassApp.GLOBAL.PAY_PERSONID = Uri.parse(overrideUrl).getQueryParameter("personid");
//                CompassApp.GLOBAL.PAY_PWD = Uri.parse(overrideUrl).getQueryParameter("pwd");
//                CompassApp.GLOBAL.CUSTOMER_PAYED_LEVEL = Uri.parse(overrideUrl).getQueryParameter("sl");
//                return true;
//            }
//            if (overrideUrl.contains("paytype=alipay")) {//调用支付宝支付
//                String sign = Uri.parse(overrideUrl).getQueryParameter("sign");
//                CompassApp.GLOBAL.PAY_PERSONID = Uri.parse(overrideUrl).getQueryParameter("personid");
//                CompassApp.GLOBAL.PAY_PWD = Uri.parse(overrideUrl).getQueryParameter("pwd");
//                CompassApp.GLOBAL.CUSTOMER_PAYED_LEVEL = Uri.parse(overrideUrl).getQueryParameter("sl");
//
//                final String finalOrderInfo = sign;
//                Runnable payRunnable = new Runnable() {
//
//                    @Override
//                    public void run() {
//
//                        PayTask alipay = new PayTask(MyWebActivity.this);
////                String result = alipay.payV2(orderInfo,true);
//                        Map<String, String> result = alipay.payV2(finalOrderInfo, true);
//                        Message msg = new Message();
//                        msg.what = 1000;
//                        msg.obj = result;
//                        handler.sendMessage(msg);
//                    }
//                };
//                if (!TextUtils.isEmpty(finalOrderInfo)) {
//                    // 必须异步调用
//                    Thread payThread = new Thread(payRunnable);
//                    payThread.start();
//                }
//                return true;
//            }
//
//            if (overrideUrl.contains("action=blank")) {
//                Intent intent = new Intent();
//                intent.setAction("android.intent.action.VIEW");
//                Uri uri = Uri.parse(overrideUrl);
//                intent.setData(uri);
//                startActivity(intent);
//                return true;
//            }
//
//            if (overrideUrl.contains("exe")) {
//                Uri uri = Uri.parse(overrideUrl);
//                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//                startActivity(intent);
//                return true;
//            }
//
//            if(overrideUrl.startsWith("tel://")){
//                String num = overrideUrl.substring(6,overrideUrl.length());
//                Uri uri = Uri.parse("tel:" + num);
//                Intent it = new Intent(Intent.ACTION_DIAL, uri);
//                mContext.startActivity(it);
//                return true;
//            }

            view.loadUrl(overrideUrl);
            return true;
        }
    }

//    private Handler handler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            switch (msg.what) {
//                case 1000:
//                    Map<String, String> result = (Map<String, String>) msg.obj;
//                    if (result != null) {
//                        String resultStatus = result.get("resultStatus");
//                        String memo = result.get("memo");
//                        if (!TextUtils.isEmpty(resultStatus)) {
////                            if(resultStatus.equals("9000")||resultStatus.equals("8000")){
//                            Intent intent = new Intent(WebActivity.this, WXPayEntryActivity.class);
//                            intent.putExtra("type", "alipay");
//                            if (resultStatus.equals("9000") || resultStatus.equals("8000")) {
//                                intent.putExtra("status", 0);
//                            } else {
//                                intent.putExtra("status", -1);
//                                intent.putExtra("memo", memo);
//                            }
//                            startActivity(intent);
//                            return;
////                            }
//                        }
//                    }
//                    CompassApp.addStatis(CompassApp.mgr.PAYMENT, "1", "", System.currentTimeMillis());
//                    Toast.makeText(getApplicationContext(), "支付失败", Toast.LENGTH_LONG).show();
//                    break;
//            }
//        }
//    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }


}
