package com.app2641.model;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class MixinModel extends AbstractModel {

	/**
	 * 指定した素材1idと素材2idからアイテムレコードを取得する
	 */
	public Cursor fetchItemByMaterialIds (SQLiteDatabase db, int m1_id, int m2_id) {
		String sql = "select item._id, item.name, item.qty from mixin " +
			"inner join item on item._id = mixin.item_id " +
			"where mixin.m1_id = ? and mixin.m2_id = ?";
		
		return this.executeSQL(db, sql, new String[]{String.valueOf(m1_id), String.valueOf(m2_id)});
	}
	
}
