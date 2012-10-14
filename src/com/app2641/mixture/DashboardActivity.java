package com.app2641.mixture;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.ads.*;

import android.app.Activity;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class DashboardActivity extends Activity implements LoaderCallbacks<String> {
	
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
			Fragment fragment = null;
			
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
		
		// version設定
		initVersion();
		
		// database初期化
		initDatabse();
		
		// fragment初期化
		initFragment();
		
		// 広告の表示
		initAdMob();
	}
	
	@Override
	public void onResume ()
	{
		super.onResume();
		
		// 初回起動時にAPIKEY生成
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
		API_KEY = sp.getString("API_KEY", "none");
		
		if (API_KEY == "none") {
			getLoaderManager().initLoader(1, null, this);
		}
	}
	
	
	// asynctak loader methods
	public Loader<String> onCreateLoader(int id, Bundle args)
	{
		prog = new ProgressDialog(this);
		prog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		prog.setMessage(getResources().getString(R.string.init));
		prog.show();
		
		ApiGenerateApiKey api = new ApiGenerateApiKey(DashboardActivity.this);
		api.forceLoad();
		return api;
	}
	
	public void onLoadFinished(Loader<String> loader, String res)
	{
		Pattern pattern = Pattern.compile("^false.*$");
		Matcher matcher = pattern.matcher(res);
		boolean match = matcher.matches();
		
		prog.dismiss();
		
		// error処理
		if (match == true) {
			Pattern pattern2 = Pattern.compile("false");
			Matcher matcher2 = pattern2.matcher(res);
			String error = matcher2.replaceFirst("");
			
			if (error == "server") {
				NetWorkErrorHandler.sendEmptyMessage(SERVER_MAINTENANCE);
			} else {
				NetWorkErrorHandler.sendEmptyMessage(NETWORK_ERROR);
			}
			
		// apikey取得
		} else {
			SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
			Editor editor = sp.edit();
			editor.putString("API_KEY", res);	// ApiKey
			editor.putInt("LEVEL", 1);	// level
			editor.putInt("EXP", 200); // 残りexp
			editor.putBoolean("MASTER", false);	// 調合師の極意
			editor.putBoolean("VIP", false);	// 特別待遇カード
			editor.putInt("MONEY", 0);	// 所持金
			editor.putBoolean("FIRST_SCAN", false);	// はじめてのスキャン
			editor.putBoolean("FIRST_RARE", false);	// はじめてのレアスキャン
			editor.putBoolean("FIRST_MIX", false);	// はじめてのミックス
			editor.putBoolean("FIRST_LEVELUP", false);	// はじめてのレベルアップ
			editor.putBoolean("FIRST_SHOP", false);	// はじめてのショップ開店
			editor.commit();
			
			
			// welcomeダイアログの表示
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setIcon(android.R.drawable.ic_menu_info_details);
			builder.setTitle(getResources().getString(R.string.welcome_dialog_title));
			builder.setMessage(getResources().getString(R.string.welcome_dialog_message));
			builder.setPositiveButton("OK", new DialogInterface.OnClickListener(){
				public void onClick (DialogInterface dialog, int id) {
					dialog.cancel();
				}
			});
			builder.show();
		}
	}
	
	public void onLoaderReset (Loader<String> loader)
	{
	}
	

	
	// versionごとの処理
	public void initVersion ()
	{
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
		int VERSION = sp.getInt("VERSION", 0);
		
		if (Ver > VERSION) {
			InitVersion initVer = new InitVersion(Ver);
			VERSION = initVer.execute();
			sp.edit().putInt("VERSION", VERSION).commit();
		}
	}
	
	/**
	 * database初期化処理
	 * 初回起動時にデータベースを生成する
	 */
	public void initDatabse ()
	{
		DatabaseHelper db = new DatabaseHelper(this);
		
		try {
			db.init();
			
		} catch (IOException e) {
			throw new Error("Unable to create database");
		}
	}
	
	
	/**
	 * fragment初期化
	 */
	public void initFragment ()
	{
		FragmentManager manager = getFragmentManager();
		FragmentTransaction transaction = manager.beginTransaction();
		Fragment frag = manager.findFragmentByTag("fragment_dashboard_table");
		
		if (frag == null) {
			frag = new FragmentDashboardTable();
			transaction.add(R.id.activity_dashboard_container, frag, "fragment_dashboard_table");
		}
		transaction.commit();
	}
	
	
	/**
	 * 広告の初期化
	 */
	public void initAdMob ()
	{
		adView = (AdView) findViewById(R.id.adView);
		adView.loadAd(new AdRequest());
	}
	
	@Override
	public boolean onOptionsItemSelected (MenuItem item)
	{
		DashboardOptionsMenuEvents events = new DashboardOptionsMenuEvents(DashboardActivity.this, item);
		
		switch (item.getItemId()) {
			case R.id.menu_dashboard_help:
				events.dashboardHelp();
				break;
			
			case R.id.menu_status:
				break;
		
			case R.id.menu_about_app:
				break;
			
			case R.id.menu_clear:
				SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
				sp.edit().remove("API_KEY").commit();
				break;
			
			case R.id.menu_item_detail_help:
				events.detailHelp();
				break;
				
			case R.id.menu_important_help:
				events.importantHelp();
				break;
			
			case R.id.menu_important:
				FragmentManager manager = getFragmentManager();
				FragmentTransaction transaction = manager.beginTransaction();
				Fragment fragment = new FragmentImportant();
				
				transaction.replace(R.id.activity_dashboard_container, fragment);
				transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
				transaction.addToBackStack(null);
				transaction.commit();
				break;
			
		}
		return true;
	}
	
	
	/**********
	 * click event
	 **********/
	// scan押下時
	public void moveToScan (View v)
	{
		Intent intent = new Intent("com.google.zxing.client.android.SCAN");
		intent.putExtra("SCAN_MODE", "ONE_D_MODE");
		
		try {
//			throw new ActivityNotFoundException();
			startActivityForResult(intent, 0);
			
		} catch (ActivityNotFoundException e) {
			FragmentManager manager = getFragmentManager();
			FragmentTransaction transaction = manager.beginTransaction();
			
			Fragment fragment = new FragmentDashboardFailedScan();
			transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
			transaction.replace(R.id.activity_dashboard_container, fragment);
			transaction.addToBackStack(null);
			transaction.commit();
		}
	}
	
	// mix 押下時
	public void moveToMix (View v)
	{
		
	}
	
	// shop押下時
	public void moveToShop (View v)
	{
		Intent intent = new Intent(this, ShopActivity.class);
		intent.setAction(Intent.ACTION_VIEW);
		startActivity(intent);
	}
	
	// collection押下時
	public void moveToCollection (View v)
	{
		Intent intent = new Intent(this, CollectionActivity.class);
		intent.setAction(Intent.ACTION_VIEW);
		startActivity(intent);
	}
	
	// intentのレスポンス処理
	@Override
	public void onActivityResult (int requestCode, int resultCode, Intent intent)
	{
		if (resultCode == RESULT_OK) {
			// scan後の処理
			if (requestCode == 0) {
				String code = intent.getStringExtra("SCAN_RESULT");
//				Toast.makeText(this, code, 0).show();
				
				DatabaseHelper helper = new DatabaseHelper(DashboardActivity.this);
				String sql = "SELECT * FROM material ORDER BY RANDOM();";
				Cursor c = helper.executeSql(sql, new String[]{});
				
				if (c.moveToFirst()) {
					FragmentManager manager = getFragmentManager();
					FragmentTransaction transaction = manager.beginTransaction();
					
					Bundle bundle = new Bundle();
					bundle.putInt("id", c.getInt(c.getColumnIndex("_id")));
					
					Fragment fragment = new FragmentItemResult();
					fragment.setArguments(bundle);
					transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
					transaction.replace(R.id.activity_dashboard_container, fragment);
					transaction.addToBackStack(null);
					
					transaction.commit();
				}
				
				c.close();
			}
		}
	}
}
