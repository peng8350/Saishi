package com.peng.saishi.activity;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.peng.saishi.R;
import com.peng.saishi.activity.base.BaseBackActivity;
import com.peng.saishi.entity.NewInfo;
import com.peng.saishi.entity.config.AppConfig;
import com.peng.saishi.utils.ActChangeUtils;
import com.peng.saishi.utils.FrescoUtils;
import com.peng.saishi.utils.SystemUtils;

public class NewsActivity extends BaseBackActivity {

	private NewInfo info;
	@ViewInject(R.id.News_content)
	private TextView content_tv;
	@ViewInject(R.id.News_title)
	private TextView title_tv;
	@ViewInject(R.id.News_time)
	private TextView time_tv;
	@ViewInject(R.id.News_image)
	private SimpleDraweeView pic_view;

	@Override
	public void init() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_news);
		super.init();
		ViewUtils.inject(this);
		initData();
	}

	// 初始化textview的数据源
	private void initData() {
		// TODO Auto-generated method stub
		info = (NewInfo) getIntent().getSerializableExtra("News");
		title_tv.setText(info.getTitle());
		time_tv.setText("时间:\t\t\t" + info.getTime());
		boolean nodata = getIntent().getBooleanExtra("nodata", false);
		if (!nodata) {

			FrescoUtils.Display(pic_view, AppConfig.New_Path + info.getId(),
					SystemUtils.getAppWidth(), 200,false);
		} else {
			int res_id=0;
			try {
				res_id = Integer.parseInt(R.drawable.class
						.getField("new" + info.getId())
						.get(null).toString());
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoSuchFieldException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			FrescoUtils.Display(pic_view,"res://com.peng.saishi/"+res_id,
					SystemUtils.getAppWidth(), 200,false);
		}
		content_tv.setText(info.getContent());
	}

	// 点击了图片
	@OnClick(R.id.News_image)
	public void ClickImage(View v) {
		Intent intent = new Intent(this, PhotoActivity.class);
		intent.putExtra("id", info.getId() + "");
		intent.putExtra("type", 5);
		startActivity(intent);
	}

	@OnClick(R.id.News_btn)
	public void EnterWeb(View v) {
		// 进入网络界面
		ActChangeUtils.ChangeActivity(this, WebActivity.class, "url",
				info.getWebUrl());
	}

}
