package com.app2641.mixture;

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class FragmentDashboardFailedScan extends Fragment {
	
	@Override
	public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		return inflater.inflate(R.layout.fragment_datashboard_failedscan, container, false);
	}
	
	@Override
	public void onStart ()
	{
		super.onStart();
		
		Button btn = (Button) getActivity().findViewById(R.id.download_scan_app);
		
		btn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Uri uri = Uri.parse("market://details?id=com.google.zxing.client.android");
				Intent intent = new Intent(Intent.ACTION_VIEW, uri);
				startActivity(intent);
			}
		});
	}
}
