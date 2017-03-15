package com.peng.saishi.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
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

public class MyQuestionAdapter extends BaseAdapter {
	public MyQuestionAdapter(Context context, List<QuestionInfo> list) {
		super();
		this.context = context;
		this.list = list;
	}

	private Context context;
	private List<QuestionInfo> list;

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list == null ? 0 : list.size();
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
		final ViewHolder holder;
		if (view == null) {
			holder = new ViewHolder();
			view = LayoutInflater.from(context).inflate(
					R.layout.item_myquestion, null);
			ViewUtils.inject(holder, view);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		final QuestionInfo info = list.get(position);
		holder.box.setVisibility(info.isShow() ? View.VISIBLE : View.GONE);
		holder.box.setChecked(info.isChecked());
		holder.box.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				info.setChecked(isChecked);
			}
		});
		holder.name_tv.setText(info.getMatchName());
		holder.title_tv.setText(info.getTitle());
		holder.time_tv.setText(info.getTime());
		FrescoUtils.Display(holder.head_image,
				AppConfig.Matchpic_path + info.getMatchid(), 100, 100,false);

		view.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (holder.box.isShown()) {
					info.setChecked(!info.isChecked());
					holder.box.setChecked(info.isChecked());
				} else
					ActChangeUtils.ChangeActivity((Activity) context,
							AnswerActivity.class, "question", info);
			}
		});
		return view;

	}

	class ViewHolder {
		@ViewInject(R.id.item_myquestion_checkbox)
		CheckBox box;
		@ViewInject(R.id.item_myquestion_name)
		TextView name_tv;
		@ViewInject(R.id.item_myquestion_time)
		TextView time_tv;
		@ViewInject(R.id.item_myquestion_title)
		TextView title_tv;
		@ViewInject(R.id.item_myquestion_head)
		SimpleDraweeView head_image;
	}

}
