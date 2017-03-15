package com.peng.saishi.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.widget.TextView;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.CreateGroupCallback;

import com.peng.saishi.R;
import com.peng.saishi.activity.base.BaseBackActivity;
import com.peng.saishi.adapter.MyPagerAdapter;
import com.peng.saishi.entity.config.AppConfig;
import com.peng.saishi.fragment.AddGroup1Frag;
import com.peng.saishi.fragment.AddGroup2Frag;
import com.peng.saishi.fragment.AddGroup3Frag;
import com.peng.saishi.interfaces.observer.SaveLIstener;
import com.peng.saishi.interfaces.service.MyStringCallBack;
import com.peng.saishi.utils.DialogUtils;
import com.peng.saishi.utils.FIleUtils;
import com.peng.saishi.utils.GlobeScopeUtils;
import com.peng.saishi.utils.QRCodeUtils;
import com.peng.saishi.utils.ToastUtils;
import com.zhy.http.okhttp.OkHttpUtils;

public class AddGroupActivity extends BaseBackActivity implements
		OnPageChangeListener {
	private ViewPager pager;
	private List<Fragment> frags;
	private TextView title, finish;
	private String[] titles = { "选择一个比赛", "完善资料", "补充标签" };
	private String keys[] = { "id", "name", "matchname", "need", "intro",
			"yaoqiu", "plan", "target", "type", "matchid", "time" };
	private List<String> tags;// 组队的标签
	private String[] values = new String[11];
	public List<String> getTags() {
		return tags;
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		if (pager.getCurrentItem() != 0) {
			pager.setCurrentItem(pager.getCurrentItem() - 1);

		} else {
			finish();
			return;
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.back_tv:
			if (pager.getCurrentItem() != 0) {
				pager.setCurrentItem(pager.getCurrentItem() - 1);
			} else {
				finish();
				return;
			}
			break;
		case R.id.Addgroup_btn:
			// 点击完成后,弹出对话框等待,请求服务器插入数据,完成最后,跳到主界面
			UploadServer();
			break;

		default:
			break;
		}
	}

	public String[] getValues() {
		return values;
	}

	public void setValues(String[] values) {
		this.values = values;
	}

	@SuppressWarnings("deprecation")
	public void init() {
		setContentView(R.layout.activity_addgroup);
		super.init();
		pager = (ViewPager) findViewById(R.id.Addgroup_pager);
		title = (TextView) findViewById(R.id.Addgroup_title);
		finish = (TextView) findViewById(R.id.Addgroup_btn);
		tags = new ArrayList<String>();
		finish.setOnClickListener(this);
		finish.setVisibility(View.GONE);
		((TextView) findViewById(R.id.back_tv)).setText("主界面");
		title.setText(titles[0]);
		initfrags();
		pager.setAdapter(new MyPagerAdapter(getSupportFragmentManager(), frags));
		pager.setOnPageChangeListener(this);
		FIleUtils.saveBitmap(
				BitmapFactory.decodeResource(getResources(), R.drawable.pic3),
				AppConfig.pic_temp, null);
	};

	// 初始化fragment
	public void initfrags() {
		frags = new ArrayList<Fragment>();
		frags.add(new AddGroup1Frag());
		frags.add(new AddGroup2Frag());
		frags.add(new AddGroup3Frag(finish, tags));
	}

	// 下一步
	public void next() {
		pager.setCurrentItem(pager.getCurrentItem() + 1, true);
		title.setText(titles[pager.getCurrentItem()]);
	}

	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		// TODO Auto-generated method stub
		frags.get(1).onActivityResult(arg0, arg1, arg2);
	}

	// 上传到服务器添加组队信息
	public void UploadServer() {
		final Dialog load_dialog = DialogUtils.createLoadingDialog(
				AddGroupActivity.this, "上传信息中...");
		load_dialog.show();
		JMessageClient.createGroup(values[1], "没有", new CreateGroupCallback() {

			@Override
			public void gotResult(int arg0, String arg1, final long arg2) {
				// TODO Auto-generated method stub
				if (arg0 == 0) {
					final Map<String, String> params = new HashMap<>();
					for (int i = 0; i < keys.length; i++) {
						params.put(keys[i], values[i]);
					}
					params.put("owner", GlobeScopeUtils.getUser());

					String last_tags = "";
					for (int i = 0; i < tags.size(); i++) {
						if (i == tags.size() - 1) {
							last_tags += tags.get(i);
						} else
							last_tags += tags.get(i) + " ";
					}
					params.put("groupid", arg2 + "");
					params.put("tags", last_tags);

					FIleUtils.saveBitmap(QRCodeUtils.createQRImage("{'id':"
							+ arg2 + ",'owner':"+GlobeScopeUtils.getUser()+"}", 500, 500), AppConfig.code_temp,
							new SaveLIstener() {
							
								@Override
								public void OnSaveSuccess() {
									// TODO Auto-generated method stub
									// TODO Auto-generated method stub

									Map<String, File> filesMap = new HashMap<>();
									filesMap.put("pic", new File(
											AppConfig.pic_temp));
									filesMap.put("pic2", new File(
											AppConfig.code_temp));
									OkHttpUtils.post().url(AppConfig.AddTeam)
											.params(params).files("files", filesMap).build()
											.execute(new MyStringCallBack() {

												@Override
												public void onResponse(
														String response, int id) {
													// TODO Auto-generated
													// method stub

													// TODO Auto-generated
													// method stub
													if (id!=0) {
														load_dialog.dismiss();
														ToastUtils.ShowNetError();
														return ;
													}
													try {
														JSONObject res_obj = new JSONObject(
																response);

														if (res_obj.getString(
																"result")
																.equals("ok")) {
															// 已经新开了一个队伍
															ToastUtils
																	.ShowLongToast("新建队伍成功!!");
															setResult(RESULT_OK);
															finish();

														} else {
															ToastUtils
																	.ShowLongToast("新建队伍失败!!不明原因"
																			+ response);
														}
														load_dialog.dismiss();
													} catch (JSONException e) {
														// TODO Auto-generated
														// catch block
														e.printStackTrace();
													}

												}

												@Override
												public void onError(Call call,
														Exception e, int id) {
													// TODO Auto-generated
													// method stub

													// TODO Auto-generated
													// method stub
													ToastUtils
															.ShowShortToast("非法错误"+id);
													load_dialog.dismiss();

												}
											});

								}

								@Override
								public void OnSaveFailed() {
									// TODO Auto-generated method stub

								}
							});

				}
				else {
					ToastUtils.ShowShortToast("创建失败,原因:"+arg1);
					load_dialog.dismiss();
				}

			}
		});

	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageSelected(int arg0) {
		// TODO Auto-generated method stub
		title.setText(titles[arg0]);

		((TextView) findViewById(R.id.back_tv)).setText(arg0 == 0 ? "主界面"
				: "上一步");
	}
}
