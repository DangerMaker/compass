package com.ez08.compass.ui.kefu;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.thinkive.framework.util.ScreenUtil;
import com.ez08.compass.CompassApp;
import com.ez08.compass.R;
import com.ez08.compass.database.IMDBHelper;
import com.ez08.compass.entity.PushMessageEntity;
import com.ez08.compass.net.H5Interface;
import com.ez08.compass.net.NetInterface;
import com.ez08.compass.tools.AuthTool;
import com.ez08.compass.tools.FileUtils;
import com.ez08.compass.tools.MessageService;
import com.ez08.compass.tools.PermissionHelper;
import com.ez08.compass.tools.UtilTools;
import com.ez08.compass.ui.BaseFragment;
import com.ez08.compass.ui.WebActivity;
import com.ez08.compass.ui.personal.MessageCenterActivity;
import com.ez08.compass.ui.personal.MyCollectActivity;
import com.ez08.compass.ui.view.TouchLayout;
import com.ez08.compass.userauth.AuthUserInfo;
import com.ez08.support.net.NetResponseHandler2;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class MyKefuFragment extends BaseFragment implements OnClickListener {

    private TextView mKefuName;
    private TextView personTv;
    private TextView mKefuTalkTv; //对话内容
    private TextView mTimeTv; //last对话时间
    private ImageView mKefuUnreadDot; //未读提示
    private ImageView mKefuImage; //未读dot依附图标
    private TextView mMessageTv; //消息未读count
    private RelativeLayout mMessageGroup; //未读消息父控件
    private TextView mKefuNameTv; //(***)的专属客服
    private TextView lEndify; // 确认验证码

    private RelativeLayout mEvu; //评价弹窗
    private RelativeLayout mTitleBar;
    private RelativeLayout mTalkLayout;

    RelativeLayout kefu_block1;
    RelativeLayout kefu_block2;
    RelativeLayout kefu_block3;
    RelativeLayout kefu_block4;
    RelativeLayout kefu_block5;
    RelativeLayout kefu_block6;
    RelativeLayout kefu_block7;
    RelativeLayout kefu_block8;
    LinearLayout kefu_frag1;
    LinearLayout kefu_frag2;

    private final int KEFU_RATING = 101;
    private final int GET_YC_STATUS = 102;
    private final int CREATE_YC_WORKFLOW = 103;
    private final int GET_KH_SEND_CODE = 104;   //发短信开户
    private final int GET_KH_VERIFY_CODE = 105; //验证短信接口

    private IMDBHelper helper;
    private Cursor mCursor;
    private SharedPreferences mySharedPreferences;
    private SharedPreferences dataSharedPreferences;

    private final int WHAT_VERYCODE_TIMER = 1003; // 验证码时限
    private long mCurrentTime = 60 * 1000;

    private String kaihuMn;
    private String kaihuMd;
    private String kaihuMobile;
    private String kefuQq;
    private String mPhone;
    private String uid;

    private PermissionHelper permissionHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View view = View.inflate(getActivity(), R.layout.fragment_kefu,
                null);
        kefu_block1 = (RelativeLayout) view.findViewById(R.id.kefu_fram_1);
        kefu_block2 = (RelativeLayout) view.findViewById(R.id.kefu_fram_2);
        kefu_block3 = (RelativeLayout) view.findViewById(R.id.kefu_fram_3);
        kefu_block4 = (RelativeLayout) view.findViewById(R.id.kefu_fram_4);
        kefu_block5 = (RelativeLayout) view.findViewById(R.id.kefu_fram_5);
        kefu_block6 = (RelativeLayout) view.findViewById(R.id.kefu_fram_6);
        kefu_block7 = (RelativeLayout) view.findViewById(R.id.kefu_fram_7);
        kefu_block8 = (RelativeLayout) view.findViewById(R.id.kefu_fram_8);
        kefu_block1.setOnClickListener(this);
        kefu_block2.setOnClickListener(this);
        kefu_block3.setOnClickListener(this);
        kefu_block4.setOnClickListener(this);
        kefu_block5.setOnClickListener(this);
        kefu_block6.setOnClickListener(this);
        kefu_block7.setOnClickListener(this);
        kefu_block8.setOnClickListener(this);

        kefu_frag1 = (LinearLayout) view.findViewById(R.id.kefu_frag1);
        kefu_frag2 = (LinearLayout) view.findViewById(R.id.kefu_frag2);

        RelativeLayout.LayoutParams fragParams1 = (RelativeLayout.LayoutParams) kefu_frag1.getLayoutParams();
        fragParams1.height = (int)((ScreenUtil.getScreenWidth(getActivity()) - ScreenUtil.dpToPx(getActivity(),20 * 5))/4);

        RelativeLayout.LayoutParams fragParams2 = (RelativeLayout.LayoutParams) kefu_frag2.getLayoutParams();
        fragParams2.height = fragParams1.height;

        if(AuthTool.downloadPC()){
            kefu_block7.setVisibility(View.VISIBLE);
        }else{
            kefu_block7.setVisibility(View.INVISIBLE);
        }

        if(AuthTool.feature()){
            kefu_block8.setVisibility(View.VISIBLE);
        }else{
            kefu_block8.setVisibility(View.INVISIBLE);
        }

        mMessageGroup = (RelativeLayout) view.findViewById(R.id.kefu_msg_count_group);
        mMessageTv = (TextView) view.findViewById(R.id.kefu_msg_count);
        mTalkLayout = (RelativeLayout) view.findViewById(R.id.kefu_talk);
        mTimeTv = (TextView) view.findViewById(R.id.kefu_fragment_time);
        mTalkLayout.setOnClickListener(this);
        mKefuImage = (ImageView) view.findViewById(R.id.kefu_talk_iv);
        mKefuNameTv = (TextView) view.findViewById(R.id.kefu_user_name);
        personTv = (TextView) view.findViewById(R.id.person_extrance);
        personTv.setOnClickListener(this);

        if (CompassApp.GLOBAL.THEME_STYLE == 1) {
            ((ImageView)(view.findViewById(R.id.kefu_fram_1_img))).setColorFilter(Color.parseColor("#c2c0c2"));
            ((ImageView)(view.findViewById(R.id.kefu_fram_2_img))).setColorFilter(Color.parseColor("#c2c0c2"));
            ((ImageView)(view.findViewById(R.id.kefu_fram_3_img))).setColorFilter(Color.parseColor("#c2c0c2"));
            ((ImageView)(view.findViewById(R.id.kefu_fram_4_img))).setColorFilter(Color.parseColor("#c2c0c2"));
            ((ImageView)(view.findViewById(R.id.kefu_fram_5_img))).setColorFilter(Color.parseColor("#c2c0c2"));
            ((ImageView)(view.findViewById(R.id.kefu_fram_6_img))).setColorFilter(Color.parseColor("#c2c0c2"));
            ((ImageView)(view.findViewById(R.id.kefu_fram_7_img))).setColorFilter(Color.parseColor("#c2c0c2"));
            ((ImageView)(view.findViewById(R.id.kefu_fram_8_img))).setColorFilter(Color.parseColor("#c2c0c2"));
            mKefuImage.setColorFilter(Color.parseColor("#c2c0c2"));
        }

        mySharedPreferences = getActivity()
                .getSharedPreferences("kefu", Activity.MODE_PRIVATE);
        dataSharedPreferences = getActivity().getSharedPreferences(AuthUserInfo.getMyCid() + "kefu",
                Activity.MODE_PRIVATE);
        String name = mySharedPreferences.getString("name", "");
        String praiserate = mySharedPreferences.getString("praiserate", "0");
        int rate = (int) Double.parseDouble(praiserate);
        String level = mySharedPreferences.getString("level", "0");
        uid = mySharedPreferences.getString("uid", "0");
        mPhone = mySharedPreferences.getString("phone", "0");
        kefuQq = mySharedPreferences.getString("qq","0");
        if (TextUtils.isEmpty(AuthUserInfo.getMyRealName())) {
            mKefuNameTv.setText(AuthUserInfo.getMyCid() + "的专属客服");
        } else {
            mKefuNameTv.setText(AuthUserInfo.getMyRealName() + "(" + AuthUserInfo.getMyCid() + ")" + "的专属客服");
        }

        TextView lKefuLevel = (TextView) view.findViewById(R.id.kefu_level);
        mKefuName = (TextView) view.findViewById(R.id.kefu_brief);
        mEvu = (RelativeLayout) view.findViewById(R.id.kefu_evu);
        mTitleBar = (RelativeLayout) view.findViewById(R.id.kefu_title_bar);
        int lLevel = 0;
        try {
            lLevel = Integer.parseInt(level);
        } catch (NumberFormatException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        try {
            lLevel = Integer.parseInt(level);
        } catch (NumberFormatException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if (lLevel == 50) {
            lKefuLevel.setText("客服专员");
        } else if (lLevel == 100) {
            lKefuLevel.setText("中级客服");
        } else if (lLevel == 150) {
            lKefuLevel.setText("高级客服");
        }
        TextView lKefuRate = (TextView) view.findViewById(R.id.kefu_rate);
        lKefuRate.setText("好评率 :" + rate + "%");

        //--------------
        IntentFilter filter2 = new IntentFilter();
        filter2.addAction(MessageService.ACTION_NOTIFY_KEFU);
        filter2.addAction("dataChange");
        getActivity().registerReceiver(dataChangeReceiver, filter2);

        mKefuName.setText(name + "(" + uid + ")");
        TouchLayout kefu_home = (TouchLayout) view.findViewById(R.id.kefu_home);
        kefu_home.setView(mTitleBar, mEvu);
        LinearLayout lKefuRate1 = (LinearLayout) view
                .findViewById(R.id.kefu_rate1);
        LinearLayout lKefuRate2 = (LinearLayout) view
                .findViewById(R.id.kefu_rate2);
        LinearLayout lKefuRate3 = (LinearLayout) view
                .findViewById(R.id.kefu_rate3);
        lKefuRate1.setOnClickListener(this);
        lKefuRate2.setOnClickListener(this);
        lKefuRate3.setOnClickListener(this);

        mKefuUnreadDot = (ImageView) view.findViewById(R.id.kefu_unread_dot);
        mKefuTalkTv = (TextView) view.findViewById(R.id.kefu_talk_tv);
        helper = IMDBHelper.getInstance(getActivity());
        refreshTalk();

        return view;
    }

    @Override
    public void onDestroyView() {
        mContext.unregisterReceiver(dataChangeReceiver);
        super.onDestroyView();

    }

    private BroadcastReceiver dataChangeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            refreshTalk();
        }
    };

    public interface CallBack {
        public void hasRedDot(boolean dot);
    }

    public CallBack mCallBack;

    public void setCallBack(CallBack callBack) {
        mCallBack = callBack;
    }

    public void refreshTalk() {
        if (mKefuTalkTv == null) {
            return;
        }
        mCursor = CreateCursor();
        mCursor.moveToLast();
        String lastText = "";
        String imageurl = "";
        String lastTime = "";

        try {
            lastText = mCursor.getString(mCursor.getColumnIndex("msg"));
            imageurl = mCursor.getString(mCursor.getColumnIndex("imageurl"));
            lastTime = mCursor.getString(mCursor.getColumnIndex("localTime"));
        } catch (Exception e) {
            lastText = "";
            imageurl = "";
            lastTime = "";
        }
        String lLastData = dataSharedPreferences.getString("lastData", "");
        if (TextUtils.equals(lastText + lastTime + imageurl, lLastData)) {
            mKefuUnreadDot.setVisibility(View.GONE);
        } else {
            mKefuUnreadDot.setVisibility(View.VISIBLE);
        }
        if (!TextUtils.isEmpty(lastText)) {
            mKefuTalkTv.setText(lastText);
        } else if (!TextUtils.isEmpty(imageurl)) {
            mKefuTalkTv.setText("[图片]");
        } else {
            mKefuTalkTv.setText("");
        }
        if (!TextUtils.isEmpty(lastTime) && lastTime.length() == 14) {
            String time[] = {lastTime.substring(4), lastTime.substring(4, 6), lastTime.substring(6, 8), lastTime.substring(8, 10), lastTime.substring(10, 12)};
            mTimeTv.setText(time[1] + "-" + time[2] + " " + time[3] + ":" + time[4]);
        } else {
            mTimeTv.setText("");
        }
        //------------------------
        List<PushMessageEntity> list = initMessageData();
        int count = 0;
        for (int i = 0; i < list.size(); i++) {
            PushMessageEntity pushMessage = list.get(i);
            if (!pushMessage.ishasfind()) {
                count++;
            }
        }
        if (count == 0) {
            mMessageGroup.setVisibility(View.GONE);
        } else {
            mMessageGroup.setVisibility(View.VISIBLE);
            mMessageTv.setText(count + "");
        }

        if (mMessageGroup.getVisibility() == View.VISIBLE || mKefuUnreadDot.getVisibility() == View.VISIBLE) {
            if (mCallBack != null) {
                mCallBack.hasRedDot(true);
            }
        } else {
            if (mCallBack != null) {
                mCallBack.hasRedDot(false);
            }
        }
    }

    private List<PushMessageEntity> initMessageData() {
        JSONArray array = null;
        List list = new ArrayList<PushMessageEntity>();
        SharedPreferences preferences = getActivity().getSharedPreferences("pushlist&" + AuthUserInfo.getMyCid(),
                Activity.MODE_PRIVATE);
        String str = preferences.getString("list", "");
        if (TextUtils.isEmpty(str)) {
            array = new JSONArray();
        } else {
            try {
                array = new JSONArray(str);
                if (array != null && array.length() > 0) {
                    for (int i = array.length() - 1; i >= 0; i--) {
                        PushMessageEntity pushMessage = new PushMessageEntity();
                        JSONObject jsonObject = (JSONObject) array.get(i);
                        pushMessage.setPushid(jsonObject.getString("pushid"));
                        pushMessage.setTitle(jsonObject.getString("title"));
                        pushMessage.setDescription(jsonObject.getString("description"));
                        pushMessage.setImgurl(jsonObject.getString("imgurl"));
                        pushMessage.setUri(jsonObject.getString("uri"));
                        pushMessage.setPushtype(jsonObject.getString("pushtype"));
                        pushMessage.setUsertype(jsonObject.getString("usertype"));
                        pushMessage.setTime(jsonObject.getString("time"));
                        pushMessage.setStarttime(jsonObject.getString("starttime"));
                        pushMessage.setEndtime(jsonObject.getString("endtime"));
                        pushMessage.setIshasfind(jsonObject.getBoolean("ifhasfind"));
                        pushMessage.setReceivertime(jsonObject.getString("receivertime"));
                        list.add(pushMessage);
                    }
                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return list;
    }

    private Cursor CreateCursor() {
        return helper.getConversationTableCursor(CompassApp.GLOBAL.KEFU_ID);
    }

    private void setTextLimit(int what, final EditText et) {

        switch (what) {
            case WHAT_REQUEST_SET:
                et.setFilters(new InputFilter[]{new InputFilter.LengthFilter(40)});
                break;
            default:
                break;
        }
    }

    private static final int WHAT_REQUEST_SET = 1001;
    AlertDialog dialog = null;

    private boolean setrateGone() {
        if (mEvu.getVisibility() == View.VISIBLE) {
            mEvu.setVisibility(View.GONE);
            return true;
        }
        return false;
    }

    private final int PERMISSION_REQUEST = 1;


    private void doPhone() {
        boolean hasPhonePermission = permissionHelper.checkPermission(Manifest.permission.CALL_PHONE, getActivity());
        if (!hasPhonePermission) {
            String[] permissions = new String[]{Manifest.permission.CALL_PHONE};
            permissionHelper.registerPermission(getActivity(), permissions, PERMISSION_REQUEST);
            return;
        }

        new AlertDialog.Builder(getActivity()).setTitle("拨打电话")
                .setMessage("是否拨打" + mPhone + "?")
                .setNegativeButton("取消", null)
                .setPositiveButton("确定", new AlertDialog.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub

                        Intent intent = new Intent(Intent.ACTION_CALL, Uri
                                .parse("tel:" + mPhone));
                        startActivity(intent);
                        CompassApp.addStatis(CompassApp.GLOBAL.mgr.KEFU_MAIN, "3", "",
                                System.currentTimeMillis());
                    }
                }).show();

    }

    private void doPhone2() {
        boolean hasPhonePermission = permissionHelper.checkPermission(Manifest.permission.CALL_PHONE, getActivity());
        if (!hasPhonePermission) {
            String[] permissions = new String[]{Manifest.permission.CALL_PHONE};
            permissionHelper.registerPermission(getActivity(), permissions, PERMISSION_REQUEST);
            return;
        }

        new AlertDialog.Builder(getActivity()).setTitle("投诉")
                .setMessage("是否拨打" + " 010-82550620 进行投诉" + "?")
                .setNegativeButton("取消", null)
                .setPositiveButton("确定", new AlertDialog.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        Intent intent = new Intent(Intent.ACTION_CALL, Uri
                                .parse("tel:" + "01082550620"));
                        startActivity(intent);
                        CompassApp.addStatis(CompassApp.GLOBAL.mgr.KEFU_MAIN, "5", "",
                                System.currentTimeMillis());
                    }
                }).show();

    }


    @Override
    public void onClick(View arg0) {
        // TODO Auto-generated method stub
        switch (arg0.getId()) {
            case R.id.kefu_talk:
                if (!setrateGone()) {
                    Intent i = new Intent(getActivity(), KefuTalkActivity.class);
                    startActivity(i);
                    CompassApp.addStatis(CompassApp.GLOBAL.mgr.KEFU_MAIN, "2", "",
                            System.currentTimeMillis());
                }
                break;
            case R.id.person_extrance:
                if (!setrateGone()) {
                    Intent per = new Intent(getActivity(), PersonActivity.class);
                    startActivity(per);
                    CompassApp.addStatis(CompassApp.GLOBAL.mgr.KEFU_MAIN, "1", "",
                            System.currentTimeMillis());
                }
                break;
            case R.id.kefu_fram_1:
                if (setrateGone()) {
                    return;
                }

                if (TextUtils.isEmpty(mPhone)) {
                    Toast.makeText(getActivity(), "拨打失败", Toast.LENGTH_LONG).show();
                    return;
                }

                permissionHelper = PermissionHelper.initPermissionHelper();

                doPhone();
//                new AlertDialog.Builder(getActivity()).setTitle("拨打电话")
//                        .setMessage("是否拨打" + mPhone + "?")
//                        .setNegativeButton("取消", null)
//                        .setPositiveButton("确定", new AlertDialog.OnClickListener() {
//
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                // TODO Auto-generated method stub
//
//                                Intent intent = new Intent(Intent.ACTION_CALL, Uri
//                                        .parse("tel:" + mPhone));
//                                startActivity(intent);
//                                CompassApp.addStatis(CompassApp.mgr.KEFU_MAIN, "3", "",
//                                        System.currentTimeMillis());
//                            }
//                        }).show();

                break;
            case R.id.kefu_fram_2:

//                showKaiHuDialog();

                if (setrateGone()) {
                    return;
                }
                if (!isNetworkAvailble()) {
                    return;
                }
                showBusyDialog();
                NetInterface.getYcStatus(mHandler, GET_YC_STATUS);
                break;
            case R.id.kefu_fram_3:
                if (setrateGone()) {
                    return;
                }
                permissionHelper = PermissionHelper.initPermissionHelper();

                doPhone2();
//                new AlertDialog.Builder(getActivity()).setTitle("投诉")
//                        .setMessage("是否拨打" + " 010-82550620 进行投诉" + "?")
//                        .setNegativeButton("取消", null)
//                        .setPositiveButton("确定", new AlertDialog.OnClickListener() {
//
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                // TODO Auto-generated method stub
//                                Intent intent = new Intent(Intent.ACTION_CALL, Uri
//                                        .parse("tel:" + "01082550620"));
//                                startActivity(intent);
//                                CompassApp.addStatis(CompassApp.mgr.KEFU_MAIN, "5", "",
//                                        System.currentTimeMillis());
//                            }
//                        }).show();
                break;
            case R.id.kefu_fram_4:
                if (mEvu.getVisibility() == View.VISIBLE) {
                    mEvu.setVisibility(View.GONE);
//                    mTitleBar.setBackgroundColor(backgroundColorNormal);
                    // mTitleBar.setVisibility(View.VISIBLE);
                } else {
                    mEvu.setVisibility(View.VISIBLE);
//                    mTitleBar.setBackgroundColor(backgroundColorPer);
                    // mTitleBar.setVisibility(View.GONE);
                }
                break;
            case R.id.kefu_fram_5:
                if (setrateGone()) {
                    return;
                }
                CompassApp.addStatis(CompassApp.GLOBAL.mgr.KEFU_MAIN, "7", "",
                        System.currentTimeMillis());
                Intent intent1 = new Intent(getActivity(), MessageCenterActivity.class);
                startActivity(intent1);
                break;
            case R.id.kefu_fram_6:
                if (setrateGone()) {
                    return;
                }
                CompassApp.addStatis(CompassApp.GLOBAL.mgr.KEFU_MAIN, "8", "",
                        System.currentTimeMillis());
                Intent intent11 = new Intent(getActivity(), MyCollectActivity.class);
                startActivity(intent11);
                break;
            case R.id.kefu_fram_7:
                Intent intentWeb6 = new Intent(getActivity(), WebActivity.class);
                intentWeb6.putExtra("title", "下载PC");
                intentWeb6.putExtra("url", H5Interface.Download.intro(getActivity()));
                intentWeb6.putExtra("action", H5Interface.Download.FUNC_XZPC);
                startActivity(intentWeb6);
                CompassApp.addStatis(CompassApp.GLOBAL.mgr.KEFU_MAIN, "9", "",
                        System.currentTimeMillis());
                break;
            case R.id.kefu_fram_8:
                Intent intentWeb7 = new Intent(getActivity(), WebActivity.class);
                intentWeb7.putExtra("title", "特色功能");
                intentWeb7.putExtra("url", H5Interface.Feature.intro(getActivity()));
                intentWeb7.putExtra("action", H5Interface.Feature.FUNC_TSGN);
                startActivity(intentWeb7);
                CompassApp.addStatis(CompassApp.GLOBAL.mgr.KEFU_MAIN, "10", "",
                        System.currentTimeMillis());
                break;
            case R.id.kefu_rate1:
                if (!isNetworkAvailble()) {
                    return;
                }
                CompassApp.addStatis(CompassApp.GLOBAL.mgr.KEFU_MAIN, "6", "level=0",
                        System.currentTimeMillis());
                mEvu.setVisibility(View.GONE);
                NetInterface
                        .requestServiceRating(mHandler, KEFU_RATING, uid, 1, "");
                break;
            case R.id.kefu_rate2:
                if (!isNetworkAvailble()) {
                    return;
                }
                mEvu.setVisibility(View.GONE);
                CompassApp.addStatis(CompassApp.GLOBAL.mgr.KEFU_MAIN, "6", "level=1",
                        System.currentTimeMillis());
                NetInterface.requestServiceRating(mHandler, KEFU_RATING, uid, 100,
                        "");
                break;
            case R.id.kefu_rate3:
                if (!isNetworkAvailble()) {
                    return;
                }
                mEvu.setVisibility(View.GONE);
                CompassApp.addStatis(CompassApp.GLOBAL.mgr.KEFU_MAIN, "6", "level=2",
                        System.currentTimeMillis());
                showSetDialog();
                break;
            default:
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
//            case PERMISSION_REQUEST:
//                String permission = permissions[0];
//                if (permission.equals(Manifest.permission.CALL_PHONE)) {
//                    int grantResult = grantResults[0];
//                    if (grantResult != PackageManager.PERMISSION_GRANTED) {
//                        Toast.makeText(getActivity(), "打电话权限未开启，无法打电话!", Toast.LENGTH_LONG).show();
//                    } else {
//                        doPhone();
//                    }
//                } else {
//                    Toast.makeText(getActivity(), "打电话权限未开启，无法打电话!", Toast.LENGTH_LONG).show();
//                }
//                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    public boolean isNetworkAvailble() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();
        if (info == null || !info.isAvailable()) {
            Toast.makeText(getActivity(), "没有可用网络", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void showSetDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("您的差评理由");
        View view = LayoutInflater.from(getActivity()).inflate(
                R.layout.rate_input_layout, null);
        final EditText etContent = (EditText) view
                .findViewById(R.id.et_content);
        setTextLimit(WHAT_REQUEST_SET, etContent);
        etContent.setSelection(etContent.getText().length());
        builder.setView(view);
        builder.setPositiveButton("取消", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                // TODO Auto-generated method stub
                if (TextUtils.isEmpty(etContent.getText().toString())) {
                    Toast.makeText(getActivity(), "请输入您的解决建议",
                            Toast.LENGTH_LONG).show();
                    return;
                }
                if (!isNetworkAvailble()) {
                    return;
                }
                NetInterface.requestServiceRating(mHandler, KEFU_RATING, uid,
                        200, etContent.getText().toString());
            }
        });
        dialog = builder.create();

        dialog.show();
        dialog.setOnDismissListener(new OnDismissListener() {

            @Override
            public void onDismiss(DialogInterface arg0) {
                // TODO Auto-generated method stub
                // dot3.setBackgroundResource(R.drawable.dot_n);
            }
        });
    }

    private void showKaiHuDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        TextView titleTv = new TextView(getActivity());
        titleTv.setText("将要使用您绑定的手机号码进行开户验证");
        int padd = UtilTools.dip2px(getActivity(), 10);
        titleTv.setPadding(padd, padd, padd, padd);
        titleTv.setGravity(Gravity.CENTER);
        titleTv.setTextColor(getResources().getColor(R.color.mediumblue));
        titleTv.setTextSize(19);

        View view = LayoutInflater.from(getActivity()).inflate(
                R.layout.kaihu_decide_layout, null);
        lEndify = (TextView) view.findViewById(R.id.kaihu_infy_code);
        final EditText verifyEdite = (EditText) view.findViewById(R.id.kaihu_verify_edite);

        TextView lKaihuMobileTv = (TextView) view.findViewById(R.id.kaihu_decide_mobile);

        String mobile = "";
        if (kaihuMobile.length() == 11) {
            String mobile0 = kaihuMobile.substring(0, 3);
            String mobile2 = kaihuMobile.substring(7, 11);
            mobile = mobile0 + "****" + mobile2;
        } else {
            mobile = "";
        }
        lKaihuMobileTv.setText(mobile);

        lEndify.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                NetInterface.getKHSendCode(mHandler, GET_KH_SEND_CODE, kaihuMobile);
            }
        });
        builder.setCustomTitle(titleTv);
        builder.setView(view);
        builder.setPositiveButton("前往开户", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                // TODO Auto-generated method stub

            }
        });
        dialog = builder.create();

        dialog.show();

        //重写“确定”（AlertDialog.BUTTON_POSITIVE），截取监听
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String verify = verifyEdite.getText().toString().trim();
                if (TextUtils.isEmpty(verify)) {
                    Toast.makeText(getActivity(), "请输入验证码", Toast.LENGTH_LONG).show();
                    return;
                }
                NetInterface.getKHVerifyCode(mHandler, GET_KH_VERIFY_CODE, kaihuMobile, verify);
                dialog.dismiss();
            }
        });
    }


    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case WHAT_VERYCODE_TIMER:
                    try {
                        mCurrentTime = mCurrentTime - 1000;
                        if (mCurrentTime <= 0) {
                            lEndify.setText("获取验证码");
                            lEndify.setClickable(true);
                            lEndify.setTextColor(getResources().getColor(R.color.mediumblue));
                            mCurrentTime = 60 * 1000;
                        } else {
                            handler.sendMessageDelayed(
                                    handler.obtainMessage(WHAT_VERYCODE_TIMER), 1000);
                            lEndify.setTextColor(getResources().getColor(R.color.lable_item_style));
                            int count = (int) (mCurrentTime / 1000);
                            lEndify.setText(count + "秒后重发");
                        }
                    } catch (IllegalStateException e) {

                    }

                    break;

                default:
                    break;
            }
        }

        ;
    };

    @SuppressLint("HandlerLeak")
    NetResponseHandler2 mHandler = new NetResponseHandler2() {
        @Override
        public void netConnectLost(int arg0) {
        }

        @Override
        public void timeout(int arg0) {
            dismissBusyDialog();
        }
        @Override
        public void receive(int arg0, boolean arg1, Intent intent) {
            // TODO Auto-generated method stub
            dismissBusyDialog();
            String msg = intent.getStringExtra("msg");
            switch (arg0) {
                case KEFU_RATING:
                    if (!TextUtils.isEmpty(msg)) {
                        if (!TextUtils.equals(msg, "请重新登录")) {
                            Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG)
                                    .show();
                        }
                    }
                    break;
                case GET_YC_STATUS:
                    if (intent != null) {
                        if (!intent.getBooleanExtra("result", false)) {
                            Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG)
                                    .show();
                        } else {
                            if (intent.getIntExtra("exist", 0) == 0) {
                                openState = true;
                                showifCreateYcWorkflow(msg,intent);
                            } else {
                                ClipboardManager cmb = (ClipboardManager) getActivity()
                                        .getSystemService(Context.CLIPBOARD_SERVICE);
                                cmb.setText(intent.getStringExtra("mobile"));
                                openState = false;
                                gotoOpenAccountMould(intent);

                            }
                        }
                    }

                    break;
                case CREATE_YC_WORKFLOW:
                    //一创库先不导入
//                    dismissBusyDialog();
//                    if (intent != null) {
//                        if (!intent.getBooleanExtra("result", false)) {
//                            Toast.makeText(getActivity(),
//                                    intent.getStringExtra("msg"), Toast.LENGTH_LONG)
//                                    .show();
//                        } else {
//                            Intent intentKaihu = new Intent();
//                            String color = CompassApp.GLOBAL.THEME_STYLE == 0 ? "FF0000" : "232226";
//                            String newUrl = "https://kh.fcsc.com/fcsc/acct4/index.html?or=ZNZ&color=" + color + "&mn=" + kaihuMn + "&md=" + kaihuMd + "&ocr=1";
//                            intentKaihu.putExtra("url", newUrl);
//                            intentKaihu.setClass(getActivity(), FirstCapitalOpenAccountActivity.class);
//                            startActivity(intentKaihu);
//                            CompassApp.addStatis(CompassApp.mgr.KEFU_MAIN, "4", "",
//                                    System.currentTimeMillis());
//                        }
//                    }
                    break;
                case GET_KH_SEND_CODE:
                    if (intent.getBooleanExtra("result", false)) {
                        Toast.makeText(getActivity(), "验证码已发送", Toast.LENGTH_LONG).show();
                        handler.sendMessageDelayed(
                                handler.obtainMessage(WHAT_VERYCODE_TIMER), 1000);
                        lEndify.setClickable(false);
                        lEndify.setTextColor(getResources().getColor(
                                R.color.lable_item_style));
                    } else {
                        Toast.makeText(getActivity(), "网络异常", Toast.LENGTH_LONG).show();
                    }
                    break;
                case GET_KH_VERIFY_CODE:
//                    if (intent.getBooleanExtra("result", false)) {
//                        if(!openState) {
//                            Intent intentKaihu = new Intent();
//                            String color = CompassApp.GLOBAL.THEME_STYLE == 0 ? "FF0000" : "232226";
//                            String newUrl = "https://kh.fcsc.com/fcsc/acct4/index.html?or=ZNZ&color=" + color + "&mn=" + kaihuMn + "&md=" + kaihuMd + "&ocr=1";
//                            intentKaihu.putExtra("url", newUrl);
//                            intentKaihu.setClass(getActivity(), FirstCapitalOpenAccountActivity.class);
//                            startActivity(intentKaihu);
//                            CompassApp.addStatis(CompassApp.GLOBAL.mgr.KEFU_MAIN, "4", "",
//                                    System.currentTimeMillis());
//                        }else{
//                            NetInterface.createYcWorkflow(mHandler,
//                                    CREATE_YC_WORKFLOW);
//                            showBusyDialog();
//                        }
//                    } else {
//                        if (TextUtils.isEmpty(msg)) {
//                            msg = "验证失败，请稍后重试";
//                        }
//                        Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
//                    }
                    break;
                default:
                    break;
            }
        }

        boolean openState = true;  //true first open

        private void load(String name) {

            File dir = getActivity().getDir("libs/armeabi", Context.MODE_PRIVATE);
            File soFile = new File(dir, name);
            FileUtils.assetToFile(getActivity(), name, soFile);

            try {
                System.load(soFile.getAbsolutePath());
            } catch (Exception e) {
            }
        }


        private void showifCreateYcWorkflow(String msg, final Intent intent) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("开户");
            builder.setMessage(msg);
            builder.setPositiveButton("取消",
                    new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
            builder.setNegativeButton("确定",
                    new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            ClipboardManager cmb = (ClipboardManager) getActivity()
                                    .getSystemService(Context.CLIPBOARD_SERVICE);
                            cmb.setText(intent.getStringExtra("mobile"));
                            gotoOpenAccountMould(intent);
                        }
                    });
            dialog = builder.create();

            dialog.show();
            dialog.setOnDismissListener(new OnDismissListener() {

                @Override
                public void onDismiss(DialogInterface arg0) {
                    // TODO Auto-generated method stub
                    // dot3.setBackgroundResource(R.drawable.dot_n);
                }
            });

        }

    };

    protected void dismissBusyDialog() {
        if (pDialog != null) {
            pDialog.dismiss();
        }
    }

    ProgressDialog pDialog;

    protected void showBusyDialog() {
        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("请稍候...");
        pDialog.setCancelable(true);
        pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        if (Thread.currentThread() == Looper.getMainLooper().getThread()) {
            pDialog.show();
        } else {
            getActivity().runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    pDialog.show();
                }
            });
        }
    }

    private void gotoOpenAccountMould(Intent arg) {
        kaihuMn = arg.getStringExtra("mobile_encoded");
        kaihuMd = arg.getStringExtra("mobile_encodedmd5");
        kaihuMobile = arg.getStringExtra("mobile");
        showKaiHuDialog();
    }
}
