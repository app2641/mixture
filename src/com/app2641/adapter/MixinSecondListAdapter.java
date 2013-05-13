package com.app2641.adapter;

import com.app2641.mixture.R;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class MixinSecondListAdapter extends CursorAdapter {

	public MixinSecondListAdapter(Context context, Cursor c) {
		super(context, c, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
	}
	
	

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		ViewHolder holder = (ViewHolder) view.getTag();
		holder.name.setText(cursor.getString(cursor.getColumnIndex("m2_name")));
//		holder.date.setText(cursor.getString(cursor.getColumnIndex("m2_last_date")));
		holder.date.setText("最終取得日: 2013/04/02");
		holder.qty.setText(String.valueOf(cursor.getInt(cursor.getColumnIndex("m2_qty"))));
		
		int icon_id = context.getResources().getIdentifier("material_icon", "drawable", context.getPackageName());
		holder.icon.setImageResource(icon_id);
	}

	
	
	@Override
	public View newView(Context context, Cursor cursor, ViewGroup container) {
		LayoutInflater inflater = LayoutInflater.from(context);
		View view = inflater.inflate(R.layout.view_second_mixin_list_item, null);
		
		ViewHolder holder = new ViewHolder();
		holder.icon = (ImageView) view.findViewById(R.id.material_list_icon_image);
		holder.name = (TextView) view.findViewById(R.id.material_list_name_text);
		holder.date = (TextView) view.findViewById(R.id.material_list_last_date_text);
		holder.qty = (TextView) view.findViewById(R.id.material_list_qty_text);
		view.setTag(holder);
		
		return view;
	}

	
	
	/**
	 * ViewHolderパターン
	 */
	private class ViewHolder {
		public ImageView icon;
		public TextView name;
		public TextView date;
		public TextView qty;
	}
}
