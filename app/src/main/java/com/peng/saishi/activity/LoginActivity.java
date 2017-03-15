package com.peng.saishi.activity;

import android.annotation.SuppressLint;
import android.app.ActionBar.LayoutParams;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.*;
import android.widget.CompoundButton.OnCheckedChangeListener;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.im.android.api.JMessageClient;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.facebook.drawee.view.SimpleDraweeView;
import com.peng.saishi.R;
import com.peng.saishi.activity.base.BaseActivity;
import com.peng.saishi.adapter.UserMenuAdapter;
import com.peng.saishi.entity.MatchInfo;
import com.peng.saishi.entity.User;
import com.peng.saishi.entity.config.AppConfig;
import com.peng.saishi.interfaces.service.MyStringCallBack;
import com.peng.saishi.utils.*;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;
import okhttp3.Call;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.*;

/**
 * 登陆的activity
 * 
 * @author peng 成员变量: 用户输入框，密码输入框,记住密码和自动登陆多选框 登陆按钮,注册按钮,头像
 *         实现onkeylistener,监听回车事件， 监听文本输入事件，随时改变图像 用户和密码输入框带有删除全部的功能
 */
public class LoginActivity extends BaseActivity implements OnKeyListener,
		TextWatcher, OnCheckedChangeListener {
	private List<User> load_users;
	private EditText et_user, et_pswd;
	private CheckBox cb_rem, cb_autolog;
	private Button btn_login, clear1, clear2, btn_more;
	private TextView tv_reg;
	private SimpleDraweeView image_head;
	private PopupWindow window;
	private ListView user_list;
	private View user_view;
	private View et_layout;
	private ImageState nowState = ImageState.NONE;

	public enum ImageState {
		NONE, HAVE;
	}

	/**
	 * 菜单、返回键响应
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			exitBy2Click(); // 调用双击退出函数
		}

		return false;
	}

	/**
	 * 双击退出函数
	 */
	private static Boolean isExit = false;

	private void exitBy2Click() {
		Timer tExit = null;
		if (isExit == false) {
			isExit = true; // 准备退出
			Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
			tExit = new Timer();

			tExit.schedule(new TimerTask() {
				@Override
				public void run() {
					isExit = false; // 取消退出
				}
			}, 2000); // 如果2秒钟内没有按下返回键，则启动定时器取消掉刚才执行的任务

		} else {
			finish();
			System.exit(0);
		}
	}

	// @Overrideload(AppConfig.Radio_path + param[1],
	// folder + "/" + param[1] + ".amr",
	// new RequestCallBack<File>() {
	//
	// @Override
	// public void onSuccess(
	// ResponseInfo<File> arg0) {}
	//
	// @Override
	// public void onFailure(HttpException arg0,
	// String arg1) {}
	// });
	public void onClick(View v) {
		// TODO Auto-generated method stub
		// 下拉,clear1,clear2,reg,login
		switch (v.getId()) {
		case R.id.Login_textView2:
			// 忘记密码
			Intent intent = new Intent(this, ForgetActivity1.class);
			startActivity(intent);
			break;
		case R.id.activity_login_xiala:
			// 弹出下拉选择账号
			if (window == null) {
				initPop();
			}
			window.showAsDropDown(et_user, 0, 0);
			break;

		case R.id.Login_clear1:

			et_user.setText("");
			et_user.requestFocus();
			break;
		case R.id.Login_clear2:
			et_pswd.setText("");
			et_pswd.requestFocus();
			break;
		case R.id.Login_button1:
			Login(et_user.getText().toString(), et_pswd.getText().toString());
			break;
		case R.id.Login_Reg:
			ActChangeUtils.ChangeActivity(this, RegActivity.class);
			break;
		default:
			break;
		}
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		// 在oncreate方法里面的
		if (!isTaskRoot()) {
			finish();
			return;
		}
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		setContentView(R.layout.activity_login);
		cb_autolog = (CheckBox) findViewById(R.id.Login_auto);

		cb_rem = (CheckBox) findViewById(R.id.Login_checkBox1);
		et_pswd = (EditText) findViewById(R.id.Login_editText1);
		et_user = (EditText) findViewById(R.id.Login_EditText01);
		btn_login = (Button) findViewById(R.id.Login_button1);
		tv_reg = (TextView) findViewById(R.id.Login_Reg);
		clear1 = (Button) findViewById(R.id.Login_clear1);
		clear2 = (Button) findViewById(R.id.Login_clear2);
		btn_more = (Button) findViewById(R.id.activity_login_xiala);
		image_head = (SimpleDraweeView) findViewById(R.id.activity_login_iconView1);
		et_layout = findViewById(R.id.login_et_layout);
		findViewById(R.id.Login_textView2).setOnClickListener(this);
		clear1.setOnClickListener(this);
		initPop();
		clear2.setOnClickListener(this);
		btn_more.setOnClickListener(this);
		btn_login.setOnClickListener(this);
		tv_reg.setOnClickListener(this);
		et_pswd.setOnKeyListener(this);
		et_user.setOnKeyListener(this);
		et_user.addTextChangedListener(this);
		cb_autolog.setOnCheckedChangeListener(this);
		SharedPreferences preferences = PreferencesTool.getInstance("Login");
		boolean rem = preferences.getBoolean("Rem", false);
		boolean auto = preferences.getBoolean("auto", false);
		// 判断忘记密码和自动登陆的sharedpreferences
		cb_autolog.setChecked(auto);
		cb_rem.setChecked(rem);
		JMessageClient.init(getApplicationContext());
		JMessageClient
				.setNotificationMode(JMessageClient.NOTI_MODE_NO_NOTIFICATION);

		if (cb_rem.isChecked()) {
			et_user.setText(preferences.getString("username", ""));
			et_pswd.setText(preferences.getString("pswd", ""));
		}
		if (cb_autolog.isChecked()) {
			Login(et_user.getText().toString(), et_pswd.getText().toString());
		}
		YoYo.with(Techniques.SlideInLeft).duration(1200).playOn(et_layout);
		YoYo.with(Techniques.SlideInLeft).duration(1200).playOn(btn_login);
		YoYo.with(Techniques.SlideInLeft).duration(1200).playOn(cb_autolog);
		YoYo.with(Techniques.SlideInLeft).duration(1200).playOn(cb_rem);
		YoYo.with(Techniques.SlideInLeft).duration(1200).playOn(image_head);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		JPushInterface.onPause(this);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		JPushInterface.onResume(this);
	}

	// 初始化pop的菜单
	@SuppressLint({ "NewApi", "InflateParams" })
	private void initPop() {
		// TODO Auto-generated method stub
		window = new PopupWindow(SystemUtils.getAppWidth() - 80,
				LayoutParams.WRAP_CONTENT);
		user_view = LayoutInflater.from(this).inflate(R.layout.pop_usershow,
				null);
		window.setContentView(user_view);
		user_list = (ListView) user_view
				.findViewById(R.id.pop_usershow_listView1);
		window.setOutsideTouchable(true);
		window.setFocusable(true);
		window.setBackgroundDrawable(new ColorDrawable(
				android.R.color.transparent));
		load_users = User.listAll(User.class);
		user_list.setAdapter(new UserMenuAdapter(this, load_users, window));

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

	/**
	 * 监听user_et文本改变 改变后,修改图像 注意判断字符串是不是空
	 */
	@SuppressWarnings("deprecation")
	@Override
	public void afterTextChanged(Editable s) {
		// TODO Auto-generated method stub
		String content = s.toString();

		if (s == et_user.getText()) {
			if (CheckUtils.isMobileNO(content)) {
				User now_user = null;
				if (load_users != null)
					for (User user : load_users) {

						if (user.getUser().equals(et_user.getText().toString())) {

							now_user = user;
						}
					}
				if (now_user != null) {
					nowState = ImageState.HAVE;
					FrescoUtils.Display(image_head, AppConfig.Userpic_path
							+ now_user.getId(), 200, 200, true);

				}
			} else {
				et_pswd.setText("");
				if (nowState == ImageState.HAVE) {
					image_head.setImageBitmap(BitmapFactory.decodeResource(
							getResources(), R.drawable.newuser));
					nowState = ImageState.NONE;
				}
			}
			if (content.length() == 0) {
				clear1.setVisibility(View.INVISIBLE);
			} else {
				clear1.setVisibility(View.VISIBLE);
			}

			// 根据账号变化
		} else if (s == et_pswd.getText()) {

			if (content.length() == 0) {
				clear2.setVisibility(View.INVISIBLE);
			} else {
				clear2.setVisibility(View.VISIBLE);
			}
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

	List<MatchInfo> db_matches;

	// 登陆的时候判断
	public void Login(final String user, final String pswd) {
		if (user.equals("")) {
			// 密码或者账号都为空
			YoYo.with(Techniques.Shake).duration(500).playOn(et_user);
			return;
		}
		if (pswd.equals("")) {
			YoYo.with(Techniques.Shake).duration(500).playOn(et_pswd);
			return;
		}
		if (!CheckUtils.isMobileNO(user)) {
			DialogUtils.ShowTips("你输入的不是手机号码格式", this);
			return;
		}

		if (!CheckUtils.isConSpeCharacters(pswd)) {
			// 不带有非法字符串
			final Dialog dialog = DialogUtils.createLoadingDialog(this,
					"登陆中...");
			dialog.show();
			// TODO Auto-generated method stub
			db_matches = MatchInfo.findWithQuery(MatchInfo.class,
					"select * from Match_Info order by mid desc");
			final Map<String, String> map = new HashMap<>();
			final boolean fitst = db_matches.size() == 0;
			map.put("user", user);
			map.put("pswd", pswd);
			map.put("first",
					db_matches == null || db_matches.size() == 0 ? "true"
							: "false");
			final long time = System.currentTimeMillis();

			OkHttpUtils.post().url(AppConfig.LOGIN).params(map).build()
					.execute(new MyStringCallBack() {

						@Override
						public void onResponse(String response, int id) {
							// TODO Auto-generated method stub
							// TODO Auto-generated method stub
							// TODO Auto-generated method stub

							if (id != 0 && !response.startsWith("[")) {
								ToastUtils.ShowNetError();
								dialog.dismiss();
								return;
							}
							try {
								OkHttpUtils.post().url(AppConfig.getToken)
										.build().execute(new StringCallback() {

											@Override
											public void onResponse(String arg0,
													int arg1) {
												// TODO Auto-generated method
												// stub
												AppConfig.qiniu_taken = arg0;
											}

											@Override
											public void onError(Call arg0,
													Exception arg1, int arg2) {
												// TODO Auto-generated method
												// stub

											}
										});
								final JSONObject res_obj = new JSONObject(
										response);
								if (res_obj.get("result").equals("ok")) {
									PreferencesTool.setParams(
											"Login",
											new String[] { "username", "pswd",
													"Rem", "auto" },
											new Object[] { user, pswd,
													cb_rem.isChecked(),
													cb_autolog.isChecked() });
									User me = null;
									try {
										if (fitst) {
											db_matches = JsonParser.getListFromJson(
													res_obj.getJSONArray("matches"),

													MatchInfo.class);
											MatchInfo.saveInTx(db_matches);

										}

										me = JsonParser.getBeanFromJson(
												res_obj.getJSONObject("user"),
												User.class);

										me.setMatchs(db_matches);

										me.setTeam1(JsonParser.getListFromJson(
												res_obj.getJSONArray("team1"),
												com.peng.saishi.entity.TeamInfo.class));
										me.setTeam2(JsonParser.getListFromJson(
												res_obj.getJSONArray("team2"),
												com.peng.saishi.entity.TeamInfo.class));
										me.setShoucangs(getUserLike(
												res_obj.getString("shoucangs"),
												db_matches));
									} catch (JSONException e1) {
										// TODO
										// Auto-generated
										// catch block
										e1.printStackTrace();
									}
									GlobeScopeUtils.Put("ME", me);
									dialog.dismiss();
									ActChangeUtils.ChangeActivity(
											LoginActivity.this,
											MainActivity.class,
											new String[] { "user", "pswd" },
											new String[] { me.getUser(),
													me.getPswd() });
									finish();

									me.save();
								} else {
									dialog.dismiss();
									ToastUtils.ShowShortToast("密码错误!!");
								}

							} catch (JSONException e) {
								// TODO Auto-generated catch
								// blocknumber
								e.printStackTrace();
								dialog.dismiss();
							}

						}

						@Override
						public void onError(Call arg0, Exception arg1, int arg2) {
							// TODO Auto-generated method stub
							super.onError(arg0, arg1, arg2);
							dialog.dismiss();
							ToastUtils.ShowNetError();
						}
					});

		} else {
			DialogUtils.ShowTips(getString(R.string.log_feifa), this);

			return;
		}

	}

	public List<MatchInfo> getUserLike(String contain_str, List<MatchInfo> all) {
		List<MatchInfo> params = new ArrayList<>();
		for (MatchInfo info : db_matches) {
			if (contain_str.contains(info.getId() + "")) {
				params.add(info);
			}
		}
		return params;
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		// TODO Auto-generated method stub
		if (isChecked) {
			cb_rem.setChecked(true);
		}
	}

	// 传入User,直接给user_et,和密码输入框直接副职
	public void setUserIntoEt(User user) {
		et_user.setText(user.getUser());
		et_pswd.setText(user.getPswd());
	}

}
