package com.ez08.support.util;

import java.io.ByteArrayOutputStream;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Arrays;
import java.util.Vector;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.ez08.support.net.EzMessage;
import com.ez08.support.net.EzMessageFactory;

public class EzValue implements Externalizable{

	/*
	 * Value TYPE
	 */
	//INT
	public static final int VALUE_TYPE_INT32 = 0;  
	public static final int VALUE_TYPE_INT64 = 1;  
	public static final int VALUE_TYPE_BOOL = 2;  
	public static final int VALUE_TYPE_ENUM = 3;  
	//FLOAT OR DOUBLE
	public static final int VALUE_TYPE_FLOAT = 4; 
	public static final int VALUE_TYPE_DOUBLE = 5;
	//Length-delimited
	public static final int VALUE_TYPE_STRING = 6;
	public static final int VALUE_TYPE_BYTES = 7;
	public static final int VALUE_TYPE_INT32S = 8;
	public static final int VALUE_TYPE_INT64S = 9;
	public static final int VALUE_TYPE_FLOATS = 10;
	public static final int VALUE_TYPE_DOUBLES = 11;
	public static final int VALUE_TYPE_MESSAGE = 12;
	public static final int VALUE_TYPE_MESSAGES = 13;
	public static final int VALUE_TYPE_KEYVALUE = 14;
	public static final int VALUE_TYPE_KEYVALUES = 15;
	public static final int VALUE_TYPE_STRINGS = 16;
	public static final int VALUE_TYPE_VALUES = 17;
	//signed int
	public static final int VALUE_TYPE_SINT32 = 18;  
	public static final int VALUE_TYPE_SINT64 = 19; 
	public static final int VALUE_TYPE_SINT32S = 20;  
	public static final int VALUE_TYPE_SINT64S = 21;
	//unsigned int
	public static final int VALUE_TYPE_UINT32 = 22; 
	public static final int VALUE_TYPE_UINT64 = 23;  
	//encrytion
	public static final int VALUE_TYPE_ENCBYTES = 24;
	//only use for input and output
	public static final int VALUE_TYPE_NULL = 25;

	/**
	 * value	 
	 */
	private Object 		value;
	//Value Type
	private int valueType = 0;
	/**
	 */
	public EzValue()
	{
		valueType = VALUE_TYPE_INT32;
		value = 0;
	}

	/**
	 */
	public EzValue(int inValue)
	{
		valueType = VALUE_TYPE_INT32;
		value = inValue;
	}
	/**
	 */
	public EzValue(long inValue)
	{
		valueType = VALUE_TYPE_INT64;
		value = inValue;
	}
	/**
	 */
	public EzValue(boolean inValue)
	{
		valueType = VALUE_TYPE_BOOL;
		if(inValue == false)
			value = 0;
		else value = 1;
	}
	/**
	 */
	public EzValue(float inValue)
	{
		valueType = VALUE_TYPE_FLOAT;
		value = inValue;
	}
	/**
	 */
	public EzValue(double inValue)
	{
		valueType = VALUE_TYPE_DOUBLE;
		value = inValue;
	}
	/**
	 */
	public EzValue(String inValue)
	{
		valueType = VALUE_TYPE_STRING;
		if(inValue != null)
			value = ByteString.copyFromUtf8(inValue);
		else value = null;
	}
	public EzValue(String[] inValue)
	{
		valueType = VALUE_TYPE_STRINGS;
		if(inValue==null || inValue.length == 0)
			return;
		ByteString[] sValues = new ByteString[inValue.length]; 
		for(int i=0;i<sValues.length;i++)
		{
			if(inValue[i]!=null)
				sValues[i]= ByteString.copyFromUtf8(inValue[i]);
			else
				sValues[i] = ByteString.EMPTY;
		}
		value = sValues;
	}
	/**
	 * byte[] value
	 */
	public EzValue(byte[] inValue)
	{
		valueType = VALUE_TYPE_BYTES;
		if(inValue != null)
			value =  ByteString.copyFrom(inValue);
		else
			value = null;
	}
	/**
	 * int[] value
	 */
	public EzValue(int[] inValue)
	{
		valueType = VALUE_TYPE_INT32S;
		if(inValue != null)
			value = inValue.clone();
		else value = null;
	}
	/**
	 * long[] value
	 */
	public EzValue(long[] inValue)
	{
		valueType = VALUE_TYPE_INT64S;
		if(inValue != null)
			value = inValue.clone();
		else 
			value = null;
	}
	/**
	 * float[] value
	 */
	public EzValue(float[] inValue)
	{
		valueType = VALUE_TYPE_FLOATS;
		if(inValue != null)
			value = inValue.clone();
		else 
			value = null;
	}
	/**
	 * double[] value
	 */
	public EzValue(double[] inValue)
	{
		valueType = VALUE_TYPE_DOUBLES;
		if(inValue != null)
			value = inValue.clone();
		else 
			value = null;
	}
	/**
	 * EzMessage value
	 */
	public EzValue(EzMessage inMessage)
	{
		valueType = VALUE_TYPE_MESSAGE;
		if(inMessage == null)
			value = null;
		else 
			value = inMessage.clone();
	}
	/**
	 * EzMessage[] value
	 */
	public EzValue(EzMessage[] inMessages)
	{
		valueType = VALUE_TYPE_MESSAGES;
		EzMessage[] messages = null;
		if(inMessages == null || inMessages.length == 0)
			messages = null;
		else 
		{
			messages = new EzMessage[inMessages.length];
			for(int i=0;i<messages.length;i++)
			{
				if(inMessages[i] != null)
					messages[i] = inMessages[i].clone();
				else
					messages[i] = null;
			}
		}
		value = messages;
	}
	/**
	 * KeyValue value
	 */
	public EzValue(EzKeyValue inKeyValue)
	{
		valueType = VALUE_TYPE_KEYVALUE;
		if(inKeyValue == null)
			value = null;
		else 
			value = inKeyValue.clone();
	}

	/**
	 * EzKeyValue[] value
	 */
	public EzValue(EzKeyValue[] inKeyValues)
	{
		valueType = VALUE_TYPE_KEYVALUES;
		EzKeyValue[] keyValues = null;
		if(inKeyValues == null || inKeyValues.length == 0)
			keyValues = null;
		else 
		{
			keyValues = new EzKeyValue[inKeyValues.length];
			for(int i=0;i<inKeyValues.length;i++)
			{
				if(inKeyValues[i] != null)
					keyValues[i] = inKeyValues[i].clone();
				else
					keyValues[i] = null;
			}
		}
		value = keyValues;
	}
	/**
	 * EzValue[] value
	 */
	public EzValue(EzValue[] inValues)
	{
		valueType = VALUE_TYPE_VALUES;
		EzValue[] ezValues = null;
		if(inValues == null || inValues.length == 0)
			ezValues = null;
		else 
		{
			ezValues = new EzValue[inValues.length];
			for(int i=0;i<inValues.length;i++)
			{
				if(inValues[i] != null)
					ezValues[i] = inValues[i].clone();
				else 
					ezValues[i] = null;
			}
		}
		value = ezValues;
	}

	
	/**
	 * to string
	 */
	public String description(){
		StringBuffer strBuffer = new StringBuffer();
		//
		switch(getType())
		{
		case VALUE_TYPE_SINT32:
		case VALUE_TYPE_SINT64:
		case VALUE_TYPE_INT32:  
		case VALUE_TYPE_INT64:  
		case VALUE_TYPE_BOOL:  
		case VALUE_TYPE_ENUM:
			strBuffer.append(Long.toString(getInt64()));
			break;
		//FLOAT OR DOUBLE
		case VALUE_TYPE_FLOAT:
		case VALUE_TYPE_DOUBLE:
			strBuffer.append(Double.toString(getDouble()));
			break;
		case VALUE_TYPE_STRING:
			strBuffer.append(getString());
			break;
		case VALUE_TYPE_ENCBYTES:
		case VALUE_TYPE_BYTES:
			if(getBytes() != null )
			{  byte [] bytes = getBytes();
				strBuffer.append(getBytes().length + "," + getBytes().toString());
//				for(Byte b:bytes){
//					strBuffer.append(Integer.toHexString(b & 0xFF) + " "); 
//				}
				
			}
			else
			{
				strBuffer.append("null");
			}
			break;
		case VALUE_TYPE_SINT32S:
		case VALUE_TYPE_INT32S:
			if(getInt32s() != null)
			{
				strBuffer.append("{");
				for(int i=0;i<getInt32s().length;i++)
				{
					strBuffer.append(getInt32s()[i]);
					strBuffer.append(",");
				}
				strBuffer.append("}");
			}
			break;
		case VALUE_TYPE_SINT64S:
		case VALUE_TYPE_INT64S:
			if(getInt64s() != null)
			{
				strBuffer.append("{");
				for(int i=0;i<getInt64s().length;i++)
				{
					strBuffer.append(getInt64s()[i]);
					strBuffer.append(",");
				}
				strBuffer.append("}");
			}
			break;
		case VALUE_TYPE_FLOATS:
			if(getFloats() != null)
			{
				strBuffer.append("{");
				for(int i=0;i<getFloats().length;i++)
				{
					strBuffer.append(getFloats()[i]);
					strBuffer.append(",");
				}
				strBuffer.append("}");
			}
			break;
		case VALUE_TYPE_DOUBLES:
			if(getDoubles() != null)
			{
				strBuffer.append("{");
				for(int i=0;i<getDoubles().length;i++)
				{
					strBuffer.append(getDoubles()[i]);
					strBuffer.append(",");
				}
				strBuffer.append("}");
			}
			break;
			case VALUE_TYPE_STRINGS:
			if(getStrings() != null)
			{
				strBuffer.append("{");
				for(int i=0;i<getStrings().length;i++)
				{
					strBuffer.append(getStrings()[i]);
					strBuffer.append(",");
				}
				strBuffer.append("}");
			}
			break;
		case VALUE_TYPE_MESSAGE:
			if(getMessage() != null)
			{
				strBuffer.append("\r\n");
				strBuffer.append(getMessage().description());
				strBuffer.append("\r\n");
			}
			break;
		case VALUE_TYPE_MESSAGES:
			if(getMessages() != null)
			{
				for(int i=0;i<getMessages().length;i++)
				{
					strBuffer.append("\r\n");
					if(getMessages()[i] != null)
						strBuffer.append(getMessages()[i].description());
					else
						strBuffer.append("null");
				}
				strBuffer.append("\r\n");
			}
			break;
		case VALUE_TYPE_KEYVALUE:
			if(getKeyValue() != null)
			{
				strBuffer.append("\r\n");
				strBuffer.append(getKeyValue().description());
				strBuffer.append("\r\n");
			}
			break;
		case VALUE_TYPE_KEYVALUES:
			if(getKeyValues() != null)
			{
				for(int i=0;i<getKeyValues().length;i++)
				{
					strBuffer.append("\r\n");
					if(getKeyValues()[i] != null)
					{
						strBuffer.append(getKeyValues()[i].description());
					}
					else
						strBuffer.append("null");
				}
				strBuffer.append("\r\n");
			}
			break;
		case VALUE_TYPE_VALUES:
			if(getValues() != null)
			{
				for(int i=0;i<getValues().length;i++)
				{
					strBuffer.append("\r\n");
					if(getValues()[i] != null)
						strBuffer.append(getValues()[i].description());
					else
						strBuffer.append("null");
				}
				strBuffer.append("\r\n");
			}
			break;
		default:
			break;
		}
		return strBuffer.toString();
	}
	
	/**
	 * clone
	 */
	public EzValue clone()
	{
		EzValue out = new EzValue();
		out.setValue(this);
		return out;
	}
	/**
	 * get the value type
	 */
	public int getType()
	{
		return this.valueType;
	}
	/**
	 * get int32
	 */
	public int getInt32()
	{
		if(value == null)
			return 0;
		if(value.getClass() == Integer.class)
		{
			return ((Integer)value).intValue();
		}
		else if(value.getClass() == Long.class)
		{
			return (int)((Long)value).longValue();
		}
		else if(value.getClass() == Short.class)
		{
			return ((Short)value).intValue();
		}
		else return 0;
	}
	/**
	 * get int64
	 */
	public long getInt64()
	{
		if(value == null)
			return 0;
		if(value.getClass() == Long.class)
		{
			return ((Long)value).longValue();
		}
		else if(value.getClass() == Short.class)
		{
			return ((Short)value).intValue();
		}
		else if(value.getClass() == Integer.class)
		{
			return ((Integer)value).intValue();
		}
		else return 0;	
	}
	/**
	 * get boolean
	 */
	public boolean getBoolean()
	{
		if(value == null)
			return false;
		if(getInt32() == 0)
			return false;
		else return true;
	}
	/**
	 * get float
	 */
	public float getFloat()
	{
		if(value == null)
			return 0.0f;
		if(value.getClass()== Float.class)
		{
			return ((Float)value).floatValue();
		}
		else return 0.0f;
	}
	/**
	 * get double
	 */
	public double getDouble()
	{
		if(value == null)
			return 0.0;
		if(value.getClass() == Double.class)
		{
			return ((Double)value).doubleValue();
		}
		else return 0.0;
	}
	/**
	 * get string
	 */
	public String getString()
	{
		if(value == null)
			return null;
		if(value.getClass() == ByteString.class)
			return ((ByteString)value).toStringUtf8();
		else return null;
	}
	/**
	 * get string
	 */
	public String getStringWithDefault(String defaultv)
	{
		if(value == null)
			return defaultv;
		if(value.getClass() == ByteString.class)
			return ((ByteString)value).toStringUtf8();
		else return defaultv;
	}
	/**
	 * get bytestring
	 */
	public ByteString getByteString()
	{
		if(value == null)
			return null;
		if(value.getClass() == ByteString.class )
			return ((ByteString)value);
		else return null;
	}
	
	/**
	 * get bytestrings
	 */
	public ByteString[] getByteStrings()
	{
		if(value == null)
			return null;
		if(!value.getClass().isArray())
			return null;
		if(value.getClass().getComponentType() == ByteString.class )
			return ((ByteString[])value);
		else return null;
	}
	
	public String[] getStrings() {
		if(value == null)
			return null;
		if(!value.getClass().isArray())
			return null;
		if(value.getClass().getComponentType() == ByteString.class )
		{
			ByteString[] sValues = ((ByteString[])value);
			String[] strings = new String[sValues.length];
			for(int i=0;i<strings.length;i++)
			{
				strings[i] = sValues[i].toStringUtf8();
			}
			return strings;
		}
		else return null;		
	}

	/**
	 * get message
	 */
	public EzMessage getMessage()
	{
		if(value == null)
			return null;
		if(value.getClass() == EzMessage.class)
			return ((EzMessage)value);
		else return null;
	}
	
	/**
	 * get messages
	 */
	public EzMessage[] getMessages()
	{
		if(value == null)
			return null;
		if(!value.getClass().isArray())
			return null;
		if(value.getClass().getComponentType() == EzMessage.class )
			return ((EzMessage[])value);
		else return null;	
	}
	
	/**
	 * get EzKeyValue
	 */
	public EzKeyValue getKeyValue()
	{
		if(value == null)
			return null;
		if(value.getClass() == EzKeyValue.class)
			return ((EzKeyValue)value);
		else return null;	
	}

	/**
	 * get EzKeyValue[]
	 */
	public EzKeyValue[] getKeyValues()
	{
		if(value == null)
			return null;
		if(!value.getClass().isArray())
			return null;
		if(value.getClass().getComponentType() == EzKeyValue.class )
			return ((EzKeyValue[])value);
		else return null;
	}
	/**
	 * get EzValue[]
	 */
	public EzValue[] getValues()
	{
		if(value == null)
			return null;
		if(!value.getClass().isArray())
			return null;
		if(value.getClass().getComponentType() == EzValue.class )
			return ((EzValue[])value);
		else return null;
	}
	/**
	 * read value from the inputstream by inValueType;
	 */
	public boolean readValueFrom(CodedInputStream input,int inValueType)
	{
		try
		{
			valueType = inValueType;
			switch(inValueType)
			{
			case VALUE_TYPE_NULL:
				return false;//null,read nothing;
			case VALUE_TYPE_UINT32:  
			case VALUE_TYPE_INT32:  
			case VALUE_TYPE_BOOL:  
			case VALUE_TYPE_ENUM:
				value = input.readInt32();
				break;
			case VALUE_TYPE_INT64:  
			case VALUE_TYPE_UINT64:  
				value = input.readInt64();
				break;
			case VALUE_TYPE_SINT32:  
				value = input.readSInt32();
				break;
			case VALUE_TYPE_SINT64:  
				value = input.readSInt64();
				break;
			//FLOAT OR DOUBLE
			case VALUE_TYPE_FLOAT:
				value = input.readFloat();
				break;
			case VALUE_TYPE_DOUBLE:
				value = input.readDouble();
				break;
			case VALUE_TYPE_STRING:
			case VALUE_TYPE_BYTES:
			case VALUE_TYPE_ENCBYTES:
				value = input.readBytes();
				break;
			case VALUE_TYPE_STRINGS:
				{
					int nLength = input.readInt32();
					if(nLength > 0)
					{
						ByteString[] sValues = null;
						int nSize = input.readInt32();
						if(nSize > 0)
							sValues = new ByteString[nSize];
						for(int i=0;i<nSize;i++)
						{
							sValues[i] = input.readBytes();
						}
						value = sValues;
					}
				}
				break;
			case VALUE_TYPE_INT32S:
				{
					int nLength = input.readInt32();
					if(nLength > 0)
					{
						int[] nValues = null;
						int nSize = input.readInt32();
						if(nSize > 0)
							nValues = new int[nSize];
						for(int i=0;i<nSize;i++)
						{
							nValues[i] = input.readInt32();
						}
						value = nValues;
					}
				}
				break;
			case VALUE_TYPE_INT64S:
				{
					int nLength = input.readInt32();
					if(nLength > 0)
					{
						long[] lValues = null;
						int nSize = input.readInt32();
						if(nSize > 0)
							lValues = new long[nSize];
						for(int i=0;i<nSize;i++)
						{
							lValues[i] = input.readInt64();
						}
						value = lValues;
					}
				}
				break;
			case VALUE_TYPE_SINT32S:
			{
				int nLength = input.readInt32();
				if(nLength > 0)
				{
					int[] nValues = null;
					int nSize = input.readInt32();
					if(nSize > 0)
						nValues = new int[nSize];
					for(int i=0;i<nSize;i++)
					{
						nValues[i] = input.readSInt32();
					}
					value = nValues;
				}
			}
			break;
			case VALUE_TYPE_SINT64S:
			{
				int nLength = input.readInt32();
				if(nLength > 0)
				{
					long[] lValues = null;
					int nSize = input.readInt32();
					if(nSize > 0)
						lValues = new long[nSize];
					for(int i=0;i<nSize;i++)
					{
						lValues[i] = input.readSInt64();
					}
					value = lValues;
				}
			}
			break;
			case VALUE_TYPE_FLOATS:
				{
					int nLength = input.readInt32();
					if(nLength > 0)
					{
						float[] fValues = null;
						int nSize = input.readInt32();
						if(nSize > 0)
							fValues = new float[nSize];
						for(int i=0;i<nSize;i++)
						{
							fValues[i] = input.readFloat();
						}
						value = fValues;
					}
				}
				break;
			case VALUE_TYPE_DOUBLES:
			{
				int nLength = input.readInt32();
				if(nLength > 0)
				{
					double[] dValues = null;
					int nSize = input.readInt32();
					if(nSize > 0)
						dValues = new double[nSize];
					for(int i=0;i<nSize;i++)
					{
						dValues[i] = input.readDouble();
					}
					value = dValues;
				}
			}
			break;
			case VALUE_TYPE_MESSAGE:
			{
				ByteString msgContent = input.readBytes();
				if(msgContent != null && !msgContent.isEmpty())
				{
					value = EzMessageFactory.CreateMessageObject(msgContent.toByteArray());
				}
				else
					value = null;
			}
			break;
			case VALUE_TYPE_MESSAGES:
			{
				ByteString msgContent = input.readBytes();
				if(msgContent != null && !msgContent.isEmpty())
					value = EzMessageFactory.CreateRpMessageObjects(msgContent.toByteArray());
				else
					value = null;
			}
			break;
			case VALUE_TYPE_KEYVALUE:
			{
				EzKeyValue keyValue = new EzKeyValue();
				if(keyValue.readKeyValueFrom(input) == true)
					value = keyValue;
				else
					value = null;
			}
			break;
			case VALUE_TYPE_KEYVALUES:
			{
				int nLength = input.readInt32();
				if(nLength>0)
				{
					int nSize = input.readInt32();
					if(nSize>0)
					{
						EzKeyValue[] keyValues = new EzKeyValue[nSize];
						for(int i=0;i<nSize;i++)
						{
							EzKeyValue kv = new EzKeyValue();
							if(kv.readKeyValueFrom(input))
								keyValues[i] = kv;
							else keyValues[i] = null;
						}
						value = keyValues;
					}
					else
					{
						value = null;
					}
				}
			}
			break;
			case VALUE_TYPE_VALUES:
			{
				int nLength = input.readInt32();
				if(nLength>0)
				{
					int nSize = input.readInt32();
					if(nSize>0)
					{
						EzValue[] ezValues = new EzValue[nSize];
						for(int i=0;i<nSize;i++)
						{
							EzValue ev = new EzValue();
							if(ev.readValueFrom(input))
								ezValues[i] = ev;
							else ezValues[i] = null;
						}
						value = ezValues;
					}
					else
					{
						value = null;
					}
				}
			}
			break;
			default:
				return false;
			}
		    return true;
		}
		catch (Exception e)
		{
			e.printStackTrace(); 
			return false;
		}
	}
	//read value from input stream
	public boolean readValueFrom(CodedInputStream input)
	{
		try
		{
			if(input == null)
				return false;
			valueType = input.readInt32();
			return readValueFrom(input,valueType);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}
	}	
	/**
	 * write value to output stream
	 */
	public boolean writeValueTo(CodedOutputStream output)
	{
		try
		{
			if(output == null)
				return false;
			output.writeInt32NoTag(valueType);
			return writeValueToNoType(output);
		}
	    catch (Exception e)
		{
			e.printStackTrace(); 
			return false;
		}
		
	}
	/*
	 * write value to output stream without type infomation;
	 */
	public boolean writeValueToNoType(CodedOutputStream output)
	{
		try
		{
		    //
			switch(valueType)
			{
			case VALUE_TYPE_UINT32:
			case VALUE_TYPE_INT32:
			case VALUE_TYPE_BOOL:
			case VALUE_TYPE_ENUM:
				output.writeInt32NoTag(getInt32());
				break;
			case VALUE_TYPE_UINT64:
			case VALUE_TYPE_INT64:
				output.writeInt64NoTag(getInt64());
				break;
			case VALUE_TYPE_SINT32:  
				output.writeSInt32NoTag(getInt32());
				break;
			case VALUE_TYPE_SINT64:  
				output.writeSInt64NoTag(getInt64());
				break;
			//FLOAT OR DOUBLE
			case VALUE_TYPE_FLOAT:
				output.writeFloatNoTag(getFloat());
				break;
			case VALUE_TYPE_DOUBLE:
				output.writeDoubleNoTag(getDouble());
				break;
			case VALUE_TYPE_STRING:
			case VALUE_TYPE_BYTES:
			case VALUE_TYPE_ENCBYTES:
				if(getByteString() != null)
					output.writeBytesNoTag(getByteString());
				else
					output.writeInt32NoTag(0);//null
				break;
			case VALUE_TYPE_STRINGS:
				if(getByteStrings() != null)
				{
					ByteString[] sValues = getByteStrings();
					int nLength = 0;
					nLength += CodedOutputStream.computeInt32SizeNoTag(sValues.length);
					for(int i=0;i<sValues.length;i++)
					{
						nLength += CodedOutputStream.computeBytesSizeNoTag(sValues[i]);
					}
					output.writeInt32NoTag(nLength);
					output.writeInt32NoTag(sValues.length);
					for(int i=0;i<sValues.length;i++)
					{
						if(sValues[i] != null)
							output.writeBytesNoTag(sValues[i]);
						else
							output.writeBytesNoTag(ByteString.EMPTY);
					}
				}
				else
					output.writeInt32NoTag(0);//null
				break;
			case VALUE_TYPE_INT32S:
				if(getInt32s() != null)
				{
					int[] nValues = getInt32s();
					int nLength = 0;
					for(int i=0;i<nValues.length;i++)
					{
						nLength += CodedOutputStream.computeInt32SizeNoTag(nValues[i]);
					}
					if(nValues.length > 0)
						nLength += CodedOutputStream.computeInt32SizeNoTag(nValues.length);
					output.writeInt32NoTag(nLength);//bytes length
					output.writeInt32NoTag(nValues.length);//size
					for(int i=0;i<nValues.length;i++)
					{
						output.writeInt32NoTag(nValues[i]);
					}
				}
				else 
					output.writeInt32NoTag(0);//null
				break;
			case VALUE_TYPE_INT64S:
				if(getInt64s() != null)
				{
					long[] lValues = getInt64s();
					int nLength = 0;
					for(int i=0;i<lValues.length;i++)
					{
						nLength += CodedOutputStream.computeInt64SizeNoTag(lValues[i]);
					}
					if(lValues.length > 0)
						nLength += CodedOutputStream.computeInt32SizeNoTag(lValues.length);
					output.writeInt32NoTag(nLength);//length of bytes
					output.writeInt32NoTag(lValues.length);//size
					for(int i=0;i<lValues.length;i++)
					{
						output.writeInt64NoTag(lValues[i]);
					}
				}
				else 
					output.writeInt32NoTag(0);//null
				break;
			case VALUE_TYPE_SINT32S:
				if(getInt32s() != null)
				{
					int[] nValues = getInt32s();
					int nLength = 0;
					for(int i=0;i<nValues.length;i++)
					{
						nLength += CodedOutputStream.computeSInt32SizeNoTag(nValues[i]);
					}
					if(nValues.length > 0)
						nLength += CodedOutputStream.computeInt32SizeNoTag(nValues.length);
					output.writeInt32NoTag(nLength);//length of bytes
					output.writeInt32NoTag(nValues.length);//size
					for(int i=0;i<nValues.length;i++)
					{
						output.writeSInt32NoTag(nValues[i]);
					}
				}
				else 
					output.writeInt32NoTag(0);//null
				break;
			case VALUE_TYPE_SINT64S:
				if(getInt64s() != null)
				{
					long[] lValues = getInt64s();
					int nLength = 0;
					for(int i=0;i<lValues.length;i++)
					{
						nLength += CodedOutputStream.computeSInt64SizeNoTag(lValues[i]);
					}
					if(lValues.length > 0)
						nLength += CodedOutputStream.computeInt32SizeNoTag(lValues.length);
					output.writeInt32NoTag(nLength);//length of bytes
					output.writeInt32NoTag(lValues.length);//size
					for(int i=0;i<lValues.length;i++)
					{
						output.writeSInt64NoTag(lValues[i]);
					}
				}
				else 
					output.writeInt32NoTag(0);//null
				break;
			case VALUE_TYPE_FLOATS:
				if(getFloats() != null)
				{
					float[] fValues = getFloats();
					int nLength = 0;
					for(int i=0;i<fValues.length;i++)
					{
						nLength += CodedOutputStream.computeFloatSizeNoTag(fValues[i]);
					}
					if(fValues.length > 0)
						nLength += CodedOutputStream.computeInt32SizeNoTag(fValues.length);
					output.writeInt32NoTag(nLength);//length of bytes
					output.writeInt32NoTag(fValues.length);//size
					for(int i=0;i<fValues.length;i++)
					{
						output.writeFloatNoTag(fValues[i]);
					}
				}
				else 
					output.writeInt32NoTag(0);//null
				break;
			case VALUE_TYPE_DOUBLES:
				if(getDoubles() != null)
				{
					double[] dValues = getDoubles();
					int nLength = 0;
					for(int i=0;i<dValues.length;i++)
					{
						nLength += CodedOutputStream.computeDoubleSizeNoTag(dValues[i]);
					}
					if(dValues.length > 0)
						nLength += CodedOutputStream.computeInt32SizeNoTag(dValues.length);
					output.writeInt32NoTag(nLength);//length of bytes
					output.writeInt32NoTag(dValues.length);//size
					for(int i=0;i<dValues.length;i++)
					{
						output.writeDoubleNoTag(dValues[i]);
					}
				}
				else 
					output.writeInt32NoTag(0);//null
				break;
			case VALUE_TYPE_MESSAGE:
				if(getMessage() != null)
				{
					byte[] msgbytes = getMessage().serializeToPB();
					if(msgbytes == null || msgbytes.length == 0)
					{
						output.writeInt32NoTag(0);//null
					}
					else
					{
						output.writeInt32NoTag(msgbytes.length);//length of bytes
						output.writeRawBytes(msgbytes);
					}
				}
				else
					output.writeInt32NoTag(0);//null
				break;
			case VALUE_TYPE_MESSAGES:
				if(getMessages() != null)
				{
					EzMessage[] messages = getMessages();
					ByteArrayOutputStream rawOutput = new ByteArrayOutputStream();
				    CodedOutputStream cop = CodedOutputStream.newInstance(rawOutput);
				    cop.writeInt32NoTag(messages.length);//message size;
				    for(int i=0;i<messages.length;i++)
					{
						if(messages[i] != null)
						{
							byte[] msgbytes = messages[i].serializeToPB();
							if(msgbytes != null)
							{
								cop.writeInt32NoTag(msgbytes.length);
								cop.writeRawBytes(msgbytes);
							}
							else cop.writeInt32NoTag(0);
						}
						else
							cop.writeInt32NoTag(0);
					}
				    cop.flush();
				    byte[] bytearray = rawOutput.toByteArray();
				    rawOutput.close();
				    //real output,length + content.
				    output.writeInt32NoTag(bytearray.length);
				    output.writeRawBytes(bytearray);
				}
				else
					output.writeInt32NoTag(0);//null
				break;
			case VALUE_TYPE_KEYVALUE:
				if(getKeyValue() != null)
				{
					getKeyValue().writeKeyValueTo(output);
				}
				else
					output.writeInt32NoTag(0);//null
				break;
			case VALUE_TYPE_KEYVALUES:
				if(getKeyValues() != null)
				{
					EzKeyValue[] keyValues = getKeyValues();
					ByteArrayOutputStream rawOutput = new ByteArrayOutputStream();
				    CodedOutputStream cop = CodedOutputStream.newInstance(rawOutput);
				    cop.writeInt32NoTag(keyValues.length);//size;
				    for(int i=0;i<keyValues.length;i++)
					{
						if(keyValues[i] != null)
							keyValues[i].writeKeyValueTo(cop);
						else
							cop.writeInt32NoTag(0);
					}
				    cop.flush();
				    byte[] bytearray = rawOutput.toByteArray();
				    rawOutput.close();
				    //real output,length + content.
				    output.writeInt32NoTag(bytearray.length);
				    output.writeRawBytes(bytearray);
				}
				else
					output.writeInt32NoTag(0);//null
				break;
			case VALUE_TYPE_VALUES:
				if(getValues() != null)
				{
					EzValue[] ezValues = getValues();
					ByteArrayOutputStream rawOutput = new ByteArrayOutputStream();
				    CodedOutputStream cop = CodedOutputStream.newInstance(rawOutput);
				    cop.writeInt32NoTag(ezValues.length);//size;
				    for(int i=0;i<ezValues.length;i++)
					{
						if(ezValues[i] != null)
							ezValues[i].writeValueTo(cop);
						else
						{
							cop.writeInt32NoTag(VALUE_TYPE_NULL);
						}
					}
				    cop.flush();
				    byte[] bytearray = rawOutput.toByteArray();
				    rawOutput.close();
				    //real output,length + content.
				    output.writeInt32NoTag(bytearray.length);
				    output.writeRawBytes(bytearray);
				}
				else
					output.writeInt32NoTag(0);//null
				break;
			default:
				break;
			}
		    output.flush();
		    return true;
		}
		catch (Exception e)
		{
			e.printStackTrace(); 
			return false;
		}
	}
	
	/**
	 * get bytes,byte[]
	 */
	public byte[] getBytes()
	{
		if(value == null)
			return null;
		if(value.getClass() == ByteString.class )
			return ((ByteString)value).toByteArray();
		else return null;
	}
	
	/**
	 * get bytes,byte[]
	 */
	public byte[] getEncBytes()
	{
		if(value == null)
			return null;
		if(value.getClass() == ByteString.class )
			return ((ByteString)value).toByteArray();
		else return null;
	}
	
	/**
	 * get decoded value by the RC4Key;
	 */
	public EzValue getDecValue(String RC4Key)
	{
        byte[] outputData = RC4.doCrypt(getEncBytes(), RC4Key.getBytes() ,RC4.ENCRYPT_MODE);
        if(outputData != null)
        {
        	EzValue v = new EzValue();
        	v.valueFromBytes(outputData);
        	return v;
        }
		return null;
	}
	/**
	 * get int32s,int[]
	 */
	public int[] getInt32s()
	{
		if(value == null)
			return null;
		if(!value.getClass().isArray())
			return null;
		if(value.getClass().getComponentType() == int.class)
			return (int[])value;
		else return null;

	}
	
	/**
	 * get int64s,long[]
	 */
	public long[] getInt64s()
	{
		if(value == null)
			return null;
		if(!value.getClass().isArray())
			return null;
		if(value.getClass().getComponentType() == long.class)
			return (long[])value;
		else return null;
	}
	/**
	 * get floats,float[]
	 */
	public float[] getFloats()
	{
		if(value == null)
			return null;
		if(!value.getClass().isArray())
			return null;
		if(value.getClass().getComponentType() == float.class)
			return (float[])value;
		else return null;
	}
	/**
	 * get doubles,double[]
	 */
	public double[] getDoubles()
	{
		if(value == null)
			return null;
		if(!value.getClass().isArray())
			return null;
		if(value.getClass().getComponentType() == double.class)
			return (double[])value;
		else return null;

	}

	/**
	 * reset
	 */
	public void reset()
	{
		valueType = VALUE_TYPE_INT32;
		value = 0;
	}
	/**
	 * set int value
	 */
	public void setValue(int inValue)
	{
		reset();
		valueType = VALUE_TYPE_INT32;
		value = inValue;
	}
	/**
	 * set int value with signed flag;
	 */
	public void setValue(int inValue,boolean bSigned)
	{
		reset();
		if(bSigned ==false)
			valueType = VALUE_TYPE_INT32;
		else
			valueType = VALUE_TYPE_SINT32;			
		value = inValue;
	}
	/**
	 * set long value;
	 */
	public void setValue(long inValue)
	{
		reset();
		valueType = VALUE_TYPE_INT64;
		value = inValue;
	}
	/**
	 * set long value with signed flag;
	 */
	public void setValue(long inValue,boolean bSigned)
	{
		reset();
		if(bSigned == false)
			valueType = VALUE_TYPE_INT64;
		else
			valueType = VALUE_TYPE_SINT64;
		//
		value = inValue;
	}
	/**
	 * set boolean value;
	 */
	public void setValue(boolean inValue)
	{
		reset();
		valueType = VALUE_TYPE_BOOL;
		if(inValue == false)
			value = 0;
		else value = 1;
	}
	/**
	 * set float value;
	 */
	public void setValue(float inValue)
	{
		reset();
		valueType = VALUE_TYPE_FLOAT;
		value = inValue;
	}
	/**
	 * set double value;
	 */
	public void setValue(double inValue)
	{
		reset();
		valueType = VALUE_TYPE_DOUBLE;
		value = inValue;
	}
	/**
	 * set string value;
	 */
	public void setValue(String inValue)
	{
		reset();
		valueType = VALUE_TYPE_STRING;
		if(inValue != null)
		{
			value= ByteString.copyFromUtf8(inValue);
		}
		else
		{
			value = null;
		}
	}
	/**
	 * string[];
	 */
	public void setValue(String[] inValue)
	{
		reset();
		valueType = VALUE_TYPE_STRINGS;
		if(inValue==null || inValue.length == 0)
			return;
		ByteString[] sValues = new ByteString[inValue.length]; 
		for(int i=0;i<sValues.length;i++)
		{
			if(inValue[i] != null)
			{
				sValues[i]= ByteString.copyFromUtf8(inValue[i]);
			}
			else
			{
				sValues[i] = ByteString.EMPTY;
			}
		}
		value = sValues;
	}
	/**
	 * set byte[] value;
	 */
	public void setValue(byte[] inValue)
	{
		reset();
		valueType = VALUE_TYPE_BYTES;
		if(inValue!=null)
			value =  ByteString.copyFrom(inValue);
		else
			value = null;
	}
	/**
	 * set ByteString value;
	 */
	public void setValue(ByteString inValue)
	{
		reset();
		valueType = VALUE_TYPE_BYTES;
		if(inValue != null)
			value =  ByteString.copyFrom(inValue.toByteArray());
		else 
			value = null;
	}
	
	/*
	 * SET ENC VALUE
	 */
	public void setEncValue(byte[] inValue)
	{
		reset();
		valueType = VALUE_TYPE_ENCBYTES;
		if(inValue != null)
			value =  ByteString.copyFrom(inValue);
		else 
			value = null;
	}

	/**
	 * set int[] value;
	 */
	public void setValue(int[] inValue)
	{
		reset();
		valueType = VALUE_TYPE_INT32S;
		if(inValue != null)
			value = inValue.clone();
		else value = null;
	}
	/**
	 * set int[] value with signed flag;
	 */
	public void setValue(int[] inValue,boolean bSigned)
	{
		reset();
		if(bSigned == false)
			valueType = VALUE_TYPE_INT32S;
		else
			valueType = VALUE_TYPE_SINT32S;
		if(inValue != null)
			value = inValue.clone();
		else value = null;
	}

	/**
	 * set long[] value;
	 */
	public void setValue(long[] inValue)
	{
		reset();
		valueType = VALUE_TYPE_INT64S;
		if(inValue != null)
			value = inValue.clone();
		else
			value = null;
	}
	/**
	 * set long[] value with signed flag;
	 */
	public void setValue(long[] inValue,boolean bSigned)
	{
		reset();
		if(bSigned == false)
			valueType = VALUE_TYPE_INT64S;
		else
			valueType = VALUE_TYPE_SINT64S;
		if(inValue != null)
			value = inValue.clone();
		else
			value = null;
	}
	/**
	 * set float[] value;
	 */
	public void setValue(float[] inValue)
	{
		reset();
		valueType = VALUE_TYPE_FLOATS;
		if(inValue != null)
			value = inValue.clone();
		else
			value = null;
	}
	/**
	 * set double[] value;
	 */
	public void setValue(double[] inValue)
	{
		reset();
		valueType = VALUE_TYPE_DOUBLES;
		if(inValue != null)
			value = inValue.clone();
		else
			value = null;
	}
	
	/**
	 * set EzMessage value;
	 */
	public void setValue(EzMessage inValue)
	{
		reset();
		valueType = VALUE_TYPE_MESSAGE;
		if(inValue!= null)
			value = inValue.clone();
		else
			value = null;
	}
	
	/**
	 * set EzMessage[] value;
	 */
	public void setValue(EzMessage[] inValue)
	{
		reset();
		valueType = VALUE_TYPE_MESSAGES;
		if(inValue == null || inValue.length == 0)
			value = null;
		else 
		{
			EzMessage[] messages = new EzMessage[inValue.length];
			for(int i=0;i<messages.length;i++)
			{
				if(inValue[i] != null)
				{
					messages[i] = inValue[i].clone();
				}
				else
				{
					messages[i] = null;
				}
			}
			value = messages;
		}
	}
	
	/**
	 * set EzKeyValue value;
	 */
	public void setValue(EzKeyValue inKeyValue)
	{
		reset();
		valueType = VALUE_TYPE_KEYVALUE;
		if(inKeyValue == null)
			value = null;
		else 
			value = inKeyValue.clone();
	}

	/**
	 * set EzKeyValue[] value;
	 */
	public void setValue(EzKeyValue[] inKeyValues)
	{
		reset();
		valueType = VALUE_TYPE_KEYVALUES;
		if(inKeyValues == null || inKeyValues.length == 0)
			value = null;
		else 
		{
			EzKeyValue[] keyValues = new EzKeyValue[inKeyValues.length];
			for(int i=0;i<inKeyValues.length;i++)
			{
				if(inKeyValues[i] != null)
					keyValues[i] = inKeyValues[i].clone();
				else
					keyValues[i] = null;
			}
			value = keyValues;
		}
	}

	
	/**
	 * set EzValue;
	 */
	public void setValue(EzValue inValue)
	{
		reset();
		if(inValue == null)
			return;
		this.valueType = inValue.getType();
		switch(inValue.valueType)
		{
		case VALUE_TYPE_UINT64:  
		case VALUE_TYPE_SINT64:  
		case VALUE_TYPE_INT64:
			value = inValue.getInt64();
			break;
		case VALUE_TYPE_UINT32:  
		case VALUE_TYPE_SINT32:  
		case VALUE_TYPE_INT32:  
		case VALUE_TYPE_BOOL:  
		case VALUE_TYPE_ENUM:
			value = inValue.getInt32();
			break;
		//FLOAT OR DOUBLE
		case VALUE_TYPE_FLOAT:
			value = inValue.getFloat();
			break;
		case VALUE_TYPE_DOUBLE:
			value = inValue.getDouble();
			break;
		case VALUE_TYPE_STRING:
		case VALUE_TYPE_BYTES:
		case VALUE_TYPE_ENCBYTES:
			if(inValue.getByteString() != null)
				value = ByteString.copyFrom(inValue.getByteString().toByteArray());
			else
				value = null;
			break;
		case VALUE_TYPE_STRINGS:
			if(inValue.getByteStrings() != null && inValue.getByteStrings().length > 0)
			{	
				ByteString[] sValues = new ByteString[inValue.getByteStrings().length];
				ByteString[] insValues = inValue.getByteStrings();
				for(int i=0;i<inValue.getByteStrings().length;i++)
				{
					if(insValues[i]!=null)
					{
						sValues[i]= ByteString.copyFrom(insValues[i].toByteArray());
					}
					else
					{
						sValues[i] = ByteString.EMPTY;
					}
				}
				value = sValues;
			}
			break;
		case VALUE_TYPE_INT32S:
		case VALUE_TYPE_SINT32S:
			if(inValue.getInt32s() != null)
				value = inValue.getInt32s().clone();
			else
				value = null;
			break;
		case VALUE_TYPE_INT64S:
		case VALUE_TYPE_SINT64S:
			if(inValue.getInt64s() != null)
				value = inValue.getInt64s().clone();
			else
				value = null;
			break;
		case VALUE_TYPE_FLOATS:
			if(inValue.getFloats() != null)
				value = inValue.getFloats().clone();
			else
				value = null;
			break;
		case VALUE_TYPE_DOUBLES:
			if(inValue.getDoubles() != null)
				value = inValue.getDoubles().clone();
			else
				value = null;
			break;
		case VALUE_TYPE_MESSAGE:
			if(inValue.getMessage() != null)
				value = inValue.getMessage().clone();
			else
				value = null;
			break;
		case VALUE_TYPE_MESSAGES:
			setValue(inValue.getMessages());
			break;
		case VALUE_TYPE_KEYVALUE:
			setValue(inValue.getKeyValue());
			break;
		case VALUE_TYPE_KEYVALUES:
			setValue(inValue.getKeyValues());
			break;
		case VALUE_TYPE_VALUES:
			setValue(inValue.getValues());
			break;
		default:
			break;
		}
	}
	/**
	 * set EzValue[] value;
	 */
	public void setValue(EzValue[] inValues)
	{
		reset();
		valueType = VALUE_TYPE_VALUES;
		if(inValues == null || inValues.length == 0)
			value = null;
		else 
		{
			EzValue[] ezValues = new EzValue[inValues.length];
			for(int i=0;i<inValues.length;i++)
			{
				if(inValues[i] != null)
					ezValues[i] = inValues[i].clone();
				else
					ezValues[i] = null;
			}
			value = ezValues;
		}
	}
	
	/**
	 * value from bytes
	 */
	public boolean valueFromBytes(byte[] bytes) {
		if(bytes == null)
			return false;
		CodedInputStream input = CodedInputStream.newInstance(bytes);
		return readValueFrom(input);
	}
	/**
	 * value to bytes
	 */
	public byte[] valueToBytes() {
		try{
			ByteArrayOutputStream rawOutput = new ByteArrayOutputStream();
		    CodedOutputStream output = CodedOutputStream.newInstance(rawOutput);
		    if(writeValueTo(output)==false)
		    	return null;
		    output.flush();
		    byte[] pbbytes = rawOutput.toByteArray();
		    rawOutput.close();
		    return pbbytes;
		}
		catch (Exception e)
	    {
	        e.printStackTrace(); 
	        return null;
	    }
	}
	
	/**
	 * set Object value;
	 */
	public boolean setValue(Object inValue)
	{
		if(inValue == null)
		{
			value = null;
			return false;
		}
		if(!inValue.getClass().isArray())
		{//
			Class cl = inValue.getClass();
			if(cl == Byte.class)
			{
				setValue(((Byte)inValue).intValue());
			}
			else if(cl == Boolean.class)
			{
				setValue(((Boolean)inValue).booleanValue());
			}
			else if(cl == Character.class)
			{
				setValue((int)((Character)inValue).charValue());
			}
			else if(cl == Long.class)
			{
				setValue(((Long)inValue).longValue());
			}
			else if(cl == Short.class)
			{
				setValue(((Short)inValue).intValue());
			}
			else if(cl == Integer.class)
			{
				setValue(((Integer)inValue).intValue());
			}
			else if(cl == Float.class)
			{
				setValue(((Float)inValue).floatValue());
			}
			else if(cl == Double.class)
			{
				setValue(((Double)inValue).doubleValue());
			}
			else if(cl == String.class)
			{
				setValue((String)inValue);
			}
			else if(cl == EzValue.class)
			{
				setValue((EzValue)inValue);
			}
			else if(cl == EzKeyValue.class)
			{
				setValue((EzKeyValue)inValue);
			}
			else if(cl == EzMessage.class)
			{
				setValue((EzMessage)inValue);
			}
			else if(cl == ByteString.class)
			{
				setValue((ByteString)inValue);
			}
			else if(cl == Bundle.class)
			{
				valueFromBundle((Bundle)inValue);
			}
			else
			{
				setValue(inValue.toString());
				return false;
			}
		}
		else
		{
			Class cl = inValue.getClass().getComponentType();
			if(cl == byte.class)
			{
				setValue((byte[])inValue);
			}
			else if(cl == char.class)
			{
				setValue((byte[])inValue);
			}
			else if(cl == short.class)
			{
				short[] shortArray = (short[])inValue;
				int[] intArray = new int[shortArray.length];
				for(int i=0;i<intArray.length;i++)
				{
					intArray[i] = shortArray[i];
				}
				setValue(intArray);
			}
			else if(cl == boolean.class)
			{
				boolean[] boolArray = (boolean[])inValue;
				int[] intArray = new int[boolArray.length];
				for(int i=0;i<intArray.length;i++)
				{
					intArray[i] = boolArray[i]?1:0;
				}
				setValue(intArray);
			}
			else if(cl == long.class)
			{
				setValue((long[])inValue);
			}
			else if(cl == int.class)
			{
				setValue((int[])inValue);
			}
			else if(cl == float.class)
			{
				setValue((float[])inValue);
			}
			else if(cl == double.class)
			{
				setValue((double[])inValue);
			}
			else if(cl == String.class)
			{
				setValue((String[])inValue);
			}
			else if(cl == EzValue.class)
			{
				setValue((EzValue[])inValue);
			}
			else if(cl == EzKeyValue.class)
			{
				setValue((EzKeyValue[])inValue);
			}
			else if(cl == EzMessage.class)
			{
				setValue((EzMessage[])inValue);
			}
			else
			{
				setValue(inValue.toString());
				return false;
			}
		}
		return true;
	}
	//
	/**
	 * equals
	 */
	public boolean equals(EzValue inValue)
	{
		if(inValue == null )
			return false;
		if(this.getType() != inValue.getType())
			return false;
		//
		switch(inValue.valueType)
		{
		case VALUE_TYPE_UINT64:  
		case VALUE_TYPE_SINT64:  
		case VALUE_TYPE_INT64:
			if(this.getInt64() == inValue.getInt64())
				return true;
			else return false;
		case VALUE_TYPE_UINT32:  
		case VALUE_TYPE_SINT32:  
		case VALUE_TYPE_INT32:  
		case VALUE_TYPE_BOOL:  
		case VALUE_TYPE_ENUM:
			if(this.getInt32() == inValue.getInt32())
				return true;
			else return false;
		//FLOAT OR DOUBLE
		case VALUE_TYPE_FLOAT:
			if(this.getFloat() == inValue.getFloat())
				return true;
			else return false;
		case VALUE_TYPE_DOUBLE:
			if(this.getDouble() == inValue.getDouble())
				return true;
			else return false;
		case VALUE_TYPE_STRING:
		case VALUE_TYPE_BYTES:
		case VALUE_TYPE_ENCBYTES:
			if(this.getByteString() == null && inValue.getByteString() == null)
			{
				return true;
			}
			else if(this.getByteString() == null || inValue.getByteString() == null)
			{
				return false;
			}
			else
				return this.getByteString().equals(inValue.getByteString());
		case VALUE_TYPE_STRINGS:
			if(this.getByteStrings() == inValue.getByteStrings())
				return true;
			else if(this.getByteStrings() == null || inValue.getByteStrings() == null)
			{
				return false;
			}
			else if(this.getByteStrings().length != inValue.getByteStrings().length)
				return false;
			else 
			{
				for(int i=0;i<this.getByteStrings().length;i++)
				{
					if( this.getByteStrings()[i].equals(inValue.getByteStrings()[i]) == false)
						return false;
				}
				return true;
			}
		case VALUE_TYPE_INT32S:
		case VALUE_TYPE_SINT32S:
			if(this.getInt32s() == inValue.getInt32s())
				return true;
			else if(this.getInt32s() == null || inValue.getInt32s() == null)
				return false;
			else
			{
				return Arrays.equals(this.getInt32s(),inValue.getInt32s());
			}
		case VALUE_TYPE_INT64S:
		case VALUE_TYPE_SINT64S:
			if(this.getInt64s() == inValue.getInt64s())
				return true;
			else if(this.getInt64s() == null || inValue.getInt64s() == null)
				return false;
			else
			{
				return Arrays.equals(this.getInt64s(),inValue.getInt64s());
			}
		case VALUE_TYPE_FLOATS:
			if(this.getFloats() == inValue.getFloats())
				return true;
			else if(this.getFloats() == null || inValue.getFloats() == null)
				return false;
			else
			{
				return Arrays.equals(this.getFloats(),inValue.getFloats());
			}
		case VALUE_TYPE_DOUBLES:
			if(this.getDoubles() == inValue.getDoubles())
				return true;
			else if(this.getDoubles() == null || inValue.getDoubles() == null)
				return false;
			else
			{
				return Arrays.equals(this.getDoubles(),inValue.getDoubles());
			}
		case VALUE_TYPE_MESSAGE:
			if(this.getMessage() == inValue.getMessage())
				return true;
			else if(this.getMessage() == null || inValue.getMessage() == null)
				return false;
			else
			{
				return this.getMessage().equals(inValue.getMessage());
			}
		case VALUE_TYPE_MESSAGES:
			if(this.getMessages() == inValue.getMessages())
				return true;
			else if(this.getMessages() == null || inValue.getMessages() == null)
				return false;
			else if(this.getMessages().length != inValue.getMessages().length)
				return false;
			else
			{
				for(int i=0;i<this.getMessages().length;i++)
				{
					if(this.getMessages()[i] == inValue.getMessages()[i])
						continue;
					else if(this.getMessages()[i] == null || inValue.getMessages()[i] == null)
						return false;
					else if(this.getMessages()[i].equals(inValue.getMessages()[i]) == false)
						return false;
				}
				return true;
			}
		case VALUE_TYPE_KEYVALUE:
			if(this.getKeyValue() == inValue.getKeyValue())
				return true;
			else if(this.getKeyValue() == null || inValue.getKeyValue() == null)
				return false;
			else
			{
				return this.getKeyValue().equals(inValue.getKeyValue());
			}
		case VALUE_TYPE_KEYVALUES:
			if(this.getKeyValues() == inValue.getKeyValues())
				return true;
			else if(this.getKeyValues() == null || inValue.getKeyValues() == null)
				return false;
			else if(this.getKeyValues().length != inValue.getKeyValues().length)
				return false;
			else
			{
				for(int i=0;i<this.getKeyValues().length;i++)
				{
					if(this.getKeyValues()[i] == inValue.getKeyValues()[i])
						continue;
					else if(this.getKeyValues()[i] == null || inValue.getKeyValues()[i] == null)
						return false;
					else if(this.getKeyValues()[i].equals(inValue.getKeyValues()[i]) == false)
						return false;
				}
				return true;
			}
		case VALUE_TYPE_VALUES:
			if(this.getValues() == inValue.getValues())
				return true;
			else if(this.getValues() == null || inValue.getValues() == null)
				return false;
			else if(this.getValues().length != inValue.getValues().length)
				return false;
			else
			{
				for(int i=0;i<this.getValues().length;i++)
				{
					if(this.getValues()[i] == inValue.getValues()[i])
						continue;
					else if(this.getValues()[i] == null || inValue.getValues()[i] == null)
						return false;
					else if(this.getValues()[i].equals(inValue.getValues()[i]) == false)
						return false;
				}
				return true;
			}
		default:
			return false;
		}
	}

	public void writeExternal(ObjectOutput out) throws IOException {
		byte[] content = this.valueToBytes();
		out.writeInt(content.length);
		out.write(this.valueToBytes());
	}

	public void readExternal(ObjectInput in) throws IOException,
			ClassNotFoundException {
		int length = in.readInt();
		byte[] content = new byte[length];
		in.readFully(content);
		valueFromBytes(content);
	}
	
	/**
	 * Bundle and EzValue???
	 */
	public Bundle valueToBundle()
	{
		switch(valueType)
		{
		case VALUE_TYPE_KEYVALUE:
			{
				if(getKeyValue() != null)
				{
					Bundle bdl = new Bundle();
					getKeyValue().addToBundle(bdl);
					return bdl;
				}
				else
					return null;
			}
		case VALUE_TYPE_KEYVALUES:
			if(getKeyValues() != null)
			{
				Bundle bdl = new Bundle();
				for(int i=0;i<getKeyValues().length;i++)
				{
					if(getKeyValues()[i] != null)
						getKeyValues()[i].addToBundle(bdl);
				}
				return bdl;				
			}
			else
				return null;
		default:
			return null;
		}
	}
	/**
	 * Bundle and EzValue???
	 */
	public boolean valueFromBundle(Bundle bdl)
	{
		if(bdl == null || bdl.size() <= 0)
			return false;
		Vector<EzKeyValue> kvs = new Vector<EzKeyValue>();
    	if(bdl != null  && !bdl.isEmpty())
    	{
    		String[] keys = new String[bdl.size()];
    		bdl.keySet().toArray(keys);
    		for(int i=0;i<keys.length;i++)
    		{
    			Object value = bdl.get(keys[i]);
				EzKeyValue keyval = new EzKeyValue(keys[i]);
				if(keyval.setValue(value))
				{
					kvs.add(keyval);
				}				
    		}
    		if(kvs.size() > 0)
    		{
    			EzKeyValue[] keyvalues = new EzKeyValue[kvs.size()];
    			kvs.toArray(keyvalues);
    			setValue(keyvalues);
    		}
    	}
		return true;
	}
	/**
	 * add to bundle
	 */
	public boolean addToIntent(String key, Intent intent)
	{
		if(intent == null || key == null)
			return false;
		switch(getType())
		{
			case VALUE_TYPE_INT32:
				intent.putExtra(key, getInt32());break;
			case VALUE_TYPE_INT64:
				intent.putExtra(key, getInt64());break;
			case VALUE_TYPE_BOOL:
				intent.putExtra(key, getBoolean());break;
			case VALUE_TYPE_ENUM:
				intent.putExtra(key, getInt32());break;
			case VALUE_TYPE_FLOAT:
				intent.putExtra(key, getFloat());break;
			case VALUE_TYPE_DOUBLE:
				intent.putExtra(key, getDouble());break;
			case VALUE_TYPE_STRING:
				intent.putExtra(key, getString());break;
			case VALUE_TYPE_BYTES:
				intent.putExtra(key, getBytes());break;
			case VALUE_TYPE_STRINGS:
				intent.putExtra(key, getStrings());break;
			case VALUE_TYPE_INT32S:
				intent.putExtra(key, getInt32s());break;
			case VALUE_TYPE_INT64S:
				intent.putExtra(key, getInt64s());break;
			case VALUE_TYPE_FLOATS:
				intent.putExtra(key, getFloats());break;
			case VALUE_TYPE_DOUBLES:
				intent.putExtra(key, getDoubles());break;
			case VALUE_TYPE_MESSAGE:
				if(getMessage() != null)
					intent.putExtra(key, getMessage().serializeToPB());
				break;
			case VALUE_TYPE_MESSAGES:
				if(getMessages() != null)
				{
					try{
						byte[] bytemsgs = valueToBytes();
						intent.putExtra(key,bytemsgs);

					}
					catch (Exception e)
				    {
				        e.printStackTrace(); 
				    }
				}
				break;
			case VALUE_TYPE_KEYVALUE:
				if(getKeyValue() != null)
					intent.putExtra(key,this.valueToBundle());
				break;
			case VALUE_TYPE_KEYVALUES:
				if(getKeyValues() != null)
					intent.putExtra(key,this.valueToBundle());
				break;
			case VALUE_TYPE_VALUES:
				if(getValues() != null)
				{
					intent.putExtra(key,this.valueToBytes());
				}
				break;
			default:
				return false;
		}
		return true;
	}
	
}
