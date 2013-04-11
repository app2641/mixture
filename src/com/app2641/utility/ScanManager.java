package com.app2641.utility;

import java.util.ArrayList;
import java.util.Random;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
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
	
	
	
	/**
	 * 指定したバーコードに対応する素材IDを返す
	 */
	public int getMaterialId (String code)
	{
		
		return 1;
	}
	
	
	
	/**
	 * 現在のレベルに応じた素材IDを持ったカーソルを返す
	 */
	public Cursor generateMaterialId ()
	{
		// 1~100の乱数を生成する
		Random random = new Random();
		int rand = random.nextInt(100) + 1;
		
		// レベルの分岐
		switch (level) {
			case 1:
				break;
				
			case 2:
				ArrayList<Integer> prob = new ArrayList<Integer>();
				
				prob.add(20);
				prob.add(30);
				prob.add(35);
				prob.add(15);
				break;
		}
	}
}
