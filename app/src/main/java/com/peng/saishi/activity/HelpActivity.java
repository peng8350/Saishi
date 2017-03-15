package com.peng.saishi.activity;

import java.util.ArrayList;
import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.peng.saishi.R;
import com.peng.saishi.activity.base.BaseBackActivity;
import com.peng.saishi.adapter.HelpPagerAdapter;
import com.peng.saishi.fragment.AdviceFrag;
import com.peng.saishi.fragment.HelpFrag;
import com.peng.saishi.widget.pagertab.PagerSlidingTabStrip;

public class HelpActivity extends BaseBackActivity {
	private ViewPager pager;
	private PagerSlidingTabStrip strip;
	private List<Fragment> list;

	@Override
	public void init() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_help);
		super.init();
		pager = (ViewPager) findViewById(R.id.Help_pager);
		strip = (PagerSlidingTabStrip) findViewById(R.id.Help_pagerSliding);
		// 嘉数据
		pager.setAdapter(new HelpPagerAdapter(getSupportFragmentManager(), initFrags()));
		strip.setViewPager(pager); // 这是其所handle的ViewPager
		strip.setUnderlineHeight(3); // 设置标签栏下边的间隔线高度，单位像素
		strip.setShouldExpand(true);
		strip.setIndicatorColorResource(R.color.Dgreen);
		strip.setTextColorResource(R.color.deep_grey);
		strip.setSelectedTextColorResource(R.color.Dgreen);
		strip.setIndicatorHeight(6); // 设置Indicator 游标 高度，单位像素
	}

	private List<Fragment> initFrags() {
		// TODO Auto-generated method stub
		list = new ArrayList<Fragment>();
		list.add(new HelpFrag());
		list.add(new AdviceFrag());
		return list;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
	}

}
