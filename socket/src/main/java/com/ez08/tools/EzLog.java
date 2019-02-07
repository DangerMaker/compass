package com.ez08.tools;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

import android.os.Environment;
import android.util.Log;

public class EzLog {

	private static String pre = "EZ08LOG:";
	
	private static boolean D = true;
	
	public static boolean TO_FILE = false;
	
	public static void close(){
		D = false;
	}
	public static void open(){
		D = true;
	}
	public static void i(boolean d, String tag, String msg){
		if(D && d)
			Log.i(pre+ tag,msg);
		if(TO_FILE){
			writeToFile(tag, msg);
		}
	}
	public static void d(boolean d, String tag, String msg){
		if(D && d)
			Log.d(pre+ tag,msg);
		if(TO_FILE){
			writeToFile(tag, msg);
		}
	}
	public static void e(boolean d, String tag, String msg){
		if(D && d)
			Log.e(pre+ tag,msg);
		if(TO_FILE){
			writeToFile(tag, msg);
		}
	}
	public static void v(boolean d, String tag, String msg){
		if(D && d)
			Log.v(pre+ tag,msg);
		if(TO_FILE){
			writeToFile(tag, msg);
		}
	}
	public static void w(boolean d, String tag, String msg){
		if(D && d)
			Log.w(pre+ tag,msg);
		if(TO_FILE){
			writeToFile(tag, msg);
		}
	}

	private static void writeToFile(String tag, String content){
		String str = Environment.getExternalStorageDirectory()
				.getAbsolutePath() + "/ez08_log/";	
		File file = new File(str);
		if(!file.exists())
			file.mkdirs();
		
		file = new File(str + "log.txt");
		if(!file.exists())
			try {
				file.createNewFile();
			} catch (IOException e1) {				
				e1.printStackTrace();
			}
		 try {  
	            // 打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件  
	            FileWriter writer = new FileWriter(str+ "log.txt", true);  
	            Date date = new Date();	            
	            writer.write(date.toString() + tag + "\r\n");
	            writer.write(content + "\r\n\r\n");  
	            writer.close();  
	        } catch (IOException e) {  
	            e.printStackTrace();  
	        }  
		
		
	}
}
