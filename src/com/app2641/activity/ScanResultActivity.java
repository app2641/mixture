package com.app2641.activity;

import com.app2641.mixture.R;

import net.simonvt.menudrawer.MenuDrawer;
import android.content.Intent;
import android.os.Bundle;

public class ScanResultActivity extends MixtureActivity {
	
	public MenuDrawer mMenuDrawer;

	
	@Override
	public void onCreate (Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		mMenuDrawer = MenuDrawer.attach(this, MenuDrawer.TOUCH_MODE_FULLSCREEN);
		mMenuDrawer.setContentView(R.layout.activity_scan_result);
		mMenuDrawer.setMenuView(R.layout.main_menu);
		mMenuDrawer.setTouchMode(MenuDrawer.TOUCH_MODE_FULLSCREEN);
		super.mMenuDrawer = mMenuDrawer;
		super.mActivityName = "scan";
		
		
		// 素材データをViewにセットする
		setMaterialData();
	}
	
	
	
	/**
	 * 素材データをViewにセットする
	 */
	public void setMaterialData ()
	{
		Intent intent = getIntent();
	}
	
}
