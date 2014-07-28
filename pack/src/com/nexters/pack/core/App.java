package com.nexters.pack.core;

import java.io.File;

import android.app.Application;
import android.util.Log;

import com.google.analytics.tracking.android.GoogleAnalytics;
import com.google.analytics.tracking.android.MapBuilder;
import com.google.analytics.tracking.android.Tracker;
import com.nostra13.universalimageloader.cache.disc.impl.TotalSizeLimitedDiscCache;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.utils.StorageUtils;

public class App extends Application {
	private static GoogleAnalytics mGa;
	private static Tracker mTracker;
  	private static final String GA_PROPERTY_ID = "UA-47309628-4";
	  
	public static final int SERVER_TEST = 0;
	public static final int SERVER_PRODUCTION = 1;
	public static final int SERVER_TARGET = SERVER_TEST;

    public static final String TAG = "PACK";


    public static void log(String msg) {
		log(TAG, msg);
	}
	
	public static void log(String tag, String msg) {
		Log.d(tag, msg);
	}

	@Override
	public void onCreate() {
		super.onCreate();
		mGa = GoogleAnalytics.getInstance(this);
	    mTracker = mGa.getTracker(GA_PROPERTY_ID);
	    
        DisplayImageOptions options = new DisplayImageOptions.Builder()
            .cacheInMemory(true)
            .cacheOnDisc(true)
            .build();
		File cacheDir = StorageUtils.getCacheDirectory(this);
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
            .memoryCache(new LruMemoryCache(8 * 1024 * 1024))
            .discCache(new TotalSizeLimitedDiscCache(cacheDir, 20 * 1024 * 1024))
            .defaultDisplayImageOptions(options)
            .build();
        ImageLoader.getInstance().init(config);
	}
	public static Tracker getGaTracker() {
		return mTracker;
	}

	public static GoogleAnalytics getGaInstance() {
		return mGa;
	}
	
	public static void SendTrackingEvent(String category,String action,String label,long value){
		mTracker.send(MapBuilder.createEvent(category,action,label, null).build());
	}
}
