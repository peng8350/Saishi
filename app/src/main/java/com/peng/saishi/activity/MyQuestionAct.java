package com.peng.saishi.activity;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Vibrator;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.peng.saishi.R;
import com.peng.saishi.activity.base.BaseBackActivity;
import com.peng.saishi.adapter.MyQuestionAdapter;
import com.peng.saishi.entity.QuestionInfo;
import com.peng.saishi.entity.config.AppConfig;
import com.peng.saishi.interfaces.service.MyStringCallBack;
import com.peng.saishi.manager.NoDataManager;
import com.peng.saishi.utils.GlobeScopeUtils;
import com.peng.saishi.utils.JsonParser;
import com.peng.saishi.utils.ToastUtils;
import com.zhy.http.okhttp.OkHttpUtils;

public class MyQuestionAct extends BaseBackActivity {
	@ViewInject(R.id.myquestion_listview)
	private ListView listView;
	private View nodata_layout;
	private List<QuestionInfo> questions;
	@ViewInject(R.id.myquestion_delete)
	private TextView del_btn;
	private boolean edit = false;
	private Vibrator vibrator;

	public boolean isEdit() {
		return edit;
	}

	public void setEdit(boolean edit) {
		this.edit = edit;
		del_btn.setText(edit ? "确定" : "删除");
		if (edit) {
			for (QuestionInfo info : questions) {
				info.setShow(true);
				info.setChecked(false);
				vibrator.vibrate(300);
			}
		} else {
			for (QuestionInfo info : questions) {
				info.setShow(false);
			}
		}
		((BaseAdapter) listView.getAdapter()).notifyDataSetChanged();
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_myquestion);
		super.init();
		ViewUtils.inject(this);
		nodata_layout = NoDataManager.initNodata("你没有提问过哦!", this);
		initData();
		vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
	}

	// 点击删除按钮
	@OnClick(R.id.myquestion_delete)
	public void ClickDel(View v) {
		if (edit) {
			// 给服务端发送请求
			String params = "";
			final List<QuestionInfo> del_info = new ArrayList<>();
			for (int i = 0; i < questions.size(); i++) {
				if (questions.get(i).isChecked()) {
					params += questions.get(i).getId() + ",";
					del_info.add(questions.get(i));
				}
			}
			if (!params.equals("")) {
				params = params.substring(0, params.length() - 1);
				System.out.println(params);
				OkHttpUtils.post().url(AppConfig.DelQuestion)
						.addParams("params", params).build()
						.execute(new MyStringCallBack() {

							@Override
							public void onResponse(String response, int id) {
								// TODO Auto-generated method stub
								findViewById(R.id.wait_bar).setVisibility(View.GONE);
								if (id!=0) {
									ToastUtils.ShowNetError();
									return ;
								}
								try {
									JSONObject res_obj = new JSONObject(
											response);
									if (res_obj.getString("result")
											.equals("ok")) {
										questions.removeAll(del_info.subList(0,
												del_info.size()));
										((BaseAdapter) listView.getAdapter())
												.notifyDataSetChanged();
										ToastUtils.ShowShortToast("删除成功!");
									} else {
										ToastUtils.ShowShortToast("删除失败!");
									}
								} catch (JSONException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
							

							@Override
							public void onError(Call call, Exception e, int id) {
								// TODO Auto-generated method stub
								findViewById(R.id.wait_bar).setVisibility(View.GONE);
								ToastUtils.ShowShortToast("删除失败!");
							}
						});
			}
		}
		setEdit(!isEdit());
	}

	// 初始化数据
	private void initData() {
		// TODO Auto-generated method stub
		OkHttpUtils.post().url(AppConfig.getQuestions2)
				.addParams("id", GlobeScopeUtils.getMeId() + "").build()
				.execute(new MyStringCallBack() {

					@Override
					public void onResponse(String response, int id) {
						// TODO Auto-generated method stub
						findViewById(R.id.wait_bar).setVisibility(View.GONE);
						if (id!=0) {
							ToastUtils.ShowNetError();
							return ;
						}
						JSONArray array = null;
						try {
							array = new JSONArray(response);
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						questions = JsonParser.getListFromJson(array,
								QuestionInfo.class);
						listView.setAdapter(new MyQuestionAdapter(
								MyQuestionAct.this, questions));
						NoDataManager.ChangeState(nodata_layout, questions);
					}

					@Override
					public void onError(Call call, Exception e, int id) {
						// TODO Auto-generated method stub
						findViewById(R.id.wait_bar).setVisibility(View.GONE);
						ToastUtils.ShowNetError();
						NoDataManager.ChangeState(nodata_layout, questions);
					}
				});

	}
}
