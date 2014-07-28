package com.nexters.pack.activity;

import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.loopj.android.http.RequestParams;
import com.nexters.pack.R;
import com.nexters.pack.network.APIResponseHandler;
import com.nexters.pack.network.HttpUtil;
import com.nexters.pack.network.URL;

public class SignupActivity extends BaseSherlockActivity implements View.OnClickListener{
	private EditText emailEt;
    private EditText passwordEt;
    private EditText passwordConfirmEt;
    private Button signInBtn;
    private Button signUpBtn;
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
		
		RequestParams params = new RequestParams();
        params.put("email", getEmail());
        params.put("password", getPassword());
        params.put("confirmPassword", getPasswordConfirm());
        
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
            	Intent intent = new Intent(SignupActivity.this, StationActivity.class);
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
