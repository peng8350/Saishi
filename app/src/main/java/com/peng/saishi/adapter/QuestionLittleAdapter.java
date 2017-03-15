package com.peng.saishi.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.peng.saishi.R;
import com.peng.saishi.activity.AnswerActivity;
import com.peng.saishi.entity.QuestionInfo;
import com.peng.saishi.entity.config.AppConfig;
import com.peng.saishi.utils.ActChangeUtils;
import com.peng.saishi.utils.FrescoUtils;

public class QuestionLittleAdapter extends BaseAdapter {
	private Context context;
	private List<QuestionInfo> list;

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if (list != null && list.size() > 7) {
			return 7;
		}
		return list == null ? 0 : list.size();
	}

	public QuestionLittleAdapter(Context context, List<QuestionInfo> list) {
		super();
		this.context = context;
		this.list = list;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder = new ViewHolder();
		final QuestionInfo info = list.get(position);
		if (view == null) {
			holder = new ViewHolder();
			view = LayoutInflater.from(context).inflate(
					R.layout.item_matchfooter, null);
			ViewUtils.inject(holder, view);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		holder.name_tv.setText(info.getUsername());
		holder.title_tv.setText(info.getTitle());
		holder.time_tv.setText(info.getTime());
		FrescoUtils.Display(holder.head_iv,
				AppConfig.Userpic_path + info.getUserid(), 80, 80,false);
		view.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ActChangeUtils.ChangeActivity((Activity)context, AnswerActivity.class,"question",info);
			}
		});
		return view;
	}

	class ViewHolder {
		@ViewInject(R.id.item_matchfooter_name)
		TextView name_tv;
		@ViewInject(R.id.item_matchfooter_title)
		TextView title_tv;
		@ViewInject(R.id.item_matchfooter_time)
		TextView time_tv;
		@ViewInject(R.id.item_matchfooter_head)
		SimpleDraweeView head_iv;
	}

}
