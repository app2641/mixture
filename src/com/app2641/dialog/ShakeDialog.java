package com.app2641.dialog;

import com.app2641.dialog.OffLineShopBuyDialog.DialogCallback;
import com.app2641.mixture.R;
import com.app2641.model.DatabaseHelper;
import com.app2641.model.MixinModel;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

public class ShakeDialog extends DialogFragment {

	private DialogCallback mCallbacks;
	
	
	public ShakeDialog () {
	}
	
	
	@Override
	public Dialog onCreateDialog (Bundle savedInstanceState) {
		LayoutInflater inflater = getActivity().getLayoutInflater();
		View view = inflater.inflate(R.layout.dialog_shake, null, false);
		
		// ビューの設定
		Bundle bundle = getArguments();
		Cursor cursor = this.getItemRecord(bundle.getInt("m1_id"), bundle.getInt("m2_id"));
		cursor.moveToFirst();
		
		String txt = "「"+ bundle.getString("m1_name") + "」と「" + bundle.getString("m2_name") +
			"」を掛けわせて「" + cursor.getString(cursor.getColumnIndex("name")) + "」を生成しますか？";
		cursor.close();
		
		TextView text_view = (TextView) view.findViewById(R.id.dialog_shake_text);
		text_view.setText(txt);
		
		
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle(R.string.dialog_mixin_shake_title);
		builder.setView(view);
		
		builder.setNegativeButton("キャンセル", null);
		builder.setPositiveButton("ミックスイン", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				mCallbacks = (DialogCallback) getTargetFragment();
				mCallbacks.onClickPositiveButton(getArguments());
			}
		});
		
		return builder.create();
	}
	
	
	/**
	 * 素材1idと素材2idを合成して生成できるアイテムレコードを取得する
	 */
	public Cursor getItemRecord (int m1_id, int m2_id) {
		DatabaseHelper helpder = new DatabaseHelper(getActivity().getApplicationContext());
		SQLiteDatabase db = helpder.getReadableDatabase();
		
		MixinModel model = new MixinModel();
		return model.fetchItemByMaterialIds(db, m1_id, m2_id);
	}
	
}
