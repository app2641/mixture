package com.app2641.activity;

import net.simonvt.menudrawer.MenuDrawer;

import com.app2641.fragment.OffLineShopListFragment;
import com.app2641.fragment.OnLineShopListFragment;
import com.app2641.mixture.R;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.widget.TextView;

public class ShopActivity extends MixtureFragmentActivity implements ActionBar.TabListener {

	public SectionsPagerAdapter mSectionsPagerAdapter;

	public ViewPager mViewPager;
	
	public MenuDrawer mMenuDrawer;
	

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		
		mMenuDrawer = MenuDrawer.attach(this, MenuDrawer.MENU_DRAG_WINDOW);
		mMenuDrawer.setContentView(R.layout.activity_shop);
		mMenuDrawer.setMenuView(R.layout.main_menu);
		mMenuDrawer.setTouchMode(MenuDrawer.TOUCH_MODE_FULLSCREEN);
		super.mMenuDrawer = mMenuDrawer;
		super.mActivityName = "shop";
		
		// StatusMainMenuの背景色を変更する
		TextView mStatusMainMenu = (TextView) findViewById(R.id.main_menu_shop_item);
		mStatusMainMenu.setBackgroundColor(getResources().getColor(R.color.weight_color));
		
		// MainMenuのOnClickListenerを初期化する
		initMainMenuOnClickListeners();
		

		// Set up the action bar.
		final ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		
		// Homeアイコンを設定する
		actionBar.setDisplayHomeAsUpEnabled(true);
		

		// FragmentViewPager用のAdapterを生成する
		mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

		// 生成したAdapterをViewPagerにセットする
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);

		// Pagerのタブを移動した時に発火するイベントリスナーを設定する
		mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				actionBar.setSelectedNavigationItem(position);
				
				// MenuDrawerを表示するかしないか
				int mode = (position == 0) ? MenuDrawer.TOUCH_MODE_FULLSCREEN: MenuDrawer.TOUCH_MODE_NONE;
				mMenuDrawer.setTouchMode(mode);
			}
		});
		

		// 必要なセクション分をタブとして追加する
		for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
			// Create a tab with text corresponding to the page title defined by
			// the adapter. Also specify this Activity object, which implements
			// the TabListener interface, as the callback (listener) for when
			// this tab is selected.
			actionBar.addTab(actionBar.newTab()
					.setText(mSectionsPagerAdapter.getPageTitle(i))
					.setTabListener(this));
		}
	}

	
	
	/**
	 * オプションメニュー生成処理
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.shop, menu);
		return true;
	}
	
	

	/**
	 * タブを直接タップして選択した時の処理
	 */
	@Override
	public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
		// When the given tab is selected, switch to the corresponding page in
		// the ViewPager.
		mViewPager.setCurrentItem(tab.getPosition());
	}

	
	
	/**
	 * タブが選択から外れた時の処理
	 */
	@Override
	public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
	}
	
	

	@Override
	public void onTabReselected(ActionBar.Tab tab,FragmentTransaction fragmentTransaction) {
	}
	
	

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 * タブ用のフラグメントを格納するカスタムアダプタークラス
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		
		
		/*
		 * Adapterに格納されたフラグメントを取得する
		 */
		@Override
		public Fragment getItem (int position) {
			// getItem is called to instantiate the fragment for the given page.
			// Return a DummySectionFragment (defined as a static inner class
			// below) with the page number as its lone argument.
			
			Fragment fragment = null;
			
			switch (position) {
			case 0:
				// オフラインショップリスト
				fragment = new OffLineShopListFragment();
				break;
				
			case 1:
				// オンラインショップリスト
				fragment = new OnLineShopListFragment();
				break;
			}
			
			return fragment;
		}
		
		

		/*
		 * 格納したフラグメントの数を取得する
		 */
		@Override
		public int getCount() {
			// Show 2 total pages.
			return 2;
		}

		
		/*
		 * 指定ポジションのタブタイトルを取得する
		 */
		@Override
		public CharSequence getPageTitle(int position) {
			switch (position) {
			case 0:
				return getString(R.string.activity_shop_title_offline);
			case 1:
				return getString(R.string.activity_shop_title_online);
			}
			
			return null;
		}
	}
}
