package com.nexters.pack.network;

import com.nexters.pack.core.App;

public class URL {
	public static final String BASE_URL_DEVELOPMENT = "http://192.168.3.7:3001"; // rangken com2us imac
	//public static final String BASE_URL_DEVELOPMENT = "http://54.250.170.196:3001";
	public static final String BASE_URL_PRODUCTION = "http://54.250.170.196:3001";
	public static final String getBaseUrl() {
		if(App.SERVER_TARGET == App.SERVER_TEST) {
			return BASE_URL_DEVELOPMENT;
		} else {
			return BASE_URL_PRODUCTION;
		}
	}
	public static final String PING = getBaseUrl() + "/ping";
	public static final String SIGN_IN = getBaseUrl() + "/login";
	public static final String SIGN_UP = getBaseUrl() + "/register";
	public static final String CHECK = getBaseUrl() + "/check";
	public static final String GUESTHOUSE_NEAR = getBaseUrl() + "/guesthouses/near";
}
