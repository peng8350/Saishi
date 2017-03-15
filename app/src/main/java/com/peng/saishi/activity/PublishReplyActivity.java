package com.peng.saishi.activity;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.peng.saishi.R;
import com.peng.saishi.activity.base.BaseBackActivity;
import com.peng.saishi.entity.User;
import com.peng.saishi.entity.config.AppConfig;
import com.peng.saishi.interfaces.service.MyStringCallBack;
import com.peng.saishi.utils.DialogUtils;
import com.peng.saishi.utils.GlobeScopeUtils;
import com.peng.saishi.utils.ToastUtils;
import com.zhy.http.okhttp.OkHttpUtils;

public class PublishReplyActivity extends BaseBackActivity implements TextWatcher {

	private EditText input_et;
	private Button publish_btn;
	private int match_id;
	@Override
	public void init() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_publish);
		super.init();
		match_id = getIntent().getIntExtra("match_id", 0);
		input_et = (EditText) findViewById(R.id.publish_editText1);
		System.out.println(match_id);
		input_et.addTextChangedListener(this);
		publish_btn = (Button) findViewById(R.id.publish_Btn);
		publish_btn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
		switch (v.getId()) {
		case R.id.publish_Btn:
//			String targetmatter= request.getParameter("target");
//			String name = request.getParameter("name");
//			String content = request.getParameter("content");
//			String time = MyDateUtils.getNowTime();
//			String head = request.getParameter("icon");
			Map<String, String> params = new HashMap<>();
			params.put("content",input_et.getText().toString());
			params.put("icon",((User)GlobeScopeUtils.Get("ME")).getId()+"");
			params.put("id",match_id+"");
			params.put("name",((User)GlobeScopeUtils.Get("ME")).getUsername());
			final Dialog dialog = DialogUtils.createLoadingDialog(this, "发表中...");
			dialog.show();
			OkHttpUtils.post().url(AppConfig.AddPingjia).params(params).build().execute(new MyStringCallBack() {
				
				@Override
				public void onResponse(String response, int id) {
					// TODO Auto-generated method stub

					// TODO Auto-generated method stub
					dialog.dismiss();
					if (id!=0) {
						ToastUtils.ShowNetError();
						return ;
					}
					try {
						JSONObject res_obj = new JSONObject(response);
						if (res_obj.getString("result").equals("ok")) {
							setResult(RESULT_OK);
							finish();
							ToastUtils.ShowShortToast("评价成功");
						}	
						else{
							ToastUtils.ShowShortToast("发表失败");
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				
				}
				
				@Override
				public void onError(Call call, Exception e, int id) {
					// TODO Auto-generated method stub

					// TODO Auto-generated method stub
					ToastUtils.ShowNetError();
					dialog.dismiss();
				
				}
			});
			break;

		default:
			break;
		}
	}

	@Override
	public void afterTextChanged(Editable s) {
		// TODO Auto-generated method stub
		String content = s.toString();
		publish_btn.setEnabled(content.length() > 5);
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		// TODO Auto-generated method stub

	}

}
