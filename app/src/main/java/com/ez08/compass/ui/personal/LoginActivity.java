package com.ez08.compass.ui.personal;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Looper;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ez08.compass.CompassApp;
import com.ez08.compass.R;
import com.ez08.compass.net.HttpUtils;
import com.ez08.compass.tools.AuthTool;
import com.ez08.compass.tools.IndexSortManager;
import com.ez08.compass.tools.MD5;
import com.ez08.compass.tools.UtilTools;
import com.ez08.compass.ui.BaseActivity;
import com.ez08.compass.ui.MainActivity;
import com.ez08.compass.ui.view.MyDelEditetext;
import com.ez08.compass.userauth.AuthModule;
import com.ez08.compass.userauth.AuthUserInfo;
import com.ez08.compass.userauth.SysVarsManager;
import com.ez08.support.net.EzNet;
import com.ez08.support.net.NetManager;
import com.ez08.support.net.NetResponseHandler2;
import com.ez08.tools.IntentTools;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends BaseActivity implements OnClickListener {
    private MyDelEditetext mName;
    private MyDelEditetext mPassword;
    public static final String TEC_PHONE = "tec_auth_phone";
    public final int WHAT_LOGIN_RESPONSE = 1002;// 登录请求
    public static final String ALL_FINISH_PROJECT = "com.compass.all.finish";
    private WebView wv;
    public static boolean isOnLogin = false;

    private Context mContext;
    private SharedPreferences mySharedPreferences;

    private boolean AUTO_LOGIN = true;  //自动登录的开关
    private CheckBox mAutoCheck;
    private boolean mIsCheck;
    private String mMd5PassWord;
    private final String mTmpPassword = "000000";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        isOnLogin = true;
        mContext = this;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        mySharedPreferences = getSharedPreferences(
                "kefu", Activity.MODE_PRIVATE);
        mIsCheck = mySharedPreferences.getBoolean("md5check", false);
        mMd5PassWord = mySharedPreferences.getString("md5password", "");

        setContentView(R.layout.activity_login);
        mAutoCheck = (CheckBox) findViewById(R.id.login_auto_check);

        if (mIsCheck) {
            mAutoCheck.setChecked(true);
        } else {
            mAutoCheck.setChecked(false);
        }

        mAutoCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mIsCheck = isChecked;
                SharedPreferences.Editor editor = mySharedPreferences
                        .edit();
                editor.putBoolean("md5check", isChecked);
                editor.commit();
            }
        });
        mName = (MyDelEditetext) findViewById(R.id.userName_edit);
        mName.setText(SysVarsManager.getString(TEC_PHONE, ""));
        mName.setFocusable(true);
        mPassword = (MyDelEditetext) findViewById(R.id.passWord_edit);
        if (mIsCheck && !TextUtils.isEmpty(mMd5PassWord)) {
            mPassword.setText(mTmpPassword);
        }

        mPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(mMd5PassWord)) {
                    mMd5PassWord = "";
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        Button lNext = (Button) findViewById(R.id.load_btn);
        lNext.setOnClickListener(this);

        TextView lForget = (TextView) findViewById(R.id.forget_pass);
        lForget.setOnClickListener(this);
        TextView regist_btn = (TextView) findViewById(R.id.regist_btn);
        regist_btn.setOnClickListener(this);
        IntentFilter filter = new IntentFilter();
        filter.addAction(ALL_FINISH_PROJECT);
        registerReceiver(finishReceiver, filter);

        String content = "没有账号?    立即注册";
        SpannableString ss = new SpannableString(content);
        ss.setSpan(
                new ForegroundColorSpan(getResources().getColor(
                        R.color.lable_item_style)), 0, content.length() - 2,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(
                new ForegroundColorSpan(getResources().getColor(
                        R.color.grand_blue)), content.length() - 2,
                content.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        regist_btn.setText(ss);
        mName.postDelayed(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                showSoftInput(mName);
            }
        }, 100);
        if (!mName.getText().toString().equals("")) { // 卡号信息存在，光标在密码框上
            mPassword.setFocusable(true);
            mPassword.setFocusableInTouchMode(true);
            mPassword.requestFocus();
            mPassword.requestFocusFromTouch();
        }

        lgroup = (RelativeLayout) findViewById(R.id.login_wv);
        wv = (WebView) findViewById(R.id.wv);
        wv.getSettings().setDefaultTextEncodingName("gb2312");
        wv.getSettings().setSupportZoom(true);
        wv.getSettings().setBuiltInZoomControls(false);
        wv.getSettings().setAppCacheEnabled(true);
        wv.getSettings().setLayoutAlgorithm(LayoutAlgorithm.NORMAL);
        wv.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        wv.getSettings().setJavaScriptEnabled(true);
        wv.getSettings().setDomStorageEnabled(true);// 允许DCOM
        wv.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        wv.getSettings().setAllowFileAccess(true);
        wv.getSettings().setAppCacheEnabled(true);
        wv.getSettings().setSaveFormData(false);
        wv.getSettings().setLoadsImagesAutomatically(true);
        wv.postDelayed(heartRunnable, 5 * 1000);

        NotificationManager nm = (NotificationManager) LoginActivity.this
                .getSystemService(Context.NOTIFICATION_SERVICE);
        nm.cancelAll();

        String contented = "&model=" + android.os.Build.MODEL + "&version="
                + android.os.Build.VERSION.RELEASE + "&os=android";

        //自动登录逻辑
        Intent intented = getIntent();
        boolean lromStart = intented.getBooleanExtra("fromstart", false);
        boolean lInstall = intented.getBooleanExtra("install", false);
        if (lromStart && lInstall) {    //是从初始化来的,且初次登入
            //判断是否初次登陆
            //获取mac地址
            //初次登陆，请求登录账号密码
            String chid = UtilTools.getChannel(mContext);
            String url = "";
            String ts = System.currentTimeMillis() / 1000 + "";
            String chksum = "";
            String verifyCode = "as*)990043JJLBNA:BBjj99913456*HK:adgj";
            chksum = MD5.mD5Encode(verifyCode + ts + verifyCode, "UTF-8");
            String lEncodeContented = contented;

            url = "http://app.compass.cn/autologin.php?chid=" + chid + "&ts=" + ts + "&chksum=" + chksum + lEncodeContented;
            final HttpUtils u = new HttpUtils();
            final String finalUrl = url;
            new Thread() {
                public void run() {
                    String result = u.getJsonContent(finalUrl);
                    autoLogin(result);
                }
            }.start();
        }

        checkClipboard();

        if (CompassApp.GLOBAL.THEME_STYLE == 0) {
            lNext.setBackgroundResource(R.drawable.custom_red_button);
        } else {
            lNext.setBackgroundResource(R.drawable.custom_night_button);
        }
    }

    //自动登录
    private void autoLogin(String result) {
        String lName = "";
        String lPassword = "";
        try {
            JSONObject obj = new JSONObject(result);
            if (!obj.isNull("ret")) {
                String ret = obj.getString("ret");
                if (TextUtils.equals(ret, "OK")) {
                    JSONObject data = obj.getJSONObject("data");
                    if (data != null) {
                        if (!data.isNull("nid")) {
                            lName = data.getString("nid");
                        }
                        if (!data.isNull("pwd")) {
                            lPassword = data.getString("pwd");
                        }
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (!TextUtils.isEmpty(lName) && !TextUtils.isEmpty(lPassword)) {
            final String finalLName = lName;
            final String finalLPassword = lPassword;
            ((LoginActivity) mContext).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //此时已在主线程中，可以更新UI了
                    goToNetWork(finalLName, finalLPassword);
                }
            });

        }
    }


    Runnable heartRunnable = new Runnable() {

        @Override
        public void run() {
            // TODO Auto-generated method stub
            if (!TextUtils.isEmpty(AuthUserInfo.getMyTid())) {
                wv.loadUrl("http://app.compass.cn/stat/fpb.php?"
                        + UtilTools.getDate(getApplicationContext()));
            } else {
                wv.postDelayed(heartRunnable, 5 * 1000);
            }
        }

    };

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.load_btn:
                String msg = AuthUserInfo.getMyTid();
                String msg1 = AuthUserInfo.getMyCid();
                Log.e("", "tid=" + msg);
                Log.e("", "cid=" + msg1);

                String lName = mName.getText().toString().trim();
                String lpassword = mPassword.getText().toString().trim();
                goToNetWork(lName, lpassword);
                break;
            // TODO: 2019/1/31
            /*case R.id.forget_pass:
                Intent find = new Intent(LoginActivity.this,
                        ForgetPassActivity.class);
                find.putExtra("type", VerifyCodeActivity.FORGET_PASS);
                startActivity(find);
                break;
            case R.id.regist_btn:
                Intent regist = new Intent(LoginActivity.this,
                        VerifyCodeActivity.class);
                regist.putExtra("type", VerifyCodeActivity.REGIST_USER); // 注册
                startActivity(regist);
                break;*/
            default:
                break;
        }
    }


    @SuppressLint("HandlerLeak")
    private NetResponseHandler2 mHandler = new NetResponseHandler2() {
        @Override
        public void netConnectLost(int arg0) {
            dismissBusyDialog();
        }

        @Override
        public void timeout(int arg0) {
            dismissBusyDialog();
        }

        @Override
        public void receive(int arg0, boolean arg1, Intent intent) {
            // TODO Auto-generated method stub
            dismissBusyDialog();
            switch (arg0) {
                case WHAT_LOGIN_RESPONSE:
                    String msg = AuthUserInfo.getMyTid();
                    String msg1 = AuthUserInfo.getMyCid();
                    Log.e("", "tid=" + msg);
                    Log.e("", "cid=" + msg1);
                    if (intent != null) {
                        if ("ez08.auth.login.response".equalsIgnoreCase(intent
                                .getAction())) {
                            int sl = intent.getIntExtra("level", -1);
                            CompassApp.GLOBAL.CUSTOMER_LEVEL = sl;
                            SharedPreferences.Editor editor = mySharedPreferences
                                    .edit();
                            editor.putInt("compass_level", sl);
                            String auths = intent.getStringExtra("auths");
                            if (auths == null) {
                                auths = "";
                            }
                            CompassApp.GLOBAL.HAS_LEVEL2 = false;
                            if (!TextUtils.isEmpty(auths) && CompassApp.GLOBAL.level2ID != null) {
                                for (int i = 0; i < CompassApp.GLOBAL.level2ID.length; i++) {
                                    if (auths.contains(CompassApp.GLOBAL.level2ID[i])) {
                                        CompassApp.GLOBAL.HAS_LEVEL2 = true;
                                        break;
                                    }
                                }
                            }
                            editor.putString("auths", auths);
                            if (mIsCheck) {
                                editor.putString("md5password", mMd5PassWord);
                            }
                            editor.commit();
                            //确认auth
                            AuthTool.initType(sl, auths);
                            // 登录成功
                            AuthModule.UserAuthLoginSuccess(intent);
                            // 进入主界面
                            // startMain();
                            isOnLogin = false;
                            Intent i = new Intent(LoginActivity.this,
                                    MainActivity.class);
                            startActivity(i);
                            finish();

                            //初始化排序
                            IndexSortManager.init(mContext);
                            CompassApp.addStatis(CompassApp.GLOBAL.mgr.LOGIN, "0", AuthUserInfo.getMyCid(), System.currentTimeMillis());
                        } else {
                            // 登录失败
                            String msgstr = intent.getStringExtra("msg");
                            if (msgstr != null) {
                                if (msgstr.contains("数据包解码失败")) {
                                    Toast.makeText(getApplicationContext(), "数据包解码失败，请重启app",
                                            Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(getApplicationContext(), msgstr,
                                            Toast.LENGTH_LONG).show();
                                }
                            }
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "登录失败",
                                Toast.LENGTH_LONG).show();
                        CompassApp.addStatis(CompassApp.GLOBAL.mgr.LOGIN, "1", AuthUserInfo.getMyCid(), System.currentTimeMillis());
                    }
                    break;

                default:
                    break;
            }
        }

    };

    private void goToNetWork(String lName, String lpassword) {
        if (TextUtils.isEmpty(lpassword)) {
            Toast.makeText(getApplicationContext(), "请输入密码", Toast.LENGTH_SHORT)
                    .show();
            return;
        }

        if (UtilTools.isSpecialCharerPassword(lpassword.trim())) {
            Toast.makeText(getApplicationContext(), "密码不包括中文,请重新输入!",
                    Toast.LENGTH_LONG).show();
            return;
        }

        if (lpassword.getBytes().length > 18) {
            Toast.makeText(getApplicationContext(), "请输入长度不超过14位的密码",
                    Toast.LENGTH_LONG).show();
            return;
        }
        if (lpassword.getBytes().length < 6) {
            Toast.makeText(getApplicationContext(), "请输入长度不少于6位的密码",
                    Toast.LENGTH_LONG).show();
            return;
        }
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();
        if (info == null || !info.isAvailable()) {
            Toast.makeText(this, "没有可用网络", Toast.LENGTH_SHORT).show();
        } else {
            Intent intent = new Intent(NetManager.ACTION_AUTH_LOGIN);
            intent.putExtra("mobile", lName);
            String pass;
            if (!TextUtils.isEmpty(mMd5PassWord)) {
                pass = mMd5PassWord;
            } else {
                pass = MD5.mD5Encode(lpassword, "UTF-8");
                mMd5PassWord = pass;
            }
            Log.e("s", pass + "==================password");
            intent.putExtra("pwd", pass);

            EzNet.Request(IntentTools.intentToMessage(intent),
                    mHandler, WHAT_LOGIN_RESPONSE, 2, 0);
            SysVarsManager.putString(TEC_PHONE, lName);
            showBusyDialog();
        }
    }

    protected void dismissBusyDialog() {
        if (pDialog != null) {
            pDialog.dismiss();
        }
    }

    ProgressDialog pDialog;

    protected void showBusyDialog() {
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("请稍候...");
        pDialog.setCancelable(true);
        pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        if (Thread.currentThread() == Looper.getMainLooper().getThread()) {
            pDialog.show();
        } else {
            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    pDialog.show();
                }
            });
        }
    }

    private BroadcastReceiver finishReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context arg0, Intent arg1) {
            // TODO Auto-generated method stub
            finish();
        }

    };
    private RelativeLayout lgroup;

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        lgroup.removeAllViews();
        isOnLogin = false;
    }

    //判断剪切板的内容来实现自动登录
    public void checkClipboard() {
        ClipboardManager mClipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        ClipData mClipData = mClipboardManager.getPrimaryClip();
        if (mClipData != null && mClipData.getItemCount() != 0) {
            ClipData.Item item = mClipData.getItemAt(0);
            if (item != null && !TextUtils.isEmpty(item.getText())) {
                String phoneString = item.getText().toString();
                if (!TextUtils.isEmpty(phoneString) && phoneString.contains("【指南针】")) {
//        String phoneString = "【指南针】恭喜您激活成功！您的卡号：200392196，密码：761941。客服热线：4008188080\n";
//        String phoneString = "【指南针】您已激活指南针软件，卡号197693773，密码64874676，下载d.znz888.cn ";
                    // 提取数字
                    String card = "";
                    String password = "";
                    if (phoneString.contains("卡号")) {
                        int indxe = phoneString.indexOf("卡号");
                        String lString0 = phoneString.substring(indxe, phoneString.length());
                        String[] lString = lString0.split("");
                        for (int i = 0; i < lString.length; i++) {
                            if (!isDigit(lString[i]) && !TextUtils.isEmpty(card)) {
                                break;
                            }
                            if (isDigit(lString[i])) {
                                card = card + lString[i];
                            }
                        }
                    }

                    if (phoneString.contains("密码")) {
                        int indxe = phoneString.indexOf("密码");
                        String lString0 = phoneString.substring(indxe, phoneString.length());
                        String[] lString = lString0.split("");
                        for (int i = 0; i < lString.length; i++) {
                            if (!isDigit(lString[i]) && !TextUtils.isEmpty(password)) {
                                break;
                            }
                            if (isDigit(lString[i])) {
                                password = password + lString[i];
                            }
                        }
                    }
                    if (!TextUtils.isEmpty(card) && !TextUtils.isEmpty(password)) {
                        mClipboardManager.setPrimaryClip(ClipData.newPlainText("Label", ""));
                        goToNetWork(card, password);
                    }
                }
            }
        }
    }

    // 判断一个字符串是否都为数字
    public boolean isDigit(String strNum) {
        return strNum.matches("[0-9]{1,}");
    }

}
