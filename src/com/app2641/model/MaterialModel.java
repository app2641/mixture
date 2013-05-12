package com.app2641.model;

import java.util.Random;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

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
		
		db.execSQL(sql, new String[]{String.valueOf(qty), String.valueOf(id)});
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
			"where material.class = ? and material.experience = ? and material.qty > ? ";
		
		if (sort == "date") {
			sql += "order by material.last_date desc";
		} else if (sort == "name") {
			sql += "order by material.name asc";
		} else if (sort == "qty") {
			sql += "order by material.qty desc";
		}
		
		Cursor cursor = this.executeSQL(db, sql, new String[]{cls, "1", "0"});
		return cursor;
	}
	
	
	/**
	 * 指定idの素材とミックスイン可能な所持素材リストを取得する
	 */
	public Cursor fetchAllSecondeMaterialById (SQLiteDatabase db, int first_id) {
		String sql = "select m1._id, m1.name as m1_name, m1.qty as m1_qty, " +
			"m2._id as m2_id, m2.name as m2_name, m2.last_date as m2_late_date, m2.qty as m2_qty, " + 
			"mixin.experience from mixin " +
			"inner join material m1 on m1._id = mixin.material1_id " +
			"inner join material m2 on m2._id = mixin.material2_id " +
			"where mixin.material1_id = ? and m2.qty > ? " +
			"order by m2.qty desc";
		
		Cursor cursor = this.executeSQL(db, sql, new String[]{String.valueOf(first_id), "0"});
		return cursor;
 	}
}
