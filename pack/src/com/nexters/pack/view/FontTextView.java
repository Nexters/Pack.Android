package com.nexters.pack.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.TextView;

import com.nexters.pack.R;
import com.nexters.pack.util.FontUtil;


public class FontTextView extends TextView {

	private String fontName;

	public FontTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context, attrs);
	}

	public FontTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context, attrs);
	}

	private void init(Context context, AttributeSet attrs) {
		TypedArray a = context.getTheme().obtainStyledAttributes(
				attrs,
				R.styleable.Text,
				0, 0);

		try {
			fontName = a.getString(R.styleable.Text_font_name);
		} finally {
			a.recycle();
		}

		if(!isInEditMode() && !TextUtils.isEmpty(fontName)) {
			Typeface font = FontUtil.getTypeface(getContext(), fontName);
			setTypeface(font, Typeface.NORMAL);
		}
	}
}
