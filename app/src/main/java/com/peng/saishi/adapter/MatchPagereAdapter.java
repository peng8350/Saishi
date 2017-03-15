package com.peng.saishi.adapter;

import java.util.List;

import com.peng.saishi.R;
import com.peng.saishi.entity.MatchInfo;
import com.peng.saishi.fragment.MatchListFrag;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class MatchPagereAdapter extends FragmentPagerAdapter{
	private List<MatchInfo> list1,list2,list3;
	@Override
	public Fragment getItem(int arg0) {
		// TODO Auto-generated method stub
		return new MatchListFrag(R.layout.frag_matchlist, arg0==0?list1:arg0==1?list2:list3);
	}
	public MatchPagereAdapter(FragmentManager fm, List<MatchInfo> list1, List<MatchInfo> list2, List<MatchInfo> list3) {
		super(fm);
		this.list1 = list1;
		this.list2 = list2;
		this.list3 = list3;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 3;
	}
	public CharSequence getPageTitle(int position) {
		return position==2?"已截止":position==1?"将截止":"可报名";
	};
}
