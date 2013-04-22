package com.app2641.model;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class ItemModel extends AbstractModel {

	
	/**
	 * 指定idのアイテムレコードを取得する
	 */
	public Cursor fetchById (SQLiteDatabase db, int id)
	{
		String sql = "select * from item where item._id = ?";
		Cursor cursor = this.executeSQL(db, sql, new String[]{String.valueOf(id)});
		
		return cursor;
	}
	
	
	
	/**
	 * 存在するアイテムidを全取得する
	 */
	public Cursor countItems (SQLiteDatabase db)
	{
		String sql = "select item._id from item";
		Cursor cursor = this.executeSQL(db, sql, new String[]{});
		
		return cursor;
	}
	
	
	
	/**
	 * 生成経験のあるアイテムidを全取得する
	 */
	public Cursor countExperienceItems (SQLiteDatabase db)
	{
		String sql = "select item._id from item " +
			"where item.experience = ?";
		Cursor cursor = this.executeSQL(db, sql, new String[]{"1"});
		
		return cursor;
	}
	
}
