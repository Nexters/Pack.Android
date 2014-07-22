package com.nexters.pack.activity;

import com.nexters.pack.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;

public class SplashActivity extends BaseActivity {
	
	private static int INTRO_LOADING_TIME = 2000;
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
		Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.splash, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		
		return super.onOptionsItemSelected(item);
	}
}
