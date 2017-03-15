package com.peng.saishi.activity;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import okhttp3.Call;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.os.Message;
import android.view.View;
import android.widget.BaseAdapter;

import com.peng.saishi.R;
import com.peng.saishi.activity.base.BaseBackActivity;
import com.peng.saishi.adapter.JoinGroupAdapter;
import com.peng.saishi.entity.JoinGroup;
import com.peng.saishi.entity.User;
import com.peng.saishi.entity.config.AppConfig;
import com.peng.saishi.fragment.Main_frag3;
import com.peng.saishi.interfaces.service.MyStringCallBack;
import com.peng.saishi.manager.NoDataManager;
import com.peng.saishi.utils.ActChangeUtils;
import com.peng.saishi.utils.DialogUtils;
import com.peng.saishi.utils.GlobeScopeUtils;
import com.peng.saishi.utils.JsonParser;
import com.peng.saishi.utils.SystemUtils;
import com.peng.saishi.utils.TimeUtils;
import com.peng.saishi.utils.ToastUtils;
import com.peng.saishi.widget.swipelistview.BaseSwipeListViewListener;
import com.peng.saishi.widget.swipelistview.SwipeListView;
import com.zhy.http.okhttp.OkHttpUtils;

public class JoinGroupAct extends BaseBackActivity {

	private List<JoinGroup> joins;
	private SwipeListView listview;
	private View nodata_layout;

	@Override
	public void init() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_joingroup);
		super.init();
		joins = getIntent().getParcelableArrayListExtra("joins");
		findViewById(R.id.History_btn).setOnClickListener(this);
		nodata_layout = NoDataManager.initNodata("没有任何的通知哦!", this);
		listview = (SwipeListView) findViewById(R.id.History_listview);
		OkHttpUtils.post().url(AppConfig.getApplys)
				.addParams("id", GlobeScopeUtils.getMeId() + "").build()
				.execute(new MyStringCallBack() {
					@Override
					public void onError(Call call, Exception e, int id) {

						ToastUtils.ShowNetError();
						findViewById(R.id.wait_bar).setVisibility(View.GONE);
					}

					@Override
					public void onResponse(String response, int id) {
						// TODO Auto-generated method stub

						// TODO Auto-generated method stub
						findViewById(R.id.wait_bar).setVisibility(View.GONE);
						if (id!=0) {
							ToastUtils.ShowNetError();
							return ;
						}
						try {
							JSONArray array = new JSONArray(response);
							joins = JsonParser.getListFromJson(array,
									JoinGroup.class);
							Collections.sort(joins,
									new Comparator<JoinGroup>() {

										@Override
										public int compare(JoinGroup lhs,
												JoinGroup rhs) {
											// TODO Auto-generated method stub
											if (TimeUtils
													.getDateFromChatTime(
															lhs.getTime())
													.before(TimeUtils
															.getDateFromChatTime(rhs
																	.getTime()))) {
												return 1;
											}
											return -1;
										}
									});
							listview.setAdapter(new JoinGroupAdapter(
									JoinGroupAct.this, joins));
							NoDataManager.ChangeState(nodata_layout, joins);
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}
				});
		listview.setOffsetLeft(SystemUtils.getAppWidth() - 280);
		listview.setOffsetRight(280);
		listview.setSwipeListViewListener(new BaseSwipeListViewListener() {
			@Override
			public void onClickFrontView(int position) {
				// TODO Auto-generated method stub

				// TODO Auto-generated method stub
				List<JoinGroup> list = ((JoinGroupAdapter) listview
						.getAdapter()).getJoins();
				JoinGroup info = list.get(position);
				if (info.getStatus() == 1) {

					// TODO Auto-generated method stub
					OkHttpUtils.post().url(AppConfig.getUser)
							.addParams("id", info.getPid() + "").build()
							.execute(new MyStringCallBack() {

								@Override
								public void onResponse(String response, int id) {
									// TODO Auto-generated method stub
									if (id!=0) {
										ToastUtils.ShowNetError();
										return ;
									}
									// TODO Auto-generated method stub
									try {
										JSONObject obj = new JSONObject(
												response);
										User user = JsonParser.getBeanFromJson(
												obj, User.class);
										ActChangeUtils.ChangeActivity(
												JoinGroupAct.this,
												SeePersonActivity.class,
												"user", user);
									} catch (JSONException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}

								}

								@Override
								public void onError(Call call, Exception e,
										int id) {
									// TODO Auto-generated method stub
									ToastUtils.ShowNetError();
								}
							});
				}

			}
		});

	}

	// 直接刷新
	public void UpdateUI() {
		OkHttpUtils.post().url(AppConfig.getApplys)
				.addParams("id", GlobeScopeUtils.getMeId() + "").build()
				.execute(new MyStringCallBack() {

					@Override
					public void onResponse(String response, int id) {
						// TODO Auto-generated method stub

						// TODO Auto-generated method stub
						if (id!=0) {
							ToastUtils.ShowNetError();
							return ;
						}
						try {
							JSONArray array = new JSONArray(response);
							joins = JsonParser.getListFromJson(array,
									JoinGroup.class);
							JoinGroupAdapter adapter = (JoinGroupAdapter) listview
									.getAdapter();
							Collections.sort(joins,
									new Comparator<JoinGroup>() {

										@Override
										public int compare(JoinGroup lhs,
												JoinGroup rhs) {
											// TODO Auto-generated method stub
											if (TimeUtils
													.getDateFromChatTime(
															lhs.getTime())
													.before(TimeUtils
															.getDateFromChatTime(rhs
																	.getTime()))) {
												return 1;
											}
											return -1;
										}
									});
							adapter.setJoins(joins);
							adapter.notifyDataSetChanged();
							NoDataManager.ChangeState(nodata_layout, joins);
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
					
					}
				});
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
		switch (v.getId()) {
		case R.id.History_btn:
			// 全部清空按钮
			final Dialog dialog = DialogUtils.createLoadingDialog(this,
					"清空中...");
			dialog.show();
			OkHttpUtils.post().url(AppConfig.ClearJoin).addParams("id", GlobeScopeUtils.getMeId()+"").build().execute(new MyStringCallBack() {
				
				@Override
				public void onResponse(String response, int id) {
					// TODO Auto-generated method stub

					// TODO Auto-generated method stub
					if (id!=0) {
						ToastUtils.ShowNetError();
						dialog.dismiss();
						return;
					}
					try {
						dialog.dismiss();
						JSONObject obj = new JSONObject(response);
						if (obj.getString("result").equals("ok")) {
							joins.clear();
							BaseAdapter adapter = (BaseAdapter) listview
									.getAdapter();
							adapter.notifyDataSetChanged();
							Message msg = new Message();
							msg.what = 1;
							Main_frag3.Update();
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				
				}
				
				@Override
				public void onError(Call call, Exception e, int id) {
					// TODO Auto-generated method stub
					dialog.dismiss();
					ToastUtils.ShowNetError();
				}
			});
			break;

		default:
			break;
		}
	}

}
