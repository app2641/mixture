package com.app2641.mixture;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

	// database path
	private static String DB_PATH = "/data/data/com.app2641.mixture/databases/";
	
	// database name
	private static String DB_NAME = "mixture";
	
	// asset db data
	private static String ASSET_DB = "mixture.db";
	
	// context
	private final Context mContext;
	
	
	// constructar
	public DatabaseHelper(Context context) {
		super(context, DB_NAME, null, 1);
		this.mContext = context;
	}
	
	
	/**
	 * assetに格納したデータベースをコピーするため、空DBを生成する初期化処理
	 */
	public void init () throws IOException
	{
		boolean exists = isExistsDatabase();
		
		if (exists == false) {
			// 空のDBを生成するスーパークラスメソッド
			this.getReadableDatabase();
			
			// assetのデータをDBにコピーする
			copyDatabseFromAsset();
		}
	}
	
	/**
	 * 既にDBが作成されているかを判断する
	 * @return boolean
	 */
	public boolean isExistsDatabase ()
	{
		// db有無のフラグ
		boolean flag = true;
		
		try {
			SQLiteDatabase db = SQLiteDatabase.openDatabase(DB_PATH + DB_NAME, null, SQLiteDatabase.OPEN_READONLY);
			db.close();
			
		} catch (SQLiteException e) {
			flag = false;
		}
		
		return flag;
	}
	
	
	/**
	 * assetデータをDBにコピーする
	 */
	public void copyDatabseFromAsset ()
	{
		try {
			InputStream mInput = mContext.getAssets().open(ASSET_DB);
			OutputStream mOutput = new FileOutputStream(DB_PATH + DB_NAME);
			
			// コピー処理
			byte[] buffer = new byte[1024];
			int size;
			
			while ((size = mInput.read(buffer)) > 0) {
				mOutput.write(buffer, 0, size);
			}
			
			// streamを破棄
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
	
	// materialをidで取得
	public Cursor fetchByMaterialId (String id)
	{
		String sql = "SELECt * FROM material WHERE _id = ?";
		return this.executeSql(sql, new String[]{id});
	}
	
	// materialをclassで取得
	public Cursor fetchByMaterialClass (String cls)
	{
		String sql = "SELECT * FROM material WHERE class = ?";
		return this.executeSql(sql, new String[]{cls});
	}
	
	// itemをidで取得
	public Cursor fetchByItemId (String id)
	{
		String sql = "SELECt * FROM item WHERE _id = ?";
		return this.executeSql(sql, new String[]{id});
	}
	
	// itemをclassで取得
	public Cursor fetchByItemClass (String cls)
	{
		String sql = "SELECT * FROM item WHERE class = ?";
		return this.executeSql(sql, new String[]{cls});
	}
	
	// sql実行
	private Cursor executeSql (String sql, String[] bind)
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
