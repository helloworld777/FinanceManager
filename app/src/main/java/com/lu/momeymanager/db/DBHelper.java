package com.lu.momeymanager.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.lu.momeymanager.util.LogUtil;

public class DBHelper extends SQLiteOpenHelper {
	private final static String DB_NAME = "lu";
	private static final int DATABASE_VERSION = 5;
	public static final String TABLE_NAME = "money";
	public static final String TABLE_BANK="bank";
	private static final String TAB_MONEY_SQL = "CREATE TABLE money (" + "_id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," + "number double," + "bank VARCHAR(100),"
			+ "cardNumber varchar(100)," + "date VARCHAR(50), note  varchar(100),detail varchar);";
	private static final String TAB_BANK_SQL = "CREATE TABLE bank (" + "_id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +"bank VARCHAR(100),"
			+ "cardNumber varchar(100)," + "date VARCHAR(50), balance  double);";
	
//	private SQLiteDatabase db;
	private static final String TAG = "DBHelper";

	public DBHelper(Context context) {
		super(context, DB_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(TAB_MONEY_SQL);
		db.execSQL(TAB_BANK_SQL);
		LogUtil.d(TAG, "****************TAB_MONEY_SQL is create");
	}
	public void createBankTable(SQLiteDatabase db){
		db.execSQL("IF NOT EXISTS"+TAB_BANK_SQL);
	}
	@Override
	public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
		LogUtil.d(TAG, "****************onUpgrade");
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
		db.execSQL(TAB_MONEY_SQL);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_BANK);
		db.execSQL(TAB_BANK_SQL);
	}

}
