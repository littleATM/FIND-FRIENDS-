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

public class SignIn extends Activity {
	Button m_Button1;
	//JSONObject signInData=new JSONObject();
	EditText signInUserNameEditText,signInUserPasswordEditText,signInUserPasswordResumeEditText;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.signin);
		m_Button1=(Button)findViewById(R.id.button1);
		m_Button1.setOnClickListener(new Button.OnClickListener(){
			public void onClick(View v){			
				signInUserNameEditText=(EditText)findViewById(R.id.username);
				signInUserPasswordEditText=(EditText)findViewById(R.id.password);
				signInUserPasswordResumeEditText=(EditText)findViewById(R.id.resumepassword);
				if(signInUserPasswordEditText.getText().toString().equals(signInUserPasswordResumeEditText.getText().toString())){
				   try { 
					   if(signInUserNameEditText.getText().toString().equals("")||signInUserPasswordEditText.getText().toString().equals("")){
						   DisplayToast("用户名或者密码不能为空");
						   return;
					   }
					   //JSONObject map=new JSONObject();
					   JSONObject signInData=new JSONObject();				    
					   signInData.accumulate("username",signInUserNameEditText.getText().toString());								    
					   signInData.accumulate("password",signInUserPasswordEditText.getText().toString());				
					   								    					
				    	//signInData.accumulate("requestData",map);	
				    	signInData.accumulate("requestType","signIn");
				    	
				    	CommunicationJSON communicationJSON=new CommunicationJSON();								
				    	HttpResponse httpResponse;									
				    	httpResponse = communicationJSON.execute(signInData);							   
				    	if(httpResponse.getStatusLine().getStatusCode()==200){					
				    		
						    JSONObject responseData=new JSONObject(EntityUtils.toString(httpResponse.getEntity()));
						    if(responseData.getString("flag").equals("signInSuccess")){
						    	Log.e("responseSignIn", responseData.toString());
						    	BigPackage.username=signInUserNameEditText.getText().toString();
						    	//BigPackage.friendsRequest=responseData.getJSONArray("friendsRequest");
						    	//BigPackage.friends=responseData.getJSONArray("friends");
								Intent intent=new Intent();
								intent.setClass(SignIn.this,Main.class);
								startActivity(intent);
								SignIn.this.finish();
						    	
						    }
						    else if(responseData.getString("flag").equals("usernameExisting")){
						    	DisplayToast("用户名已存在，请重新输入");
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
				else{
					DisplayToast("两次密码不一致，请重新输入");
				}
		
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
