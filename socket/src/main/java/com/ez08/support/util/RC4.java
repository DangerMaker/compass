package com.ez08.support.util;


import javax.crypto.*;
import javax.crypto.spec.*;


/**
 *
 */
public class RC4 {

    public static final int ENCRYPT_MODE = 1;
    public static final int DECRYPT_MODE = 2;

    private static String ClassName = RC4.class.getName();
 
    public RC4() {
    }

    public static byte[] doCrypt(byte[] inputData, byte[] key, int cryptMode){
        if(null == inputData || key == null)
        	return null;

        byte state[] = new byte[256];
        int x=0;
        int y=0;
		int index1 = 0;
		int index2 = 0;
        //
		for (int i = 0; i < 256; i++) {
		   state[i] = (byte) i;
		}
		byte tmp;
		if (key == null || key.length == 0) {
		        throw new NullPointerException();
		}
		for (int i = 0; i < 256; i++) {
		        index2 = ((key[index1] & 0xff) + (state[i] & 0xff) + index2) & 0xff;
		        tmp = state[i];
		        state[i] = state[index2];
		        state[index2] = tmp;
		        index1 = (index1 + 1) % key.length;
		}
        //
        byte[] buf = inputData;
        int xorIndex;
        if (buf == null) {
                return null;
        }
        byte[] result = new byte[buf.length];
        for (int i = 0; i < buf.length; i++) {
                x = (x + 1) & 0xff;
                y = ((state[x] & 0xff) + y) & 0xff;
                tmp = state[x];
                state[x] = state[y];
                state[y] = tmp;
                xorIndex = ((state[x] & 0xff) + (state[y] & 0xff)) & 0xff;
                result[i] = (byte) (buf[i] ^ state[xorIndex]);
        }
        return result;
    }
    
    public static byte[] doCryptSys(byte[] inputData, byte[] key, int cryptMode){
        byte[] outputData = null;
        try{

            if(null == inputData || key == null 
               || ((cryptMode != ENCRYPT_MODE)&&(cryptMode != DECRYPT_MODE))){
                return null;
            }

            //???????
            SecretKey theKey = new SecretKeySpec(key,"RC4");

            //????????
            Cipher cipher = Cipher.getInstance("RC4");
            cipher.init(Cipher.ENCRYPT_MODE,theKey);
            outputData = cipher.doFinal(inputData);
        }catch(Exception e){
        	e.printStackTrace();
        }
        return outputData;   
    }
}
