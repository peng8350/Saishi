package com.peng.saishi.activity;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.Button;
import android.widget.EditText;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.api.BasicCallback;

import com.peng.saishi.R;
import com.peng.saishi.activity.base.BaseBackActivity;
import com.peng.saishi.entity.config.AppConfig;
import com.peng.saishi.interfaces.service.MyStringCallBack;
import com.peng.saishi.utils.CheckUtils;
import com.peng.saishi.utils.DialogUtils;
import com.peng.saishi.utils.ToastUtils;
import com.zhy.http.okhttp.OkHttpUtils;

public class ForgetActivity2 extends BaseBackActivity implements OnKeyListener,
		TextWatcher {

	private Button finish_btn;
	private EditText input_et;
	private boolean enable;
	String user;

	@Override
	public void init() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_forget2);
		super.init();
		user = getIntent().getStringExtra("user");
		input_et = (EditText) findViewById(R.id.Forget2_editText1);
		finish_btn = (Button) findViewById(R.id.Forget2_button1);
		finish_btn.setOnClickListener(this);
		input_et.addTextChangedListener(this);
	}

	public boolean isEnable() {
		return enable;
	}

	public void setEnable(boolean enable) {
		this.enable = enable;
		finish_btn.setVisibility(enable ? View.VISIBLE : View.GONE);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
		switch (v.getId()) {
		case R.id.Forget2_button1:
			// 点击修改密码
			// 立即修改
			if (CheckUtils.isConSpeCharacters(input_et.getText().toString())) {
				ToastUtils.ShowShortToast("包含不法字符串");
				return;
			}
			Map<String, String> params = new HashMap<>();
			params.put("id", user);
			final Dialog wait_dia = DialogUtils.createLoadingDialog(this,
					"修改密码中..");
			wait_dia.show();
			OkHttpUtils.post().params(params).url(AppConfig.getUserPswd)
					.build().execute(new MyStringCallBack() {

						@Override
						public void onResponse(String response, int id) {
							// TODO Auto-generated method stub

							// TODO Auto-generated method stub
							if (id!=0) {
								wait_dia.dismiss();
								ToastUtils.ShowNetError();
								return ;
							}
							// TODO Auto-generated method stub
							final String old_pswd = response;
							JMessageClient.login(user, old_pswd,
									new BasicCallback() {

										@Override
										public void gotResult(int arg0,
												String arg1) {
											// TODO Auto-generated method stub
											if (arg0 == 0) {

												JMessageClient
														.updateUserPassword(
																old_pswd,
																input_et.getText()
																		.toString(),
																new BasicCallback() {

																	@Override
																	public void gotResult(
																			int arg0,
																			String arg1) {
																		// TODO
																		// Auto-generated
																		// method
																		// stub

																		if (arg0 == 0) {

																			Map<String, String> params = new HashMap<>();
																			params.put(
																					"user",
																					user);
																			params.put(
																					"pswd",
																					input_et.getText()
																							.toString());
																			OkHttpUtils
																					.post()
																					.params(params)
																					.url(AppConfig.forgetpswd)
																					.build()
																					.execute(
																							new MyStringCallBack() {
																								public void onError(
																										Call call,
																										Exception e,
																										int id) {

																									// TODO
																									// Auto-generated
																									// method
																									// stub

																									// TODO
																									// Auto-generated
																									// method
																									// stub
																									ToastUtils
																											.ShowNetError();
																									wait_dia.dismiss();

																								};

																								public void onResponse(
																										String response,
																										int id) {
																									if (id!=0) {
																										wait_dia.dismiss();
																										ToastUtils.ShowNetError();
																										return ;
																									}
																									// TODO
																									// Auto-generated
																									// method
																									// stub

																									// TODO
																									// Auto-generated
																									// method
																									// stub
																									try {
																										wait_dia.dismiss();
																										JSONObject res_obj = new JSONObject(
																												response);
																										if (res_obj
																												.getString(
																														"result")
																												.equals("ok")) {
																											ToastUtils
																													.ShowLongToast("修改密码成功!");
																											finish();
																										} else {
																											ToastUtils
																													.ShowShortToast("不明原因");
																										}
																									} catch (JSONException e) {
																										// TODO
																										// Auto-generated
																										// catch
																										// block
																										e.printStackTrace();
																									}

																								};
																							});
																		} else {
																			wait_dia.dismiss();
																			ToastUtils
																					.ShowShortToast(arg1
																							+ ",错误码"
																							+ arg0);
																		}
																	}
																});
											} else {
												ToastUtils.ShowLongToast(arg1);
												wait_dia.dismiss();
											}
										}
									});

						}

						@Override
						public void onError(Call call, Exception e, int id) {
							// TODO Auto-generated method stub
							ToastUtils.ShowNetError();
							wait_dia.dismiss();
						}
					});

			break;

		default:
			break;
		}
	}

	// 改变之后
	@Override
	public void afterTextChanged(Editable s) {
		// TODO Auto-generated method stub
		String content = s.toString();
		setEnable(content.length() > 7);
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		// TODO Auto-generated method stub

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
