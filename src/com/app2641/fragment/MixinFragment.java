package com.app2641.fragment;

import com.app2641.mixture.MainActivity;
import com.app2641.mixture.R;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

public class MixinFragment extends Fragment implements ActionBar.OnNavigationListener {
	
	public final String MENU_STATE = "mixin";
	
	public MixinFragment ()
	{
	}
	
	
	@Override
	public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		return inflater.inflate(R.layout.fragment_mixin, container, false);
	}
	
	
	@Override
	public void onActivityCreated (Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		
		
		// ActionBarの設定
		final ActionBar actionbar = getActivity().getActionBar();
		actionbar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
		actionbar.setTitle(R.string.fragment_mixin_title);
		
		// ActionBarのリスト構築
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(
			actionbar.getThemedContext(),
			android.R.layout.simple_list_item_1,
			android.R.id.text1,
			new String[]{
				getString(R.string.menu_material_s),
				getString(R.string.menu_material_a),
				getString(R.string.menu_material_b),
				getString(R.string.menu_material_c),
				getString(R.string.menu_material_d)
			}
		);
		actionbar.setListNavigationCallbacks(adapter, this);
	}
	
	
	@Override
	public void onResume ()
	{
		super.onResume();
		((MainActivity) getActivity()).MENU_STATE = this.MENU_STATE;
	}


	
	/**
	 * Actionbarのリストを選択した時の処理
	 */
	@Override
	public boolean onNavigationItemSelected(int position, long id) {
		return false;
	}
	
	
	
	/**
	 * オプションメニューの生成
	 */
	@Override
	public void onCreateOptionsMenu (Menu menu, MenuInflater inflater)
	{
		inflater.inflate(R.menu.fragment_mixin, menu);
	}
	
	
	
	/**
	 * オプションメニュー選択時の処理
	 */
	@Override
	public boolean onOptionsItemSelected (MenuItem item)
	{
		switch (item.getItemId()) {
		case R.id.menu_mixin_help:
			// ヘルプダイアログを表示する
			break;
		}
		return true;
	}

}
