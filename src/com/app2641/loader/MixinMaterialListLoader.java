package com.app2641.loader;

import com.app2641.model.DatabaseHelper;
import com.app2641.model.MaterialModel;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class MixinMaterialListLoader extends SimpleCursorLoader {
	
	public String cls;
	public String sort;
	

	public MixinMaterialListLoader(Context context, String cls, String sort) {
		super(context);
		
		this.cls = cls;
		this.sort = sort;
	}
	
	

	@Override
	public Cursor loadInBackground() {
		DatabaseHelper helper = new DatabaseHelper(getContext());
		SQLiteDatabase db = helper.getReadableDatabase();
		
		MaterialModel model = new MaterialModel();
		Cursor cursor = model.fetchAllByClassWithSort(db, cls, sort);
		
		return cursor;
	}

}
