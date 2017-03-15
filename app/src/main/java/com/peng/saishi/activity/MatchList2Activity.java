package com.peng.saishi.activity;

import java.util.List;

import android.view.View;
import android.widget.ListView;

import com.peng.saishi.R;
import com.peng.saishi.activity.base.BaseBackActivity;
import com.peng.saishi.adapter.MatchListAdapter;
import com.peng.saishi.entity.MatchInfo;
import com.peng.saishi.utils.ActChangeUtils;
import com.peng.saishi.utils.GlobeScopeUtils;
import com.peng.saishi.utils.MatchUtils;

public class MatchList2Activity extends BaseBackActivity {

	private ListView listview;
	private List<MatchInfo> matches;
	@Override
	public void init() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_matchlist2);
		super.init();
		
		int type = getIntent().getIntExtra("type", 0);
		listview = (ListView) findViewById(R.id.frag_matchlist2_listView1);
		findViewById(R.id.MatchList2_search).setOnClickListener(this);
		matches = MatchUtils.getPart2Match(GlobeScopeUtils.getMatches(), type);
		listview.setAdapter(new MatchListAdapter(this, matches));
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
		switch (v.getId()) {
		case R.id.MatchList2_search:
			GlobeScopeUtils.Put("search", matches);
			ActChangeUtils.ChangeActivity(this, SearchMatchActivity.class);
			break;

		default:
			break;
		}
	}
}
