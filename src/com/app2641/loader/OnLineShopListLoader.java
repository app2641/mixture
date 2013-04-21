package com.app2641.loader;

import com.app2641.model.DatabaseHelper;
import com.app2641.model.ShopModel;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class OnLineShopListLoader extends SimpleCursorLoader {

	public OnLineShopListLoader(Context context) {
		super(context);
	}

	
	
	
	@Override
	public Cursor loadInBackground() {
		DatabaseHelper helper = new DatabaseHelper(getContext());
		SQLiteDatabase db = helper.getReadableDatabase();
		
		ShopModel model = new ShopModel();
		Cursor cursor = model.fetchOnLineShopList(db);
		
		return cursor;
	}

}
