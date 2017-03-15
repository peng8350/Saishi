package com.peng.saishi.adapter;

import java.util.List;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;
import android.view.ViewGroup;

public class MyPagerAdapter extends FragmentPagerAdapter{

	private List<Fragment> list;
	public MyPagerAdapter(FragmentManager fm) {
		super(fm);
		// TODO Auto-generated constructor stub
	}
	@Override
	public Fragment getItem(int arg0) {
		// TODO Auto-generated method stub
		return list.get(arg0);
	}
	public List<Fragment> getList() {
		return list;
	}
	public void setList(List<Fragment> list) {
		this.list = list;
	}
	public MyPagerAdapter(FragmentManager fm, List<Fragment> list) {
		super(fm);
		this.list = list;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}
	
	
	@Override
	public void destroyItem(View container, int position, Object object) {
		// TODO Auto-generated method stub
	}
	
	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		// TODO Auto-generated method stub
	}
	
	

}
