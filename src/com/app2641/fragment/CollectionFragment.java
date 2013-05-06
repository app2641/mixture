package com.app2641.fragment;

import com.app2641.mixture.MainActivity;
import com.app2641.mixture.R;

import android.app.ActionBar;
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

public class CollectionFragment extends Fragment {
	
	public final String MENU_STATE = "collection";
	
	public CollectionListPagerAdapter mPagerAdapter;
	
	public ViewPager mViewPager;

	
	public CollectionFragment ()
	{
	}
	
	
	@Override
	public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		return inflater.inflate(R.layout.fragment_collection, container, false);
	}
	
	
	@Override
	public void onActivityCreated (Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		
		// アクションバーのナビゲーションモードを変更
		final ActionBar actionbar = getActivity().getActionBar();
		actionbar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		
		// ViewPager用のAdapter生成
		mPagerAdapter = new CollectionListPagerAdapter(getChildFragmentManager());
		
		// 生成したAdapterをViewPagerにセットする
		mViewPager = (ViewPager) getView().findViewById(R.id.collation_viewpager);
		mViewPager.setAdapter(mPagerAdapter);
	}
	
	
	@Override
	public void onResume ()
	{
		super.onResume();
		((MainActivity) getActivity()).MENU_STATE = this.MENU_STATE;
	}
	
	
	
	/**
	 * オプションメニューの生成
	 */
	@Override
	public void onCreateOptionsMenu (Menu menu, MenuInflater inflater)
	{
		inflater.inflate(R.menu.fragment_collection, menu);
	}
	
	
	/**
	 * オプションメニュー押下時の処理
	 */
	@Override
	public boolean onOptionsItemSelected (MenuItem item)
	{
		switch (item.getItemId()) {
		case R.id.menu_collection_help:
			break;
		}
		return true;
	}
	
	
	
	/**
	 * フラグメントを格納するAdapter
	 */
	public class CollectionListPagerAdapter extends FragmentPagerAdapter
	{
		public CollectionListPagerAdapter (FragmentManager manager)
		{
			super(manager);
		}

		
		/**
		 * Adapterに格納されたフラグメントを取得する
		 */
		@Override
		public Fragment getItem(int position) {
			return null;
		}

		
		/**
		 * 格納したフラグメントの総数を取得する
		 */
		@Override
		public int getCount() {
			return 0;
		}
		
		
		/**
		 * 指定ポジションのフラグメントタイトルを取得する
		 */
		@Override
		public CharSequence getPageTitle (int position)
		{
			switch (position) {
			case 0:
				break;
			}
			return null;
		}
	}
}
