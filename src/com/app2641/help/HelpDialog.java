package com.app2641.help;

import android.app.AlertDialog;
import android.content.Context;

public class HelpDialog {
	
	public Context context;
	public AbstractHelpFactory factory;
	public AlertDialog.Builder builder;

	public HelpDialog (Context context, AbstractHelpFactory factory)
	{
		this.context = context;
		this.factory = factory;
		this.builder = new AlertDialog.Builder(context);
	}
	
	
	public void show ()
	{
		builder.setIcon(factory.getIcon());
		builder.setTitle(context.getResources().getString(factory.getTitle()));
		builder.setMessage(context.getResources().getString(factory.getMessage()));
		builder.setPositiveButton("OK", factory.getOnclickListener());
		builder.show();
	}
}
