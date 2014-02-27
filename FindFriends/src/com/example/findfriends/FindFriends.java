package com.example.findfriends;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class FindFriends extends Activity {
	Button m_Button1;
	EditText editText;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.findfriends);
		m_Button1=(Button)findViewById(R.id.button1);
		m_Button1.setOnClickListener(new Button.OnClickListener(){
			public void onClick(View v){			
				try {
					editText=(EditText)findViewById(R.id.editText1);				
					if(editText.getText().toString().equals("")){					
						DisplayToast("�Է����ֲ���Ϊ��");					
						return ;				
					}							
					JSONObject findFriendsData=new JSONObject();
					findFriendsData.accumulate("username",BigPackage.username);				
					findFriendsData.accumulate("findFriendsName",editText.getText().toString());				
					findFriendsData.accumulate("requestType", "findFriends");				
					CommunicationJSON communicationJSON=new CommunicationJSON();							
					HttpResponse httpResponse;								
					httpResponse = communicationJSON.execute(findFriendsData);			
					if(httpResponse.getStatusLine().getStatusCode()==200){					    
						JSONObject responseData=new JSONObject(EntityUtils.toString(httpResponse.getEntity()));				    
						if(responseData.getString("flag").equals("findFriendsSuccess")){
							DisplayToast("�ѷ�������");
				    	/*Log.e("responseLogIn", responseData.toString());
						Intent intent=new Intent();
						intent.setClass(Begin.this,Main.class);
						startActivity(intent);
						Begin.this.finish();*/				    				    
						}				    
						else if(responseData.getString("flag").equals("findFriendsFail")){				    					    
							DisplayToast("�����ڵ��û�");				    	
							return ;				    				    
						}
						else if(responseData.getString("flag").equals("friendsExisting")){
							DisplayToast("�����Ѿ��Ǻ�����");				    	
							return ;
						}else if(responseData.getString("flag").equals("youself")){
							DisplayToast("���ܼ��Լ�Ϊ����");				    	
							return ;
						}
					}
					else{
						DisplayToast("���Ӳ�������");	
					}
				} catch (JSONException e) {					
					// TODO Auto-generated catch block
					e.printStackTrace();
		
				} catch (ClientProtocolException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		    }
		});
		
	}
	public void DisplayToast(String str){
		Toast.makeText(this, str, Toast.LENGTH_SHORT).show();

	}



}
