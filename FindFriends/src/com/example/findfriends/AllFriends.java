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
public class AllFriends extends ListActivity {


	private List<String> mData;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try {		
			JSONObject friendsIist=new JSONObject();
			friendsIist.accumulate("username",BigPackage.username);																	
			//logInData.accumulate("requestData",map);
			friendsIist.accumulate("requestType", "friendsIist");
			CommunicationJSON communicationJSON=new CommunicationJSON();			
			HttpResponse httpResponse;				
			httpResponse = communicationJSON.execute(friendsIist);			
		    if(httpResponse.getStatusLine().getStatusCode()==200){	
			    JSONObject responseData=new JSONObject(EntityUtils.toString(httpResponse.getEntity()));
			    if(responseData.getString("flag").equals("friendsIistSuccess")){
			    	BigPackage.friends=responseData.getJSONArray("responseData");
			    	//BigPackage.friendsRequest=responseData.getJSONArray("friendsRequest");
			    	//BigPackage.friends=responseData.getJSONArray("friends");
					if(BigPackage.friends.length()==0){
						DisplayToast("����û���κκ���");
						return;
					}
					mData = getData();
			    }
			    else{
			    	DisplayToast("�޷���������");
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

		for(int i=0;i<BigPackage.friends.length();i++){
			list.add(BigPackage.friends.getString(i));
			
		}
		return list;
	}
	
	// ListView ��ĳ�ѡ�к���߼�
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		

	}
	
	/**
	 * listview�е�����������Ի���
	 */
	public void delete(final int position){
		new AlertDialog.Builder(this)
		.setTitle("ɾ������")
		.setMessage("ȷ��Ҫɾ��������")
		.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				try {
					JSONObject findFriendsData=new JSONObject();				
					findFriendsData.accumulate("username",BigPackage.username);											
					findFriendsData.accumulate("name",BigPackage.friends.getString(position));								
					findFriendsData.accumulate("requestType", "deleteFriends");								
					CommunicationJSON communicationJSON=new CommunicationJSON();											
					HttpResponse httpResponse;												
					httpResponse = communicationJSON.execute(findFriendsData);							
					if(httpResponse.getStatusLine().getStatusCode()==200){					    					
						JSONObject responseData=new JSONObject(EntityUtils.toString(httpResponse.getEntity()));				    					
						if(responseData.getString("flag").equals("deleteSuccess")){						
							DisplayToast("������ɾ��");		
							Intent intent=new Intent();
							intent.setClass(AllFriends.this,AllFriends.class);
							startActivity(intent);
							AllFriends.this.finish();
							
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
		}).setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {
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
				
				convertView = mInflater.inflate(R.layout.allfriends, null);
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
			    	try {
						BigPackage.specifiedFriendName=BigPackage.friends.getString(position);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					Intent intent=new Intent();								
					intent.setClass(AllFriends.this,SpecifiedFriendsMap.class);								
					startActivity(intent);						   
				}
			});
			holder.button2.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					delete(position);				
				}
			});
			
			
			return convertView;
		}
		
	}
	
	
	
	
	public void DisplayToast(String str){
		Toast.makeText(this, str, Toast.LENGTH_SHORT).show();

	}
	
	
}

/*public class AllFriends extends Activity {
	/*private LinearLayout mLayout;
	private ScrollView mScrollView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.allfriends);
		
		//mLayout=(LinearLayout) findViewById(R.id.layout);
		if(BigPackage.frieds.length()==0){
			
			return ;
		}
		for(int i=0;i<BigPackage.frieds.length();i++){
			TextView textView=new TextView(AllFriends.this);
			//textView.setText("")
		}
	}*/
/*	  private ListView listView;
	    //private List<String> data = new ArrayList<String>();
	    @Override
	    public void onCreate(Bundle savedInstanceState){
	        super.onCreate(savedInstanceState);
	       try {  
	            listView = new ListView(this);
				while(BigPackage.frieds.length()==0){
					DisplayToast("�������ڻ�û�к���");
					return;
				}
	            listView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1,getData()));
	        

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        setContentView(listView);
	    }
	     
	    
	     
	     
	    private List<String> getData() throws JSONException{
	         
	        List<String> data = new ArrayList<String>();
	        for(int i=0;i<BigPackage.frieds.length();i++){
	        	data.add(BigPackage.frieds.getJSONObject(i).getString("name"));
	        }
	         
	        return data;
	    }
		public void DisplayToast(String str){
			Toast.makeText(this, str, Toast.LENGTH_SHORT).show();

		}



}*/
