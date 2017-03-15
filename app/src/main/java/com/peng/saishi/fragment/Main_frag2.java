package com.peng.saishi.fragment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

import org.json.JSONArray;
import org.json.JSONException;

import android.graphics.Color;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.BaseAdapter;
import android.widget.HeaderViewListAdapter;
import android.widget.ListView;

import com.peng.saishi.R;
import com.peng.saishi.adapter.MainFrag2Adapter;
import com.peng.saishi.entity.TeamInfo;
import com.peng.saishi.entity.config.AppConfig;
import com.peng.saishi.fragment.base.LazyFragment;
import com.peng.saishi.interfaces.service.MyStringCallBack;
import com.peng.saishi.manager.App;
import com.peng.saishi.utils.GlobeScopeUtils;
import com.peng.saishi.utils.JsonParser;
import com.peng.saishi.utils.PreferencesTool;
import com.peng.saishi.utils.TimeUtils;
import com.peng.saishi.utils.ToastUtils;
import com.peng.saishi.widget.dropdownmenu.DropDownMenu;
import com.peng.saishi.widget.dropdownmenu.OnMenuSelectedListener;
import com.peng.saishi.widget.pulltorefesh.PullToRefreshBase;
import com.peng.saishi.widget.pulltorefesh.PullToRefreshBase.Mode;
import com.peng.saishi.widget.pulltorefesh.PullToRefreshBase.OnPullEventListener;
import com.peng.saishi.widget.pulltorefesh.PullToRefreshBase.OnRefreshListener2;
import com.peng.saishi.widget.pulltorefesh.PullToRefreshBase.State;
import com.peng.saishi.widget.pulltorefesh.PullToRefreshListView;
import com.zhy.http.okhttp.OkHttpUtils;

public class Main_frag2 extends LazyFragment implements
		OnRefreshListener2<ListView>, OnMenuSelectedListener {
	private PullToRefreshListView listView;

	private int r1 = 6, r2 = 4, r3 = 6;
	private SoundPool soundpool;
	HashMap<String, Integer> soundids = new HashMap<>();
	final String[] arr1 = new String[] {
			App.getInstance().getString(R.string.mainfrag1_m1),
			App.getInstance().getString(R.string.mainfrag1_m2),
			App.getInstance().getString(R.string.mainfrag1_m3),
			App.getInstance().getString(R.string.mainfrag1_m4),
			App.getInstance().getString(R.string.mainfrag1_m5),
			App.getInstance().getString(R.string.mainfrag1_m6), "不限" };
	final String[] arr2 = new String[] { "2-4人", "5-8人", "8-12人", ">=13人", "不限" };
	final String[] arr3 = new String[] { "半月内", "半月-1月内", "1月到3月内", "3月到半年内",
			"半年到一年内", ">1年", "不限" };
	final String[] strings = new String[] { "选择类型", "选择人数", "选择时间" };
	DropDownMenu mMenu;
	private List<TeamInfo> teams;
	private Date date1, date2, date3, date4, date5, date6;;
	MainFrag2Adapter adapter;

	public Main_frag2() {
		super(R.layout.main_frag2);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		if (soundpool != null)
			soundpool.release();
		super.onDestroyView();
	}

	View layout;

	@Override
	protected View initViews(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		layout = inflater.inflate(layoudi, null);
		OkHttpUtils.post().url(AppConfig.getAllTeam)
		.addParams("index", index + "").build()
		.execute(new MyStringCallBack() {

			@Override
			public void onResponse(String response, int id) {
				// TODO Auto-generated method stub
				// TODO Auto-generated method stub
				if (id != 0) {
					return;
				}
				JSONArray array = null;
				try {
					array = new JSONArray(response);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				teams = JsonParser.getListFromJson(array,
						TeamInfo.class);
				index += teams.size();
				adapter = new MainFrag2Adapter(teams, getActivity());


			}

			@Override
			public void onError(Call call, Exception e, int id) {
				// TODO Auto-generated method stub

			}

		});
;
		return layout;
	}

	@SuppressWarnings({ "deprecation", "static-access" })
	@Override
	protected void initData() {
		// TODO Auto-generated method stub
		((ViewStub) layout.findViewById(R.id.frag2_vs)).inflate();
		listView = (PullToRefreshListView) layout
				.findViewById(R.id.frag2_listView1);
		listView.getLoadingLayoutProxy(false, true).setPullLabel("上拉加载更多..");
		listView.setRefreshingLabel("拼命加载中...");
		listView.setReleaseLabel("释放即可加载更多");
		initDropdown();
		soundpool = new SoundPool(16, AudioManager.STREAM_MUSIC, 0);
		soundids.put("1", soundpool.load(getActivity(), R.raw.pull_event, 1));
		soundids.put("2",
				soundpool.load(getActivity(), R.raw.refreshing_sound, 1));
		soundids.put("3", soundpool.load(getActivity(), R.raw.reset_sound, 1));
		date1 = new Date(System.currentTimeMillis());
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date(System.currentTimeMillis()));
		calendar.add(Calendar.DATE, 15);
		date2 = calendar.getTime();
		calendar.setTime(new Date(System.currentTimeMillis()));
		calendar.add(Calendar.MONTH, 1);
		date3 = calendar.getTime();
		calendar.setTime(new Date(System.currentTimeMillis()));
		calendar.add(Calendar.MONTH, 3);
		date4 = calendar.getTime();
		calendar.setTime(new Date(System.currentTimeMillis()));
		calendar.add(Calendar.MONTH, 6);
		date5 = calendar.getTime();
		calendar.setTime(new Date(System.currentTimeMillis()));
		calendar.add(calendar.YEAR, 1);
		date6 = calendar.getTime();
		listView.setAdapter(adapter);
		listView.setOnRefreshListener(Main_frag2.this);
		listView.setOnPullEventListener(new OnPullEventListener<ListView>() {
			@Override
			public void onPullEvent(
					PullToRefreshBase<ListView> refreshView,
					State state, Mode direction) {
				// TODO Auto-generated method stub
					
				if (state.equals(State.PULL_TO_REFRESH)
					) {
					listView.getLoadingLayoutProxy(true,false)
							.setRefreshingLabel("正在刷新数据...");
					listView.getLoadingLayoutProxy(true,false)
							.setReleaseLabel("松开手指刷新...");
					listView.getLoadingLayoutProxy(true,false)
							.setPullLabel("下拉刷新..");
					String label = DateUtils.formatDateTime(
							App.getInstance(),
							System.currentTimeMillis(),
							DateUtils.FORMAT_SHOW_TIME
									| DateUtils.FORMAT_SHOW_DATE
									| DateUtils.FORMAT_ABBREV_ALL);
					refreshView.getLoadingLayoutProxy(true,false)
							.setLastUpdatedLabel(
									"上次更新:" + label);
					if ((Boolean) PreferencesTool.getParam(
							"setting", "sound", Boolean.class,
							true)) {
						soundpool.play(soundids.get("1"), 1, 1,
								0, 0, 1);
					}
				} else if (state.equals(State.REFRESHING)) {
					if ((Boolean) PreferencesTool.getParam(
							"setting", "sound", Boolean.class,
							true)) {
						soundpool.play(soundids.get("2"), 1, 1,
								0, 0, 1);
					}
				}
			}
		});
		

	}

	// 初始化dropdownmenu
	public void initDropdown() {
		mMenu = (DropDownMenu) layout.findViewById(R.id.frag2_dropDownMenu1);

		mMenu.setmMenuCount(3);
		mMenu.setmShowCount(6);
		mMenu.setShowCheck(true);
		mMenu.setmMenuTitleTextSize(16);
		mMenu.setmMenuTitleTextColor(Color.parseColor("#777777"));
		mMenu.setmMenuListTextSize(16);
		mMenu.setmMenuListTextColor(Color.BLACK);
		mMenu.setmMenuBackColor(Color.parseColor("#eeeeee"));
		mMenu.setmMenuPressedBackColor(Color.WHITE);
		mMenu.setmMenuPressedTitleTextColor(Color.BLACK);

		mMenu.setmCheckIcon(R.drawable.ico_make);

		mMenu.setmUpArrow(R.drawable.arrowup);
		mMenu.setmDownArrow(R.drawable.arrowdown);

		mMenu.setDefaultMenuTitle(strings);

		mMenu.setShowDivider(false);
		mMenu.setmMenuListBackColor(getResources().getColor(R.color.WHite));
		mMenu.setmMenuListSelectorRes(R.color.WHite);
		mMenu.setmArrowMarginTitle(20);

		List<String[]> items = new ArrayList<>();
		items.add(arr1);
		items.add(arr2);
		items.add(arr3);
		mMenu.setmMenuItems(items);
		mMenu.setMenuSelectedListener(this);
	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
		// TODO Auto-generated method stub
		Map<String, String> params = new HashMap<String, String>();
		params.put("index", "0");
		params.put("limit", (teams.size() < 10 ? 10 : teams.size()) + "");
		OkHttpUtils.post().url(AppConfig.getAllTeam).params(params).build()
				.execute(new MyStringCallBack() {

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
						teams.clear();
						teams.addAll(JsonParser.getListFromJson(array,
								TeamInfo.class));
						MainFrag2Adapter adapter = (MainFrag2Adapter) ((HeaderViewListAdapter) listView
								.getRefreshableView().getAdapter())
								.getWrappedAdapter();
						adapter.notifyDataSetChanged();
						listView.onRefreshComplete();

					}

					public void onError(Call call, Exception e, int id) {
						// TODO Auto-generated method stub
						ToastUtils.ShowNetError();
						listView.onRefreshComplete();
					}
				});
	}

	private int index = 0;// 当前索引值;

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
		// TODO Auto-generated method stub
		// 上啦加载
		Map<String, String> params = new HashMap<>();
		params.put("lastid", adapter.getLastId() + "");
		OkHttpUtils.post().url(AppConfig.getAllTeam).params(params).build()
				.execute(new MyStringCallBack() {

					public void onResponse(String response, int id) {
						// TODO Auto-generated method stub
						if (id != 0) {
							ToastUtils.ShowNetError();
							return;
						}
						try {
							List<TeamInfo> param_list = JsonParser
									.getListFromJson(new JSONArray(response),
											TeamInfo.class);
							teams.addAll(param_list);
							index += param_list.size();
							((BaseAdapter) ((HeaderViewListAdapter) listView
									.getRefreshableView().getAdapter())
									.getWrappedAdapter())
									.notifyDataSetChanged();
							listView.onRefreshComplete();
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}

					@Override
					public void onError(Call call, Exception e, int id) {
						// TODO Auto-generated method stub

					}

				});
	}

	@Override
	public void onSelected(View listview, int RowIndex, int ColumnIndex) {
		// TODO Auto-generated method stub
		if (ColumnIndex == 0) {
			r1 = RowIndex;
		} else if (ColumnIndex == 1) {
			r2 = RowIndex;
		} else {
			r3 = RowIndex;
		}
		MainFrag2Adapter adapter = (MainFrag2Adapter) ((HeaderViewListAdapter) listView
				.getRefreshableView().getAdapter()).getWrappedAdapter();
		List<TeamInfo> into_list = new ArrayList<>();
		System.out.println(RowIndex);
		System.out.println(ColumnIndex);
		for (TeamInfo info : teams) {
			boolean flag = true;
			flag &= r1 == 0 ? info.getType().startsWith("IT开发")
					: r1 == 1 ? info.getType().startsWith("歌唱比赛")
							: r1 == 2 ? info.getType().startsWith("会计金融")
									: r1 == 3 ? info.getType().startsWith(
											"游戏竞技") : r1 == 4 ? info.getType()
											.startsWith("体育运动")
											: r1 == 5 ? info.getType()
													.startsWith("艺术设计") : true;
			flag &= r2 == 3 ? info.getNeedperson() >= 13 : r2 == 2 ? info
					.getNeedperson() >= 9 && info.getNeedperson() <= 12
					: r2 == 1 ? info.getNeedperson() >= 5
							&& info.getNeedperson() <= 8 : r2 == 0 ? info
							.getNeedperson() >= 2 && info.getNeedperson() <= 4
							: true;
			Date date = TimeUtils.getDateFromStr(info.getTime());
			flag &= r3 == 0 ? date.after(date1) && date.before(date2)
					: r3 == 1 ? date.after(date2) && date.before(date3)
							: r3 == 2 ? date.after(date3) && date.before(date4)
									: r3 == 3 ? date.after(date4)
											&& date.before(date5)
											: r3 == 4 ? date.after(date5)
													&& date.before(date6)
													: r3 == 5 ? date
															.after(date6)
															: true;
			if (flag) {
				into_list.add(info);
			}
		}
		adapter.setList(into_list);
		adapter.notifyDataSetChanged();

	}

	@Override
	protected void setDefaultFragmentTitle(String title) {
		// TODO Auto-generated method stub

	}

}
