package com.app2641.loader;

import java.io.IOException;

import com.app2641.model.DatabaseHelper;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

public class InitApplicationLoader extends AsyncTaskLoader<String> {
	

	public InitApplicationLoader(Context context) {
		super(context);
	}



	@Override
	public String loadInBackground() {
		// データベースの初期化
		initDatabase();
		
		return "success";
	}

	
	
	/**
	 * データベース初期化処理
	 */
	public void initDatabase ()
	{
		DatabaseHelper db = new DatabaseHelper(getContext());
		
		try {
			db.init();
			
		} catch (IOException e) {
			throw new Error("Unable to create database");
		}
	}
}
