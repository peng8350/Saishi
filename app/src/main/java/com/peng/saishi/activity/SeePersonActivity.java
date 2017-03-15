package com.peng.saishi.activity;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.peng.saishi.R;
import com.peng.saishi.activity.base.BaseBackActivity;
import com.peng.saishi.entity.User;
import com.peng.saishi.entity.config.AppConfig;
import com.peng.saishi.interfaces.service.MyStringCallBack;
import com.peng.saishi.utils.ActChangeUtils;
import com.peng.saishi.utils.FrescoUtils;
import com.peng.saishi.utils.GlobeScopeUtils;
import com.peng.saishi.utils.JsonParser;
import com.peng.saishi.utils.ToastUtils;
import com.zhy.http.okhttp.OkHttpUtils;

public class SeePersonActivity extends BaseBackActivity {

	private TextView id, sex, name, place, phone, major, school, intro, age,
			xueli, hobby;
	private SimpleDraweeView head;
	private User user;
	private Button update_btn;

	private static final int UPDATE = 333;

	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		// TODO Auto-generated method stub
		if (arg0 == UPDATE && arg1 == RESULT_OK) {
			// 成功了要求更新
			Map<String, String> params = new HashMap<>();
			params.put("id", GlobeScopeUtils.getMeId() + "");
			OkHttpUtils.post().url(AppConfig.getUser).params(params).build()
					.execute(new MyStringCallBack() {

						@Override
						public void onResponse(String response, int id) {
							// TODO Auto-generated method stub
							if (id!=0) {
								ToastUtils.ShowNetError();
								return ;
							}
							try {
								User me = JsonParser
										.getBeanFromJson(new JSONObject(
												response), User.class);
								user = me;
								setDatainWidget(me);
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}

						@Override
						public void onError(Call call, Exception e, int id) {
							// TODO Auto-generated method stub
							ToastUtils.ShowNetError();
						}
					});
		}
	}

	// 直接给控件更新他们的所有信息
	public void setDatainWidget(User user) {
		Drawable drawable = getResources()
				.getDrawable(
						user.getSex().equals("女") ? R.drawable.female
								: R.drawable.male);
		drawable.setBounds(0, 0, drawable.getMinimumWidth(),
				drawable.getMinimumHeight());
		sex.setCompoundDrawables(drawable, null, null, null);
		sex.setText(user.getSex());
		name.setText(user.getUsername());
		id.setText("用户帐号\t\t\t" + user.getUser());
		place.setText("地区: \t\t\t\t\t" + user.getPlace());
		age.setText(user.getAge() + "岁");
		xueli.setText("学历: \t\t\t\t\t" + user.getXueli());
		phone.setText("联系方式: \t\t\t" + user.getPhone());
		hobby.setText("专长: \t\t\t\t\t" + user.getHobby());
		major.setText("在读专业: \t\t\t" + user.getMajor());
		school.setText("在读学校 \t\t\t" + user.getSchool());
		intro.setText(user.getIntro());
	}

	@SuppressLint("NewApi")
	@Override
	public void init() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_seeperson);
		super.init();
		id = (TextView) findViewById(R.id.Seeperson_id);
		boolean isme = getIntent().getBooleanExtra("isme", false);
		if (isme) {
			user = GlobeScopeUtils.Get("ME");
		} else {
			user = (User) getIntent().getSerializableExtra("user");
		}
		sex = (TextView) findViewById(R.id.sex);
		name = (TextView) findViewById(R.id.Seeperson_name);
		place = (TextView) findViewById(R.id.Seeperson_place);
		school = (TextView) findViewById(R.id.Seeperson_school);
		xueli = (TextView) findViewById(R.id.Seeperson_xueli);
		hobby = (TextView) findViewById(R.id.Seeperson_hobby);
		intro = (TextView) findViewById(R.id.Seeperson_intro);
		major = (TextView) findViewById(R.id.Seeperson_major);
		age = (TextView) findViewById(R.id.Seeperson_age);
		phone = (TextView) findViewById(R.id.Seeperson_Phone);
		head = (SimpleDraweeView) findViewById(R.id.Seeperson_head);
		update_btn = (Button) findViewById(R.id.Seeperson_update);
		update_btn.setOnClickListener(this);
		update_btn.setVisibility(isme ? View.VISIBLE : View.GONE);
		Drawable drawable = getResources()
				.getDrawable(
						user.getSex().equals("女") ? R.drawable.female
								: R.drawable.male);
		drawable.setBounds(0, 0, drawable.getMinimumWidth(),
				drawable.getMinimumHeight());
		sex.setCompoundDrawables(drawable, null, null, null);
		head.setOnClickListener(this);
		id.setText("用户帐号\t\t\t" + user.getUser());
		sex.setText(user.getSex());
		name.setText(user.getUsername());
		place.setText("地区: \t\t\t\t\t" + user.getPlace());
		age.setText(user.getAge() + "岁");
		xueli.setText("学历: \t\t\t\t\t" + user.getXueli());
		phone.setText("联系方式: \t\t\t" + user.getPhone());
		hobby.setText("专长: \t\t\t\t\t" + user.getHobby());
		major.setText("在读专业: \t\t\t" + user.getMajor());
		school.setText("在读学校 \t\t\t" + user.getSchool());
		intro.setText(user.getIntro());
		FrescoUtils.Display(head, AppConfig.Userpic_path + user.getId(), 150,
				150,true);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
		switch (v.getId()) {
		case R.id.Seeperson_update:
			// 点击进入修改资料页面
			ActChangeUtils.ChangeActivityForResult(this,
					PersonDataActivity.class, UPDATE);
			break;
		case R.id.Seeperson_head:
			ActChangeUtils.ChangeActivity(this, PhotoActivity.class,
					new String[] { "type", "id" }, GlobeScopeUtils.getMeId()
							+ "", 3);
			break;
		default:
			break;
		}
	}

}
