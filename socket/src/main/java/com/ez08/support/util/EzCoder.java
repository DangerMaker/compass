package com.ez08.support.util;

import java.math.BigInteger;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.HashMap;
import java.util.Map;

import com.ez08.support.net.EzMessage;
import com.ez08.support.net.EzMessageFactory;

//编解码，包含整包加密和变量两种
public class EzCoder {
	public static final int ENC_NORMAL = 0;//普通变量加密
	public static final int ENC_PACKAGE = 1;//整包加密，无签名
	public static final int ENC_PACKAGEANDSIGN = 2;//整包加密，要求签名
	
	/*
	 * 加密和签名需要的信息
	 */
	private String tid;
	private String mobile;
	private String	  rand; 	//短信随机码;
	private byte[] sKeyA; 	//自己创建和持有的，私钥A,1024bit
	private byte[] pKeyA;  	//自己创建和持有的，公钥A,
	private byte[] pKeyB;  	//对方提供的公钥，公钥B,
	private byte[]	  rc4key; 	//RC4对称加密密钥,128bit;
	private String[]  keyNames; //需要加密的变量名称
	/*
	 * action的转换map
	 */
	private HashMap<String,Integer> actionCodeMap = new HashMap<String,Integer>();

	/*
	 * 密钥交换和加解密模式 
	 * 默认值是0;
	 * 0：RSA密钥交换，RSA签名,RC4加解密
	 * 1为d-h密钥交换，md5签名,RC4加解密
	 */
	private int cryptionMode = 0;
	/*
	 * 是否已经完成密钥交换
	 */
	private boolean finishSetting = false;
	
	/*
	 * 0或者1
	 */
	public EzCoder(int mode)
	{
		cryptionMode = mode;
	}

	public void setActionCodeMap(HashMap<String,Integer> codeMap)
	{
		actionCodeMap = codeMap;
	}

	/*
	 * 密钥交换完成后，使用的解码函数；
	 * 将密文的intentMsg解码成为明文的新的EzMessage结果，
	 * 返回新的EzMessage;
	 * 如果失败返回null;
	 */
	public EzMessage decode(EzMessage intent)
	{
		EzMessage newMsg = intent.clone();
		//
		if(finishSetting == false)
			return newMsg;
		//
		int encflag = newMsg.getKVData("encflags").getInt32();
		switch(encflag)
		{
			case ENC_NORMAL:
			{
				EzKeyValue[] kvs = newMsg.getKVData("extra").getKeyValues();
				if(kvs != null)
				{
					for(int i=0;i<kvs.length;i++)
					{
						if(kvs[i] == null)
							continue;
						if(kvs[i].getType() == EzValue.VALUE_TYPE_ENCBYTES)
						{
							byte[] valuebytes = kvs[i].getEncBytes();
							if(valuebytes != null)
							{
								byte[] decbytes = RC4.doCrypt(valuebytes, rc4key, RC4.DECRYPT_MODE);
								if(decbytes != null)
								{
									kvs[i].valueFromBytes(decbytes);
								}								
							}
						}
					}
				}
				return newMsg;
			}
			case ENC_PACKAGE:
			{
				EzKeyValue[] kvs = newMsg.getKVData("extra").getKeyValues();
				for(int i=0;i<kvs.length;i++)
				{
					if(kvs[i].getName().equals("enc"))
					{
						byte[] packagebytes = kvs[i].getBytes();
						byte[] decbytes = RC4.doCrypt(packagebytes, rc4key, RC4.DECRYPT_MODE);
						return EzMessageFactory.CreateMessageObject(decbytes);
					}
				}
				return null;
			}
			case ENC_PACKAGEANDSIGN:
			{
				EzKeyValue[] kvs = newMsg.getKVData("extra").getKeyValues();
				byte[] encbytes = null;
				for(int i=0;i<kvs.length;i++)
				{
					if(kvs[i].getName().equals("enc"))
					{
						encbytes = kvs[i].getBytes();
						break;
					}
				}
				if(encbytes == null)
					return null;
				//验证签名
				for(int i=0;i<kvs.length;i++)
				{
					if(kvs[i].getName().equals("sign"))
					{
						byte[] signbytes = kvs[i].getBytes();
						try {
							if(cryptionMode == 0)
							{//rsa
								if(false == RSA.verifySign(encbytes, signbytes, RSA.getPublicKey(pKeyB)))
									return null;
								else
									break;
							}
							else if(cryptionMode == 1)
							{//md5
								if(false == MD5.verifySign(encbytes, signbytes))
									return null;
								else
									break;								
							}
						} catch (Exception e) {
							e.printStackTrace();
							return null;
						}
					}
				}
				byte[] decbytes = RC4.doCrypt(encbytes, rc4key, RC4.DECRYPT_MODE);
				return EzMessageFactory.CreateMessageObject(decbytes);
			}
			default:
				return null;
		}
	}
	/*
	 * 密钥交换完成后，使用的编码函数；
	 * 普通加密处理，将指定的变量加密，生成新的EzMessage结果并返回；
	 * 如果失败返回null;
	 * bPackage表示是否整包加密；
	 */
	public EzMessage encode(EzMessage intent,int encType)
	{
		EzMessage newMsg = intent.clone();
		if(finishSetting == false)
			return newMsg;
		//
		switch(encType)
		{
			case ENC_NORMAL:
			{
				EzKeyValue[] kvs = newMsg.getKVData("extra").getKeyValues();
				newMsg.getKVData("encflags").setValue(ENC_NORMAL);
				if(kvs != null)
				{
					for(int i=0;i<kvs.length;i++)
					{
						if(kvs[i] == null)
							continue;
						for(int j=0;j<keyNames.length;j++)
						{
							if(keyNames[j].equalsIgnoreCase(kvs[i].getName()) == true)
							{//加密值
								byte[] valuebytes = kvs[i].valueToBytes();
								kvs[i].setEncValue(RC4.doCrypt(valuebytes, rc4key, RC4.ENCRYPT_MODE));
								break;
							}
						}
					}
				}
				return newMsg;
			}
			case ENC_PACKAGE:
			{
				byte[] packagebytes = newMsg.serializeToPB();
				newMsg.getKVData("encflags").setValue(ENC_PACKAGE);
				EzKeyValue[] kvs = new EzKeyValue[1];
				kvs[0] = new EzKeyValue("enc");
				kvs[0].setEncValue(RC4.doCrypt(packagebytes, rc4key, RC4.ENCRYPT_MODE));
				newMsg.getKVData("extra").setValue(kvs);
				return newMsg;
			}
			case ENC_PACKAGEANDSIGN:
			{
				byte[] packagebytes = newMsg.serializeToPB();
				newMsg.getKVData("encflags").setValue(ENC_PACKAGEANDSIGN);
				EzKeyValue[] kvs = new EzKeyValue[2];
				kvs[0] = new EzKeyValue("enc");
				byte[] encbytes = RC4.doCrypt(packagebytes, rc4key, RC4.ENCRYPT_MODE);
				kvs[0].setEncValue(encbytes);
				kvs[1] = new EzKeyValue("sign");
				try {
					if(cryptionMode == 0)
					{//rsa
						kvs[1].setEncValue(RSA.sign(encbytes, RSA.getPrivateKey(sKeyA)));
					}
					else if(cryptionMode == 1)
					{
						kvs[1].setEncValue(MD5.sign(encbytes));						
					}
				} catch (Exception e) {
					return null;
				}
				newMsg.getKVData("extra").setValue(kvs);
				return newMsg;
			}
			default:
				return null;
		}
	}
	/*
	 * 获得本地公钥
	 */
	public byte[] getPublicKeyA()
	{
		return pKeyA;
	}
	/*
	 * 获得本地密钥
	 */
	public byte[] getPrivateKeyA()
	{
		return sKeyA;
	}
	/*
	 * 获得对方公钥
	 */
	public byte[] getPublicKeyB()
	{
		return pKeyB;
	}

	public String getSmsRand()
	{
		return rand;
	}

	/*
	 * 第一步，初始化使用，如果没有 RSA公私钥，创建RSA公私钥
	 */
	public boolean generateLocalKey()
	{
		try {
			if(cryptionMode == 0)
			{//rsa
				Map<String, byte[]> keyMap;
				keyMap = RSA.generateKey();
				sKeyA = keyMap.get(RSA.PRIVATE_KEY);
				pKeyA = keyMap.get(RSA.PUBLIC_KEY);
			}
			else if(cryptionMode == 1)
			{
				BigInteger[] keyPair = new BigInteger[2];
				if(DHE.createKeyPair(keyPair) == false)
					return false;
				sKeyA = keyPair[0].toByteArray();
				pKeyA = keyPair[1].toByteArray();
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	/*
	 * 第二步，解密RC4Key,获得真正的RC4Key
	 */
	public byte[] decodeRc4Key(byte[] pkeybbytes,byte[] inRc4keySecret)
	{
		if(cryptionMode == 0)
		{//rsa
			try {
				return RSA.decrypt(inRc4keySecret, RSA.getPrivateKey(sKeyA));
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}
		else if(cryptionMode == 1)
		{//d-h
			return DHE.calcRealKey(new BigInteger(pkeybbytes), new BigInteger(sKeyA), inRc4keySecret);
		}
		else return null;
		
	}

	/*
	 * 第三步，密钥交互后使用，设置获得的信息,其中rc4key是解密之后的结果。
	 */	
	public void setInfomation(String inTid,String inMobile,String inRand,byte[] pkeybytes,byte[] inRc4key,String[] inKeyNames)
	{
		tid = inTid;
		mobile = inMobile;
		rand = inRand;
		rc4key = inRc4key;
		keyNames = inKeyNames;
		pKeyB = pkeybytes;
		finishSetting = true;
	}
}

