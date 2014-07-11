package com.nexters.pack.activity;

import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.loopj.android.http.RequestParams;
import com.nexters.pack.R;
import com.nexters.pack.core.App;
import com.nexters.pack.network.APIResponseHandler;
import com.nexters.pack.network.HttpUtil;
import com.nexters.pack.network.URL;

public class LoginActivity extends BaseActivity implements View.OnClickListener {
	private EditText mEtEmail;
    private EditText mEtPassword;
    private Button mBtnSignIn;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_login);
		
		initResources();
        initEvents();
	}
	private void initResources() {
        mEtEmail = (EditText) findViewById(R.id.et_sign_in_email);
        mEtPassword = (EditText) findViewById(R.id.et_sign_in_password);
        mBtnSignIn = (Button) findViewById(R.id.btn_sign_in);
    }

    private void initEvents() {
        mBtnSignIn.setOnClickListener(this);
    }
    
	public void onClick(View v) {
		login();
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
                //AccountManager.getInstance().signIn(SignInActivity.this, User.build(response));
            	App.log(response.toString());
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                setResult(RESULT_OK, null);
                finish();
            }
        });
	}
	
	private String getPassword() {
        return mEtPassword.getText().toString();
    }

    private String getEmail() {
        return mEtEmail.getText().toString();
    }
	
}
