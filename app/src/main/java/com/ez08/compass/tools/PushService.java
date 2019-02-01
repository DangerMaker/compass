package com.ez08.compass.tools;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;

import com.ez08.compass.R;
import com.ez08.compass.entity.PushMessageEntity;
import com.ez08.compass.net.NetInterface;
import com.ez08.support.net.EzMessage;
import com.ez08.support.net.EzNet;
import com.ez08.support.net.NetResponseHandler;
import com.ez08.support.net.NetResponseHandler2;
import com.ez08.support.util.EzValue;
import com.ez08.tools.IntentTools;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class PushService extends Service {
    // 推送消息
    public static final String ACTION_PUSH_INFO = "ez08.push.info";

    private JSONArray array = null;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private final static int WHAT_PUSH_RESULT = 20;


    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        IntentFilter pushfilter = new IntentFilter();
        pushfilter.addAction(ACTION_PUSH_INFO);
        EzNet.regMessageHandler(pushInfoReceiver, pushfilter);

        try {
            NetInterface.getPushList(mHandler, 107);
        }catch (Exception e){

        }

        return START_REDELIVER_INTENT;
    }

    NetResponseHandler2 mHandler = new NetResponseHandler2() {
        @Override
        public void receive(int arg0, boolean arg1, Intent arg2) {
            if (arg2 != null && arg0 == 107) {
                EzValue values = IntentTools.safeGetEzValueFromIntent(
                        arg2, "list");
                if (values != null) {
                    String bc = values.description();
                    EzMessage[] msges = values.getMessages();
                    if (msges != null && msges.length > 0) {
                        for (int i = 0; i < msges.length; i++) {
                            pushInfoReceiver.receive(msges[i]);
                        }
                    }
                }
            }
        }
    };

    /**
     * 推送消息处理
     */

    String pushid;//推送消息id
    String title;//推送标题
    String description;//推送描述
    String imgurl;//图片地址
    String uri;//跳转uri：跳转到某个课堂(gotoLive:10001)；跳转到某条资讯(gotoInfo:300219)；跳转到某个自选股(gotoStock:sh600800)；跳转到分享(share:url)；跳转到外部url (gotoUrl:url)；不跳转(cancel)
    String pushtype;//推送分类
    String usertype;//用户类型
    String time;//推送时间
    String starttime;// 推送开始时间
    String endtime;//推送结束时间
    String receivertime;//接收到消息时间
    String burnflg;
    int imgpos;// 推送开始时间

    private NetResponseHandler pushInfoReceiver = new NetResponseHandler() {
        @Override
        public void receive(EzMessage ezMsg) {
            if(ezMsg == null){
                return;
            }
            String aa = ezMsg.description();
//            if (ezMsg.getName() != null && ezMsg.getName().equals("msg.push.info")) {
//                pushid = ezMsg.getKVData("publisherid").getStringWithDefault("");
//                title = ezMsg.getKVData("title").getStringWithDefault("");//推送标题
//                description = ezMsg.getKVData("description").getStringWithDefault("");//推送描述
//                imgurl = ezMsg.getKVData("imgurl").getStringWithDefault("");//图片地址
//                uri = ezMsg.getKVData("uri").getStringWithDefault("");//跳转uri：跳转到某个课堂(gotoLive:10001)；跳转到某条资讯(gotoInfo:300219)；跳转到某个自选股(gotoStock:sh600800)；跳转到分享(share:url)；跳转到外部url (gotoUrl:url)；不跳转(cancel)
//                pushtype = ezMsg.getKVData("pushtype").getStringWithDefault("");//推送分类
//                usertype = ezMsg.getKVData("usertype").getStringWithDefault("");//用户类型
//                time = ezMsg.getKVData("time").getStringWithDefault("");//推送时间
//                starttime = ezMsg.getKVData("starttime").getStringWithDefault("");// 推送开始时间
//                endtime = ezMsg.getKVData("endtime").getStringWithDefault("");//推送结束时间
//                receivertime = new SimpleDateFormat("MM-dd").format(new Date());//接收到消息时间
//                burnflg = ezMsg.getKVData("burnfla").getStringWithDefault("0");
//                imgpos = ezMsg.getKVData("imgpos").getInt32();// 推送开始时间
//            } else {
                Intent intent = IntentTools.messageToIntent(ezMsg);
                if (intent == null) {
                    return;
                }
                pushid = intent.getStringExtra("pushid");//推送消息id
                title = intent.getStringExtra("title");//推送标题
                description = intent.getStringExtra("description");//推送描述
                imgurl = intent.getStringExtra("imgurl");//图片地址
                uri = intent.getStringExtra("uri");//跳转uri：跳转到某个课堂(gotoLive:10001)；跳转到某条资讯(gotoInfo:300219)；跳转到某个自选股(gotoStock:sh600800)；跳转到分享(share:url)；跳转到外部url (gotoUrl:url)；不跳转(cancel)
                pushtype = intent.getStringExtra("pushtype");//推送分类
                usertype = intent.getStringExtra("usertype");//用户类型
                time = intent.getStringExtra("time");//推送时间
                starttime = intent.getStringExtra("starttime");// 推送开始时间
                endtime = intent.getStringExtra("endtime");//推送结束时间
                receivertime = new SimpleDateFormat("MM-dd").format(new Date());//接收到消息时间
                burnflg = intent.getStringExtra("burnflg");
                imgpos = intent.getIntExtra("imgpos", -1);// 推送开始时间
//            }

            System.out.println("burnflg=========================================" + burnflg);
            PushMessageEntity pushMessage = new PushMessageEntity();
            pushMessage.setPushid(pushid);
            pushMessage.setTitle(title);
            pushMessage.setDescription(description);
            pushMessage.setImgurl(imgurl);
            pushMessage.setImgpos(imgpos);
            if (!TextUtils.isEmpty(uri) && uri.contains("gotourl")) {
                String sUri = "";
                if (uri.contains("?")) {
                    sUri = "&" + UtilTools.getPushUrlDate(PushService.this);
                } else {
                    sUri = "?" + UtilTools.getPushUrlDate(PushService.this);
                }
                uri = uri + sUri;
            }
            pushMessage.setUri(uri);
            pushMessage.setPushtype(pushtype);
            pushMessage.setUsertype(usertype);
            pushMessage.setTime(time);
            pushMessage.setStarttime(starttime);
            pushMessage.setEndtime(endtime);
            pushMessage.setReceivertime(receivertime);
            pushMessage.setIshasfind(false);
            //如果非阅后即焚状态，将该消息保存到本地（只保存最近10条）
            if (burnflg.equals("0")) {
                savePushInfoToLocal(pushMessage);
            }
            //通知后台已成功接收消息
            NetInterface.pushResultNotify(mHandler, WHAT_PUSH_RESULT, pushMessage.getPushid(), 1);//通知后台已收到通知

            if (pushtype.equals("statusbar")) {// 状态栏推送
                Long curtime = Long.parseLong(new SimpleDateFormat(
                        "yyyyMMddHHmmss").format(new Date())) + 1000;
                if (curtime >= Long.parseLong(starttime)
                        && curtime <= Long.parseLong(endtime)) {
                    //HEHE
//                    if (MainActivity.IS_ALIVE && !MainActivity.canShow && !ClassRoomActivity.istalk && !WebActivity.isVideo) { // 程序运行状态下，判断是否在前台，前台则弹出提示框
//                        if (BaseActivity.IS_ONRESUME
//                                || MainActivity.IS_ONRESUME || StockVerticalHeader.IS_ONRESUME) {
//                            Intent homeIntent = new Intent();
//                            if (!TextUtils.isEmpty(uri) && uri.contains("gotostock")) {
//                                pushMessage.setStockCode(uri.substring(10));
//                            }
//                            homeIntent.putExtra("pushentity", pushMessage);
//                            homeIntent.setAction(BaseActivity.PUSH_DIALOG);
//                            sendBroadcast(homeIntent);
//                            return;
//                        }
//                    }

                    NotificationManager nm = (NotificationManager) PushService.this
                            .getSystemService(Context.NOTIFICATION_SERVICE);
//					NotificationCompat.Builder builder = new NotificationCompat.Builder(
//							PushService.this).setSmallIcon(R.drawable.logo_)
//							.setWhen(System.currentTimeMillis())
//							.setAutoCancel(true).setContentTitle(title)
//							.setContentText(description).setTicker("有新通知");
                    NotificationCompat.Builder builder = new NotificationCompat.Builder(
                            PushService.this).setSmallIcon(R.drawable.logo_)
                            .setWhen(System.currentTimeMillis())
                            .setAutoCancel(true).setContentTitle(title)
                            .setContentText("").setTicker("有新通知");
                    builder.setDefaults(Notification.DEFAULT_SOUND);

                    Intent pushIntent = new Intent();
                    pushIntent.setClassName("com.ez08.compass",
                            "com.ez08.compass.activity.HandleMessageActivity");
                    pushIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                    if (!TextUtils.isEmpty(uri) && uri.contains("gotourl")) {
                        pushMessage.setUrl(uri.substring(8));
                        pushIntent.setAction("gotourl");
                    } else if (!TextUtils.isEmpty(uri) && uri.contains("gotoinfo")) {
                        pushIntent.setAction("gotoinfo");
                        pushMessage.setUrl("http://www.compass.cn/app/news.php?company=compass&newsid="
                                + uri.substring(9)
                                + "&"
                                + UtilTools.getDate(PushService.this));
                        pushMessage.setShareUrl("http://www.compass.cn/app/news.php?company=compass&newsid="
                                + uri.substring(9));

                    } else if (!TextUtils.isEmpty(uri) && uri.contains("gotovideo")) {
                        pushIntent.setAction("gotovideo");
                        pushMessage.setUrl("http://www.compass.cn/app/video.php?company=compass&newsid="
                                + uri.substring(10)
                                + "&"
                                + UtilTools.getDate(PushService.this));
                        pushMessage.setShareUrl("http://www.compass.cn/app/video.php?company=compass&newsid="
                                + uri.substring(10));
                    } else if (!TextUtils.isEmpty(uri) && uri.contains("gotostock")) {
                        String stockcode = uri.substring(10);
                        pushIntent.setAction("gotostock");
                        pushMessage.setStockCode(stockcode);
                    } else if (!TextUtils.isEmpty(uri) && uri.contains("gotolive")) {
                        pushIntent.setAction("gotolive");
                    } else if (!TextUtils.isEmpty(uri) && uri.contains("share")) {
                        pushIntent.setAction("gotoshare");
                        pushMessage.setUrl(uri.substring(6));
                    } else if (uri.contains("cancel")) {
                        pushIntent.setAction("gotocancel");
                    }
                    pushIntent.putExtra("pushentity", pushMessage);
                    PendingIntent pendingIntent = PendingIntent
                            .getActivity(PushService.this,
                                    Integer.parseInt(pushid),
                                    pushIntent,
                                    PendingIntent.FLAG_UPDATE_CURRENT);
                    builder.setContentIntent(pendingIntent);
                    nm.notify(Integer.parseInt(pushid), builder.build());
                }
            } else if (pushtype.equals("homepage")) {// 首页推送
                //HEHE
//                if (MainActivity.IS_ALIVE) { // 程序运行状态下，判断是否在前台，前台则弹出提示框
//                    if ((BaseActivity.IS_ONRESUME || MainActivity.IS_ONRESUME || StockVerticalHeader.IS_ONRESUME) && !MainActivity.canShow && !ClassRoomActivity.istalk && !WebActivity.isVideo) {
//                        Intent homeIntent = new Intent();
//                        if (!TextUtils.isEmpty(uri) && uri.contains("gotostock")) {
//                            pushMessage.setStockCode(uri.substring(10));
//                        }
//                        homeIntent.putExtra("pushentity", pushMessage);
//                        homeIntent.setAction(BaseActivity.PUSH_DIALOG);
//                        sendBroadcast(homeIntent);
//                    }
//                } else { // 程序未运行状态下,保存到本地
//                    SharedPreferences pushInfoPreferences = getSharedPreferences(
//                            "homepageInfo", 0);
//                    Editor editor1 = pushInfoPreferences
//                            .edit();
//                    editor1.putBoolean("ifShowNotification", true);
//                    editor1.putString("pushtype", pushtype);
//                    editor1.putString("uri", uri);
//                    editor1.putString("pushid", pushid);
//                    editor1.putString("title", title);
//                    editor1.putString("description", description);
//                    editor1.putString("imgurl", imgurl);
//                    editor1.putString("usertype", usertype);
//                    editor1.putString("time", time);
//                    editor1.putString("starttime", starttime);
//                    editor1.putString("endtime", endtime);
//                    if (!TextUtils.isEmpty(uri) && uri.contains("gotostock")) {
//                        editor1.putString("stockcode", uri.substring(10));
//                    }
//                    editor1.commit();
//                }
            }
        }

        private void savePushInfoToLocal(PushMessageEntity pushMessage) {
            SharedPreferences preferences = AppUtils
                    .getSharedPrefCerences(PushService.this);
            //取保存在本地的list的值
            String str = preferences.getString("list", "");
            if (TextUtils.isEmpty(str)) {
                array = new JSONArray();
            } else {
                try {
                    array = new JSONArray(str);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            JSONObject jsonObject = new JSONObject();
            JSONArray arrayB = null;
            try {
                jsonObject.put("pushid", pushMessage.getPushid());
                jsonObject.put("title", pushMessage.getTitle());
                jsonObject.put("description", pushMessage.getDescription());
                jsonObject.put("imgurl", pushMessage.getImgurl());
                jsonObject.put("uri", pushMessage.getUri());
                jsonObject.put("pushtype", pushMessage.getPushtype());
                jsonObject.put("usertype", pushMessage.getUsertype());
                jsonObject.put("time", pushMessage.getTime());
                jsonObject.put("starttime", pushMessage.getStarttime());
                jsonObject.put("endtime", pushMessage.getEndtime());
                jsonObject.put("ifhasfind", pushMessage.ishasfind());
                jsonObject.put("receivertime",
                        pushMessage.getReceivertime());

                if (array.length() < 10) {
                    array.put(jsonObject);
                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        array.remove(0);
                        array.put(jsonObject);
                    } else {
                        arrayB = new JSONArray();
                        for (int m = 1; m < array.length(); m++) {
                            arrayB.put(array.get(m));
                        }
                        arrayB.put(jsonObject);
                    }

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            Editor editor = preferences.edit();
            if (arrayB != null) {
                editor.putString("list", arrayB.toString());
            } else {
                editor.putString("list", array.toString());
            }
            editor.commit();

            //发送数据变化广播
            Intent intentReceiver = new Intent();
            intentReceiver.setAction("dataChange");
            sendBroadcast(intentReceiver);
        }
    };
}
