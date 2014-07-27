package com.nexters.pack.activity;

import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.nexters.pack.R;

public class MainActivity extends SherlockFragmentActivity implements OnMapClickListener{

	GoogleMap ggMap;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initResources();
	}

	private void initResources() {

//		 FadingActionBarHelper helper = new FadingActionBarHelper()
//		 .actionBarBackground(R.color.window_actionbar_background)
//		 .headerLayout(R.layout.map)
//		 .contentLayout(R.layout.activity_main);
//		
//		 setContentView(helper.createView(this));
//		 helper.initActionBar(this);
		setContentView(R.layout.activity_main);
		initMap();
		

	}
	
	private void initMap() {
		String coordinates[] = { "37.517180", "127.041268" };
		double lat = Double.parseDouble(coordinates[0]);
		double lng = Double.parseDouble(coordinates[1]);


		LatLng position = new LatLng(lat, lng);
		GooglePlayServicesUtil.isGooglePlayServicesAvailable(MainActivity.this);

		ggMap = ((SupportMapFragment) getSupportFragmentManager()
				.findFragmentById(R.id.map)).getMap();
		
		// 터치이벤트 설정 
		ggMap.setOnMapClickListener(this);
		
		// 맵 위치이동.
		ggMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 15));

		// 마커 설정.
		ggMap.addMarker(
				new MarkerOptions().position(position).title("제목"))
				.showInfoWindow();

		// 마커 클릭 리스너
		ggMap.setOnMarkerClickListener(new OnMarkerClickListener() {

			public boolean onMarkerClick(Marker marker) {

				return false;
			}
		});		
	}
	/* Map 클릭시 터치 이벤트 
	 * @see com.google.android.gms.maps.GoogleMap.OnMapClickListener#onMapClick(com.google.android.gms.maps.model.LatLng)
	 */
	public void onMapClick(LatLng point) {
		
		// 현재 위도와 경도에서 화면 포인트를 알려준다
		Point screenPt = ggMap.getProjection().toScreenLocation(point);
		
		// 현재 화면에 찍힌 포인트로 부터 위도와 경도를 알려준다.
		LatLng latLng = ggMap.getProjection().fromScreenLocation(screenPt);
		
		//Log.DEBUG(this, "좌표: 위도(" + point.latitude + "), 경도(" + point.longitude + ")", Toast.LENGTH_LONG);
		//Log.DEBUG(this, "화면좌표: X(" + screenPt.x + "), Y(" + screenPt.y + ")", Toast.LENGTH_LONG);
		
		Log.d("맵좌표","좌표: 위도(" + String.valueOf(point.latitude) + "), 경도(" + String.valueOf(point.longitude) + ")");
		Log.d("화면좌표","화면좌표: X(" + String.valueOf(screenPt.x) + "), Y(" + String.valueOf(screenPt.y) + ")");
	}
	
}
