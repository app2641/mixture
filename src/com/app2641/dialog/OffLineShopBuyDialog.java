package com.app2641.dialog;

import com.app2641.mixture.R;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;

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
		int qty = getArguments().getInt("qty");
		int max = getActivity().getResources().getInteger(R.integer.max_material_qty);
		
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
	 * コールバックインターフェース
	 */
	public interface DialogCallback
	{
		public void onClickPositiveButton(Bundle bundle);
	}
}
