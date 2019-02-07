package com.ez08.support.net;

import java.util.HashMap;
import java.util.Vector;

import com.ez08.support.util.ByteString;
import com.ez08.support.util.CodedInputStream;

/**
 */
public class EzMessageFactory {

	/**
	 */
	static private Vector<EzMessage> msgArray;
	static private HashMap<Integer ,String> actionMap = new HashMap<Integer ,String>();
	/**
	 */
	static private int snClient=101;
	static private int snServer=100;

	static public int getSnServer()
	{
		snServer += 2;
		if(snServer<0)
			snServer = 100;
		return snServer;
	}
	static public int getSnClient()
	{
		snClient += 2;
		if(snClient<0)
			snClient = 101;
		return snClient;
	}
	//全部的业务协议
	static public boolean initProto()
	{
		clear();
		//
		String[] proto = new String[100];
		int index = 0;
		proto[index++] = "message msg.intent(201){" +
		"required int32 sn = 1;" +
		"required string tid = 2;" +
		"required string action = 3;" +
		"required string componentname = 4;" +
		"optional string category = 5;" +
		"required string uri = 6;" +
		"optional int32  flags = 7;" +
		"repeated keyvalue extra = 8;" +
		"required int32  encflags = 9;" +
		"required int32  actionid = 10;" +
		"required string  cid = 11;" +
		"}";
		proto[index++] = "message msg.unknown(202){" +
		"required bytes  value = 1;" +
		"}";
		proto[index++] = "message msg.secu.priceunit(1001){" +
		"required int64  time = 1;" +
		"required string secucode = 2;" +
		"required string secuname = 3;" +
		"required int32 preclose = 4;" +
		"required sint32 open = 5;" +
		"required sint32 current = 6;" +
		"required sint32 highest = 7;" +
		"required sint32 lowest = 8;" +
		"required int64 amount = 9;" +
		"required int64 volume = 10;" +
		"required sint32 volratio = 11;" +
		"required sint32 comratio = 12;" +
		"required sint32 combalance = 13;" +
		"required int32 handnum = 14;" +
		"required int32 exratio = 15;" +
		"required sint32 peratio = 16;" +
		"required int32 pbratio = 17;" +
		"}";
		proto[index++] = "message msg.secu.buysellunit(1002){" +
		"required int64  time = 1;" +
		"required string secucode = 2;" +
		"required string secuname = 3;" +
		"required int32 preclose = 4;" +
		"required sint32 buy1price = 5;" +
		"required int32 buy1volume = 6;" +
		"required sint32 buy2price = 7;" +
		"required int32 buy2volume = 8;" +
		"required sint32 buy3price = 9;" +
		"required int32 buy3volume = 10;" +
		"required sint32 buy4price = 11;" +
		"required int32 buy4volume = 12;" +
		"required sint32 buy5price = 13;" +
		"required int32 buy5volume = 14;" +
		"required sint32 sell1price = 15;" +
		"required int32 sell1volume = 16;" +
		"required sint32 sell2price = 17;" +
		"required int32 sell2volume = 18;" +
		"required sint32 sell3price = 19;" +
		"required int32 sell3volume = 20;" +
		"required sint32 sell4price = 21;" +
		"required int32 sell4volume = 22;" +
		"required sint32 sell5price = 23;" +
		"required int32 sell5volume = 24;" +
		"}";
		proto[index++] = "message msg.secu.klineunit(1003){" +
		"required int64 timespan = 1;" +
		"required int32 open = 2;" +
		"required sint32 high = 3;" +
		"required sint32 low = 4;" +
		"required sint32 close = 5;" +
		"required int64 amount = 6;" +
		"required int64 volume = 7;" +
		"}";
		proto[index++] = "message msg.secu.kline(2002){" +
		"required string secucode = 1;" +
		"required string secuname = 2;" +
		"required string markettime = 3;" +
		"required int32 klinetype = 4;" +
		"required int64 starttime = 5;" +
		"required int32 preclose = 6;" +
		"repeated msg.secu.klineunit data = 7;" +
		"}";
		proto[index++] = "message msg.secu.price(2003){" +
		"repeated msg.secu.priceunit data = 1;" +
		"}";
		proto[index++] = "message msg.secu.buysell(2004){" +
		"repeated msg.secu.buysellunit data = 1;" +
		"}";
		proto[index++] = "message msg.secu.detail(2005){" +
		"required string secucode = 1;" +
		"required string secuname = 2;" +
		"required int32 preclose = 3;" +
		"required int32 stimestart = 4;" +
		"repeated int32 stime = 5;" +
		"repeated sint32 price = 6;" +
		"repeated int32 volume = 7;" +
		"}";
		proto[index++] = "message msg.quote.change(2006){" +
		"required int64 time = 1;" +
		"repeated String secucodes = 2;" +
		"}";
		proto[index++] = "message msg.quote.subscribeunit(2007){" +
		"required int64 time = 1;" +
		"required String secucode = 2;" +
		"required int32 timeout = 3;" +
		"}";
		proto[index++] = "message msg.quote.subscribe(2008){" +
		"required string tid = 1;" +	
		"repeated ez08.quote.subscribeunit data = 2;" +
		"}";
		//终端信息
		proto[index++] = "message msg.auth.tidinfo(2009){" +
		"required string tid = 1;" +
		"required string cid = 2;" +
		"required string os = 3;" +
		"required string osver = 4;" +
		"required string tver = 5;" +
		"required int32  state = 6;" +
		"required string wifiMacAddr = 7;"+
		"required string imei = 8;"+
		"required string appName = 9;"+
		"required string domain = 10;"+
		"}";
		//
		proto[index++] = "message msg.secu.ctcunit(2010){" +
		"required string secucode = 1;" +
		"required string secuname = 2;" +
		"required int32  secutype = 3;" +
		"required string namepinyin = 4;" +
		"required int32 pricep = 5;" +
		"required int32 pricesp = 6;" +
		"required int32 sharesperlot = 7;" +
		"required int32 volumesunit = 8;" +
		"required int32  modifytype = 9;" +
		"}";
		//
		proto[index++] = "message msg.secu.codetablechange(2011){" +
		"required int32 id = 1;" +
		"required int32 time = 2;" +
		"repeated ez08.secu.ctcunit  data = 3;" +
		"}";
		//码表C信息
		proto[index++] = "message msg.secu.codetablec(2012){" +
		"required string secucode = 1;" +
		"required string secuname = 2;" +
		"required int64  secunumall = 3;" +
		"required int64  secunuma = 4;" +
		"required int64  secunumb = 5;" +
		"required int64  secunumh = 6;" +
		"required int32  secutype = 7;" +
		"required string namepinyin = 8;" +
		"required int32 pricep = 9;" +
		"required int32 pricesp = 10;" +
		"required int32 sharesperlot = 11;" +
		"required int32 volumesunit = 12;" +
		"required keyvalue divisions = 13;" +
		"required sint32 eps = 14;" +
		"required sint32 pps = 15;" +
		"required sint32 avps = 16;" +
		"required int64 outshares = 17;" +
		"}";
		//
		//资讯明细
		proto[index++] = "message msg.info.detail(2013){" +
		"required string infoid = 1;" +
		"required int64 time = 2;" +
		"required string title = 3;" +
		"required string source = 4;" +
		"required string content = 5;" +
		"required bytes image = 6;" +
		"required string type = 7;" +
		"required string keys = 8;"+
		"required string imageid = 9;" +
		"}";
		proto[index++] = "message msg.info.excerpt(2014){" +
		"required string infoid = 1;" +
		"required int64 time = 2;" +
		"required string  title = 3;" +
		"required string  source = 4;" +
		"required string  excerpt = 5;" +
		"required bytes  image = 6;" +
		"required string imageid = 7;" +
		"}";
		//走势
		proto[index++] = "message msg.secu.mline(2015){" +
		"required string secucode = 1;" +
		"required string secuname = 2;" +
		"required int32  preclose = 3;" +
		"required int64  prevolume = 4;" +
		"required int32  startindex = 5;" +
		"repeated sint32 prices = 6;" +
		"repeated sint64 volumes = 7;" +
		"}";
		//评论
		proto[index++] = "message msg.info.comment(2016){" +
		"required string comid = 1;" +
		"required int64 time = 2;" +
		"required string comtitle = 3;" +
		"required string cid = 4;" +
		"required string name = 5;" +
		"required string text = 6;" +
		"required string targtype = 7;" +
		"required string targid = 8;" +
		"required int32 support = 9;"+
		"required int32 against = 10;"+
		"}";
		//资讯栏目预览
		proto[index++] = "message msg.info.section(2017){" +
		"required string sectionid = 1;" +
		"required string title = 2;" +
		"required string source = 3;" +
		"required bytes image = 4;" +
		"repeated msg.info.excerpt excerpts = 5;" +
		"required int32 showmode = 6;" +
		"required string imageid = 7;" +
		"}";
		
		//个人公开信息
		proto[index++] = "message msg.user.pubinfo(2018){" +
		"required string cid = 1;" +
		"required string name = 2;" +
		"required int32 sex = 3;" +
		"required string mobile = 4;" +
		"required string oid = 5;" +
		"required string oidname = 6;" +
		"required string bid = 7;" +
		"required string bidname = 8;" +
		"required bytes image = 9;" +
		"required string country = 10;" +
		"required boolean online = 11;" +
		"required boolean cancall = 12;" +
		"required string actor = 13;" +
		"required boolean verify = 14;" +
		"required string certa = 15;" +
		"required string certb = 16;" +
		"required string brief = 17;" +
		"required string imageid = 18;" +
		"required int32 support = 19;" +
		"required int32 against = 20;" +
		"required string province = 21;" +
		"required string city = 22;" +
		 "required boolean iscs = 29;" +
		 "required string state = 30;" +

		"}";
				
		//交易使用, 2019
		proto[index++] = "message msg.trade.data(2019){" +
		"required string customerid = 1;" +
		"required string custname = 2;" +
		"required string accountid = 3;" +
		"required string accountid = 3;" +
		"required string shareholderid = 4;" +
		"required string fundacctid = 5;" +
		"required string bankacctid = 6;" +
		"required string accounttype = 7;" +
		"required string exchangetype = 8;" +
		"required string pwdtype = 9;" +
		"required string tradepwd = 10;" +
		"required string acctpwd = 11;" +
		"required string fundpwd = 12;" +
		"required string bankpwd = 13;" +
		"required string oldpwd = 14;" +
		"required string newpwd = 15;" +
		"required string commpwd = 16;" +
		"required string dynamicpwd = 17;" +
		"required string currencyid = 18;" +
		"required string orgid = 19;" +
		"required string branchid = 20;" +
		"required string broker = 21;" +
		"required string ordertype = 22;" +
		"required string busintype = 23;" +
		"required string tradetype = 24;" +
		"required string secucode = 25;" +
		"required string secuname = 26;" +
		"required string tradedate = 27;" +
		"required string deliverydate = 28;" +
		"required string commission = 29;" +
		"required string stamptax = 30;" +
		"required string transferfee = 31;" +
		"required string orderqty = 32;" +
		"required string orderprice = 33;" +
		"required string ordermoney = 34;" +
		"required string ordertime = 35;" +
		"required string orderno = 36;" +
		"required string businqty = 37;" +
		"required string businprice = 38;" +
		"required string businmoney = 39;" +
		"required string busintime = 40;" +
		"required string busindate = 41;" +
		"required string businno = 42;" +
		"required string businnum = 43;" +
		"required string withdrawqty = 44;" +
		"required string withdrawtime = 45;" +
		"required string withdrawflag = 46;" +
		"required string sharesamount = 47;" +
		"required string sharesenable = 48;" +
		"required string maketvalue = 49;" +
		"required string currentcost = 50;" +
		"required string costprice = 51;" +
		"required string buyqty = 52;" +
		"required string sellqty = 53;" +
		"required string enablemoney = 54;" +
		"required string occurmoney = 55;" +
		"required string remainmoney = 56;" +
		"required string frozenamt = 57;" +
		"required string fundcode = 58;" +
		"required string bankid = 59;" +
		"required string bankname = 60;" +
		"required string returncode = 61;" +
		"required string returninfo = 62;" +
		"required string reporttime = 63;" +
		"required string orderstatus = 64;" +
		"required string begindate = 65;" +
		"required string enddate = 66;" +
		"required string querycount = 67;" +
		"required string querystr = 68;" +
		"required string serialnum = 69;" +
		"required string transferamt = 70;" +
		"required string poststr = 71;" +
		"required string sort = 72;" +
		"required string tips = 73;" +
		"required string transferstatus = 74;"+
		"required string brokerid = 75;"+
		"required string brokername = 76;"+
		"required string telphone = 77;"+
		"required string branchname = 78;"+
		"required string area = 79;"+
		"}";
		
		//股票池
		proto[index++] = "message msg.info.stockpool(2020){" +
		"required int64 time = 1;" +
		"required string poolid = 2;" +
		"required string poolname = 3;" +
		"required int32 follow = 4;" +
		"repeated string secucodes = 5;" +
		"required string poolintro = 6;" +
		"required bytes image = 7;" +
		"required string imageid = 8;" +
		"}";
		
		//个人详细公开信息
		proto[index++] = "message msg.user.pubdetail(2021){" +
		"required string cid = 1;" +
		"required string name = 2;" +
		"required int32 sex = 3;" +
		"required string mobile = 4;" +
		"required string oid = 5;" +
		"required string oidname = 6;" +
		"required string bid = 7;" +
		"required string bidname = 8;" +
		"required bytes image = 9;" +
		"required string country = 10;" +
		"required boolean online = 11;" +
		"required boolean cancall = 12;" +
		"required string actor = 13;" +
		"required boolean verify = 14;" +
		"required string certa = 15;" +
		"required string certb = 16;" +
		"required string brief = 17;" +
		"required string imageid = 18;" +
		"required int32 support = 19;" +
		"required int32 against = 20;" +
		"required string province = 21;" +
		"required string city = 22;" +
		"required boolean isfriend = 23;" +
		"required boolean isappoint = 24;" +
		"required boolean isblock = 25;" +
		"required boolean isfollow = 26;" +
		"required boolean isappointed = 27;" +
		"required boolean isfollowed = 28;" +
		"required boolean iscs = 29;" +
		 "required string state = 30;" +

		"}";

		 proto[index++] = "message msg.model.range(3007){" +
		  "required string id = 1;" +
		  "required int32 type = 2;" +
		  "required int64 min = 3;" +
		  "required int64 max = 4;" +
		  "}";
		
		//组织
		proto[index++] = "message msg.auth.oidinfo(3002){" +
		"required string oid = 1;" +
		"required string name = 2;" +
		"required string keywords = 3;" +
		"required bytes image = 4;" +
		"required string intro = 5;" +
		"required string bids = 6;" +
		"required string state = 7;" +
		"required string imageid = 8;" +
		"required string callCenterTel = 10;" +
		"repeated keyvalue extra = 20;" +
		"}";
		//分支
		 proto[index++] = "message msg.auth.branchinfo(3003){" +
		  "required string oid = 1;" +
		  "required string bid = 2;" +
		  "required string name = 3;" +
		  "required string keywords = 4;" +
		  "required bytes image = 5;" +
		  "required string intro = 6;" +
		  "required string state = 7;" +
		  "required string imageid = 8;" +
		  "required string cids = 10;" +
		  "required string callCenterTel = 11;" +
		  "repeated keyvalue classes = 12;" +
		  "required string adimageids = 13;" +
		  "required string addr = 14;" +
		  "required double lat = 15;" +
		  "required double lng = 16;" +
		  "repeated keyvalue extra = 20;" +
		  "}";
		 
		//发布资讯内容项
		proto[index++] = "message msg.infopublish.detail(3004){" +
		"required string infoid = 1;" +
		"required int64 time = 2;" +
		"required string editor = 3;" +
		"required int64 pubtime = 4;" +
		"required string publisher = 5;" +
		"required string title = 6;" +
		"required string source = 7;" +
		"required string content = 8;" +
		"required bytes image = 9;" +
		"required string sectionid = 10;" +
		"required string keys = 11;" +
		"required int32 state = 12;" +
		"}";
		//推送内容项
		proto[index++] = "message msg.push.detail(3005){" +
		"required string infoid = 1;" +
		"required int64 time = 2;" +
		"required string editor = 3;" +
		"required int64 pubtime = 4;" +
		"required string publisher = 5;" +
		"required string title = 6;" +
		"required string source = 7;" +
		"required string content = 8;" +
		"required bytes image = 9;" +
		"required string oid = 10;" +
		"required int32 state = 11;" +
		"required string target = 12;" +
		"required int64 pushSTime = 13;" +
		"required int64 pushETime = 14;" +
		"required bytes audio = 15;" +
		"required string audiourl = 16;" +
		"required bytes vedio = 17;" +
		"required string vediourl = 18;" +
		"required bytes view = 19;" +
		"required bytes operate = 20;" +
		"required bytes buttons = 21;" +
		"}";
		  // 车辆基本信息定义
		  proto[index++] = "message msg.car.info(3008){" +
		  "required string uid = 1;" +
		  "required string 4sbid = 3;" +
		  "required string licence = 4;" +
		  "required string brand = 5;" +
		  "required string series = 6;" +
		  "required string model = 7;" +
		  "required int64 year = 8;" +
		  "required string vin = 9;" +
		  "required string protocol = 10;" +
		  "required string cali = 11;" +
		  "required string obdFeature = 12;" +
		  "required int32 mset = 13;" +
		  "required int32 cact = 14;" +
		  "required int32 mact = 15;" +
		  "required string dtcs = 16;" +
		  "required string frozen = 17;" +
		  "required string obdData = 18;" +
		  "required int32 maintenance = 19;" +
		  "required int64 insurance = 20;" +
		  "required string remind = 21;" +
		  "required int64 remindTime = 22;" +
		  "required string remindSrc = 23;" +
		  "required int64 updateTime = 24;" +
		  "required int64 ver = 25;" +
		  "required boolean del = 26;" +
		  "}";
		  // 车辆体检数据
		  proto[index++] = "message msg.car.report(3009){" +
		  "required string uid = 1;" +
		  "required string carid = 2;" +
		  "required string licence = 3;" +
		  "required string brand = 4;" +
		  "required string series = 5;" +
		  "required string model = 6;" +
		  "required string vin = 7;" +
		  "required int64 testTime = 8;" +
		  "required int32 testType = 9;" +
		  "required string testTitle = 10;" +
		  "required int32 mset = 11;" +
		  "required int32 water = 12;" +
		  "required int32 speed = 13;" +
		  "required int32 engineSpeed = 14;" +
		  "required string dtcs = 15;" +
		  "required string obdData = 16;" +
		  "required bytes obdDataByte = 17;" +
		  "required string conclusion = 18;" +
		  "required string conclusionSrc = 19;" +
		  "required int64 conclusionTime = 20;" +
		  "required int64 ver = 21;" +
		  "required boolean del = 22;" +
		  "required string 4sbid = 23;" +
		  "required int64 updateTime = 24;" +
		  "}";	
		for(int i=0;i<index;i++)
			AddMessageProto(proto[i]);
		//
		return true;
	}


	/**
	 * 
	 */
	static public boolean AddMessageProto(String strProto)
	{
		if(msgArray == null)
			msgArray = new Vector<EzMessage>();
		int nPos1 = 0;
		int nPos2 = strProto.indexOf("}");
		while(nPos2 > 0)
		{
			String oneMessageProto = strProto.substring(nPos1,nPos2+1);	
			EzMessage msg = new EzMessage(oneMessageProto);
			if(msg.getName()!=null && msg.getID() != 0)
			{
				int i=0;
				for(i=0;i<msgArray.size();i++)
				{
					if(msgArray.get(i).getName().equals(msg.getName()))
					{
						msgArray.set(i, msg);
						break;
					}
				}
				if(i>=msgArray.size())
					msgArray.add(msg);
			}
			nPos1 = nPos2 + 1;
			nPos2 = strProto.indexOf("}",nPos1);
		}
		return true;
	}

	/**
	 * 
	 */
	/*static public String getAllMessageProto()
	{
		StringBuffer sb = new StringBuffer();
		for(int i=0;i<msgArray.size();i++)
		{
			EzMessage msg = msgArray.elementAt(i);
			if(msg != null)
				sb.append(msg.serializeToProto());
		}
		return sb.toString();
	}*/

	/**
	 * 
	 */
	static public void clear()
	{
		if(msgArray == null)
			return;
		msgArray.clear();
	}

	/**
	 * 
	 */
	static public EzMessage CreateMessageObject(String name)
	{
		if(msgArray == null)
			return null;
		for(int i=0;i<msgArray.size();i++)
		{
			EzMessage msg = msgArray.elementAt(i);
			if(msg.getName().equalsIgnoreCase(name))
			{
				return msg.clone();
			}
		}
		return null;
	}
	
	/**
	 * 
	 */
	static public EzMessage CreateMessageObject(int id)
	{
		if(msgArray == null)
			return null;
		if(id == 0)
			return null;
		for(int i=0;i<msgArray.size();i++)
		{
			EzMessage msg = msgArray.elementAt(i);
			if(msg.getID() == id)
			{
				return msg.clone();
			}
		}
		return null;
	}
	
	
	/**
	 */
	static public EzMessage[] CreateRpMessageObjects(int id,int nSize)
	{
		if(msgArray == null)
			return null;
		if(nSize<=0 || id == 0)
			return null;
		
		EzMessage[] msgs = new EzMessage[nSize];
		for(int i=0;i<msgArray.size();i++)
		{
			EzMessage msg = msgArray.elementAt(i);
			if(msg.getID() == id)
			{
				for(int j=0;j<msgs.length;j++)
				{
					msgs[j] = msg.clone();
				}
				return msgs;
			}
		}
		//
		return null;
	}

	/**
	 * create the message object from the package bytes.
	 * @param packBytes
	 * @return
	 */
	static public EzMessage CreateMessageObject(byte[] packBytes) {
		if(packBytes == null)
			return null;
		CodedInputStream input = CodedInputStream.newInstance(packBytes);
		try{
			int nid = input.readInt32();
			EzMessage msg = CreateMessageObject(nid);
			if(msg!=null)
			{
				if(msg.deSerializeFromPB(input))
					return msg;
				else
					return null;
			}
			else
			{
				msg = CreateMessageObject("msg.unknown");
				if(msg != null)
				{
					msg.getKVData("value").setValue(packBytes);
					return msg;
				}
				else return null;
			}
		}
		catch (Exception e)
	    {
	        e.printStackTrace(); 
	        return null;
	    }
	}

	/**
	 * create the repeated message objects,the messages's name is same.
	 */
	public static EzMessage[] CreateRpMessageObjects(byte[] byteArray) {		
		CodedInputStream input = CodedInputStream.newInstance(byteArray);
		try{
			int nSize = input.readInt32();
			if(nSize <=0)
				return null;
			EzMessage[] msgs = new EzMessage[nSize];
			for(int i=0;i<nSize;i++)
			{
				ByteString msgBytes = input.readBytes();
				if(msgBytes != null && !msgBytes.isEmpty())
					msgs[i] = CreateMessageObject(msgBytes.toByteArray());
				else
					msgs[i] = null;
			}
			return msgs;
		}
		catch (Exception e)
	    {
	        e.printStackTrace(); 
	        return null;
	    }
	}

	public static String getMessageName(int id) {
		if(msgArray == null)
			return null;
		if(id == 0)
			return null;
		for(int i=0;i<msgArray.size();i++)
		{
			EzMessage msg = msgArray.elementAt(i);
			if(msg.getID() == id)
			{
				return new String(msg.getName());
			}
		}
		return null;
	}
	
	public static int getMessageID(String name) {
		if(msgArray == null)
			return 0;
		if(name == null || name.equalsIgnoreCase("unknown") || name.equals(""))
			return 0;
		for(int i=0;i<msgArray.size();i++)
		{
			EzMessage msg = msgArray.elementAt(i);
			if(msg.getName().equalsIgnoreCase(name))
			{
				return msg.getID();
			}
		}
		return 0;
	}
}
