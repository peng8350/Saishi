package com.peng.saishi.activity;

import com.peng.saishi.R;
import com.peng.saishi.activity.base.BaseBackActivity;
import com.peng.saishi.widget.ProgressWebView;

import android.view.KeyEvent;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebActivity extends BaseBackActivity {

	private ProgressWebView mWebView;
	private WebSettings mWebSettings;

	@Override
	public void init() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_web);
		super.init();
		mWebView = (ProgressWebView) findViewById(R.id.webView1);
		mWebView.loadUrl(getIntent().getStringExtra("url"));

		mWebSettings = mWebView.getSettings();
		mWebSettings.setJavaScriptEnabled(true); // 允许加载javascript
		mWebSettings.setSupportZoom(true); // 允许缩放
		mWebSettings.setBuiltInZoomControls(true); // 原网页基础上缩放
		mWebSettings.setUseWideViewPort(true); // 任意比例缩放

		/*****************************************************************
		 * 在点击请求的是链接时才会调用，重写此方法返回true表明点击网页里 面的链接还是在当前的WebView里跳转，不会跳到浏览器上运行。
		 *****************************************************************/
		mWebView.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return super.shouldOverrideUrlLoading(view, url);
			}
		});
	}

	/***********************************************************
	 * 监听返回键 注：这里的返回键是指在加载的网页中进入了另一个连接，点击返回
	 ***********************************************************/
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) {
			mWebView.goBack();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}
