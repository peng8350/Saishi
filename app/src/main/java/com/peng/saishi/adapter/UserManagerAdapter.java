package com.peng.saishi.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import okhttp3.Call;
import org.json.JSONException;
import org.json.JSONObject;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.facebook.drawee.view.SimpleDraweeView;
import com.peng.saishi.R;
import com.peng.saishi.activity.MainActivity;
import com.peng.saishi.entity.MatchInfo;
import com.peng.saishi.entity.User;
import com.peng.saishi.entity.config.AppConfig;
import com.peng.saishi.interfaces.service.MyStringCallBack;
import com.peng.saishi.manager.AppManager;
import com.peng.saishi.utils.ActChangeUtils;
import com.peng.saishi.utils.DialogUtils;
import com.peng.saishi.utils.FrescoUtils;
import com.peng.saishi.utils.GlobeScopeUtils;
import com.peng.saishi.utils.JsonParser;
import com.peng.saishi.utils.ToastUtils;
import com.peng.saishi.widget.CustomDialog;
import com.zhy.http.okhttp.OkHttpUtils;

@SuppressLint("InflateParams")
public class UserManagerAdapter extends BaseAdapter {
	private Context context;
	private List<User> list;

	public UserManagerAdapter(Context context, List<User> list) {
		super();
		this.context = context;
		this.list = list;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list == null ? 0 : list.size();
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
			view = LayoutInflater.from(context).inflate(
					R.layout.item_usermanager, null);
			vh.name = (TextView) view.findViewById(R.id.textView1);
			vh.number = (TextView) view.findViewById(R.id.Seeperson_intro);
			vh.head = (SimpleDraweeView) view.findViewById(R.id.Seeperson_head);
			vh.tick = (ImageView) view.findViewById(R.id.imageView1);
			view.setTag(vh);
		} else {
			vh = (ViewHolder) view.getTag();

		}
		final User info = list.get(position);
		vh.name.setText(info.getUsername());
		vh.number.setText(info.getUser() + "");
		vh.tick.setVisibility(info.getId() == GlobeScopeUtils.getMeId() ? View.VISIBLE
				: View.GONE);
		FrescoUtils.Display(vh.head, AppConfig.Userpic_path + info.getId(), 80,
				80, false);
		view.setClickable(true);
		view.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (info.getId() == GlobeScopeUtils.getMeId()) {
					return;
				}
				CustomDialog.Builder builder = new CustomDialog.Builder(context);
				builder.setTitle("切换账号");
				builder.setMessage("你确定要切换当前选择的账号吗?");
				builder.setNegativeButton("取消",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								dialog.dismiss();
							}
						});
				builder.setPositiveButton("确定",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								dialog.dismiss();
								final Dialog wait_dialog = DialogUtils
										.createLoadingDialog(context, "正在切帐号..");
								wait_dialog.show();
								Map<String, String> params = new HashMap<>();
								params.put("user", info.getUser());
								params.put("pswd", info.getPswd());
								params.put("first", "false");
								OkHttpUtils.post().url(AppConfig.LOGIN)
										.params(params).build()
										.execute(new MyStringCallBack() {

											@Override
											public void onResponse(
													String response, int id) {
												// TODO Auto-generated method
												// stub
												try {
													wait_dialog.dismiss();
													if (id != 0) {
														ToastUtils
																.ShowNetError();
														return;
													}
													JSONObject res_obj = new JSONObject(
															response);
													User new_user = JsonParser.getBeanFromJson(
															res_obj.getJSONObject("user"),
															User.class);
													List<MatchInfo> params = MatchInfo.listAll(MatchInfo.class);
													new_user.setMatchs(params);
													new_user.setTeam1(JsonParser.getListFromJson(
															res_obj.getJSONArray("team1"),
															com.peng.saishi.entity.TeamInfo.class));
													new_user.setTeam2(JsonParser.getListFromJson(
															res_obj.getJSONArray("team2"),
															com.peng.saishi.entity.TeamInfo.class));
													new_user.setShoucangs(getUserLike(
															res_obj.getString("shoucangs"),
															params));
													GlobeScopeUtils.Put("ME",
															new_user);
													;
													AppManager.FinishAllAct();
													ActChangeUtils
															.ChangeActivity(
																	(Activity) context,
																	MainActivity.class,
																	new String[] {
																			"user",
																			"pswd" },
																	new String[] {
																			info.getUser(),
																			info.getPswd() });

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
												System.out.println(id);
												ToastUtils.ShowNetError();
												wait_dialog.dismiss();
											}
										});

							}
						});
				builder.create().show();
			}
		});
		return view;
	}

	public List<MatchInfo> getUserLike(String contain_str, List<MatchInfo> all) {
		List<MatchInfo> params = new ArrayList<>();
		for (MatchInfo info : all) {
			if (contain_str.contains(info.getId() + "")) {
				params.add(info);
			}
		}
		return params;
	}

	class ViewHolder {
		TextView name, number;
		SimpleDraweeView head;
		ImageView tick;
	}

}
