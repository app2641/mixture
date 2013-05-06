package com.app2641.dialog;

import com.app2641.mixture.R;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;

public class MainMenuHelpDialog extends DialogFragment {

	
	public MainMenuHelpDialog ()
	{
	}
	
	
	@Override
	public Dialog onCreateDialog (Bundle savedInstanceState)
	{
		LayoutInflater inflater = getActivity().getLayoutInflater();
		View view = inflater.inflate(R.layout.dialog_help_mainmenu, null, false);
		
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle(R.string.dialog_main_menu_help_title);
		builder.setView(view);
		builder.setNegativeButton("OK", null);
		
		return builder.create();
	}
}
