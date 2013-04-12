package com.app2641.activity;

import java.io.IOException;

import net.simonvt.menudrawer.MenuDrawer;

import com.app2641.dialog.WelcomeDialog;
import com.app2641.mixture.R;
import com.app2641.model.DatabaseHelper;
import com.app2641.utility.ScanManager;
import com.app2641.utility.VersionManager;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

public class MixtureActivity extends Activity implements OnClickListener {

	public String mActivityName;
	
	public MenuDrawer mMenuDrawer;
	
	private TextView mScanMenu;
	private TextView mMixinMenu;
	private TextView mShopMenu;
	private TextView mCollectionMenu;
	private TextView mStatusMenu;
	

	@Override
	public void onCreate (Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		// アプリケーション初期化処理
		initApplication();
		
		// バージョンによる初期化処理
		initVersion();
	}
	
	
	
	@Override
	public void onRestart ()
	{
		final int drawerState = mMenuDrawer.getDrawerState();
        if (drawerState == MenuDrawer.STATE_OPEN || drawerState == MenuDrawer.STATE_OPENING) {
            mMenuDrawer.closeMenu();
        }
        
        super.onRestart();
	}
	
	
	
	@Override
	public void onBackPressed() {
        final int drawerState = mMenuDrawer.getDrawerState();
        if (drawerState == MenuDrawer.STATE_OPEN || drawerState == MenuDrawer.STATE_OPENING) {
            mMenuDrawer.closeMenu();
            return;
        }

        super.onBackPressed();
    }
	
	
	
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mMenuDrawer.toggleMenu();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
	
	
	
	/**
	 * データベース初期化処理
	 */
	public void initDatabase ()
	{
		DatabaseHelper db = new DatabaseHelper(this);
		
		try {
			db.init();
			
		} catch (IOException e) {
			throw new Error("Unable to create database");
		}
	}
	
	
	
	/**
	 * バージョンによる初期化処理
	 */
	public void initVersion ()
	{
		// 定数のバージョン数を取得
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
		int ver = sp.getInt("VERSION", 1);
						
		// 現在のバージョンと比較
		if (VersionManager.VERSION > ver) {
			// バージョン初期化処理を行う
			VersionManager manager = new VersionManager();
			ver = manager.applyVersions(ver);
					
			// バージョンを更新する
			sp.edit().putInt("VERSION", ver).commit();
		}
	}
	
	
	
	/**
	 * アプリケーション初期化処理
	 */
	public void initApplication ()
	{
		// 初期化フラグの定数を取得する
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
		boolean init = sp.getBoolean("INIT_APPLICATION", false);
				
		if (init == false) {
			// データベースの初期化
			initDatabase();
					
			// 定数の初期化
			Editor editor = sp.edit();
			editor.putBoolean("INIT_APPLICATION", true);
			editor.putInt("LEVEL", 1);	// level
			editor.putInt("EXP", 200); // 次のレベルアップまでの残りexp
			editor.putBoolean("MASTER", false);	// 調合師の極意所持
			editor.putBoolean("VIP", false);	// 特別待遇カードの所持
			editor.putInt("MONEY", 0);	// 所持金
			editor.putBoolean("FIRST_SCAN", false);	// はじめてのスキャン
			editor.putBoolean("FIRST_RARE", false);	// はじめてのレアスキャン
			editor.putBoolean("FIRST_MIX", false);	// はじめてのミックス
			editor.putBoolean("FIRST_LEVELUP", false);	// はじめてのレベルアップ
			editor.putBoolean("FIRST_SHOP", false);	// ショップ営業開始
			editor.commit();
					
					
			// Welcomeウィンドウの表示
			Toast.makeText(this, "init!", Toast.LENGTH_SHORT).show();
			WelcomeDialog dialog = new WelcomeDialog();
			FragmentManager manager = this.getFragmentManager();
			dialog.show(manager, "welcome");
			
			mMenuDrawer.openMenu();
		}
	}
	
	
	
	/**
	 * MainMenuにOnClickListenerを実装する
	 */
	public void initMainMenuOnClickListeners ()
	{
		// Scan
		mScanMenu = (TextView) findViewById(R.id.main_menu_scan_item);
		mScanMenu.setOnClickListener(this);
		
		// Mixin
		mMixinMenu = (TextView) findViewById(R.id.main_menu_mixin_item);
		mMixinMenu.setOnClickListener(this);
		
		// Shop
		mShopMenu = (TextView) findViewById(R.id.main_menu_shop_item);
		mShopMenu.setOnClickListener(this);
		
		// Collection
		mCollectionMenu = (TextView) findViewById(R.id.main_menu_collection_item);
		mCollectionMenu.setOnClickListener(this);
		
		// Status
		mStatusMenu = (TextView) findViewById(R.id.main_menu_status_item);
		mStatusMenu.setOnClickListener(this);
	}
	
	
	
	/**
	 * Activityのnameを返す
	 * @return String
	 */
	public String getActivityName ()
	{
		return mActivityName;
	}
	
	
	
	/**
	 * OnClickListenerインターフェイスの実装
	 */
	@Override
	public void onClick (View view)
	{
		switch (view.getId()) {
			// Scan
			case R.id.main_menu_scan_item:
				dispatchScanAtivity(view);
				break;
			
			// Mixin
			case R.id.main_menu_mixin_item:
				dispatchMixinActivity(view);
				break;
				
			// Shop
			case R.id.main_menu_shop_item:
				dispatchShopActivity(view);
				break;
				
			// Collection
			case R.id.main_menu_collection_item:
				dispatchCollectionActivity(view);
				break;
				
			// Status
			case R.id.main_menu_status_item:
				dispatchStatusActivity(view);
				break;
		}
	}
	
	
	
	/**
	 * MixtureMainMenu選択処理
	 * スキャンを開始する
	 */
	public void dispatchScanAtivity (View view)
	{
		if (this.getActivityName() == "scan") {
			// MainMenuを閉じる
			mMenuDrawer.closeMenu();
		} else {
			try {
				Intent intent = new Intent("com.google.zxing.client.android.SCAN");
				intent.putExtra("SCAN_MODE", "ONE_D_MODE");
				startActivityForResult(intent, 0);
			
			} catch (ActivityNotFoundException e) {
				// スキャンアプリが見つからない場合はインストール画面へ遷移する
				Intent intent = new Intent(this, NotFoundScanAppActivity.class);
				intent.setAction(Intent.ACTION_VIEW);
				startActivity(intent);
			}
		}
	}
	
	
	
	/**
	 * Intentの返り値を処理する
	 */
	@Override
	public void onActivityResult (int requestCode, int resultCode, Intent intent)
	{
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
				// Scan処理
				case 0:
					String code = intent.getStringExtra("SCAN_RESULT");
					ScanManager scan = new ScanManager(getApplicationContext());
					
					// コードに紐づく素材idがあるか取得する
					int material_id = scan.fetchMaterialIdByCode(code);
					
					if (material_id == 0) {
						// 取得したことのないコードの場合
					} else {
						// 既に取得したことのあるコードの場合
					}
					break;
			}
		}
	}
	
	
	
	/**
	 * MainMenu選択処理
	 * Shopを開始する
	 */
	public void dispatchShopActivity (View view)
	{
		if (this.getActivityName() == "shop") {
			// MainMenuを閉じる
			mMenuDrawer.closeMenu();
		} else {
			Toast.makeText(this, "shop", Toast.LENGTH_SHORT).show();
			Intent intent = new Intent(this, ShopActivity.class);
			intent.setAction(Intent.ACTION_VIEW);
			startActivity(intent);
		}
	}
	
	
	
	/**
	 * MainMenu選択処理
	 * Mixinを開始する
	 */
	public void dispatchMixinActivity (View view)
	{
		
	}
	
	
	
	/**
	 * MainMenu選択処理
	 * Collectionを開始する
	 */
	public void dispatchCollectionActivity (View view)
	{
		if (this.getActivityName() == "collection") {
			mMenuDrawer.closeMenu();
		} else {
			Intent intent = new Intent(this, CollectionActivity.class);
			intent.setAction(Intent.ACTION_VIEW);
			startActivity(intent);
		}
	}
	
	
	
	/**
	 * MainMenu選択処理
	 * Statusを開始する
	 */
	public void dispatchStatusActivity (View view)
	{
		if (this.getActivityName() == "status") {
			mMenuDrawer.closeMenu();
		} else {
			Intent intent = new Intent(this, StatusActivity.class);
			intent.setAction(Intent.ACTION_VIEW);
			startActivity(intent);
		}
	}
}
