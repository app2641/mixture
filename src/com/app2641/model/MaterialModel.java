package com.app2641.model;

import java.util.Random;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class MaterialModel extends AbstractModel {

	
	/*
	 * 指定クラス、指定レア度の素材をランダムで取得する
	 * int rarityが0の場合はレア度に関係なくクラス全体が対象となる
	 */
	public Cursor getRarityMaterial (SQLiteDatabase db, String cls, int rarity)
	{
		String sql = "select material.id from material " +
			"where material.class = ? ";
		String[] bind = new String[]{cls};
		
		// レア度の指定がある場合はクエリを追加修正
		if (rarity != 0) {
			sql += "and material.rarity = ?";
			bind = new String[]{cls, String.valueOf(rarity)};
		}
		
		Cursor cursor = this.executeSQL(db, sql, bind);
		
		Random random = new Random();
		int index = random.nextInt(cursor.getCount()) + 1;
		
		cursor.moveToFirst();
		cursor.move(index);
		
		return cursor;
	}
	
}
