package com.peng.saishi.activity;

import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupClickListener;

import com.peng.saishi.R;
import com.peng.saishi.activity.base.BaseBackActivity;
import com.peng.saishi.adapter.SettingAdapter;

public class SettingActivity extends BaseBackActivity implements OnGroupClickListener{

	private ExpandableListView listview;
	@Override
	public void init() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_setting);
		
		listview = (ExpandableListView) findViewById(R.id.setting_listview);
		listview.setAdapter(new SettingAdapter(this));
		int count= listview.getCount();
		for(int i=0;i<count;i++){
			listview.expandGroup(i);
		}
		listview.setOnGroupClickListener(this);
		super.init();
		
	}
	@Override
	public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
		// TODO Auto-generated method stub
		return true;
	}
	
}
