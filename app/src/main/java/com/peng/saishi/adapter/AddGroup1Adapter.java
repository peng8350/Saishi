package com.peng.saishi.adapter;

import java.util.List;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.peng.saishi.R;
import com.peng.saishi.activity.AddGroupActivity;
import com.peng.saishi.entity.MatchInfo;

@SuppressLint("InflateParams")
public class AddGroup1Adapter extends BaseAdapter {
	private Context context;
	private List<MatchInfo> list;

	public AddGroup1Adapter(Context context, List<MatchInfo> list) {
		super();
		this.context = context;
		this.list = list;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list==null?0:list.size();
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
		ViewHolder vh;
		if (view == null) {
			vh = new ViewHolder();
			view = LayoutInflater.from(context).inflate(R.layout.item_selectmatch, null);
			// 名字
			vh.tv1 = (TextView) view.findViewById(R.id.textView1);
			// 时间

			vh.tv2 = (TextView) view.findViewById(R.id.textView4);
			vh.imageView = (ImageView) view.findViewById(R.id.imageView1);
			view.setTag(vh);
		} else {
			vh = (ViewHolder) view.getTag();
		}

		view.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				AddGroupActivity activity = (AddGroupActivity) context;
				activity.next();
			}
		});
		return view;
	}

	class ViewHolder {
		ImageView imageView;
		TextView tv1, tv2;
	}
}
