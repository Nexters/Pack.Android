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
		
		// ��ġ�̺�Ʈ ���� 
		ggMap.setOnMapClickListener(this);
		
		// �� ��ġ�̵�.
		ggMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 15));

		// ��Ŀ ����.
		ggMap.addMarker(
				new MarkerOptions().position(position).title("����"))
				.showInfoWindow();

		// ��Ŀ Ŭ�� ������
		ggMap.setOnMarkerClickListener(new OnMarkerClickListener() {

			public boolean onMarkerClick(Marker marker) {

				return false;
			}
		});
		
		
	
		
	}

	@Override
	public void onMapClick(LatLng point) {
		
		// ���� ������ �浵���� ȭ�� ����Ʈ�� �˷��ش�
		Point screenPt = ggMap.getProjection().toScreenLocation(point);
		
		// ���� ȭ�鿡 ���� ����Ʈ�� ���� ������ �浵�� �˷��ش�.
		LatLng latLng = ggMap.getProjection().fromScreenLocation(screenPt);
		Log.d("����ǥ","��ǥ: ����(" + String.valueOf(point.latitude) + "), �浵(" + String.valueOf(point.longitude) + ")");
		Log.d("ȭ����ǥ","ȭ����ǥ: X(" + String.valueOf(screenPt.x) + "), Y(" + String.valueOf(screenPt.y) + ")");
	}

}
