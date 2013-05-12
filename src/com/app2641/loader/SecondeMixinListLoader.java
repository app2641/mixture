package com.app2641.loader;

import com.app2641.model.DatabaseHelper;
import com.app2641.model.MaterialModel;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.content.CursorLoader;

public class SecondeMixinListLoader extends CursorLoader {
	
	public int first_id;
	

	public SecondeMixinListLoader(Context context, int first_id) {
		super(context);
		
		this.first_id = first_id;
	}

	
	@Override
	public Cursor loadInBackground () {
		DatabaseHelper helper = new DatabaseHelper(getContext());
		SQLiteDatabase db = helper.getReadableDatabase();
		
		MaterialModel model = new MaterialModel();
		Cursor cursor = model.fetchAllSecondeMaterialById(db, first_id);
		
		return cursor;
	}
}
