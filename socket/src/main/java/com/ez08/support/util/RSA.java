package com.ez08.support.util;

import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;


public class RSA {

	public static final String KEY_ALGORITHM = "RSA";  
	public static final String PUBLIC_KEY ="publicKey";
	public static final String PRIVATE_KEY ="privateKey"; 
	//
	public static void main(String[] args) throws Exception{
		Thread.sleep(1000);
		//BigInteger a = new BigInteger(55,new Random());
		
		long tick1 = System.currentTimeMillis();
		Map<String,byte[]> keyMap = generateKey();
		long tick2 = System.currentTimeMillis();
		System.out.println(tick2-tick1);
		RSAPublicKey publicKey = getPublicKey(keyMap.get(PUBLIC_KEY));
		RSAPrivateKey privateKey = getPrivateKey(keyMap.get(PRIVATE_KEY));
		System.out.println("modulus(n):" + publicKey.getModulus().toString());
		System.out.println("public(e):" + publicKey.getPublicExponent());
		System.out.println("private(d):" + privateKey.getPrivateExponent());
		String info ="中国123456中国123456";
		String info2 ="中国1234567中国123456";
		//
		for(int i=0;i<10;i++)
		{
			//
			byte[] bytes = encrypt(info.getBytes("utf-8"),publicKey);
			byte[] newbytes = decrypt(bytes, privateKey);
			//System.out.println(new String(bytes,"utf-8"));
			
			byte[] signbytes = sign(info2.getBytes("utf-8"),privateKey);
			boolean b =  verifySign(info.getBytes("utf-8"),signbytes,publicKey);
			//System.out.println(b);
		}
	    tick2 = System.currentTimeMillis();
		System.out.println(tick2-tick1);
	}
	
	/**
	  * 加密,key可以是公钥，也可以是私钥
	  *
	  * @param message
	  * @return
	  * @throws Exception
	  */
	 public static byte[] encrypt(byte[] message, Key key) throws Exception {
	  Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
	  cipher.init(Cipher.ENCRYPT_MODE, key);
	  return cipher.doFinal(message);
	 }

	 /**
	  * 解密，key可以是公钥，也可以是私钥，如果是公钥加密就用私钥解密，反之亦??
	  *
	  * @param message
	  * @return
	  * @throws Exception
	  */
	 public static byte[] decrypt(byte[] message, Key key) throws Exception {
	  Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
	  cipher.init(Cipher.DECRYPT_MODE, key);
	  return cipher.doFinal(message);
	 }

	 /**
	  * 用私钥签??
	  *
	  * @param message
	  * @param key
	  * @return
	  * @throws Exception
	  */
	 public static byte[] sign(byte[] message, PrivateKey key) throws Exception {
	  Signature signetcheck = Signature.getInstance("MD5withRSA");
	  signetcheck.initSign(key);
	  signetcheck.update(message);
	  return signetcheck.sign();
	 }

	 /**
	  * 用公钥验证签名的正确??
	  *
	  * @param message
	  * @param signStr
	  * @return
	  * @throws Exception
	  */
	 public static boolean verifySign(byte[] message, byte[] signStr, PublicKey key)
	   throws Exception {
	  if (message == null || signStr == null || key == null) {
	   return false;
	  }
	  Signature signetcheck = Signature.getInstance("MD5withRSA");
	  signetcheck.initVerify(key);
	  signetcheck.update(message);
	  return signetcheck.verify(signStr);
	 }
	
	 /*
	  * 创建RSA密钥
	  */
	public static Map<String,byte[]> generateKey() throws NoSuchAlgorithmException{
		Map<String,byte[]> keyMap = new HashMap<String,byte[]>();
		KeyPairGenerator keygen = KeyPairGenerator.getInstance(KEY_ALGORITHM);
		SecureRandom random = new SecureRandom();
		random.setSeed(System.currentTimeMillis());
		keygen.initialize(1024, random);
		//
		KeyPair kp = keygen.generateKeyPair();
		RSAPrivateKey privateKey = (RSAPrivateKey)kp.getPrivate();
 		byte[] privateKeyBytes = privateKey.getEncoded();
		RSAPublicKey publicKey = (RSAPublicKey)kp.getPublic(); 
		byte[] publicKeyBytes = publicKey.getEncoded();
		keyMap.put(PUBLIC_KEY, publicKeyBytes);
		keyMap.put(PRIVATE_KEY, privateKeyBytes);
		return keyMap;
	}
	
	/*
	 * 根据bytes获得publickey对象
	 */
	public static RSAPublicKey getPublicKey(byte[] publicKey) throws Exception{
		byte[] keyBytes = publicKey;
		X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
		return (RSAPublicKey) keyFactory.generatePublic(spec);
	}
	
	/*
	 * 根据bytes获得privatekey对象
	 */
	public static RSAPrivateKey getPrivateKey(byte[] privateKey) throws Exception{
		byte[] keyBytes = privateKey;
		PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
		return (RSAPrivateKey) keyFactory.generatePrivate(spec);
	}

}
