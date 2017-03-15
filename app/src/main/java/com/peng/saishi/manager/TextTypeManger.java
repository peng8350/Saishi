package com.peng.saishi.manager;

import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.widget.TextView;

public class TextTypeManger {


	/**
	 * 通过调用这个方法,可直接设置textview的字体类型
	 */
	public static  void setTextViewType(TextView tv){
		AssetManager assetmanager = App.getInstance().getAssets();
		Typeface typeface = Typeface.createFromAsset(assetmanager, "ww.ttf");
		tv.setTypeface(typeface);
	}
	
}
