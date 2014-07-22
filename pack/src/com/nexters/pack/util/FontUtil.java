package com.nexters.pack.util;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.graphics.Typeface;

public class FontUtil {
	
	public static Map<String, Typeface> fonts = new HashMap<String, Typeface>();
	
	public static Typeface getTypeface(Context context, String fontName) {
		Typeface typeface = fonts.get(fontName);
		if(typeface == null) {
			String fontPath = "fonts/" + fontName;
			typeface = Typeface.createFromAsset(context.getAssets(), fontPath);
			fonts.put(fontName, typeface);
		}
		
		return typeface;
	}
}
