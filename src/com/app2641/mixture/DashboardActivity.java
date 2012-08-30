package com.app2641.mixture;

import java.io.IOException;

import com.google.ads.*;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.app.FragmentManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class DashboardActivity extends Activity {
	
	private AdView adView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dashboard);
		
		// database������
		initDatabse();
		
		// fragment������
		initFragment();
		
		// �L���̕\��
		initAdMob();
	}
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_dashboard, menu);
        return true;
    }
	
	public boolean onOptionsItemSelected (MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_help:
			Toast.makeText(this, "help", Toast.LENGTH_SHORT).show();
			break;
		}
		return true;
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
	
	
	/**
	 * fragment������
	 */
	public void initFragment ()
	{
		FragmentManager manager = getFragmentManager();
		FragmentTransaction transaction = manager.beginTransaction();
		Fragment frag = manager.findFragmentByTag("fragment_dashboard_table");
		
		if (frag == null) {
			frag = new FragmentDashboardTable();
			transaction.add(R.id.activity_dashboard_container, frag, "fragment_dashboard_table");
		}
		transaction.commit();
	}
	
	
	/**
	 * �L���̏�����
	 */
	public void initAdMob ()
	{
		adView = (AdView) findViewById(R.id.adView);
		adView.loadAd(new AdRequest());
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
			transaction.replace(R.id.activity_dashboard_container, fragment);
			transaction.addToBackStack(null);
			transaction.commit();
		}
	}
	
	// mix ������
	public void moveToMix (View v)
	{
		
	}
	
	// shop������
	public void moveToShop (View v)
	{
		Intent intent = new Intent(this, ShopActivity.class);
		intent.setAction(Intent.ACTION_VIEW);
		startActivity(intent);
	}
	
	// collection������
	public void moveToCollection (View v)
	{
		Intent intent = new Intent(this, CollectionActivity.class);
		intent.setAction(Intent.ACTION_VIEW);
		startActivity(intent);
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
				transaction.replace(R.id.activity_dashboard_container, fragment);
				transaction.addToBackStack(null);
				
				transaction.commit();
			}
		}
	}
}
