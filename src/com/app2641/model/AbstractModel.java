package com.app2641.model;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class AbstractModel {

	/*
	 * ReadableDatabaseでクエリを実行する
	 */
	public Cursor executeSQL (SQLiteDatabase db, String sql, String[] bind)
	{
		Cursor cursor = db.rawQuery(sql, bind);
		return cursor;
	}
	
}
