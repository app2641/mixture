package com.app2641.dialog;

import com.app2641.mixture.R;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;

public class ShopHelpDialog extends DialogFragment {

	
	public ShopHelpDialog ()
	{
		
	}
	
	
	
	@Override
	public Dialog onCreateDialog (Bundle savedInstanceState)
	{
		LayoutInflater inflater = getActivity().getLayoutInflater();
		View view = inflater.inflate(R.layout.dialog_help_shop, null, false);
		
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		
		builder.setIcon(android.R.drawable.ic_menu_info_details);
		builder.setTitle(R.string.dialog_help_shop_title);
		builder.setView(view);
		builder.setPositiveButton("OK", null);
		
		return builder.create();
	}
	
}
