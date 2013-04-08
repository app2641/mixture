package com.app2641.activity;

import net.simonvt.menudrawer.MenuDrawer;

import com.app2641.mixture.R;

import android.os.Bundle;
import android.widget.TextView;


public class StatusActivity extends MixtureActivity {

	protected String mActivityName = "status";
	
	public MenuDrawer mMenuDrawer;
	
	
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
	}
}
