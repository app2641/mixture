package com.app2641.mixture;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

public class FragmentImportant extends Fragment {
	
	@Override
	public void onCreate (Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}

	@Override
	public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		return inflater.inflate(R.layout.fragment_important, container, false);
	}
	
	@Override
	public void onCreateOptionsMenu (Menu menu, MenuInflater inflater)
	{
		inflater.inflate(R.menu.fragment_important, menu);
	}
	
	
}
