package com.app2641.dialog;

import com.app2641.mixture.R;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class ShopMaxQtyErrorDialog extends DialogFragment {

	
	public ShopMaxQtyErrorDialog () {
		
	}
	
	
	
	@Override
	public Dialog onCreateDialog (Bundle savedInstanceState)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		
		builder.setTitle(R.string.dialog_shop_max_qty_error_title);
		builder.setMessage(R.string.dialog_shop_max_qty_error_message);
		builder.setPositiveButton("OK", null);
		
		return builder.create();
	}
	
}
