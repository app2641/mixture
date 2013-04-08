package com.app2641.activity;

import net.simonvt.menudrawer.MenuDrawer;

import com.app2641.mixture.R;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class NotFoundScanAppActivity extends MixtureActivity {

	
	@Override
	public void onCreate (Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		mMenuDrawer = MenuDrawer.attach(this, MenuDrawer.MENU_DRAG_WINDOW);
		mMenuDrawer.setContentView(R.layout.activity_not_found_scan_app);
		mMenuDrawer.setMenuView(R.layout.main_menu);
		mMenuDrawer.setTouchMode(MenuDrawer.TOUCH_MODE_FULLSCREEN);
		super.mMenuDrawer = mMenuDrawer;
		super.mActivityName = "scan";
		
		// StatusMainMenuの背景色を変更する
		TextView mStatusMainMenu = (TextView) findViewById(R.id.main_menu_scan_item);
		mStatusMainMenu.setBackgroundColor(getResources().getColor(R.color.weight_color));
		
		// Homeアイコンを有効化
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		// MainMenuのOnClickListenerを初期化する
		initMainMenuOnClickListeners();
		
		
		Button button = (Button) findViewById(R.id.download_scan_app_button);
		button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Uri uri = Uri.parse("market://details?id=com.google.zxing.client.android");
				Intent intent = new Intent(Intent.ACTION_VIEW, uri);
				startActivity(intent);
			}
		});
	}
}
