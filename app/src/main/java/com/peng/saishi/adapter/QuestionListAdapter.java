package com.peng.saishi.adapter;

import java.util.List;
import android.annotation.SuppressLint;
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

public class QuestionListAdapter extends BaseAdapter {
	private Context context;
	private List<QuestionInfo> list;

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list==null?0: list.size();
	}

	public List<QuestionInfo> getList() {
		return list;
	}

	public void setList(List<QuestionInfo> list) {
		this.list = list;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	public QuestionListAdapter(Context context, List<QuestionInfo> list) {
		super();
		this.context = context;
		this.list = list;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@SuppressLint("InflateParams")
	@Override
	public View getView(int position, View view, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder;
		final QuestionInfo info = list.get(position);
		if (view == null) {
			holder = new ViewHolder();
			view = LayoutInflater.from(context).inflate(
					R.layout.item_questionlist, null);
			ViewUtils.inject(holder, view);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		holder.time_tv.setText("\t\t" + info.getTime());
		holder.title_tv.setText(info.getTitle());
		holder.name_tv.setText("\t\t" + info.getUsername());
		holder.visit_tv.setText("\t\t" + info.getVisit());
		holder.content_tv.setText(info.getContent());
		
		FrescoUtils.Display(holder.image,
				AppConfig.Userpic_path + info.getUserid(), 100, 100,false);
		view.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ActChangeUtils.ChangeActivity((Activity) context,
						AnswerActivity.class, "question", info);
			}
		});
		return view;
	}

	class ViewHolder {
		@ViewInject(R.id.item_questionlist_content)
		TextView content_tv;
		@ViewInject(R.id.item_questionlist_title)
		TextView title_tv;
		@ViewInject(R.id.item_questionlist_time)
		TextView time_tv;
		@ViewInject(R.id.item_questionlist_visit)
		TextView visit_tv;
		@ViewInject(R.id.item_questionlist_username)
		TextView name_tv;
		@ViewInject(R.id.item_questionlist_imageView1)
		SimpleDraweeView image;
	}

}
