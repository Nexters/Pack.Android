package com.nexters.pack.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnScrollChangedListener;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

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
import com.nexters.pack.activity.StationActivity;
import com.nexters.pack.core.App;
import com.nexters.pack.util.CommonUtil;

public class MainFragment extends BaseSherlockFragment implements OnMapClickListener{
    private Bundle mArguments;
    private ViewGroup mainListContainer;
    private Drawable mActionBarBackgroundDrawable;
    
    private GoogleMap ggMap;
    
    
    @Override
	public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	
    	
    }
    @Override
	public View getView(ViewGroup container) {
    	final View view = inflater.inflate(R.layout.fragment_main, container, false);
        if (mArguments != null){
        	// arg 처리 
        }
        mainListContainer = (ViewGroup) view.findViewById(R.id.main_list_ll);
        // 임시로 30개
        for(int i=0; i < 30; i++){
        	addItem(i);
        }
        
        final ScrollView s = (ScrollView) view;
        // 투명 이미지를 만들어서 맵뷰와 스크롤뷰 터치 둘다 가능하도록 만듬
        ImageView transparentImageView = (ImageView) view.findViewById(R.id.transparent_map_image);
        transparentImageView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                   case MotionEvent.ACTION_DOWN:
                        // Disallow ScrollView to intercept touch events.
                        s.requestDisallowInterceptTouchEvent(true);
                        // Disable touch on transparent view
                        return false;

                   case MotionEvent.ACTION_UP:
                        // Allow ScrollView to intercept touch events.
                        s.requestDisallowInterceptTouchEvent(false);
                        return true;

                   case MotionEvent.ACTION_MOVE:
                        s.requestDisallowInterceptTouchEvent(true);
                        return false;

                   default: 
                        return true;
                }   
            }
        });
        
        // 액션바 페이딩을 위한
        
        mActionBarBackgroundDrawable = activity.getResources().getDrawable(R.color.window_actionbar_background);
        activity.getActionBar().setBackgroundDrawable(mActionBarBackgroundDrawable);
        /*
        mActionBarBackgroundDrawable.setAlpha(0);
        s.getViewTreeObserver().addOnScrollChangedListener(new OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
            	int actionBarHeight = CommonUtil.pxToDp(view.getContext(), activity.getActionBar().getHeight());
            	int mapHeight = CommonUtil.pxToDp(view.getContext(), view.findViewById(R.id.main_map).getHeight());
            	
            	float headerHeight = mapHeight + actionBarHeight;
                int scrollY = s.getScrollY();
                float ratio = (float) Math.min(Math.max(scrollY, 0), headerHeight) / headerHeight;
                int newAlpha = (int) (ratio * 255);
                mActionBarBackgroundDrawable.setAlpha(newAlpha);
            }
        });
        */
        
        initMap();
        return view;
    	
    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mArguments = getArguments();
       
    }
    
    private void initMap() {
		String coordinates[] = { "37.517180", "127.041268" };
		double lat = Double.parseDouble(coordinates[0]);
		double lng = Double.parseDouble(coordinates[1]);


		LatLng position = new LatLng(lat, lng);
		GooglePlayServicesUtil.isGooglePlayServicesAvailable(activity);

		ggMap = ((SupportMapFragment) getFragmentManager()
				.findFragmentById(R.id.main_map)).getMap();
		
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

				return true;
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

		App.log("맵좌표","좌표: 위도(" + String.valueOf(point.latitude) + "), 경도(" + String.valueOf(point.longitude) + ")");
		App.log("화면좌표","화면좌표: X(" + String.valueOf(screenPt.x) + "), Y(" + String.valueOf(screenPt.y) + ")");
	}
    private void addItem(int index){
    	// 서버에서 받아야와하는 데이터!
    	View v = inflater.inflate(R.layout.main_item, null);
    	TextView textView = (TextView) v
				.findViewById(R.id.main_item_tv);
		ImageView imageView = (ImageView) v
				.findViewById(R.id.main_item_iv);
		if (textView != null) {
			textView.setText("야호 테스트");
		}
		v.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 임시로 이동 -> Framgment 이동 으로 변경(addFragment)
				Intent intent = new Intent(activity, StationActivity.class);
		        startActivity(intent);
			}

		});
    	mainListContainer.addView(v);
    }
    
}
