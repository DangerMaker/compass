package com.ez08.support.net;

import java.util.Hashtable;
import java.util.Set;
import java.util.Vector;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import com.ez08.support.util.EzKeyValue;
import com.ez08.support.util.EzValue;
import com.ez08.tools.EzLog;
import com.ez08.tools.IntentTools;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

public class EzNet {

    private static Hashtable<NetResponseHandler,IntentFilter> mMessageHandlerTable = 
    		new Hashtable<NetResponseHandler,IntentFilter>();

	private static final String tag = "EzNet";
	private static final boolean D = true;
	//回调的标准WHAT
	public static final int WHAT_MESSAGE_ARRIVED = 1;
	public static final int WHAT_SEND_OK = 0;
	public static final int WHAT_TIMEOUT = -1;
	public static final int WHAT_CANCEL = -2;
	public static final int WHAT_NET_DISCONNECT = -3;
//	public static final int WHAT_NET_FAIL_CONNECT = -4;
	
	public static final int NET_NOT_CONNECTED = -1;
	public static final int NET_NO_NETWORK = -2;
	
	/**
	 * 网络请求队列
	 */
	public static BlockingQueue<EzNetRequest> sendQueue = 
		new ArrayBlockingQueue<EzNetRequest>(100);
	
	private static Hashtable<Integer,EzNetRequest> SendTable =
		new Hashtable<Integer,EzNetRequest>();
	
	

	static void clearMessage(){
		synchronized(LOCK){
			sendQueue.clear();
			SendTable.clear(); 
		}
	}
	
	/**
	 * 检查是否有已经发送的数据包在等待请求结果
	 * @return
	 */
	public static boolean hasPakWaiting(){
		return (sendQueue.size()>0 || SendTable.size()>0);
	}
	
	/**
	 * 重新发送在等待请求的数据包
	 */
//	public static void resend(){
//		EzLog.w(D, tag, "网络重连成功，重新发送已经发送过但是没有响应的数据包");
//		Set<Integer> set = SendTable.keySet();
//		if(set == null || set.size()==0) return;
//		for(Integer sn:set){
//			EzNetRequest request = SendTable.get(sn);
//			if(request!=null){
//				if(request.isTimeOut()){
//					SendTable.remove(sn);
//					notifyRequest(request, WHAT_TIMEOUT);
//				}else{
//					sendQueue.offer(request);
//				}
//			}
//		}
//	}
	
	/**
	 * 登记所有需要监听网络消息的handler
	 * @param handler
	 * @param filter
	 */
	public static void regMessageHandler(NetResponseHandler handler, IntentFilter filter){
		if(handler == null)
			return;
		IntentFilter f = mMessageHandlerTable.get(handler);
		if(f!=null){
			mMessageHandlerTable.remove(handler);
		}
		if(filter == null){
			mMessageHandlerTable.remove(handler);
		}else{
			mMessageHandlerTable.put(handler, filter);
		}
		
	}
	public static void unregMessageHandler(NetResponseHandler handler){
		mMessageHandlerTable.remove(handler);
	}
	
	
	private static void noHandlerPakage(EzMessage message){
		if(message==null) return;
		String action = message.getKVData("action").getString();
		if(action==null)
			return;
		Set<NetResponseHandler> set = mMessageHandlerTable.keySet();
		if(set == null || set.size()==0) return;
		for(NetResponseHandler handler:set){
			IntentFilter f = mMessageHandlerTable.get(handler);
			if(f!=null){
				if(f.hasAction(action)){
					handler.receive(message);
				}
			}
		}
		
	}
	
	/**
	 * 网络请求接口
	 * 
	 * @param intent
	 * @param handler
	 * @param what
	 * @param enp
	 * @return
	 * >0 本请求发送的SN标号
	 * -1 当前没有网络连接，未发送
	 * -2 当前没有可用的网络，未发送
	 */
	public  static int Request(Intent intent, Handler handler, int what, int enp, long timeout){
		EzMessage msg = IntentTools.intentToMessage(intent);
		return Request(msg,handler,what,enp,timeout);
	}
	public  static int Request(EzMessage message, Handler handler, int what, int enp, long timeout){
	
		EzNetRequest request = new EzNetRequest(message,  handler,  what,  enp, timeout);
		return Request(request);
	}
	
	public  static int Request(EzMessage message){
		return Request(message,null,0,0,0);
	}
	
	private static Boolean LOCK = true; 
	public synchronized static int Request(EzNetRequest request){
		EzMessage msg = request.reqMsg;
		int ret = -1;
		
		//检查网络连接是否存在，如果不存在但是有可用网络，则重连，如果网络不可用，则返回信息告知用户网络不可用
	   	if(NetManager.mState == NetManager.STATE_NONE ){
	    	  if(NetManager.isNetworkAvilable()){
	    		  NetManager.startNet();	    		  
	    	  }else{
	    		  return NET_NO_NETWORK;//当前没有可用的网络
	    	  }
	   	}
		
		if(request.reqType == EzNetRequest.REQ_TYPE_NORMAL && msg!=null){
			ret = EzMessageFactory.getSnClient();//需要获取请求序号
			request.mSn = ret;						
			msg.getKVData("sn").setValue(ret);//序列号
			synchronized(LOCK){
				SendTable.put(ret, request);
			}
		}
		sendQueue.offer(request);			
		return ret;
		
	}
	
	/**
	 * @param intent
	 * @return
	 */
	synchronized static void Response(EzMessage message){
		int sn = message.getKVData("sn").getInt32();		
		EzNetRequest request = SendTable.get(sn);		
		if(request != null){
			synchronized(LOCK){
				SendTable.remove(sn);
			}
			request.mSn = sn;
			request.resMsg = message;
			notifyRequest(request,WHAT_MESSAGE_ARRIVED);
		}
		noHandlerPakage(message);
	}
	
	/**
	 * 服务器已经收到这个数据包，并发回确认信息
	 * @param sn
	 */
	synchronized static void sendOK(int sn){
		if(sn <=0 ) return ;
		EzNetRequest request = SendTable.get(sn);
		if(request == null) return ;
		request.mSendOK = true;
		notifyRequest(request,WHAT_SEND_OK);		
	}
		
	
	/**
	 * 撤销网络请求，这可能是等待数据的页面退出了,这是的cancel主要是避免handler一直保存从而占用内存，
	 * 这并不能阻止数据包被发送到服务器
	 */
	public synchronized static void cancelRequest(int sn){
		synchronized(LOCK){
			SendTable.remove(sn);
		}
	}
	/**
	 * 检查超时请求，这个需要由NetManager定时调用
	 */
	public synchronized static void checkTimeOut(){
		synchronized(LOCK){
			Vector<Integer> outSN = new Vector<Integer>();
			Set<Integer> set = SendTable.keySet();
			if(set == null || set.size()==0) return;
		
			for(Integer sn:set){
				EzNetRequest request = SendTable.get(sn);
				if(request!=null && request.isTimeOut()){
					notifyRequest(request,WHAT_TIMEOUT);
					EzLog.e(D, tag, "发现一个请求超时，sn = " + request.mSn);
					outSN.add(Integer.valueOf(sn));						
				}
			}
			for(Integer sn:outSN){
				SendTable.remove(sn);
			}
		}
	}
	
	/**
	 * 通知发送者数据发送状态，如网络超时，数据发送成功等等
	 * @param request
	 * @param what
	 */
	private static void notifyRequest(EzNetRequest request, int arg1){
		Handler handler = request.mHandler;
		if(handler == null){
			noHandlerPakage(request.resMsg);
			return;
		}
		handler.obtainMessage(request.mWhat, arg1, 0, request).sendToTarget();
	}
	public static void connectLost(){
		synchronized(LOCK){
			Set<Integer> set = SendTable.keySet();
			if(set == null || set.size()==0) return;
			for(Integer sn:set){
				EzNetRequest request = SendTable.get(sn);
				if(request!=null){
					notifyRequest(request,WHAT_NET_DISCONNECT);				
				}
			}
		}
		clearMessage();
	}
		 
}
