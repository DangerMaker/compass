package com.ez08.tools;

import java.util.Set;
import java.util.Vector;

import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.ez08.support.net.EzMessage;
import com.ez08.support.net.EzMessageFactory;
import com.ez08.support.util.ByteString;
import com.ez08.support.util.EzKeyValue;
import com.ez08.support.util.EzValue;

public class IntentTools {
    /**
     * EzMessage 转 Intent
     */
    public static Intent messageToIntent(EzMessage msg)
    {
    	if(msg == null)
    		return null;
    	Intent intent = new Intent();
    	try{
	    	String strValue = msg.getKVData("componentname").getString();
	    	if(strValue != null)
	    	{
	    		intent.setComponent(ComponentName.unflattenFromString(strValue));
	    	}
	    	strValue = msg.getKVData("action").getString();
	    	if(strValue != null)
	    	{
	    		intent.setAction(strValue);
	    	}
	    	strValue = msg.getKVData("uri").getString();
	    	if(strValue != null)
	    	{
	    		intent.setData(Uri.parse(strValue));
	    	}
	    	ByteString[] strValues = msg.getKVData("category").getByteStrings();
	    	if(strValues != null)
	    	{
	    		for(int i=0;i<strValues.length;i++)
	    		{
	    			intent.addCategory(strValues.toString());
	    		}
	    	}
	    	intent.setFlags(msg.getKVData("flags").getInt32());
	    	EzKeyValue[] kvs = msg.getKVData("extra").getKeyValues();    
	    	Bundle bdl = keyValue2Bundle(kvs);
	    	if(bdl != null)
	        	intent.putExtras(bdl);
	    	//增加sn和userid到extra中;
			intent.putExtra("sn", msg.getKVData("sn").getInt32());
			intent.putExtra("tid", msg.getKVData("tid").getString());
    	}catch(Exception e){
    		return null;
    	}
    	return intent;
    } 
    
    public static Bundle keyValue2Bundle(EzKeyValue[] kvs){
    	if(kvs==null || kvs.length==0) return null;
    	
    	Bundle bdl = new Bundle();
		for(int i=0;i<kvs.length;i++)
		{
			if(kvs[i] != null)
				kvs[i].addToBundle(bdl);
		}    	
		return bdl;
    }
    /**
     * Intent 转 EzMessage
     */
    public static EzMessage intentToMessage(Intent intent)
    {
    	EzMessage msg = EzMessageFactory.CreateMessageObject("msg.intent");
    	if(msg == null || intent == null)
    		return null;
    	//从Intent中取sn和tid;
//    	msg.getKVData("sn").setValue(intent.getIntExtra("sn", EzMessageFactory.getSnClient()));//序列号
    	msg.getKVData("sn").setValue(intent.getIntExtra("sn", -1));//序列号
    	String tempID = intent.getStringExtra("tid");
    	if(tempID == null || tempID.length() <=0 )
    		tempID = "";
    	msg.getKVData("tid").setValue(tempID);
    	// 
		if(intent.getComponent() != null)
			msg.getKVData("componentname").setValue(intent.getComponent().flattenToString());	
		if(intent.getAction() != null)
			msg.getKVData("action").setValue(intent.getAction());
		if(intent.getDataString() != null)
			msg.getKVData("uri").setValue(intent.getDataString());		
		Set<String> ctg = intent.getCategories();
		if(ctg != null && ctg.size()>0)
		{
			String[] ctgArray = new String[ctg.size()];
			ctg.toArray(ctgArray);
			msg.getKVData("category").setValue(ctgArray);
		}
		msg.getKVData("flags").setValue(intent.getFlags());

		Vector<EzKeyValue> kvs = new Vector<EzKeyValue>();
    	Bundle bdl = intent.getExtras();
    	if(bdl != null  && !bdl.isEmpty())
    	{
    		String[] keys = new String[bdl.size()];
    		bdl.keySet().toArray(keys);
    		for(int i=0;i<keys.length;i++)
    		{
    			if(keys[i].equals("sn") || keys[i].equals("tid"))
    				continue;
    			Object value = bdl.get(keys[i]);
				EzKeyValue keyval = new EzKeyValue(keys[i]);
				if(keyval.setValue(value))
				{
					kvs.add(keyval);
				}				
    		}
    		if(kvs.size() > 0)
    		{
    			EzKeyValue[] keyvalues = new EzKeyValue[kvs.size()];
    			kvs.toArray(keyvalues);
    			msg.getKVData("extra").setValue(keyvalues);
    		}
    	}
    	return msg;
    }
    
    public static EzValue bundle2KeyValues(Bundle bdl){
    	if(bdl != null  && !bdl.isEmpty())
    	{
    		Vector<EzKeyValue> kvs = new Vector<EzKeyValue>();
    		String[] keys = new String[bdl.size()];
    		bdl.keySet().toArray(keys);
    		for(int i=0;i<keys.length;i++)
    		{
//    			if(keys[i].equals("sn") || keys[i].equals("tid"))
//    				continue;
    			Object value = bdl.get(keys[i]);
				EzKeyValue keyval = new EzKeyValue(keys[i]);
				if(keyval.setValue(value))
				{
					kvs.add(keyval);
				}				
    		}
    		if(kvs.size() > 0)
    		{
    			EzKeyValue[] keyvalues = new EzKeyValue[kvs.size()];
    			kvs.toArray(keyvalues);
    			return new EzValue(keyvalues);
    		}
    	}
    	return null;
    }
	 public static EzValue safeGetEzValueFromIntent(Intent intent, String pn){
		 if(intent==null || pn ==null || pn.equalsIgnoreCase(""))
			 return null;
		 Object o = intent.getSerializableExtra(pn);
		 if(o!=null && o instanceof EzValue)
			 return (EzValue)o;
		 return null;
	 }
}
