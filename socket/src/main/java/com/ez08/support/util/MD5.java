package com.ez08.support.util;

import java.security.PrivateKey;

//import sun.misc.BASE64Encoder;

public class MD5 {

	/** MD5加密后转成字符串 */    
	/*public static String getMD5Base64(String srcString)     
	{     
	    java.security.MessageDigest md = null;     
	    try    
	    {     
	        md = java.security.MessageDigest.getInstance("MD5");     
	    }     
	    catch (java.security.NoSuchAlgorithmException e)     
	    {     
	        e.printStackTrace();     
	    }     
	    md.update(srcString.getBytes());     
	    byte[] buf = md.digest();
	    BASE64Encoder base64=new BASE64Encoder();
        return base64.encode(buf);
	}*/
	
	public static byte[] getMD5(byte[] inBytes)     
	{     
	    java.security.MessageDigest md = null;     
	    try    
	    {
	        md = java.security.MessageDigest.getInstance("MD5");
	    }
	    catch (java.security.NoSuchAlgorithmException e)     
	    {
	        e.printStackTrace();
	    }
	    md.update(inBytes);     
	    byte[] buf = md.digest();
        return buf;
	}
	
	public static String bytes2HexString(byte[] b) {
		  String ret = "";
		  for (int i = 0; i < b.length; i++) {
		   String hex = Integer.toHexString(b[ i ] & 0xFF);
		   if (hex.length() == 1) {
		    hex = '0' + hex;
		   }
		   ret += hex.toUpperCase();
		  }
		  return ret;
	}

	public static String getMD5HexString(byte[] inBytes)     
	{     
	    java.security.MessageDigest md = null;     
	    try    
	    {
	        md = java.security.MessageDigest.getInstance("MD5");
	    }
	    catch (java.security.NoSuchAlgorithmException e)     
	    {
	        e.printStackTrace();
	    }
	    md.update(inBytes);     
	    byte[] buf = md.digest();
	    return bytes2HexString(buf);
	}
	
	 public static byte[] sign(byte[] message)
	 {
		 return getMD5(message);
	 }
	 public static boolean verifySign(byte[] message,byte[] signData)
	 {
		byte[] signnew = getMD5(message);
		if(signnew.length != signData.length)
			return false;
		for(int i=0;i<signnew.length;i++)
		{
			if(signnew[i] != signData[i])
				return false;
		}
		return true;
	 }
}
