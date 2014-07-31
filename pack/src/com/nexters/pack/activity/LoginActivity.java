package com.nexters.pack.activity;

import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gcm.GCMRegistrar;
import com.kakao.APIErrorResult;
import com.kakao.MeResponseCallback;
import com.kakao.Session;
import com.kakao.SessionCallback;
import com.kakao.UserManagement;
import com.kakao.UserProfile;
import com.kakao.exception.KakaoException;
import com.kakao.widget.LoginButton;
import com.loopj.android.http.RequestParams;
import com.nexters.pack.R;
import com.nexters.pack.core.App;
import com.nexters.pack.network.APIResponseHandler;
import com.nexters.pack.network.HttpUtil;
import com.nexters.pack.network.URL;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;



public class LoginActivity extends BaseSherlockActivity implements View.OnClickListener {
	private EditText emailEt;
    private EditText passwordEt;
    private Button signInBtn;
    private Button signUpBtn;
    
    private LoginButton loginButton;
    private final SessionCallback mySessionCallback = new MySessionStatusCallback();
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.ab_background_light));
		
		startGCM();
		initResources();
        initEvents();
        // 세션을 초기화 한다
        if(Session.initializeSession(this, mySessionCallback)){
            // 1. 세션을 갱신 중이면, 프로그레스바를 보이거나 버튼을 숨기는 등의 액션을 취한다
        	App.log("initializeSession");
        } else if (Session.getCurrentSession().isOpened()){
            // 이미 카카오 로그인이 되어있음!
        	loginButton.setVisibility(View.GONE);
        	requestMe(false);
        	App.log("isOpened");
        }
	}
	@Override
	protected void onResume() {
        super.onResume();
       
    }
	
	private void initResources() {
		emailEt = (EditText) findViewById(R.id.et_sign_in_email);
        if(App.SERVER_TARGET == App.SERVER_TEST)
        	emailEt.setText("test@gmail.com");
        passwordEt = (EditText) findViewById(R.id.et_sign_in_password);
        if(App.SERVER_TARGET == App.SERVER_TEST)
        	passwordEt.setText("12341234");
        signInBtn = (Button) findViewById(R.id.btn_sign_in);
        signUpBtn = (Button) findViewById(R.id.btn_sign_up);
        loginButton = (LoginButton) findViewById(R.id.com_kakao_login);
        
    }

    private void initEvents() {
    	signInBtn.setOnClickListener(this);
        signUpBtn.setOnClickListener(this);
        loginButton.setLoginSessionCallback(mySessionCallback);
    }
    
	public void onClick(View v) {
		switch (v.getId()) {
        	case R.id.btn_sign_in:
        		login();
        		break;
        	case R.id.btn_sign_up:
        		signUp();
        		break;
		}
	}
	
	private void login(){
		String url = URL.SIGN_IN;
		
		RequestParams params = new RequestParams();
        params.put("email", getEmail());
        params.put("password", getPassword());
        
        HttpUtil.post(url, null, params, new APIResponseHandler(LoginActivity.this) {

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
            	super.onSuccess(response);
            	App.setToken(LoginActivity.this, response.optJSONObject("data").optString("token"));
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                setResult(RESULT_OK, null);
                finish();
            }
            @Override
        	public void onFailure(Throwable error, String response) {
        		showShortToast( "아이디 비번 확인해라");
        	}
            
        });
	}
	private void signUp(){
		Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
        startActivity(intent);
	}
	private void checkKakao(final UserProfile userProfile){
		String url = URL.CHECK;
		
		RequestParams params = new RequestParams();
        params.put("channel", "kakao");
        params.put("id", userProfile.getId() + "");
        
        HttpUtil.post(url, null, params, new APIResponseHandler(LoginActivity.this) {

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
            	String code = response.optString("status");
                if(!TextUtils.isEmpty(code) && code.equals("0")){
                	// 해당 아이디로 유저가 있음
                	Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    setResult(RESULT_OK, null);
                    finish();
                }else{
                	// 아이디 유저없음 해당 카카오 프로필로 회원가입!
                	Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                	intent.putExtra("kakao",userProfile);
                    startActivity(intent);
                    setResult(RESULT_OK, null);
                    finish();
                }
            	
            }
        });
	}
	private String getPassword() {
        return passwordEt.getText().toString();
    }

    private String getEmail() {
        return emailEt.getText().toString();
    }
    
    protected void onSessionOpened(){
    	loginButton.setVisibility(View.INVISIBLE);
    	requestMe(false);
    }
    
    private void requestMe(final boolean changeActivity) {
        UserManagement.requestMe(new MeResponseCallback() {

            @Override
            protected void onSuccess(final UserProfile userProfile) {
                userProfile.saveUserToCache();
                ImageLoader.getInstance().loadImage(userProfile.getProfileImagePath(),  new ImageLoadingListener() {

					@Override
					public void onLoadingStarted(String imageUri, View view) {
						// TODO Auto-generated method stub
						
					}

					@Override
					public void onLoadingFailed(String imageUri, View view,
							FailReason failReason) {
						// TODO Auto-generated method stub
					
					}

					@Override
					public void onLoadingComplete(String imageUri, View view,
							Bitmap loadedImage) {
						ImageView profileIV = (ImageView)findViewById(R.id.profileImgView);
						profileIV.setImageDrawable(new BitmapDrawable(getResources(), loadedImage));
						if(changeActivity){
							checkKakao(userProfile);
						}
					}

					@Override
					public void onLoadingCancelled(String imageUri, View view) {
						if(changeActivity){
							checkKakao(userProfile);
						}
					}
                	
                });
            }

            @Override
            protected void onNotSignedUp() {
            	App.log("onNotSignedUp");
            	showShortToast("카카오 회원가입이 되어있지 않습니다.");
            }

            @Override
            protected void onSessionClosedFailure(final APIErrorResult errorResult) {
            	App.log("onSessionClosedFailure");
            	showShortToast("카카오 인증에 실패하였습니다.(1)");
            }

            @Override
            protected void onFailure(final APIErrorResult errorResult) {
                String message = "failed to get user info. msg=" + errorResult;
                showShortToast("카카오 인증에 실패하였습니다.(2)");
                
            }
        });
    }
    
    private class MySessionStatusCallback implements SessionCallback {
        /**
         * 세션이 오픈되었으면 가입페이지로 이동 한다.
         */
        @Override
        public void onSessionOpened() {
            // 프로그레스바를 보이고 있었다면 중지하고 세션 오픈후 보일 페이지로 이동
        	LoginActivity.this.onSessionOpened();
        	App.log("onSessionOpened");
        }

        /**
         * 세션이 삭제되었으니 로그인 화면이 보여야 한다.
         * @param exception  에러가 발생하여 close가 된 경우 해당 exception
         */
        @Override
        public void onSessionClosed(final KakaoException exception) {
            // 프로그레스바를 보이고 있었다면 중지하고 세션 오픈을 못했으니 다시 로그인 버튼 노출.
            loginButton.setVisibility(View.VISIBLE);
        	App.log("onSessionClosed");
        }

    }
    
    /**
     * GCM 서비스를 시작한다.
     */
    private void startGCM(){
         
        /**
         * GCM Service가 이용 가능한 Device인지 체크한다.
         * api 8(Android 2.2) 미만인 경우나 GCMService를 이용할 수 없는
         * 디바이스의 경우 오류를 발생시키니 반드시 예외처리하도록 한다.
         */
        try {
            GCMRegistrar.checkDevice(getApplicationContext());
        } catch (Exception e) {
            // TODO: handle exception
            App.log("This device can't use GCM");
            return;
        }
         
         
        /**
         * 2.SharedPreference에 저장된 RegistrationID가 있는지 확인한다.
         * 없는 경우 null이 아닌 ""이 리턴
         */
        String regId = GCMRegistrar.getRegistrationId(getApplicationContext());
         
        /**
         * Registration Id가 없는 경우(어플리케이션 최초 설치로 발급받은 적이 없거나,
         * 삭제 후 재설치 등 SharedPreference에 저장된 Registration Id가 없는 경우가 이에 해당한다.)
         */
        if(regId == "" || regId == null){
            /**
             * 3.RegstrationId가 없는 경우 GCM Server로 Regsitration ID를 발급 요청한다.
             * 발급 요청이 정상적으로 이루어진 경우 Registration ID는 SharedPreference에 저장되며,
             * GCMIntentService.class의 onRegistered를 콜백한다.
             */
            GCMRegistrar.register(getApplicationContext(), App.PROJECT_ID); 
        }else{
        	App.log("Push id : " + regId);
            //Toast.makeText(getApplicationContext(), "Exist Registration Id: " + regId, Toast.LENGTH_LONG).show();
        }
    }
}
