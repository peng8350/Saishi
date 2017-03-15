package com.peng.saishi.activity;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.peng.saishi.R;
import com.peng.saishi.entity.MyFIleInfo;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.TextView;

public class FileMoreAct extends Activity {
	@ViewInject(R.id.Filemore_name)
	private TextView tv_name;
	@ViewInject(R.id.Filemore_mimetype)
	private TextView tv_mimetype;
	@ViewInject(R.id.Filemore_time)
	private TextView tv_time;
	@ViewInject(R.id.Filemore_size)
	private TextView tv_size;
	@ViewInject(R.id.Filemore_type)
	private TextView tv_type;
	@ViewInject(R.id.Filemore_end)
	private TextView tv_end;
	@ViewInject(R.id.Filemore_parent)
	private TextView tv_parent;
	@ViewInject(R.id.Filemore_title)
	private TextView tv_title;

	private MyFIleInfo info;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_filemore);
		ViewUtils.inject(this);
		int resid = getIntent().getIntExtra("resid", 0);
		info = (MyFIleInfo) getIntent().getSerializableExtra("info");

		InitData(resid);
	}

	// 初始化数据到控件里面
	private void InitData(int resid) {
		// TODO Auto-generated method stub
		tv_time.setText(info.getCreate_time());
		tv_end.setText(info.getEnd());
		tv_type.setText(info.getType());
		tv_mimetype.setText(info.getMimetype());
		tv_size.setText(info.getSize());
		tv_parent.setText(info.getParent());
		tv_name.setText(info.getName());
		tv_time.setText(info.getName());
		Drawable drawable = getResources().getDrawable(resid);
		tv_title.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
	}

}
