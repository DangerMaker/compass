package com.ez08.support.net;

import java.io.ByteArrayOutputStream;
import java.io.StringWriter;
import java.util.Vector;

import com.ez08.support.util.CodedInputStream;
import com.ez08.support.util.CodedOutputStream;
import com.ez08.support.util.WireFormat;


public class EzMessage {
	/**
	 * 
	 */
	private String 	name;
	/**
	 * 
	 */
	private int	 	id;
	/**
	 * 
	 */
	private Vector<EzCommField> kvData;
	
	
	
	/**
	 * new from the proto string;
	 */
	public EzMessage(String strProto) {
		if(kvData == null)
			kvData = new Vector<EzCommField>();
		deSerializeFromProto(strProto);
	}
	
	/**
	 * ���캯��,��proto�ַ���
	 */
	public EzMessage(){
		if(kvData == null)
			kvData = new Vector<EzCommField>();
	}
	
	public EzMessage(byte[] indata){
		EzMessage inMsg = EzMessageFactory.CreateMessageObject(indata);
		if(kvData == null)
			kvData = new Vector<EzCommField>();
		if(inMsg != null)
		{
			this.id = inMsg.id;
			this.name = new String(inMsg.name);
			for(int i=0;i<inMsg.kvData.size();i++)
			{
				if(inMsg.kvData.get(i) == null)
				{
					continue;
				}
				else
				{
					this.kvData.add(inMsg.kvData.get(i).clone());
				}
			}			
		}
	}
	
	/**
	 * ��¡һ��
	 */
	public EzMessage clone()
	{
		EzMessage msgnew = new EzMessage();
		msgnew.id = this.id;
		msgnew.name = new String(this.name);
		for(int i=0;i<this.kvData.size();i++)
		{
			if(this.kvData.get(i) == null)
			{
				continue;
			}
			else
			{
				msgnew.kvData.add(this.kvData.get(i).clone());
			}
		}
		return msgnew;
	}
	
	public String getName()
	{
		return name;
	}

	public int getID()
	{
		return id;
	}
	
	/**
	  * description the message infomation;
	  */
	public String description() {
		 StringWriter protoWriter = new StringWriter();
		 if(name==null || name.equals("") || id == 0)
			 return null;
	     try{     	
	    	 	//message header
		    	 protoWriter.write("message ");
		    	 protoWriter.write(name);
		    	 protoWriter.write("(");
		    	 protoWriter.write(Integer.toString(id));
		    	 protoWriter.write(") {\r\n");
	    		//message key value array
	    		for(int i=0;i<kvData.size();i++)
	    		{
	    			EzCommField extraVar = kvData.get(i);
	    			if(extraVar != null)
	    			{
	    				protoWriter.write(extraVar.description());
	    			}
	    		}
	    		//message end
	    		protoWriter.write("}\r\n");
	    		return protoWriter.toString();
	     }
	     catch (Exception e)
	     {
	        e.printStackTrace(); 
	        return null;
	     }
	}

	 /**
	  * serialize To Proto string
	  */
	public String serializeToProto() {
		 StringWriter protoWriter = new StringWriter();
		 if(name==null || name.equals("") || id == 0)
			 return null;
	     try{     	
	    	 	//message header
		    	 protoWriter.write("message ");
		    	 protoWriter.write(name);
		    	 protoWriter.write("(");
		    	 protoWriter.write(Integer.toString(id));
		    	 protoWriter.write(") {\r\n");
	    		//message key value array;
	    		for(int i=0;i<kvData.size();i++)
	    		{
	    			EzCommField extraVar = kvData.get(i);
	    			if(extraVar != null)
	    			{
	    				protoWriter.write(extraVar.getProtoString());
	    			}
	    		}
	    		//message end
	    		protoWriter.write("}\r\n");
	    		return protoWriter.toString();
	     }
	     catch (Exception e)
	     {
	        e.printStackTrace(); 
	        return null;
	     }
	}
	
	 /**
	  * load the proto.
	  */
	public boolean deSerializeFromProto(String strProto) {
		if(strProto == null)
			return false;
		int nPos1 = strProto.indexOf("message");
		 if(nPos1 < 0)
			 return false;
	     try{     	
	    	    strProto = strProto.replaceAll("\r\n", "");
	    	 	int nPos2 = strProto.indexOf(")",7);
	    	 	name = strProto.substring(nPos1+8,nPos2);
	    	 	String[] nameid = name.split("\\(");
	    	 	if(nameid.length != 2)
	    	 		return false;
	    	 	name = nameid[0].trim();
	    	 	id = Integer.parseInt(nameid[1].trim());
	    	 	//
	    	 	nPos1 = strProto.indexOf("{");
	    	 	nPos2 = strProto.indexOf("}");
	    	 	if(nPos1 < 0 || nPos2 < 0)
	    	 		return false;
	    	 	String[] sVarArray = strProto.substring(nPos1+1,nPos2).split(";");
	    	 	int i=0;
	    	 	while(i<sVarArray.length)
	    	 	{
	    	 		String strKeyValue = sVarArray[i++];
	    	 		strKeyValue = strKeyValue.trim();
	    	 		if(strKeyValue.length() <= 5 || strKeyValue.indexOf("=")<=0)
	    	 			continue;
	    	 		EzCommField kv = EzCommField.createVarFromProto(strKeyValue);
	    	 		if(kv != null)
	    	 		{
	    	 			kvData.add(kv);
	    	 		}
	    	 	}	    	 	
	    	 	return true;
	     }
	     catch (Exception e)
	     {
	        e.printStackTrace(); 
	        return false;
	     }
	}

	/**
	 * ��ȡtagid��Ӧ��EzCommField
	 */
	public EzCommField getVarWithTag(int tag)
	{
		int tagid = WireFormat.getTagFieldNumber(tag);
		int wiretype = WireFormat.getTagWireType(tag);
		for(int i=0;i<kvData.size();i++)
    	{
    		EzCommField kv = kvData.elementAt(i);
    		if(kv != null && kv.getFieldID() == tagid && kv.geWireType() == wiretype)
    			return kv;
    	}
		return null;
	}
	 /**
	  * ���л���proto message buffer bytes
	  */
	public byte[] serializeToPB() {
		try{
			if(id == 202)
			{//msg.unknown
				return getKVData("value").getBytes();
			}
			//
			ByteArrayOutputStream rawOutput = new ByteArrayOutputStream();
		    CodedOutputStream output = CodedOutputStream.newInstance(rawOutput);
		    if(serializeToPB(output)==false)
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
	
	public boolean serializeToPB(CodedOutputStream output)
	{
		if(output == null)
			return false;
		try{
			if(id == 202)
			{//msg.unknown
				byte[] value = getKVData("value").getBytes();
				if(value != null)
				{
					output.writeRawBytes(value);
					return true;
				}
				else return false;
			}
			//
			output.writeInt32NoTag(id);
		    if(kvData != null)
		 	{
		    	for(int i=0;i<kvData.size();i++)
		    	{
		    		EzCommField kv = kvData.elementAt(i);
		    		if(kv != null)
		    			kv.writeFieldTo(output);
		    	}
		 	}
		    return true;
		}
		catch (Exception e)
	    {
	        e.printStackTrace(); 
	        return false;
	    }
	}
	
	/**
	  * deSerialize From messege proto buffer bytes
	  */
	public boolean deSerializeFromPB(byte[] bytes) {
		CodedInputStream input = CodedInputStream.newInstance(bytes);
		try{
			int nid = input.readInt32();
			if(nid != id)
				return false;
			else 
				return deSerializeFromPB(input);
		}
		catch (Exception e)
	    {
	        e.printStackTrace(); 
	        return false;
	    }
	}
	
	/**
	  * deSerialize From messege proto buffer bytes
	  */
	public boolean deSerializeFromPB(CodedInputStream input) {
		if(input == null)
			return false;		
		try{
			int tag = input.readTag();
			while(tag!=0)
			{
				EzCommField var = getVarWithTag(tag);
				if(var != null)
					var.readFrom(input);
				else
				{
					input.skipField(tag);
				}
				if(var == kvData.lastElement())
					break;
				tag = input.readTag();
			}	
			return true;
		}
		catch (Exception e)
	    {
	        e.printStackTrace(); 
	        //return false;
	        return true;
	    }
	}

	
	/**
	 * set kv data,the input Field need exist;
	 */
	public boolean SetKVData(EzCommField inCommVar) {
		if(inCommVar == null)
			return false;
		for(int i=0;i<kvData.size();i++)
		{
			EzCommField extraVar = kvData.get(i);
			if(extraVar != null)
			{
				if(extraVar.getName().equals(inCommVar.getName()))
				{
					kvData.set(i, inCommVar.clone());
					return true;
				}
			}
		}
		return false;
	}
	/**
	 * add KV Data field,if the input field exist,replace.
	 */
	public void addKVData(EzCommField inCommVar) {
		if(inCommVar == null)
			return;
		for(int i=0;i<kvData.size();i++)
		{
			EzCommField extraVar = kvData.get(i);
			if(extraVar != null)
			{
				if(extraVar.getName().equals(inCommVar.getName()))
				{
					kvData.set(i, inCommVar.clone());
					return;
				}
			}
		}
		kvData.add(inCommVar.clone());
	}
	/**
	 * get the kv data;
	 * @param strName
	 * 		kvData 
	 */
	public EzCommField getKVData(String strName) {
		for(int i=0;i<kvData.size();i++)
		{
			EzCommField extraVar = kvData.get(i);
			if(extraVar != null)
			{
				if(extraVar.getName().equals(strName))
				{
					return extraVar;
				}
			}
		}
		return null;
	}
	/**
	 * 
	 * Enum names
	 * 
	 */
	public String EnumNames() {
		String names = "";
		for(int i=0;i<kvData.size();i++)
		{
			EzCommField extraVar = kvData.get(i);
			if(extraVar != null && extraVar.getName() != null)
			{
				names += extraVar.getName();
			}
		}
		return names;
	}
	
	/*
	 * equals
	 */
	public boolean equals(EzMessage inValue)
	{
		//1
		if(this.id != inValue.id)
			return false;
		else if(kvData == inValue.kvData)
			return true;
		else if( kvData == null || inValue.kvData == null)
			return false;
		else if(kvData.size() != inValue.kvData.size())
			return false;
		else 
		{	
			for(int i=0;i<kvData.size();i++)
			{
				if(false == kvData.get(i).equals(inValue.kvData.get(i)))
					return false;
			}
			return true;
		}
	}
}
