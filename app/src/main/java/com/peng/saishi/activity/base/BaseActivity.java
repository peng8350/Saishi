package com.peng.saishi.activity.base;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View.OnClickListener;
import com.peng.saishi.manager.AppManager;
/**
 * 
 * @author peng
 *
 *１.点击事件的监听
 *2.初始化方法，方便使用
 *3.点击后退事件处理
 */
public abstract class BaseActivity extends AppCompatActivity implements OnClickListener {


	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		super.onCreate(savedInstanceState);
		AppManager.AddActivity(this);
		init();
	}

	public abstract void init();

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		AppManager.RemoveAct(this);
	}

}
