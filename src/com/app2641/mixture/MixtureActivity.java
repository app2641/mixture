package com.app2641.mixture;

import com.app2641.fragment.MixtureFragment;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;

public class MixtureActivity extends Activity {

	public void fragmentReplace (MixtureFragment fragment)
	{
		FragmentManager manager = getFragmentManager();
		FragmentTransaction transaction = manager.beginTransaction();
		
		transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
		transaction.replace(fragment.getContainerId(), fragment);
		transaction.addToBackStack(null);
		transaction.commit();
	}
}
