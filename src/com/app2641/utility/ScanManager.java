package com.app2641.utility;

import java.util.ArrayList;
import java.util.Random;

import com.app2641.model.CodeModel;
import com.app2641.model.DatabaseHelper;
import com.app2641.model.MaterialModel;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;

public class ScanManager {

	private Context mContext;
	
	public int level;
	
	
	public ScanManager (Context context)
	{
		mContext = context;
		
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
		level = sp.getInt("LEVEL", 1);
	}
	
	
	/*
	 * レアスキャンを行うかどうかのフラグを返す
	 */
	private boolean _getRareScanFlag ()
	{
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(mContext);
		boolean master = sp.getBoolean("MASTER", false);
		boolean first_scan = sp.getBoolean("FIRST_SCAN", false);
		
		// first_scanフラグがfalseの場合はレアスキャンさせない。
		if (first_scan == false) {
			return false;
		}
		
		
		// レアスキャンの確率
		// 調合師の極意を所持している場合は15% 不所持ならば5%
		int prob = (master == true) ? 15: 5;
		
		// 乱数の生成
		int rand = this._generateRandomInt();
		
		// レアスキャン判定
		// 乱数が確率値以下ならレアスキャン
		boolean rare_flag = (rand <= prob) ? true: false;
		return rare_flag;
	}
	
	
	
	/**
	 * 指定したバーコードに対応する素材IDを返す
	 */
	public ContentValues fetchMaterialIdByCode (String code)
	{
		DatabaseHelper helper = new DatabaseHelper(mContext);
		SQLiteDatabase db = helper.getWritableDatabase();
		
		ContentValues values = null;
		
		try {
			db.beginTransaction();
			
			// レアスキャンフラグを取得する
			boolean rare_flag = this._getRareScanFlag();
			
			// 指定コードに対応する素材を取得する
			CodeModel code_model = new CodeModel();
			Cursor code_cursor = code_model.fetchByMaterialByCode(db, code, level);
			int id;
			
			// 指定コードで取得した実績があるかどうか
			if (code_cursor.moveToFirst()) {
				if (rare_flag == true) {
					// レアスキャンが成功した場合
					if (code_cursor.isNull(code_cursor.getColumnIndex("rare_id"))) {
						id = this._generateRareId(db);
						int code_id = code_cursor.getInt(code_cursor.getColumnIndex("_id"));
						
						// コードとrare_idを紐付ける
						String sql = "update code set rare_id = ? " +
							"where _id = ?;";
						String[] bind = new String[]{String.valueOf(id), String.valueOf(code_id)};
						
						db.execSQL(sql, bind);
						
					} else {
						id = code_cursor.getInt(code_cursor.getColumnIndex("rare_id"));
					}
					
					// TOTAL_RARE定数を更新
					_updateTotalConstant("TOTAL_RARE");
					
				} else {
					// 通常スキャンの場合
					if (code_cursor.isNull(code_cursor.getColumnIndex("material_id"))) {
						id = this._generateMaterialId(db);
						int code_id = code_cursor.getInt(code_cursor.getColumnIndex("_id"));
						
						// コードとmaterial_idを紐付ける
						String sql = "update code set material_id = ? " +
							"where _id = ?;";
						String[] bind = new String[]{String.valueOf(id), String.valueOf(code_id)};
						
						db.execSQL(sql, bind);
						
					} else {
						id = code_cursor.getInt(code_cursor.getColumnIndex("material_id"));
					}
					
					// TOTAL_SCAN定数を更新
					_updateTotalConstant("TOTAL_SCAN");
				}
			} else {
				String sql;
				String[] bind;
				
				if (rare_flag == true) {
					// レアスキャンが成功した場合
					id = this._generateRareId(db);
					sql = "insert into code (code, level, rare_id) " +
						"values (?, ?, ?);";
					bind = new String[]{code, String.valueOf(level), String.valueOf(id)};
					
					// TOTAL_RARE定数の更新
					_updateTotalConstant("TOTA_RARE");
					
				} else {
					// 通常スキャンの場合
					id = this._generateMaterialId(db);
					sql = "insert into code (code, level, material_id) " +
						"values (?, ?, ?);";
					bind = new String[]{code, String.valueOf(level), String.valueOf(id)};
					
					// TOTAL_SCAN定数の更新
					_updateTotalConstant("TOTAL_SCAN");
				}
				
				db.execSQL(sql, bind);
			}
			code_cursor.close();
			
			
			// 取得したidから素材レコードを取得する
			MaterialModel material_model = new MaterialModel();
			Cursor cursor = material_model.fetchById(db, id);
			
			if (cursor.moveToFirst()) {
				values = new ContentValues();
				values.put("id", cursor.getInt(cursor.getColumnIndex("_id")));
				values.put("name", cursor.getString(cursor.getColumnIndex("name")));
				values.put("description", cursor.getString(cursor.getColumnIndex("description")));
				values.put("class", cursor.getString(cursor.getColumnIndex("class")));
				values.put("price", cursor.getInt(cursor.getColumnIndex("price")));
				values.put("experience", cursor.getInt(cursor.getColumnIndex("experience")));
				values.put("qty", cursor.getInt(cursor.getColumnIndex("qty")));
				values.put("rare", rare_flag);
				
				
				// 一度も取得経験のない素材であった場合experienceを更新する
				if (cursor.getInt(cursor.getColumnIndex("experience")) == 0) {
					String sql = "update material set experience = ? " +
						"where _id = ?";
					db.execSQL(sql, new String[]{"1", String.valueOf(id)});
				}
			}
			cursor.close();
			
			db.setTransactionSuccessful();
			
		} finally {
			db.endTransaction();
		}
		
		db.close();
		
		return values;
	}
	
	
	
	/**
	 * 現在のレベルに応じた素材IDを返す
	 */
	private int _generateMaterialId (SQLiteDatabase db)
	{
		String cls = null;
		int rarity = 0;
		
		Integer[] prob;
		int key;
		
		
		// 1~100の乱数を生成する
		int rand = this._generateRandomInt();
		
		// レベルの分岐
		switch (level) {
			case 1:
				if (rand <= 60) {
					// 60%
					cls = "D";
					rarity = 1;
					
				} else {
					// 40%
					cls = "D";
					rarity = 2;
				}
				break;
				
			case 2:
				prob = new Integer[]{20, 30, 35, 15};
				key = this._getProbKey(prob, rand);
				
				switch (key) {
					case 0:
						cls = "D";
						rarity = 1;
						break;
					
					case 1:
						cls = "D";
						rarity = 2;
						break;
						
					case 2:
						cls = "D";
						rarity = 3;
						break;
						
					case 3:
						cls = "D";
						rarity = 4;
						break;
				}
				break;
				
			case 3:
				prob = new Integer[]{10, 10, 15, 20, 25, 20};
				key = this._getProbKey(prob, rand);
				
				switch (key) {
					case 0:
						cls = "D";
						rarity = 1;
						break;
						
					case 1:
						cls = "D";
						rarity = 2;
						break;
						
					case 2:
						cls = "D";
						rarity = 3;
						break;
						
					case 3:
						cls = "D";
						rarity = 4;
						break;
						
					case 4:
						cls = "C";
						rarity = 1;
						break;
						
					case 5:
						cls = "C";
						rarity = 2;
						break;
				}
				break;
				
			case 4:
				prob = new Integer[]{5, 5, 10, 10, 15, 20, 25, 10};
				key = this._getProbKey(prob, rand);
				
				switch (key) {
					case 0:
						cls = "D";
						rarity = 1;
						break;
						
					case 1:
						cls = "D";
						rarity = 2;
						break;
						
					case 2:
						cls = "D";
						rarity = 3;
						break;
						
					case 3:
						cls = "D";
						rarity = 4;
						break;
						
					case 4:
						cls = "C";
						rarity = 1;
						break;
						
					case 5:
						cls = "C";
						rarity = 2;
						break;
						
					case 6:
						cls = "C";
						rarity = 3;
						break;
						
					case 7:
						cls = "C";
						rarity = 4;
						break;
				}
				break;
				
			case 5:
				prob = new Integer[]{10, 10, 10, 15, 20, 20, 15};
				key = this._getProbKey(prob, rand);
				
				switch (key) {
					case 0:
						cls = "D";
						rarity = 0;
						break;
					
					case 1:
						cls = "C";
						rarity = 1;
						break;
						
					case 2:
						cls = "C";
						rarity = 2;
						break;
						
					case 3:
						cls = "C";
						rarity = 3;
						break;
						
					case 4:
						cls = "C";
						rarity = 4;
						break;
						
					case 5:
						cls = "B";
						rarity = 1;
						break;
						
					case 6:
						cls = "B";
						rarity = 2;
						break;
				}
				break;
				
			case 6:
				prob = new Integer[]{7, 7, 8, 9, 11, 13, 15, 20, 10};
				key = this._getProbKey(prob, rand);
				
				switch (key) {
					case 0:
						cls = "D";
						rarity = 0;
						break;
						
					case 1:
						cls = "C";
						rarity = 1;
						break;
						
					case 2:
						cls = "C";
						rarity = 2;
						break;
						
					case 3:
						cls = "C";
						rarity = 3;
						break;
						
					case 4:
						cls = "C";
						rarity = 4;
						break;
						
					case 5:
						cls = "B";
						rarity = 1;
						break;
						
					case 6:
						cls = "B";
						rarity = 2;
						break;
						
					case 7:
						cls = "B";
						rarity = 3;
						break;
						
					case 8:
						cls = "B";
						rarity = 4;
						break;
				}
				break;
				
			case 7:
				prob = new Integer[]{5, 7, 9, 9, 15, 20, 20, 15};
				key = this._getProbKey(prob, rand);
				
				switch (key) {
					case 0:
						cls = "D";
						rarity = 0;
						break;
						
					case 1:
						cls = "C";
						rarity = 0;
						break;
					
					case 2:
						cls = "B";
						rarity = 1;
						break;
						
					case 3:
						cls = "B";
						rarity = 2;
						break;
						
					case 4:
						cls = "B";
						rarity = 3;
						break;
						
					case 5:
						cls = "B";
						rarity = 4;
						break;
						
					case 6:
						cls = "A";
						rarity = 1;
						break;
						
					case 7:
						cls = "A";
						rarity = 2;
						break;
				}
				break;
				
			case 8:
				prob = new Integer[]{3, 5, 7, 7, 9, 10, 12, 15, 20, 15};
				key = this._getProbKey(prob, rand);
				
				switch (key) {
					case 0:
						cls = "D";
						rarity = 0;
						break;
						
					case 1:
						cls = "C";
						rarity = 0;
						break;
						
					case 2:
						cls = "B";
						rarity = 1;
						break;
						
					case 3:
						cls = "B";
						rarity = 2;
						break;
						
					case 4:
						cls = "B";
						rarity = 3;
						break;
						
					case 5:
						cls = "B";
						rarity = 4;
						break;
						
					case 6:
						cls = "A";
						rarity = 1;
						break;
						
					case 7:
						cls = "A";
						rarity = 2;
						break;
						
					case 8:
						cls = "A";
						rarity = 3;
						break;
						
					case 9:
						cls = "A";
						rarity = 4;
						break;
				}
				break;
				
			case 9:
				prob = new Integer[]{3, 5, 7, 9, 10, 13, 18, 20, 15};
				key = this._getProbKey(prob, rand);
				
				switch (key) {
					case 0:
						cls = "D";
						rarity = 0;
						break;
						
					case 1:
						cls = "C";
						rarity = 0;
						break;
						
					case 2:
						cls = "B";
						rarity = 0;
						break;
						
					case 3:
						cls = "A";
						rarity = 1;
						break;
						
					case 4:
						cls = "A";
						rarity = 2;
						break;
						
					case 5:
						cls = "A";
						rarity = 3;
						break;
						
					case 6:
						cls = "A";
						rarity = 4;
						break;
						
					case 7:
						cls = "S";
						rarity = 1;
						break;
						
					case 8:
						cls = "S";
						rarity = 2;
						break;
				}
				break;
				
			case 10:
				prob = new Integer[]{3, 3, 5, 5, 7, 8, 11, 14, 17, 20, 7};
				key = this._getProbKey(prob, rand);
				
				switch (key) {
					case 0:
						cls = "D";
						rarity = 0;
						break;
						
					case 1:
						cls = "C";
						rarity = 0;
						break;
						
					case 2:
						cls = "B";
						rarity = 0;
						break;
						
					case 3:
						cls = "A";
						rarity = 1;
						break;
						
					case 4:
						cls = "A";
						rarity = 2;
						break;
						
					case 5:
						cls = "A";
						rarity = 3;
						break;
						
					case 6:
						cls = "A";
						rarity = 4;
						break;
						
					case 7:
						cls = "S";
						rarity = 1;
						break;
						
					case 8:
						cls = "S";
						rarity = 2;
						break;
						
					case 9:
						cls = "S";
						rarity = 3;
						break;
						
					case 10:
						cls = "S";
						rarity = 4;
						break;
				}
				break;
		}
		
		MaterialModel model = new MaterialModel();
		Cursor cursor = model.getRarityMaterial(db, cls, rarity);
		int id = cursor.getInt(cursor.getColumnIndex("_id"));
		cursor.close();
		
		return id;
	}
	
	
	
	/**
	 * 乱数が指定確率のどのキーなのかを返す
	 */
	private int _getProbKey (Integer[] prob, int rand)
	{
		int i = 0, key = 0, length = prob.length;
		
		for (int x = 0; x < length; x++) {
			i += prob[x];
			
			// どの確率に収まるかをkeyに保存
			if (rand <= i) {
				key = x;
				break;
			}
		}
		
		return key;
	}
	
	
	
	/**
	 * 現在のレベルに応じたレアスキャンidを返す
	 * @return id
	 */
	private int _generateRareId (SQLiteDatabase db)
	{
		String cls = null;
		int rarity = 0;
		
		switch (level) {
			case 1:
				cls = "D";
				rarity = 3;
				break;
				
			case 2:
				cls = "D";
				rarity = 4;
				break;
				
			case 3:
				cls = "C";
				rarity = 3;
				break;
				
			case 4:
				cls = "C";
				rarity = 4;
				break;
				
			case 5:
				cls = "B";
				rarity = 3;
				break;
				
			case 6:
				cls = "B";
				rarity = 4;
				break;
				
			case 7:
				cls = "A";
				rarity = 3;
				break;
				
			case 8:
				cls = "A";
				rarity = 4;
				break;
				
			case 9:
				cls = "S";
				rarity = 3;
				break;
				
			case 10:
				cls = "S";
				rarity = 4;
				break;
		}
	
		MaterialModel model = new MaterialModel();
		Cursor cursor = model.getRarityMaterial(db, cls, rarity);
		int id = cursor.getInt(cursor.getColumnIndex("_id"));
		cursor.close();
		
		return id;
	}
	
	
	
	/*
	 * 乱数を生成する
	 */
	private int _generateRandomInt ()
	{
		Random random = new Random();
		int rand = random.nextInt(100) + 1;
		
		return rand;
	}
	
	
	/**
	 * TOTAL系 定数の更新
	 */
	private void _updateTotalConstant (String key)
	{
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(mContext);
		int total = sp.getInt(key, 0);
		
		Editor editor = sp.edit();
		editor.putInt(key, (total + 1));
		editor.commit();
	}
}
