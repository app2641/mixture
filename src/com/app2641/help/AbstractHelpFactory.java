package com.app2641.help;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

public abstract class AbstractHelpFactory {
	
	// ヘルプダイアログのタイトルをfactoryから取得する
	abstract int getTitle ();
	
	// ヘルプダイアログのテキストをfactoryから取得する
	abstract int getMessage ();
	
	// ヘルプダイアログのアイコンをfactoryから取得する
	public int getIcon ()
	{
		return android.R.drawable.ic_menu_info_details;
	}
	
	// ヘルプダイアログのOKボタンイベントハンドラを取得する
	public OnClickListener getOnclickListener ()
	{
		return new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		};
	}
}
