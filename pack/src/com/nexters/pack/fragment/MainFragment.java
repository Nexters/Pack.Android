package com.nexters.pack.fragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
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
import com.loopj.android.http.RequestParams;
import com.nexters.pack.R;
import com.nexters.pack.activity.SplashActivity;
import com.nexters.pack.activity.StationActivity;
import com.nexters.pack.core.App;
import com.nexters.pack.network.APIResponseHandler;
import com.nexters.pack.network.HttpUtil;
import com.nexters.pack.network.URL;
import com.nexters.pack.util.ImageManagingHelper;

public class MainFragment extends BaseSherlockFragment implements OnMapClickListener{
    private Bundle mArguments;
    private ViewGroup mainListContainer;
    private Drawable mActionBarBackgroundDrawable;
    
    private GoogleMap ggMap;
    
    
    @Override
	public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	
    	
    }
	@SuppressLint("NewApi")
	@Override
	public View getView(ViewGroup container) {
    	final View view = inflater.inflate(R.layout.fragment_main, container, false);
        if (mArguments != null){
        	// arg 처리 
        }
        mainListContainer = (ViewGroup) view.findViewById(R.id.main_list_ll);
        
        String url = URL.GUESTHOUSE_NEAR;
        RequestParams params = new RequestParams();
        params.put("lat", "128");
        params.put("lng", "35");
        params.put("token", App.getToken(activity));
        
        // 토큰이 으로 유저 정보 가져오기
        /*
        HttpUtil.post(url, null, params, new APIResponseHandler(activity) {
            @Override
            public void onStart() {
                super.onStart();
                showLoading();
            }
            @Override
            public void onFinish() {
                super.onFinish();
                hideLoading();
            }
            @Override
            public void onSuccess(JSONObject response) {
            	App.log("HTTP2  : " + response.toString());
            	if(response.optJSONObject("data") != null){
            		JSONArray guestHouseArray = response.optJSONArray("data");
            		for(int i = 0 ; i < guestHouseArray.length(); i++){
            			try {
							JSONObject guestHouse = guestHouseArray.getJSONObject(i);
							addGuesthouseListItem(guestHouse);
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
            			
            		}
            	}
            }
        });
        */
        //test
        for(int i=0; i < 30; i++){
        	addGuesthouseListItem(null);
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
    private void addGuesthouseListItem(JSONObject guestHouse){
    	//App.log("addGuesthouseListItem : " + guestHouse.toString());
    	// 서버에서 받아야와하는 데이터!
    	View v = inflater.inflate(R.layout.main_item, null);
    	TextView nameTV = (TextView) v.findViewById(R.id.main_item_name_tv);
    	TextView addressTV = (TextView) v.findViewById(R.id.main_item_address_tv);
    	ImageView imageView = (ImageView) v.findViewById(R.id.main_item_iv);
		BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
		Bitmap bitmap = drawable.getBitmap();
		imageView.setImageBitmap(ImageManagingHelper.getCroppedBitmap(bitmap, 80));
		
		if (nameTV != null) {
			//nameTV.setText(guestHouse.optString("name"));
		}
		if (addressTV != null) {
			//addressTV.setText(guestHouse.optString("address"));
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
