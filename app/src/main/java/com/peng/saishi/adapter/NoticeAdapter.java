package com.peng.saishi.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
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
import com.peng.saishi.activity.MatchActivity;
import com.peng.saishi.entity.MatchInfo;
import com.peng.saishi.entity.NoticeInfo;
import com.peng.saishi.entity.config.AppConfig;
import com.peng.saishi.interfaces.observer.SwipeDismissTouchListener;
import com.peng.saishi.interfaces.observer.SwipeDismissTouchListener.OnDismissCallback;
import com.peng.saishi.manager.MatchManager;
import com.peng.saishi.utils.FrescoUtils;

public class NoticeAdapter extends BaseAdapter {

	private Context context;
	private List<NoticeInfo> list;

	public NoticeAdapter(Context context, List<NoticeInfo> list) {
		super();
		this.context = context;
		this.list = list;
	}

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
	public View getView(final int position, View view, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder;
		if (view == null) {
			holder = new ViewHolder();
			view = LayoutInflater.from(context).inflate(R.layout.item_notice,
					null);
			ViewUtils.inject(holder, view);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		final NoticeInfo info = list.get(position);
		holder.content_tv.setText(info.getContent());
		holder.time_tv.setText(info.getTime());
		holder.name_tv.setText(info.getName());
		FrescoUtils.Display(holder.match_image,
				AppConfig.Matchpic_path + info.getTargetid(), 72, 72,false);
		view.setOnTouchListener(new SwipeDismissTouchListener(view, null,
				new OnDismissCallback() {

					@Override
					public void onDismiss(View view, Object token) {
						// TODO Auto-generated method stub
							list.remove(position);
							info.delete();
							notifyDataSetChanged();
					}
				}));
		view.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				MatchInfo target = MatchManager.getMatchById(info.getTargetid());
				Intent intent = new Intent(context, MatchActivity.class);
				intent.putExtra("match", target);
				context.startActivity(intent);
			}
		});
		return view;
	}

	class ViewHolder {
		@ViewInject(R.id.item_notice_image)
		SimpleDraweeView match_image;
		@ViewInject(R.id.item_notice_time)
		TextView time_tv;
		@ViewInject(R.id.item_notice_name)
		TextView name_tv;
		@ViewInject(R.id.item_notice_forward)
		TextView forward_tv;
		@ViewInject(R.id.item_notice_content)
		TextView content_tv;
	}

}
