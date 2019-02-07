package com.ez08.support.util;

import java.math.BigInteger;
import java.util.Random;

public class DHE {
	
	public static final  BigInteger p = new BigInteger("d6a3b07cf1a0c69775597260f010f30c1c297e827d93ba3186fa32433c614956614b7600568f3c155c2ca84df14bdee7f6794425968b739c1c4bc734d0cc79bcda721cdfc6d99c6ca9a7b878ae1b6f1728ba9f493b3a0221360e20ca7af1d635d0945e95bc5212f41da24e9b7d99ba6b897a6107686b37aeee229699e4520937",16);
	public static final  BigInteger g = BigInteger.valueOf(2L);
	private static final  BigInteger MAXRC4 = new BigInteger("AAAAAAAAAAAAAAAA".getBytes());
	/**
	 * ���� keyPair[0]Ϊ˽Կ��keyPair[1]Ϊ��Կ��
	 * ��Կ���ڴ��ݸ��Է���
	 */
	public static boolean createKeyPair(BigInteger[] keyPair)
	{
		if(keyPair == null)
			return false;
		//p and g,1024bit p;
		//rand private
		BigInteger skeya =  new BigInteger(512,new Random(System.currentTimeMillis()));   
		BigInteger pkeya = g.modPow(skeya, p);
		keyPair[0] = skeya;
		keyPair[1] = pkeya;
		return true;
	}

	/**
	 * 1.���ݶԷ����صĹ�Կ�ͱ��ص�˽Կ�����㹲�����ʱ������Կ;
	 * ���Ƴ����� 128bit;
	 */
	public static byte[] calcShareKey(BigInteger pkeyb,BigInteger skeya)
	{
		if(pkeyb == null)
			return null;
		//p and g,1024bit p;
		//rand private
		BigInteger tempSharekey = pkeyb.modPow(skeya, p).mod(MAXRC4);
		//System.out.println("skeya = " + skeya.toString(16));
		//System.out.println("pkeyb = " + pkeyb.toString(16));
		//System.out.println("sharekey = " + tempSharekey.toString(16));
		return tempSharekey.toByteArray();
	}
	/**
	 * 1.���ݶԷ����صĹ�Կ�ͱ��ص�˽Կ�����㹲�����ʱ������Կ;
	 * 2.��RC4��SecrectKey����ʱ������Կ���ܳ�����ʹ�õ�RC4Key,�ַ���ģʽ
	 */
	public static byte[] calcRealKey(BigInteger pkeyb,BigInteger skeya,byte[] SecrectKey)
	{
		if(pkeyb == null)
			return null;
		//p and g,1024bit p;
		//rand private
		byte[] tempRC4key = calcShareKey(pkeyb,skeya);
		byte[] realKey = RC4.doCrypt(SecrectKey,tempRC4key,RC4.DECRYPT_MODE);
		return realKey;
	}
}
