package com.peng.saishi.adapter;

import java.io.File;

import okhttp3.Call;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.peng.saishi.R;
import com.peng.saishi.activity.AboutActivity;
import com.peng.saishi.activity.UserManagerActivity;
import com.peng.saishi.entity.config.AppConfig;
import com.peng.saishi.interfaces.service.MyStringCallBack;
import com.peng.saishi.utils.ActChangeUtils;
import com.peng.saishi.utils.DialogUtils;
import com.peng.saishi.utils.FrescoUtils;
import com.peng.saishi.utils.GlobeScopeUtils;
import com.peng.saishi.utils.PreferencesTool;
import com.peng.saishi.utils.ToastUtils;
import com.peng.saishi.widget.CustomDialog;
import com.peng.saishi.widget.SwitchButton.SwitchButton;
import com.zhy.http.okhttp.OkHttpUtils;

@SuppressLint("InflateParams")
public class SettingAdapter extends BaseExpandableListAdapter {
	String version = "";
	private Context context;
	ProgressDialog down_dia;

	public SettingAdapter(Context context) {
		super();
		this.context = context;
	}

	@Override
	public int getGroupCount() {
		// TODO Auto-generated method stub
		return 3;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		// TODO Auto-generated method stub
		return groupPosition == 0 ? 1 : 4;
	}

	@Override
	public Object getGroup(int groupPosition) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getGroupId(int groupPosition) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return false;
	}

	@SuppressLint("ResourceAsColor")
	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View view = new View(context);
		view.setLayoutParams(new AbsListView.LayoutParams(
				LayoutParams.MATCH_PARENT, 50));
		return view;
	}

	@SuppressLint("ResourceAsColor")
	@Override
	public View getChildView(int groupPosition, final int childPosition,
			boolean isLastChild, View view, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder vh;
		if (view == null) {
			vh = new ViewHolder();
			view = LayoutInflater.from(context).inflate(R.layout.item_setting,
					null);
			vh.content = (TextView) view
					.findViewById(R.id.item_setting_textView1);
			view.setTag(vh);
		} else {
			vh = (ViewHolder) view.getTag();
		}
		int realpos = 1 + (groupPosition == 0 ? childPosition
				: groupPosition == 1 ? childPosition + 1 : childPosition + 5);
		int draw_id = 0;
		int str = 0;
		try {
			draw_id = Integer.parseInt(R.drawable.class
					.getField("setting" + realpos).get(null).toString());
			str = Integer.parseInt(R.string.class.getField("setting" + realpos)
					.get(null).toString());
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Drawable drawable = context.getResources().getDrawable(draw_id);
		drawable.setBounds(0, 0, drawable.getMinimumWidth(),
				drawable.getMinimumHeight());
		vh.content.setCompoundDrawables(drawable, null, null, null);

		vh.content.setText(context.getString(str));

		// 添加自定义
		if (groupPosition == 0 && childPosition == 0) {
			// 添加circleimageview
			SimpleDraweeView user_iamge = new SimpleDraweeView(context);

			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
					90, 90);
			params.addRule(android.widget.RelativeLayout.ALIGN_PARENT_RIGHT);
			params.addRule(RelativeLayout.CENTER_VERTICAL);
			params.setMargins(0, 0, 40, 0);
			user_iamge.setLayoutParams(params);
			GenericDraweeHierarchyBuilder builder = new GenericDraweeHierarchyBuilder(
					context.getResources());
			RoundingParams round_param = new RoundingParams();
			round_param.setRoundAsCircle(true);
			GenericDraweeHierarchy hierarchy = builder
					.setPlaceholderImage(R.drawable.loadinguser)
					.setRoundingParams(round_param).build();
			user_iamge.setHierarchy(hierarchy);
			((RelativeLayout) view).addView(user_iamge);
			FrescoUtils.Display(user_iamge, AppConfig.Userpic_path
					+ GlobeScopeUtils.getMeId(), 100, 100, false);
			view.setClickable(true);
			view.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					ActChangeUtils.ChangeActivity((Activity) context,
							UserManagerActivity.class);
				}
			});
		} else if (groupPosition == 1) {
			SwitchButton switchButton = new SwitchButton(context);
			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			params.addRule(android.widget.RelativeLayout.ALIGN_PARENT_RIGHT);
			params.addRule(RelativeLayout.CENTER_VERTICAL);
			params.setMargins(0, 0, 20, 0);
			switchButton.setLayoutParams(params);
			switchButton
					.setChecked((boolean) (childPosition == 0 ? PreferencesTool
							.getParam("setting", "sound", Boolean.class, true)
							: childPosition == 1 ? PreferencesTool.getParam(
									"setting", "vriate", Boolean.class, false)
									: childPosition == 3 ? PreferencesTool
											.getParam("setting", "change",
													Boolean.class, false)
											: PreferencesTool.getParam(
													"setting", "icon",
													Boolean.class, true)));
			switchButton
					.setOnCheckedChangeListener(new OnCheckedChangeListener() {

						@Override
						public void onCheckedChanged(CompoundButton buttonView,
								boolean isChecked) {
							// TODO Auto-generated method stub
							if (childPosition == 0) {
								// 声音
								PreferencesTool.setParam("setting", "sound",
										isChecked);
							} else if (childPosition == 1) {
								// 震动
								PreferencesTool.setParam("setting", "vriate",
										isChecked);
							} else if (childPosition == 2) {
								// 通知图标
								PreferencesTool.setParam("setting", "icon",
										isChecked);
							} else {
								// 跟随头像变化
								PreferencesTool.setParam("setting", "change",
										isChecked);
								((Activity) context)
										.setResult(Activity.RESULT_OK);
							}

						}
					});
			((RelativeLayout) view).addView(switchButton);
		} else {
			ImageView forward = new ImageView(context);
			forward.setImageBitmap(BitmapFactory.decodeResource(
					context.getResources(), R.drawable.forward));
			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			params.addRule(android.widget.RelativeLayout.ALIGN_PARENT_RIGHT);
			params.addRule(RelativeLayout.CENTER_VERTICAL);
			params.setMargins(0, 0, 20, 0);
			forward.setLayoutParams(params);
			((RelativeLayout) view).addView(forward);
			view.setClickable(true);
			view.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if (childPosition == 0) {
						// 更新
						final Dialog dialog = DialogUtils.createLoadingDialog(
								context, "正在检查版本...");
						dialog.show();
						try {
							OkHttpUtils
									.post()
									.url(AppConfig.CheckVersion)
									.addParams(
											"version",
											context.getPackageManager()
													.getPackageInfo(
															context.getPackageName(),
															0).versionName)
									.build().execute(new MyStringCallBack() {

										@Override
										public void onResponse(String response,
												int id) {
											// TODO Auto-generated method stub
											System.out.println(response);
											if (id != 0) {
												dialog.dismiss();
												ToastUtils.ShowNetError();
												return;
											}
											// TODO Auto-generated method stub
											if (response.trim().equals("same".trim())) {
												CustomDialog.Builder builder = new CustomDialog.Builder(
														context);
												builder.setTitle("提示");
												builder.setMessage("当前已经是最新版本了,不需要更新!!!");
												builder.setPositiveButton(
														"关闭",
														new DialogInterface.OnClickListener() {

															@Override
															public void onClick(
																	DialogInterface dialog,
																	int which) {
																// TODO
																// Auto-generated
																// method stub
																dialog.dismiss();
															}
														});
												builder.create().show();

											} else {
												// 不相同时提示用户下载最新版本
												JSONObject obj;

												try {
													obj = new JSONObject(
															response);
													version = obj
															.getString("version");
												} catch (JSONException e) {
													// TODO Auto-generated catch
													// block
													e.printStackTrace();
												}

												CustomDialog.Builder builder = new CustomDialog.Builder(
														context);
												builder.setTitle("更新提示");
												builder.setMessage("系统发现你的当前版本与服务端的版本号并不是同一个版本,请问需要更新吗?");
												builder.setPositiveButton(
														"确定更新",
														new DialogInterface.OnClickListener() {

															@Override
															public void onClick(
																	DialogInterface dialog,
																	int which) {
																// TODO
																// Auto-generated
																// method stub
																dialog.dismiss();
																// 假如用户确定更新之后,弹出对话框

																// 对应ＵＲＬ的地方
																if (!new File(
																		AppConfig.APkLocal_path)
																		.exists()) {
																	new File(
																			AppConfig.APkLocal_path)
																			.mkdirs();
																}
																File local_file = new File(
																		AppConfig.APkLocal_path
																				+ "Saiban.apk");
																if (local_file
																		.exists()) {
																	local_file
																			.delete();
																}
																HttpHandler<File> httpHandler = new HttpUtils()
																		.download(
																				AppConfig.APK_PATH
																						+ "Saiban"
																						+ version
																						+ ".apk",
																				AppConfig.APkLocal_path
																						+ "Saiban.apk",
																				true,
																				true,
																				new RequestCallBack<File>() {

																					public void onLoading(
																							long total,
																							long current,
																							boolean isUploading) {
																						down_dia.setMax((int) total);
																						down_dia.setProgress((int) current);
																					};

																					public void onStart() {
																						ToastUtils
																								.ShowShortToast("文件正在下载中....");
																					};

																					@Override
																					public void onFailure(
																							HttpException arg0,
																							String arg1) {
																						// TODO
																						// Auto-generated
																						// method
																						// stub
																						ToastUtils
																								.ShowLongToast("文件下载失败");
																						down_dia.dismiss();
																						down_dia = null;

																					}

																					@Override
																					public void onSuccess(
																							ResponseInfo<File> arg0) {
																						// TODO
																						// Auto-generated
																						// method
																						// stub
																						final Handler handler = new Handler();
																						new Thread(
																								new Runnable() {
																									public void run() {
																										// 请写在线程运行的代码
																										try {
																											Thread.sleep(200);
																										} catch (InterruptedException e) {
																											// TODO
																											// Auto-generated
																											// catch
																											// block
																											e.printStackTrace();
																										}
																										handler.post(new Runnable() {

																											@Override
																											public void run() {
																												// TODO
																												// Auto-generated
																												// method
																												// stub
																												Intent intent = new Intent();
																												// 通过这个意图直接从当地更新APK
																												intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
																												intent.setAction(android.content.Intent.ACTION_VIEW);
																												intent.setDataAndType(
																														Uri.fromFile(new File(
																																AppConfig.APkLocal_path
																																		+ "Saiban.apk")),
																														"application/vnd.android.package-archive");
																												context.startActivity(intent);
																												down_dia.dismiss();
																												down_dia = null;
																											}
																										});
																									}
																								})
																								.start();

																					}
																				});
																down_dia = DialogUtils
																		.openDialog(
																				context,
																				httpHandler);
															}
														});
												builder.setNegativeButton(
														"取消",
														new DialogInterface.OnClickListener() {

															@Override
															public void onClick(
																	DialogInterface dialog,
																	int which) {
																// TODO
																// Auto-generated
																// method stub
																dialog.dismiss();
															}
														});
												builder.create().show();

											}
											dialog.dismiss();

										}

										@Override
										public void onError(Call call,
												Exception e, int id) {
											// TODO Auto-generated method stub
											ToastUtils.ShowNetError();
											dialog.dismiss();
										}
									});

						} catch (NameNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					} else if (childPosition == 1) {
						ImagePipeline pipeline = Fresco.getImagePipeline();
						pipeline.clearMemoryCaches();
						pipeline.clearDiskCaches();
						pipeline.clearCaches();
						ToastUtils.ShowShortToast("清空缓存成功");
					} else if (childPosition == 2) {
						// 关于app
						ActChangeUtils.ChangeActivity((Activity) context,
								AboutActivity.class);
					} else {
						// 退出
						DialogUtils.ExitApp(context);
					}
				}
			});
		}
		return view;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return false;
	}

	class ViewHolder {
		TextView content;
	}
}
