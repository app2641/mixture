package com.app2641.fragment;

import com.app2641.mixture.R;
import com.app2641.mixture.R.id;
import com.app2641.mixture.R.layout;

import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class FragmentDashboardInstallScanApp extends MixtureFragment {
	
	public int container_id = R.id.activity_dashboard_container;
	
	public int getContainerId ()
	{
		return container_id;
	}
	
	
	
	@Override
	public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		return inflater.inflate(R.layout.activity_not_found_scan_app, container, false);
	}
	
	@Override
	public void onStart ()
	{
		super.onStart();
		
		Button btn = (Button) getActivity().findViewById(R.id.download_scan_app_button);
		
		btn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Uri uri = Uri.parse("market://details?id=com.google.zxing.client.android");
				Intent intent = new Intent(Intent.ACTION_VIEW, uri);
				startActivity(intent);
			}
		});
	}

}
