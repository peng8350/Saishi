package com.peng.saishi.activity;

import java.util.List;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.widget.ListView;

import com.peng.saishi.R;
import com.peng.saishi.activity.base.BaseBackActivity;
import com.peng.saishi.adapter.UserManagerAdapter;
import com.peng.saishi.entity.User;
import com.peng.saishi.manager.AppManager;
import com.peng.saishi.utils.ActChangeUtils;
import com.peng.saishi.utils.PreferencesTool;
import com.peng.saishi.widget.CustomDialog;

public class UserManagerActivity extends BaseBackActivity {

	private ListView listview;
	private List<User> users;
	@Override
	public void init() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_usermanager);
		super.init();
		listview = (ListView) findViewById(R.id.usermanager_listview);
		findViewById(R.id.usermanager_Exit_btn).setOnClickListener(this);
		users = User.listAll(User.class);
		listview.setAdapter(new UserManagerAdapter(this, users));
	}

	public void onClick(android.view.View v) {

		super.onClick(v);
		switch (v.getId()) {
		case R.id.usermanager_Exit_btn:
			CustomDialog.Builder builder = new CustomDialog.Builder(this);
			builder.setTitle("切换账号");
			builder.setMessage("你确定要退出当前账号,切换到登陆界面吗?");
			builder.setPositiveButton("是的", new OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					PreferencesTool.setParam("Login", "auto", false);
					AppManager.FinishAllAct();
					
					ActChangeUtils.ChangeActivity(UserManagerActivity.this, LoginActivity.class);

				}
			});
			builder.setNegativeButton("不是", new OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					dialog.dismiss();
				}
			});
			builder.create().show();
			break;

		default:
			break;
		}

	};


}
