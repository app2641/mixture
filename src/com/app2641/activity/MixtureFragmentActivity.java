package com.app2641.activity;

import com.app2641.mixture.R;

import net.simonvt.menudrawer.MenuDrawer;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

public class MixtureFragmentActivity extends FragmentActivity implements OnClickListener {

	public String mActivityName;
	
	public MenuDrawer mMenuDrawer;
	
	private TextView mScanMenu;
	private TextView mMixinMenu;
	private TextView mShopMenu;
	private TextView mCollectionMenu;
	private TextView mStatusMenu;
	
	
	
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
	 * MainMenuにOnClickListenerを実装する
	 */
	public void initMainMenuOnClickListeners ()
	{
		// Scan
		mScanMenu = (TextView) findViewById(R.id.main_menu_scan_item);
		mScanMenu.setOnClickListener(this);
		
		// Mixin
		mMixinMenu = (TextView) findViewById(R.id.main_menu_mixin_item);
		mMixinMenu.setOnClickListener(this);
		
		// Shop
		mShopMenu = (TextView) findViewById(R.id.main_menu_shop_item);
		mShopMenu.setOnClickListener(this);
		
		// Collection
		mCollectionMenu = (TextView) findViewById(R.id.main_menu_collection_item);
		mCollectionMenu.setOnClickListener(this);
		
		// Status
		mStatusMenu = (TextView) findViewById(R.id.main_menu_status_item);
		mStatusMenu.setOnClickListener(this);
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
	 * OnClickListenerインターフェイスの実装
	 */
	@Override
	public void onClick (View view)
	{
		switch (view.getId()) {
			// Scan
			case R.id.main_menu_scan_item:
				dispatchScanAtivity(view);
				break;
			
			// Mixin
			case R.id.main_menu_mixin_item:
				dispatchMixinActivity(view);
				break;
				
			// Shop
			case R.id.main_menu_shop_item:
				dispatchShopActivity(view);
				break;
				
			// Collection
			case R.id.main_menu_collection_item:
				dispatchCollectionActivity(view);
				break;
				
			// Status
			case R.id.main_menu_status_item:
				dispatchStatusActivity(view);
				break;
		}
	}
	
	
	
	/**
	 * MixtureMainMenu選択処理
	 * スキャンを開始する
	 */
	public void dispatchScanAtivity (View view)
	{
		try {
			Intent intent = new Intent("com.google.zxing.client.android.SCAN");
			intent.putExtra("SCAN_MODE", "ONE_D_MODE");
			startActivityForResult(intent, 0);
			
		} catch (ActivityNotFoundException e) {
			// スキャンアプリが見つからない場合はインストール画面へ遷移する
			Intent intent = new Intent(this, ShopActivity.class);
			intent.setAction(Intent.ACTION_VIEW);
			startActivity(intent);
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
					break;
			}
		}
	}
	
	
	
	/**
	 * MainMenu選択処理
	 * Shopを開始する
	 */
	public void dispatchShopActivity (View view)
	{
		if (this.getActivityName() == "shop") {
			// MainMenuを閉じる
			mMenuDrawer.closeMenu();
		} else {
			Toast.makeText(this, "shop", Toast.LENGTH_SHORT).show();
			Intent intent = new Intent(this, ShopActivity.class);
			intent.setAction(Intent.ACTION_VIEW);
			startActivity(intent);
		}
	}
	
	
	
	/**
	 * MainMenu選択処理
	 * Mixinを開始する
	 */
	public void dispatchMixinActivity (View view)
	{
		
	}
	
	
	
	/**
	 * MainMenu選択処理
	 * Collectionを開始する
	 */
	public void dispatchCollectionActivity (View view)
	{
		if (this.getActivityName() == "collection") {
			mMenuDrawer.closeMenu();
		} else {
			Intent intent = new Intent(this, CollectionActivity.class);
			intent.setAction(Intent.ACTION_VIEW);
			startActivity(intent);
		}
	}
	
	
	
	/**
	 * MainMenu選択処理
	 * Statusを開始する
	 */
	public void dispatchStatusActivity (View view)
	{
		if (this.getActivityName() == "status") {
			mMenuDrawer.closeMenu();
		} else {
			Intent intent = new Intent(this, StatusActivity.class);
			intent.setAction(Intent.ACTION_VIEW);
			startActivity(intent);
		}
	}
}
