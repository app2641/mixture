package com.app2641.dialog;

import com.app2641.mixture.R;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class ScanResultSaleDialog extends DialogFragment {

	
	public ScanResultSaleDialog ()
	{
	}

	
	@Override
	public Dialog onCreateDialog (Bundle savedInstanceState)
	{
		LayoutInflater inflater = getActivity().getLayoutInflater();
		View view = inflater.inflate(R.layout.dialog_scan_result_sale, null, false);
		
		// Viewに売却後の所持金をセットする
		Bundle bundle = getArguments();
		final String name = bundle.getString("name");
		final int price = bundle.getInt("price");
		final int money = bundle.getInt("money");
		final int sum = price + money;
		
		TextView money_view = (TextView) view.findViewById(R.id.dialog_scan_result_sale_money);
		money_view.setText(String.valueOf(sum) + "pr");
		
		
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		
		builder.setTitle(R.string.dialog_scan_result_sale_title);
		builder.setView(view);
		
		builder.setNegativeButton("キャンセル", null);
		builder.setPositiveButton("売却", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// 所持金を更新する
				SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
				int total = sp.getInt("TOTAL_MONEY", 0);
				Editor editor = sp.edit();
				editor.putInt("MONEY", sum);
				editor.putInt("TOTAL_MONEY", (total + price));
				editor.commit();
				
				// Activityをfinish
				String text = name + "を" + String.valueOf(price) + "prで売却をしました";
				Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
				
				getActivity().finish();
			}
		});
		
		return builder.create();
	}
}
