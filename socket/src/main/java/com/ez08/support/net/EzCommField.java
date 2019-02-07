package com.ez08.support.net;

import com.ez08.support.util.ByteString;
import com.ez08.support.util.CodedInputStream;
import com.ez08.support.util.CodedOutputStream;
import com.ez08.support.util.EzKeyValue;
import com.ez08.support.util.EzValue;


public class EzCommField extends EzKeyValue{

	/*
	 * VAR DATA TYPE
	 */
	//0,Varint
	public static final int COMMVAR_TYPE_INT32 = 0;  
	public static final int COMMVAR_TYPE_INT64 = 1;  
	public static final int COMMVAR_TYPE_UINT32 = 2;  
	public static final int COMMVAR_TYPE_UINT64 = 3; 
	public static final int COMMVAR_TYPE_SINT32 = 4;//long
	public static final int COMMVAR_TYPE_SINT64 = 5;
	public static final int COMMVAR_TYPE_BOOL = 6;
	public static final int COMMVAR_TYPE_ENUM = 7;//<--NOT USE NOW;  
	//1,64 bit
	public static final int COMMVAR_TYPE_FIXED64 = 8;  
	public static final int COMMVAR_TYPE_SFIXED64 = 9;  
	public static final int COMMVAR_TYPE_DOUBLE = 10;  
	//5,32 bit
	public static final int COMMVAR_TYPE_FIXED32 = 11;  
	public static final int COMMVAR_TYPE_SFIXED32 = 12;  
	public static final int COMMVAR_TYPE_FLOAT = 13; 
	//2,Length-delimited
	public static final int COMMVAR_TYPE_STRING = 14;  
	public static final int COMMVAR_TYPE_BYTES = 15;  
	public static final int COMMVAR_TYPE_MESSAGE = 16;  
	public static final int COMMVAR_TYPE_KEYVALUE = 17;  
	public static final int COMMVAR_TYPE_VALUE = 18;  
	//3,Start group,4,End group,skipped...
	
	//PRE ATTRI TYPE
	public static final int PREATTRI_Required = 0;
	public static final int PREATTRI_Optional = 1;
	public static final int PREATTRI_Repeated = 2;
	
	//field id
	private int	fieldID = 0;
	//CommVarType
	private int commVarType = 0;
	//message id,COMMVAR_TYPE_MESSAGE
	private int messageID = 0;
	//
	public int preAttri = 0;

	
	/**
	 * 
	 */
	public EzCommField()
	{
		
	}	
	/**
	 * 
	 */
	public EzCommField(String inName,int fid,int inVarType,int attri) {
		super.setName(inName);
		fieldID = fid;
		commVarType = inVarType;
		preAttri = attri;
	}

	/**
	 * clone
	 */
	public EzCommField clone()
	{
		EzCommField var = new EzCommField();
		var.setName(getName());
		var.fieldID = this.fieldID;
		var.preAttri = this.preAttri;
		var.commVarType = this.commVarType;
		var.messageID = this.messageID;
		var.setValue((EzValue)this);
		return var;
	}
	
	/**
	 * get the proto string 
	 */
	public String getProtoString(){
		StringBuffer strBuffer = new StringBuffer();
		switch(preAttri)
		{
		case PREATTRI_Repeated:
			strBuffer.append("repeated ");
			break;
		case PREATTRI_Optional:
			strBuffer.append("optional ");
			break;
		case PREATTRI_Required:
		default:
			strBuffer.append("required ");
			break;
		}
		//
		switch(commVarType)
		{
		case COMMVAR_TYPE_INT32:
			strBuffer.append("int32 ");
			break;
		case COMMVAR_TYPE_INT64: 
			strBuffer.append("int64 ");
			break;
		case COMMVAR_TYPE_UINT32:  
			strBuffer.append("uint32 ");
			break;
		case COMMVAR_TYPE_UINT64: 
			strBuffer.append("uint64 ");
			break;
		case COMMVAR_TYPE_SINT32:
			strBuffer.append("sint32 ");
			break;
		case COMMVAR_TYPE_SINT64:
			strBuffer.append("sint64 ");
			break;
		case COMMVAR_TYPE_BOOL:
			strBuffer.append("bool ");
			break;
		case COMMVAR_TYPE_ENUM:
			strBuffer.append("enum ");
			break;
		case COMMVAR_TYPE_FIXED64:  
			strBuffer.append("fixed64 ");
			break;
		case COMMVAR_TYPE_SFIXED64:  
			strBuffer.append("sfixed64 ");
			break;
		case COMMVAR_TYPE_DOUBLE:  
			strBuffer.append("double ");
			break;
		case COMMVAR_TYPE_STRING:  
			strBuffer.append("string ");
			break;
		case COMMVAR_TYPE_BYTES:  
			strBuffer.append("bytes ");
			break;
		case COMMVAR_TYPE_MESSAGE:  
			if(messageID != 0)
			{
				String name = EzMessageFactory.getMessageName(messageID);
				if(name != null)
					strBuffer.append(name);
				else strBuffer.append("unknown ");
			}
			else if(getMessage() != null && getMessage().getName() !=null && !getMessage().getName().equals(""))
			{
				strBuffer.append(getMessage().getName());
				strBuffer.append(" ");
			}
			else 
				strBuffer.append("unknown ");
			break;
		case COMMVAR_TYPE_FIXED32: 
			strBuffer.append("fixed32 ");
			break;
		case COMMVAR_TYPE_SFIXED32:  
			strBuffer.append("sfixed32 ");
			break;
		case COMMVAR_TYPE_FLOAT:
			strBuffer.append("float ");
			break;
		case COMMVAR_TYPE_KEYVALUE:
			strBuffer.append("keyvalue ");
			break;
		case COMMVAR_TYPE_VALUE:
			strBuffer.append("value ");
			break;
		default:
			strBuffer.append("unknown ");
			break;
		}
		if(getName() == null || getName().equals(""))
			strBuffer.append("noname");
		else
			strBuffer.append(getName());
		strBuffer.append(" = ");
		strBuffer.append(Integer.toString(fieldID));
		strBuffer.append(";\r\n");
		return strBuffer.toString();
	}
		
	
	/**
	 * set the proto String
	 */
	public boolean setProtoString(String strProto){
		try
		{
			String[] namevalue = strProto.trim().split("=");
			if(namevalue.length != 2)
				return false;
			//tag ID
			fieldID = Integer.parseInt(namevalue[1].trim());
			//
			String[] namearray = namevalue[0].split(" ");
			if(namearray.length<3)
				return false;
			//
			int nIndex = 0;
			String strPreAttri = namearray[nIndex].trim();
			if(strPreAttri.equalsIgnoreCase("repeated"))
			{
				preAttri = PREATTRI_Repeated;
			}
			else if(strPreAttri.equalsIgnoreCase("optional"))
			{
				preAttri = PREATTRI_Optional;
			}
			else// if(strPreAttri.equalsIgnoreCase("required"))
			{
				preAttri = PREATTRI_Required;
			}
			//
			nIndex++;
			while(nIndex < namearray.length&& namearray[nIndex].length()<=0)
			{
				nIndex++;
			}
			if(nIndex >= namearray.length || namearray[nIndex].length()<=0)
				return false;
			String strvartype = namearray[nIndex].trim();
			//varType
			if(strvartype.equalsIgnoreCase("int32"))
				commVarType = COMMVAR_TYPE_INT32;
			else if(strvartype.equalsIgnoreCase("int64"))
				commVarType = COMMVAR_TYPE_INT64;
			else if(strvartype.equalsIgnoreCase("uint32"))
				commVarType = COMMVAR_TYPE_UINT32;
			else if(strvartype.equalsIgnoreCase("uint64"))
				commVarType = COMMVAR_TYPE_UINT64;
			else if(strvartype.equalsIgnoreCase("sint32"))
				commVarType = COMMVAR_TYPE_SINT32;
			else if(strvartype.equalsIgnoreCase("sint64"))
				commVarType = COMMVAR_TYPE_SINT64;
			else if(strvartype.equalsIgnoreCase("bool"))
				commVarType = COMMVAR_TYPE_BOOL;
			else if(strvartype.equalsIgnoreCase("boolean"))
			    commVarType = COMMVAR_TYPE_BOOL;
			else if(strvartype.equalsIgnoreCase("enum"))
				commVarType = COMMVAR_TYPE_ENUM;
			else if(strvartype.equalsIgnoreCase("fixed64"))
				commVarType = COMMVAR_TYPE_FIXED64;
			else if(strvartype.equalsIgnoreCase("sfixed64"))
				commVarType = COMMVAR_TYPE_SFIXED64;
			else if(strvartype.equalsIgnoreCase("double"))
				commVarType = COMMVAR_TYPE_DOUBLE;
			else if(strvartype.equalsIgnoreCase("string"))
				commVarType = COMMVAR_TYPE_STRING;
			else if(strvartype.equalsIgnoreCase("bytes"))
				commVarType = COMMVAR_TYPE_BYTES;
			else if(strvartype.equalsIgnoreCase("byte"))
				commVarType = COMMVAR_TYPE_BYTES;
			else if(strvartype.equalsIgnoreCase("fixed32"))
				commVarType = COMMVAR_TYPE_FIXED32;
			else if(strvartype.equalsIgnoreCase("sfixed32"))
				commVarType = COMMVAR_TYPE_SFIXED32;
			else if(strvartype.equalsIgnoreCase("float"))
				commVarType = COMMVAR_TYPE_FLOAT;
			else if(strvartype.equalsIgnoreCase("keyvalue"))
				commVarType = COMMVAR_TYPE_KEYVALUE;
			else if(strvartype.equalsIgnoreCase("value"))
				commVarType = COMMVAR_TYPE_VALUE;
			else
			{
				commVarType = COMMVAR_TYPE_MESSAGE;
				messageID = EzMessageFactory.getMessageID(strvartype);
			}
			//
			nIndex++;
			while(nIndex < namearray.length&& namearray[nIndex].length()<=0)
			{
				nIndex++;
			}
			if(nIndex >= namearray.length || namearray[nIndex].length()<=0)
				return false;
			String strname = namearray[nIndex].trim();
			setName(strname);
			return true;
		  }
        catch (Exception e)
        {
        	e.printStackTrace(); 
        	return false;
        }
        finally
        {
        }
	}
	
	public static EzCommField createVarFromProto(String strProto){
		EzCommField var = new EzCommField();
		if(var.setProtoString(strProto) == true)
			return var;
		else return null;
	}
		
	 /**
	  * proto buf
	  */
	public boolean writeFieldTo(CodedOutputStream output) {
		if(output == null)
			return false;
		try{
			if(preAttri == PREATTRI_Repeated)
			{
				switch(commVarType)
				{
				case COMMVAR_TYPE_BYTES:  
					if(getByteString()!=null)
						output.writeBytes(fieldID, getByteString());
					break;
					
				case COMMVAR_TYPE_INT32:
				case COMMVAR_TYPE_INT64:
				case COMMVAR_TYPE_UINT32:
				case COMMVAR_TYPE_UINT64:
				case COMMVAR_TYPE_SINT32:
				case COMMVAR_TYPE_SINT64:
				case COMMVAR_TYPE_BOOL://int
				case COMMVAR_TYPE_ENUM://int
				case COMMVAR_TYPE_FIXED64:  
				case COMMVAR_TYPE_SFIXED64: 
				case COMMVAR_TYPE_FIXED32: 
				case COMMVAR_TYPE_SFIXED32:  
				case COMMVAR_TYPE_DOUBLE:  
				case COMMVAR_TYPE_FLOAT:
				case COMMVAR_TYPE_MESSAGE:
				case COMMVAR_TYPE_KEYVALUE:
				case COMMVAR_TYPE_STRING:
				case COMMVAR_TYPE_VALUE:
					{
					//wiretype == 2,write tag;
					output.writeTag(fieldID, 2);
					super.writeValueToNoType(output);
					}
					break;
				default:
					break;
				}
			}
			else
			{
				switch(commVarType)
				{
				case COMMVAR_TYPE_INT32:
					output.writeInt32(fieldID, getInt32());
					break;
				case COMMVAR_TYPE_INT64: 
					output.writeInt64(fieldID, getInt64());
					break;
				case COMMVAR_TYPE_UINT32:  
					output.writeUInt32(fieldID, getInt32());
					break;
				case COMMVAR_TYPE_UINT64: 
					output.writeUInt64(fieldID, getInt64());
					break;
				case COMMVAR_TYPE_SINT32:
					output.writeSInt32(fieldID, getInt32());
					break;
				case COMMVAR_TYPE_SINT64:
					output.writeSInt64(fieldID, getInt64());
					break;
				case COMMVAR_TYPE_BOOL://int
					output.writeInt32(fieldID, getInt32());
					break;
				case COMMVAR_TYPE_ENUM://int
					output.writeInt32(fieldID, getInt32());
					break;
				case COMMVAR_TYPE_FIXED64:  
					output.writeFixed64(fieldID, getInt64());
					break;
				case COMMVAR_TYPE_SFIXED64:  
					output.writeSFixed64(fieldID, getInt64());
					break;
				case COMMVAR_TYPE_DOUBLE:  
					output.writeDouble(fieldID, getDouble());
					break;
				case COMMVAR_TYPE_STRING:  
					if(getString()!=null && !getString().equals(""))
						output.writeString(fieldID, getString());
					break;
				case COMMVAR_TYPE_BYTES:  
					if(getByteString()!=null)
						output.writeBytes(fieldID, getByteString());
					break;
				case COMMVAR_TYPE_MESSAGE: 
					{
						if(getMessage() != null)
						{
							output.writeTag(fieldID, 2);
							super.writeValueToNoType(output);
						}
					}
					break;
				case COMMVAR_TYPE_KEYVALUE:
					if(getKeyValue() != null)
					{
						output.writeTag(fieldID, 2);
						super.writeValueToNoType(output);
					}
					break;
				case COMMVAR_TYPE_VALUE:
					{
						ByteString bs = ByteString.copyFrom(super.valueToBytes());
						if(bs != null)
							output.writeBytes(fieldID, bs);
					}
					break;
				case COMMVAR_TYPE_FIXED32: 
					output.writeFixed32(fieldID, getInt32());
					break;
				case COMMVAR_TYPE_SFIXED32:  
					output.writeSFixed32(fieldID, getInt32());
					break;
				case COMMVAR_TYPE_FLOAT:
					output.writeFloat(fieldID, getFloat());
					break;
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
	 * 
	 */
	public boolean readFrom(CodedInputStream input)
	{
		if(input == null)
			return false;
		try{
			if(preAttri == PREATTRI_Repeated)
			{
				//array
				switch(commVarType)
				{
				case COMMVAR_TYPE_BYTES:  
					setValue(input.readBytes());
					break;
				case COMMVAR_TYPE_SINT32:
					readValueFrom(input,EzValue.VALUE_TYPE_SINT32S);
					break;
				case COMMVAR_TYPE_SINT64:
					readValueFrom(input,EzValue.VALUE_TYPE_SINT64S);
					break;
				case COMMVAR_TYPE_INT32:
				case COMMVAR_TYPE_UINT32:
				case COMMVAR_TYPE_BOOL://int32
				case COMMVAR_TYPE_ENUM://int32
				case COMMVAR_TYPE_FIXED32: 
				case COMMVAR_TYPE_SFIXED32:  
				{
					readValueFrom(input,EzValue.VALUE_TYPE_INT32S);
					break;
				}
				case COMMVAR_TYPE_INT64:
				case COMMVAR_TYPE_UINT64:
				case COMMVAR_TYPE_FIXED64:  
				case COMMVAR_TYPE_SFIXED64: 
				{
					readValueFrom(input,EzValue.VALUE_TYPE_INT64S);
					break;
				}
				case COMMVAR_TYPE_DOUBLE:  
				{
					readValueFrom(input,EzValue.VALUE_TYPE_DOUBLES);
					break;
				}
				case COMMVAR_TYPE_FLOAT:
				{
					readValueFrom(input,EzValue.VALUE_TYPE_FLOATS);
					break;
				}
				case COMMVAR_TYPE_MESSAGE:
				{
					readValueFrom(input,EzValue.VALUE_TYPE_MESSAGES);
					break;
				}
				case COMMVAR_TYPE_KEYVALUE:
				{
					readValueFrom(input,EzValue.VALUE_TYPE_KEYVALUES);
					break;
				}
				case COMMVAR_TYPE_STRING:
				{
					readValueFrom(input,EzValue.VALUE_TYPE_STRINGS);
					break;
				}
				case COMMVAR_TYPE_VALUE:
				{
					readValueFrom(input,EzValue.VALUE_TYPE_VALUES);
					break;
				}
				default:
					return false;
				}
			}
			else
			{
				switch(commVarType)
				{
				case COMMVAR_TYPE_INT32:
					setValue(input.readInt32());
					break;
				case COMMVAR_TYPE_INT64: 
					setValue(input.readInt64());
					break;
				case COMMVAR_TYPE_UINT32:  
					setValue(input.readUInt32());
					break;
				case COMMVAR_TYPE_UINT64: 
					setValue(input.readUInt64());
					break;
				case COMMVAR_TYPE_SINT32:
					setValue(input.readSInt32());
					break;
				case COMMVAR_TYPE_SINT64:
					setValue(input.readSInt64());
					break;
				case COMMVAR_TYPE_BOOL://int
					setValue(input.readInt32());
					break;
				case COMMVAR_TYPE_ENUM://int
					setValue(input.readInt32());
					break;
				case COMMVAR_TYPE_FIXED64:  
					setValue(input.readFixed64());
					break;
				case COMMVAR_TYPE_SFIXED64:  
					setValue(input.readSFixed64());
					break;
				case COMMVAR_TYPE_DOUBLE:  
					setValue(input.readDouble());
					break;
				case COMMVAR_TYPE_STRING:
					setValue(input.readString());
					break;
				case COMMVAR_TYPE_BYTES:  
					setValue(input.readBytes());
					break;
				case COMMVAR_TYPE_MESSAGE: 
					readValueFrom(input,EzValue.VALUE_TYPE_MESSAGE);
					break;
				case COMMVAR_TYPE_KEYVALUE: 
					readValueFrom(input,EzValue.VALUE_TYPE_KEYVALUE);
					break;
				case COMMVAR_TYPE_VALUE: 
					{
						ByteString bs = input.readBytes();
						if(bs != null)
							super.valueFromBytes(bs.toByteArray());
					}
					break;
				case COMMVAR_TYPE_FIXED32: 
					setValue(input.readFixed32());
					break;
				case COMMVAR_TYPE_SFIXED32:  
					setValue(input.readSFixed32());
					break;
				case COMMVAR_TYPE_FLOAT:
					setValue(input.readFloat());
					break;
				default:
					return false;
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
	 * EzCommField
	 * 
	 * @return int
	 * 
	 */
	public int getVarType() {
		return commVarType;
	}
	
	/**
	 * 
	 * @return int
	 */
	public int geWireType() {
		if(preAttri == PREATTRI_Repeated)
			return 2;
		switch(commVarType)
		{
		case COMMVAR_TYPE_INT32:
		case COMMVAR_TYPE_INT64: 
		case COMMVAR_TYPE_UINT32:  
		case COMMVAR_TYPE_UINT64: 
		case COMMVAR_TYPE_SINT32:
		case COMMVAR_TYPE_SINT64:
		case COMMVAR_TYPE_BOOL://int
		case COMMVAR_TYPE_ENUM://int
			return 0;
		case COMMVAR_TYPE_FIXED64:  
		case COMMVAR_TYPE_SFIXED64:  
		case COMMVAR_TYPE_DOUBLE:  
			return 1;
		case COMMVAR_TYPE_STRING:
		case COMMVAR_TYPE_BYTES:  
		case COMMVAR_TYPE_MESSAGE: 
		case COMMVAR_TYPE_KEYVALUE: 
		case COMMVAR_TYPE_VALUE: 
			return 2;
		case COMMVAR_TYPE_FIXED32: 
		case COMMVAR_TYPE_SFIXED32:  
		case COMMVAR_TYPE_FLOAT:
			return 5;
		default:
			return -1;
		}
	}
	/**
	 * 
	 * @return int
	 */
	public int getPreAttri() {
		return preAttri;
	}
	
	/**
	 * getFieldID
	 */
	public int getFieldID()
	{
		return fieldID;
	}
	
	/**
	 * getMessageID
	 */
	public int getMessageID()
	{
		return messageID;
	} 
	
	public boolean equals(EzCommField inValue)
	{
		if(this.getFieldID() != inValue.getFieldID() 
				|| this.getPreAttri() != inValue.getPreAttri()
				|| this.getMessageID() != inValue.getMessageID()
				|| this.getVarType() != inValue.getVarType())
			return false;
		else
		{
			return ((EzKeyValue)this).equals((EzKeyValue)inValue);
		}
	}
}
