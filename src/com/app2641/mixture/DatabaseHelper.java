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
	 * asset�Ɋi�[�����f�[�^�x�[�X���R�s�[���邽�߁A��DB�𐶐����鏉��������
	 */
	public void init () throws IOException
	{
		boolean exists = isExistsDatabase();
		
		if (exists == false) {
			// ���DB�𐶐�����X�[�p�[�N���X���\�b�h
			this.getReadableDatabase();
			
			// asset�̃f�[�^��DB�ɃR�s�[����
			copyDatabseFromAsset();
		}
	}
	
	/**
	 * ����DB���쐬����Ă��邩�𔻒f����
	 * @return boolean
	 */
	public boolean isExistsDatabase ()
	{
		// db�L���̃t���O
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
	 * asset�f�[�^��DB�ɃR�s�[����
	 */
	public void copyDatabseFromAsset ()
	{
		try {
			InputStream mInput = mContext.getAssets().open(ASSET_DB);
			OutputStream mOutput = new FileOutputStream(DB_PATH + DB_NAME);
			
			// �R�s�[����
			byte[] buffer = new byte[1024];
			int size;
			
			while ((size = mInput.read(buffer)) > 0) {
				mOutput.write(buffer, 0, size);
			}
			
			// stream��j��
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
	
	// material��id�Ŏ擾
	public Cursor fetchByMaterialId (String id)
	{
		String sql = "SELECt * FROM material WHERE _id = ?";
		return this.executeSql(sql, new String[]{id});
	}
	
	// material��class�Ŏ擾
	public Cursor fetchByMaterialClass (String cls)
	{
		String sql = "SELECT * FROM material WHERE class = ?";
		return this.executeSql(sql, new String[]{cls});
	}
	
	// item��id�Ŏ擾
	public Cursor fetchByItemId (String id)
	{
		String sql = "SELECt * FROM item WHERE _id = ?";
		return this.executeSql(sql, new String[]{id});
	}
	
	// item��class�Ŏ擾
	public Cursor fetchByItemClass (String cls)
	{
		String sql = "SELECT * FROM item WHERE class = ?";
		return this.executeSql(sql, new String[]{cls});
	}
	
	// sql���s
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
