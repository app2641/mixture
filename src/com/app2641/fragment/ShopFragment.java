package com.app2641.fragment;

import net.simonvt.menudrawer.MenuDrawer;

import com.app2641.dialog.ShopHelpDialog;
import com.app2641.mixture.MainActivity;
import com.app2641.mixture.R;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.app.ActionBar.Tab;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

public class ShopFragment extends Fragment implements ActionBar.TabListener {
	
	public ViewPager mViewPager;
	
	public ShopListPagerAdapter mPagerAdapter;
	
	
	public ShopFragment ()
	{
	}

	
	
	@Override
	public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		return inflater.inflate(R.layout.fragment_shop, container, false);
	}
	
	
	
	@Override
	public void onActivityCreated (Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		
		
		// アクションバーのナビゲーションモードを変更
		final ActionBar actionbar = getActivity().getActionBar();
		actionbar.setNavigationMode(actionbar.NAVIGATION_MODE_TABS);
		
		
		// ViewPager用のAdapterを生成する
		mPagerAdapter = new ShopListPagerAdapter(getFragmentManager());
		
		// 生成したAdapterをViewPagerにセットする
		mViewPager = (ViewPager) getView().findViewById(R.id.shop_viewpager);
		mViewPager.setAdapter(mPagerAdapter);
		
		// Pagerのタブを移動した時に発火するイベントリスナーを設定する
		mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				actionbar.setSelectedNavigationItem(position);
				
				// MenuDrawerを表示するかしないか
				int mode = (position == 0) ? MenuDrawer.TOUCH_MODE_FULLSCREEN: MenuDrawer.TOUCH_MODE_NONE;
				((MainActivity) getActivity()).mMenuDrawer.setTouchMode(mode);
			}
		});
		
		
		// 必要なセクションをタブとして追加する
		for (int i = 0; i < mPagerAdapter.getCount(); i++) {
			Tab tab = actionbar.newTab();
			tab.setText(mPagerAdapter.getPageTitle(i));
			tab.setTabListener(this);
			
			actionbar.addTab(tab);
		}
	}
	
	
	
	/**
	 * オプションメニューの生成
	 */
	@Override
	public void onCreateOptionsMenu (Menu menu, MenuInflater inflater)
	{
		inflater.inflate(R.menu.fragment_shop, menu);
	}
	
	
	
	/**
	 * オプションメニュー選択処理
	 */
	public boolean onOptionsItemSelected (MenuItem item)
	{
		switch (item.getItemId()) {
		case R.id.menu_shop_help:
			// ヘルプダイアログを表示する
			ShopHelpDialog dialog = new ShopHelpDialog();
			dialog.show(getFragmentManager(), "shop_help");
			break;
		}
		return true;
	}
	
	
	
	/**
	 * ActionBar TabListeners
	 */
	@Override
	public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
		mViewPager.setCurrentItem(tab.getPosition());
	}

	
	
	@Override
	public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
	}
	
	

	@Override
	public void onTabReselected(ActionBar.Tab tab,FragmentTransaction fragmentTransaction) {
	}
	
	
	
	/**
	 * Fragmentを格納するアダプタークラス
	 */
	public class ShopListPagerAdapter extends FragmentPagerAdapter
	{
		public ShopListPagerAdapter (FragmentManager manager)
		{
			super(manager);
		}
		

		
		/**
		 * Adapterに格納されたフラグメントを取得する
		 */
		@Override
		public Fragment getItem(int position)
		{
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

		
		
		/**
		 * 格納されたフラグメントの総数を取得する
		 */
		@Override
		public int getCount()
		{
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
