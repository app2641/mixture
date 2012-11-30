package com.app2641.fragment;

import com.app2641.mixture.DatabaseHelper;
import com.app2641.mixture.R;
import com.google.ads.AdRequest;
import com.google.ads.AdView;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class FragmentItemResult extends MixtureFragment
{
	public int container_id = R.id.activity_dashboard_container;
	
	public int getContainerId ()
	{
		return this.container_id;
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
		View view = inflater.inflate(R.layout.fragment_item_result, container, false);
		
		String id = String.valueOf(getArguments().getInt("id"));
		DatabaseHelper helper = new DatabaseHelper(getActivity());
		Cursor c = helper.fetchByMaterialId(id);
		
		if (c.moveToFirst()) {
			TextView name = (TextView) view.findViewById(R.id.item_name);
			name.setText(c.getString(c.getColumnIndex("name")));
			
			TextView cls = (TextView) view.findViewById(R.id.item_class);
			cls.setText("class " + c.getString(c.getColumnIndex("class")));
			
			TextView price = (TextView) view.findViewById(R.id.item_detail_price);
			price.setText("���ꉿ�i: " + String.valueOf(c.getInt(c.getColumnIndex("price"))) + "price");
			
			TextView exp = (TextView) view.findViewById(R.id.item_detail_exp);
			exp.setText("�o���l: " + String.valueOf(c.getInt(c.getColumnIndex("exp"))) + "exp");
			
			TextView description = (TextView) view.findViewById(R.id.item_detail_description);
			description.setText(c.getString(c.getColumnIndex("description")));
		}
		c.close();
		
		return view;
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
	{
		inflater.inflate(R.menu.fragment_item_result, menu);
	}
	
	@Override
	public boolean onOptionsItemSelected (MenuItem item)
	{
		switch (item.getItemId()) {
		case android.R.id.home:
			break;
		}
		
		return true;
	}
	
	
	@Override
	public void onActivityCreated (Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		
		// �L���̏���
		AdView adView = (AdView) getActivity().findViewById(R.id.adView);
		adView.loadAd(new AdRequest());
	}
}
