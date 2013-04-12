package com.app2641.utility;

import java.util.ArrayList;
import java.util.Random;

import com.app2641.model.CodeModel;
import com.app2641.model.DatabaseHelper;
import com.app2641.model.MaterialModel;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
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
	public int fetchMaterialIdByCode (String code)
	{
		DatabaseHelper helper = new DatabaseHelper(mContext);
		SQLiteDatabase db = helper.getWritableDatabase();
		
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
							"where _id = ?";
						String[] bind = new String[]{String.valueOf(id), String.valueOf(code_id)};
						
						db.execSQL(sql, bind);
						
					} else {
						id = code_cursor.getInt(code_cursor.getColumnIndex("rare_id"));
					}
					
				} else {
					// 通常スキャンの場合
					if (code_cursor.isNull(code_cursor.getColumnIndex("material_id"))) {
						id = this._generateMaterialId(db);
						int code_id = code_cursor.getInt(code_cursor.getColumnIndex("_id"));
						
						// コードとmaterial_idを紐付ける
						String sql = "update code set rare_id = ? " +
							"where _id = ?";
						String[] bind = new String[]{String.valueOf(id), String.valueOf(code_id)};
						
						db.execSQL(sql, bind);
						
					} else {
						id = code_cursor.getInt(code_cursor.getColumnIndex("material_id"));
					}
				}
			} else {
				if (rare_flag == true) {
					// レアスキャンが成功した場合
				} else {
					// 通常スキャンの場合
				}
			}
			
			code_cursor.close();
			
			db.setTransactionSuccessful();
			
		} finally {
			db.endTransaction();
		}
		
		
		Cursor cursor = model.getMaterialId(db, code, level);
		
		int id = (cursor.moveToFirst()) ? cursor.getInt(cursor.getColumnIndex("_id")): 0;
		cursor.close();
		db.close();
		
		return id;
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
		}
		
		return 1;
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
}
