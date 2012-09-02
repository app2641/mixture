package com.app2641.mixture;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

public class FragmentDashboardTable extends Fragment {
	
	@Override
	public void onCreate (Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}
	
	@Override
	public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		return inflater.inflate(R.layout.fragment_dashboard_table, container, false);
	}
	
	@Override
	public void onCreateOptionsMenu (Menu menu, MenuInflater inflater)
	{
		inflater.inflate(R.menu.activity_dashboard, menu);
	}
	
	@Override
	public boolean onOptionsItemSelected (MenuItem item)
	{
		switch (item.getItemId()) {
		case R.id.menu_help:
			Toast.makeText(getActivity(), "help", Toast.LENGTH_SHORT).show();
			break;
		}
		return true;
	}
}
