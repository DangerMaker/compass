package com.ez08.support.util;

public class CH6 {
	public static byte[] encodeCH6(byte[] inBytes)
	{
		if(inBytes==null || inBytes.length == 0)
			return null;
		int nLength = inBytes.length;
		int nNewByteNum = nLength/3*4;
		if(nLength%3>0)
			nNewByteNum += (nLength%3)+1;
		byte[] outBytes = new byte[nNewByteNum];
		byte byteValue;
		int nPosA,nPosB;
		int i = 0;
		for(i=0;i<nLength/3;i++)
		{
			nPosA = i*3;
			nPosB = i*4;
			byteValue = 0;
			byteValue |= (inBytes[nPosA+0]&0xC0) >> 2;
			byteValue |= (inBytes[nPosA+1]&0xC0) >> 4;
			byteValue |= (inBytes[nPosA+2]&0xC0) >> 6;
			//set
			outBytes[nPosB+0] = (byte)((inBytes[nPosA+0]&0x3F)|0x40);
			outBytes[nPosB+1] = (byte)((inBytes[nPosA+1]&0x3F)|0x40);
			outBytes[nPosB+2] = (byte)((inBytes[nPosA+2]&0x3F)|0x40);
			outBytes[nPosB+3] = (byte)(byteValue|0x40);
		}
		//nLength%3
		int nExNumb = nLength%3;
		if(nExNumb>0)
		{
			nPosA = i*3;
			nPosB = i*4;
			byteValue = 0;
			byteValue |= (inBytes[nPosA+0]&0xC0) >> 2;
			if(nExNumb>1)
				byteValue |= (inBytes[nPosA+1]&0xC0) >> 4;
			//set
			outBytes[nPosB++] = (byte)((inBytes[nPosA+0]&0x3F)|0x40);
			if(nExNumb>1)
				outBytes[nPosB++] = (byte)((inBytes[nPosA+1]&0x3F)|0x40);
			outBytes[nPosB] = (byte)(byteValue|0x40);
		}
		return outBytes;
	}
	/*
	 * 
	 */
	public static byte[] decodeCH6(byte[] inBytes)
	{
		if(inBytes==null || inBytes.length == 0 || (inBytes.length%4)==1)
			return null;
		int nLength = inBytes.length;
		int nNewByteNum = nLength/4*3;
		if(nLength%4>0)
			nNewByteNum += (nLength%4)-1;

		byte[] outBytes = new byte[nNewByteNum];
		byte byteValue;
		int nPosA,nPosB;
		int i = 0;
		for(i=0;i<nLength/4;i++)
		{
			nPosA = i*4;
			nPosB = i*3;
			byteValue = inBytes[nPosA+3];
			//set
			outBytes[nPosB+0] = (byte)((inBytes[nPosA+0]&0x3F)|((byteValue&0x30)<<2));
			outBytes[nPosB+1] = (byte)((inBytes[nPosA+1]&0x3F)|((byteValue&0x0C)<<4));
			outBytes[nPosB+2] = (byte)((inBytes[nPosA+2]&0x3F)|((byteValue&0x03)<<6));
		}
		//nLength%4
		int nExNumb = nLength%4;
		if(nExNumb>1)
		{
			nPosA = i*4;
			nPosB = i*3;
			byteValue = inBytes[nPosA+nExNumb-1];
			outBytes[nPosB++] = (byte)((inBytes[nPosA+0]&0x3F)|((byteValue&0x30)<<2));
			if(nExNumb>2)
				outBytes[nPosB++] = (byte)((inBytes[nPosA+1]&0x3F)|((byteValue&0x0C)<<4));
		}
		return outBytes;
	}
}
