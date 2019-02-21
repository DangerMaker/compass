package com.ez08.support.net;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

import com.ez08.support.util.RSA;
import com.ez08.tools.EzLog;
import com.ez08.tools.IntentTools;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;


public class EzSocket {
	private static final String tag = "EzSocket";
	private static boolean D=true;
	//当前连接状态
	private int mState;
    public static final int STATE_NONE = 0;       // 未连接
    public static final int STATE_CONNECTING = 1; // 正在连接网络
    public static final int STATE_CONNECTED = 2;  // 网络已连接
//    public static final int STATE_LOGON = 3;  // 用户已连接


    private EzSocketReader mReaderThread;
    private EzSocketWriter mWriterThread;
        
    private synchronized void setState(int state) {
        mState = state;
    }
    /**
     * Return the current connection state. */
    public int getState() {
        return mState;
    }

    /**
     * 启动连接，如果当前有连接存在，则关闭并停止所有相关线程
     * @param isa
     */
    synchronized void connect(InetSocketAddress isa) {
        if (mConnectThread != null) {mConnectThread.cancel(); mConnectThread = null;}
        if (mReaderThread != null) {mReaderThread.close();mReaderThread = null;}
        if (mWriterThread != null) {mWriterThread.close();mWriterThread = null;}
        //这里还要考虑清理当前的请求队列
        
        if(isa!=null){
        mConnectThread = new ConnectThread(isa);
        setState(STATE_CONNECTING);
        mConnectThread.start();
        }
    }
    
    /**
     * 网络连接成功，启动读写线程
     * @param socket
     */
    private synchronized void connected(Socket socket) {
        EzLog.d(D,tag, "网络连接已成功，开始启动读线程....");
        if (mReaderThread != null) {mReaderThread.close();mReaderThread = null;}
        if (mWriterThread != null) {mWriterThread.close();mWriterThread = null;}
       
        try {
			mReaderThread = new EzSocketReader(socket);
		} catch (IOException e) {
			mReaderThread = null;
			e.printStackTrace();
			setState(STATE_NONE);
			return;
		}
		mReaderThread.start();
		EzLog.d(D,tag, "读线程启动成功，构造写线程，并发送握手包....");
		try {
			mWriterThread = new EzSocketWriter(socket);
		} catch (IOException e) {
			mWriterThread = null;
			EzSocketReader zsr = mReaderThread;
			if(zsr!=null)
				zsr.close();
			mReaderThread = null;
			e.printStackTrace();
			setState(STATE_NONE);
			return;
		}
        setState(STATE_CONNECTED);
    }
    
    /**
     * 启动写进程，只有当NetManager收到合法的确认包的时候，才能够启动写程序
     */
    void startWriter(){
    	if(!mWriterThread.mStartWrite)
    		mWriterThread.start();

    }
    
    
    /**
     * 连接尝试失败，只有在建立连接的过程中出现异常才会调用这个功能
     * 连接异常会停止所有相关线程，并告知NetManager提醒客户
     * @param msg
     */
    private void connectionFailed(String msg) {
        EzLog.i(D,tag,"connectionFailed网络连接失败:" + msg);
        if (mConnectThread != null) {mConnectThread.cancel(); mConnectThread = null;}
        if (mReaderThread != null) {mReaderThread.close();mReaderThread = null;}
        if (mWriterThread != null) {mWriterThread.close();mWriterThread = null;}
        setState(STATE_NONE);
        NetManager.connectFail(msg);
    }

    /**
     * 连接中断，这是由于读写线程出现异常造成的，会停止所有线程，并通知NetManager
     */
    private void connectionLost() {
        EzLog.i(D,tag,"connectionLost网络连接丢失");
        if (mConnectThread != null) {mConnectThread.cancel(); mConnectThread = null;}
        if (mReaderThread != null) {mReaderThread.close();mReaderThread = null;}
        if (mWriterThread != null) {mWriterThread.close();mWriterThread = null;}
        setState(STATE_NONE);
        NetManager.connectLost();

    }
    /**
     * 停止所有线程，关闭网络连接 
     */
    public synchronized void stop() { 
        EzLog.d(D,tag, "调用stop（）.停止所有网络线程...");
        if (mConnectThread != null) {mConnectThread.cancel(); mConnectThread = null;}
        if (mReaderThread != null) {mReaderThread.close();mReaderThread = null;}
        if (mWriterThread != null) {mWriterThread.close();mWriterThread = null;}
        setState(STATE_NONE);
    }    
     
     
    /**
     * 建立连接的线程，此线程只是负责建立网络连接，如果成功，则调用connected启动读写线程
     */
    private ConnectThread mConnectThread;
    private class ConnectThread extends Thread {
        private final InetSocketAddress mmISA;
        private final Socket mmSocket;
        public ConnectThread(InetSocketAddress isa) {
        	mmISA = isa;
        	mmSocket = new Socket();
        }

        public void run() {
            EzLog.w(D,tag, "网络连接线程启动，开始尝试连接网络......");
            EzLog.i(D,tag, "" + mmISA);
            setName("NetConnectThread"); 
            try {
                mmSocket.connect(mmISA,20000);
            } catch (IOException e) {
                connectionFailed(e.getMessage());
                // Close the socket
                try {
                    mmSocket.close();
                } catch (IOException e2) {
                    EzLog.e(D,tag, "unable to close() socket during connection failure"+ e2.getMessage());
                }
                return;
            }

            // Reset the ConnectThread because we're done
            synchronized (EzSocket.this) {
                mConnectThread = null;
            }
            connected(mmSocket);
        }

        public void cancel() {
            try {
                mmSocket.close();
                Thread.sleep(3000);
            } catch (IOException e) {
                Log.e(tag, "close() of connect socket failed", e);
            } catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
    }
    
    private int lastSendSN;
    /**
     * 写线程
     * @author longtan
     *
     */
    private class EzSocketWriter extends Thread {
    	
    	private DataOutputStream writer;
    	private boolean mClosing = false;
    	public boolean mStartWrite = false;

    	/*
    	 * 构造函数 
    	 */
    	public EzSocketWriter(Socket insock) throws IOException
    	{    		
    		writer = new DataOutputStream(insock.getOutputStream());  
    		
    		//发送连接包   		
    		EzMessage ezmsg = NetManager.getConnectMessage();
    		write(ezmsg);   	
    		EzLog.d(D,tag, "握手包发送成功，等待服务器返回握手应答....");
    	} 

    	public void run(){ 
    		mStartWrite = true;
    		try
    		{
    			setName("NetWriter");
    			while(true)
    			{
    				EzNetRequest request = EzNet.sendQueue.take();
    				EzMessage reqMsg = request.reqMsg;
    				
    				//检查是不是发送心跳请求
    				if(request.reqType == EzNetRequest.REQ_TYPE_HEARTBEAT){
    					writer.writeByte(0xFC);
    					writer.flush();
//    					Log.i(tag,"发送一个心跳！");
    					continue;
    				}
    				if(request.reqType == EzNetRequest.REQ_TYPE_DATA_RESPONSE){
    					writer.writeByte(0xFD);
    					writer.writeInt(request.mSn);
    					writer.flush();
    					EzLog.i(D,tag,"确认一个包收到！sn=" + request.mSn);
    					continue;
    				}
    				if(request.reqType == EzNetRequest.REQ_TYPE_HEARTBEAT_RESPONSE){
    					writer.writeByte(0xFC);
    					writer.flush();
    					EzLog.i(D,tag,"回应一个心跳！");
    					continue;
    				}
    				 
 
    				//显示发送的请求的信息

		    		String action = reqMsg.getKVData("action").getString();
		    		EzLog.i(D,tag, ">>>>>>>>>>>>   EzApp.netRequest: ACTION = :" + action + 
							"  SN:" + request.mSn);
    		    	
    				 
    		    	lastSendSN = request.mSn;
    		    	EzMessage message = null;
    		    	try{
           			    if(request.mEnp >=0 ){
        					message = NetManager.mEzCoder.encode(reqMsg, request.mEnp);
        				}
        		    	if(message!=null)
        		    		write(message);
    		    	}catch(Exception e){
    		    		EzLog.e(D,tag, "数据发送异常！300+" + e.getMessage());
    		    		e.printStackTrace();   		    		
    		    	}

    			}
    		}
    		catch(InterruptedException e){
    			if(!mClosing){
    				e.printStackTrace();
    				EzLog.d(D,tag,"网络写线程异常中断1：" + e.getMessage());
    			}else{
    				EzLog.d(D,tag,"关闭写线程1：" + e.getMessage());
    			}
    		}catch(Exception e){
    			if(!mClosing){
    				EzLog.d(D,tag,"网络写线程异常中断2：" + e.getMessage());
    				e.printStackTrace();
    				connectionLost();//异常退出
    			}else{
    				EzLog.d(D,tag,"关闭写线程2：" + e.getMessage());
    			}
    			
    		}
    	} 
    	
    	public synchronized void write(EzMessage message) throws IOException{
    		byte[] msgBuf = message.serializeToPB();
    		if(msgBuf != null)
    		{
    			EzLog.d(D,tag,"Message len = " +msgBuf.length );
    	   		if(NetManager.NET_DEBUG && NetManager.NET_DEBUG_DETAIL)
    	   			EzLog.i(D,tag,message.description());
    	   	    writer.writeByte(0xFE);
    			writer.writeInt(msgBuf.length);
    			if(msgBuf.length>0)
    			{
    				writer.write(msgBuf);
    			} 
    			writer.flush();
    		}
    		NetManager.resetLastNetOperateTime();
    	}
    	
    	public void close()
    	{
    		EzLog.i(D, tag, "调用writer.close()");
    		mClosing =  true;
    		try {
    			writer.close();    			
    		} catch (IOException e) {
    			e.printStackTrace();
    		}
    		writer = null;
    		this.interrupt();
    	}
    }
    
    /**
     * 读线程
     * @author longtan
     *
     */
    private class EzSocketReader extends Thread {
    	private int errorCount = 0;
    	private DataInputStream reader;
    	private static final String tag = "EzSocketReader";
    	private boolean mClosing = false;
    	/*
    	 * 
    	 * 构造函数
    	 */
    	public EzSocketReader(Socket insock) throws IOException
    	{
    		reader = new DataInputStream(insock.getInputStream());
    	}
 
    	/*
    	 *线程阻塞式的接收数据
    	 */
    	public void run(){
    		setName("NetReader");
    		int errorCounter = 0;
    		while(true){
    			
    	    	try{
    	    		byte[] ibyte = new byte[1];
    	    		reader.readFully(ibyte);    
     				int flag = (ibyte[0] & 0xFF); 
    				switch(flag){
    				case 0xFE://收到数据包
        				//读取长度
        				int nLength = reader.readInt();       				

        				if(nLength > 2048*1024){
        					byte[]	packContent = new byte[nLength];  
        					EzLog.e(D, tag, "数据包过大，收取并丢弃 lEN=" + nLength);
        					reader.readFully(packContent);
        				}else if(nLength > 0){
        					byte[]	packContent = new byte[nLength];        					
        					EzLog.i(D, tag, "<<<<<<<<<<<    NetRespose: len = :" + nLength);
        					reader.readFully(packContent);
        					ParsePackage(packContent);					
        				}else{
        					EzLog.e(D, tag, "数据包长度错误 len = :" + nLength);
        				}
        				NetManager.resetLastReceiveTime();
        				break;
    				case 0xFD:
    					int sn = reader.readInt();
    					EzLog.i(D,tag,"收到一个确认 sn=" + sn);
    					EzNet.sendOK(sn);
    					NetManager.resetLastReceiveTime();
    					break;
    				case 0xFB:
    					EzLog.i(D,tag,"收到心跳回应！");
    					NetManager.resetLastReceiveTime();
    					break;
    				case 0xFC:
       					EzLog.i(D,tag,"收到心跳！");
    					EzNetRequest hearbeart = new EzNetRequest(EzNetRequest.REQ_TYPE_HEARTBEAT_RESPONSE);
    					EzNet.Request(hearbeart);
    					NetManager.resetLastReceiveTime();
    					break;
    				default:
    					reader.skip(reader.available());
    				}
    				errorCounter = 0;
    	   			//制造一个崩溃
//        			makeErrorCounter++;
//        			if(makeErrorCounter>20){
//        				Intent intent = null;
//        				intent.putExtra("aa", "");
//        			}

	    		}catch(IOException e){	    			
	    			Log.i(tag,"网络读线程错误，重试次数超过10,或主动断网，断网重连 mClosing =" + mClosing);	    			
	    			if(!mClosing)
	    				connectionLost();
	    			break;
	    		}
    	    	catch(Exception e){
	    			e.printStackTrace();
	    			if(!mClosing)
	    				connectionLost();
	    			Log.i(tag,"网络包处理异常：Exception" + e.getMessage());
	    			break;
	    		}catch(OutOfMemoryError e){
	    			EzLog.i(D, tag, "内存溢出错误，将返回的数据包丢弃");
	    			connectionLost();//重置网络，以恢复错误
	    			break;
	    		}
    	    	finally{
    	    		NetManager.resetLastReceiveTime();
    	    	}
			}
    	}
    	
    	/*
    	 * 解析数据包
    	 */
    	int makeErrorCounter = 0;
    	
    	private void ParsePackage(byte[] pack)
    	{
    		EzLog.i(D, tag, "解析一条信息");
    			EzMessage msg = EzMessageFactory.CreateMessageObject(pack);
    			String action = msg.getKVData("action").getString();
    			EzLog.i(D,tag,"action = :" + action);
    			EzMessage msgNew = msg;
    			//如果不是connect包，则解密
    			if(NetManager.ACTION_AUTH_CONNECT.equalsIgnoreCase(action)){
    				NetManager.connectMessageHandle(msgNew);
    			}else if(NetManager.ACTION_AUTH_LOGIN_RESPONSE.equalsIgnoreCase(action)
    					|| NetManager.ACTION_AUTH_LOGOUT_RESPONSE.equalsIgnoreCase(action)){
    				msgNew = NetManager.mEzCoder.decode(msg);
    				NetManager.connectMessageHandle(msgNew);
    			}else{   			
    				msgNew = NetManager.mEzCoder.decode(msg);
    			}
    			if(msgNew != null){    				
    				if(NetManager.NET_DEBUG && NetManager.NET_DEBUG_DETAIL){   					
    	    			EzLog.i(D,tag,msgNew.description());   
    				}
    				EzNet.Response(msgNew);
    			}
     				
    	}	
    	//关闭
    	public void close()
    	{
    		EzLog.i(D, tag, "调用reader.close()");
    		mClosing = true;
    		try {
    			reader.close();
    		} catch (IOException e) {
    			//e.printStackTrace();
    		}
    		reader = null;
    		this.interrupt();
    	}
    }
}
