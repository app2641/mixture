package com.app2641.activity;

import net.simonvt.menudrawer.MenuDrawer;

import com.app2641.mixture.R;

import android.os.Bundle;


public class StatusActivity extends MixtureActivity {

	protected String mActivityName = "status";
	
	protected MenuDrawer mMenuDrawer;
	
	@Override
	public void onCreate (Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		
		mMenuDrawer = MenuDrawer.attach(this);
		mMenuDrawer.setContentView(R.layout.activity_status);
		mMenuDrawer.setMenuView(R.layout.main_menu);
		mMenuDrawer.setDropShadowColor(R.color.main_color);
		
		mMenuDrawer.toggleMenu();
	}
}
