package com.example.findfriends;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;  
import android.content.res.Configuration;  
import android.graphics.drawable.Drawable;
import android.os.*; 
import android.util.Log;
import android.view.Menu;  
import android.widget.Button;
import android.widget.FrameLayout;  
import android.widget.Toast;  

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.BMapManager;  
import com.baidu.mapapi.map.ItemizedOverlay;
import com.baidu.mapapi.map.LocationData;
import com.baidu.mapapi.map.MKMapViewListener;  
import com.baidu.mapapi.map.MapController;  
import com.baidu.mapapi.map.MapPoi;  
import com.baidu.mapapi.map.MapView;  
import com.baidu.mapapi.map.OverlayItem;
import com.baidu.mapapi.utils.DistanceUtil;
import com.baidu.*;
import com.baidu.platform.comapi.basestruct.GeoPoint;  
   
public class AllFriendsMap extends Activity{  
	BMapManager mBMapMan = null;  
	MapView mMapView = null;
	LocationClient locationClient; 
	LocationClientOption option = new LocationClientOption();
	public BDLocationListener myListener = new MyLocationListener();
	static int first;
        @Override  
        public void onCreate(Bundle savedInstanceState){  
        	super.onCreate(savedInstanceState);  
        	
        	
        	
        	
        	
        	
        	mBMapMan=new BMapManager(getApplication());  
        	mBMapMan.init("68cd3bf72a92698f0ac2df47195dc3fb", null);    
        	//ע�⣺��������setContentViewǰ��ʼ��BMapManager���󣬷���ᱨ��  
        	setContentView(R.layout.allfriendsmap);  
        	mMapView=(MapView)findViewById(R.id.bmapsView);  
        	mMapView.setBuiltInZoomControls(true);  
        	//�����������õ����ſؼ�  
        	MapController mMapController=mMapView.getController();  
        	
        	// �õ�mMapView�Ŀ���Ȩ,�����������ƺ�����ƽ�ƺ�����  
        	//GeoPoint point =new GeoPoint((int)(39.915* 1E6),(int)(116.404* 1E6));  
        	//�ø����ľ�γ�ȹ���һ��GeoPoint����λ��΢�� (�� * 1E6)  
        	//mMapController.setCenter(point);//���õ�ͼ���ĵ�  
        	mMapController.setZoom(7);//���õ�ͼzoom���� 
        	first=0;
        	
        	
        	
        	locationClient=new LocationClient(mBMapMan.getContext());
        	locationClient.setAK("68cd3bf72a92698f0ac2df47195dc3fb");            
            option.setOpenGps(true);
            option.setAddrType("all");
            option.setScanSpan(50000);
            option.disableCache(true);
           
            //option.setPoiDistance(1000);        
            //option.setPoiExtraInfo(true);
            //option.setPriority(  LocationClientOption.NetWorkFirst );
            locationClient.setLocOption(option);          
            locationClient.registerLocationListener( myListener );          
            locationClient.start();
        	locationClient.requestLocation();
        	//Log.e("end", "reach");
        	
        }  
        @Override  
        protected void onDestroy(){  
                mMapView.destroy();  
                if(mBMapMan!=null){  
                        mBMapMan.destroy();  
                        mBMapMan=null;  
                }  
                if (locationClient != null)
                	locationClient.stop();
                super.onDestroy();  
        }  
        @Override  
        protected void onPause(){  
                mMapView.onPause();  
                if(mBMapMan!=null){  
                       mBMapMan.stop();  
                }  
                super.onPause();  
        }  
        @Override  
        protected void onResume(){  
                mMapView.onResume();  
                if(mBMapMan!=null){  
                        mBMapMan.start();  
                }  
               super.onResume();  
        } 
    	public void DisplayToast(String str){
    		Toast.makeText(this, str, Toast.LENGTH_SHORT).show();

    	}
    	 public class MyLocationListener implements BDLocationListener {
             @Override
            public void onReceiveLocation(BDLocation location) {
            	 
            	 Log.e("me",BigPackage.username+"  "+location.getLongitude()+"    "+location.getLatitude());
  
 //transfer the location

               if (location == null||(location.getLatitude()==4.9e-324&&location.getLongitude()==4.9e-324)||mMapView.getOverlays()==null)
                   return ;
               try {		

				    	//BigPackage.friendsRequest=responseData.getJSONArray("friendsRequest");
				    	//BigPackage.friends=responseData.getJSONArray("friends");
				    	//Log.e("firends",BigPackage.specifiedFriendName+"  "+responseData.getDouble("longitude")+"    "+responseData.getDouble("latitude"));
				     /** 
		                *  ����Ҫ���Overlay�ĵط�ʹ�����´��룬 
		                *  ����Activity��onCreate()�� 
		                */  
		               // �ø����ľ�γ�ȹ���GeoPoint����λ��΢�� (�� * 1E6)  
		               //����IteminizedOverlay  
		               //׼��overlayͼ�����ݣ�����ʵ������޸�  
		               Drawable mark= getResources().getDrawable(R.drawable.icon_gcoding); 
		               Drawable markMe= getResources().getDrawable(R.drawable.icon_me); 
		               OverlayTest itemOverlay = new OverlayTest(mark, mMapView);  
		               //��IteminizedOverlay��ӵ�MapView��  
		                 
		           //mMapView.getOverlays().clear();  
   		           

   		           if(mMapView.getOverlays()==null)
   		        	   return;
   		           mMapView.getOverlays().clear();  
   		           mMapView.getOverlays().add(itemOverlay);  

  		               GeoPoint p1 = new GeoPoint((int) (location.getLatitude() * 1E6), (int) (location.getLongitude() * 1E6));  
  		            MyOverlayItem itemMe = new MyOverlayItem(p1,BigPackage.username,BigPackage.username);
		               itemMe.setMarker(markMe);
		               itemOverlay.addItem(itemMe); 
   		               mMapView.refresh();  
   		               //Log.e("abc",location.getLatitude()+ "     "+location.getLongitude());
   		            	mMapView.getController().animateTo(new GeoPoint((int)(location.getLatitude()*1e6),  
   		            	(int)(location.getLongitude()* 1e6))); 
   				JSONObject specifiedFriendsLocation=new JSONObject();
   				specifiedFriendsLocation.accumulate("username",BigPackage.username);																	
   				//logInData.accumulate("requestData",map);
   				specifiedFriendsLocation.accumulate("requestType", "allFriendsLocation");
   				specifiedFriendsLocation.accumulate("longitude",location.getLongitude());
   				specifiedFriendsLocation.accumulate("latitude",location.getLatitude());
   				CommunicationJSON communicationJSON=new CommunicationJSON();			
   				HttpResponse httpResponse;				
   				httpResponse = communicationJSON.execute(specifiedFriendsLocation);	
   				
   			    if(httpResponse.getStatusLine().getStatusCode()==200){   			    	
   				    JSONObject responseData=new JSONObject(EntityUtils.toString(httpResponse.getEntity()));
   				    if(responseData.getString("flag").equals("allFriendsLocationSuccess")){
   				    	
   				    	Log.e("1","exist");
   				    	Log.e("1",responseData.toString());
   				    	
    		               for(int i=0;i<responseData.getJSONArray("responseData").length();i++){
        		        	   if(responseData.getJSONArray("responseData").getJSONObject(i).getString("name").equals(BigPackage.username))
        		        		   continue;
    		            	   GeoPoint p = new GeoPoint((int) (responseData.getJSONArray("responseData").getJSONObject(i).getDouble("latitude") * 1E6)
       		            			, (int) (responseData.getJSONArray("responseData").getJSONObject(i).getDouble("longitude") * 1E6));
    		            	   MyOverlayItem item = new MyOverlayItem(p,responseData.getJSONArray("responseData").getJSONObject(i).getString("name")
        		            		   ,responseData.getJSONArray("responseData").getJSONObject(i).getString("name")); 
           		               //���overlay, ���������Overlayʱʹ��addItem(List<OverlayItem>)Ч�ʸ���     
    		            	   if(responseData.getJSONArray("responseData").getJSONObject(i).getInt("time")>600)
    		            		   item.setMarker(getResources().getDrawable(R.drawable.offline));
           		               itemOverlay.addItem(item); 
   		                   }
   		               //��OverlayItem׼��Overlay����  
   		               //ʹ��setMarker()��������overlayͼƬ,�����������ʹ�ù���ItemizedOverlayʱ��Ĭ������   
   		              // item1.setMarker(mark);                      		                  
   		               //��������׼��������׼���ã�ʹ�����·�������overlay.  
   		               mMapView.refresh();  
   		               //Log.e("abc",location.getLatitude()+ "     "+location.getLongitude());
   		               if(first==0){
   		            	mMapView.getController().animateTo(new GeoPoint((int)(location.getLatitude()*1e6),  
   		            	(int)(location.getLongitude()* 1e6)));
   		            	first++;
   		               } 
   		               //ɾ��overlay .  
   		               //itemOverlay.removeItem(itemOverlay.getItem(0));  
   		               //mMapView.refresh();  
   		               //���overlay  
   		               // itemOverlay.removeAll();  
   		               // mMapView.refresh();  
   				    }
   				    else if(responseData.getString("flag").equals("allFriendsLocationNull")){
   				    	Log.e("1","notexist");
   				    	
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
             public void onReceivePoi(BDLocation poiLocation) {
                  if (poiLocation == null){
                         return ;
                   }         
             }		
 		}
    	 class OverlayTest extends ItemizedOverlay<OverlayItem> {  
 		    //��MapView����ItemizedOverlay  



 		    public OverlayTest(Drawable mark,MapView mapView){  
 		            super(mark,mapView);  
 		    }      	

 		    protected boolean onTap(int index) {  
 		        //�ڴ˴���item����¼�  
 	           MyOverlayItem item = (MyOverlayItem) getItem(index);
 	           item.pop(mMapView);
 		        return true;  
 		    }  
 		        public boolean onTap(GeoPoint pt, MapView mapView){  
 		                //�ڴ˴���MapView�ĵ���¼��������� trueʱ  
 		                super.onTap(pt,mapView);  
 		                return false;  
 		        }  
 		        // ��2.1.1 ��ʼ��ʹ�� add/remove ����overlay , ������д���½ӿ�  
 		        /* 
 		        @Override 
 		        protected OverlayItem createItem(int i) { 
 		                return mGeoList.get(i); 
 		        } 
 		        
 		        @Override 
 		        public int size() { 
 		                return mGeoList.size(); 
 		        } 
 		        */  
 		}
}  