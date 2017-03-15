package com.peng.saishi.adapter;

import android.content.Context;

import com.peng.saishi.widget.WheelView.adapters.AbstractWheelTextAdapter;

public class MyWheelAdapter extends AbstractWheelTextAdapter {


	public MyWheelAdapter(Context context,  String[] list) {
		super(context);
		this.list = list;
	}
	private String[] list;

	@Override
	public int getItemsCount() {
		// TODO Auto-generated method stub
		return list.length;
	}
	@Override
	protected CharSequence getItemText(int index) {
		// TODO Auto-generated method stub
		return list[index];
	}

}
