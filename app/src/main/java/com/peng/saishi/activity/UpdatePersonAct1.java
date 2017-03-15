package com.peng.saishi.activity;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.peng.saishi.R;
import com.peng.saishi.activity.base.BaseBackActivity;
import com.peng.saishi.adapter.MyWheelAdapter;
import com.peng.saishi.widget.WheelView.OnWheelChangedListener;
import com.peng.saishi.widget.WheelView.WheelView;

public class UpdatePersonAct1 extends BaseBackActivity implements OnWheelChangedListener {

	private TextView update_et;
	private TextView title;
	private WheelView wheel;
	private String[] sexes = { "男", "女" };
	private String[] xuelis = { "高中以下", "专科", "本科", "硕士", "博士级" };
	private String[] ages = new String[22];
	int type=0;
	private String[] realdatas;

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub

		super.onDestroy();
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_updateperson1);
		super.init();
		String title_str = getIntent().getStringExtra("data1");
		String update_str = getIntent().getStringExtra("data2");
		type = Integer.parseInt(getIntent().getStringExtra("type"));
		for (int i = 18; i < 40; i++) {

			ages[i - 18] = String.valueOf(i)+"岁";
		}
		if (type == 1) {
			realdatas = ages;
		} else if (type == 2) {
			realdatas = sexes;
		} else {
			realdatas = xuelis;
		}
		update_et = (TextView) findViewById(R.id.updateperson1_editText1);
		title = (TextView) findViewById(R.id.updateperson1_textView1);
		wheel = (WheelView) findViewById(R.id.updateperson1_wheelView1);
		title.setText(title_str);
		update_et.setText(update_str);
		wheel.setViewAdapter(new MyWheelAdapter(this, realdatas));
		wheel.addChangingListener(this);
	}

	@Override
	public void onChanged(WheelView wheel, int oldValue, int newValue) {
		// TODO Auto-generated method stub
		update_et.setText(realdatas[newValue]);
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		Intent intent = new Intent();
		intent.putExtra("result", update_et.getText().toString());
		intent.putExtra("type", type);
		setResult(RESULT_OK, intent);
		finish();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.back_tv:
			Intent intent = new Intent();
			intent.putExtra("result", update_et.getText().toString());
			intent.putExtra("type", type);
			setResult(RESULT_OK, intent);
			finish();
			break;

		default:
			break;
		}
	}
}
