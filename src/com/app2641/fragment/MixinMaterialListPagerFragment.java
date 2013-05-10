package com.app2641.fragment;

import net.simonvt.menudrawer.MenuDrawer;

import com.app2641.mixture.MainActivity;
import com.app2641.mixture.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class MixinMaterialListPagerFragment extends Fragment {
	
	public ViewPager mViewPager;
	
	public String cls;

	
	public MixinMaterialListPagerFragment ()
	{
	}
	
	
	@Override
	public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		// クラス名を取得
		cls = getArguments().getString("class", "D");
		return inflater.inflate(R.layout.fragment_mixin_material_list_pager, container, false);
	}
	
	
	@Override
	public void onActivityCreated (Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		
		// ViewPager用のAdapter生成
		MaterialListPagerAdapter adapter = new MaterialListPagerAdapter(getChildFragmentManager());
		
		// 生成したAdapterをViewPagerにセットする
		mViewPager = (ViewPager) getView().findViewById(R.id.mixin_viewpager);
		mViewPager.setAdapter(adapter);
		
		// Pagerのタブを移動した時に発火するイベントリスナーを設定する
		mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				// MenuDrawerを表示するかしないか
				int mode = (position == 0) ? MenuDrawer.TOUCH_MODE_FULLSCREEN: MenuDrawer.TOUCH_MODE_NONE;
						
				MainActivity act = (MainActivity) getActivity();
				if (act != null) {
					act.mMenuDrawer.setTouchMode(mode);
				}
			}
		});
	}
	
	
	
	/**
	 * フラグメントを格納するアダプター
	 */
	public class MaterialListPagerAdapter extends FragmentPagerAdapter
	{
		public MaterialListPagerAdapter(FragmentManager manager) {
			super(manager);
		}
		

		@Override
		public Fragment getItem(int position) {
			// ポジションで表示順のフラグを判別
			String sort = "date";
			switch (position) {
			case 1:
				sort = "name";
				break;
				
			case 2:
				sort = "qty";
				break;
			}
			
			Bundle bundle = new Bundle();
			bundle.putString("sort", sort);
			bundle.putString("class", cls);
			
			Fragment fragment = new MixinMaterialListFragment();
			fragment.setArguments(bundle);
			return fragment;
		}

		
		@Override
		public int getCount() {
			return 3;
		}
		
		
		@Override
		public CharSequence getPageTitle (int position)
		{
			switch (position) {
			case 0:
				return getResources().getString(R.string.fragment_mixin_pager_title_date);
				
			case 1:
				return getResources().getString(R.string.fragment_mixin_pager_title_name);
				
			case 2:
				return getResources().getString(R.string.fragment_mixin_pager_title_qty);
			}
			
			return null;
		}
	}
	
}
