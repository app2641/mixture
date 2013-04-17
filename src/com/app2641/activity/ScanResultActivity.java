package com.app2641.activity;

import com.app2641.mixture.R;
import com.app2641.model.DatabaseHelper;
import com.app2641.model.MaterialModel;

import net.simonvt.menudrawer.MenuDrawer;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ScanResultActivity extends MixtureActivity implements OnClickListener {
	
	// 素材処理を行ったかどうかのフラグ
	public boolean do_flag = false;
	
	public MenuDrawer mMenuDrawer;

	
	@Override
	public void onCreate (Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		mMenuDrawer = MenuDrawer.attach(this, MenuDrawer.MENU_DRAG_WINDOW);
		mMenuDrawer.setContentView(R.layout.activity_scan_result);
		mMenuDrawer.setMenuView(R.layout.main_menu);
		mMenuDrawer.setTouchMode(MenuDrawer.TOUCH_MODE_FULLSCREEN);
		super.mMenuDrawer = mMenuDrawer;
		super.mActivityName = "scan";
		
		
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
		
		
		// StatusMainMenuの背景色を変更する
		TextView mStatusMainMenu = (TextView) findViewById(R.id.main_menu_scan_item);
		mStatusMainMenu.setBackgroundColor(getResources().getColor(R.color.weight_color));
				
		// Homeアイコンを設定する
		getActionBar().setDisplayHomeAsUpEnabled(true);
				
		// MainMenuのOnClickListenerを初期化する
		initMainMenuOnClickListeners();
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
				finish();
				break;
				
			case R.id.scan_result_sale_material_button:
				// 素材売却処理
				dispatchSaleMaterialAction();
				break;
				
			default:
				// 素材処理を行ったか判断してからMainMenuで移動させる
				checkMaterialAction();
				super.onClick(view);
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
		super.onBackPressed();
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
		if (do_flag == false) {
			int qty = getIntent().getIntExtra("qty", 0);
			
			if (qty == getResources().getInteger(R.integer.max_material_qty)) {
				// 素材所持数が限度数に達していた場合は素材売却処理
				dispatchSaleMaterialAction();
				return;
				
			} else {
				// 素材取得処理
				dispatchGetMaterialAction();
			}
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
	}
	
	
	
	/**
	 * 素材を売却するアクション
	 */
	public void dispatchSaleMaterialAction ()
	{
		// do_flag = true;
	}
	
}
