package com.peng.saishi.activity;

import android.annotation.SuppressLint;
import android.app.ActionBar.LayoutParams;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.*;
import android.content.DialogInterface.OnClickListener;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.*;
import android.widget.PopupWindow.OnDismissListener;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.content.EventNotificationContent;
import cn.jpush.im.android.api.content.EventNotificationContent.EventNotificationType;
import cn.jpush.im.android.api.content.TextContent;
import cn.jpush.im.android.api.enums.ContentType;
import cn.jpush.im.android.api.enums.ConversationType;
import cn.jpush.im.android.api.event.LoginStateChangeEvent;
import cn.jpush.im.android.api.event.MessageEvent;
import cn.jpush.im.api.BasicCallback;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationClientOption.AMapLocationMode;
import com.amap.api.location.AMapLocationListener;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.NotificationTarget;
import com.jpeng.jptabbar.JPTabBar;
import com.jpeng.jptabbar.OnTabSelectListener;
import com.jpeng.jptabbar.anno.NorIcons;
import com.jpeng.jptabbar.anno.SeleIcons;
import com.jpeng.jptabbar.anno.Titles;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.peng.saishi.R;
import com.peng.saishi.activity.base.BaseMainActivity;
import com.peng.saishi.adapter.MyPagerAdapter;
import com.peng.saishi.entity.MatchInfo;
import com.peng.saishi.entity.NoticeInfo;
import com.peng.saishi.entity.TeamInfo;
import com.peng.saishi.entity.User;
import com.peng.saishi.entity.config.AppConfig;
import com.peng.saishi.fragment.*;
import com.peng.saishi.interfaces.observer.AlarmListener;
import com.peng.saishi.interfaces.observer.BadgeListener;
import com.peng.saishi.interfaces.service.MyStringCallBack;
import com.peng.saishi.manager.App;
import com.peng.saishi.manager.AppManager;
import com.peng.saishi.receiver.MyAlarmReceiver;
import com.peng.saishi.utils.*;
import com.peng.saishi.utils.animation.GlideCircleTransform;
import com.peng.saishi.utils.animation.GlideRoundTransform;
import com.peng.saishi.widget.BadgeView;
import com.peng.saishi.widget.CustomDialog;
import com.peng.saishi.widget.SegmentView;
import com.peng.saishi.widget.SegmentView.onSegmentViewClickListener;
import com.zhy.http.okhttp.OkHttpUtils;
import okhttp3.Call;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.*;

@SuppressLint({ "NewApi", "InflateParams" })
public class MainActivity extends BaseMainActivity implements

		onSegmentViewClickListener, BadgeListener, OnDismissListener,
		AlarmListener, AMapLocationListener,OnTabSelectListener {
	public static final int Addgroup_requst = 10001;
	private ViewPager pager;
	private JPTabBar mBar;
	private ImageButton btn1;
	private RadioButton[] radios;
	private TextView title_tv;
	private SegmentView seg;
	private PopupWindow menu;
	public static int QRCODE_REQUSTCODE = 999;
	@ViewInject(R.id.ImageButton2)
	private ImageButton topleft_btn;
	private MyAlarmReceiver alarmReceiver;

	@ViewInject(R.id.main_topbadge)
	private BadgeView top_badge;

	private AMapLocationClient mLocationClient;
	@Titles
	private static final int[] TITLES = {R.string.Main_Tab1,R.string.Main_Tab2,R.string.Main_Tab3,R.string.Main_Tab4};
	@NorIcons
	private static final int[] NORMAL_ICONS = {R.drawable.tab1_normal,R.drawable.tab2_normal,R.drawable.tab3_normal,R.drawable.tab4_normal};
	@SeleIcons
	private static final int[] SELECTED_ICONS={R.drawable.tab1_selected,R.drawable.tab2_selected,R.drawable.tab3_selected,R.drawable.tab4_selected};


	@OnClick(R.id.ImageButton2)
	public void ClickNoticeIcon(View v) {

		// 点击了通知图标,打开activity
		ActChangeUtils.ChangeActivity(this, NoticeActivity.class);
		top_badge.setVisibility(View.GONE);

	}

	private void initGaode() {
		// TODO Auto-generated method stub
		// 声明AMapLocationClient类对象
		// 初始化定位

		mLocationClient = new AMapLocationClient(getApplicationContext());
		// 设置定位回调监听
		mLocationClient.setLocationListener(this);
		// 设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式。
		// 初始化AMapLocationClientOption对象
		AMapLocationClientOption mLocationOption = new AMapLocationClientOption();
		// 设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
		mLocationOption.setLocationMode(AMapLocationMode.Hight_Accuracy);
		// 设置定位间隔,单位毫秒,默认为2000ms
		mLocationOption.setInterval(10000);
		// 设置定位参数
		// 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
		// 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
		// 在定位结束后，在合适的生命周期调用onDestroy()方法
		// 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
		// 启动定位
		mLocationClient.setLocationOption(mLocationOption);
		mLocationClient.startLocation();

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.menu_textView1:
			// 点击菜单1
			ActChangeUtils.ChangeActivityForResult(this,
					AddGroupActivity.class, MainActivity.Addgroup_requst);
			menu.dismiss();
			break;
		case R.id.menu_textView2:
			// 点击菜单２
			ActChangeUtils.ChangeActivity(this, QrCodeActivity.class);
			menu.dismiss();
			break;
		case R.id.menu_textView3:
			// 点击菜单３
			ActChangeUtils.ChangeActivity(this, HelpActivity.class);
			menu.dismiss();
			break;
		case R.id.menu_textView4:
			// 点击菜单4
			// 分享
			ShareAppUtils.showShare(this);
			menu.dismiss();
			break;
		case R.id.Login_more:
			// 弹出popwindow
			if (menu == null) {
				menu = new PopupWindow(LayoutParams.WRAP_CONTENT,
						LayoutParams.WRAP_CONTENT);
				menu.setBackgroundDrawable(new ColorDrawable(
						getResources().getColor(android.R.color.transparent)));
				menu.setOnDismissListener(this);
				menu.setFocusable(true);
				View menu_layout = LayoutInflater.from(this).inflate(
						R.layout.view_menu, null);
				menu_layout.findViewById(R.id.menu_textView1)
						.setOnClickListener(this);
				menu_layout.findViewById(R.id.menu_textView2)
						.setOnClickListener(this);
				menu_layout.findViewById(R.id.menu_textView3)
						.setOnClickListener(this);
				menu_layout.findViewById(R.id.menu_textView4)
						.setOnClickListener(this);
				menu.setContentView(menu_layout);

			}
			if (menu.isShowing()) {
				menu.dismiss();
			} else {
				backgroundAlpha(0.4f);
				menu.showAsDropDown(btn1, -100, 0);
			}
			break;

		default:
			break;
		}
	}

	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		// TODO Auto-generated method stub
		super.onActivityResult(arg0, arg1, arg2);
		if (arg0 == Addgroup_requst && arg1 == RESULT_OK) {
			pagers.get(2).onActivityResult(arg0, arg1, arg2);
		}
		if (arg0 == QRCODE_REQUSTCODE && arg1 == RESULT_OK && arg2 != null) {
			if (arg2.getIntExtra("type", 0) == 1) {

				Bundle bundle = arg2.getExtras();
				// ��ʾɨ�赽������
				ActChangeUtils.ChangeActivity(this, WebActivity.class, "url",
						bundle.getString("result"));
			} else {
				Message s = new Message();
				s.what = 1;
				Main_frag3.handler.sendMessage(s);
			}
		}
		if (arg0 == Main_frag4.UPDATE && arg1 == RESULT_OK) {
			((Main_frag4) pagers.get(3)).onActivityResult(arg0, arg1, arg2);
		}
		if (arg0 == Main_frag5.UPDATEICON && arg1 == RESULT_OK) {
			((Main_frag5) pagers.get(4)).onActivityResult(arg0, arg1, arg2);
		}
	}

	@SuppressWarnings("deprecation")
	private void ShowNotication(MessageEvent event) {
		// TODO Auto-generated method stub
		// PreferencesTool.setParam("setting", "sound", isChecked);
		// } else if (childPosition == 1) {
		// // 震动
		// PreferencesTool.setParam("setting", "vriate", isChecked);
		if (!event.getMessage().getContentType().equals(ContentType.text)) {
			return;
		}
		int teamid = 0;
		int type = 0;
		boolean issound = (Boolean) PreferencesTool.getParam("setting",
				"sound", Boolean.class, true);
		boolean isvriate = (Boolean) PreferencesTool.getParam("setting",
				"vriate", Boolean.class, false);
		String content = null;
		String teamname = null;
		try {
			JSONObject obj = new JSONObject(((TextContent) event.getMessage()
					.getContent()).getText());
			teamid = obj.getInt("teamid");
			content = obj.getString("content");
			teamname = obj.getString("teamname");
			type = obj.getInt("type");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (SystemUtils.isForeground(this,
				"com.peng.saishi.activity.ChatActivity")
				&& GlobeScopeUtils.getConver() == teamid) {
			return;
		}
		NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		final Notification noti = new Notification(R.drawable.notiicon,
				type == 1 ? content : type == 2 ? "[图片]" : type == 3 ? "[语音]''"
						: "'[文件]'", 0);
		noti.defaults |= Notification.DEFAULT_LIGHTS;
		if (issound) {
			noti.sound = Uri.parse("android.resource://com.peng.saishi/"
					+ R.raw.noti);
		}
		if (isvriate) {
			noti.defaults |= Notification.DEFAULT_VIBRATE;
		}

		final RemoteViews remoteViews = new RemoteViews(getPackageName(),
				R.layout.view_notice);
		remoteViews.setImageViewResource(R.id.notice_rv,
				R.drawable.loadinggroup);
		remoteViews.setTextViewText(R.id.notice_name, teamname);
		remoteViews.setTextViewText(R.id.notice_time, TimeUtils
				.dateToStrLong(new Date(event.getMessage().getCreateTime())));
		if (type == 1) {

			remoteViews.setTextViewText(R.id.notice_content, content);
		} else if (type == 2) {
			remoteViews.setTextViewText(R.id.notice_content, "[图片]");
		} else if (type == 3) {
			remoteViews.setTextViewText(R.id.notice_content, "[语音]''");
		} else {
			remoteViews.setTextViewText(R.id.notice_content, "[文件]''");
		}
		noti.contentView = remoteViews;
		Intent inten = new Intent(this, ChatActivity.class);
		TeamInfo target = getTarget(teamid);

		inten.putExtra("team", target);
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0, inten,
				0);
		noti.flags = Notification.FLAG_AUTO_CANCEL;
		noti.contentIntent = contentIntent;
		manager.notify(0, noti);
		NotificationTarget noti_target = new NotificationTarget(this,
				remoteViews, R.id.notice_rv, noti, 0);
		Glide.with(getApplicationContext())
				.load(AppConfig.Teanpic_path + teamid).asBitmap().centerCrop()
				.placeholder(R.drawable.loadinggroup)
				.transform(new GlideCircleTransform(this)).dontAnimate()
				.into(noti_target);
	}

	@SuppressWarnings({ "deprecation" })
	public void ShowNotce(NoticeInfo info, MatchInfo match) {
		boolean issound = (Boolean) PreferencesTool.getParam("setting",
				"sound", Boolean.class, true);
		boolean isvriate = (Boolean) PreferencesTool.getParam("setting",
				"vriate", Boolean.class, false);
		NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		final Notification noti = new Notification(R.drawable.notiicon,
				info.getContent(), 0);
		noti.defaults |= Notification.DEFAULT_LIGHTS;
		if (issound) {
			noti.sound = Uri.parse("android.resource://com.peng.saishi/"
					+ R.raw.noti);
		}
		if (isvriate) {
			noti.defaults |= Notification.DEFAULT_VIBRATE;
		}

		final RemoteViews remoteViews = new RemoteViews(getPackageName(),
				R.layout.view_notice);
		remoteViews.setTextViewText(R.id.notice_name, info.getName());
		remoteViews.setTextViewText(R.id.notice_time,
				TimeUtils.dateToStrLong(new Date(System.currentTimeMillis())));
		remoteViews.setImageViewResource(R.id.notice_rv, R.drawable.loading1);
		remoteViews.setTextViewText(R.id.notice_content, info.getContent());
		noti.contentView = remoteViews;
		Intent inten = new Intent(this, MatchActivity.class);
		inten.putExtra("match", match);
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0, inten,
				0);
		noti.flags = Notification.FLAG_AUTO_CANCEL;
		noti.contentIntent = contentIntent;
		manager.notify(0, noti);
		NotificationTarget target = new NotificationTarget(this, remoteViews,
				R.id.notice_rv, noti, 0);
		Glide.with(getApplicationContext())
				.load(AppConfig.Matchpic_path + match.getId()).asBitmap()
				.centerCrop().placeholder(R.drawable.loadinggroup)
				.transform(new GlideRoundTransform(App.getInstance(), 40))
				.dontAnimate().into(target);
	}

	public TeamInfo getTarget(int teamid) {
		for (TeamInfo t : GlobeScopeUtils.getUserTeam1()) {
			if (teamid == t.getId()) {
				return t;
			}
		}
		for (TeamInfo t1 : GlobeScopeUtils.getUserTeam2()) {
			if (teamid == t1.getId()) {
				return t1;
			}
		}
		return null;
	}

	public void onEventMainThread(LoginStateChangeEvent event) {
		LoginStateChangeEvent.Reason reason = event.getReason();// 获取变更的原因
		switch (reason) {
		case user_password_change:
			// 用户密码在服务器端被修改
			break;
		case user_logout:
			// 用户换设备登录
			CustomDialog.Builder builder = new CustomDialog.Builder(this);
			builder.setTitle("下线事件");
			builder.setPositiveButton("重新登录", new OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					dialog.dismiss();
					JMessageClient.login(GlobeScopeUtils.getUser(),
							((User) GlobeScopeUtils.Get("ME")).getPswd(),
							new BasicCallback() {

								@Override
								public void gotResult(int arg0, String arg1) {
									// TODO Auto-generated method stub
									if (arg0 == 0) {
										ToastUtils.ShowShortToast("重新登录成功!");
									} else {
										ToastUtils
												.ShowShortToast("重新登录失败,可能密码已经被修改了");
										AppManager.FinishAllAct();
										ActChangeUtils.ChangeActivity(
												MainActivity.this,
												LoginActivity.class);
									}
								}

							});
				}
			});
			builder.setNegativeButton("退出帐号", new OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					dialog.dismiss();
					AppManager.FinishAllAct();
					ActChangeUtils.ChangeActivity(MainActivity.this,
							LoginActivity.class);
				}
			});
			builder.setMessage("你好,系统检测到你的帐号在别的移动端设备有登录,你要采取以下下面的哪两个操作呢?");
			Dialog dialog = builder.create();
			dialog.show();
			dialog.setCancelable(false);
			;
			break;
		case user_deleted:
			// 用户被删除
			break;
		}
	}

	@SuppressWarnings("deprecation")
	public void onEventMainThread(MessageEvent event) {
		cn.jpush.im.android.api.model.Message msg = event.getMessage();
		if (event.getMessage().getTargetType().equals(ConversationType.group)
				&& event.getMessage().getContentType().equals(ContentType.text)) {
			((Main_frag4) pagers.get(3)).UpdateMsg(event);
			if ((Boolean) PreferencesTool.getParam("setting", "icon",
					Boolean.class, true)) {
				ShowNotication(event);
			}
		} else if (event.getMessage().getTargetType()
				.equals(ConversationType.single)
				&& event.getMessage().getContentType().equals(ContentType.text)) {
			// 队伍申请加入后更新
			// 某个成员加入的事件
			final String text = ((TextContent) event.getMessage().getContent())
					.getText();
			if (text.startsWith("{")) {
				// 加入这个队
				try {
					final JSONObject text_obj = new JSONObject(text);
					final String target = text_obj.getString("uid");
					final String groupid = text_obj.getString("groupid");
					final String members = text_obj.getString("members");
					JMessageClient.addGroupMembers(Long.parseLong(groupid),
							Arrays.asList(target), new BasicCallback() {

								@Override
								public void gotResult(int arg0, String arg1) {
									// TODO Auto-generated method stub
									if (arg0 == 0) {
										Map<String, String> params = new HashMap<>();
										params.put("uid", target + "");
										params.put("members", members);
										params.put("groupid", groupid);
										OkHttpUtils
												.post()
												.url(AppConfig.QRcodeTeam)
												.params(params)
												.build()
												.execute(
														new MyStringCallBack() {

															@Override
															public void onResponse(
																	String response,
																	int id) {
																if (id != 0) {
																	ToastUtils
																			.ShowNetError();
																	return;
																}
																new Thread(
																		new Runnable() {
																			public void run() {
																				// 请写在线程运行的代码
																				JMessageClient
																						.sendMessage(JMessageClient
																								.createSingleTextMessage(
																										target,
																										"9"));
																			}
																		})
																		.start();
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

									}
								}
							});
				} catch (NumberFormatException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				Message s = new Message();
				s.what = 1;
				Main_frag3.handler.sendMessage(s);
				JMessageClient.deleteSingleConversation(event.getMessage()
						.getFromName());
			}
		} else if (event.getMessage().getContentType()
				.equals(ContentType.eventNotification)
				&& ((EventNotificationContent) event.getMessage().getContent())
						.getEventNotificationType().equals(
								EventNotificationType.group_member_removed)) {
			// 成员被T事件
			Message s = new Message();

			s.what = 1;
			Main_frag3.handler.sendMessage(s);
			JMessageClient.deleteGroupConversation(Long.parseLong(event
					.getMessage().getTargetID()));
			((Main_frag4) pagers.get(3)).UpdateMSg2();
		} else if (event.getMessage().getContentType()
				.equals(ContentType.eventNotification)
				&& ((EventNotificationContent) event.getMessage().getContent())
						.getEventNotificationType().equals(
								EventNotificationType.group_member_added)) {
			EventNotificationContent ctn = ((EventNotificationContent) event
					.getMessage().getContent());
			System.out.println(msg.getFromID());

			// final JSONObject obj = new JSONObject();
			// try {
			// obj.put("groupid", );
			// obj.put("username", GlobeScopeUtils.getUserName());
			// obj.put("teamid", team.getId());
			// obj.put("userid", GlobeScopeUtils.getMeId());
			// obj.put("content", "大家好,我是 ");
			// obj.put("type", 1);
			// obj.put("teamname", team.getName());
			// } catch (JSONException e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// upload_bar.setVisibility(View.GONE);
			// }
			// final Message msg = JMessageClient.createGroupTextMessage(
			// team.getGroupid(), obj.toString());
		}
	}

	@SuppressWarnings("deprecation")
	@SuppressLint("NewApi")
	@Override
	public void init() {

		setContentView(R.layout.activity_main);
		String user = getIntent().getStringExtra("user");
		String pswd = getIntent().getStringExtra("pswd");
		if (NoticeInfo.count(NoticeInfo.class, "userid = ?",
				new String[]{GlobeScopeUtils.getMeId()+""}) == 0) {
			NoticeInfo info1 = new NoticeInfo(
					GlobeScopeUtils.getMeId(),
					TimeUtils.getNoticeTime(new Date(System.currentTimeMillis())),
					"上海东岸开放空间灯塔设计全球方案征集", "上海东岸开放空间灯塔设计全球方案征集" + "已经发布了!",
					Integer.parseInt(15 + ""));
			NoticeInfo info2 = new NoticeInfo(
					GlobeScopeUtils.getMeId(),
					TimeUtils.getNoticeTime(new Date(System.currentTimeMillis())),
					"首届中国iOS应用开发大赛", "首届中国iOS应用开发大赛" + "已经发布了!", Integer
							.parseInt(25 + ""));
			NoticeInfo info3 = new NoticeInfo(
					GlobeScopeUtils.getMeId(),
					TimeUtils.getNoticeTime(new Date(System.currentTimeMillis())),
					"腾讯GAD · 2016全国高校游戏创意设计大赛 ", "腾讯GAD · 2016全国高校游戏创意设计大赛 "
							+ "已经发布了!", Integer.parseInt(26 + ""));
			NoticeInfo info4 = new NoticeInfo(
					GlobeScopeUtils.getMeId(),
					TimeUtils.getNoticeTime(new Date(System.currentTimeMillis())),
					"腾讯GAD · 2016全国高校游戏创意设计大赛 ", "这比赛还有30天就要截止报名了,亲要抓紧时间!",
					Integer.parseInt(26 + ""));
			NoticeInfo info5 = new NoticeInfo(
					GlobeScopeUtils.getMeId(),
					TimeUtils.getNoticeTime(new Date(System.currentTimeMillis())),
					"2016年中国三对三篮球联", "这比赛还有5天就要截止报名了,亲要抓紧时间!", Integer
							.parseInt(34 + ""));
			NoticeInfo info6 = new NoticeInfo(
					GlobeScopeUtils.getMeId(),
					TimeUtils.getNoticeTime(new Date(System.currentTimeMillis())),
					"腾讯GAD · 2016全国高校游戏创意设计大赛 ", "这比赛还有15天就要截止报名了,亲要抓紧时间!"
							+ "已经发布了!", Integer.parseInt(26 + ""));
			NoticeInfo info7 = new NoticeInfo(
					GlobeScopeUtils.getMeId(),
					TimeUtils.getNoticeTime(new Date(System.currentTimeMillis())),
					"微软编程之美2016挑战赛", "这比赛还有5天就要截止报名了,亲要抓紧时间!", Integer
							.parseInt(32 + ""));
			NoticeInfo info8 = new NoticeInfo(
					GlobeScopeUtils.getMeId(),
					TimeUtils.getNoticeTime(new Date(System.currentTimeMillis())),
					"华为“创想杯”校园开发者大赛", "这比赛还有5天就要截止报名了,亲要抓紧时间!", Integer
							.parseInt(30 + ""));
			info1.save();
			info2.save();
			info3.save();
			info4.save();
			info5.save();
			info6.save();
			info7.save();
			info8.save();
		}
		initGaode();
		JMessageClient.login(user, pswd, new BasicCallback() {

			@Override
			public void gotResult(int arg0, String arg1) {
				// TODO Auto-generated
				// method stub
				if (arg0 == 0) {
					PreferencesTool.setParam("User", "Login", true);
					((Main_frag4) pagers.get(3)).initData();
				} else if (arg0 == 801003) {
					// dialog.dismiss();
					ToastUtils.ShowShortToast("用户名不存在");
				} else if (arg0 == 801004) {
					// dialog.dismiss();
					ToastUtils.ShowShortToast("用户手机和密码不匹配");
				} else if (arg0 == 871504) {
					ToastUtils
							.ShowLongToast("特殊机型,由于本机的rom组件屏蔽了app的推送服务,所以组队和聊天功能无法使用");
				} else {
					// dialog.dismiss();

					ToastUtils.ShowShortToast(arg1);
				}
			}
		});
		JMessageClient.registerEventReceiver(this);
		IntentFilter filiter = new IntentFilter("newmatch");

		ViewUtils.inject(this);
		btn1 = (ImageButton) findViewById(R.id.Login_more);
		seg = (SegmentView) findViewById(R.id.main_segmentView1);
		pager = (ViewPager) findViewById(R.id.Main_Middle);
		mBar = (JPTabBar) findViewById(R.id.Main_bottom);
		mBar.setTabListener(this);
		pager.setAdapter(new MyPagerAdapter(getSupportFragmentManager(),
				getPagers()));
		radios = new RadioButton[4];
		top_badge.setVisibility(View.GONE);
		btn1.setOnClickListener(this);
		title_tv = (TextView) findViewById(R.id.Main_title);
		seg.setOnSegmentViewClickListener(this);
		pager.setOffscreenPageLimit(4);
		seg.setSegmentText("寻找", 0);
		seg.setSegmentText("我的", 1);
		seg.setSegmentTextSize(17);
		filiter.addAction("action");
		registerReceiver(MyPushReceiver, filiter);
		initAlarmReceiver();
	}

	private void initAlarmReceiver() {
		// TODO Auto-generated method stub
		alarmReceiver = new MyAlarmReceiver();
		alarmReceiver.setListener(this);

		IntentFilter filter = new IntentFilter("alarm");
		registerReceiver(alarmReceiver, filter);
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	List<Fragment> pagers;

	public void setPagers(List<Fragment> pagers) {
		this.pagers = pagers;
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		// 清空裁剪图片的缓存
		PreferencesTool.setParam("User", "Login", false);
		JMessageClient.unRegisterEventReceiver(this);
		unregisterReceiver(MyPushReceiver);
		JMessageClient.logout();
		unregisterReceiver(alarmReceiver);
		mLocationClient.stopLocation();
		mLocationClient.unRegisterLocationListener(this);
		super.onDestroy();
	}

	// 得到4个fragment
	public List<Fragment> getPagers() {
		if (pagers == null) {

			pagers = new ArrayList<Fragment>();
			pagers.add(new Main_frag1());
			pagers.add(new Main_frag2());
			pagers.add(new Main_frag3());
			Main_frag4 mainf4 = new Main_frag4();
			mainf4.setBad_lis(this);
			pagers.add(mainf4);
			Main_frag5 frag5 = new Main_frag5();
			frag5.setListener(this);
			pagers.add(frag5);
		}

		return pagers;
	}



	// 切换页面
	public void TabPage(int index) {
		pager.setCurrentItem(index);
	}

	@Override
	public void onSegmentViewClick(View v, int position) {
		// TODO Auto-generated method stub
		switch (position) {
		case 0:
			TabPage(1);
			break;
		case 1:
			TabPage(2);
			break;

		default:
			break;
		}
	}

	/**
	 * 设置添加屏幕的背景透明度
	 * 
	 * @param bgAlpha
	 */
	public void backgroundAlpha(float bgAlpha) {
		WindowManager.LayoutParams lp = getWindow().getAttributes();
		lp.alpha = bgAlpha; // 0.0-1.0
		getWindow().setAttributes(lp);
	}

	@Override
	public void onBadgeUpdate(int count) {
		// TODO Auto-generated method stub
		mBar.showBadge(0,count);
	}

	@Override
	public void onBadgesMinus(int count) {
		// TODO Auto-generated method stub
		mBar.showBadge(0,count);
	}

	@Override
	public void onBadge2Visiable(boolean flag) {
		// TODO Auto-generated method stub
		mBar.showBadge(2,0);
	}

	@Override
	public void onDismiss() {
		// TODO Auto-generated method stub
		backgroundAlpha(1f);
	}

	BroadcastReceiver MyPushReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub

			// 当接受到信息时,通过intent 更改updatint

			MatchInfo info = (MatchInfo) intent.getSerializableExtra("info");
			if (info == null) {
				return;
			}
			info.save();
			// 报出异常,指针空指针
			int type = info.getType2();
			NoticeInfo noticeInfo = new NoticeInfo(
					GlobeScopeUtils.getMeId(),
					TimeUtils.getNoticeTime(new Date(System.currentTimeMillis())),
					info.getName(), info.getName() + "已经发布了!", Integer
							.parseInt(info.getId() + ""));
			noticeInfo.save();
			ShowNotce(noticeInfo, info);
			top_badge.setVisibility(View.VISIBLE);
			GlobeScopeUtils.getMatches().add(info);
			Collections.sort(GlobeScopeUtils.getMatches(),
					new Comparator<MatchInfo>() {

						@Override
						public int compare(MatchInfo lhs, MatchInfo rhs) {
							// TODO Auto-generated method stub
							if (lhs.getId() < rhs.getId()) {
								return 1;
							}
							return -1;
						}
					});
			// 插入数据到数据库
			((Main_frag1) pagers.get(0)).updateBottom(type, info);

		}
	};

	@Override
	public void onReceiveAlarm(Intent intent) {
		// TODO Auto-generated method stub
		// 一旦接受到了,就立马刷新ui
		// match
		MatchInfo matchInfo = (MatchInfo) intent.getSerializableExtra("match");
		int day = intent.getIntExtra("day", 15);
		NoticeInfo noticeInfo = new NoticeInfo(GlobeScopeUtils.getMeId(),
				TimeUtils.getNoticeTime(new Date(System.currentTimeMillis())),
				matchInfo.getName(), "还有" + day + "天就要比赛了,请抓紧时间啊!亲!",
				Integer.parseInt(matchInfo.getId() + ""));

		noticeInfo.save();
		ShowNotce(noticeInfo, matchInfo);
		top_badge.setVisibility(View.VISIBLE);

	}

	@Override
	public void onLocationChanged(AMapLocation location) {
		// TODO Auto-generated method stub
		if (location.getErrorCode() == 0) {
			// 可在其中解析amapLocation获取相应内容。
			if (location.getAddress() != null
					&& !location.getAddress().equals("")) {
				GlobeScopeUtils.getMeEntity().setReal_address(
						location.getAddress());
			}
		} else {
		}

	}

	@Override
	public void onTabSelect(int index) {
		title_tv.setText(index==0? getString(R.string.Main_Tab1)
				: index == 1? getString(R.string.Main_Tab2)
				: index == 2 ? getString(R.string.Main_Tab3)
				: getString(R.string.Main_Tab4));
		if (index == 1) {

			title_tv.setVisibility(View.GONE);
			seg.setVisibility(View.VISIBLE);
		} else {

			title_tv.setVisibility(View.VISIBLE);
			seg.setVisibility(View.GONE);
		}

		if (index > radios.length) {
			return;
		}
			if (index > 1) {
				pager.setCurrentItem(index+1);
			}

			else {
				pager.setCurrentItem(index);
			}
		if (index == 1) {
			((TextView) seg.getChildAt(0)).setSelected(true);
			((TextView) seg.getChildAt(1)).setSelected(false);
		}

		else if (index == 2) {
			((TextView) seg.getChildAt(1)).setSelected(true);
			((TextView) seg.getChildAt(0)).setSelected(false);
		}

	}

}
