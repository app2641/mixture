package com.app2641.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.app2641.mixture.R;

public class WelcomeDialog extends DialogFragment {

	public int title = R.string.dialog_welcome_title;
	public int layout = R.layout.dialog_welcome;
	
	
	@Override
	public Dialog onCreateDialog (Bundle savedInstanceState)
	{
		LayoutInflater inflater = getActivity().getLayoutInflater();
		View view = inflater.inflate(layout, null, false);
		
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setIcon(android.R.drawable.ic_menu_info_details);
		builder.setTitle(title);
		builder.setView(view);
		builder.setPositiveButton("OK", null);
		
		return builder.create();
	}
}
