package com.ez08.compass.autoupdate.tools;

public class AutoStringUtility {
	/**
	 * 判断版本号大小，版本号格式为 1.m.n
	 * @param versionName1 
	 * @param versionName2
	 * @return -1,0 1
	 */
	public static boolean compareStringVersion(String versionName1, String versionName2){
		try{
			String[] local=versionName1.split("\\.");
			String[] target=versionName2.split("\\.");
//			int code1=0;
//			int code2=0;
//			for(int i=strs1.length-1,j=0;i>=0;i--,j++){
//				int value=Integer.parseInt(strs1[i]);
//				code1+=value*Math.pow(10,j);
//			}
//			for(int i=strs2.length-1,j=0;i>=0;i--,j++){
//				int value=Integer.parseInt(strs2[i]);
//				code2+=value*Math.pow(10,j);
//			}
			boolean setVersion = false;
			for (int i =0; i < local.length; i++) {
				int a=Integer.parseInt(local[i]);
				int b=Integer.parseInt(target[i]);
				if (a < b) {
					setVersion = true;
					break;
				}else if(a>b){
					setVersion = false;
					break;
				}else{
					setVersion=false;
				}
			}
			return setVersion;
//			if (!setVersion) {
//				return;
//			}
//			
//			return (code1-code2<0)?-1:(code1==code2?0:1);
		}catch(Exception e){
			throw new RuntimeException("版本号格式有误");
		}
	}
}
