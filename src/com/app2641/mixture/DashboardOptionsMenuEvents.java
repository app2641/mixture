package com.app2641.mixture;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.MenuItem;
import android.widget.Toast;

public class DashboardOptionsMenuEvents
{
	private MenuItem item;
	private Context context;
	
	public DashboardOptionsMenuEvents (Context context, MenuItem item)
	{
		this.context = context;
		this.item = item;
	}
	
	public void dashboardHelp ()
	{
		Toast.makeText(this.context, "help", Toast.LENGTH_SHORT).show();
	}
	
	public void detailHelp ()
	{
		Toast.makeText(this.context, "item detail help", Toast.LENGTH_SHORT).show();
	}
	
	public void importantHelp ()
	{
		Toast.makeText(this.context, "important help", Toast.LENGTH_SHORT).show();
	}
}