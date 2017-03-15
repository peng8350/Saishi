package com.peng.saishi.fragment;

import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.RadioGroup.LayoutParams;
import com.peng.saishi.R;
import com.peng.saishi.activity.MatchActivity;
import com.peng.saishi.activity.MatchList2Activity;
import com.peng.saishi.activity.MatchListActivity;
import com.peng.saishi.adapter.MainFrag1bottomAdapter;
import com.peng.saishi.adapter.MyPagerAdapter;
import com.peng.saishi.entity.MatchInfo;
import com.peng.saishi.fragment.base.LazyFragment;
import com.peng.saishi.utils.ActChangeUtils;
import com.peng.saishi.utils.GlobeScopeUtils;
import com.peng.saishi.utils.MatchUtils;
import com.peng.saishi.widget.HorizontalListView;
import com.peng.saishi.widget.autoviewpager.AutoScrollViewPager;

import java.util.ArrayList;
import java.util.List;

public class Main_frag1 extends LazyFragment implements OnPageChangeListener,
		OnClickListener, OnItemClickListener {
	AutoScrollViewPager viewpager;
	RadioGroup pages_indictors;
	private TextView middle1, middle2, middle3, middle4, middle5, middle6;
	private TextView bottom_tv1, bottom_tv2, bottom_tv3, pager_title;
	private HorizontalListView bottom_lv1, bottom_lv2, bottom_lv3;
	private List<MatchInfo> list1, list2, list3;// 对应底部的３个bottom数据,bottom_lv...
	private List<MatchInfo> piclist;// 对应顶部哪个图片数据列表,不同的比赛bean
	private List<Fragment> pic_frags;
	private View bottoms[];

	public Main_frag1() {
		super(R.layout.main_frag1);
		// TODO Auto-generated constructor stub
	}

	// 获取中间的图片
	public Drawable[] getdrawables() {
		Drawable[] drawables = new Drawable[6];
		drawables[0] = getResources().getDrawable(R.drawable.main_frag1_1);
		drawables[1] = getResources().getDrawable(R.drawable.main_frag1_2);
		drawables[2] = getResources().getDrawable(R.drawable.main_frag1_3);
		drawables[3] = getResources().getDrawable(R.drawable.main_frag1_4);
		drawables[4] = getResources().getDrawable(R.drawable.main_frag1_5);
		drawables[5] = getResources().getDrawable(R.drawable.main_frag1_6);
		return drawables;
	}

	View layout;

	@SuppressWarnings("deprecation")
	@Override
	protected View initViews(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		layout = inflater.inflate(layoudi, null);
		piclist = new ArrayList<>();
		pic_frags = new ArrayList<>();
		viewpager = (AutoScrollViewPager) layout
				.findViewById(R.id.main_frag1_autoviewpager);
		// 得到图片
		middle1 = (TextView) layout.findViewById(R.id.frag1_middle1);
		middle2 = (TextView) layout.findViewById(R.id.frag1_middle2);
		middle3 = (TextView) layout.findViewById(R.id.frag1_middle3);
		middle4 = (TextView) layout.findViewById(R.id.frag1_middle4);
		middle5 = (TextView) layout.findViewById(R.id.frag1_middle5);
		middle6 = (TextView) layout.findViewById(R.id.frag1_middle6);
		bottoms = new View[3];
		bottoms[0] = layout.findViewById(R.id.frag1_bottom1);
		bottoms[1] = layout.findViewById(R.id.frag1_bottom2);
		bottoms[2] = layout.findViewById(R.id.frag1_bottom3);

		bottom_tv1 = (TextView) bottoms[0].findViewById(R.id.frag1_bottom_tv);
		bottom_tv2 = (TextView) bottoms[1].findViewById(R.id.frag1_bottom_tv);
		bottom_tv3 = (TextView) bottoms[2].findViewById(R.id.frag1_bottom_tv);
		bottom_lv1 = (HorizontalListView) bottoms[0]
				.findViewById(R.id.frag1_horlistview);
		bottom_lv2 = (HorizontalListView) bottoms[1]
				.findViewById(R.id.frag1_horlistview);
		bottom_lv3 = (HorizontalListView) bottoms[2]
				.findViewById(R.id.frag1_horlistview);
		bottoms[0].setOnClickListener(this);
		bottoms[1].setOnClickListener(this);
		bottoms[2].setOnClickListener(this);
		pages_indictors = (RadioGroup) layout.findViewById(R.id.radioGroup1);
		middle1.setOnClickListener(this);
		middle2.setOnClickListener(this);
		middle3.setOnClickListener(this);
		middle4.setOnClickListener(this);
		middle5.setOnClickListener(this);
		middle6.setOnClickListener(this);
		viewpager.setInterval(2000);
		viewpager.setCycle(true);
		viewpager.startAutoScroll();
		viewpager.invalidate();
		viewpager.setOnPageChangeListener(this);
		viewpager.refreshDrawableState();
		// 当触摸的时候，停止轮播
		viewpager.setStopScrollWhenTouch(true);
		viewpager.setBorderAnimation(true);
		bottom_lv1.setOnItemClickListener(this);
		bottom_lv2.setOnItemClickListener(this);
		bottom_lv3.setOnItemClickListener(this);
		return layout;
	}

	public void updateBottom(int which,MatchInfo info){
		HorizontalListView update_listview = which==1?bottom_lv1:which==2?bottom_lv2:bottom_lv3;
		List<MatchInfo> update_list = which==1?list1:which==2?list2:list3;
		BaseAdapter adapter = (BaseAdapter) update_listview.getAdapter();
		update_list.add(0, info);
		piclist.add(0,info);
		piclist.remove(6);
		adapter.notifyDataSetChanged();
	}
	
	@Override
	protected void initData() {
		// TODO Auto-generated method stub
		AssetManager assetmanager = getActivity().getAssets();
		Typeface typeface = Typeface.createFromAsset(assetmanager, "ww.ttf");
		bottom_tv1.setTypeface(typeface);
		bottom_tv2.setTypeface(typeface);
		bottom_tv3.setTypeface(typeface);
		bottom_tv1.setText("猜你喜欢");
		bottom_tv2.setText("热门比赛");
		bottom_tv3.setText("最新比赛");
		bottom_tv1.setCompoundDrawablesWithIntrinsicBounds(getResources()
				.getDrawable(R.drawable.mainfrag1_badge1), null, getResources()
				.getDrawable(R.drawable.forward), null);
		bottom_tv2.setCompoundDrawablesWithIntrinsicBounds(getResources()
				.getDrawable(R.drawable.mainfrag1_badge2), null, getResources()
				.getDrawable(R.drawable.forward), null);
		bottom_tv3.setCompoundDrawablesWithIntrinsicBounds(getResources()
				.getDrawable(R.drawable.mainfrag1_badge3), null, getResources()
				.getDrawable(R.drawable.forward), null);
		middle1.setText(getString(R.string.mainfrag1_m1));
		middle2.setText(getString(R.string.mainfrag1_m2));
		middle3.setText(getString(R.string.mainfrag1_m3));
		middle4.setText(getString(R.string.mainfrag1_m4));
		middle5.setText(getString(R.string.mainfrag1_m5));
		middle6.setText(getString(R.string.mainfrag1_m6));
		Drawable[] middle_drawables = getdrawables();
		pager_title = (TextView) layout.findViewById(R.id.frag1_pagertitle);
		middle1.setCompoundDrawablesWithIntrinsicBounds(null,
				middle_drawables[0], null, null);
		middle2.setCompoundDrawablesWithIntrinsicBounds(null,
				middle_drawables[1], null, null);
		middle3.setCompoundDrawablesWithIntrinsicBounds(null,
				middle_drawables[2], null, null);
		middle4.setCompoundDrawablesWithIntrinsicBounds(null,
				middle_drawables[3], null, null);
		middle5.setCompoundDrawablesWithIntrinsicBounds(null,
				middle_drawables[4], null, null);
		middle6.setCompoundDrawablesWithIntrinsicBounds(null,
				middle_drawables[5], null, null);
		initBigData();
	}

	// 初始化一些很大类型的集合
	private void initBigData() {
		// TODO Auto-generated method stub
		list1 = MatchUtils.getPart2Match15Limit(
				(List<MatchInfo>) GlobeScopeUtils.getMatches(), 1);
		list2 = MatchUtils.getPart2Match15Limit(
				(List<MatchInfo>) GlobeScopeUtils.getMatches(), 2);
		list3 = MatchUtils.getPart2Match15Limit(
				(List<MatchInfo>) GlobeScopeUtils.getMatches(), 3);
		if (list1 != null && list1.size() > 1) {
			piclist.add(list1.get(1));
			piclist.add(list1.get(0));
		}
		if (list2 != null && list2.size() > 1) {
			piclist.add(list2.get(0));
			piclist.add(list2.get(1));
			piclist.add(list2.get(2));
		}
		if (list3 != null && list3.size() > 1) {

			piclist.add(list3.get(0));
			piclist.add(list3.get(1));
			piclist.add(list3.get(2));
		}
		bottom_lv1.setAdapter(new MainFrag1bottomAdapter(getActivity(), list1));

		bottom_lv2.setAdapter(new MainFrag1bottomAdapter(getActivity(), list2));
		bottom_lv3.setAdapter(new MainFrag1bottomAdapter(getActivity(), list3));
		for (int i = 0; i < piclist.size(); i++) {
			RadioButton btn = new RadioButton(getActivity());
			LayoutParams params = new RadioGroup.LayoutParams(15, 15);
			params.setMargins(5, 0, 5, 0);
			btn.setLayoutParams(params);
			btn.setBackgroundResource(R.drawable.dot);
			btn.setClickable(false);
			btn.setButtonDrawable(android.R.color.transparent);
			pic_frags.add(new PicFragment(piclist.get(i)));
			pages_indictors.addView(btn);
		}
		viewpager.setAdapter(new MyPagerAdapter(getChildFragmentManager(),
				pic_frags));
		onPageSelected(0);

	}

	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		viewpager.stopAutoScroll();
		super.onDestroyView();

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
		((RadioButton) pages_indictors.getChildAt(arg0)).setChecked(true);
		pager_title.setText(piclist.get(arg0).getName());
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.frag1_bottom1:
			ActChangeUtils.ChangeActivity(getActivity(),
					MatchList2Activity.class, "type", 1);
			break;
		case R.id.frag1_bottom2:
			ActChangeUtils.ChangeActivity(getActivity(),
					MatchList2Activity.class, "type", 2);
			break;
		case R.id.frag1_bottom3:
			ActChangeUtils.ChangeActivity(getActivity(),
					MatchList2Activity.class, "type", 3);
			break;
		case R.id.frag1_middle1:
			ActChangeUtils.ChangeActivity(getActivity(),
					MatchListActivity.class, "type", 1);
			break;
		case R.id.frag1_middle2:
			ActChangeUtils.ChangeActivity(getActivity(),
					MatchListActivity.class, "type", 2);
			break;
		case R.id.frag1_middle3:
			ActChangeUtils.ChangeActivity(getActivity(),
					MatchListActivity.class, "type", 3);
			break;
		case R.id.frag1_middle4:
			ActChangeUtils.ChangeActivity(getActivity(),
					MatchListActivity.class, "type", 4);
			break;
		case R.id.frag1_middle5:
			ActChangeUtils.ChangeActivity(getActivity(),
					MatchListActivity.class, "type", 5);
			break;
		case R.id.frag1_middle6:
			ActChangeUtils.ChangeActivity(getActivity(),
					MatchListActivity.class, "type", 6);
			break;
		default:
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub

		ActChangeUtils.ChangeActivity(
				getActivity(),
				MatchActivity.class,
				"match",
				parent == bottom_lv1 ? list1.get(position)
						: parent == bottom_lv2 ? list2.get(position) : list3
								.get(position));
	}

	@Override
	protected void setDefaultFragmentTitle(String title) {
		// TODO Auto-generated method stub

	}
	
	//更新比赛列表,以及上面的内容
	

}
