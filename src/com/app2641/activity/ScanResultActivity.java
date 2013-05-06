package com.app2641.activity;

import com.app2641.dialog.FirstRareDialog;
import com.app2641.dialog.FirstScanDialog;
import com.app2641.dialog.ScanResultSaleDialog;
import com.app2641.mixture.R;
import com.app2641.model.DatabaseHelper;
import com.app2641.model.MaterialModel;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ScanResultActivity extends FragmentActivity implements OnClickListener {
	
	// 素材処理を行ったかどうかのフラグ
	public boolean do_flag = false;

	
	@Override
	public void onCreate (Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_scan_result);
		
		// 素材データをViewにセットする
		setMaterialView();
		
		// 素材取得ボタンのイベントリスナーを設定する
		Button get_btn = (Button) findViewById(R.id.scan_result_get_material_button);
		get_btn.setOnClickListener(this);
		
		// この素材の取得数が9だった場合は取得ボタンをdisable
		int qty = getIntent().getIntExtra("qty", 0);
		if (qty == getResources().getInteger(R.integer.max_material_qty)) {
			get_btn.setEnabled(false);
		}
		
		// 素材売却ボタンのイベントリスナーを登録する
		Button sale_btn = (Button) findViewById(R.id.scan_result_sale_material_button);
		sale_btn.setOnClickListener(this);
		
		
		// first_scan / first_rare フラグの有無でダイアログを表示する
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		boolean first_scan = sp.getBoolean("FIRST_SCAN", false);
		boolean first_rare = sp.getBoolean("FIRST_RARE", false);
		
		if (first_scan == false) {
			Editor editor = sp.edit();
			editor.putBoolean("FIRST_SCAN", true);
			editor.commit();
			
			FirstScanDialog dialog = new FirstScanDialog();
			dialog.show(getSupportFragmentManager(), "first_scan");
			
		} else if (first_rare == false) {
			Boolean rare = getIntent().getBooleanExtra("rare", false);
			
			if (rare == true) {
				Editor editor = sp.edit();
				editor.putBoolean("FIRST_RARE", true);
				editor.commit();
				
				FirstRareDialog dialog = new FirstRareDialog();
				dialog.show(getSupportFragmentManager(), "first_rare");
			}
		}
		
	}
	
	
	
	/**
	 * ボタンのクリックイベントリスナー
	 */
	@Override
	public void onClick (View view)
	{
		switch (view.getId()) {
			case R.id.scan_result_get_material_button:
				// 素材取得処理
				dispatchGetMaterialAction();
				break;
				
			case R.id.scan_result_sale_material_button:
				// 素材売却処理
				dispatchSaleMaterialAction();
				break;
		}
	}
	
	
	
	/*
	 * 戻るボタンの処理
	 * 素材を処理するフラグdo_flagがfalseの場合は素材取得処理を行う
	 */
	@Override
	public void onBackPressed ()
	{
		// 素材処理を行ったかどうか
		checkMaterialAction();
	}
	
	
	
	/**
	 * 素材データをViewにセットする
	 */
	public void setMaterialView ()
	{
		Intent intent = getIntent();
		
		// 素材名の設定
		String name = intent.getStringExtra("name");
		TextView name_view = (TextView) findViewById(R.id.scan_result_material_name);
		name_view.setText(name);
		
		// 素材クラスの設定
		String cls = intent.getStringExtra("class");
		TextView cls_view = (TextView) findViewById(R.id.scan_result_material_class);
		cls_view.setText("class "+cls);
		
		// New!バッチ
		LinearLayout container = (LinearLayout) findViewById(R.id.scan_result_icon_container);
		LayoutInflater inflater;
		Integer experience = intent.getIntExtra("experience", 1);
		if (experience == 0) {
			// icon_containerにImageViewを追加する
			inflater = getLayoutInflater();
			inflater.inflate(R.layout.view_scan_result_new_batch, container);
		}
		
		// Rare!バッチ
		Boolean rare = intent.getBooleanExtra("rare", false);
		if (rare == true) {
			// icon_containerにImageViewを追加する
			inflater = getLayoutInflater();
			inflater.inflate(R.layout.view_scan_result_rare_batch, container);
		}
		
		// 素材売却値段の設定
		Integer price = intent.getIntExtra("price", 0);
		TextView price_view = (TextView) findViewById(R.id.scan_result_material_price);
		price_view.setText("価格 "+String.valueOf(price)+"pr");
		
		// 素材説明の設定
		String description = intent.getStringExtra("description");
		TextView description_view = (TextView) findViewById(R.id.scan_result_material_description);
		description_view.setText(description);
	}
	
	
	
	/**
	 * 素材処理を行ったかチェックして処理を分岐させる
	 */
	public void checkMaterialAction ()
	{
		int qty = getIntent().getIntExtra("qty", 0);
			
		if (qty == getResources().getInteger(R.integer.max_material_qty)) {
			// 素材所持数が限度数に達していた場合は素材売却処理
			dispatchSaleMaterialAction();
				
		} else {
			// 素材取得処理
			dispatchGetMaterialAction();
		}
	}
	
	
	
	/**
	 * 素材を取得するアクション
	 */
	public void dispatchGetMaterialAction ()
	{
		DatabaseHelper helper = new DatabaseHelper(getApplicationContext());
		SQLiteDatabase db = helper.getWritableDatabase();
		
		Intent intent = getIntent();
		int qty = intent.getIntExtra("qty", 0);
		int id  = intent.getIntExtra("id", 0);
		String name = intent.getStringExtra("name");
		
		MaterialModel model = new MaterialModel();
		model.updateQty(db, (qty + 1), id);
		
		do_flag = true;
		
		Toast.makeText(this, name+"を取得しました", Toast.LENGTH_SHORT).show();
		
		finish();
	}
	
	
	
	/**
	 * 素材を売却するアクション
	 */
	public void dispatchSaleMaterialAction ()
	{
		// 所持金の取得
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		int money = sp.getInt("MONEY", 0);
		
		// 素材価格の取得
		Intent intent = getIntent();
		String name = intent.getStringExtra("name");
		int price = intent.getIntExtra("price", 0);
		
		Bundle bundle = new Bundle();
		bundle.putString("name", name);
		bundle.putInt("price", price);
		bundle.putInt("money", money);
		
		ScanResultSaleDialog dialog = new ScanResultSaleDialog();
		dialog.setArguments(bundle);
		dialog.show(getSupportFragmentManager(), "scan_result_sale");
	}
	
}
