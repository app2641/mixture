package com.app2641.fragment;

import com.app2641.mixture.R;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

public class FragmentDashboardTable extends MixtureFragment {
	
	public int container_id = R.id.activity_dashboard_container;
	
	public int getContainerId ()
	{
		return container_id;
	}
	
	
	
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
		inflater.inflate(R.menu.fragment_dashboard, menu);
	}
	
}
