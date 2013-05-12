package com.app2641.activity;

import com.app2641.adapter.SecondMixinListAdapter;
import com.app2641.fragment.MixinShakeFragment;
import com.app2641.loader.SecondeMixinListLoader;
import com.app2641.mixture.R;

import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.app.ListActivity;
import android.app.LoaderManager;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

public class SecondMixinListActivity extends FragmentActivity implements LoaderManager.LoaderCallbacks<Cursor> {
	
	// ひとつ目の素材id
	int first_id;
	
	SecondMixinListAdapter mAdapter;
	
	
	@Override
	public void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_second_mixin_list);
		
		first_id = getIntent().getIntExtra("id", 0);
		
		// アダプターを生成する
		mAdapter = new SecondMixinListAdapter(getApplicationContext(), null);
		
		// Loaderの初期化
		getLoaderManager().initLoader(0, null, this);
			
		// ListViewにAdapterをセットする
		ListView list = (ListView) findViewById(android.R.id.list);
		list.setAdapter(mAdapter);
		
		list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				ListView list = (ListView) parent;
				Cursor cursor = (Cursor) list.getItemAtPosition(position);
				
				// 既に生成経験があるかどうか
				int experience = cursor.getInt(cursor.getColumnIndex("experience"));
				if (experience == 1) {
					// Dialogでシェイク
				} else {
					// シェイク画面へ遷移
					Bundle bundle = new Bundle();
					bundle.putInt("m1_id", cursor.getInt(cursor.getColumnIndex("_id")));
					bundle.putString("m1_name", cursor.getString(cursor.getColumnIndex("m1_name")));
					bundle.putInt("m1_qty", cursor.getInt(cursor.getColumnIndex("m1_qty")));
					bundle.putInt("m2_id", cursor.getInt(cursor.getColumnIndex("m2_id")));
					bundle.putString("m2_name", cursor.getString(cursor.getColumnIndex("m2_name")));
					bundle.putInt("m2_qty", cursor.getInt(cursor.getColumnIndex("m2_qty")));
					
					MixinShakeFragment fragment = new MixinShakeFragment();
					fragment.setArguments(bundle);
					
					FragmentTransaction trans = getSupportFragmentManager().beginTransaction();
					trans.replace(R.id.mixin_second_list_container, fragment);
					trans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
					trans.addToBackStack("mixin_shake");
					trans.commit();
				}
			}
		});
		
		Toast.makeText(getApplicationContext(), "ふたつめのミックスイン素材を指定してください", Toast.LENGTH_SHORT).show();
	}


	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle bundle) {
		// Loaderの生成
		return new SecondeMixinListLoader(getApplicationContext(), first_id);
	}


	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
		mAdapter.swapCursor(cursor);
	}


	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		mAdapter.swapCursor(null);
	}

}
