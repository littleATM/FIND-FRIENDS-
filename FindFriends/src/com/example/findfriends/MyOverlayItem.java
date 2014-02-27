package com.example.findfriends;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.OverlayItem;
import com.baidu.platform.comapi.basestruct.GeoPoint;

public class MyOverlayItem extends OverlayItem {
	Button button=null;

	public MyOverlayItem(GeoPoint arg0, String arg1, String arg2) {
		super(arg0, arg1, arg2);
		// TODO Auto-generated constructor stub
	}
	public void pop(final MapView mMapView){
		if(button!=null)
   		     mMapView.removeView(button);
		 button = new Button(mMapView.getContext());
         button.setBackgroundResource(R.drawable.popup);
         button.setText(this.getTitle());
	         GeoPoint pt = new GeoPoint (this.getPoint().getLatitudeE6(),this.getPoint().getLongitudeE6());
	         //�������ֲ���
	         MapView.LayoutParams layoutParam  = new MapView.LayoutParams(
	               //�ؼ���,�̳���ViewGroup.LayoutParams
	               MapView.LayoutParams.WRAP_CONTENT,
	                //�ؼ���,�̳���ViewGroup.LayoutParams
	               MapView.LayoutParams.WRAP_CONTENT,
	               //ʹ�ؼ��̶���ĳ������λ��
	                pt,
	                -4,
	                -40,
	               //�ؼ����뷽ʽ
	                 MapView.LayoutParams.BOTTOM_CENTER);
	         //���View��MapView��
	         button.setOnClickListener(new OnClickListener(){
	        	 public void onClick(View view){
	        		 mMapView.removeView(button);
	        	 }
	         });
	         mMapView.addView(button,layoutParam);
	}

}
