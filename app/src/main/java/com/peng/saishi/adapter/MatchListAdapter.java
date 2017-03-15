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
import com.peng.saishi.R;
import com.peng.saishi.activity.AddGroupActivity;
import com.peng.saishi.activity.MatchActivity;
import com.peng.saishi.activity.SearchMatchActivity;
import com.peng.saishi.activity.ShouActivity;
import com.peng.saishi.entity.MatchInfo;
import com.peng.saishi.entity.config.AppConfig;
import com.peng.saishi.utils.ActChangeUtils;
import com.peng.saishi.utils.FrescoUtils;

@SuppressLint("InflateParams")
public class MatchListAdapter extends BaseAdapter {

	private Context context;
	private List<MatchInfo> list;

	public List<MatchInfo> getList() {
		return list;
	}

	public void setList(List<MatchInfo> list) {
		this.list = list;
	}

	public MatchListAdapter(Context context, List<MatchInfo> list) {
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
	public View getView(int position, View view, ViewGroup parent) {
		// TODO Auto-generated method stub
		final ViewHolder vh;
		if (view == null) {
			vh = new ViewHolder();
			view = LayoutInflater.from(context).inflate(
					R.layout.item_matchlist, null);
			// 名字
			vh.tv1 = (TextView) view.findViewById(R.id.textView1);
			// zhuban单位
			vh.tv3 = (TextView) view.findViewById(R.id.Seeperson_age);
			// 地点时间
			vh.tv4 = (TextView) view.findViewById(R.id.textView4);
			// 好评度
			vh.tv5 = (TextView) view.findViewById(R.id.Seeperson_xueli);
			vh.imageView = (SimpleDraweeView) view
					.findViewById(R.id.imageView1);
			view.setTag(vh);
		} else {
			vh = (ViewHolder) view.getTag();
		}
		final MatchInfo info = list.get(position);
		vh.tv1.setText(info.getName());
		vh.tv3.setText("主办单位:\t" + info.getZhuban());
		vh.tv4.setText(info.getTime() + "截止  " + info.getPlace());
		vh.tv5.setText(info.getScore() + "");
		FrescoUtils.Display(vh.imageView,
				AppConfig.Matchpic_path + info.getId(), 150, 150, true);
		view.setClickable(true);
		view.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (context.getClass().equals(ShouActivity.class)) {
					// 假如是收藏的
					ActChangeUtils.ChangeActivity((Activity) context,
							MatchActivity.class, "match", info);
				} else if (context.getClass().equals(AddGroupActivity.class)) {
					AddGroupActivity act = (AddGroupActivity) context;
					act.getValues()[2] = info.getName();
					act.getValues()[8] = info.getFenlei();
					act.getValues()[9] = info.getId() + "";
					act.getValues()[10] = info.getTime();
					act.next();

				} else {
					ActChangeUtils.ChangeActivity((Activity) context,
							MatchActivity.class, "match", info);
				}
				if (context.getClass().equals(SearchMatchActivity.class)) {
					((Activity) context).finish();
				}
			}
		});
		return view;
	}

	class ViewHolder {
		SimpleDraweeView imageView;
		TextView tv1, tv3, tv4, tv5;
	}

}
