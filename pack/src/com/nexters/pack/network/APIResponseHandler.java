package com.nexters.pack.network;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.Context;
import android.text.TextUtils;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.nexters.pack.R;
import com.nexters.pack.activity.BaseActivity;
import com.nexters.pack.activity.BaseSherlockFragmentActivity;
import com.nexters.pack.core.App;


public class APIResponseHandler extends JsonHttpResponseHandler {
	private AlertDialog alertDialog;
	public static final String CODE_SUCCESS = "0";
	
	private Context context;
	public APIResponseHandler(Context context) {
		this.context = context;
	}
	@Override
	public void onStart() {
	}

	@Override
	public void onFinish() {
	}

	
	@Override
    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
		App.log("HTTP  : " + response.toString());
		String code = response.optString("status");
		String message = response.optString("msg");
		
		if(!TextUtils.isEmpty(code) && !CODE_SUCCESS.equals(code)) {	    	
	    	AlertDialog.Builder builder = new AlertDialog.Builder(context);
	        builder.setMessage(message + "(" + code + ")");
	        builder.setPositiveButton(R.string.ok, null);
	        
	        alertDialog = builder.create();
	        alertDialog.show();
		} else {
			onCompletelyFinish();
		}
    }
	
	@Override
	public void onFailure(int statusCode, Header[] headers, Throwable e,
			JSONObject errorResponse) {
		App.log("e : " + e);
		if( alertDialog != null && alertDialog.isShowing() ) return;
    	
    	AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(R.string.error_cannot_connect_network);
        builder.setPositiveButton(R.string.ok, null);
        
        alertDialog = builder.create();
        alertDialog.show();
		onCompletelyFinish();
		
		if(errorResponse != null)
			App.log(errorResponse.optString("msg"));
	}
	
	
	
	public void onCompletelyFinish() {
		App.log("onCompletelyFinish");
	}
}
