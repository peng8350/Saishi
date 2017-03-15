package com.peng.saishi.fragment.base;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class BaseFragment extends Fragment {
	protected View layout;
	private int layoutid;
	
	
	public BaseFragment(int layoutid) {
		super();
		this.layoutid = layoutid;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		layout = inflater.inflate(layoutid, null);
		init();
		return layout;
	}
	
	public abstract void init();

}
