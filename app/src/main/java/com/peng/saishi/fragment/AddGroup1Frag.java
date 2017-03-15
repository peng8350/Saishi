package com.peng.saishi.fragment;

import java.util.List;

import android.widget.ListView;

import com.peng.saishi.R;
import com.peng.saishi.adapter.MatchListAdapter;
import com.peng.saishi.entity.MatchInfo;
import com.peng.saishi.fragment.base.BaseFragment;
import com.peng.saishi.utils.GlobeScopeUtils;

public class AddGroup1Frag extends BaseFragment {

	private ListView listView;
	private List<MatchInfo> matches;// 收藏的比赛大全
	public AddGroup1Frag() {
		super(R.layout.frag_addgroup1);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		listView = (ListView) layout.findViewById(R.id.frag_addgroup1_listView1);
		matches=GlobeScopeUtils.getShoucangs();
		if (getActivity()==null) {
			return;
		}
		listView.setAdapter(new MatchListAdapter(getActivity(), matches));	
	}
}
