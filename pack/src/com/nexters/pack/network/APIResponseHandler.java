package com.nexters.pack.network;

import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.Context;
import android.text.TextUtils;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.nexters.pack.R;
import com.nexters.pack.core.App;


public class APIResponseHandler extends JsonHttpResponseHandler {
	private AlertDialog alertDialog;
	public static final String CODE_SUCCESS = "1";
	
	private Context context;
	public APIResponseHandler(Context context) {
		this.context = context;
	}
	@Override
	public void onStart() {
		
	}

	@Override
	public void onFinish() {}

	@Override
	public void onSuccess(JSONObject response) {}

	@Override
	public final void onSuccess(int statusCode, JSONObject response) {
		App.log("HTTP  : " + response.toString());
		String code = response.optString("result");
		String message = response.optString("msg");
		
		if(!TextUtils.isEmpty(code) && !CODE_SUCCESS.equals(code)) {	    	
	    	AlertDialog.Builder builder = new AlertDialog.Builder(context);
	        builder.setMessage(message + "(" + code + ")");
	        builder.setPositiveButton(R.string.ok, null);
	        
	        alertDialog = builder.create();
	        alertDialog.show();
		} else {
			onSuccess(response);
			onCompletelyFinish();
		}
	}

	@Override
	public void onFailure(Throwable error, String response) {
		// ������ 404 500 ��������� ��������� ������
		StringBuilder errorMessageBuilder = new StringBuilder();
		if(!TextUtils.isEmpty(response)) {
			errorMessageBuilder.append(response).append("\n");
		}
		if(error != null) {
			errorMessageBuilder.append(error.getMessage());
		}
		
		if( alertDialog != null && alertDialog.isShowing() ) return;
    	
    	AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(R.string.error_cannot_connect_network);
        builder.setPositiveButton(R.string.ok, null);
        
        alertDialog = builder.create();
        alertDialog.show();
		
		onCompletelyFinish();
		App.log(errorMessageBuilder.toString().trim());
	}
	@Override
	public void onFailure ( Throwable e, JSONObject errorResponse ) {
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
	
	public void onCompletelyFinish() {}
}
