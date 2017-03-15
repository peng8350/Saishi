package com.peng.saishi.activity;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.EditText;

import com.daimajia.androidanimations.library.YoYo;
import com.daimajia.androidanimations.library.attention.ShakeAnimator;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.peng.saishi.R;
import com.peng.saishi.activity.base.BaseBackActivity;
import com.peng.saishi.entity.MatchInfo;
import com.peng.saishi.entity.config.AppConfig;
import com.peng.saishi.interfaces.service.MyStringCallBack;
import com.peng.saishi.utils.DialogUtils;
import com.peng.saishi.utils.GlobeScopeUtils;
import com.peng.saishi.utils.ToastUtils;
import com.zhy.http.okhttp.OkHttpUtils;

public class CreateQuestionAct extends BaseBackActivity implements
		OnKeyListener {
	@ViewInject(R.id.QuesCreate_editText1)
	private EditText title_et;
	@ViewInject(R.id.QuesCreate_editText2)
	private EditText content_et;
	private MatchInfo match;

	@Override
	public void init() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_questioncreate);
		match = (MatchInfo) getIntent().getSerializableExtra("match");
		super.init();
		ViewUtils.inject(this);
		title_et.setOnKeyListener(this);
	}

	@Override
	public boolean onKey(View v, int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_ENTER) {
			return true;
		}
		return false;
	}

	@OnClick(R.id.QuesCreate_button1)
	public void Sendout(View v) {
		String title = title_et.getText().toString();
		String content = content_et.getText().toString();
		if (title.trim().equals("")) {
			YoYo.with(new ShakeAnimator()).duration(300).playOn(title_et);
			return ;
		}
		if ( content.trim().equals("")) {
			YoYo.with(new ShakeAnimator()).duration(300).playOn(content_et);
			return ;
		}
		if (title.length() < 5) {
			ToastUtils.ShowShortToast("标题不能少于5个字!");
			return;
		}
		if (content.length() < 10) {
			ToastUtils.ShowShortToast("正文不能少于10个字!");
			return;
		}
		// String title = request.getParameter("title");
		// String content = request.getParameter("content");
		// String matchid = request.getParameter("matchid");
		// String userid = request.getParameter("userid");
		// String username = request.getParameter("username");
		Map<String, String> params = new HashMap<>();
		params.put("username", GlobeScopeUtils.getUserName());
		params.put("userid", GlobeScopeUtils.getMeId() + "");
		params.put("content", content);
		params.put("title", title);
		params.put("matchid", match.getId() + "");
		params.put("matchname", match.getName());
		final Dialog wait_dialog = DialogUtils.createLoadingDialog(this, "发表中...");
		wait_dialog.show();
		OkHttpUtils.post().url(AppConfig.CreateQuestion).params(params).build()
				.execute(new MyStringCallBack() {

					@Override
					public void onError(Call call, Exception e, int id) {
						// TODO Auto-generated method stub
						wait_dialog.dismiss();
						
						ToastUtils.ShowShortToast("发表失败!!");
					}

					@Override
					public void onResponse(String response, int id) {
						// TODO Auto-generated method stub
						try {
							if (id!=0) {
								ToastUtils.ShowNetError();
								wait_dialog.dismiss();
								return ;
							}
							JSONObject res_obj = new JSONObject(response);
							wait_dialog.dismiss();
							if (res_obj.getString("result").equals("ok")) {
								setResult(RESULT_OK);
								SendSuceess();
							} else {
								ToastUtils.ShowShortToast("不明原因!");
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

				});

	}

	// 关闭activity
	public void SendSuceess() {
		finish();
		ToastUtils.ShowShortToast("发表成功!");
	}

}
