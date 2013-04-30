package com.app2641.fragment;

import com.app2641.adapter.OnLineShopListAdapter;
import com.app2641.dialog.ShopMaxQtyErrorDialog;
import com.app2641.loader.OnLineShopListLoader;
import com.app2641.mixture.R;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

public class OnLineShopListFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor> {

	private OnLineShopListAdapter mAdapter;
	
	private OnLineShopListLoader mLoader;
	
	
	
	/**
	 * コンストラクタ
	 */
	public OnLineShopListFragment ()
	{
	}
	
	
	
	@Override
	public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.fragment_shop_list, container, false);
		return view;
	}
	
	
	
	@Override
	public void onActivityCreated (Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		
		// Adapterにレイアウト情報をセット
		mAdapter = new OnLineShopListAdapter(getActivity().getApplicationContext(), null);
		
		// Loaderの初期化
		getLoaderManager().initLoader(0, null, this);
		
		
		// ListViewにAdapterをセットする
		ListView list = (ListView) getView().findViewById(android.R.id.list);
		list.setAdapter(mAdapter);
		
		list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			// リストアイテムタップ時のイベント
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				ListView list = (ListView) parent;
				Cursor cursor = (Cursor) list.getItemAtPosition(position);
				
				// 選択した素材の現在の所持数
				int qty = cursor.getInt(cursor.getColumnIndex("qty"));
				
				// 素材所持限度数
				int max = getActivity().getResources().getInteger(R.integer.max_material_qty);
				

				if (qty == max) {
					// 既に限度数所持をしている場合は買えないよダイアログ
					ShopMaxQtyErrorDialog dialog = new ShopMaxQtyErrorDialog();
					dialog.show(getFragmentManager(), "max_qty");
					
				} else {
					// 現在の所持金を取得
//					SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
//					int money = sp.getInt("MONEY", 0);
//					
//					Bundle bundle = new Bundle();
//					bundle.putInt("_id", cursor.getInt(cursor.getColumnIndex("_id")));
//					bundle.putString("name", cursor.getString(cursor.getColumnIndex("name")));
//					bundle.putInt("price", cursor.getInt(cursor.getColumnIndex("price")));
//					bundle.putInt("money", money);
//					
//					// 商品購入ダイアログの表示					
//					mDialog = new OffLineShopBuyDialog();
//					mDialog.setArguments(bundle);
//					mDialog.show(getFragmentManager(), "offline_shop_buy");
				}
			}
		});
	}
	
	
	
	@Override
	public void setEmptyText (CharSequence text) {
		TextView empty_view = (TextView) getListView().getEmptyView();
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
		int level = sp.getInt("LEVEL", 1);
		
		// ショップリストのEmptyText設定
		if (level == 1) {
			// ショップ利用レベルに達していない場合
			empty_view.setText(R.string.fragment_shop_disable_error);
		} else {
			// ショップで購入できる素材がない場合
			empty_view.setText(R.string.fragment_shop_none_list_error);
		}
	}
	
	
	
	@Override
	public void onDestroyView ()
	{
		super.onDestroyView();
		
		// Loaderの破棄
		getLoaderManager().destroyLoader(0);
	}
	
	
	
	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle bundle) {
		// CursorLoaderの生成
		return new OnLineShopListLoader(getActivity().getApplicationContext());
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
		// CursorをAdapterから閉じる処理
		mAdapter.swapCursor(cursor);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {
		mAdapter.swapCursor(null);
	}
}
