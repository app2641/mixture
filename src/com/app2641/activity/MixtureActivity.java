package com.app2641.activity;


import net.simonvt.menudrawer.MenuDrawer;

import com.app2641.dialog.WelcomeDialog;
import com.app2641.mixture.R;
import com.app2641.utility.ScanManager;
import com.app2641.utility.VersionManager;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

public class MixtureActivity extends Activity {

	public String mActivityName;
	
	public MenuDrawer mMenuDrawer;
	
	// scan後かどうかのフラグ
	public boolean scan_flag = false;
	// scan情報を格納する
	public ContentValues mValues;
	

	@Override
	public void onCreate (Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		// バージョンによる初期化処理
		initVersion();
	}
	
	
	
	@Override
	public void onResume ()
	{
		// Scan後の場合は素材取得アクティビティへ遷移する
		if (scan_flag == true) {
			Intent intent = new Intent(getApplicationContext(), ScanResultActivity.class);
			intent.setAction(Intent.ACTION_VIEW);
			intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
			intent.putExtra("id", ((Integer) mValues.get("id")));
			intent.putExtra("name", ((String) mValues.get("name")));
			intent.putExtra("description", ((String) mValues.get("description")));
			intent.putExtra("class", ((String) mValues.get("class")));
			intent.putExtra("price", ((Integer) mValues.get("price")));
			intent.putExtra("experience", ((Integer) mValues.get("experience")));
			intent.putExtra("rare", ((Boolean) mValues.get("rare")));
			intent.putExtra("qty", ((Integer) mValues.get("qty")));
			startActivity(intent);
			
			scan_flag = false;
		}
		
		super.onResume();
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
	 * Activityのnameを返す
	 * @return String
	 */
	public String getActivityName ()
	{
		return mActivityName;
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
				
				Toast.makeText(getApplicationContext(), R.string.toast_scan_barcode, Toast.LENGTH_SHORT).show();
			
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
					
					// コードに紐づく素材レコードを取得する
					mValues = scan.fetchMaterialIdByCode(code);
					scan_flag = true;
					break;
			}
		}
	}
	
}
