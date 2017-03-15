package com.peng.saishi.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.Context;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.api.BasicCallback;

import com.peng.saishi.R;
import com.peng.saishi.activity.base.BaseBackActivity;
import com.peng.saishi.adapter.MemberAdapter;
import com.peng.saishi.entity.TeamInfo;
import com.peng.saishi.entity.User;
import com.peng.saishi.entity.config.AppConfig;
import com.peng.saishi.interfaces.service.MyStringCallBack;
import com.peng.saishi.utils.DialogUtils;
import com.peng.saishi.utils.ToastUtils;
import com.zhy.http.okhttp.OkHttpUtils;

public class MemberActivity extends BaseBackActivity {

	private GridView gridview;
	private TeamInfo team;
	private Button btn;
	private MemberAdapter adapter;
	private Vibrator vibrator;
	private boolean needupdate = false;
	private boolean instate = false;

	public boolean isInstate() {
		return instate;
	}

	public void setInstate(boolean instate) {
		this.instate = instate;
		adapter.setIsedit(instate);
		if (instate) {
			// 给客户端发送一个震动

			vibrator.vibrate(300);
		}
		btn.setText(instate ? "确定" : "移除");
		adapter.notifyDataSetChanged();
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_member);
		super.init();
		btn = (Button) findViewById(R.id.member_Btn);
		btn.setOnClickListener(this);
		btn.setVisibility(getIntent().getBooleanExtra("isowner", false) ? View.VISIBLE
				: View.GONE);
		vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		team = getIntent().getParcelableExtra("team");
		gridview = (GridView) findViewById(R.id.Member_gridView1);
		adapter = new MemberAdapter(this, team.getMembers());
		gridview.setAdapter(adapter);
	}

	// 检查存不存在被T用户
	public boolean checkkick(List<String> param, int id) {
		boolean flag = false;
		for (String s : param) {
			if (Integer.parseInt(s) == id) {
				flag = true;
				break;
			}
		}
		return flag;
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		if (needupdate)
			setResult(RESULT_OK);
		finish();
		super.onBackPressed();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.member_Btn:
			// 点击了,判断是不是在编辑
			if (instate) {

			}
			if (instate) {
				// 给客户端发送,请求服务器T人
				List<String> removes = adapter.getCheck_pos();
				if (removes.size() == 0) {
					setInstate(false);
					return;
				}
				final Dialog dialog = DialogUtils.createLoadingDialog(this,
						"踢人中...");
				dialog.show();
				final List<User> users = adapter.getList();
				final List<String> kick_list = new ArrayList<String>();// T了的用户的username集合
				final List<String> nokick_list = new ArrayList<String>();// 没有杯Ｔ用户的username集合

				for (int i = 0; i < users.size(); i++) {
					if (checkkick(removes, i)) {
						// 存在这个被T的用户
						kick_list.add(users.get(i).getUser());
					} else {
						nokick_list.add(users.get(i).getUser());
					}
				}

				// 推送服务端删除成功后
				// String teamid =
				// request.getParameter("teamid");
				// String nokickmember_param =
				// request.getParameter("nokickmember");
				// String kickmember_param =
				// request.getParameter("kickmember");
				String nokick = "", kick = "";
				for (int i = 0; i < nokick_list.size(); i++) {
					if (i == nokick_list.size() - 1) {
						nokick += nokick_list.get(i);
					} else {
						nokick += nokick_list.get(i) + ",";
					}
				}
				for (int i = 0; i < kick_list.size(); i++) {
					if (i == kick_list.size() - 1) {
						kick += kick_list.get(i);
					} else {
						kick += kick_list.get(i) + ",";
					}
				}

				Map<String, String> params = new HashMap<>();
				params.put("nokickmember", nokick);
				params.put("kickmember", kick);
				System.out.println(nokick);
				System.out.println(kick);
				params.put("teamid", team.getId() + "");
				OkHttpUtils.post().url(AppConfig.KickPerson).params(params)
						.build().execute(new MyStringCallBack() {

							@Override
							public void onResponse(String response, int id) {
								// TODO Auto-generated
								// method stub
							
								if (id != 0) {
									dialog.dismiss();
									ToastUtils.ShowNetError();
									return;
								}
								try {
									JSONObject obj = new JSONObject(response);
									if (obj.getString("result").equals("ok")) {
										// 请写在线程运行的代码

										JMessageClient.removeGroupMembers(
												team.getGroupid(), kick_list,
												new BasicCallback() {

													@Override
													public void gotResult(
															int arg0,
															String arg1) {
														// TODO Auto-generated
														// method stub
														dialog.dismiss();
														if (arg0 == 0) {
															for (int i = 0; i < kick_list
																	.size(); i++)
																users.remove(i);
															ToastUtils.ShowShortToast("移除成功");
															// TODO
															// Auto-generated
															// method stub
															adapter.notifyDataSetChanged();
															adapter.ClearState();
															needupdate = true;// 需要对条回去的act进行一个更新
														} else {
															ToastUtils
																	.ShowShortToast("操作失败,"
																			+ arg1);
															dialog.dismiss();
														}
													}
												});

									} else {
										dialog.dismiss();
										ToastUtils
												.ShowShortToast("删除失败,Sever不明原因");
									}
								} catch (JSONException e) {
									// TODO Auto-generated
									// catch block
									dialog.dismiss();
									e.printStackTrace();
								}
							}

							@Override
							public void onError(Call call, Exception e, int id) {
								// TODO Auto-generated
								// method stub
								ToastUtils.ShowNetError();
								dialog.dismiss();
							}
						});

			}
			setInstate(!instate);
			break;
		case R.id.back_tv:
			if (needupdate)
				setResult(RESULT_OK);
			finish();
			break;

		default:
			break;
		}
	}

}
