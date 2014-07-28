package com.nexters.pack.activity;

import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

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
		
		initResources();
        initEvents();
	}
	@Override
	protected void onResume() {
        super.onResume();
        // 세션을 초기화 한다
        if(Session.initializeSession(this, mySessionCallback)){
            // 1. 세션을 갱신 중이면, 프로그레스바를 보이거나 버튼을 숨기는 등의 액션을 취한다
            loginButton.setVisibility(View.GONE);
        } else if (Session.getCurrentSession().isOpened()){
            // 2. 세션이 오픈된된 상태이면, 다음 activity로 이동한다.
        	onSessionOpened();
        }
            // 3. else 로그인 창이 보인다.
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
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                setResult(RESULT_OK, null);
                finish();
            }
        });
	}
	private void signUp(){
		Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
        startActivity(intent);
	}
	private String getPassword() {
        return passwordEt.getText().toString();
    }

    private String getEmail() {
        return emailEt.getText().toString();
    }
    
    protected void onSessionOpened(){
    	loginButton.setVisibility(View.INVISIBLE);
    	requestMe();
    }
    
    private void requestMe() {
        UserManagement.requestMe(new MeResponseCallback() {

            @Override
            protected void onSuccess(final UserProfile userProfile) {
                App.log("UserProfile : " + userProfile);
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
						// TODO Auto-generated method stub
						ImageView profileIV = (ImageView)findViewById(R.id.profileImgView);
						profileIV.setImageDrawable(new BitmapDrawable(getResources(), loadedImage));
					}

					@Override
					public void onLoadingCancelled(String imageUri, View view) {
						// TODO Auto-generated method stub
						
					}
                	
                });
            }

            @Override
            protected void onNotSignedUp() {
            	App.log("onNotSignedUp");
            }

            @Override
            protected void onSessionClosedFailure(final APIErrorResult errorResult) {
            	App.log("onSessionClosedFailure");
            }

            @Override
            protected void onFailure(final APIErrorResult errorResult) {
                String message = "failed to get user info. msg=" + errorResult;
                App.log(message);
                
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
        }

        /**
         * 세션이 삭제되었으니 로그인 화면이 보여야 한다.
         * @param exception  에러가 발생하여 close가 된 경우 해당 exception
         */
        @Override
        public void onSessionClosed(final KakaoException exception) {
            // 프로그레스바를 보이고 있었다면 중지하고 세션 오픈을 못했으니 다시 로그인 버튼 노출.
            loginButton.setVisibility(View.VISIBLE);
        }

    }
}
