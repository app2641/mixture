package com.app2641.mixture;

import java.io.IOException;

import android.app.FragmentManager;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

import com.app2641.activity.MixtureActivity;
import com.app2641.dialog.WelcomeDialog;
import com.app2641.model.DatabaseHelper;

import net.simonvt.menudrawer.MenuDrawer;

public class MixtureMenuDrawer extends MenuDrawer {
	
	// application version
	private int Ver = 1;
	
	private MixtureActivity mActivity;

	MixtureMenuDrawer(MixtureActivity activity) {
		super(activity);
		// TODO Auto-generated constructor stub
		mActivity = activity;
		
		
		// バージョンによる初期化処理
		initVersion();
		
		// mixtureアプリケーションの初期化
		initApplication();
		
		// database初期化処理
		initDatabase();
	}
	

	
	/**
	 * mixtureアプリケーションの初期化
	 */
	public void initApplication ()
	{
		// 初期化フラグの定数を取得する
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(mActivity);
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
			WelcomeDialog dialog = new WelcomeDialog();
			FragmentManager manager = mActivity.getFragmentManager();
			dialog.show(manager, "welcome");
		}
	}
	
	
	
	/**
	 * バージョンによる初期化処理
	 * initVersion
	 */
	public void initVersion ()
	{
		// 定数のバージョン数を取得
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(mActivity);
		int VERSION = sp.getInt("VERSION", 0);
				
		// 現在のバージョンと比較
		if (Ver > VERSION) {
			// バージョン初期化処理を行う
			InitVersion initVer = new InitVersion(Ver);
			VERSION = initVer.execute();
			sp.edit().putInt("VERSION", VERSION).commit();
		}
	}
	
	
	
	/**
	 * データベース初期化処理
	 * initDatabase
	 */
	public void initDatabase ()
	{
		DatabaseHelper db = new DatabaseHelper(mActivity);
		
		try {
			db.init();
			
		} catch (IOException e) {
			throw new Error("Unable to create database");
		}
	}
	
	

	@Override
	public void toggleMenu(boolean animate) {
		// TODO Auto-generated method stub

	}

	@Override
	public void openMenu(boolean animate) {
		// TODO Auto-generated method stub

	}

	@Override
	public void closeMenu(boolean animate) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isMenuVisible() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setMenuSize(int size) {
		// TODO Auto-generated method stub

	}

	@Override
	protected int getIndicatorStartPos() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setOffsetMenuEnabled(boolean offsetMenu) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean getOffsetMenuEnabled() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setDropShadowColor(int color) {
		// TODO Auto-generated method stub

	}

	@Override
	public void peekDrawer() {
		// TODO Auto-generated method stub

	}

	@Override
	public void peekDrawer(long delay) {
		// TODO Auto-generated method stub

	}

	@Override
	public void peekDrawer(long startDelay, long delay) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setHardwareLayerEnabled(boolean enabled) {
		// TODO Auto-generated method stub

	}

	@Override
	public int getTouchMode() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setTouchMode(int mode) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setTouchBezelSize(int size) {
		// TODO Auto-generated method stub

	}

	@Override
	public int getTouchBezelSize() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected void onLayout(boolean arg0, int arg1, int arg2, int arg3, int arg4) {
		// TODO Auto-generated method stub

	}

}
