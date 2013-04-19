package com.app2641.model;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class ShopModel extends AbstractModel {

	
	/**
	 * オプラインショップの品目を返す
	 */
	public Cursor fetchOffLineShopList (SQLiteDatabase db)
	{
		String sql = "select material._id, material.name, material.class, " +
			"material.price, material.qty from shop " +
			"inner join shop.material_id = material._id " +
			"where shop.online = ?";
		Cursor cursor = this.executeSQL(db, sql, new String[]{"0"});
		
		return cursor;
	}
}
