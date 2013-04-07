package com.app2641.mixture;

import java.io.IOException;

import android.app.FragmentManager;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.app2641.activity.MixtureActivity;
import com.app2641.dialog.WelcomeDialog;
import com.app2641.model.DatabaseHelper;
import com.app2641.utility.VersionManager;

import net.simonvt.menudrawer.MenuDrawer;

public class MixtureMenuDrawer extends MenuDrawer {
	
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
		
	}
	
	
	
	/**
	 * バージョンによる初期化処理
	 * initVersion
	 */
	public void initVersion ()
	{
		
	}
	
	
	
	/**
	 * データベース初期化処理
	 * initDatabase
	 */
	public void initDatabase ()
	{
		
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
