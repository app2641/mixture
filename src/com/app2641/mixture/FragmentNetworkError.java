package com.app2641.mixture;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class FragmentNetworkError extends Fragment
{
	@Override
	public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		return inflater.inflate(R.layout.fragment_network_error, container, false);
	}

	@Override
	public void onStart ()
	{
		super.onStart();
		
		Button btn = (Button) getActivity().findViewById(R.id.close_btn);
		
		btn.setOnClickListener(new View.OnClickListener(){
			public void onClick (View view) {
				getActivity().finish();
			}
		});
	}
}
