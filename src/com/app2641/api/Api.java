package com.app2641.api;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import android.content.AsyncTaskLoader;
import android.content.Context;

public class Api extends AsyncTaskLoader<String> {
	
	private InterfaceApiFactory factory;

	public Api (Context context, InterfaceApiFactory factory)
	{
		super(context);
		this.factory = factory;
	}

	@Override
	public String loadInBackground ()
	{
		try {
			DefaultHttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost(this.factory.getUrl());
			HttpParams param = client.getParams();
			HttpConnectionParams.setConnectionTimeout(param, 5000);
			
			HttpResponse response = client.execute(post);
			
			if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
				throw new Exception("status");
			}
			
			String json = EntityUtils.toString(response.getEntity(), "UTF-8");
			
			// json解析
			JSONObject root = new JSONObject(json);
			String success = root.getString("success");
			
			// apikeyの生成失敗
			if (success == "false") {
				throw new Exception(root.getString("msg"));
			}
			
			return root.getString("key");
			
		} catch (Exception e) {
			return "false" + e.getMessage();
		}
	}
}
