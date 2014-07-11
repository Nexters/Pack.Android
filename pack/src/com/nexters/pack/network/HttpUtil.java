package com.nexters.pack.network;

import java.util.Map;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nexters.pack.core.App;

public class HttpUtil {
	
	private static AsyncHttpClient client;
	
	private static AsyncHttpClient getClient() {
		if(client == null) {
			client = new AsyncHttpClient();
		}
		
		return client;
	}
	
	public static void get(String url, Map<String, String> headers, RequestParams params, AsyncHttpResponseHandler handler) {
		setHeaders(headers);
		App.log("GET : " + url);
		getClient().get(url, params, handler);
	}

	public static void post(String url, Map<String, String> headers, RequestParams params, AsyncHttpResponseHandler handler) {
		setHeaders(headers);
		App.log("POST : " + url + "?" + params);
		getClient().post(url, params, handler);
	}
	
	public static void put(String url, Map<String, String> headers, RequestParams params, AsyncHttpResponseHandler handler) {
		setHeaders(headers);
		getClient().put(url, params, handler);
	}
	
	public static void delete(String url, Map<String, String> headers, RequestParams params, AsyncHttpResponseHandler handler) {
		setHeaders(headers);
		getClient().delete(null, url, null, params, handler);
	}
	
	public static void setHeaders(Map<String, String> headers) {
		if(headers == null) {
			return;
		}
		
		for(Map.Entry<String, String> entry : headers.entrySet()) {
			getClient().addHeader(entry.getKey(), entry.getValue());
		}
	}
}
