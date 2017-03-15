package com.peng.saishi.activity.base;

import com.peng.saishi.manager.AppManager;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View.OnClickListener;
/**
 * 
 * @author peng
 *
 *１.点击事件的监听
 *2.初始化方法，方便使用
 *3.点击后退事件处理
 */
@SuppressLint("NewApi")
public abstract class BaseMainActivity extends AppCompatActivity implements OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		AppManager.AddActivity(this);
		init();
	}

	public abstract void init();
	
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode==KeyEvent.KEYCODE_BACK) {
			return super.moveTaskToBack(true);
		}
		return super.onKeyDown(keyCode, event);
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		AppManager.RemoveAct(this);
	}
	
}
