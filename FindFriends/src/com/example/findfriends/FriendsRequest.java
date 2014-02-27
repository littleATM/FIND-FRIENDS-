package com.example.findfriends;
import com.example.findfriends.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
public class FriendsRequest extends ListActivity {


	private List<String> mData;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try {		
			JSONObject friendsRequestIist=new JSONObject();
			friendsRequestIist.accumulate("username",BigPackage.username);																	
			//logInData.accumulate("requestData",map);
			friendsRequestIist.accumulate("requestType", "friendsRequestIist");
			CommunicationJSON communicationJSON=new CommunicationJSON();			
			HttpResponse httpResponse;				
			httpResponse = communicationJSON.execute(friendsRequestIist);			
		    if(httpResponse.getStatusLine().getStatusCode()==200){	
		    	Log.e("trace","friendsRequest");
			    JSONObject responseData=new JSONObject(EntityUtils.toString(httpResponse.getEntity()));
			    if(responseData.getString("flag").equals("friendsRequestIistSuccess")){
			    	BigPackage.friendsRequest=responseData.getJSONArray("responseData");
			    	//BigPackage.friendsRequest=responseData.getJSONArray("friendsRequest");
			    	//BigPackage.friends=responseData.getJSONArray("friends");
					if(BigPackage.friendsRequest.length()==0){
						DisplayToast("您还没有任何好友请求");
						return;
					}
					mData = getData();
			    }
			    else{
			    	DisplayToast("无法连接网络");
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
		MyAdapter adapter = new MyAdapter(this);
		setListAdapter(adapter);
	}

	private List<String> getData() throws JSONException {
		List<String> list = new ArrayList<String>();

		for(int i=0;i<BigPackage.friendsRequest.length();i++){
			list.add(BigPackage.friendsRequest.getString(i));
			
		}
		return list;
	}
	
	// ListView 中某项被选中后的逻辑
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		

	}
	
	/**
	 * listview中点击按键弹出对话框
	 */
	public void acceptRequest(final int position){
		new AlertDialog.Builder(this)
		.setTitle("加好友")
		.setMessage("确定要接受请求吗")
		.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				try {
					JSONObject findFriendsData=new JSONObject();				
					findFriendsData.accumulate("username",BigPackage.username);											
					findFriendsData.accumulate("name",BigPackage.friendsRequest.getString(position));								
					findFriendsData.accumulate("requestType", "acceptRequest");								
					CommunicationJSON communicationJSON=new CommunicationJSON();											
					HttpResponse httpResponse;												
					httpResponse = communicationJSON.execute(findFriendsData);							
					if(httpResponse.getStatusLine().getStatusCode()==200){					    					
						JSONObject responseData=new JSONObject(EntityUtils.toString(httpResponse.getEntity()));				    					
						if(responseData.getString("flag").equals("acceptSuccess")){						
							DisplayToast("好友已添加");			
							Intent intent=new Intent();
							intent.setClass(FriendsRequest.this,FriendsRequest.class);
							startActivity(intent);
							FriendsRequest.this.finish();
						}	
						else{
							DisplayToast("error");
						}
					}				
					else{					
						DisplayToast("连接不到网络");					
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
		}).setNegativeButton("取消", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				//AllFriends.this.finish();
			}
		})
		.show();
		
	}
	public void rejectRequest(final int position){
		new AlertDialog.Builder(this)
		.setTitle("拒绝请求")
		.setMessage("确定要拒绝请求吗")
		.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				try {
					JSONObject findFriendsData=new JSONObject();				
					findFriendsData.accumulate("username",BigPackage.username);											
					findFriendsData.accumulate("name",BigPackage.friendsRequest.getString(position));								
					findFriendsData.accumulate("requestType", "rejectRequest");								
					CommunicationJSON communicationJSON=new CommunicationJSON();											
					HttpResponse httpResponse;												
					httpResponse = communicationJSON.execute(findFriendsData);							
					if(httpResponse.getStatusLine().getStatusCode()==200){					    					
						JSONObject responseData=new JSONObject(EntityUtils.toString(httpResponse.getEntity()));				    					
						if(responseData.getString("flag").equals("rejectSuccess")){						
							DisplayToast("请求已拒绝");		
							Intent intent=new Intent();
							intent.setClass(FriendsRequest.this,FriendsRequest.class);
							startActivity(intent);
							FriendsRequest.this.finish();
						}			    								
					}				
					else{					
						DisplayToast("连接不到网络");					
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
		}).setNegativeButton("取消", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				//AllFriends.this.finish();
			}
		})
		.show();
		
	}
	
	
	
	public final class ViewHolder{
		public TextView name;
		public Button button1;
		public Button button2;
	}
	
	
	public class MyAdapter extends BaseAdapter{

		private LayoutInflater mInflater;
		
		
		public MyAdapter(Context context){
			this.mInflater = LayoutInflater.from(context);
		}
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mData.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			
			ViewHolder holder = null;
			if (convertView == null) {
				
				holder=new ViewHolder();  
				
				convertView = mInflater.inflate(R.layout.friendsrequest, null);
				holder.name = (TextView)convertView.findViewById(R.id.name);
				holder.button1 = (Button)convertView.findViewById(R.id.button1);
				holder.button2 = (Button)convertView.findViewById(R.id.button2);
				convertView.setTag(holder);
				
			}else {
				
				holder = (ViewHolder)convertView.getTag();
			}
			
			holder.name.setText((CharSequence) mData.get(position));
			
			holder.button1.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {	
					acceptRequest(position);
				}
			});
			holder.button2.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					rejectRequest(position);				
				}
			});
			
			
			return convertView;
		}
		
	}
	
	
	
	
	public void DisplayToast(String str){
		Toast.makeText(this, str, Toast.LENGTH_SHORT).show();

	}
	
	
}