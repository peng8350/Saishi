package com.peng.saishi.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import com.peng.saishi.R;
import com.peng.saishi.activity.base.BaseBackActivity;
import com.peng.saishi.entity.config.AppConfig;
import com.peng.saishi.utils.CheckUtils;
import com.peng.saishi.utils.ToastUtils;

/**
 * 注册的activity
 * 
 * @author peng 有一个注册的方法 成员变量 四个edittext
 * 
 * 
 * 
 */
public class RegActivity extends BaseBackActivity implements OnKeyListener {

	private static final int CODE_ING = 1; // 已发送，倒计时
	private static final int CODE_REPEAT = 2; // 重新发送
	private static final int SMSDDK_HANDLER = 3; // 短信回调
	private int TIME = 60;// 倒计时60s

	private EditText user_et, pswd_et, code_et;
	private Button code_btn, finish_btn;
	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		@SuppressLint("ShowToast")
		public void handleMessage(Message msg) {
			if (msg.what == -9) {
				code_btn.setText("重新发送(" + TIME-- + ")");
			} else if (msg.what == -8) {
				code_btn.setEnabled(true);
				code_btn.setText("获取验证码");
				TIME = 60;
			}
			switch (msg.what) {
			case CODE_ING:

				break;
			case CODE_REPEAT:

				break;
			case SMSDDK_HANDLER:
				int event = msg.arg1;
				int result = msg.arg2;
				Object data = msg.obj;

				// 短信注册成功后，返回MainActivity,然后提示新好友
				if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {// 提交验证码成功
					
					if (result == SMSSDK.RESULT_COMPLETE) {
						// 这里我们要跳转祝页面
						Intent intent = new Intent(RegActivity.this,
								RegMoreActivity.class);
						intent.putExtra("user", user_et
								.getText().toString());
						intent.putExtra("pswd", pswd_et
								.getText().toString());
						startActivity(intent);
						finish();


						
					} else {
						Toast.makeText(RegActivity.this, "验证码错误", 0).show();


					
					}

				} else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
					Toast.makeText(getApplicationContext(), "验证码已经发送",
							Toast.LENGTH_SHORT).show();
				} else {
					((Throwable) data).printStackTrace();
				}

				break;

			default:
				break;
			}
		};
	};

	@SuppressLint("ShowToast")
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
		switch (v.getId()) {
		case R.id.Reg_button1:
			// 需要验证码
			if (!CheckUtils.isMobileNO(user_et.getText().toString())) {
				Toast.makeText(this, "请检查你的手机号码是否输入正确", 0).show();
				return;
			}
			SMSSDK.getVerificationCode("86", user_et.getText().toString());
			code_btn.setEnabled(false);
			new Thread(new Runnable() {
				@Override
				public void run() {
					for (int i = 60; i > 0; i--) {
						handler.sendEmptyMessage(-9);
						if (i <= 0) {
							break;
						}
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					handler.sendEmptyMessage(-8);

				}
			}).start();
			break;
		case R.id.Reg_button2:
			// 注册啊那妞
			String user_str = user_et.getText().toString();
			String pswd_str = pswd_et.getText().toString();
			if (user_str.equals("") || pswd_str.equals("")) {
				// 密码或者账号都为空
				ToastUtils.ShowShortToast(getString(R.string.log_notnull));
				return;
			}

			if (!CheckUtils.isMobileNO(user_et.getText().toString())) {
				Toast.makeText(this, "请检查你的手机号码是否输入正确", 0).show();
				return;
			}
			if (pswd_str.length() < 8) {
				ToastUtils.ShowShortToast("密码不能小于8位");
				return;
			}
			if (CheckUtils.isConSpeCharacters(pswd_et.getText().toString())) {
				ToastUtils.ShowShortToast(getString(R.string.log_feifa));
				return;

			}
			Intent intent = new Intent(RegActivity.this,
					RegMoreActivity.class);
			intent.putExtra("user", user_et
					.getText().toString());
			intent.putExtra("pswd", pswd_et
					.getText().toString());
			startActivity(intent);
			finish();
//			SMSSDK.submitVerificationCode("86", user_et.getText().toString(),
//					code_et.getText().toString());
			break;

		default:
			break;
		}
	}

	// 初始化验证码sdk
	public void initSdk() {
		SMSSDK.initSDK(this, AppConfig.mob_key, AppConfig.mob_pswd);
		EventHandler eventHandler = new EventHandler() {
			@Override
			public void afterEvent(int arg0, int arg1, Object arg2) {
				// TODO Auto-generated method stub
				Message msg = new Message();
				msg.arg1 = arg0;
				msg.arg2 = arg1;
				msg.obj = arg2;
				msg.what = SMSDDK_HANDLER;
				handler.sendMessage(msg);
			}
		};

		SMSSDK.registerEventHandler(eventHandler);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		SMSSDK.unregisterAllEventHandler();
		super.onDestroy();
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_reg);
		super.init();
		user_et = (EditText) findViewById(R.id.Reg_editText1);
		pswd_et = (EditText) findViewById(R.id.Reg_editText2);
		code_et = (EditText) findViewById(R.id.Reg_editText3);
		code_btn = (Button) findViewById(R.id.Reg_button1);
		finish_btn = (Button) findViewById(R.id.Reg_button2);
		code_btn.setOnClickListener(this);
		finish_btn.setOnClickListener(this);
		user_et.setOnKeyListener(this);
		pswd_et.setOnKeyListener(this);
		code_et.setOnKeyListener(this);
		initSdk();
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
