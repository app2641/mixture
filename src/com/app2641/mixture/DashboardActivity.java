package com.app2641.mixture;

import java.io.IOException;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.app.FragmentManager;
import android.view.View;
import android.widget.Toast;

public class DashboardActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dashboard);
		
		// database������
		initDatabse();
	}
	
	
	/**
	 * database����������
	 * ����N�����Ƀf�[�^�x�[�X�𐶐�����
	 */
	public void initDatabse ()
	{
		DatabaseHelper db = new DatabaseHelper(this);
		
		try {
			db.init();
			
		} catch (IOException e) {
			throw new Error("Unable to create database");
		}
	}
	
	
	/**********
	 * click event
	 **********/
	// scan������
	public void moveToScan (View v)
	{
		Intent intent = new Intent("com.google.zxing.client.android.SCAN");
		intent.putExtra("SCAN_MODE", "ONE_D_MODE");
		
		try {
//			throw new ActivityNotFoundException();
			startActivityForResult(intent, 0);
			
		} catch (ActivityNotFoundException e) {
			FragmentManager manager = getFragmentManager();
			FragmentTransaction transaction = manager.beginTransaction();
			
			Fragment fragment = new FragmentDashboardFailedScan();
			transaction.replace(R.id.fragment_dashboard, fragment);
			transaction.addToBackStack(null);
			transaction.commit();
		}
		
	}
	
	// intent�̃��X�|���X����
	@Override
	public void onActivityResult (int requestCode, int resultCode, Intent intent)
	{
		if (resultCode == RESULT_OK) {
			// scan��̏���
			if (requestCode == 0) {
				String code = intent.getStringExtra("SCAN_RESULT");
				Toast.makeText(this, code, 0).show();
				
				FragmentManager manager = getFragmentManager();
				FragmentTransaction transaction = manager.beginTransaction();
				
				Fragment fragment = new FragmentItemResult();
				transaction.replace(R.id.fragment_dashboard, fragment);
				transaction.addToBackStack(null);
				
				transaction.commit();
			}
		}
	}
}
