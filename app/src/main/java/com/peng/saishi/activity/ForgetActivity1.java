package com.peng.saishi.activity;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

import com.peng.saishi.R;
import com.peng.saishi.activity.base.BaseBackActivity;
import com.peng.saishi.entity.config.AppConfig;
import com.peng.saishi.interfaces.service.MyStringCallBack;
import com.peng.saishi.utils.ActChangeUtils;
import com.peng.saishi.utils.CheckUtils;
import com.peng.saishi.utils.ToastUtils;
import com.zhy.http.okhttp.OkHttpUtils;

public class ForgetActivity1 extends BaseBackActivity {
	private static final int CODE_ING = 1; // 已发送，倒计时
	private static final int CODE_REPEAT = 2; // 重新发送
	private static final int SMSDDK_HANDLER = 3; // 短信回调
	private int TIME = 60;// 倒计时60s
	private EditText phone_et;
	private EditText code_et;
	private Button send_btn;
	private Button next_btn;
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			if (msg.what == -9) {
				send_btn.setText("重新发送(" + TIME-- + ")");
			} else if (msg.what == -8) {
				send_btn.setEnabled(true);
				send_btn.setText("获取验证码");
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
						ActChangeUtils.ChangeActivity(ForgetActivity1.this, ForgetActivity2.class, "user",
								phone_et.getText().toString());
						finish();
					} else {
						Toast.makeText(ForgetActivity1.this, "验证码错误", 0).show();
					}

				} else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
					Toast.makeText(getApplicationContext(), "验证码已经发送", Toast.LENGTH_SHORT).show();
				} else {
					((Throwable) data).printStackTrace();
				}

				break;

			default:
				break;
			}
		};
	};

	@Override
	public void init() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_forget1);
		super.init();
		phone_et = (EditText) findViewById(R.id.Forget1_editText1);
		code_et = (EditText) findViewById(R.id.Forget1_editText3);
		send_btn = (Button) findViewById(R.id.Forget1_button1);
		next_btn = (Button) findViewById(R.id.Forget1_button2);
		send_btn.setOnClickListener(this);
		next_btn.setOnClickListener(this);
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
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
		switch (v.getId()) {
		case R.id.Forget1_button1:
			// 需要验证码
			if (!CheckUtils.isMobileNO(phone_et.getText().toString())) {
				Toast.makeText(this, "请检查你的手机号码是否输入正确", 0).show();
				return;
			}
			send_btn.setEnabled(false);
			SMSSDK.getVerificationCode("86", phone_et.getText().toString());
			
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
		case R.id.Forget1_button2:
			// 注册啊那妞

			if (!CheckUtils.isMobileNO(phone_et.getText().toString())) {
				Toast.makeText(this, "请检查你的手机号码是否输入正确", 0).show();
				return;
			}	
			
			if (CheckUtils.isConSpeCharacters(phone_et.getText().toString())) {
				ToastUtils.ShowShortToast(getString(R.string.log_feifa));
				return;

			}
			Map<String, String> param = new HashMap<>();
			param.put("user",phone_et.getText().toString());
			OkHttpUtils.post().url(AppConfig.UserExit).params(param).build().execute(new MyStringCallBack() {
				
				@Override
				public void onResponse(String response, int id) {
					// TODO Auto-generated method stub
					if (id!=0) {
						ToastUtils.ShowNetError();
						return ;
					}
					try {
						JSONObject obj =new JSONObject(response);
						if (obj.getString("have").equals("true")) {
							//存在
							SMSSDK.submitVerificationCode("86", phone_et.getText().toString(), code_et.getText().toString());
							
						}
						else{
							ToastUtils.ShowShortToast("用户不存在");
						}
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
		
			break;

		default:
			break;
		}
	}

}
