package com.app2641.fragment;

import com.app2641.mixture.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class MixinShakeFragment extends Fragment {

	public int m1_id;
	public String m1_name;
	public int m1_qty;
	
	public int m2_id;
	public String m2_name;
	public int m2_qty;
	
	public MixinShakeFragment () {
	}
	
	
	
	@Override
	public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		Bundle bundle = getArguments();
		m1_id = bundle.getInt("m1_id");
		m1_name = bundle.getString("m1_name");
		m1_qty = bundle.getInt("m1_qty");
		
		m2_id = bundle.getInt("m2_id");
		m2_name = bundle.getString("m2_name");
		m2_qty = bundle.getInt("m2_qty");
		
		
		return inflater.inflate(R.layout.fragment_mixin_shake, container, false);
	}
	
	
	@Override
	public void onActivityCreated (Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		String text = m1_name + " と " + m2_name + " を掛けあわせて新しいアイテムを生成します。\n" +
			"携帯端末を振ると素材の合成が始まります。";
		
		TextView shake_view = (TextView) getView().findViewById(R.id.mixin_shake_text);
		shake_view.setText(text);
	}
}
