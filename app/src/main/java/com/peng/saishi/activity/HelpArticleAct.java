package com.peng.saishi.activity;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import com.peng.saishi.R;
import com.peng.saishi.activity.base.BaseBackActivity;
import com.peng.saishi.adapter.HelpArticleAdapter;
import com.peng.saishi.entity.HelpInfo;

public class HelpArticleAct extends BaseBackActivity {

	private ListView listview;
	private TextView title_tv;

	@SuppressLint("InflateParams")
	@Override
	public void init() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_helparticle);
		super.init();
		listview = (ListView) findViewById(R.id.HelpArticle_listView1);
		title_tv = (TextView) findViewById(R.id.HelpArticle_title);
		String title = getIntent().getStringExtra("title");
		title_tv.setText(title);
		HelpInfo info = (HelpInfo) getIntent().getSerializableExtra("info");
		View header_view = LayoutInflater.from(this).inflate(R.layout.header_helparticle, null);
		((TextView) header_view.findViewById(R.id.textView1)).setText(info.getTitle());
		listview.addHeaderView(header_view);
		listview.setAdapter(new HelpArticleAdapter(this, info.getContents()));
		
	}

}
