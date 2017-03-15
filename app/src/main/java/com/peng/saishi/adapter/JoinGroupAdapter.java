package com.peng.saishi.adapter;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.GetGroupMembersCallback;
import cn.jpush.im.android.api.model.UserInfo;
import cn.jpush.im.api.BasicCallback;

import com.facebook.drawee.view.SimpleDraweeView;
import com.peng.saishi.R;
import com.peng.saishi.activity.JoinGroupAct;
import com.peng.saishi.entity.JoinGroup;
import com.peng.saishi.entity.config.AppConfig;
import com.peng.saishi.interfaces.service.MyStringCallBack;
import com.peng.saishi.utils.DialogUtils;
import com.peng.saishi.utils.FrescoUtils;
import com.peng.saishi.utils.GlobeScopeUtils;
import com.peng.saishi.utils.ToastUtils;
import com.zhy.http.okhttp.OkHttpUtils;

public class JoinGroupAdapter extends BaseAdapter {
	private Context context;
	private List<JoinGroup> joins;

	public JoinGroupAdapter(Context context, List<JoinGroup> joins) {
		super();
		this.context = context;
		this.joins = joins;
	}

	public List<JoinGroup> getJoins() {
		return joins;
	}

	public void setJoins(List<JoinGroup> joins) {
		this.joins = joins;
	}


	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return joins==null?0: joins.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder vh;
		if (view == null) {
			vh = new ViewHolder();
			view = LayoutInflater.from(context).inflate(R.layout.item_joingroup, null);
			vh.btn1 = (Button) view.findViewById(R.id.item_joingroup_btn1);
			vh.btn2 = (Button) view.findViewById(R.id.item_joingroup_btn2);
			vh.content = (TextView) view.findViewById(R.id.item_joingroup_t2);
			vh.time = (TextView) view.findViewById(R.id.item_joingroup_t3);
			vh.name = (TextView) view.findViewById(R.id.item_joingroup_t1);
			vh.image = (SimpleDraweeView) view.findViewById(R.id.item_joingroup_riv);
			view.setTag(vh);
		} else {
			vh = (ViewHolder) view.getTag();
		}
		final JoinGroup info = joins.get(position);
		if (info.getStatus() == 3) {

			int imageid = GlobeScopeUtils.getMeId() == info.getUid() ? info.getPid() : info.getUid();
			FrescoUtils.Display(vh.image, AppConfig.Userpic_path+imageid, 100, 100,false);
		} else {
			FrescoUtils.Display(vh.image, AppConfig.Teanpic_path+info.getPicid(), 100, 100,false);
		}
		vh.time.setText(info.getTime());
		vh.btn1.setVisibility(info.getStatus() == 1 ? View.VISIBLE : View.GONE);
		vh.btn2.setVisibility(info.getStatus() == 1 ? View.VISIBLE : View.GONE);
		vh.content.setText(info.getContent());
		view.findViewById(R.id.item_joingroupback_btn1).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				OkHttpUtils.post().url(AppConfig.ClearOneJoin).addParams("id", info.getId() + "").build().execute(new MyStringCallBack() {
					
					@Override
					public void onResponse(String response, int id) {
						// TODO Auto-generated method stub
						if (id!=0) {
							ToastUtils.ShowNetError();
							return ;
						}
						joins.remove(info);
						notifyDataSetChanged();
					}
					
					@Override
					public void onError(Call call, Exception e, int id) {
						// TODO Auto-generated method stub
						ToastUtils.ShowNetError();
					}
				});
			}
		});
		vh.name.setText(info.getTeamname());
		if (info.getStatus() == 1) {

			vh.btn2.setOnClickListener(new OnClickListener() {

				@Override

				public void onClick(View v) {
					final Dialog wait_log = DialogUtils.createLoadingDialog(context, "稍后...");
					wait_log.show();
					JMessageClient.addGroupMembers(info.getGroupid(),
							Arrays.asList(new String[] { info.getTarget() + "" }), new BasicCallback() {

						@Override
						public void gotResult(int arg0, String arg1) {
							// TODO Auto-generated method stub
							if (arg0 == 0) {

								// TODO Auto-generated method stub
								// 接受他的加入
								JMessageClient.getGroupMembers(info.getGroupid(), new GetGroupMembersCallback() {

									@Override
									public void gotResult(int arg0, String arg1, List<UserInfo> arg2) {
										// TODO Auto-generated method stub
										String members = "";
										for (int i = 0; i < arg2.size(); i++) {
											if (i == arg2.size() - 1) {
												members += arg2.get(i).getUserName();
											} else {
												members += arg2.get(i).getUserName() + ",";
											}
										}
										Map<String, String> params = new HashMap<>();
										params.put("uid", info.getUid() + "");
										params.put("pid", info.getPid() + "");
										params.put("id",info.getId()+"");
										params.put("members", members);
										params.put("groupid", info.getGroupid() + "");
										OkHttpUtils.post().url(AppConfig.AgreeJoin).params(params).build().execute(new MyStringCallBack() {
											
											@Override
											public void onResponse(String response, int id) {
												// TODO Auto-generated method stub
												

												wait_log.dismiss();
												if (id!=0) {
													ToastUtils.ShowNetError();
													return ;
												}
												try {
													// TODO Auto-generated
													// method stubt
													new Thread(new Runnable() {
														public void run() {
															// 请写在线程运行的代码
															JMessageClient.sendMessage(JMessageClient
																	.createSingleTextMessage(info.getTarget(), "1"));
														}
													}).start();
													JSONObject obj = new JSONObject(response);
													if (obj.getString("result").equals("ok")) {
														JoinGroupAct act = (JoinGroupAct) context;
														act.UpdateUI();
													} else {
														ToastUtils.ShowShortToast(obj.getString("result"));
													}
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
												wait_log.dismiss();
											}
										});
									}
								});

							} else {
								wait_log.dismiss();
								ToastUtils.ShowShortToast("推送服务器异常");
							}
						}
					});
				}
			});
			vh.btn1.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					// 拒绝他的加入
					final Dialog wait_log = DialogUtils.createLoadingDialog(context, "稍后...");
					wait_log.show();
					Map<String, String> params = new HashMap<>();

					params.put("groupid", info.getGroupid() + "");
					params.put("uid", info.getUid() + "");
					params.put("pid", info.getPid() + "");
					OkHttpUtils.post().url(AppConfig.RefuseJoin).params(params).build().execute(new MyStringCallBack() {
						
						@Override
						public void onResponse(String response, int id) {
							try {
								wait_log.dismiss();
								if (id!=0) {
									ToastUtils.ShowNetError();
									return ;
								}
								// TODO Auto-generated method stub
								JSONObject obj = new JSONObject(response);
								if (obj.getString("result").equals("ok")) {
									JoinGroupAct act = (JoinGroupAct) context;
									act.UpdateUI();
								} else {
									ToastUtils.ShowShortToast(obj.getString("result"));
								}
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						
						@Override
						public void onError(Call call, Exception e, int id) {
							// TODO Auto-generated method stub
							wait_log.dismiss();
						}
					});
				}
			});
		}

		return view;
	}

	class ViewHolder {
		Button btn1, btn2;
		TextView name, content, time;
		SimpleDraweeView image;
	}

}
