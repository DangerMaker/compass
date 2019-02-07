package com.ez08.support.util;

import java.io.ByteArrayOutputStream;

import android.os.Bundle;

public class EzKeyValue extends EzValue{

	//name
	private String  name;

	/**
	 */
	public EzKeyValue()
	{
		
	}
	//
	public EzKeyValue(String inName) {
		name = new String(inName);
	}
	//
	public EzKeyValue(String inName,EzValue inValue)
	{
		name = new String(inName);
		setValue(inValue);
	}
	
	public EzKeyValue(String inName,Object inValue)
	{
		name = new String(inName);
		if(inValue == null)
			return;
		setValue(inValue);
	}
	/**
	 * clone
	 */
	public EzKeyValue clone()
	{
		EzKeyValue var = new EzKeyValue();
		if(this.name != null)
			var.name = new String(this.name);
		var.setValue((EzValue)this);
		return var;
	}	
	
	/**
	 * write key value to output. content =  key + value;
	 */
	public boolean writeKeyValueTo(CodedOutputStream output)
	{
		try
		{
			if(name == null || name.equals(""))
				return false;
			ByteArrayOutputStream rawOutput = new ByteArrayOutputStream();
		    CodedOutputStream cop = CodedOutputStream.newInstance(rawOutput);
			//§Õkey
		    cop.writeStringNoTag(name);
			//§Õvalue(??valueType)
			writeValueTo(cop);
		    cop.flush();
		    byte[] bytearray = rawOutput.toByteArray();
		    rawOutput.close();
		    //real output,length + content.
		    output.writeInt32NoTag(bytearray.length);
		    output.writeRawBytes(bytearray);
		    return true;
		}
	    catch (Exception e)
		{
			e.printStackTrace(); 
			return false;
		}
		
	}
	
	/**
	 * read key value from input;
	 */
	public boolean readKeyValueFrom(CodedInputStream input)
	{
		try
		{
			int nLength = input.readInt32();
			if(nLength > 0)
			{
				//key
				name = input.readString();
				if(name == null)
					return false;
			    //value
				return readValueFrom(input);
			}
			else return false;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}
	}	

	
	public String getName()
	{
		if(name == null)
			return "";
		else return new String(name);
	}
	
	/**
	 * set the key name
	 */
	public void setName(String inName)
	{
		if(inName == null)
			name = inName;
		else 
			name = new String(inName);
	}
	
	public boolean equals(EzKeyValue inValue)
	{
		if(name == inValue.getName())
		{
			return ((EzValue)this).equals((EzValue)inValue);
		}
		else if(name ==null || inValue.getName() == null)
		{
			return false;
		}
		else if(name.equals(inValue.getName())==false)
		{
			return false;
		}
		else
		{
			return ((EzValue)this).equals((EzValue)inValue);
		}
	}

	/**
	 * description
	 */
	public String description(){
		StringBuffer strBuffer = new StringBuffer();
		//
		if(getName() == null || getName().equals(""))
			strBuffer.append("noname");
		else
			strBuffer.append(getName());
		strBuffer.append(" = {");
		//
		strBuffer.append(super.description());
		//
		strBuffer.append("};\r\n");
		return strBuffer.toString();
	}
	//use for android
	/**
	 * add to bundle
	 */
	public boolean addToBundle(Bundle bdl)
	{
		if(bdl == null || name == null)
			return false;
		switch(getType())
		{
			case VALUE_TYPE_INT32:
				bdl.putInt(name, getInt32());break;
			case VALUE_TYPE_INT64:
				bdl.putLong(name, getInt64());break;
			case VALUE_TYPE_BOOL:
				bdl.putBoolean(name, getBoolean());break;
			case VALUE_TYPE_ENUM:
				bdl.putInt(name, getInt32());break;
			case VALUE_TYPE_FLOAT:
				bdl.putFloat(name, getFloat());break;
			case VALUE_TYPE_DOUBLE:
				bdl.putDouble(name, getDouble());break;
			case VALUE_TYPE_STRING:
				bdl.putString(name, getString());break;
			case VALUE_TYPE_BYTES:
				bdl.putByteArray(name, getBytes());break;
			case VALUE_TYPE_STRINGS:
				bdl.putStringArray(name, getStrings());break;
			case VALUE_TYPE_INT32S:
				bdl.putIntArray(name, getInt32s());break;
			case VALUE_TYPE_INT64S:
				bdl.putLongArray(name, getInt64s());break;
			case VALUE_TYPE_FLOATS:
				bdl.putFloatArray(name, getFloats());break;
			case VALUE_TYPE_DOUBLES:
				bdl.putDoubleArray(name, getDoubles());break;
			case VALUE_TYPE_MESSAGE:
				if(getMessage() != null)
				{
					bdl.putSerializable(name,super.clone());
				}
				break;
			case VALUE_TYPE_MESSAGES:
				if(getMessages() != null)
				{
					bdl.putSerializable(name,super.clone());
				}
				break;
			case VALUE_TYPE_KEYVALUE:
				if(getKeyValue() != null)
				{
					bdl.putBundle(name,this.valueToBundle());
				}
				break;
			case VALUE_TYPE_KEYVALUES:
				if(getKeyValues() != null)
					bdl.putBundle(name,this.valueToBundle());
				break;
			case VALUE_TYPE_VALUES:
				if(getValues() != null)
				{
					bdl.putSerializable(name,super.clone());
				}
				break;
			default:
				return false;
		}
		return true;
	}
}