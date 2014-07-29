package com.nexters.pack.core;

import android.content.Context;
import android.content.Intent;

import com.google.android.gcm.GCMBaseIntentService;
import com.nexters.pack.R;

public class GCMIntentService extends GCMBaseIntentService{
	/**
     * GCM Server로부터 발급받은 Project ID를 통해 SuperClass인
     * GCMBaseIntentService를 생성해야한다. 
     */
    public GCMIntentService() {
    	super(App.PROJECT_ID);
    }
 
    @Override
    protected void onError(Context arg0, String arg1) {
        // TODO Auto-generated method stub
        /**
         * GCM 오류 발생 시 처리해야 할 코드를 작성한다.
         * ErrorCode에 대해선 GCM 홈페이지와 GCMConstants 내 static variable 참조한다. 
         */
    	App.log("onMessage : " + arg1);
    }
 
    @Override
    protected void onMessage(Context arg0, Intent arg1) {
        // TODO Auto-generated method stub
        /**
         * GCMServer가 전송하는 메시지가 정상 처리 된 경우 구현하는 메소드이다.
         * Notification, 앱 실행 등등 개발자가 하고 싶은 로직을 해당 메소드에서 구현한다.
         * 전달받은 메시지는 Intent.getExtras().getString(key)를 통해 가져올 수 있다.
         */
    	App.log("onMessage");
    }
 
    @Override
    protected void onRegistered(Context arg0, String regId) {
        // TODO Auto-generated method stub
        /**
         * GCMRegistrar.getRegistrationId(context)가 실행되어 registrationId를 발급받은 경우 해당 메소드가 콜백된다.
         * 메시지 발송을 위해 regId를 서버로 전송하도록 하자.
         */
    	App.log("onRegistered : " + regId);
    }
 
    @Override
    protected void onUnregistered(Context arg0, String arg1) {
        // TODO Auto-generated method stub
        /**
         * GCMRegistrar.unregister(context) 호출로 해당 디바이스의 registrationId를 해지요청한 경우 해당 메소드가 콜백된다.
         */
    	App.log("onUnregistered : " + arg1);
    }
}
