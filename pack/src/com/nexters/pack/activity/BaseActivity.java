package com.nexters.pack.activity;

import com.nexters.pack.R;
import com.nexters.pack.view.FontTextView;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class BaseActivity extends Activity {
	private View actionbarView;
	private FontTextView actionbarTitleText;
	private View leftBt, rightBt;
	private ImageView leftImageBt, rightImageBt;
	private TextView leftTextBt, rightTextBt;
	
	private View mLoadingView;
    private int mLoadingStackCount = 0;
    private AlertDialog mAlertDialog;

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		if(!(this instanceof SplashActivity)) {
			actionbarView = LayoutInflater.from(this).inflate(R.layout.actionbar, null);
			ActionBar actionbar = getActionBar();
			actionbar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
			actionbar.setCustomView(actionbarView, new ActionBar.LayoutParams(
					ActionBar.LayoutParams.MATCH_PARENT,
					ActionBar.LayoutParams.MATCH_PARENT));

			actionbarTitleText = (FontTextView) actionbarView.findViewById(R.id.title);
			leftBt = actionbarView.findViewById(R.id.bt_left);
			rightBt = actionbarView.findViewById(R.id.bt_right);
			leftImageBt = (ImageView) actionbarView.findViewById(R.id.bt_left_img);
			rightImageBt = (ImageView) actionbarView.findViewById(R.id.bt_right_img);
			leftTextBt = (TextView) actionbarView.findViewById(R.id.bt_left_text);
			rightTextBt = (TextView) actionbarView.findViewById(R.id.bt_right_text);
			onInitActionBar();
		}
		
		mLoadingView = LayoutInflater.from(this).inflate(R.layout.view_loading, null);
		mLoadingView.setVisibility(View.INVISIBLE);
	}

    @Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		((ViewGroup) getWindow().getDecorView().findViewById(android.R.id.content)).addView(mLoadingView);
	}
	
	protected void onInitActionBar() {
		setActionBarTitle(getTitle());
	}
	
	public void setActionBarTitle(int resId) {
		setActionBarTitle(getString(resId));
	}

	public void setActionBarTitle(CharSequence title) {
		actionbarTitleText.setText(title);
	}

	public FontTextView getActionBarTitle() {
		return actionbarTitleText;
	}
	
	public synchronized void showLoading() {
		mLoadingStackCount++;
		mLoadingView.setVisibility(View.VISIBLE);
	}

	public synchronized void hideLoading() {
		mLoadingStackCount--;
		if(mLoadingStackCount <= 0) {
			mLoadingStackCount = 0;
			mLoadingView.setVisibility(View.INVISIBLE);
		}
	}

    public void showShortToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

	public void showAlert(int resId) {
		showAlert(getString(resId));
	}

	public void showAlert(String message) {
    	if (mAlertDialog != null && mAlertDialog.isShowing())
            return;
    	
    	AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message);
        builder.setPositiveButton(R.string.ok, null);
        
        mAlertDialog = builder.create();
        mAlertDialog.show();
        
        TextView messageText = (TextView) mAlertDialog.findViewById(android.R.id.message);
        messageText.setGravity(Gravity.CENTER);
    }
}
