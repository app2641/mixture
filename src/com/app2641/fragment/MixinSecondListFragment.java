package com.app2641.fragment;

import com.app2641.adapter.MixinSecondListAdapter;
import com.app2641.dialog.ShakeDialog;
import com.app2641.loader.MixinSecondListLoader;
import com.app2641.mixture.R;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

public class MixinSecondListFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor> {
	
	// ひとつ目の素材id
	int first_id;
	
	MixinSecondListAdapter mAdapter;
	
	
	@Override
	public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_second_mixin, container, false);
	}
	
	
	@Override
	public void onActivityCreated (Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		first_id = getArguments().getInt("id");
		
		// アダプターを生成する
		mAdapter = new MixinSecondListAdapter(getActivity().getApplicationContext(), null);
		
		// Loaderの初期化
		getLoaderManager().initLoader(0, null, this);
			
		// ListViewにAdapterをセットする
		ListView list = (ListView) getView().findViewById(android.R.id.list);
		list.setAdapter(mAdapter);
		
		list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				ListView list = (ListView) parent;
				Cursor cursor = (Cursor) list.getItemAtPosition(position);
				
				Bundle bundle = new Bundle();
				bundle.putInt("m1_id", cursor.getInt(cursor.getColumnIndex("_id")));
				bundle.putString("m1_name", cursor.getString(cursor.getColumnIndex("m1_name")));
				bundle.putInt("m1_qty", cursor.getInt(cursor.getColumnIndex("m1_qty")));
				bundle.putInt("m2_id", cursor.getInt(cursor.getColumnIndex("m2_id")));
				bundle.putString("m2_name", cursor.getString(cursor.getColumnIndex("m2_name")));
				bundle.putInt("m2_qty", cursor.getInt(cursor.getColumnIndex("m2_qty")));
				
				// 既に生成経験があるかどうか
				int experience = cursor.getInt(cursor.getColumnIndex("experience"));
				if (experience == 1) {
					// Dialogでシェイク
					ShakeDialog dialog = new ShakeDialog();
					dialog.setArguments(bundle);
					dialog.show(getChildFragmentManager(), "shake_dialog");
					
				} else {
					// シェイク画面へ遷移
					MixinShakeFragment fragment = new MixinShakeFragment();
					fragment.setArguments(bundle);
					
					FragmentTransaction trans = getChildFragmentManager().beginTransaction();
					trans.replace(R.id.activity_container, fragment);
					trans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
					trans.addToBackStack("mixin_shake");
					trans.commit();
				}
			}
		});
		
		Toast.makeText(getActivity().getApplicationContext(), "ふたつめのミックスイン素材を指定してください", Toast.LENGTH_SHORT).show();
	}


	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle bundle) {
		return new MixinSecondListLoader(getActivity().getApplicationContext(), first_id);
	}


	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
		mAdapter.swapCursor(cursor);
	}


	@Override
	public void onLoaderReset(Loader<Cursor> cursor) {
		mAdapter.swapCursor(null);
	}
}
