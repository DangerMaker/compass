package com.ez08.support.net;


import com.ez08.tools.IntentTools;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;

public class EzNetRequest {


	public Handler mHandler;//回调接口，可以是Hanlder  NetResponseHandler 或 String，String是特种回调
	
	public int mEnp;//加密类型enctype
	
	public int mWhat;//回调参数
	
	public EzMessage reqMsg;
	public EzMessage resMsg;
	
	public long mTime;//创建时间
	public int mSn;
	public long mTimeOut;//超时类型，<=0表示标准超时
	public boolean mSendOK;//是否已经发送成功
	
	
	
	public static final int REQ_TYPE_NORMAL = 0;//普通数据包
	public static final int REQ_TYPE_HEARTBEAT = 1;//心跳数据包
	public static final int REQ_TYPE_HEARTBEAT_RESPONSE = 2;//心跳回应包
	public static final int REQ_TYPE_DATA_RESPONSE = 3;//数据收到确认包，这是mSn是所需要确认的包得sn
	public int reqType = 0;//请求类型
	public EzNetRequest(int type){
		reqType = type;
	}
	public EzNetRequest(EzMessage msg, Handler handler, int what, int enp,long timeout){
		reqMsg = msg;
		mHandler = handler;
		mWhat = what;
		mEnp = enp;
		mTimeOut = timeout;
		mTime = System.currentTimeMillis();
	}
	public boolean isTimeOut(){
//		long t = System.currentTimeMillis() - mTime;
//		if(mTimeOut == 0)
//			mTimeOut = NetManager.TIMEOUT;
//		if(t>mTimeOut)
//			return true;
		return false;
	}
	
	public static Intent getResponseIntentFromOSMessage(Message osMsg){
		EzMessage msg = getResponseMessageFromOSMessage(osMsg);
		return IntentTools.messageToIntent(msg);
	}
	
	public static EzMessage getResponseMessageFromOSMessage(Message osMsg){
		if(osMsg == null || osMsg.obj == null)
			return null;
		if(osMsg.obj instanceof EzNetRequest){
			return ((EzNetRequest)(osMsg.obj)).resMsg;
		}
		
		return null;
	}
	
}
