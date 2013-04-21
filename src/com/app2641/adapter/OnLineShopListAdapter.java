package com.app2641.adapter;

import com.app2641.mixture.R;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class OnLineShopListAdapter extends CursorAdapter {

	
	public OnLineShopListAdapter(Context context, Cursor c) {
		super(context, c);
	}

	
	
	/**
	 * CursorデータをViewにバインドする
	 */
	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		// 素材名View
		TextView name_view = (TextView) view.findViewById(R.id.shop_list_item_name);
		name_view.setText(cursor.getString(cursor.getColumnIndex("name")));
	}

	
	
	/**
	 * Layoutを指定してViewを生成する
	 */
	@Override
	public View newView(Context context, Cursor cursor, ViewGroup container) {
		LayoutInflater inflater = LayoutInflater.from(context);
		return inflater.inflate(R.layout.view_shop_list_item, container);
	}

}
