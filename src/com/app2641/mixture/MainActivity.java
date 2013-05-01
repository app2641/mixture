package com.app2641.mixture;

import com.app2641.activity.NotFoundScanAppActivity;
import com.app2641.dialog.WelcomeDialog;
import com.app2641.fragment.CollectionFragment;
import com.app2641.fragment.MixinFragment;
import com.app2641.fragment.ShopFragment;
import com.app2641.fragment.StatusFragment;
import com.app2641.loader.InitApplicationLoader;

import net.simonvt.menudrawer.MenuDrawer;
import android.app.LoaderManager.LoaderCallbacks;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends FragmentActivity implements LoaderCallbacks<String>, OnClickListener {
	
	// 現在表示しているメニュー状態を格納する
	public static String MENU_STATE = "init";
	
	// スライドメインメニュー
	public MenuDrawer mMenuDrawer;
	
	// 初期化プログレスダイアログ
	private ProgressDialog mProgress;

	
	@Override
	public void onCreate (Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		// スライドメインメニューの初期化
		initMainMenu();

		// アプリケーションの初期化
		initApplication();
	}
	
	
	
	/**
	 * アプリケーションの初期化
	 */
	public void initApplication ()
	{
		// 初期化フラグの定数を取得する
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		boolean init = sp.getBoolean("INIT_APPLICATION", false);
								
		if (init == false) {
			// InitApplicationLoaderの初期化
			getLoaderManager().initLoader(0, null, this);
					
		} else {
			// Statusフラグメントの表示
			dispatchStatus();
		}
	}
	
	
	/**
	 * スライドメインメニューの初期化処理
	 */
	public void initMainMenu ()
	{
		mMenuDrawer = MenuDrawer.attach(this, MenuDrawer.MENU_DRAG_WINDOW);
		mMenuDrawer.setTouchMode(MenuDrawer.TOUCH_MODE_FULLSCREEN);
		mMenuDrawer.setContentView(R.layout.activity_main);
		mMenuDrawer.setMenuView(R.layout.main_menu);
		
		// メインメニューのOnClickListenerを設定する
		// Scan
		TextView mScanMenu = (TextView) findViewById(R.id.main_menu_scan_item);
		mScanMenu.setOnClickListener(this);
				
		// Mixin
		TextView mMixinMenu = (TextView) findViewById(R.id.main_menu_mixin_item);
		mMixinMenu.setOnClickListener(this);
				
		// Shop
		TextView mShopMenu = (TextView) findViewById(R.id.main_menu_shop_item);
		mShopMenu.setOnClickListener(this);
				
		// Collection
		TextView mCollectionMenu = (TextView) findViewById(R.id.main_menu_collection_item);
		mCollectionMenu.setOnClickListener(this);
				
		// Status
		TextView mStatusMenu = (TextView) findViewById(R.id.main_menu_status_item);
		mStatusMenu.setOnClickListener(this);
	}



	/**
	 * メインメニュー押下時の処理
	 */
	@Override
	public void onClick(View view)
	{
		switch (view.getId()) {
		// Scan
		case R.id.main_menu_scan_item:
			dispatchScan();
			break;
		
		// Mixin
		case R.id.main_menu_mixin_item:
			dispatchMixin();
			break;
			
		// Shop
		case R.id.main_menu_shop_item:
			dispatchShop();
			break;
			
		// Collection
		case R.id.main_menu_collection_item:
			dispatchCollection();
			break;
			
		// Status
		case R.id.main_menu_status_item:
			dispatchStatus();
			break;
		}
	}
	
	
	
	/**
	 * Scan処理の開始
	 */
	public void dispatchScan ()
	{
		if (MENU_STATE == "scan") {
			mMenuDrawer.closeMenu();
			return;
		}
		
		
		try {
			Intent intent = new Intent("com.google.zxing.client.android.SCAN");
			intent.putExtra("SCAN_MODE", "ONE_D_MODE");
			startActivityForResult(intent, 0);
				
			Toast.makeText(getApplicationContext(), R.string.toast_scan_barcode, Toast.LENGTH_SHORT).show();
		
		} catch (ActivityNotFoundException e) {
			// スキャンアプリが見つからない場合はインストール画面へ遷移する
			Intent intent = new Intent(this, NotFoundScanAppActivity.class);
			intent.setAction(Intent.ACTION_VIEW);
			startActivity(intent);
		}
		
		MENU_STATE = "scan";
	}
	
	
	
	/**
	 * Mixin処理の開始
	 */
	public void dispatchMixin ()
	{
		if (MENU_STATE == "mixin") {
			mMenuDrawer.closeMenu();
			return;
		}
		
		
		// フラグメントの差し替え
		Fragment fragment = new MixinFragment();
		FragmentTransaction trans = getSupportFragmentManager().beginTransaction();
		
		trans.replace(R.id.activity_container, fragment);
		trans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
		trans.addToBackStack(null);
		trans.commit();
				
		// メニュー状態を更新
		MENU_STATE = "mixin";
		mMenuDrawer.closeMenu();
	}
	
	
	
	/**
	 * Shop処理の開始
	 */
	public void dispatchShop ()
	{
		if (MENU_STATE == "shop") {
			mMenuDrawer.closeMenu();
			return;
		}
		
		
		// フラグメントの差し替え
		Fragment fragment = new ShopFragment();
		FragmentTransaction trans = getSupportFragmentManager().beginTransaction();
				
		trans.replace(R.id.activity_container, fragment);
		trans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
		trans.addToBackStack(null);
		trans.commit();
				
		// メニュー状態を更新
		MENU_STATE = "shop";
		mMenuDrawer.closeMenu();
	}
	
	
	
	/**
	 * Collection処理の開始
	 */
	public void dispatchCollection ()
	{
		if (MENU_STATE == "collection") {
			mMenuDrawer.closeMenu();
			return;
		}
		
		
		// フラグメントの差し替え
		Fragment fragment = new CollectionFragment();
		FragmentTransaction trans = getSupportFragmentManager().beginTransaction();
				
		trans.replace(R.id.activity_container, fragment);
		trans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
		trans.addToBackStack(null);
		trans.commit();
				
		// メニュー状態を更新
		MENU_STATE = "collection";
		mMenuDrawer.closeMenu();
	}
	
	
	/**
	 * Status処理の開始
	 */
	public void dispatchStatus ()
	{
		if (MENU_STATE == "status") {
			mMenuDrawer.closeMenu();
			return;
		}
		
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		boolean welcome = sp.getBoolean("WELCOME", false);
		
		// WELCOMEフラグがfalseの場合はWelcomeダイアログを表示する
		if (welcome == false) {
			Editor editor = sp.edit();
			editor.putBoolean("WELCOME", true);
			editor.commit();
			
			WelcomeDialog dialog = new WelcomeDialog();
			dialog.show(getFragmentManager(), "welcome");
		}
		
		
		// フラグメントの差し替え
		Fragment fragment = new StatusFragment();
		FragmentTransaction trans = getSupportFragmentManager().beginTransaction();
		
		trans.replace(R.id.activity_container, fragment);
		trans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
		trans.addToBackStack(null);
		trans.commit();
		
		// メニュー状態を更新
		MENU_STATE = "status";
		mMenuDrawer.closeMenu();
	}



	/**
	 * アプリケーション初期化用のローダを生成
	 */
	@Override
	public Loader<String> onCreateLoader(int id, Bundle bundle)
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



	/**
	 * ローダ処理後のコールバック
	 */
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
		editor.putBoolean("WELCOME", false);  // Welcomeダイアログの表示
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
		
		// Statusフラグメントの表示
		dispatchStatus();
		
		// プログレスダイアログの消去
		if (mProgress.isShowing()) {
			mProgress.dismiss();
		}
	}



	@Override
	public void onLoaderReset(Loader<String> loader)
	{
	}
}
