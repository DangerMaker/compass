package com.ez08.tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

import com.ez08.support.util.CodedInputStream;
import com.ez08.support.util.CodedOutputStream;
import com.ez08.support.util.EzKeyValue;
import com.ez08.support.util.EzValue;

public class SDTools {
	private static final String tag = "SDTools";
	
	/**
	 * 是否有SD卡
	 */
	public static boolean hasSD() {
		String status = Environment.getExternalStorageState();
		if (status.equals(Environment.MEDIA_MOUNTED)) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * 检查目录是否存在，如果不存在，则创建之，
	 * @param dir
	 * @return 如果目录本来就存在，则返回true，否则返回false
	 */
	public static boolean createDirIfNotExsit(String pathstr) throws Exception {
		File path = new File(pathstr);
		boolean exists = path.exists();
		if (!exists) {
			path.mkdirs();
		}		
		return exists;
	}

	public static boolean isFileExist(String dir, String file) {
		String str = Environment.getExternalStorageDirectory()
				.getAbsolutePath() + "/" + dir + "/" + file;
		File path = new File(str);
		return path.exists();
	}

	
	public static void save(String dir, String filename, byte[] bytes){
		FileOutputStream fout = null;
		String SDPATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + dir + "/";
		try{
			createDirIfNotExsit(SDPATH);
			fout = new FileOutputStream(SDPATH + filename);
			fout.write(bytes);
		}catch(Exception e){
			e.printStackTrace();

		}finally{
			try {
				fout.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}
	
	public static byte[] getBytesOfFile(String dir, String filename){
		if(dir==null || filename == null || filename.equalsIgnoreCase("") || dir.equalsIgnoreCase(""))
			return null;
		
		byte[] content = null;
		//读取文件内容
		String SDPATH=Environment.getExternalStorageDirectory() + "/" + dir + "/";
		try {
			File file = new File(SDPATH + filename);
			int count = (int) file.length();
			if(count>0){
				content = new byte[count];
				FileInputStream fin = new FileInputStream(SDPATH + filename);
				fin.read(content);
				fin.close();
			}

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return content;

	}
	/**
	 * 创建并打开文件
	 * @return
	 */
	public static CodedOutputStream openForWrite(String dir, String file)
			throws Exception {
		String SDPATH = Environment.getExternalStorageDirectory()
				.getAbsolutePath() + "/" + dir + "/";
		createDirIfNotExsit(SDPATH);
		FileOutputStream fout = new FileOutputStream(SDPATH + file);
		CodedOutputStream cOut = CodedOutputStream.newInstance(fout);
		return cOut;

	}
	public static Bundle getHeader(String dir, String file){
		try{
			CodedInputStream in = openForRead(dir,  file);
			EzValue value = new EzValue();
			value.readValueFrom(in);
			
			EzKeyValue [] vls = value.getKeyValues() ;
			return IntentTools.keyValue2Bundle(vls);
			
		}catch(Exception e ){
			e.printStackTrace();
		}
		return null;
	}
	public static CodedInputStream openForRead(String dir, String file) {
		FileInputStream fin = null;
		String SDPATH = Environment.getExternalStorageDirectory()
				.getAbsolutePath() + "/" + dir + "/";
		try {
			fin = new FileInputStream(SDPATH + file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
		return CodedInputStream.newInstance(fin);
	}
	
	/**
	 * 向文件中写入一个Bundle
	 * @param out
	 * @param bdl
	 */
	public static void writeAsEzValue(CodedOutputStream out, Bundle bdl) {
		if (out == null || bdl == null)
			return;
		EzValue value = IntentTools.bundle2KeyValues(bdl);
		value.writeValueTo(out);
	}
	public static void writeAsEzValue(CodedOutputStream out, String key, Object value) {
		if (out == null || value == null || key == null){
			Log.e(tag,"有数据项为空");
			return;
		}
		EzKeyValue keyValue = new EzKeyValue(key, value);
		keyValue.writeKeyValueTo(out);
	}
	public static void writeAsEzValue(CodedOutputStream out, EzKeyValue keyValue) {
		if (out == null || keyValue == null ){
			Log.e(tag,"有数据项为空");
			return;
		}
		keyValue.writeKeyValueTo(out);
	}
	
	public static boolean delete(String dir, String file) {
		String str = Environment.getExternalStorageDirectory()
				.getAbsolutePath() + "/" + dir + "/" + file;
		File path = new File(str);
		return path.delete();
	}

}
