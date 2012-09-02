package com.app2641.mixture;

import java.io.IOException;

import com.google.ads.*;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.app.FragmentManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class DashboardActivity extends Activity {
	
	private AdView adView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dashboard);
		
		// database初期化
		initDatabse();
		
		// fragment初期化
		initFragment();
		
		// 広告の表示
		initAdMob();
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
