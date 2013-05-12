package com.app2641.mixture;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.app2641.activity.NotFoundScanAppActivity;
import com.app2641.activity.ScanResultActivity;
import com.app2641.data.MainMenuItem;
import com.app2641.dialog.MainMenuHelpDialog;
import com.app2641.dialog.WelcomeDialog;
import com.app2641.fragment.CollectionFragment;
import com.app2641.fragment.MixinFragment;
import com.app2641.fragment.ShopFragment;
import com.app2641.fragment.StatusFragment;
import com.app2641.model.DatabaseHelper;
import com.app2641.utility.ScanManager;

import net.simonvt.menudrawer.MenuDrawer;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends FragmentActivity {
	
	// 現在表示しているメニュー状態を格納する
	public String MENU_STATE = "init";
	
	// スライドメインメニュー
	public MenuDrawer mMenuDrawer;
	
	// 初期化プログレスダイアログ
	private ProgressDialog mProgress;
	
	// スキャン後のフラグメント置換フラグ
	private boolean scan_result_flag = false;
	
	// スキャン情報格納ContentsValue
	private ContentValues mValues;
	
	// メインメニューのビュー
	private View mMainMenuView;

	
	@Override
	public void onCreate (Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		// Homeアイコンを表示する
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		// スライドメインメニューの初期化
		initMainMenu();

		// アプリケーションの初期化
		initApplication();
	}
	
	
	@Override
	public void onResume ()
	{
		super.onResume();
		
		// 所持金表示の更新
		setMoneyView();
	}
	
	
	/**
	 * オプションメニュー押下時の処理
	 */
	@Override
	public boolean onOptionsItemSelected (MenuItem item)
	{
		switch (item.getItemId()) {
		}
		
		return true;
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
	
	
	/**
	 * バックキー動作
	 */
	@Override
	public void onBackPressed ()
	{
		final int drawerState = mMenuDrawer.getDrawerState();
        if (drawerState == MenuDrawer.STATE_OPEN || drawerState == MenuDrawer.STATE_OPENING) {
            mMenuDrawer.closeMenu();
            return;
        }

        super.onBackPressed();
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
			// プログレスダイアログの生成
			mProgress = new ProgressDialog(this);
			mProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			mProgress.setMessage(getResources().getString(R.string.init_application));
			mProgress.show();
			
			// データベースの初期化
			initDatabase();
			
			// 定数の初期化
			initConstantValues();
		}
		
		// Statusフラグメントの表示
		dispatchStatus(false);
	}
	
	
	
	/**
	 * データベースの初期化
	 */
	public void initDatabase ()
	{
		DatabaseHelper db = new DatabaseHelper(getApplicationContext());
		
		try {
			db.init();
			
		} catch (IOException e) {
			throw new Error("Unable to create database");
		}
	}
	
	
	
	/**
	 * 定数の初期化
	 */
	public void initConstantValues ()
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
		editor.putBoolean("SECOND_SCAN", false); // 2度目のスキャン
		editor.putBoolean("FIRST_RARE", false);	// はじめてのレアスキャン
		editor.putBoolean("FIRST_MIXIN", false);	// はじめてのミックスイン
		editor.putBoolean("FIRST_LEVELUP", false);	// はじめてのレベルアップ
		editor.putBoolean("FIRST_SHOP", false);	// ショップ営業開始
		
		// 成績
		editor.putInt("TOTAL_SCAN", 0); // 総スキャン回数
		editor.putInt("TOTAL_RARE", 0); // 総レアスキャン回数
		editor.putInt("TOTAL_MIXIN", 0); // 総ミックスイン回数
		editor.putInt("TOTAL_EXP", 0);  // 獲得総経験値
		editor.putInt("TOTAL_MONEY", 0); // 獲得総金額
		editor.commit();
		
		// プログレスダイアログの消去
		if (mProgress.isShowing()) {
			mProgress.dismiss();
			
			// Welcomeダイアログの表示
			WelcomeDialog dialog = new WelcomeDialog();
			dialog.show(getFragmentManager(), "welcome");
		}
	}
	
	
	public void initMainMenu ()
	{
		mMenuDrawer = MenuDrawer.attach(this, MenuDrawer.MENU_DRAG_WINDOW);
		mMenuDrawer.setTouchMode(MenuDrawer.TOUCH_MODE_FULLSCREEN);
		mMenuDrawer.setContentView(R.layout.activity_main);
		
		// MainMenuのリストを生成する
		List<MainMenuItem> items = new ArrayList<MainMenuItem>();
		items.add(new MainMenuItem(MainMenuItem.SCAN, R.string.main_menu_scan, R.drawable.main_menu_scan_icon));
		items.add(new MainMenuItem(MainMenuItem.MIXIN, R.string.main_menu_mixin, R.drawable.main_menu_mixin_icon));
		items.add(new MainMenuItem(MainMenuItem.SHOP, R.string.main_menu_shop, R.drawable.main_menu_shop_icon));
		items.add(new MainMenuItem(MainMenuItem.COLLECTION, R.string.main_menu_collection, R.drawable.main_menu_collection_icon));
		items.add(new MainMenuItem(MainMenuItem.STATUS, R.string.main_menu_status, R.drawable.main_menu_status_icon));
		items.add(new MainMenuItem(MainMenuItem.HELP, R.string.main_menu_help, android.R.drawable.ic_menu_help));
		MainMenuAdapter adapter = new MainMenuAdapter(items);
		
		mMainMenuView = getLayoutInflater().inflate(R.layout.main_menu_list, null, false);
		ListView menu = (ListView) mMainMenuView.findViewById(android.R.id.list);
		menu.setAdapter(adapter);
		menu.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				ListView menu = (ListView) parent;
				MainMenuItem item = (MainMenuItem) menu.getItemAtPosition(position);
				String type = item.getType();
				
				if (type == MainMenuItem.SCAN) {
					dispatchScan();
				} else if (type == MainMenuItem.MIXIN) {
					dispatchMixin();
				} else if (type == MainMenuItem.SHOP) {
					dispatchShop();
				} else if (type == MainMenuItem.COLLECTION) {
					dispatchCollection();
				} else if (type == MainMenuItem.STATUS) {
					dispatchStatus(true);
				} else if (type == MainMenuItem.HELP) {
					MainMenuHelpDialog dialog = new MainMenuHelpDialog();
					dialog.show(getSupportFragmentManager(), "main_menu_help");
				}
			}
		});
		
		
		// 所持金をビューにセットする
		setMoneyView();
		
		mMenuDrawer.setMenuView(mMainMenuView);
	}
	
	
	/**
	 * 所持金をビューにセットする
	 */
	public void setMoneyView ()
	{
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		int money = sp.getInt("MONEY", 0);
		
		TextView money_view = (TextView) mMainMenuView.findViewById(R.id.main_menu_money);
		money_view.setText("所持金: " + String.valueOf(money) + "pr");
	}
	
	
	/**
	 * MainManu構築用アダプター
	 */
	public class MainMenuAdapter extends BaseAdapter
	{
		private LayoutInflater mInflater;
		private List<MainMenuItem> mItems;
		
		public MainMenuAdapter (List<MainMenuItem> items)
		{
			mInflater = getLayoutInflater();
			mItems = items;
		}
		
		
		@Override
		public int getCount ()
		{
			return mItems.size();
		}
		
		
		@Override
		public MainMenuItem getItem (int position)
		{
			return mItems.get(position);
		}
		
		
		@Override
		public long getItemId (int position)
		{
			return position;
		}
		
		
		@Override
		public View getView (int position, View convertView, ViewGroup parent)
		{
			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.view_main_menu_list, null, false);
			}
			
			MainMenuItem item = (MainMenuItem) getItem(position);
			TextView menu_view = (TextView) convertView.findViewById(R.id.view_main_menu_list_item);

			// メニューの構築
			menu_view.setText(getResources().getString(item.getTextResource()));
			menu_view.setCompoundDrawablesWithIntrinsicBounds(item.getIconResource(), 0, 0, 0);
			
			return convertView;
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
				
		} catch (ActivityNotFoundException e) {
			// スキャンアプリが見つからない場合はインストール画面へ遷移する
			Intent intent = new Intent(this, NotFoundScanAppActivity.class);
			intent.setAction(Intent.ACTION_VIEW);
			intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
			startActivity(intent);
		}
	}
	
	
	/**
	 * Intentの返り値を処理する
	 */
	@Override
	public void onActivityResult (int requestCode, int resultCode, Intent current)
	{
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
				// Scan処理
				case 0:
					String code = current.getStringExtra("SCAN_RESULT");
					ScanManager scan = new ScanManager(getApplicationContext());
					
					// コードに紐づく素材レコードを取得する
					mValues = scan.fetchMaterialIdByCode(code);
					
					// スキャン後フラグを更新する
					scan_result_flag = true;
					break;
			}
		}
	}
	
	
	@Override
	public void onResumeFragments ()
	{
		super.onResumeFragments();
		
		if (scan_result_flag == true) {
			scan_result_flag = false;
			
			// スキャン結果フラグメントへ置換する
			Intent intent = new Intent(getApplicationContext(), ScanResultActivity.class);
			intent.setAction(Intent.ACTION_VIEW);
			intent.putExtra("id", ((Integer) mValues.get("id")));
			intent.putExtra("name", ((String) mValues.get("name")));
			intent.putExtra("description", ((String) mValues.get("description")));
			intent.putExtra("class", ((String) mValues.get("class")));
			intent.putExtra("price", ((Integer) mValues.get("price")));
			intent.putExtra("experience", ((Integer) mValues.get("experience")));
			intent.putExtra("rare", ((Boolean) mValues.get("rare")));
			intent.putExtra("qty", ((Integer) mValues.get("qty")));
			startActivity(intent);
		}
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
		trans.addToBackStack("mixin_fragment");
		trans.commit();
				
		// メニュー状態を更新
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
		trans.addToBackStack("shop_fragment");
		trans.commit();
				
		// メニュー状態を更新
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
		trans.addToBackStack("collection_fragment");
		trans.commit();
				
		// メニュー状態を更新
		mMenuDrawer.closeMenu();
	}
	
	
	/**
	 * Status処理の開始
	 */
	public void dispatchStatus (boolean stack_flag)
	{
		if (MENU_STATE == "status") {
			mMenuDrawer.closeMenu();
			return;
		}
		
		// フラグメントの差し替え
		Fragment fragment = new StatusFragment();
		FragmentTransaction trans = getSupportFragmentManager().beginTransaction();
		
		trans.replace(R.id.activity_container, fragment);
		trans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
		
		// stack_flagがtrueの場合はバックスタックに追加する
		if (stack_flag == true) {
			trans.addToBackStack("status_flagment");
		}
		trans.commit();
		
		// メニュー状態を更新
		mMenuDrawer.closeMenu();
	}
}
