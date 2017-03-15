package com.peng.saishi.activity;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

import org.json.JSONException;
import org.json.JSONObject;

import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.EditText;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.api.BasicCallback;

import com.peng.saishi.R;
import com.peng.saishi.activity.base.BaseBackActivity;
import com.peng.saishi.entity.User;
import com.peng.saishi.entity.config.AppConfig;
import com.peng.saishi.interfaces.service.MyStringCallBack;
import com.peng.saishi.utils.GlobeScopeUtils;
import com.peng.saishi.utils.ToastUtils;
import com.zhy.http.okhttp.OkHttpUtils;

public class UpdatePswdActivity extends BaseBackActivity implements
		OnKeyListener {

	private EditText et_old, et_new, et_sure;

	@Override
	public void init() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_updatepswd);
		findViewById(R.id.updatepswd_button1).setOnClickListener(this);
		super.init();
		et_old = (EditText) findViewById(R.id.updatepswd_editText1);
		et_new = (EditText) findViewById(R.id.updatepswd_editText2);
		et_sure = (EditText) findViewById(R.id.updatepswd_editText3);
		et_new.setOnKeyListener(this);
		et_old.setOnKeyListener(this);
		et_sure.setOnKeyListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
		switch (v.getId()) {
		case R.id.updatepswd_button1:
			// 提交
			final String old = et_old.getText().toString();
			final String new_str = et_new.getText().toString();
			String sure_str = et_sure.getText().toString();
			if (old.equals("") || new_str.equals("") || sure_str.equals("")) {
				ToastUtils.ShowShortToast("请填写,不能留空!");
				return;
			}
			if (!new_str.equals(sure_str)) {
				// 不相等
				ToastUtils.ShowShortToast("两个新密码不相同");
				return;
			}
			if (new_str.equals(old)) {
				// 小雨8位
				ToastUtils.ShowShortToast("新密码和旧密码相同");
				return;
			}
			if (new_str.length() < 8) {
				// 小雨8位
				ToastUtils.ShowShortToast("密码要大于等于8位");
				return;
			}
			JMessageClient.updateUserPassword(old, new_str,
					new BasicCallback() {

						@Override
						public void gotResult(int arg0, String arg1) {
							// TODO Auto-generated method stub
							if (arg0 == 0) {
								Map<String, String> params = new HashMap<>();
								params.put("old", old);
								params.put("new", new_str);
								params.put("user", ((User) GlobeScopeUtils
										.Get("ME")).getUser());
								OkHttpUtils.post().url(AppConfig.UpdatePswd)
										.params(params).build()
										.execute(new MyStringCallBack() {

											@Override
											public void onResponse(
													String response, int id) {

												// TODO Auto-generated method
												// stub
												if (id!=0) {
													ToastUtils.ShowNetError();
													return ;
												}
												try {
													JSONObject res_obj = new JSONObject(
															response);
													if (res_obj.get("result")
															.equals("ok")) {

														finish();
														ToastUtils
																.ShowShortToast("修改密码成功!");
													} else {
														ToastUtils
																.ShowShortToast("修改密码失败!请检查旧密码是否有误");
													}
												} catch (JSONException e) {
													// TODO Auto-generated catch
													// block
													e.printStackTrace();
												}

											}

											@Override
											public void onError(Call call,
													Exception e, int id) {
												// TODO Auto-generated method
												// stub
												ToastUtils.ShowNetError();
											}
										});
							} else {
								ToastUtils
										.ShowShortToast(arg1 + ",错误码:" + arg0);
							}

						}
					});

			break;

		default:
			break;
		}
	}

	@Override
	public boolean onKey(View v, int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_ENTER) {
			return true;
		}
		return false;
	}

}
