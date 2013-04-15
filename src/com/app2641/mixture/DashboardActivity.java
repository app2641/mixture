package com.app2641.mixture;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.app2641.activity.CollectionActivity;
import com.app2641.activity.ShopActivity;
import com.app2641.api.Api;
import com.app2641.api.GenerateApiKey;

import com.app2641.dialog.DashboardHelp;
import com.app2641.dialog.WelcomeDialog;
import com.app2641.fragment.FragmentDashboardInstallScanApp;
import com.app2641.fragment.FragmentImportant;
import com.app2641.fragment.FragmentItemResult;
import com.app2641.fragment.FragmentNetworkError;
import com.app2641.fragment.FragmentServerMaintenance;
import com.app2641.fragment.MixtureFragment;
import com.app2641.model.DatabaseHelper;

import com.google.ads.*;

import com.app2641.mixture.R;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.LoaderManager.LoaderCallbacks;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.app.FragmentManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class DashboardActivity extends MixtureActivity implements LoaderCallbacks<String> {
	
	// application version
	private int Ver = 1;
	
	private AdView adView;
	private String API_KEY;
	private ProgressDialog prog;
	
	private int NETWORK_ERROR = 0;
	private int SERVER_MAINTENANCE = 1;
	
	// networkerror Handler
	private Handler NetWorkErrorHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			MixtureFragment fragment = null;
			
			if (msg.what == NETWORK_ERROR) {
				fragment = new FragmentNetworkError();
				
			} else if (msg.what == SERVER_MAINTENANCE) {
				fragment = new FragmentServerMaintenance();
			}
			
			setTitle(getResources().getString(R.string.title_activity_network_error));
			
			FragmentManager manager = getFragmentManager();
			FragmentTransaction transaction = manager.beginTransaction();
			transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
			transaction.replace(R.id.activity_dashboard_container, fragment);
			transaction.commit();
		}
	};
	
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dashboard);
		
		// versionによる初期化処理
		initVersion();
		
		// database初期化処理
		initDatabse();
		
		// fragment初期化処理(Dashboardを表示する)
//		super.fragmentReplace(new FragmentDashboardTable());
		
		// AdMob広告初期化
		initAdMob();
	}
	
	@Override
	public void onResume ()
	{
		super.onResume();
		
		// apiキーを取得する
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
		API_KEY = sp.getString("API_KEY", "none");
		
		// apiキーを所持してない場合には別スレッドでapiキーの発行を行う
		if (API_KEY == "none") {
			getLoaderManager().initLoader(1, null, this);
		}
	}
	
	
	// 非同期でmixtureサーバと接続してapiキーの発行する
	public Loader<String> onCreateLoader(int id, Bundle args)
	{
		// プログレスウィンドウの生成、表示
		prog = new ProgressDialog(this);
		prog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		prog.setMessage(getResources().getString(R.string.init));
		prog.show();
		
		// apiキーを生成する
		Api api = new Api(DashboardActivity.this, new GenerateApiKey());
		api.forceLoad();
		return api;
	}
	
	
	// 非同期処理レスポンスの処理
	public void onLoadFinished(Loader<String> loader, String res)
	{
		// 正規表現でレスポンスを精査する
		Pattern pattern = Pattern.compile("^false.*$");
		Matcher matcher = pattern.matcher(res);
		boolean match = matcher.matches();
		
		prog.dismiss();
		
		// レスポンスでエラーが発生していた場合
		if (match == true) {
			// 正規表現で false文字列を置換 
			Pattern pattern2 = Pattern.compile("false");
			Matcher matcher2 = pattern2.matcher(res);
			String error = matcher2.replaceFirst("");
			
			// エラー内容がサーバメンテかどうか
			if (error == "server") {
				NetWorkErrorHandler.sendEmptyMessage(SERVER_MAINTENANCE);
			} else {
				NetWorkErrorHandler.sendEmptyMessage(NETWORK_ERROR);
			}
			
		// apiキーの生成、取得に成功した場合
		} else {
			// 定数の生成
			SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
			Editor editor = sp.edit();
			editor.putString("API_KEY", res);	// ApiKey
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
			
			
			// welcomeウィンドウの生成、表示
			WelcomeDialog dialog = new WelcomeDialog();
			FragmentManager manager = getFragmentManager();
			dialog.show(manager, "welcome");
		}
	}
	
	public void onLoaderReset (Loader<String> loader)
	{
	}
	

	
	// version初期化処理
	public void initVersion ()
	{
		// 定数のバージョン数を取得
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
		int VERSION = sp.getInt("VERSION", 0);
		
		// 現在のバージョンと比較
		if (Ver > VERSION) {
			// バージョン初期化処理を行う
//			InitVersion initVer = new InitVersion(Ver);
//			VERSION = initVer.execute();
//			sp.edit().putInt("VERSION", VERSION).commit();
		}
	}
	
	
	// database初期化処理
	public void initDatabse ()
	{
		DatabaseHelper db = new DatabaseHelper(this);
		
		try {
			db.init();
			
		} catch (IOException e) {
			throw new Error("Unable to create database");
		}
	}
	
	
	// Admob広告の初期化処理
	public void initAdMob ()
	{
		adView = (AdView) findViewById(R.id.adView);
		adView.loadAd(new AdRequest());
	}
	
	
	@Override
	public boolean onOptionsItemSelected (MenuItem item)
	{
		switch (item.getItemId()) {
			case R.id.menu_dashboard_help:
				// ヘルプダイアログを表示する
				DashboardHelp dialog = new DashboardHelp();
				FragmentManager manager = getFragmentManager();
				dialog.show(manager, "dashboard_help");
				break;
			
			case R.id.menu_status:
//				dialog = new HelpDialog(this, new WelcomeFactory());
//				dialog.show();
				break;
		
			case R.id.menu_about_app:
				break;
			
			case R.id.menu_clear:
				SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
				sp.edit().remove("API_KEY").commit();
				break;
			
			case R.id.menu_item_detail_help:
//				dialog = new HelpDialog(this, new WelcomeFactory());
//				dialog.show();
				break;
				
			case R.id.menu_important_help:
//				dialog = new HelpDialog(this, new WelcomeFactory());
//				dialog.show();
				break;
			
			case R.id.menu_important:
				super.fragmentReplace(new FragmentImportant());
				break;
		}
		return true;
	}
	
	
	// スキャンアプリを立ち上げる 
	public void dispatchScanActivity (View v)
	{
		Intent intent = new Intent("com.google.zxing.client.android.SCAN");
		intent.putExtra("SCAN_MODE", "ONE_D_MODE");
		
		try {
//			throw new ActivityNotFoundException();
			startActivityForResult(intent, 0);
			
		// スキャンアプリが見つからなかった場合にはインストール画面へ遷移する
		} catch (ActivityNotFoundException e) {
			super.fragmentReplace(new FragmentDashboardInstallScanApp());
		}
	}
	
	
	// mix画面へ遷移する
	public void dispatchMixActivity (View v)
	{
		
	}
	
	
	// shop画面へ遷移する
	public void dispatchShopActivity (View v)
	{
		Intent intent = new Intent(this, ShopActivity.class);
		intent.setAction(Intent.ACTION_VIEW);
		startActivity(intent);
	}
	
	
	// collection画面へ遷移する
	public void dispatchCollectionActivity (View v)
	{
		Intent intent = new Intent(this, CollectionActivity.class);
		intent.setAction(Intent.ACTION_VIEW);
		startActivity(intent);
	}
	
	
	// intentの結果を処理する
	@Override
	public void onActivityResult (int requestCode, int resultCode, Intent intent)
	{
		if (resultCode == RESULT_OK) {
			// scan結果を処理
			if (requestCode == 0) {
				String code = intent.getStringExtra("SCAN_RESULT");
				
				// データベースヘルパーの生成
				DatabaseHelper helper = new DatabaseHelper(DashboardActivity.this);
				String sql = "SELECT * FROM material ORDER BY RANDOM();";
//				Cursor c = helper.executeSql(sql, new String[]{});
				
//				if (c.moveToFirst()) {
					// スキャン結果画面へ遷移させる
					Bundle bundle = new Bundle();
//					bundle.putInt("id", c.getInt(c.getColumnIndex("_id")));
					
					Fragment fragment = new FragmentItemResult();
					fragment.setArguments(bundle);
					
//					super.fragmentReplace(new FragmentDashboardTable());
//				}
				
//				c.close();
			}
		}
	}
}
