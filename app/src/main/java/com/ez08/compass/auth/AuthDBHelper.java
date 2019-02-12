package com.ez08.compass.auth;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class AuthDBHelper extends SQLiteOpenHelper {

	private static final String DB_NAME = "userauth_android_userauth.db";// appuid_modulename.db
	private static final int DB_VERSION = 3;

	private static AuthDBHelper dbHelper;

	public static AuthDBHelper getInstance(Context context) {
		if (dbHelper == null) {
			dbHelper = new AuthDBHelper(context);
		}
		return dbHelper;
	}

	private AuthDBHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {

		/* 创建当前用户信息�?*/
		String sqlUserInfo = "create table if not exists auth_user_info("
				+ "cid varchar(30) primary key not null," + "name varchar(50),"
				+ "realname varchar(50)," + "mobile varchar(30),"
				+ "imageid varchar(30)," + "brief varchar(100),"
				+ "sex integer," + "state integer," + "cfg_addFriend integer,"
				+ "country varchar(50)," + "cfg_descript varchar(100),"
				+ "province varchar(50)," + "login integer,"
				+ "city varchar(50)" + ")";

		String sqlFriendInfo = "create table if not exists friend_info("
				+ "cid varchar(30) primary key not null," + "name varchar(50),"
				+ "realname varchar(50)," + "mobile varchar(30),"
				+ "imageid varchar(30)," + "brief varchar(100),"
				+ "isfriend integer" + ")";
		db.execSQL(sqlUserInfo);
		db.execSQL(sqlFriendInfo);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		db.delete("auth_user_info", null, null);
		db.delete("friend_info", null, null);
		onCreate(db);
	}

	public static void recreateDB(Context context) {
		SQLiteDatabase db = AuthDBHelper.getInstance(context)
				.getWritableDatabase();

		// 删除数据表如果存在friend_info drop table if exists
		String sql = "DROP TABLE if exists friend_info;";
		db.execSQL(sql);
		AuthDBHelper.getInstance(context).onCreate(db);
	}

}

