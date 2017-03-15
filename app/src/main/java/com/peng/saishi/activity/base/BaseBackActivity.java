package com.peng.saishi.activity.base;

import android.view.View;
import android.widget.TextView;

import com.peng.saishi.R;

public class BaseBackActivity extends BaseFragActivity{

	private TextView back;
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.back_tv:
//			
			finish();
			break;

		default:
			break;
		}
	}

	@Override
	public  void init() {
		// TODO Auto-generated method stub
		back = (TextView) findViewById(R.id.back_tv);
		back.setOnClickListener(this);
		System.out.println("初始化");
	}
	
	

}
