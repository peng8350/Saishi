package com.peng.saishi.activity;

import java.util.List;

import android.view.View;
import android.widget.ListView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.peng.saishi.R;
import com.peng.saishi.activity.base.BaseBackActivity;
import com.peng.saishi.adapter.NoticeAdapter;
import com.peng.saishi.entity.NoticeInfo;
import com.peng.saishi.manager.NoDataManager;
import com.peng.saishi.utils.GlobeScopeUtils;

/**
 * 
 * 这里需要使用到dbutils，使用最强的框架来提高APP的性能优化
 */

public class NoticeActivity extends BaseBackActivity {

	@ViewInject(R.id.Notice_listview)
	private ListView listView;
	// 数据源
	private List<NoticeInfo> notices;

	private View nodata_layout;
	private NoticeAdapter adapter;

	@Override
	public void init() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_notice);
		super.init();
		ViewUtils.inject(this);
		nodata_layout = NoDataManager.initNodata("没有比赛通知哦", this);
		notices = getNotices();
		adapter = new NoticeAdapter(this, notices);
		listView.setAdapter(adapter);

		NoDataManager.ChangeState(nodata_layout, notices);
	}

	private List<NoticeInfo> getNotices() {
		// TODO Auto-generated method stub
		List<NoticeInfo> params = NoticeInfo.findWithQuery(NoticeInfo.class,
				"select * from Notice_Info where userid = ? order by id desc	",  GlobeScopeUtils.getMeId() + "" );
		// 通过调用数据库来得到数据
		return params;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
	}

}
