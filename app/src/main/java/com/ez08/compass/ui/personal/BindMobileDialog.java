package com.ez08.compass.ui.personal;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ez08.compass.R;
import com.ez08.compass.tools.ToastUtils;

import java.lang.ref.WeakReference;

public class BindMobileDialog extends Dialog implements
        View.OnClickListener {

    TextView getCodeView;
    TextView contentView;
    RelativeLayout lButton;
    RelativeLayout rButton;
    EditText mobileView;
    EditText verifyView;
    TextView cancelText;

    BindListener bindListener;
    boolean force = false;
    String content;
    IntervalHandler intervalHandler;

    public BindMobileDialog(Context context, int forceInt, String content) {
        super(context,R.style.dialog);
        // TODO Auto-generated constructor stub
        if(forceInt == 1){
            force = false;
        }else if(forceInt == 2){
            force = true;
        }
        this.content = content;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_bind_mobile);
        setCanceledOnTouchOutside(false);
//        setCancelable(false);

        getCodeView = (TextView) findViewById(R.id.kaihu_infy_code);
        lButton = (RelativeLayout) findViewById(R.id.push_msg_lbtn);
        rButton = (RelativeLayout) findViewById(R.id.push_msg_rbtn);
        mobileView = (EditText) findViewById(R.id.kaihu_decide_mobile);
        verifyView = (EditText) findViewById(R.id.kaihu_verify_edite);
        contentView = (TextView) findViewById(R.id.mes_content);
        cancelText = (TextView) findViewById(R.id.push_cancle_tv);
        getCodeView.setOnClickListener(this);
        lButton.setOnClickListener(this);
        rButton.setOnClickListener(this);
        intervalHandler = new IntervalHandler(this);
        contentView.setText(content);

        if(force){
            cancelText.setText("退出");
        }else{
            cancelText.setText("取消");
        }
    }


    @Override
    public void onClick(View arg0) {
        // TODO Auto-generated method stub
        switch (arg0.getId()) {
            case R.id.kaihu_infy_code:
                String mobile = mobileView.getText().toString();
                if (TextUtils.isEmpty(mobile)) {
                    ToastUtils.show(getContext(), "请输入手机号");
                    return;
                }
                bindListener.sendVerifyCode(mobile, new VerifyCode() {
                    @Override
                    public void finish() {
                        //倒计时
                        intervalHandler.sendEmptyMessage(0);
                    }
                });
                break;
            case R.id.push_msg_rbtn:
                String mobile1 = mobileView.getText().toString();
                String verity1 = verifyView.getText().toString();
                if (TextUtils.isEmpty(mobile1)) {
                    ToastUtils.show(getContext(), "请输入手机号");
                    return;
                }
                if (TextUtils.isEmpty(verity1)) {
                    ToastUtils.show(getContext(), "请输出验证码");
                    return;
                }

                bindListener.bindMobile(mobile1, verity1, new Bind() {
                    @Override
                    public void finish() {
                        if (isShowing()) {
                            dismiss();
                        }
                    }
                });
                break;
            case R.id.push_msg_lbtn:
                bindListener.cancel(force);
                break;
            default:
                break;
        }

    }


    public void setNormal() {
        getCodeView.setText("获取验证码");
        getCodeView.setClickable(true);
        getCodeView.setTextColor(ContextCompat.getColor(getContext(), R.color.mediumblue));
    }

    public void setInterval(long mCurrentTime) {
        getCodeView.setTextColor(ContextCompat.getColor(getContext(), R.color.lable_item_style));
        int count = (int) (mCurrentTime / 1000);
        getCodeView.setText(count + "秒后重发");
        getCodeView.setClickable(false);
    }

    public void setBindListener(BindListener listener) {
        bindListener = listener;
    }

    @Override
    public void dismiss() {
        // TODO Auto-generated method stub
        super.dismiss();
        intervalHandler.removeMessages(0);
    }

    static class IntervalHandler extends Handler {
        WeakReference<BindMobileDialog> reference;
        private long mCurrentTime = 60 * 1000;

        public IntervalHandler(BindMobileDialog activity) {
            this.reference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            BindMobileDialog dialog = reference.get();
            mCurrentTime = mCurrentTime - 1000;
            if (mCurrentTime <= 0) {
                dialog.setNormal();
                mCurrentTime = 60 * 1000;
            } else {
                sendEmptyMessageDelayed(0, 1000);
                dialog.setInterval(mCurrentTime);
            }
//            sendEmptyMessageDelayed(0, 60 * 1000);
        }
    }

    public interface BindListener {
        void sendVerifyCode(String mobile, VerifyCode verifyCode);

        void bindMobile(String mobile, String verity, Bind bind);

        void cancel(boolean isForce);
    }

    public interface VerifyCode {
        void finish();
    }

    public interface Bind {
        void finish();
    }
}
