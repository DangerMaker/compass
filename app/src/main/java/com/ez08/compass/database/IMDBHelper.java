package com.ez08.compass.database;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;
import android.util.Log;
import com.ez08.compass.entity.SearchItem;
import com.ez08.compass.tools.FileUtils;
import com.ez08.compass.userauth.AuthUserInfo;
import com.ez08.support.net.EzMessage;

public class IMDBHelper extends SQLiteOpenHelper {

	private static final String IM_DB_NAME = "appuid_android_im";
	public static final int IM_VERSION = 3;
	private static IMDBHelper helper;
	public static final String IM_TABLE_CONVERSATION_HISTORY = "im_message_history";
	public static final String IM_TABLE_FRIEND_INFO = "im_friend_info";

	public static final String IM_TABLE_INFO_ID = "im_info_id";

	private IMDBHelper(Context context) {
		super(context, IM_DB_NAME, null, IM_VERSION);
	}

	public static IMDBHelper getInstance(Context context) {
		if (helper == null) {
			helper = new IMDBHelper(context);
		}
		return helper;
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		// TODO Auto-generated method stub
		buildCsTable(database);
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub

	}

	/**
	 * 创建所有应用都可能需要的客户服务数据库
	 * 
	 * @param database
	 */
	private static void buildCsTable(SQLiteDatabase database) {
		createMessageTable(database);
		createUserTable(IM_TABLE_FRIEND_INFO, database);
	}

	private String code;
	private String market;
	private String name;
	private int type;

	private static void createSearchHistory(SQLiteDatabase database) {
		database.execSQL("CREATE TABLE IF NOT EXISTS stock_search_history ("
				+ "_id integer primary key," + "cid text," + "code text,"
				+ "market text," + "name text," + "type integer);");
	}

	public void saveStockSearchHistory(SearchItem item, String cid) {
		createSearchHistory(getWritableDatabase());

		Cursor cursor = query("stock_search_history", null,
				"cid =? and code =? and market =?",
				new String[] { cid, item.getCode(), "1" }, null,
				null, "_id desc", null);
		if (cursor != null && cursor.moveToNext()) {
			return;
		}

		SQLiteDatabase database = getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put("code", item.getCode());
		values.put("market", "1");
		values.put("name", item.getName());
		values.put("type", "1");
		values.put("cid", cid);
		database.insert("stock_search_history", null, values);

		database.close();

	}

	public Cursor getStockSearchHistory(String cid) {
		createSearchHistory(getWritableDatabase());
		// return query("stock_search_history", null, "cid =?",
		// new String[] { cid }, null, null, "_id desc",
		// "1,7");

		String sql = "select * from stock_search_history where cid=" + cid
				+ " order by _id desc limit 0,7";

		return getWritableDatabase().rawQuery(sql, null);
	}

	public void clearSearchStockHistory(String cid) {
		getWritableDatabase().delete("stock_search_history", "cid = ?",
				new String[] { cid });
		// String sql="delete from stock_search_history where cid = "+cid;
		// getWritableDatabase().rawQuery(sql, null);
	}

	private static void createInfoId(SQLiteDatabase database) {
		database.execSQL("CREATE TABLE IF NOT EXISTS info_search_id ("
				+ "_id integer primary key," + "cid text," + "infoid text,"
				+  "type integer);");
	}

	public void saveInfoId(String infoId, String cid) {
		createInfoId(getWritableDatabase());

		Cursor cursor = query("info_search_id", null,
				"cid =? and infoid =? and type =?",
				new String[] { cid, infoId, "1" }, null,
				null, "_id desc", null);
		if (cursor != null && cursor.moveToNext()) {
			return;
		}

		SQLiteDatabase database = getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put("infoid", infoId);
		values.put("type", "1");
		values.put("cid", cid);
		database.insert("info_search_id", null, values);

		database.close();

	}

	public Cursor getInfoIdList(String cid) {
		createInfoId(getWritableDatabase());
		// return query("stock_search_history", null, "cid =?",
		// new String[] { cid }, null, null, "_id desc",
		// "1,7");

		String sql = "select * from info_search_id where cid=" + cid
				+ " order by _id desc limit 0,1000";

		return getWritableDatabase().rawQuery(sql, null);
	}

	public void clearInfoId(String cid) {
		getWritableDatabase().delete("stock_search_history", "cid = ?",
				new String[] { cid });
		// String sql="delete from stock_search_history where cid = "+cid;
		// getWritableDatabase().rawQuery(sql, null);
	}

	private static void createMessageTable(SQLiteDatabase database) {
		createSearchHistory(database);
		database.execSQL("DROP TABLE IF EXISTS cs_table");
		database.execSQL("CREATE TABLE IF NOT EXISTS im_message_history ("
				+ "_id INTEGER PRIMARY KEY," + "loginCid TEXT,"// 当前登录的用户的cid
				+ "target TEXT," // 沟通对象，他可能有自动助理
				+ "fromcid TEXT," // 从哪里来，可能是客服发，也可能是助理发
				+ "personCid TEXT," // 对方的cid（机器人cid是sys.robot. 加上personCid

				// 消息类别，0 收到的消息，1 发送的消息
				+ "type INTEGER,"

				// + "test INTEGER," //增加一个测试用字段
				/**
				 * 处理标志， 0 未读， /未发送 ； 1 已读， /已发送;
				 */
				+ "state INTEGER,"

				+ "localTime INTEGER,"// 保存到数据库中时的本地时间
				+ "time INTEGER,"// 消息发送时的时间，对应发送者的本地时间

				// 以下为消息内容
				+ "msg TEXT," // 消息内容,消息体中名字为text，与数据库定义冲突，所以修改为msg
				+ "image BOLB,"// Byte[ ] 可选，图片
				+ "imageurl	TEXT,"// String
									// 可选，网络图片地址，如果image和imageurl同时出现，image代表缩略图，点击后，将访问imageurl对应的详细大图；
				+ "audio BOLB,"// Byte[ ] 可选，音频数据或网络音频地址
				+ "audiourl		TEXT,"// String
				+ "vedio BOLB,"// Byte[ ] 可选，视频数据或网络视频地址
				+ "vediourl	TEXT,"//
				+ "custom BOLB,"// KeyValue 可选，Key为dataid,用于客户端识别数据类型；
								// Value为byte[],自定的数据内容；
				+ "operate BOLB,"// EzMessage[ ]
				// (EzIntent[ ]) 可选，远程操作指令，一个或多个;
				// 具体内容为EzIntent,可以为任何操作；
				// 参数为操作需要的参数，和具体业务设计有关；
				+ "view	BOLB,"// EzMessage[ ]
				// (EzIntent[ ]) 可选，一个控件描述，一个或多个
				// 具体内容为EzIntent,如参数定义如下：
				// action为ez08.show.setlayout
				// 参数layout：String 布局文件名称
				// 其他参数为布局文件需要的参数，和具体业务设计有关；
				+ "btntype	INTEGER," // 可选，默认值为0，
				// 0：按钮在选择后，自动消失；
				// 1：按钮在选择后，保持不变，可以再次被选择；
				// 其他待扩展；
				+ "btnclicked	INTEGER," // ，默认值为0，为1表示按钮已经点击过，不用再显示了

				+ "buttons	BOLB,"// EzMessage[ ]
				// (EzIntent数组) 可选，如果有值表示对话内容携带按钮,
				// 具体内容为 EzIntent,每个按钮的具体定义如下：
				// 参数text：String 按钮名称
				// 参数cmd：EzMessage[ ](EzIntent数组)
				// 一个按钮可以携带一个或者多个指令消息，按顺序执行；
				+ "validtype INTEGER" // Int32 可选，默认值为0，
				// 0：表示一直有效，当对方不在线，将离线保存，等待上线后，自动推送；
				// 1：表示在线有效，如果对方不在线，无法立刻接收到，将被丢弃掉；
				// 其他类型待扩展；

				+ ");");
	}

	public static void createUserTable(String table, SQLiteDatabase database) {

		database.execSQL("DROP TABLE IF EXISTS " + table);
		database.execSQL("CREATE TABLE IF NOT EXISTS "
				+ table
				+ " ("
				+ "_id INTEGER PRIMARY KEY,"
				+ "loginCid TEXT,"// 当前登录的用户的cid
				+ "cid TEXT,"
				+ "name TEXT,"
				+ "sex INTEGER,"
				+ "mobile TEXT,"
				+ "oid TEXT,"
				+ "oidName TEXT,"
				+ "bid TEXT,"
				+ "bidName TEXT,"
				+ "image BOLB,"
				+ "imageid TEXT,"
				+ "country TEXT,"
				+ "province TEXT,"
				+ "city TEXT,"
				+ "cancall INTEGER,"
				+ "actor TEXT,"
				+ "type INTEGER,"// 类别0--朋友 1--特约专家 2--用户 3--系统消息
				+ "online INTEGER,"// 是否在线，如果本机未登录，则不显示连线信息
				+ "verify INTEGER,"// 是否认证，目前有单位的都会认证，车友不能认证
				+ "brief TEXT,"
				+ "certa TEXT,"
				+ "certb TEXT,"
				+ "support INTEGER,"// 是否有新消息
				+ "against INTEGER,"// 是否有新消息

				+ "iscs INTEGER,"// 是否客服，1--客服，0--用户

				+ "isfriend INTEGER," + "isappoint INTEGER,"
				+ "isblock INTEGER," + "isfollow INTEGER,"
				+ "isappointed INTEGER," + "isfollowed INTEGER,"
				+ "islocked INTEGER,"// 锁定，这样的不会被删除，主要是自己和系统助理的信息

				+ "detail INTEGER,"// ==1 表示已经有本用户详细信息了
				+ "deleteMark INTEGER"// 这时为避免在更新用户信息时删除该用户发送的消息条数
				+ ");");
		// saveFriendInfo("系统助理", "sys.robot.assistant", "", "",
		// "我是您的助理，诚挚为您服务！");

	}


	public void saveUserDetailInfo(EzMessage user) {

		if (user == null)
			return;
		String cid = user.getKVData("cid").getString();
		ContentValues cv = getContentValuesByMessage(user);
		boolean isFriend = user.getKVData("isfriend").getBoolean();
		boolean isBlocked = user.getKVData("isblock").getBoolean();
		boolean isAppoint = user.getKVData("isappoint").getBoolean();
		boolean isAppointed = user.getKVData("isappointed").getBoolean();
		boolean isfollow = user.getKVData("isfollow").getBoolean();
		boolean isfollowed = user.getKVData("isfollowed").getBoolean();

		cv.put("loginCid", AuthUserInfo.getMyCid());

		if (isFriend)
			cv.put("isfriend", 1);
		else
			cv.put("isfriend", 0);

		if (isBlocked)
			cv.put("isblock", 1);
		else
			cv.put("isblock", 0);

		if (isAppoint)
			cv.put("isappoint", 1);
		else
			cv.put("isappoint", 0);

		if (isAppointed)
			cv.put("isappointed", 1);
		else
			cv.put("isappointed", 0);

		if (isfollow)
			cv.put("isfollow", 1);
		else
			cv.put("isfollow", 0);

		if (isfollowed)
			cv.put("isfollowed", 1);
		else
			cv.put("isfollowed", 0);

		cv.put("detail", 1);
		update(IMDBHelper.IM_TABLE_FRIEND_INFO, cv, "cid LIKE ?",
				new String[] { cid });

	}

	private ContentValues getContentValuesByMessage(EzMessage user) {
		String cid = user.getKVData("cid").getString();
		String name = user.getKVData("name").getString();
		String mobile = user.getKVData("mobile").getString();
		String oid = user.getKVData("oid").getString();
		String oidname = user.getKVData("oidname").getString();
		String bid = user.getKVData("bid").getString();
		String bidname = user.getKVData("bidname").getString();
		String country = user.getKVData("country").getString();
		String city = user.getKVData("city").getString();
		String province = user.getKVData("province").getString();
		String actor = user.getKVData("actor").getString();
		String certa = user.getKVData("certa").getString();
		String certb = user.getKVData("certb").getString();
		String brief = user.getKVData("brief").getString();
		int support = user.getKVData("support").getInt32();
		int against = user.getKVData("against").getInt32();
		int sex = user.getKVData("sex").getInt32();
		boolean online = user.getKVData("online").getBoolean();
		boolean cancall = user.getKVData("cancall").getBoolean();
		boolean verify = user.getKVData("verify").getBoolean();
		byte[] image = user.getKVData("image").getBytes();
		String imageid = user.getKVData("imageid").getString();
		int iscs = user.getKVData("iscs").getInt32();
		ContentValues cv = new ContentValues();

		cv.put("loginCid", AuthUserInfo.getMyCid());
		cv.put("cid", cid);
		cv.put("name", name);
		cv.put("mobile", mobile);
		cv.put("oidname", oidname);
		cv.put("bid", bid);
		cv.put("bidname", bidname);
		cv.put("country", country);
		cv.put("city", city);
		cv.put("province", province);
		cv.put("imageid", imageid);
		cv.put("actor", actor);
		cv.put("certa", certa);
		cv.put("certb", certb);
		cv.put("brief", brief);
		cv.put("verify", verify);
		cv.put("support", support);
		cv.put("against", against);
		cv.put("sex", sex);
		cv.put("oid", oid);
		cv.put("iscs", iscs);
		cv.put("islocked", iscs);
		if (online)
			cv.put("online", 1);
		else
			cv.put("online", 0);
		if (cancall)
			cv.put("cancall", 1);
		else
			cv.put("cancall", 0);

		cv.put("image", image);
		return cv;
	}

	public Cursor query(String table, String[] columns, String selection,
                        String[] selectionArgs, String groupBy, String having,
                        String orderBy, String limit) {
		Cursor cursor = null;
		SQLiteDatabase database;
		try {
			database = getWritableDatabase();
			cursor = database.query(table, columns, selection, selectionArgs,
					groupBy, having, orderBy);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return cursor;
	}

	/**
	 * 获取私聊消息数据库的cursor
	 * 
	 * 
	 */
	public Cursor getConversationTableCursor(String targetCid) {
		String selection = "";
		// selection = "personCid LIKE '" + targetCid + "'";
		if (TextUtils.isEmpty(AuthUserInfo.getMyCid())) {
			return null;
		}
		selection = "loginCid LIKE '" + AuthUserInfo.getMyCid()
				+ "' AND personCid LIKE '" + targetCid + "'";
		return query(IMDBHelper.IM_TABLE_CONVERSATION_HISTORY, null, selection,
				null, null, null, null, null);
	}
	
	public void initTotalCount(String targetCid){
		if (TextUtils.isEmpty(AuthUserInfo.getMyCid())) {
			return;
		}
		String total_sql = "select * from "
				+ IMDBHelper.IM_TABLE_CONVERSATION_HISTORY
				+ " where loginCid LIKE '" + AuthUserInfo.getMyCid()
				+ "' AND personCid LIKE '" + targetCid + "'";
		totalCount=getWritableDatabase().rawQuery(total_sql, null).getCount();
	}
	
	/**
	 * @param targetCid 目标id
	 * @param currentpage 当前页
	 * @param pagesize 每页显示数目
	 * @return
	 */
	public Cursor getConversationTotalTableCursor(String targetCid, int currentpage, int pagesize){
		if (TextUtils.isEmpty(AuthUserInfo.getMyCid())) {
			return null;
		}
//		String sql = "select * from "
//				+ IMDBHelper.IM_TABLE_CONVERSATION_HISTORY
//				+ " where loginCid LIKE '" + AuthUserInfo.getMyCid()
//				+ "' AND personCid LIKE '" + targetCid + "'" + "order by _id desc limit 0,10";
		int pagecount=totalCount%pagesize==0?totalCount/pagesize:(totalCount/pagesize+1);
		String sql = "select * from "
				+ IMDBHelper.IM_TABLE_CONVERSATION_HISTORY
				+ " where loginCid LIKE '" + AuthUserInfo.getMyCid()
				+ "' AND personCid LIKE '" + targetCid + "'" + " limit "+(totalCount-(currentpage+1)*(pagesize))+","+pagesize*(currentpage+1);
		
		return getWritableDatabase().rawQuery(sql, null);
	}
	private static int totalCount=0;
	/**
	 * @param userCid
	 * @return
	 */
	public boolean deleteConversationHistory(String userCid) {
		boolean flag = false;
		SQLiteDatabase database;
		int affectedRow = 0;
		try {
			database = getWritableDatabase();
			affectedRow = database.delete(IM_TABLE_CONVERSATION_HISTORY,
					"personCid" + "=?", new String[] { userCid });
			flag = affectedRow == 0 ? false : true;
			System.out.println(affectedRow);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}

	public boolean deleteAll(String tarID){
		boolean flag = false;
		SQLiteDatabase database;
		int affectedRow = 0;
		try {
			database = getWritableDatabase();
			affectedRow=database.delete(IM_TABLE_CONVERSATION_HISTORY,"loginCid=? and personCid=? ",new String[]{AuthUserInfo.getMyCid(),tarID});
			flag = affectedRow == 0 ? false : true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return flag;
	}
	
	/**
	 * 清除缓存
	 * @return
	 */
	public boolean deleteAllCache(){
		boolean flag = false;
		SQLiteDatabase database;
		int affectedRow = 0;
		try {
			database = getWritableDatabase();
			affectedRow=database.delete(IM_TABLE_CONVERSATION_HISTORY,"loginCid=? ",new String[]{AuthUserInfo.getMyCid()});
			flag = affectedRow == 0 ? false : true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return flag;
	}
	
	public boolean deleteItem(String id){
		boolean flag = false;
		SQLiteDatabase database;
		int affectedRow = 0;
		try {
			database = getWritableDatabase();
			affectedRow=database.delete(IM_TABLE_CONVERSATION_HISTORY,"localTime=? ",new String[]{id});
			flag = affectedRow == 0 ? false : true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return flag;
	}
	
	public boolean insert(String table, String nullColumnHack,
                          ContentValues values) {
		boolean flag = false;
		SQLiteDatabase database;
		long id = -1;
		try {
			database = getWritableDatabase();
			id = database.insert(table, nullColumnHack, values);
			flag = (id != -1 ? true : false);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}

	public boolean delete(){
		boolean flag = false;
		 SQLiteDatabase db = getWritableDatabase();
		  db.delete("person", "personid<?", new String[]{"2"});
		  db.close();
		  
		  return flag;
	}
	
	/**
	 * 保存私聊信息
	 * 
	 * @param intent
	 * @param send
	 * @param localTime
	 */
	public void saveMessage(Intent intent, boolean send, long localTime) {
		ContentValues cv = new ContentValues();
		cv.put("loginCid", AuthUserInfo.getMyCid());

		String personCid = null;
		String target = null;
		// 私聊
		// 群聊中，表里面的target对方cid，from表示发送消息的人的cid，即自己的cid
		// target = intent.getStringExtra("target");
		// //--------------解决策略：如果target为空，证明是从房间传过来的，这时把roomid赋给target
		// if(TextUtils.isEmpty(target)){
		// target=intent.getStringExtra("roomid");
		//
		// }
		// String roomid=intent.getStringExtra("roomid");

		// if(!TextUtils.isEmpty(roomid)){
		// target=roomid;
		// }else{
		// target = intent.getStringExtra("target");
		// }
		String from = intent.getStringExtra("from");
		String roomid = intent.getStringExtra("roomid");
		// ---------
		if (send) {
			personCid = intent.getStringExtra("target");
			cv.put("type", 1);// 这是我发出的消息
		} else {
			if (TextUtils.isEmpty(roomid)) {
				personCid = intent.getStringExtra("from");
			} else {
				personCid = roomid;
			}
			cv.put("type", 0);
		}

		if (personCid == null)
			return;

		cv.put("personCid", personCid);

		cv.put("target", target);

		if (!TextUtils.isEmpty(roomid)) {
			from = roomid;
		}

		if (from == null)
			from = AuthUserInfo.getMyCid();
		cv.put("fromcid", from);

		cv.put("localTime", localTime);
		cv.put("time", intent.getLongExtra("time", 0));
		cv.put("state", 0);
		byte[] bytes = null;
		// 文字消息
		String text = intent.getStringExtra("text");
		if (!TextUtils.isEmpty(text)) {
			for (int i = 0; i < FileUtils.SEND_ALI.length; i++) {
				if (text.contains(FileUtils.SEND_ALI[i])) {
					text = text.replace(FileUtils.SEND_ALI[i], "["
							+ FileUtils.EMOS_ALI[i] + "]");
				}
			}
			cv.put("msg", text);
		}
		Log.e("roomid", roomid + "=roomid=" + target + "@@" + text);
		// 图片
		bytes = intent.getByteArrayExtra("image");
		if (bytes != null)
			cv.put("image", bytes);
		text = intent.getStringExtra("imageurl");
		if (!TextUtils.isEmpty(text))
			cv.put("imageurl", text);
		// 音频
		bytes = intent.getByteArrayExtra("audio");
		if (bytes != null)
			cv.put("audio", bytes);
		text = intent.getStringExtra("audiourl");
		if (text != null)
			cv.put("audiourl", text);
		// 视频
		bytes = intent.getByteArrayExtra("vedio");
		if (bytes != null)
			cv.put("vedio", bytes);
		text = intent.getStringExtra("vediourl");
		if (text != null)
			cv.put("vediourl", text);
		// 自定义
		bytes = intent.getByteArrayExtra("custom");
		if (bytes != null)
			cv.put("custom", bytes);
		// 远程操作
		bytes = intent.getByteArrayExtra("operate");
		if (bytes != null)
			cv.put("operate", bytes);
		// 嵌入控件
		bytes = intent.getByteArrayExtra("view");
		if (bytes != null)
			cv.put("view", bytes);
		// 按钮类别
		int type = intent.getIntExtra("btntype", 0);
		cv.put("btntype", type);

		// 发送，1-不在线则抛弃
		type = intent.getIntExtra("validtype", 0);
		cv.put("validtype", type);
		insert(IMDBHelper.IM_TABLE_CONVERSATION_HISTORY, "target", cv);

	}

	public boolean update(String table, ContentValues values,
                          String whereClause, String[] whereArgs) {
		boolean flag = false;
		SQLiteDatabase database;
		int affectedRow = 0;
		try {
			database = getWritableDatabase();
			affectedRow = database
					.update(table, values, whereClause, whereArgs);
			flag = affectedRow == 0 ? false : true;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return flag;
	}
}
