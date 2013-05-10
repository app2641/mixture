package com.app2641.model;

import java.util.Random;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class MaterialModel extends AbstractModel {

	/**
	 * 指定idの素材レコードを取得する
	 */
	public Cursor fetchById (SQLiteDatabase db, int id)
	{
		String sql = "select * from material where _id = ?";
		Cursor cursor = this.executeSQL(db, sql, new String[]{String.valueOf(id)});
		
		return cursor;
	}
	
	
	
	
	/**
	 * 指定クラス、指定レア度の素材をランダムで取得する
	 * int rarityが0の場合はレア度に関係なくクラス全体が対象となる
	 */
	public Cursor getRarityMaterial (SQLiteDatabase db, String cls, int rarity)
	{
		String sql = "select material._id from material " +
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



	/**
	 * 指定素材のQtyを更新する
	 */
	public void updateQty (SQLiteDatabase db, int qty, int id) {
		String sql = "update material set qty = ? " +
			"where _id = ?";
		
		this.executeSQL(db, sql, new String[]{String.valueOf(qty), String.valueOf(id)});
	}
	
	
	
	/**
	 * 存在する素材idを全取得する
	 */
	public Cursor countMaterials (SQLiteDatabase db)
	{
		String sql = "select material._id from material";
		Cursor cursor = this.executeSQL(db, sql, new String[]{});
		
		return cursor;
	}
	
	
	
	/**
	 * 取得経験のある素材idを全取得する
	 */
	public Cursor countExperienceMaterial (SQLiteDatabase db)
	{
		String sql = "select material._id from material " +
			"where material.experience = ?";
		Cursor cursor = this.executeSQL(db, sql, new String[]{"1"});
		
		return cursor;
	}
	
	
	
	/**
	 * 指定クラスの所持素材を指定ソート順で全取得する
	 */
	public Cursor fetchAllByClassWithSort (SQLiteDatabase db, String cls, String sort)
	{
		String sql = "select material._id, material.name, material.last_date, material.qty from material " +
			"where material.class = ? and material.experience = ? ";
		
		if (sort == "date") {
			sql += "order by material.last_date desc";
		} else if (sort == "name") {
			sql += "order by material.name desc";
		} else if (sort == "qty") {
			sql += "order by material.qty desc";
		}
		
		Cursor cursor = this.executeSQL(db, sql, new String[]{cls, "1"});
		return cursor;
	}
}
