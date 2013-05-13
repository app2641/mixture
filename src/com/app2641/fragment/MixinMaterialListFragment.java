package com.app2641.fragment;

import com.app2641.activity.MixinShakeActivity;
import com.app2641.adapter.MixinMaterialListAdapter;
import com.app2641.loader.MixinMaterialListLoader;
import com.app2641.mixture.R;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

public class MixinMaterialListFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor> {

	public MixinMaterialListAdapter mAdapter;
	
	public String cls;
	public String sort;
	
	
	public MixinMaterialListFragment ()
	{
	}
	
	
	@Override
	public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		Bundle bundle = getArguments();
		cls = bundle.getString("class", "D");
		sort = bundle.getString("sort", "date");
		
		return inflater.inflate(R.layout.fragment_mixin_material_list, container, false);
	}
	
	
	
	@Override
	public void onActivityCreated (Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		
		
		// Adapterの生成
		mAdapter = new MixinMaterialListAdapter(getActivity().getApplicationContext(), null);
		
		// Loaderの初期化
		getLoaderManager().initLoader(0, null, this);
		
		// ListViewにAdapterをセットする
		ListView list = (ListView) getView().findViewById(android.R.id.list);
		list.setAdapter(mAdapter);
		
		// リスト押下時の処理
		list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				ListView list = (ListView) parent;
				Cursor cursor = (Cursor) list.getItemAtPosition(position);
				
				// ふたつめの素材選択画面へ遷移
				Intent intent = new Intent(getActivity().getApplicationContext(), MixinShakeActivity.class);
				intent.setAction(Intent.ACTION_VIEW);
				intent.putExtra("id", cursor.getInt(cursor.getColumnIndex("_id")));
				getActivity().startActivity(intent);
			}
		});
	}
	
	
	@Override
	public void onResume () {
		super.onResume();
		getLoaderManager().restartLoader(0, null, this);
	}
	
	
	@Override
	public void onDestroyView ()
	{
		super.onDestroyView();
		
		// Loaderの破棄
		getLoaderManager().destroyLoader(0);
	}

	
	
	/**
	 * Loaderの初期化
	 */
	public Loader<Cursor> onCreateLoader(int id, Bundle bundle)
	{
		// CursorLoaderの生成
		return new MixinMaterialListLoader(getActivity().getApplicationContext(), cls, sort);
	}
	
	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
		// CursorをAdapterから閉じる処理
		mAdapter.swapCursor(cursor);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> cursor) {
		mAdapter.swapCursor(null);
	}
}

