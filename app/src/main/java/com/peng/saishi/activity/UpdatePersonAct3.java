package com.peng.saishi.activity;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.peng.saishi.R;
import com.peng.saishi.activity.base.BaseBackActivity;
import com.peng.saishi.utils.ToastUtils;

public class UpdatePersonAct3 extends BaseBackActivity {

	private TextView update_et;
	int type = 0;

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub

		super.onDestroy();
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_updateperson3);
		super.init();
		String update_str = getIntent().getStringExtra("data2");
		type = Integer.parseInt(getIntent().getStringExtra("type"));
		findViewById(R.id.updateperson3_button1).setOnClickListener(this);
		update_et = (TextView) findViewById(R.id.updateperson3_editText1);
		update_et.setText(update_str);
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
		super.onClick(v);
		switch (v.getId()) {
		case R.id.updateperson3_button1:
			if (update_et.getText().toString().length() < 9) {
				ToastUtils.ShowShortToast("不允许少于9个字");
				return;
			}
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
