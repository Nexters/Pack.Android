package com.nexters.pack.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.kakao.UserProfile;
import com.loopj.android.http.*;
import com.nexters.pack.R;
import com.nexters.pack.core.App;
import com.nexters.pack.network.APIResponseHandler;
import com.nexters.pack.network.HttpUtil;
import com.nexters.pack.network.URL;
import com.nostra13.universalimageloader.core.ImageLoader;

public class SignupActivity extends BaseSherlockActivity implements View.OnClickListener{
	private EditText emailEt;
    private EditText passwordEt;
    private EditText passwordConfirmEt;
    private Button signInBtn;
    private Button signUpBtn;
    
    private UserProfile userProfile;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_signup);
		getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.ab_background_light));
		
		initResources();
        initEvents();
	}
	private void initResources() {
		emailEt = (EditText) findViewById(R.id.et_sign_in_email);
        passwordEt = (EditText) findViewById(R.id.et_sign_in_password);
        passwordConfirmEt = (EditText) findViewById(R.id.et_sign_in_password_confirm);
        signInBtn = (Button) findViewById(R.id.btn_sign_in);
        signUpBtn = (Button) findViewById(R.id.btn_sign_up);
        
        Intent intent = getIntent();
        try {
        	userProfile = UserProfile.loadFromCache();
		} catch (Exception e) {
			userProfile = intent.getExtras().getParcelable("kakao");
		}
        if(userProfile != null){
        	App.log("kakao id : " + userProfile.getId());
            App.log("kakao nick : " + userProfile.getNickname());
            App.log("kakaoProfileImg : " + ImageLoader.getInstance().getDiscCache().get(userProfile.getThumbnailImagePath()));
        }
        if(App.SERVER_TARGET == App.SERVER_TEST){
        	emailEt.setText("packtest1@gmail.com");
        	passwordEt.setText("12341234");
        	passwordConfirmEt.setText("12341234");
        }

    }

    private void initEvents() {
    	signInBtn.setOnClickListener(this);
    	signUpBtn.setOnClickListener(this);
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
		
	}
	
	private void signUp(){
		String url = URL.SIGN_UP;
		File imageFile = ImageLoader.getInstance().getDiscCache().get(userProfile.getProfileImagePath());

		//Map<String, String> headers = new HashMap<String, String>();
		//headers.put("Authorization", String.format("Token token=\"%s\"", user.authenticationToken));
		//headers.put("Content-Type","multipart/form-data;");
		
		RequestParams params = new RequestParams();
        params.put("email", getEmail());
        params.put("password", getPassword());
        params.put("confirmPassword", getPasswordConfirm());
       
        if( userProfile!= null ){
        	params.put("kakao[id]",userProfile.getId());
        	params.put("kakao[properties][nickname]", userProfile.getNickname());
            params.put("kakao[properties][thumbnail_image]", userProfile.getThumbnailImagePath());
            params.put("kakao[properties][profile_image]", userProfile.getProfileImagePath());
        }
        
        if(imageFile != null){
        	try {
                params.put("image", imageFile);
            } catch (FileNotFoundException e) {
                App.log("FileNotFoundException");
                e.printStackTrace();
                return;
            }
        }
        
        
        HttpUtil.post(url, null, params, new APIResponseHandler(SignupActivity.this) {

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
            	App.setToken(SignupActivity.this, response.optJSONObject("data").optString("token"));
                Intent intent = new Intent(SignupActivity.this, MainActivity.class);
                startActivity(intent);
                setResult(RESULT_OK, null);
                finish();
            }
        });
	}
	
	private String getPassword() {
        return passwordEt.getText().toString();
    }
	private String getPasswordConfirm() {
        return passwordConfirmEt.getText().toString();
    }
    private String getEmail() {
        return emailEt.getText().toString();
    }
}
