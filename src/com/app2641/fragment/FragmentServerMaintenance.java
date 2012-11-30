package com.app2641.fragment;

import com.app2641.mixture.R;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class FragmentServerMaintenance extends MixtureFragment
{
	public int container_id = R.id.activity_dashboard_container;
	
	public int getContainerId ()
	{
		return this.container_id;
	}
	
	
	
	@Override
	public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		return inflater.inflate(R.layout.fragment_server_maintenance, container, false);
	}

}
