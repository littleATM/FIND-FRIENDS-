package com.example.findfriends;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import android.util.Log;

public class CommunicationJSON {
	HttpClient httpClient;
	HttpPost httpPost;
	HttpResponse httpResponse;
	
	public HttpResponse execute(JSONObject data) throws ClientProtocolException, IOException{
		httpClient=new DefaultHttpClient();
		httpPost=new HttpPost(URL.URL);
		httpPost.setEntity(new StringEntity(data.toString()));
		Log.e("StringEntity", data.toString());
		httpResponse =httpClient.execute(httpPost);
		return httpResponse;
		
	}

}
