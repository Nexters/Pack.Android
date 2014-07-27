package com.nexters.pack.activity;

import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

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

public class MapAcitivity extends FragmentActivity  implements OnMapClickListener {
	
	GoogleMap ggMap;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initResources();
	}
	

	private void initResources() {
		setContentView(R.layout.map);
		
		String coordinates[] = { "37.517180", "127.041268" };
		double lat = Double.parseDouble(coordinates[0]);
		double lng = Double.parseDouble(coordinates[1]);

		LatLng position = new LatLng(lat, lng);
		GooglePlayServicesUtil.isGooglePlayServicesAvailable(MapAcitivity.this);

		ggMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
		
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

	@Override
	public void onMapClick(LatLng point) {
		
		// 현재 위도와 경도에서 화면 포인트를 알려준다
		Point screenPt = ggMap.getProjection().toScreenLocation(point);
		
		// 현재 화면에 찍힌 포인트로 부터 위도와 경도를 알려준다.
		LatLng latLng = ggMap.getProjection().fromScreenLocation(screenPt);
		Log.d("맵좌표","좌표: 위도(" + String.valueOf(point.latitude) + "), 경도(" + String.valueOf(point.longitude) + ")");
		Log.d("화면좌표","화면좌표: X(" + String.valueOf(screenPt.x) + "), Y(" + String.valueOf(screenPt.y) + ")");
	}

}
