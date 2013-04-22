package com.app2641.activity;

import net.simonvt.menudrawer.MenuDrawer;

import com.app2641.dialog.WelcomeDialog;
import com.app2641.loader.InitApplicationLoader;
import com.app2641.mixture.R;
import com.app2641.model.DatabaseHelper;
import com.app2641.model.ItemModel;
import com.app2641.model.MaterialModel;

import android.app.ProgressDialog;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Loader;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.TextView;


public class StatusActivity extends MixtureActivity implements LoaderCallbacks<String> {

	protected String mActivityName = "status";
	
	public MenuDrawer mMenuDrawer;
	
	// アプリ初期化中に表示するプログレスバー
	private ProgressDialog mProgress;
	
	
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
		
		
		// アプリケーション初期化処理
		initApplication();
	}
	
	
	
	/**
	 * アプリケーション初期化処理
	 */
	public void initApplication ()
	{
		// 初期化フラグの定数を取得する
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		boolean init = sp.getBoolean("INIT_APPLICATION", false);
						
		if (init == false) {
			// InitApplicationLoaderの初期化
			getLoaderManager().initLoader(0, null, this);
			
			// Welcomeウィンドウの表示
			WelcomeDialog dialog = new WelcomeDialog();
			dialog.show(getFragmentManager(), "welcome");
			
		} else {
			initViews();
		}
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


	
	/**
	 * InitApplicationLoaderの生成
	 */
	@Override
	public Loader<String> onCreateLoader (int id, Bundle bundle)
	{
		// プログレスダイアログの生成
		mProgress = new ProgressDialog(this);
		mProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mProgress.setMessage(getResources().getString(R.string.init_application));
		mProgress.show();
				
		InitApplicationLoader loader = new InitApplicationLoader(getApplicationContext());
		loader.forceLoad();
		return loader;
	}
	
	

	@Override
	public void onLoadFinished(Loader<String> loader, String result)
	{
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		
		// 定数の初期化
		Editor editor = sp.edit();
		editor.putBoolean("INIT_APPLICATION", true);
		
		// 基本情報
		editor.putInt("LEVEL", 1);	// level
		editor.putInt("EXP", 200); // 次のレベルアップまでの残りexp
		editor.putBoolean("MASTER", false);	// 調合師の極意所持
		editor.putBoolean("VIP", false);	// 特別待遇カードの所持
		editor.putInt("MONEY", 0);	// 所持金
		
		// イベントフラグ
		editor.putBoolean("FIRST_SCAN", false);	// はじめてのスキャン
		editor.putBoolean("FIRST_RARE", false);	// はじめてのレアスキャン
		editor.putBoolean("FIRST_MIX", false);	// はじめてのミックスイン
		editor.putBoolean("FIRST_LEVELUP", false);	// はじめてのレベルアップ
		editor.putBoolean("FIRST_SHOP", false);	// ショップ営業開始
		
		// 成績
		editor.putInt("TOTAL_SCAN", 0); // 総スキャン回数
		editor.putInt("TOTAL_RARE", 0); // 総レアスキャン回数
		editor.putInt("TOTAL_MIXIN", 0); // 総ミックスイン回数
		editor.putInt("TOTAL_EXP", 0);  // 獲得総経験値
		editor.putInt("TOTAL_MONEY", 0); // 獲得総金額
		editor.commit();
		
		initViews();
		
		// プログレスダイアログの消去
		if (mProgress.isShowing()) {
			mProgress.dismiss();
		}
	}



	@Override
	public void onLoaderReset(Loader<String> loader) {
	}
}
