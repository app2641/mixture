package com.app2641.dialog;

import com.app2641.mixture.R;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

public class OffLineShopBuyDialog extends DialogFragment {

	private DialogCallback mCallbacks;
	
	
	/**
	 * コンストラクタ
	 */
	public OffLineShopBuyDialog () {}
	
	
	@Override
	public Dialog onCreateDialog (Bundle savedInstanceState)
	{
		LayoutInflater inflater = getActivity().getLayoutInflater();
		View view = inflater.inflate(R.layout.activity_collection, null, false);
		
		Bundle bundle = getArguments();
		int qty = bundle.getInt("qty");
		int max = getActivity().getResources().getInteger(R.integer.max_material_qty);
		
		
		// 素材名をViewに指定する
		TextView name_view = (TextView) view.findViewById(R.id.dialog_offline_shop_buy_name);
		name_view.setText(bundle.getString("name"));
		
		
		// 個数スピナーのアイテムを生成
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity().getApplicationContext(), android.R.layout.simple_spinner_item);
		for (int i = 1; i <= (max - qty); i++) {
			adapter.add(String.valueOf(i));
		}
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		
		Spinner qty_spinner = (Spinner) view.findViewById(R.id.dialog_offline_shop_buy_qty_spinner);
		qty_spinner.setAdapter(adapter);
		
		// spinner選択イベント
		qty_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				// 個数を選択した時
				selectedSpinnerItem(parent, view, position, id);
				Spinner spinner = (Spinner) parent;
				String selected = (String) spinner.getSelectedItem();
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// 何も選択されなかった場合
			}
		});
		
		
		// 計算テキストの設定
		int money = bundle.getInt("money");
		int price = bundle.getInt("price");
		TextView calculate_view = (TextView) view.findViewById(R.id.dialog_shop_buy_calculation_text);
		String calculate = buildCalculateText(money, price, 1);
		calculate_view.setText(calculate);
		
		
		
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle(R.string.dialog_offline_shop_buy_title);
		builder.setView(view);
		
		// ボタンの設定
		builder.setNegativeButton(R.string.dialog_shop_buy_cancel_button, null);
		builder.setPositiveButton(R.string.dialog_shop_buy_submit_button, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				mCallbacks = (DialogCallback) getTargetFragment();
				mCallbacks.onClickPositiveButton(getArguments());
			}
		});
		
		return builder.create();
	}
	
	
	
	/**
	 * 個数Spinnerでアイテムを選択した時の処理
	 */
	public void selectedSpinnerItem (AdapterView<?> parent, View view, int position, long id)
	{
		Spinner qty_spinner = (Spinner) parent;
		String selected = (String) qty_spinner.getSelectedItem();
		
		int money = getArguments().getInt("money");
		int price = getArguments().getInt("price");
		
		// 計算テキストビューの更新
		TextView calculate_view = (TextView) view.findViewById(R.id.dialog_shop_buy_calculation_text);
		String calculate_text = buildCalculateText(money, price, Integer.parseInt(selected));
		calculate_view.setText(calculate_text);
		
		// 残高テキストビューの更新
		TextView balance_view = (TextView) view.findViewById(R.id.dialog_shop_buy_balance_text);
		int balance = money - (price * Integer.parseInt(selected));
		String balance_text = String.valueOf(balance);
		balance_view.setText(balance_text);
		
		// 残高がマイナスの場合はTextColorを変更する
		boolean enable_flag = true;
		if (balance < 0) {
			balance_view.setTextColor(getActivity().getResources().getColor(android.R.color.holo_red_light));
			enable_flag = false;
			
		} else {
			balance_view.setTextColor(getActivity().getResources().getColor(android.R.color.black));
		}
		
		// ポジティブボタンのenable/disableをコールバック
		mCallbacks = (DialogCallback) getTargetFragment();
		mCallbacks.setPositiveButtonEnabled(enable_flag);
	}
	
	
	
	/**
	 * 素材購入の計算式テキストを生成する
	 */
	public String buildCalculateText (int money, int price, int qty)
	{
		String calculate = String.valueOf(money) + " - (" +
			String.valueOf(price) + " * " + String.valueOf(qty) + ") =";
		
		return calculate;
	}
	
	
	
	/**
	 * 素材購入後の残高を生成する
	 */
	public int buildBalanceInteger (TextView view, int money, int price, int qty)
	{
		return 0;
	}
	
	
	
	/**
	 * コールバックインターフェース
	 */
	public interface DialogCallback
	{
		public void onClickPositiveButton(Bundle bundle);
		
		public void setPositiveButtonEnabled(boolean flag);
	}
}
