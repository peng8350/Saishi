package com.peng.saishi.fragment;

import java.util.List;

import android.widget.ListView;

import com.peng.saishi.R;
import com.peng.saishi.adapter.MatchListAdapter;
import com.peng.saishi.entity.MatchInfo;
import com.peng.saishi.fragment.base.BaseFragment;

public class MatchListFrag extends BaseFragment{
	
	

	public MatchListFrag(int layoutid, List<MatchInfo> list) {
		super(layoutid);
		this.list = list;
	}
	private ListView listview;
	private List<MatchInfo> list;

	@Override
	public void init() {
		// TODO Auto-generated method stub
		listview =(ListView) layout.findViewById(R.id.frag_matchlist_listView1);
		listview.setAdapter(new MatchListAdapter(getActivity(), list));
		
	}
}
