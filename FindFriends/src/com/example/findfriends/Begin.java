package com.example.findfriends;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Begin extends Activity {
	Button m_Button1,m_Button2,m_Button3;
	//JSONObject signInData=new JSONObject();
	EditText logInUserNameEditText,logInUserPasswordEditText;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.begin);
		m_Button1=(Button)findViewById(R.id.button1);
		m_Button2=(Button)findViewById(R.id.button2);
		m_Button1.setOnClickListener(new Button.OnClickListener(){
			public void onClick(View v){			
				logInUserNameEditText=(EditText)findViewById(R.id.username);
				logInUserPasswordEditText=(EditText)findViewById(R.id.password);
				JSONObject map=new JSONObject();
				try {
					if(logInUserNameEditText.getText().toString().equals("")||logInUserPasswordEditText.getText().toString().equals("")){
						DisplayToast("用户名或者密码不能为空");
						return ;
					}			
					JSONObject logInData=new JSONObject();
					logInData.accumulate("username",logInUserNameEditText.getText().toString());												
					logInData.accumulate("password",logInUserPasswordEditText.getText().toString());					
					//logInData.accumulate("requestData",map);
					logInData.accumulate("requestType", "logIn");
					CommunicationJSON communicationJSON=new CommunicationJSON();			
					HttpResponse httpResponse;				
					httpResponse = communicationJSON.execute(logInData);			
				    if(httpResponse.getStatusLine().getStatusCode()==200){	
					    JSONObject responseData=new JSONObject(EntityUtils.toString(httpResponse.getEntity()));
					    if(responseData.getString("flag").equals("logInSuccess")){
					    	Log.e("responseLogIn", responseData.toString());
					    	BigPackage.username=logInUserNameEditText.getText().toString();
					    	//BigPackage.friendsRequest=responseData.getJSONArray("friendsRequest");
					    	//BigPackage.friends=responseData.getJSONArray("friends");
							Intent intent=new Intent();
							intent.setClass(Begin.this,Main.class);
							startActivity(intent);
							Begin.this.finish();
					    	
					    }
					    else if(responseData.getString("flag").equals("logInFail")){
					    	DisplayToast("用户名或者密码错误，请重新输入");
					    	return ;
					    }
				    }
				} catch (ClientProtocolException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		});
		m_Button2.setOnClickListener(new Button.OnClickListener(){ 
			public void onClick(View v){	
				Intent intent=new Intent();
				intent.setClass(Begin.this,SignIn.class);
				startActivity(intent);
				Begin.this.finish();
	    }
	});		
		}
		 
	public void DisplayToast(String str){
		Toast.makeText(this, str, Toast.LENGTH_SHORT).show();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.begin, menu);
		return true;
	}
}
