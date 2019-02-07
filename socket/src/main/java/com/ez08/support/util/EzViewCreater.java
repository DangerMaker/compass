package com.ez08.support.util;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import android.app.Application;
import android.content.res.XmlResourceParser;
import android.database.Cursor;
import android.util.Log;

public class EzViewCreater {
	private static final String tag = "EzViewCreater";
	/**
	 * 反射解析接口
	 */
	public static XmlResourceParser EzParser(byte[] bytes) {
		
       XmlResourceParser parser = null;
 	   Object obj = null; 
	   Constructor con_tor = null;
	   Class c;
	   Method md = null; 	   
	   try {
		   c = Class.forName("android.content.res.XmlBlock");
		   Class partypes[] = new Class[1];
		   partypes[0] = bytes.getClass();

		   con_tor = c.getConstructor(partypes);
	   
		   Object objs[] = new Object[1];
		   objs[0] = bytes;
	   
		   obj = con_tor.newInstance(objs);
		   md = c.getMethod("newParser", (Class[])null);
		   parser = (XmlResourceParser) md.invoke(obj, (Object[])null);

	   } catch (Exception e) {
		   e.printStackTrace();
	   } 	   
	   return parser;		
	}
	public  byte[] getFile(String name, Application app){
		InputStream is = null; 
	    int index = name.indexOf('.');
	    String subname = name;
	    if(index>0)
	       subname = name.substring(0,index);
	    int resId1 = app.getResources().getIdentifier(app.getPackageName() + ":layout/" + subname, null,null);
	        		
	    if(resId1 >0){
			is = app.getResources().openRawResource(resId1);
			if(is!=null){
			   byte[] bytes;
				try {
					bytes = new  byte[is.available()];
					is.read(bytes);
					return bytes;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
   
			}
	    }
	    return null;
	 }
}
