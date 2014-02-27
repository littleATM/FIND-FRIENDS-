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

public class Main extends Activity {
	Button m_Button1,m_Button2,m_Button3;
	//JSONObject signInData=new JSONObject();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		m_Button1=(Button)findViewById(R.id.button1);
		m_Button2=(Button)findViewById(R.id.button2);
		m_Button3=(Button)findViewById(R.id.button3);
		m_Button1.setOnClickListener(new Button.OnClickListener(){
			public void onClick(View v){	
				Intent intent=new Intent();
				intent.setClass(Main.this,Friends.class);
				startActivity(intent);
				//Main.this.finish();
			}});
		m_Button2.setOnClickListener(new Button.OnClickListener(){ 
			public void onClick(View v){	
				Intent intent=new Intent();
				intent.setClass(Main.this,AllFriendsMap.class);
				startActivity(intent);
				//Main.this.finish();
	    }
	});
		m_Button3.setOnClickListener(new Button.OnClickListener(){ 
			public void onClick(View v){	
				Intent intent=new Intent();
				intent.setClass(Main.this,PeopleNearbyMap.class);
				startActivity(intent);
				//Main.this.finish();
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
