package com.app2641.model;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteCantOpenDatabaseException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

	// database path
	private static String DB_PATH = "/data/data/com.app2641.mixture/databases/";
	
	// database name
	private static String DB_NAME = "mixture";
	
	// asset db data
	private static String ASSET_DB = "mixture_app.sqlite";
	
	// context
	private final Context mContext;
	
	
	// constructar
	public DatabaseHelper(Context context) {
		super(context, DB_NAME, null, 1);
		this.mContext = context;
	}
	
	
	/**
	 * asset
	 */
	public void init () throws IOException
	{
		boolean exists = isExistsDatabase();
		
		if (exists == false) {
			// DB
			this.getReadableDatabase();
			
			// assetフォルダのデータファイルからDBに投入
			copyDatabseFromAsset();
		}
	}
	
	/**
	 * @return boolean
	 */
	public boolean isExistsDatabase ()
	{
		// dbフラグ
		boolean flag = true;
		
		try {
			SQLiteDatabase db = SQLiteDatabase.openDatabase(DB_PATH + DB_NAME, null, SQLiteDatabase.OPEN_READONLY);
			db.close();
			
		} catch (SQLiteCantOpenDatabaseException e) {
			flag = false;
		}
		
		return flag;
	}
	
	
	public void copyDatabseFromAsset ()
	{
		try {
			InputStream mInput = mContext.getAssets().open(ASSET_DB);
			OutputStream mOutput = new FileOutputStream(DB_PATH + DB_NAME);
			
			byte[] buffer = new byte[1024];
			int size;
			
			while ((size = mInput.read(buffer)) > 0) {
				mOutput.write(buffer, 0, size);
			}
			
			mOutput.flush();
			mOutput.close();
			mInput.close();
			
		} catch (IOException e) {
			throw new Error("Error copying database");
		}
	}
	
	
	/*********************
	 * sql list
	 *********************/
	
	public Cursor fetchByMaterialId (String id)
	{
		String sql = "SELECt * FROM material WHERE _id = ?";
		return this.executeSql(sql, new String[]{id});
	}
	
	public Cursor fetchByMaterialClass (String cls)
	{
		String sql = "SELECT * FROM material WHERE class = ?";
		return this.executeSql(sql, new String[]{cls});
	}
	
	public Cursor fetchByItemId (String id)
	{
		String sql = "SELECt * FROM item WHERE _id = ?";
		return this.executeSql(sql, new String[]{id});
	}
	
	public Cursor fetchByItemClass (String cls)
	{
		String sql = "SELECT * FROM item WHERE class = ?";
		return this.executeSql(sql, new String[]{cls});
	}
	
	public Cursor executeSql (String sql, String[] bind)
	{
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor c = db.rawQuery(sql, bind);
		return c;
	}
	
	

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

}
