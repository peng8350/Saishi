package com.peng.saishi.activity;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;
import com.facebook.drawee.view.SimpleDraweeView;
import com.peng.saishi.R;
import com.peng.saishi.activity.base.BaseBackActivity;
import com.peng.saishi.adapter.MatchAdapter;
import com.peng.saishi.adapter.NewsAdapter;
import com.peng.saishi.adapter.QuestionLittleAdapter;
import com.peng.saishi.adapter.ReplyAdapter;
import com.peng.saishi.entity.MatchInfo;
import com.peng.saishi.entity.NewInfo;
import com.peng.saishi.entity.Pingjia;
import com.peng.saishi.entity.QuestionInfo;
import com.peng.saishi.entity.config.AppConfig;
import com.peng.saishi.interfaces.service.MyStringCallBack;
import com.peng.saishi.manager.NoDataManager;
import com.peng.saishi.utils.*;
import com.peng.saishi.widget.BadgeView;
import com.peng.saishi.widget.HorizontalListView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;
import okhttp3.Call;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.*;

@SuppressLint("InflateParams")
public class MatchActivity extends BaseBackActivity implements OnClickListener,
		OnItemClickListener {
	private MatchInfo info;
	private View footer, header;
	private ListView match_list, reply_list;
	private List<Pingjia> replys;
	private TextView count_tv;
	private Button shou_btn, web_btn;
	SimpleDraweeView image;
	private boolean shoucang;
	private Date date1, date2;// date1指的是当前时间的前10天,date2表示现在的时间

	public boolean isShoucang() {
		return shoucang;
	}

	public void setShoucang(boolean shoucang) {
		this.shoucang = shoucang;
		if (shoucang) {
			shou_btn.setText("取消收藏");
		} else {
			shou_btn.setText("收藏");
		}

	}

	@SuppressLint("InflateParams")
	@Override
	public void init() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_match);
		super.init();
		info = (MatchInfo) getIntent().getSerializableExtra("match");
		OkHttpUtils.post().url(AppConfig.getMatchVisit)
				.addParams("id", info.getId() + "").build()
				.execute(new StringCallback() {

					@Override
					public void onResponse(String arg0, int arg1) {
						// TODO Auto-generated method stub
						MatchInfo target=null;
						for(MatchInfo a:GlobeScopeUtils.getMatches()){
							if (a.getId()==info.getId()) {
								target = a;
								break;
							}
						}
						target.setVisitPerson(Integer.parseInt(arg0));
						MatchInfo.save(target);
					}

					@Override
					public void onError(Call arg0, Exception arg1, int arg2) {
						// TODO Auto-generated method stub

					}
				});
		// 假数据
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date(System.currentTimeMillis()));
		calendar.add(Calendar.DATE, 10);
		date1 = calendar.getTime();
		date2 = new Date(System.currentTimeMillis());
		match_list = (ListView) findViewById(R.id.Match_listview);
		footer = LayoutInflater.from(this).inflate(R.layout.footer_match, null);
		header = LayoutInflater.from(this).inflate(R.layout.header_match, null);
		reply_list = (ListView) footer.findViewById(R.id.Match_footer2)
				.findViewById(R.id.footer_match_listview);
		initFooter(footer);
		footer.findViewById(R.id.Match_footer2).setOnClickListener(this);
		match_list.addHeaderView(header);
		match_list.addFooterView(footer);
		nodata_layout = NoDataManager.initNodata("暂时没有新闻哦!", this);
		initHeaderNews(
				(HorizontalListView) header
						.findViewById(R.id.header_match_horizontallistview),
				(ProgressBar) header.findViewById(R.id.header_match_progress));

		match_list.setAdapter(new MatchAdapter(info, this));
		OkHttpUtils.post().url(AppConfig.Visit)
				.addParams("id", info.getId() + "").build().execute(null);
		TextView title = (TextView) header
				.findViewById(R.id.header_match_textView1);
		RatingBar rBar = (RatingBar) header
				.findViewById(R.id.header_match_ratingBar1);
		TextView good = (TextView) header
				.findViewById(R.id.header_match_textView2);
		TextView fenlei = (TextView) header
				.findViewById(R.id.header_match_textView3);
		TextView more = (TextView) header
				.findViewById(R.id.header_match_textView5);
		BadgeView process = (BadgeView) header
				.findViewById(R.id.header_match_badgeView1);
		image = (SimpleDraweeView) header
				.findViewById(R.id.header_match_imageView1);
		image.setOnClickListener(this);
		web_btn = (Button) header.findViewById(R.id.header_match_button1);
		shou_btn = (Button) header.findViewById(R.id.header_match_button2);
		shou_btn.setOnClickListener(this);
		web_btn.setOnClickListener(this);
		findViewById(R.id.Match_imageView1).setOnClickListener(this);
		title.setText(info.getName());
		process.setText(TimeUtils.getDateFromStr(info.getTime()).after(date2)
				&& TimeUtils.getDateFromStr(info.getTime()).before(date1) ? "将截止"
				: TimeUtils.getDateFromStr(info.getTime()).after(date1) ? "可报名"
						: "已截止");
		// good.setText("(" + info.getGood() + "分)");
		// fenlei.setText((info.getType() == 1 ?
		// getString(R.string.mainfrag1_m1)
		// : info.getType() == 2 ? getString(R.string.mainfrag1_m2)
		// : info.getType() == 3 ? getString(R.string.mainfrag1_m3)
		// : info.getType() == 4 ? getString(R.string.mainfrag1_m4)
		// : info.getType() == 5 ? getString(R.string.mainfrag1_m5)
		// : getString(R.string.mainfrag1_m6))
		// + " | " + info.getFenlei());
		more.setText("时间: " + info.getTime() + "\n地点: " + info.getPlace()
				+ "\n已有" + info.getVisitPerson() + "人阅读");
		// 对时间的判断
		// 设置分数
		rBar.setMax(100);
		rBar.setProgress((int) (info.getScore() * 10));
		// 设置分数text
		good.setText("(" + info.getScore() + "分)");
		fenlei.setText(info.getFenlei());
		FrescoUtils.Display(image, AppConfig.Matchpic_path + info.getId(), 250,
				250, true);
		Map<String, String> params = new HashMap<>();

		params.put("userid", GlobeScopeUtils.getMeId() + "");
		params.put("matchid", info.getId() + "");
		OkHttpUtils.post().url(AppConfig.isAdd).params(params).build()
				.execute(new MyStringCallBack() {

					@Override
					public void onResponse(String response, int id) {
						// TODO Auto-generated method stub
						if (id != 0) {
							ToastUtils.ShowNetError();
							return;
						}
						// TODO Auto-generated method stub
						try {
							JSONObject res_obj = new JSONObject(response);
							if (res_obj.getString("have").equals("true")) {
								setShoucang(true);
							} else {
								setShoucang(false);
							}
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

	// 初始化底部
	private void initFooter(View footer2) {
		// TODO Auto-generated method stub
		View question_footlayout = footer2.findViewById(R.id.Match_footer1);
		TextView title_tv = (TextView) question_footlayout
				.findViewById(R.id.footer_match_textView2);

		TextView descr_tv = (TextView) question_footlayout
				.findViewById(R.id.footer_match_textView4);
		question_footlayout.setOnClickListener(this);
		final ListView ques_list = (ListView) question_footlayout
				.findViewById(R.id.footer_match_listview);
		title_tv.setText("选手答疑");
		descr_tv.setText("查看更多");
		getQuestionData(ques_list);
		// 初始化评价的
		getReplys();
	}

	public void getQuestionData(final ListView listview) {
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
						List<QuestionInfo> list = JsonParser.getListFromJson(
								array, QuestionInfo.class);
						info.setQuestions(list);
						listview.setAdapter(new QuestionLittleAdapter(
								MatchActivity.this, info.getQuestions()));
					}

					@Override
					public void onError(Call call, Exception e, int id) {
						// TODO Auto-generated method stub
						System.out.println(id + "失败");
					}
				});
	}

	private View nodata_layout;
	private boolean nodata;

	private void initHeaderNews(final HorizontalListView listview,
			final ProgressBar progressBar) {
		// TODO Auto-generated method stub
		// 初始化新闻列表的数据
		listview.setOnItemClickListener(this);
		if (info != null && info.getNews() != null) {
			listview.setAdapter(new NewsAdapter(this, info.getNews(), false));
			NoDataManager.ChangeState(nodata_layout, info.getNews());
			progressBar.setVisibility(View.GONE);

		} else
			OkHttpUtils.post().url(AppConfig.getNews)
					.addParams("id", info.getId() + "").build()
					.execute(new MyStringCallBack() {

						@Override
						public void onResponse(String response, int id) {
							// TODO Auto-generated method stub
							if (id != 0) {
								ToastUtils.ShowNetError();
								return;
							}
							JSONArray res_obj = null;
							try {
								res_obj = new JSONArray(response);
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							List<NewInfo> list = JsonParser.getListFromJson(
									res_obj, NewInfo.class);
							info.setNews(list);
							if (list.size() == 0) {
								nodata = true;
								Random random = new Random();
								String[] titles = getResources().getStringArray(R.array.titles);
								String[] contents= getResources().getStringArray(R.array.contents);
								for (int i = 0; i < random.nextInt(9) + 1; i++) {
									NewInfo info = new NewInfo();
									int a=random.nextInt(9) ;
									info.setTitle(titles[a]);
									info.setContent(contents[a]);
									info.setMatchid(MatchActivity.this.info
											.getId());
									info.setWebUrl("http://www.baidu.com/");
									info.setTime("2016年9月20日");
									info.setId(a);
									list.add(info);
								}
							}
							listview.setAdapter(new NewsAdapter(
									MatchActivity.this, list, nodata));
							progressBar.setVisibility(View.GONE);
							NoDataManager.ChangeState(nodata_layout,
									info.getNews());
						}

						@Override
						public void onError(Call call, Exception e, int id) {
							// TODO Auto-generated method stub
							ToastUtils.ShowNetError();
							progressBar.setVisibility(View.GONE);
							NoDataManager.ChangeState(nodata_layout,
									info.getNews());

						}
					});
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	private void getReplys() {
		// TODO Auto-generated method stub
		count_tv = (TextView) footer.findViewById(R.id.Match_footer2)
				.findViewById(R.id.footer_match_textView4);
		Map<String, String> params = new HashMap<>();
		params.put("id", info.getId() + "");
		OkHttpUtils.post().url(AppConfig.getMatchPingjia).params(params)
				.build().execute(new MyStringCallBack() {

					@Override
					public void onResponse(String response, int id) {
						// TODO Auto-generated method stub
						if (id != 0) {
							ToastUtils.ShowNetError();
							return;
						}
						// TODO Auto-generated method stub
						try {
							JSONArray array = new JSONArray(response);
							replys = JsonParser.getListFromJson(array,
									Pingjia.class);
							reply_list.setAdapter(new ReplyAdapter(
									MatchActivity.this, replys, Integer
											.parseInt(info.getId() + "")));
							count_tv.setText("共" + replys.size() + "条");
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
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		// TODO Auto-generated method stub
		if (arg0 == 101 && arg1 == RESULT_OK) {
			// 传回来的list更新
			replys = arg2.getParcelableArrayListExtra("replys");
			if (replys.size() != reply_list.getAdapter().getCount()) {
				reply_list.setAdapter(new ReplyAdapter(this, replys, Integer
						.parseInt(info.getId() + "")));
				count_tv.setText("共" + replys.size() + "条");
			}
		}
		if (arg0 == 102 && arg1 == RESULT_OK) {
			// 传回来的list更新
			getQuestionData((ListView) findViewById(R.id.footer_match_listview));

		}

	}

	// 从收藏的比赛列表移走拥挤胡
	public int Remove(int teamid) {
		List<MatchInfo> shoucangs = GlobeScopeUtils.getShoucangs();
		for (int i = 0; i < shoucangs.size(); i++) {
			if (shoucangs.get(i).getId() == teamid) {
				return i;
			}
		}
		return 0;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stubothershoucang
		super.onClick(v);
		switch (v.getId()) {
		case R.id.Match_footer1:
			Intent questionlist_intent = new Intent(this, QuestionListAct.class);

			questionlist_intent.putExtra("match", info);
			startActivityForResult(questionlist_intent, 102);
			break;
		case R.id.Match_footer2:
			Intent intent = new Intent(this, ReplyActivity.class);
			intent.putParcelableArrayListExtra("replys",
					(ArrayList<? extends Parcelable>) replys);
			intent.putExtra("match_id", Integer.parseInt(info.getId() + ""));
			startActivityForResult(intent, 101);

			break;
		case R.id.Match_imageView1:
			// 分享比赛给其他app
			ShareAppUtils.showMatchShare(this, info);
			break;
		case R.id.header_match_button1:
			// 网申地址
			ActChangeUtils.ChangeActivity(this, WebActivity.class, "url",
					info.getWebUrl());
			// Intent inte = new Intent();
			// inte.setAction("android.intent.action.VIEW");
			// Uri content_url = Uri.parse(info.getWebUrl());
			// inte.setData(content_url);
			// startActivity(inte);
			break;
		case R.id.header_match_button2:
			// 收藏
			// String id = request.getParameter("id");
			// String match_id = request.getParameter("target");
			// String isadd = request.getParameter("add");
			Map<String, String> params = new HashMap<>();
			params.put("id", GlobeScopeUtils.getMeId() + "");
			params.put("target", info.getId() + "");
			params.put("add",
					shou_btn.getText().toString().equals("收藏") ? "true"
							: "false");
			OkHttpUtils.post().url(AppConfig.SHoucang).params(params).build()
					.execute(new MyStringCallBack() {

						@Override
						public void onResponse(String response, int id) {
							// TODO Auto-generated method stub
							if (id != 0) {
								ToastUtils.ShowNetError();
								return;
							}
							// TODO Auto-generated method stub
							try {
								JSONObject res_obj = new JSONObject(response);
								if (res_obj.getString("result").equals("ok")) {
									// 成功了,一般状态下为这个
									setShoucang(!shoucang);
									if (!shoucang) {
										// 移走
										GlobeScopeUtils.getShoucangs().remove(
												Remove(Integer.parseInt(info
														.getId() + "")));

										RemoveAlarm(info);
									} else {
										GlobeScopeUtils.getShoucangs()
												.add(info);
										addAlarm(info);
									}
								} else {

								}
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

			break;
		case R.id.header_match_imageView1:
			ActChangeUtils.ChangeActivity(this, PhotoActivity.class,
					new String[] { "type", "id" }, info.getId() + "", 1);
			break;
		default:
			break;
		}
	}

	private void RemoveAlarm(MatchInfo info) {
		// TODO Auto-generated method stub
		Intent intent = new Intent("alarm");
		AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

		PendingIntent pendingIntent1 = PendingIntent.getBroadcast(this,
				Integer.parseInt(info.getId() + "") * 3 - 1, intent, 0);
		PendingIntent pendingIntent2 = PendingIntent.getBroadcast(this,
				Integer.parseInt(info.getId() + "") * 3 - 2, intent, 0);
		PendingIntent pendingIntent3 = PendingIntent.getBroadcast(this,
				Integer.parseInt(info.getId() + "") * 3 - 3, intent, 0);
		manager.cancel(pendingIntent1);
		manager.cancel(pendingIntent2);
		manager.cancel(pendingIntent3);
	}

	// 添加
	private void addAlarm(MatchInfo info) {
		// TODO Auto-generated method stub
		Intent intent1 = new Intent("alarm");
		intent1.putExtra("match", info);
		intent1.putExtra("day", 5);
		Intent intent2 = new Intent("alarm");
		intent2.putExtra("match", info);
		intent2.putExtra("day", 15);
		Intent intent3 = new Intent("alarm");
		intent3.putExtra("match", info);
		intent3.putExtra("day", 30);
		Date match_date = TimeUtils.getDateFromStr(info.getTime());
		Date expected_date1 = CalendarUtils.getAddDayDate(-5, match_date);
		Date expected_date2 = CalendarUtils.getAddDayDate(-15, match_date);
		Date expected_date3 = CalendarUtils.getAddDayDate(-30, match_date);
		AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
		PendingIntent pending = PendingIntent.getBroadcast(this,
				Integer.parseInt(info.getId() + "") * 3, intent1,
				PendingIntent.FLAG_UPDATE_CURRENT);
		PendingIntent pending1 = PendingIntent.getBroadcast(this,
				Integer.parseInt(info.getId() + "") * 3 - 1, intent2,
				PendingIntent.FLAG_UPDATE_CURRENT);
		PendingIntent pending2 = PendingIntent.getBroadcast(this,
				Integer.parseInt(info.getId() + "") * 3 - 2, intent3,
				PendingIntent.FLAG_UPDATE_CURRENT);
		long now_time = System.currentTimeMillis();
		if (new Date(now_time).before(expected_date1)) {
			manager.set(AlarmManager.RTC_WAKEUP, expected_date1.getTime(),
					pending);
		}
		if (new Date(now_time).before(expected_date2)) {
			manager.set(AlarmManager.RTC_WAKEUP, expected_date2.getTime(),
					pending1);
		}
		if (new Date(now_time).before(expected_date3)) {
			manager.set(AlarmManager.RTC_WAKEUP, expected_date3.getTime(),
					pending2);
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		Intent inten = new Intent(this, NewsActivity.class);
		inten.putExtra("News", info.getNews().get(position));
		inten.putExtra("nodata", nodata);
		startActivity(inten);
	}
}
