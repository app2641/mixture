package com.app2641.activity;

import com.app2641.fragment.MixinSecondListFragment;
import com.app2641.mixture.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

public class MixinShakeActivity extends FragmentActivity {

	// ひとつめの素材id
	public int first_id;
	
	
	@Override
	public void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shake);
		
		// ひとつめの素材id
		first_id = getIntent().getIntExtra("id", 1);
		
		// ふたつめの素材リストにfragmentを差し替え
		Bundle bundle = new Bundle();
		bundle.putInt("id", first_id);
		
		Fragment fragment = new MixinSecondListFragment();
		fragment.setArguments(bundle);
		
		FragmentTransaction trans = getSupportFragmentManager().beginTransaction();
		trans.replace(R.id.activity_container, fragment);
		trans.commit();
	}
	
}
