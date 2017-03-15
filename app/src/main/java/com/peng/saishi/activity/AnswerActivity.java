package com.peng.saishi.activity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

import org.json.JSONArray;
import org.json.JSONException;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HeaderViewListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.daimajia.androidanimations.library.YoYo;
import com.daimajia.androidanimations.library.attention.ShakeAnimator;
import com.facebook.drawee.view.SimpleDraweeView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.peng.saishi.R;
import com.peng.saishi.activity.base.BaseBackActivity;
import com.peng.saishi.adapter.AnswerAdapter;
import com.peng.saishi.entity.AnswerInfo;
import com.peng.saishi.entity.QuestionInfo;
import com.peng.saishi.entity.config.AppConfig;
import com.peng.saishi.interfaces.service.MyStringCallBack;
import com.peng.saishi.utils.FrescoUtils;
import com.peng.saishi.utils.GlobeScopeUtils;
import com.peng.saishi.utils.JsonParser;
import com.peng.saishi.utils.TimeUtils;
import com.peng.saishi.utils.ToastUtils;
import com.zhy.http.okhttp.OkHttpUtils;

public class AnswerActivity extends BaseBackActivity {
	@ViewInject(R.id.Answer_listView1)
	private ListView listView;
	@ViewInject(R.id.header_answer_name)
	private TextView name_tv;
	@ViewInject(R.id.header_answer_time)
	private TextView time_tv;
	@ViewInject(R.id.header_answer_descriable)
	private TextView descri_tv;
	@ViewInject(R.id.header_answer_head)
	private SimpleDraweeView user_image;
	@ViewInject(R.id.header_answer_content)
	private TextView content_tv;
	@ViewInject(R.id.Answer_bottom)
	private RelativeLayout bottom_layout;
	@ViewInject(R.id.Answer_inputet)
	private EditText input_et;
	@ViewInject(R.id.Answer_send_btn)
	private Button send_btn;

	private List<AnswerInfo> answers;
	private QuestionInfo questionInfo;

	public boolean isBottom_vis() {
		return bottom_vis;
	}

	public void setBottom_vis(boolean bottom_vis) {
		this.bottom_vis = bottom_vis;
		if (bottom_vis) {
			
			input_et.requestFocus();
		}
		bottom_layout.setVisibility(bottom_vis ? View.VISIBLE : View.GONE);
	}

	private boolean bottom_vis;

	@Override
	public void init() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_answer);
		super.init();
		ViewUtils.inject(this);
		questionInfo = (QuestionInfo) getIntent().getSerializableExtra(
				"question");
		OkHttpUtils.post().url(AppConfig.VisitQuestion)
				.addParams("id", questionInfo.getId() + "").build()
				.execute(null);
		View header = LayoutInflater.from(this).inflate(R.layout.header_answer,
				null);
		initHeader(header);
		listView.addHeaderView(header);
		initAnswer();
	}
	TextView header_answer_descri ;
	private void initHeader(View header) {

		// TODO Auto-generated method stub
		TextView header_name_tv = (TextView) header
				.findViewById(R.id.header_answer_name);
		header_answer_descri = (TextView) header
				.findViewById(R.id.header_answer_descriable);
		SimpleDraweeView header_answer_image = (SimpleDraweeView) header
				.findViewById(R.id.header_answer_head);
		TextView header_answer_time = (TextView) header
				.findViewById(R.id.header_answer_time);
		TextView header_answer_content = (TextView) header
				.findViewById(R.id.header_answer_content);
		header_name_tv.setText(questionInfo.getTitle());
		header_answer_descri.setText(questionInfo.getVisit() + "人浏览\t"
				+ (answers == null ? 0 : answers.size()) + "人回复");
		header_answer_content.setText(questionInfo.getContent());
		header_answer_time.setText(questionInfo.getTime());
		FrescoUtils.Display(header_answer_image, AppConfig.Userpic_path
				+questionInfo.getUserid(), 130, 130,true);
	}

	// 初始化回复
	private void initAnswer() {
		// TODO Auto-generated method stub
		listView.setAdapter(new AnswerAdapter(this, null));
		OkHttpUtils.post().url(AppConfig.GetAnswer)
				.addParams("id", questionInfo.getId() + "").build()
				.execute(new MyStringCallBack() {

					@Override
					public void onResponse(String response, int id) {
						// TODO Auto-generated method stub
						if (id!=0) {
							ToastUtils.ShowNetError();
							return ;
						}
						try {

							answers = JsonParser.getListFromJson(new JSONArray(
									response), AnswerInfo.class);
							header_answer_descri.setText(questionInfo.getVisit() + "人浏览\t"
									+ (answers == null ? 0 : answers.size()) + "人回复");
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						listView.setAdapter(new AnswerAdapter(
								AnswerActivity.this, answers));
					}

					@Override
					public void onError(Call call, Exception e, int id) {
						// TODO Auto-generated method stub
						ToastUtils.ShowNetError();
					}
				});
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		if (bottom_vis) {
			setBottom_vis(!bottom_vis);
			return;
		}
		super.onBackPressed();
	}

	// 点击回复按钮
	@OnClick(R.id.Answer_button1)
	public void ClickAnswer(View v) {
		setBottom_vis(!bottom_vis);
	}

	// 发送回复消息
	@OnClick(R.id.Answer_send_btn)
	public void SendAnswerMsg(View v) {
		final String content = input_et.getText().toString();
		if (content.equals("")) {
			YoYo.with(new ShakeAnimator()).duration(300).playOn(input_et);
			return ;
		}
		
		setBottom_vis(false);
		Map<String, String> params = new HashMap<>();
		final String userid = GlobeScopeUtils.getMeId() + "";
		final String username = GlobeScopeUtils.getUserName();
		final String questionid = questionInfo.getId() + "";
		params.put("userid", userid);
		params.put("username", username);
		params.put("content", content);
		params.put("questionid", questionid);
		OkHttpUtils.post().url(AppConfig.AnswerQuestion).params(params).build()
				.execute(new MyStringCallBack() {

					@Override
					public void onResponse(String response, int id) {
						// TODO Auto-generated method stub
						if (id!=0) {
							ToastUtils.ShowNetError();
							return ;
						}
						input_et.setText("");
						answers.add(new AnswerInfo(0, username, content,
								TimeUtils.getCurrentTime(), Integer
										.parseInt(userid), Integer
										.parseInt(questionid)));
						header_answer_descri.setText(questionInfo.getVisit() + "人浏览\t"
								+ (answers == null ? 0 : answers.size()) + "人回复");
						((BaseAdapter) ((HeaderViewListAdapter) listView
								.getAdapter()).getWrappedAdapter())
								.notifyDataSetChanged();
					}

					@Override
					public void onError(Call call, Exception e, int id) {
						// TODO Auto-generated method stub
						ToastUtils.ShowNetError();
					}
				});

	}
}
