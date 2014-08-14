package com.nexters.pack.model;

import com.kakao.UserProfile;
import com.nexters.pack.network.URL;

public class User {
	private static User user = new User();
	private String profileURL = "";
	private String userName = "";
	
	private User() {}

    public static User getInstance() {
        return user;
    }
    

    public String getProfileURL(){
    	return URL.getBaseUrl()+"/"+this.profileURL;
    }

	public void setProfileURL(String profileURL) {
		this.profileURL = profileURL;
	}
}
