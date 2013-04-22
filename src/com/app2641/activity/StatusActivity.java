package com.app2641.activity;

import net.simonvt.menudrawer.MenuDrawer;

import com.app2641.mixture.R;
import com.app2641.model.DatabaseHelper;
import com.app2641.model.ItemModel;
import com.app2641.model.MaterialModel;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.TextView;


public class StatusActivity extends MixtureActivity {

	protected String mActivityName = "status";
	
	public MenuDrawer mMenuDrawer;
	
	
	@Override
	public void onCreate (Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		
		mMenuDrawer = MenuDrawer.attach(this, MenuDrawer.MENU_DRAG_WINDOW);
		mMenuDrawer.setContentView(R.layout.activity_status);
		mMenuDrawer.setMenuView(R.layout.main_menu);
		mMenuDrawer.setTouchMode(MenuDrawer.TOUCH_MODE_FULLSCREEN);
		super.mMenuDrawer = mMenuDrawer;
		super.mActivityName = "status";
		
		// StatusMainMenuの背景色を変更する
		TextView mStatusMainMenu = (TextView) findViewById(R.id.main_menu_status_item);
		mStatusMainMenu.setBackgroundColor(getResources().getColor(R.color.weight_color));
		
		// Homeアイコンを設定する
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		// MainMenuのOnClickListenerを初期化する
		initMainMenuOnClickListeners();
		
		// Viewの構築
		initViews();
	}
	
	
	
	/**
	 * テキストビューに値を突っ込む
	 */
	public void initViews ()
	{
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		
		// 現在のレベル
		int level = sp.getInt("LEVEL", 1);
		TextView level_view = (TextView) findViewById(R.id.status_preparation_level);
		level_view.setText(String.valueOf(level));
		
		// 次のレベルまでに必要な経験値
		int exp = sp.getInt("EXP", 200);
		TextView exp_view = (TextView) findViewById(R.id.status_next_level_exp);
		exp_view.setText(String.valueOf(exp) + "exp");
		
		// 今までに獲得した総経験値
		int total_exp = sp.getInt("TOTAL_EXP", 0);
		TextView total_exp_view = (TextView) findViewById(R.id.status_total_exp);
		total_exp_view.setText(String.valueOf(total_exp) + "exp");
		
		// 今までに獲得した総額
		int total_money = sp.getInt("TOTAL_MONEY", 0);
		TextView total_money_view = (TextView) findViewById(R.id.status_total_money);
		total_money_view.setText(String.valueOf(total_money) + "pr");
		
		// 総スキャン回数
		int total_scan = sp.getInt("TOTAL_SCAN", 0);
		TextView total_scan_view = (TextView) findViewById(R.id.status_total_scan);
		total_scan_view.setText(String.valueOf(total_scan));
		
		// 総レアスキャン回数
		int total_rare_scan = sp.getInt("TOTAL_RARE", 0);
		TextView total_rare_scan_view = (TextView) findViewById(R.id.status_total_rare_scan);
		total_rare_scan_view.setText(String.valueOf(total_rare_scan));
		
		// 総ミックスイン回数
		int total_mixin = sp.getInt("TOTAL_MIXIN", 0);
		TextView total_mixin_view = (TextView) findViewById(R.id.status_total_mixin);
		total_mixin_view.setText(String.valueOf(total_mixin));
		
		
		DatabaseHelper helper = new DatabaseHelper(getApplicationContext());
		SQLiteDatabase db = helper.getReadableDatabase();
		MaterialModel material = new MaterialModel();
		
		// 総素材数
		Cursor material_total_cursor = material.countMaterials(db);
		int total_material = 0;
		if (material_total_cursor.moveToFirst()) {
			total_material = material_total_cursor.getCount();
		}
		material_total_cursor.close();
		
		// 獲得した素材数
		Cursor material_experience_cursor = material.countExperienceMaterial(db);
		int total_experience_material = 0;
		if (material_experience_cursor.moveToFirst()) {
			total_experience_material = material_experience_cursor.getCount();
		}
		material_experience_cursor.close();
		
		// テキストビューに設定
		TextView material_experience_view = (TextView) findViewById(R.id.status_total_get_material);
		String mateiral_text = String.valueOf(total_experience_material) + "/" + String.valueOf(total_material);
		material_experience_view.setText(mateiral_text);
		
		
		// 総アイテム数
		ItemModel item = new ItemModel();
		Cursor item_total_cursor = item.countItems(db);
		int total_item = 0;
		if (item_total_cursor.moveToFirst()) {
			total_item = item_total_cursor.getCount();
		}
		item_total_cursor.close();
		
		// 生成したアイテム数
		Cursor item_experience_cursor = item.countExperienceItems(db);
		int total_experience_item = 0;
		if (item_experience_cursor.moveToFirst()) {
			total_experience_item = item_experience_cursor.getCount();
		}
		item_experience_cursor.close();
		db.close();
		
		// テキストビューに設定
		TextView item_experience_view = (TextView) findViewById(R.id.status_total_get_item);
		String item_text = String.valueOf(total_experience_item) + "/" + String.valueOf(total_item);
		item_experience_view.setText(item_text);
	}
}
