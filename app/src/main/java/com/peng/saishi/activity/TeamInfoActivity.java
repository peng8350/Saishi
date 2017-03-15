package com.peng.saishi.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.GetGroupInfoCallback;
import cn.jpush.im.android.api.callback.GetGroupMembersCallback;
import cn.jpush.im.android.api.model.GroupInfo;
import cn.jpush.im.android.api.model.UserInfo;
import cn.jpush.im.api.BasicCallback;

import com.facebook.drawee.view.SimpleDraweeView;
import com.peng.saishi.R;
import com.peng.saishi.activity.base.BaseBackActivity;
import com.peng.saishi.adapter.TeamLittleAdapter;
import com.peng.saishi.entity.MatchInfo;
import com.peng.saishi.entity.TeamInfo;
import com.peng.saishi.entity.User;
import com.peng.saishi.entity.config.AppConfig;
import com.peng.saishi.fragment.Main_frag3;
import com.peng.saishi.interfaces.service.MyStringCallBack;
import com.peng.saishi.manager.MatchManager;
import com.peng.saishi.utils.ActChangeUtils;
import com.peng.saishi.utils.DialogUtils;
import com.peng.saishi.utils.FrescoUtils;
import com.peng.saishi.utils.GlobeScopeUtils;
import com.peng.saishi.utils.JsonParser;
import com.peng.saishi.utils.ToastUtils;
import com.peng.saishi.widget.CustomDialog;
import com.peng.saishi.widget.HorizontalListView;
import com.peng.saishi.widget.TagCloudView;
import com.peng.saishi.widget.actionsheet.ActionSheet;
import com.peng.saishi.widget.actionsheet.ActionSheet.MenuItemClickListener;
import com.zhy.http.okhttp.OkHttpUtils;

public class TeamInfoActivity extends BaseBackActivity implements
		OnItemClickListener, MenuItemClickListener {
	private TextView content1, content2, content3, content4, header_tv1,
			header_tv2, header_tv3;
	private HorizontalListView listview;
	private SimpleDraweeView team_head;
	private ImageView team_code;
	private TagCloudView tagcloud;
	private TeamInfo info;
	private boolean haveme = false;
	private boolean isowner = false;
	String members = "";
	public static final int REMOVESUCCESS = 555;

	public static final int UpdateTeam = 554;

	@Override
	public void init() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_teaminfo);
		super.init();
		// 是否
		setTheme(R.style.ActionSheetStyleIOS7);

		info = getIntent().getParcelableExtra("team");
		team_head = (SimpleDraweeView) findViewById(R.id.teaminfo_imageview1);
		listview = (HorizontalListView) findViewById(R.id.teaminfo_listview);
		tagcloud = (TagCloudView) findViewById(R.id.teaminfo_tagcloudview);
		content1 = (TextView) findViewById(R.id.teaminfo_tv1);
		content2 = (TextView) findViewById(R.id.teaminfo_tv2);
		content3 = (TextView) findViewById(R.id.teaminfo_tv3);
		team_code = (ImageView) findViewById(R.id.teaminfo_imageview2);
		content4 = (TextView) findViewById(R.id.teaminfo_tv4);
		header_tv1 = (TextView) findViewById(R.id.teaminfo_headertv1);
		header_tv2 = (TextView) findViewById(R.id.teaminfo_headertv2);
		header_tv3 = (TextView) findViewById(R.id.teaminfo_headertv3);
		team_code.setOnClickListener(this);

		findViewById(R.id.Teaminfo_more).setOnClickListener(this);
		team_head.setOnClickListener(this);
		setDataInfo();
		findViewById(R.id.teaminfo_showperson).setOnClickListener(this);
		header_tv2.setOnClickListener(this);
		listview.setOnItemClickListener(this);
		List<String> tags = new ArrayList<>();
		for (String str : info.getTags().split(" ")) {
			tags.add(str);
		}
		tagcloud.setTags(tags);

		JMessageClient.getGroupMembers(info.getGroupid(),
				new GetGroupMembersCallback() {

					@Override
					public void gotResult(int arg0, String arg1,
							List<UserInfo> arg2) {
						// TODO Auto-generated method stub
						List<UserInfo> users = arg2;
						if (arg0 == 0) {

							for (int i = 0; i < users.size(); i++) {
								if (users.get(i).getUserName()
										.equals(GlobeScopeUtils.getUser())) {
									haveme = true;
								}
								if (i == users.size() - 1) {
									members += users.get(i).getUserName();
								} else {
									members += users.get(i).getUserName() + ",";

								}
							}
							List<User> db_users = User.findWithQuery(
									User.class,
									"select * from User where user in ("+members+")"
							);

							if (members.equals("")) {
								return;
							}
							if (db_users.size()==users.size()) {
								info.setMembers(db_users);
								listview.setAdapter(new TeamLittleAdapter(
														TeamInfoActivity.this,
														db_users));
								team_code
								.setVisibility(haveMe(db_users) ? View.VISIBLE
										: View.GONE);
								return;
							}
							initMember(members);
							
						}

					}

					
				});
		System.out.println(info.getOwner());
		isowner = GlobeScopeUtils.getUser().equals(info.getOwner());
	}
	
	private boolean haveMe(List<User> db_users) {
		// TODO Auto-generated method stub
		for(User u:db_users){
			if (u.getId()==GlobeScopeUtils.getMeId()) {
				return true;
			}
		}
		return false;
	}

	
	public void initMember(final String members) {
		Map<String, String> params = new HashMap<>();
		params.put("members", members);
		OkHttpUtils.post().url(AppConfig.getTeamMember).params(params).build()
				.execute(new MyStringCallBack() {

					@Override
					public void onResponse(String response, int id) {
						// TODO Auto-generated method
						// stub
						if (id != 0) {
							ToastUtils.ShowNetError();
							return;
						}
						// TODO Auto-generated method
						// stub
						try {
							JSONArray array = new JSONArray(response);
							List<User> list = JsonParser.getListFromJson(array,
									User.class);
							User.saveInTx(list);
							listview.setAdapter(new TeamLittleAdapter(
									TeamInfoActivity.this, list));
							info.setMembers(list);
							team_code
							.setVisibility(haveMe(list) ? View.VISIBLE
									: View.GONE);
						} catch (JSONException e) {
							// TODO Auto-generated catch
							// block
							e.printStackTrace();
						}

					}

					@Override
					public void onError(Call call, Exception e, int id) {
						// TODO Auto-generated method
						// stub
						ToastUtils.ShowNetError();
					}
				});
	}

	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		// TODO Auto-generated method stub
		if (arg0 == REMOVESUCCESS && arg1 == RESULT_OK) {
			// 用户被更新的时候刷新
			JMessageClient.getGroupMembers(info.getGroupid(),
					new GetGroupMembersCallback() {

						@Override
						public void gotResult(int arg0, String arg1,
								List<UserInfo> arg2) {
							// TODO Auto-generated method stub

							if (arg0 == 0) {
								for (int i = 0; i < arg2.size(); i++) {
									if (arg2.get(i).getUserName()
											.equals(GlobeScopeUtils.getUser())) {
										haveme = true;
									}
									if (i == arg2.size() - 1) {
										members += arg2.get(i).getUserName();
									} else {
										members += arg2.get(i).getUserName()
												+ ",";

									}
								}

								if (members.equals("")) {
									return;
								}
								Map<String, String> params = new HashMap<>();
								params.put("members", members);
								OkHttpUtils.post().url(AppConfig.getTeamMember)
										.params(params).build()
										.execute(new MyStringCallBack() {

											@Override
											public void onResponse(
													String response, int id) {
												// TODO Auto-generated method
												// stub
												if (id != 0) {
													ToastUtils.ShowNetError();
													return;
												}
												// TODO Auto-generated method
												// stub
												try {
													JSONArray array = new JSONArray(
															response);

													List<User> list = JsonParser
															.getListFromJson(
																	array,
																	User.class);
													listview.setAdapter(new TeamLittleAdapter(
															TeamInfoActivity.this,
															list));
													info.setMembers(list);
												} catch (JSONException e) {
													// TODO Auto-generated catch
													// block
													e.printStackTrace();
												}

											}

											@Override
											public void onError(Call call,
													Exception e, int id) {
												// TODO Auto-generated method
												// stub
												ToastUtils.ShowNetError();
											}
										});
							}
						}
					});
		}
		if (arg0 == UpdateTeam && arg1 == RESULT_OK) {
			// 更新队伍的资料
			Map<String, String> params = new HashMap<>();
			params.put("id", info.getId() + "");
			OkHttpUtils.post().url(AppConfig.getTeam).params(params).build()
					.execute(new MyStringCallBack() {

						@Override
						public void onResponse(String response, int id) {
							// TODO Auto-generated method stub
							if (id != 0) {
								ToastUtils.ShowNetError();
								return;
							}
							try {
								JSONObject obj = new JSONObject(response);
								info = JsonParser.getBeanFromJson(
										obj.getJSONObject("object"),
										TeamInfo.class);
								setDataInfo();
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
		}
	}

	// 为javabean设置数据进去
	private void setDataInfo() {
		// TODO Auto-generated method stub
		content1.setText(info.getIntro());
		content2.setText(info.getYaoqiu());
		content3.setText(info.getPlan());
		content4.setText(info.getTarget());
		header_tv1.setText(info.getName());
		header_tv2.setText(info.getMatchName());
		header_tv3.setText("比赛类型:\t\t\t" + info.getType() + "\n\n比赛时间:\t\t\t"

		+ info.getTime());

		FrescoUtils.Display(team_head, AppConfig.Teanpic_path + info.getId(),
				200, 200, true);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_MENU
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			onClick(findViewById(R.id.Teaminfo_more));
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
		switch (v.getId()) {
		case R.id.teaminfo_imageview2:

			Intent inte = new Intent(this, CodeShowActivity.class);

			inte.putExtra("info", info);
			startActivity(inte);
			break;
		case R.id.Teaminfo_more:
			ActionSheet sheet = new ActionSheet(this);
			sheet.setTitle("更多");
			sheet.setCancelButtonTitle("关闭");
			if (haveme && isowner) {
				sheet.addItems(new String[] { "查看比赛详情", "修改队伍信息" });
			} else if (!isowner && haveme) {
				sheet.addItems(new String[] { "查看比赛详情", "退出这个队伍" });
			} else {
				sheet.addItems(new String[] { "申请进入这个队伍", "查看比赛详情" });
			}
			sheet.setItemClickListener(this);

			sheet.showMenu();
			break;
		case R.id.teaminfo_showperson:
			// 显示群成员
			Intent intent = new Intent(this, MemberActivity.class);
			intent.putExtra("team", info);
			intent.putExtra("isowner", isowner);
			startActivityForResult(intent, REMOVESUCCESS);
			break;
		case R.id.teaminfo_headertv2:
			// 点击了进入比赛的介绍
			break;
		case R.id.teaminfo_imageview1:
			ActChangeUtils.ChangeActivity(this, PhotoActivity.class,
					new String[] { "type", "id" }, info.getId() + "", 2);
			break;
		default:
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		Intent intent = new Intent(this, SeePersonActivity.class);
		intent.putExtra("isme", false);
		intent.putExtra("user", info.getMembers().get(position));
		startActivity(intent);
	}

	@Override
	public void onItemClick(int itemPosition) {
		// TODO Auto-generated method stub
		if (haveme && isowner) {
			if (itemPosition == 0) {

				MatchInfo target = MatchManager.getMatchById(info.getId());
				ActChangeUtils.ChangeActivity(this, MatchActivity.class,
						"match", target);
			} else {
				Intent intent = new Intent(this, UpdateTeamAct.class);
				intent.putExtra("team", info);
				startActivityForResult(intent, UpdateTeam);
			}
		} else if (!isowner && haveme) {
			if (itemPosition == 0) {
				MatchInfo target = MatchManager.getMatchById(info.getId());
				ActChangeUtils.ChangeActivity(this, MatchActivity.class,
						"match", target);
			} else {

				// 用户退出群组
				CustomDialog.Builder builder = new CustomDialog.Builder(this);
				builder.setTitle("提示");
				builder.setMessage("你确定要退出这个队伍吗?");
				builder.setPositiveButton("确定",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								final Dialog wait_log = DialogUtils
										.createLoadingDialog(
												TeamInfoActivity.this, "退出中...");
								wait_log.show();
								JMessageClient.exitGroup(info.getGroupid(),
										new BasicCallback() {

											@Override
											public void gotResult(int arg0,
													String arg1) {
												// TODO Auto-generated method
												// stub
												if (arg0 == 0) {
													Map<String, String> params = new HashMap<>();
													params.put("teamid",
															info.getId() + "");
													params.put(
															"id",
															GlobeScopeUtils
																	.getMeId()
																	+ "");
													params.put("member",
															members);
													OkHttpUtils
															.post()
															.url(AppConfig.ExitTeam)
															.params(params)
															.build()
															.execute(
																	new MyStringCallBack() {

																		@Override
																		public void onResponse(
																				String response,
																				int id) {
																			// TODO
																			// Auto-generated
																			// method
																			// stub
																			if (id != 0) {
																				ToastUtils
																						.ShowNetError();
																				return;
																			}
																			// TODO
																			// Auto-generated
																			// method
																			// stub
																			wait_log.dismiss();
																			try {
																				JSONObject res_obj = new JSONObject(
																						response);
																				if (res_obj
																						.getString(
																								"result")
																						.equals("ok")) {
																					ToastUtils
																							.ShowShortToast("退出队伍成功!");
																					JMessageClient
																							.deleteGroupConversation(info
																									.getGroupid());
																					setResult(RESULT_OK);
																					finish();
																					Message s = new Message();
																					s.what = 1;
																					Main_frag3.handler
																							.sendMessage(s);
																				} else {
																					ToastUtils
																							.ShowShortToast("退出队伍失败,服务端异常");
																				}
																			} catch (JSONException e) {
																				// TODO
																				// Auto-generated
																				// catch
																				// block
																				e.printStackTrace();
																			}

																		}

																		@Override
																		public void onError(
																				Call call,
																				Exception e,
																				int id) {
																			// TODO
																			// Auto-generated
																			// method
																			// stub
																			wait_log.dismiss();
																			ToastUtils
																					.ShowNetError();
																		}
																	});
												} else {
													wait_log.dismiss();
													ToastUtils.ShowLongToast("退出失败,不明原因");
												}
											}
										});
								dialog.dismiss();
							}
						});
				builder.setNegativeButton("取消",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								dialog.dismiss();
							}
						});
				builder.create().show();

			}
		} else {
			if (itemPosition == 1) {
				MatchInfo target = MatchManager.getMatchById(info.getId());
				ActChangeUtils.ChangeActivity(this, MatchActivity.class,
						"match", target);
			} else {
				// 申请加入
				if (info.getNeedperson() == info.getNowperson()) {
					ToastUtils.ShowShortToast("这个队伍已经满人了!");
					return;
				}
				CustomDialog.Builder builder = new CustomDialog.Builder(this);
				builder.setTitle("申请加入");
				final EditText eText = new EditText(this);
				eText.setHint("请输入你的附加内容,比如,我是....");
				eText.setTextSize(14);
				eText.setBackgroundResource(R.drawable.update_et_bg);
				eText.requestFocus();
				builder.setContentView(eText);
				builder.setPositiveButton("申请", new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						final Map<String, String> params = new HashMap<>();
						params.put("pid", GlobeScopeUtils.getMeId() + "");
						JMessageClient.getGroupInfo(info.getGroupid(),
								new GetGroupInfoCallback() {

									@Override
									public void gotResult(int arg0,
											String arg1, GroupInfo arg2) {
										// TODO Auto-generated method stub
										params.put("uid", arg2.getGroupOwner());
										params.put("groupid", info.getGroupid()
												+ "");
										params.put("target",
												GlobeScopeUtils.getUser());
										params.put("teamname", info.getName());
										params.put("picid", info.getId() + "");
										params.put("content",
												GlobeScopeUtils.getUserName()
														+ ":"
														+ eText.getText()
																.toString());
										OkHttpUtils
												.post()
												.url(AppConfig.ApplyMember)
												.params(params)
												.build()
												.execute(
														new MyStringCallBack() {

															@Override
															public void onResponse(
																	String response,
																	int id) {
																// TODO
																// Auto-generated
																// method stub
																if (id != 0) {
																	ToastUtils
																			.ShowNetError();
																	return;
																}
																try {
																	JSONObject obj = new JSONObject(
																			response);
																	if (obj.getString(
																			"result")
																			.equals("ok")) {

																		ToastUtils
																				.ShowShortToast("发送成功!!");
																	} else {
																		ToastUtils
																				.ShowShortToast(obj
																						.getString("result"));
																	}
																} catch (JSONException e) {
																	// TODO
																	// Auto-generated
																	// catch
																	// block
																	e.printStackTrace();
																}
															}

															@Override
															public void onError(
																	Call call,
																	Exception e,
																	int id) {
																// TODO
																// Auto-generated
																// method stub
																ToastUtils
																		.ShowNetError();
															}
														});
										;
									}
								});
						dialog.dismiss();
					}
				});
				builder.setNegativeButton("取消", new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						dialog.dismiss();
					}
				});
				builder.create().show();

			}
		}
	}
}
