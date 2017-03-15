package com.peng.saishi.activity;

import java.util.Collections;
import java.util.Comparator;

import okhttp3.Call;

import org.json.JSONArray;
import org.json.JSONException;

import android.content.Intent;
import android.view.View;
import android.widget.HeaderViewListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.peng.saishi.R;
import com.peng.saishi.activity.base.BaseBackActivity;
import com.peng.saishi.adapter.QuestionListAdapter;
import com.peng.saishi.entity.MatchInfo;
import com.peng.saishi.entity.QuestionInfo;
import com.peng.saishi.entity.config.AppConfig;
import com.peng.saishi.interfaces.service.MyStringCallBack;
import com.peng.saishi.manager.NoDataManager;
import com.peng.saishi.utils.JsonParser;
import com.peng.saishi.utils.ToastUtils;
import com.peng.saishi.widget.pulltorefesh.PullToRefreshBase;
import com.peng.saishi.widget.pulltorefesh.PullToRefreshBase.OnRefreshListener2;
import com.peng.saishi.widget.pulltorefesh.PullToRefreshListView;
import com.zhy.http.okhttp.OkHttpUtils;

public class QuestionListAct extends BaseBackActivity implements
		OnRefreshListener2<ListView>, OnCheckedChangeListener {

	private MatchInfo info;
	@ViewInject(R.id.Questionlist_group)
	RadioGroup radioGroup;
	@ViewInject(R.id.Questionlist_listView1)
	private PullToRefreshListView listview;
	@ViewInject(R.id.radioButton1)
	private RadioButton tab1;
	@ViewInject(R.id.radioButton2)
	private RadioButton tab2;
	private View nodata_layout;

	// 初始化的数量
	private int origin_count;

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (origin_count != info.getQuestions().size()) {
			setResult(RESULT_OK);
		}
		super.onClick(v);
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		if (origin_count != info.getQuestions().size()) {
			setResult(RESULT_OK);
		}
		super.onBackPressed();
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_questionlist);
		super.init();
		info = (MatchInfo) getIntent().getSerializableExtra("match");
		origin_count = info.getQuestions().size();
		ViewUtils.inject(this);
		initdata();
		nodata_layout = NoDataManager.initNodata("没有人答疑!", this);
		listview.setOnRefreshListener(this);
		listview.getLoadingLayoutProxy().setRefreshingLabel("正在刷新数据...");
		listview.getLoadingLayoutProxy().setReleaseLabel("松开手指刷新...");
		listview.getLoadingLayoutProxy().setPullLabel("下拉刷新..");
		radioGroup.setOnCheckedChangeListener(this);

		// TODO Auto-generated method stub
		OkHttpUtils.post().url(AppConfig.GetQuestions)
				.addParams("id", info.getId() + "").build()
				.execute(new MyStringCallBack() {

					@Override
					public void onResponse(String response, int id) {
						// TODO Auto-generated method stub
						if (id != 0) {
							ToastUtils.ShowNetError();
							return;
						}
						JSONArray array = null;
						try {
							array = new JSONArray(response);
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						info.setQuestions(JsonParser.getListFromJson(array,
								QuestionInfo.class));
						if (((RadioButton)radioGroup.getChildAt(0)).isChecked()) {
							//时间排序
						}
						else{
							//点咱排序
							
						}
						listview.setAdapter(new QuestionListAdapter(
								QuestionListAct.this, info.getQuestions()));
						NoDataManager.ChangeState(nodata_layout,
								info.getQuestions());
					}

					@Override
					public void onError(Call call, Exception e, int id) {
						// TODO Auto-generated method stub
						ToastUtils.ShowNetError();
						NoDataManager.ChangeState(nodata_layout,
								info.getQuestions());
					}
				});

	}

	@OnClick(R.id.Questionlist_Btn)
	public void EnterCreateQueAct(View v) {
		Intent intent = new Intent(this, CreateQuestionAct.class);
		intent.putExtra("match", info);
		startActivityForResult(intent, 101);
	}

	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		// TODO Auto-generated method stub
		if (arg0 == 101 && arg1 == RESULT_OK) {
			// 假如返回需要更新activity的list列表的时候,
			// arg0返回码为101,请求为ok

			//让下拉刷新的listview直接刷新列表
			listview.setRefreshing(true);
			
			
		}
		super.onActivityResult(arg0, arg1, arg2);
	}

	private void initdata() {

		// TODO Auto-generated method stub
		listview.setAdapter(new QuestionListAdapter(QuestionListAct.this, info
				.getQuestions()));
	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
		// TODO Auto-generated method stub
		OkHttpUtils.post().url(AppConfig.GetQuestions)
				.addParams("id", info.getId() + "").build()
				.execute(new MyStringCallBack() {

					@Override
					public void onResponse(String response, int id) {
						// TODO Auto-generated method stub
						if (id != 0) {
							ToastUtils.ShowNetError();
							return;
						}
						JSONArray array = null;
						try {
							array = new JSONArray(response);
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						info.setQuestions(JsonParser.getListFromJson(array,
								QuestionInfo.class));
						onCheckedChanged(radioGroup, radioGroup.getCheckedRadioButtonId());
						listview.onRefreshComplete();
						NoDataManager.ChangeState(nodata_layout,
								info.getQuestions());
					}

					@Override
					public void onError(Call call, Exception e, int id) {
						// TODO Auto-generated method stub
						ToastUtils.ShowNetError();
						NoDataManager.ChangeState(nodata_layout,
								info.getQuestions());
					}
				});
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		// TODO Auto-generated method stub
		// 重新排序
		if (checkedId == tab1.getId()) {
			Collections.sort(info.getQuestions(),
					new Comparator<QuestionInfo>() {

						@Override
						public int compare(QuestionInfo lhs, QuestionInfo rhs) {
							// TODO Auto-generated method stub
							if (com.peng.saishi.utils.TimeUtils
									.getDateFromChatTime(lhs.getTime())
									.getTime() < com.peng.saishi.utils.TimeUtils
									.getDateFromChatTime(rhs.getTime())
									.getTime()) {
								return 1;
							}
							return -1;
						}
					});
		} else {
			Collections.sort(info.getQuestions(),
					new Comparator<QuestionInfo>() {

						@Override
						public int compare(QuestionInfo lhs, QuestionInfo rhs) {
							// TODO Auto-generated method stub
							if (lhs.getVisit() < rhs.getVisit()) {
								return 1;
							}
							return -1;
						}
					});
		}
		QuestionListAdapter adapter = ((QuestionListAdapter) ((HeaderViewListAdapter) listview
				.getRefreshableView().getAdapter()).getWrappedAdapter());
		adapter.setList(info.getQuestions());
		adapter.notifyDataSetChanged();
	}

}
