package com.nexters.pack.activity;

import org.apache.http.Header;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.Window;

import com.loopj.android.http.RequestParams;
import com.nexters.pack.R;
import com.nexters.pack.core.App;
import com.nexters.pack.model.User;
import com.nexters.pack.network.APIResponseHandler;
import com.nexters.pack.network.HttpUtil;
import com.nexters.pack.network.URL;

public class SplashActivity extends BaseActivity {
	
	private static int INTRO_LOADING_TIME = 1000;
    private Handler mHandler = null;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_splash);
		
		mHandler = new Handler();
		mHandler.postDelayed(new Runnable() {
            public void run() {
                endIntro();
            }
        }, INTRO_LOADING_TIME);
	}
	private void endIntro() {
		String url = URL.CHECK;
		
		if( !TextUtils.isEmpty( App.getToken(SplashActivity.this) )){
			RequestParams params = new RequestParams();
	        params.put("channel", "local");
	        params.put("token", App.getToken(SplashActivity.this));
	        
	        // 토큰이 으로 유저 정보 가져오기
	        HttpUtil.post(url, null, params, new APIResponseHandler(SplashActivity.this) {
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
	            public void onSuccess(int statusCode, Header[] headers, JSONObject response){
	            	App.log(response + "");
	            	String code = response.optString("status");
	                if(!TextUtils.isEmpty(code) && code.equals("0") && response.optJSONObject("data") != null){
	                	// 토큰으로 유저 정보 가져옴 -> 메인
	                	App.setToken(SplashActivity.this, response.optJSONObject("data").optString("token"));
	                	User.getInstance().setProfileURL(response.optJSONObject("data").optString("image"));
	                	goMain();
	                }else{
	                	// 토큰정보없음 -> 로그인
	                	goLogin();
	                }
	            }
	        });
		}else{
			goLogin();
		}
	}
	private void goMain(){
		Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        startActivity(intent);
        setResult(RESULT_OK);
        finish();
	}
	private void goLogin(){
		Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
        startActivity(intent);
        setResult(RESULT_OK);
        finish();
	}
}
