package com.peng.saishi.adapter;

import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

public class HelpPagerAdapter extends MyPagerAdapter{

	public HelpPagerAdapter(FragmentManager fm, List<Fragment> list) {
		super(fm, list);
		// TODO Auto-generated constructor stub
	}
	
	
	@Override
	public CharSequence getPageTitle(int position) {
		// TODO Auto-generated method stub
		return position==0?"帮助":"反馈";
	}

}
