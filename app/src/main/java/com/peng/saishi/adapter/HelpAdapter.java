package com.peng.saishi.adapter;

import java.util.List;

import com.peng.saishi.R;
import com.peng.saishi.activity.HelpArticleAct;
import com.peng.saishi.entity.HelpInfo;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

public class HelpAdapter extends BaseExpandableListAdapter {
	private Context context;
	private List<HelpInfo> list1;
	private List<HelpInfo> list2;
	public HelpAdapter(Context context, List<HelpInfo> list1,List<HelpInfo> list2) {
		super();
		this.context = context;
		this.list1 = list1;
		this.list2 =  list2;
	}

	@Override
	public int getGroupCount() {
		// TODO Auto-generated method stub
		return 2;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		// TODO Auto-generated method stub
		return groupPosition == 0 ?list1.size():list2.size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getGroupId(int groupPosition) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View layout = LayoutInflater.from(context).inflate(R.layout.group_help, null);
		TextView textView = (TextView) layout.findViewById(R.id.Seeperson_intro);
		textView.setText(groupPosition == 1 ? "功能教程" : "热门问题");
		return layout;
	}

	@Override
	public View getChildView(final int groupPosition, int childPosition, boolean isLastChild, View view, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder vh;
		if (view == null) {
			vh = new ViewHolder();
			view = LayoutInflater.from(context).inflate(R.layout.item_frag_help, null);
			vh.tv = (TextView) view.findViewById(R.id.textView1);
			view.setTag(vh);
		} else {
			vh = (ViewHolder) view.getTag();
		}
		final HelpInfo info = groupPosition == 0 ? list1.get(childPosition) : list2.get(childPosition);
		vh.tv.setText(info.getTitle());
		view.setClickable(true);
		view.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String title = "";
				if (groupPosition==0) {
					title = "热门问题";
				}
				else{
					title = "功能介绍";
				}
				Intent intent = new Intent(context,HelpArticleAct.class);
				intent.putExtra("title", title);
				intent.putExtra("info", info);
				context.startActivity(intent);
			}
		});
		return view;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return false;
	}

	class ViewHolder {
		TextView tv;
	}

}
