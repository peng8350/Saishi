package com.peng.saishi.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.peng.saishi.R;
import com.peng.saishi.entity.MatchInfo;

@SuppressLint("InflateParams")
public class MatchAdapter extends BaseAdapter{
	private String[] titles,contents;
	private Context context;
	public MatchAdapter(MatchInfo info, Context context) {
		super();
		this.context = context;
		titles = info.getTitles().split(" ");
		contents = info.getContent().split("/t/a");
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return titles==null?0:titles.length;
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
			view = LayoutInflater.from(context).inflate(R.layout.item_match, null);
			vh.title = (TextView) view.findViewById(R.id.item_match_textView2);
			vh.content = (TextView) view.findViewById(R.id.item_match_textView4);
			view.setTag(vh);
		} else {
			vh = (ViewHolder) view.getTag();
		}
		//设置数据源
		vh.title.setText(titles[position]);
		vh.content.setText(contents[position]);

		return view;
	}

	
	class ViewHolder{
		TextView title,content;
	}
}
