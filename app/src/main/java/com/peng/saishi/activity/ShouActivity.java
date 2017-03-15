package com.peng.saishi.activity;

import java.util.List;

import android.view.View;
import android.widget.ListView;

import com.peng.saishi.R;
import com.peng.saishi.activity.base.BaseBackActivity;
import com.peng.saishi.adapter.MatchListAdapter;
import com.peng.saishi.entity.MatchInfo;
import com.peng.saishi.manager.NoDataManager;
import com.peng.saishi.utils.GlobeScopeUtils;

public class ShouActivity extends BaseBackActivity {

	private ListView listview;
	private List<MatchInfo> matches;
	private View nodata_layout;

	@Override
	public void init() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_shoucang);
		super.init();
		listview = (ListView) findViewById(R.id.activity_shoucang_listView1);
		nodata_layout = NoDataManager.initNodata("你没有收藏任何比赛!!!", this);
		matches =GlobeScopeUtils.getShoucangs();
		listview.setAdapter(new MatchListAdapter(ShouActivity.this, matches));
		NoDataManager.ChangeState(nodata_layout, matches);
	}

}
