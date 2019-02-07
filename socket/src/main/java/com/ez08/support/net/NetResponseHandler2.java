package com.ez08.support.net;



import android.content.Intent;
import android.os.Handler;
import android.os.Message;

public abstract class NetResponseHandler2 extends Handler {
	@Override
	public void handleMessage(Message msg) {
		if (msg == null){
			return;
		}
		switch(msg.arg1){
		case EzNet.WHAT_SEND_OK:
			serverReceived(msg.what);
			break;
		case EzNet.WHAT_TIMEOUT:
			timeout(msg.what);
			break;
		case EzNet.WHAT_CANCEL:
			cancel(msg.what);
			break;
		case EzNet.WHAT_MESSAGE_ARRIVED:
			Intent intent = EzNetRequest.getResponseIntentFromOSMessage(msg);
			if(NetManager.mCid!=null&&NetManager.mCid.contains("T-")&&"ez08.auth.connect".equalsIgnoreCase(intent
					.getAction())){
				Intent mUseless=new Intent();
				mUseless.putExtra("errcode", -1);
				mUseless.putExtra("msg", "请重新登录");
				receive(msg.what,false,mUseless);
				return;
			}
			msg.obj = null;
			boolean result = false;
			if (intent != null) {
				result = intent.getBooleanExtra("result", false);
			}
			receive(msg.what,result,intent);
			break;
		case EzNet.WHAT_NET_DISCONNECT:
			netConnectLost(msg.what);
			break;			
		}
	}
	abstract public void receive(int what, boolean result, Intent intent);
	
	public void serverReceived(int what){
		
	}
	public void timeout(int what){
		
	}
	public void netConnectLost(int what){
		
	}
	public void cancel(int what){
		
	}
}
