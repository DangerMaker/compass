package com.ez08.compass.ui.personal;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.ez08.compass.CompassApp;
import com.ez08.compass.R;
import com.ez08.compass.entity.ItemStock;
import com.ez08.compass.entity.PushMessageEntity;
import com.ez08.compass.entity.StockCodeEntity;
import com.ez08.compass.net.NetInterface;
import com.ez08.compass.third.UmengShareManager;
import com.ez08.compass.third.share.LinkShare;
import com.ez08.compass.tools.PushManager;
import com.ez08.compass.tools.UtilTools;
import com.ez08.compass.ui.MainActivity;
import com.ez08.compass.ui.WebActivity;
import com.ez08.compass.ui.stocks.StockVertcialTabActivity;
import com.ez08.support.net.NetResponseHandler2;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PushMessageDialog extends Dialog implements
        View.OnClickListener {

    private Context context;
    private final static int WHAT_PUSH_RESULT = 30;
    private String imgurl;
    private String url;
    private int mWidth;
    private LinearLayout mPushLayout;
    private ProgressBar mPushBar;
    private ImageView mPushImg;
    private TextView mPushTitle;
    private TextView mPushContent;
    private ImageView mCLoseBtn;
    private RelativeLayout mEnsureBtn;
    private LinearLayout mDoubleLayout;
    private RelativeLayout mGotoBtn;
    private RelativeLayout mCancleBtn;
    private TextView mPushTv;
    private ScrollView mPushScroll;
    private PushMessageEntity mEntity;


    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            mPushTitle.setText(mEntity.getTitle());
            mPushContent.setText(mEntity.getDescription());
            mPushBar.setVisibility(View.GONE);
            mPushLayout.setVisibility(View.VISIBLE);
            switch (msg.what) {
                case 0: // 成功
                    Bitmap bmp = (Bitmap) msg.obj;
                    int height = bmp.getHeight();
                    int width = bmp.getWidth();
                    LayoutParams params = (LayoutParams) mPushImg.getLayoutParams();
                    params.width = mWidth;
                    int lHeight = mWidth * height / width;

                    post(new Runnable() {
                        @Override
                        public void run() {
                            int lScrollHeight = mPushScroll.getMeasuredHeight();
                            if (lScrollHeight < UtilTools.dip2px(getContext(), 200)) {
                                lScrollHeight = UtilTools.dip2px(getContext(), 200);
                            }
                            if (lScrollHeight > UtilTools.dip2px(getContext(), 400)) {
                                lScrollHeight = UtilTools.dip2px(getContext(), 400);
                            }
                            LayoutParams paramsScrolled = (LayoutParams) mPushScroll.getLayoutParams();
                            paramsScrolled.height = lScrollHeight;
                            mPushScroll.setLayoutParams(paramsScrolled);
                        }
                    });

                    params.height = lHeight;
                    params.gravity = Gravity.CENTER_HORIZONTAL;
                    mPushImg.setLayoutParams(params);
                    mPushImg.setImageBitmap(bmp);
                    mPushImg.setVisibility(View.VISIBLE);
                    mPushImg.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent imgIntent = new Intent(context, ImgScanActivity.class);
                            String[] lImageIds = {imgurl};
                            imgIntent.putExtra(ImgScanActivity.KEY_IMAGE_ID, lImageIds);
                            imgIntent.putExtra("targetid", 0);
                            context.startActivity(imgIntent);
                        }
                    });
                    break;
                case 1:
//                    LayoutParams paramsScroll = (LayoutParams) mPushScroll.getLayoutParams();
//                    paramsScroll.height = UtilTools.dip2px(getContext(), 300);
//                    mPushScroll.setLayoutParams(paramsScroll);
                    mPushImg.requestLayout();
                    mPushImg.setVisibility(View.GONE);
                    post(new Runnable() {
                        @Override
                        public void run() {
//                            int aa=mPushContent.getMeasuredHeight();
                            int lScrollHeight = mPushScroll.getMeasuredHeight();
                            if (lScrollHeight > UtilTools.dip2px(getContext(), 300)) {
                                lScrollHeight = UtilTools.dip2px(getContext(), 300);
                            }
                            LayoutParams paramsScrolled = (LayoutParams) mPushScroll.getLayoutParams();
                            paramsScrolled.height = lScrollHeight;
                            mPushScroll.setLayoutParams(paramsScrolled);
                        }
                    });
                    break;
                default:
                    break;
            }
            String uri = mEntity.getUri();
            if (uri != null && uri.contains("share")) {
                mPushTv.setText("分享");
                mEnsureBtn.setVisibility(View.GONE);
                mDoubleLayout.setVisibility(View.VISIBLE);
            } else if (uri == null || uri.contains("cancel")) {
                mPushTv.setText("确定");
                mCancleBtn.setVisibility(View.GONE);
//				mCLoseBtn.setVisibility(View.GONE);
                mEnsureBtn.setVisibility(View.VISIBLE);
                mDoubleLayout.setVisibility(View.GONE);
            } else {
                mPushTv.setText("前往");
                mEnsureBtn.setVisibility(View.GONE);
                mDoubleLayout.setVisibility(View.VISIBLE);
            }

        }

        ;
    };

    static NetResponseHandler2 mHandler = new NetResponseHandler2() {
        @Override
        public void receive(int arg0, boolean arg1, Intent arg2) {
            if (arg2 != null) {
                //Toast.makeText(CompassApp.getmContext(), "反馈成功!", 0).show();
            }

        }
    };

    public PushMessageDialog(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }


    public PushMessageDialog(Context context, int theme) {
        super(context, theme);
        // TODO Auto-generated constructor stub
    }


    public PushMessageDialog(Context context, PushMessageEntity entity) {
        super(context, R.style.dialog);
        this.context = context;
        if (entity == null) {
            return;
        }
        SharedPreferences pushInfoPreferences = context.getSharedPreferences(
                "homepageInfo", 0);
        Long curtime = Long.parseLong(new SimpleDateFormat("yyyyMMddHHmmss")
                .format(new Date()));
        try {
            if (curtime >= Long.parseLong(entity.getStarttime())
                    && curtime <= Long.parseLong(entity.getEndtime())) {
                pushInfoPreferences.edit().putBoolean("ifShowNotification", false)
                        .commit();
            }
        } catch (NumberFormatException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
            pushInfoPreferences.edit().putBoolean("ifShowNotification", false)
                    .commit();
        }

        mEntity = entity;
        this.imgurl = entity.getImgurl();
        new Thread() {
            public void run() {
                Bitmap bmp = null;
                try {
                    bmp = getBitmap(imgurl);
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    bmp = null;
                }
                Message msg = Message.obtain();
                if (bmp != null) {
                    msg.obj = bmp;
                    msg.what = 0;
                    handler.sendMessage(msg);
                } else {
                    msg.what = 1;
                    handler.sendMessage(msg);
                }

            }

            ;
        }.start();

    }

    @Override
    public void show() {
        // TODO Auto-generated method stub
        super.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (mEntity.getImgpos() == 0) {
            setContentView(R.layout.dialog_push_message1);
        } else {
            setContentView(R.layout.dialog_push_message2);
        }

        Display disp = getWindow().getWindowManager().getDefaultDisplay();
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        setCanceledOnTouchOutside(false);// 设置点击屏幕Dialog不消失
        // lp.y = 30; // 30是屏幕底部toolbar的高度；
        mWidth = lp.width = disp.getWidth()
                - UtilTools.dip2px(getContext(), 40);
        // lp.width = (int) (d.widthPixels * 0.8);
        lp.gravity = Gravity.CENTER;
        getWindow().setAttributes(lp);

        mPushLayout = (LinearLayout) findViewById(R.id.push_group);

        mPushBar = (ProgressBar) findViewById(R.id.push_bar);
        mCLoseBtn = (ImageView) findViewById(R.id.push_close);
        mPushLayout.setVisibility(View.GONE);
        mCLoseBtn.setVisibility(View.GONE);
        mPushBar.setVisibility(View.VISIBLE);

        mPushImg = (ImageView) findViewById(R.id.push_mes_img);
        mPushTitle = (TextView) findViewById(R.id.push_mes_title);
        mPushContent = (TextView) findViewById(R.id.push_mes_content);

        mDoubleLayout = (LinearLayout) findViewById(R.id.push_group_layout);
        mGotoBtn = (RelativeLayout) findViewById(R.id.push_msg_rbtn);
        mCancleBtn = (RelativeLayout) findViewById(R.id.push_msg_lbtn);
        mEnsureBtn = (RelativeLayout) findViewById(R.id.push_ensure_lbtn);

        mPushScroll = (ScrollView) findViewById(R.id.push_scroll);
        mPushTv = (TextView) findViewById(R.id.push_msg_tv);

        mGotoBtn.setOnClickListener(this);
        mCancleBtn.setOnClickListener(this);
        mEnsureBtn.setOnClickListener(this);

        if (CompassApp.GLOBAL.THEME_STYLE == 0) {
            mPushLayout.setBackgroundResource(R.drawable.push_dialog);
            mGotoBtn.setBackgroundResource(R.drawable.push_dialog_btn);
            mCancleBtn.setBackgroundResource(R.drawable.push_dialog_btn);
            mEnsureBtn.setBackgroundResource(R.drawable.push_dialog_btn);
        } else {
            mPushLayout.setBackgroundResource(R.drawable.push_dialog_night);
            mGotoBtn.setBackgroundResource(R.drawable.push_dialog_btn_night);
            mCancleBtn.setBackgroundResource(R.drawable.push_dialog_btn_night);
            mEnsureBtn.setBackgroundResource(R.drawable.push_dialog_btn_night);
        }
    }

    public Bitmap getBitmap(String path) throws IOException {
        URL url = new URL(path);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(5000);
        conn.setRequestMethod("GET");
        if (conn.getResponseCode() == 200) {
            InputStream inputStream = conn.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            return bitmap;
        }
        return null;

    }

    @Override
    public void onClick(View arg0) {
        // TODO Auto-generated method stub
        String pushid = mEntity.getPushid();
        switch (arg0.getId()) {
            case R.id.push_close:
            case R.id.push_msg_lbtn:
                NetInterface
                        .pushResultNotify(mHandler, WHAT_PUSH_RESULT, pushid, 9);
                dismiss();
                break;
            case R.id.push_msg_rbtn:
            case R.id.push_ensure_lbtn:
                String uri = mEntity.getUri();
                pushid = mEntity.getPushid();
                Intent intent = null;
                if (uri != null && uri.contains("gotolive")) {
                    NetInterface.pushResultNotify(mHandler, WHAT_PUSH_RESULT,
                            pushid, 3);
                    PushManager.updatePushInfoToLocal(context, pushid, true);
                    intent = new Intent(context, MainActivity.class);
                    intent.putExtra("comefrompush", true);
                    context.startActivity(intent);
                    Intent homeIntent = new Intent();
                    homeIntent.setAction(MainActivity.GOTO_LIVE);
                    homeIntent.putExtra("uri", uri);
                    context.sendBroadcast(homeIntent);
                    dismiss();
                } else if (uri != null && uri.contains("gotoinfo")) {
                    NetInterface.pushResultNotify(mHandler, WHAT_PUSH_RESULT,
                            pushid, 3);
                    PushManager.updatePushInfoToLocal(context, pushid, true);
                    startWebActivity(
                            context,
                            intent,
                            mEntity.getTitle(),
                            mEntity.getImgurl(),
                            "http://www.compass.cn/app/news.php?company=compass&newsid="
                                    + uri.substring(9) + "&"
                                    + UtilTools.getDate(context), "内参", 0);
                    dismiss();
                } else if (uri != null && uri.contains("gotovideo")) {
                    NetInterface.pushResultNotify(mHandler, WHAT_PUSH_RESULT,
                            pushid, 3);
                    PushManager.updatePushInfoToLocal(context, pushid, true);
                    startWebActivity(
                            context,
                            intent,
                            mEntity.getTitle(),
                            mEntity.getImgurl(),
                            "http://www.compass.cn/app/video.php?company=compass&newsid="
                                    + uri.substring(10) + "&"
                                    + UtilTools.getDate(context), "视频", 1);
                    dismiss();
                } else if (uri != null && uri.contains("gotostock")) {
                    String stockcode = uri.substring(10);
                    NetInterface.pushResultNotify(mHandler, WHAT_PUSH_RESULT,
                            pushid, 3);
                    PushManager.updatePushInfoToLocal(context, pushid, true);
                    CompassApp.GLOBAL.mStockList.clear();
                    StockCodeEntity entitye = new StockCodeEntity();
                    String code = stockcode;
                    entitye.code=code;
                    List<String> codes = new ArrayList<String>();
                    entitye.codes = codes;
                    CompassApp.GLOBAL.mStockList.add(entitye);
                    intent = new Intent(context,
                            StockVertcialTabActivity.class);
                    intent.putExtra("comefrompush", true);
                    intent.putExtra("comefrompush", true);
                    context.startActivity(intent);
                    dismiss();
                } else if (uri != null && uri.contains("gotourl")) {
                    NetInterface.pushResultNotify(mHandler, WHAT_PUSH_RESULT,
                            pushid, 3);
                    PushManager.updatePushInfoToLocal(context, pushid, true);
                    try {
                        intent = new Intent();
                        intent.setAction("android.intent.action.VIEW");
                        Uri content_url = Uri.parse(uri.substring(8));
                        intent.setData(content_url);
                        context.startActivity(intent);
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    dismiss();
                } else if (uri != null && uri.contains("share")) {
                    NetInterface.pushResultNotify(mHandler, WHAT_PUSH_RESULT,
                            pushid, 3);
                    PushManager.updatePushInfoToLocal(context, pushid, true);
                    UmengShareManager shareManager = new UmengShareManager((Activity) context);
                    shareManager.share(new LinkShare(mEntity.getTitle(),mEntity.getImgurl(),
                            mEntity.getDescription(),uri.substring(6), pushid),null);
                    dismiss();
                } else {
                    PushManager.updatePushInfoToLocal(context, pushid, true);
                    dismiss();
                }
                break;
            default:
                break;
        }

    }

    @Override
    public void dismiss() {
        // TODO Auto-generated method stub
        super.dismiss();
    }

    protected void startWebActivity(Context context, Intent intent, String name, String imgurl, String url,
                                    String title, int type) {
        intent = new Intent(context, WebActivity.class);
        ItemStock lEntity = new ItemStock();
        lEntity.setUrl(url);
        lEntity.setName(name);
        lEntity.setTitle(title);
        intent.putExtra("entity", lEntity);
        intent.putExtra("type", type);
        intent.putExtra("imgId", imgurl);
        intent.putExtra("shareurl", url);
        context.startActivity(intent);

    }
}
