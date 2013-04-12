package com.app2641.model;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class CodeModel extends AbstractModel {

	
	/*
	 * コードとレベルを指定して既に取得したコードが判別する
	 */
	public Cursor fetchByMaterialByCode (SQLiteDatabase db, String code, int level)
	{
		String sql = "select _id, material_id, rare_id from code " +
			"where code = ? and level = ?";
		
		String[] bind = new String[]{code, String.valueOf(level)};
		Cursor cursor = this.executeSQL(db, sql, bind);
		
		return cursor;
	}
}
