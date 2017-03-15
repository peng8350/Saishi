package com.peng.saishi.activity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.peng.saishi.R;
import com.peng.saishi.activity.base.BaseBackActivity;
import com.peng.saishi.adapter.MatchPagereAdapter;
import com.peng.saishi.entity.MatchInfo;
import com.peng.saishi.utils.ActChangeUtils;
import com.peng.saishi.utils.GlobeScopeUtils;
import com.peng.saishi.utils.MatchUtils;
import com.peng.saishi.utils.TimeUtils;
import com.peng.saishi.widget.pagertab.PagerSlidingTabStrip;

public class MatchListActivity extends BaseBackActivity {

	private ViewPager pager;
	private PagerSlidingTabStrip strip;
	private TextView title_tv;
	private List<MatchInfo> datas;
	private Date date1, date2;// date1指的是当前时间的前10天,date2表示现在的时间

	@Override
	public void init() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_matchlist);
		super.init();
		Calendar calendar= Calendar.getInstance();
		calendar.setTime(new Date(System.currentTimeMillis()));
		calendar.add(Calendar.DATE, 10);
		date1 = calendar.getTime();
		date2 = new Date(System.currentTimeMillis());
		int type = getIntent().getIntExtra("type", 0);
		pager = (ViewPager) findViewById(R.id.Matchlist_pager);
		title_tv = (TextView) findViewById(R.id.MatchList_title);
		strip = (PagerSlidingTabStrip) findViewById(R.id.MatchList_pagerSliding);
		// 嘉数据
		datas = MatchUtils.getPart1Match(GlobeScopeUtils.getMatches(), type);
		// 要处理分类
		List<MatchInfo> list1 = new ArrayList<MatchInfo>();
		List<MatchInfo> list2 = new ArrayList<MatchInfo>();
		List<MatchInfo> list3 = new ArrayList<MatchInfo>();
		for (MatchInfo info : datas) {
			if (TimeUtils.getDateFromStr(info.getTime()).after(date2)&&TimeUtils.getDateFromStr(info.getTime()).before(date1)) {
				// 假如信息的事件是少于10天前的数据
				list2.add(info);
			} else if (TimeUtils.getDateFromStr(info.getTime()).after(date1)) {
				list1.add(info);
			} else {
				list3.add(info);
			}
		}
		pager.setAdapter(new MatchPagereAdapter(getSupportFragmentManager(), list1, list2, list3));
		title_tv.setText(type == 1 ? getString(R.string.mainfrag1_m1)
				: type == 2 ? getString(R.string.mainfrag1_m2)
						: type == 3 ? getString(R.string.mainfrag1_m3)
								: type == 4 ? getString(R.string.mainfrag1_m4)
										: type == 5 ? getString(R.string.mainfrag1_m5)
												: getString(R.string.mainfrag1_m6));
		strip.setViewPager(pager); // 这是其所handle的ViewPager
		strip.setUnderlineHeight(3); // 设置标签栏下边的间隔线高度，单位像素search
		GlobeScopeUtils.Put("search", datas);

		strip.setShouldExpand(true);
		strip.setIndicatorColorResource(R.color.Dgreen);
		strip.setTextColorResource(R.color.deep_grey);
		strip.setSelectedTextColorResource(R.color.Dgreen);
		strip.setIndicatorHeight(6); // 设置Indicator 游标 高度，单位像素
		findViewById(R.id.MatchList_search).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
		switch (v.getId()) {
		case R.id.MatchList_search:
			ActChangeUtils.ChangeActivity(this, SearchMatchActivity.class);
			break;

		default:
			break;
		}
	}

}
