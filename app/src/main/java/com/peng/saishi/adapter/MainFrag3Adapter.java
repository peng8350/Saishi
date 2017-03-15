package com.peng.saishi.adapter;

import java.util.List;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import com.facebook.drawee.view.SimpleDraweeView;
import com.peng.saishi.R;
import com.peng.saishi.activity.ChatActivity;
import com.peng.saishi.activity.MainActivity;
import com.peng.saishi.entity.TeamInfo;
import com.peng.saishi.entity.config.AppConfig;
import com.peng.saishi.fragment.Main_frag4;
import com.peng.saishi.utils.FrescoUtils;
import com.peng.saishi.utils.GlobeScopeUtils;
import com.peng.saishi.widget.pinnedheaderlistview.SectionedBaseAdapter;

@SuppressLint("InflateParams")
public class MainFrag3Adapter extends SectionedBaseAdapter {

	private List<TeamInfo> group1;

	@Override
	public Object getItem(int section, int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int section, int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	public MainFrag3Adapter(List<TeamInfo> group1, Context context, List<TeamInfo> group2) {
		super();
		this.group1 = group1;
		this.context = context;
		this.group2 = group2;
	}

	@Override
	public int getSectionCount() {
		// TODO Auto-generated method stub
		return 2;
	}

	@Override
	public int getCountForSection(int section) {
		// TODO Auto-generated method stub
		return section == 0 ? group1.size() : group2.	size();
	}

	@Override
	public View getItemView(int section, int position, View view, ViewGroup parent) {
		// TODO Auto-generated method stub
		final ViewHolder vh;
		if (view == null) {
			vh = new ViewHolder();
			view = LayoutInflater.from(context).inflate(R.layout.item_frag3, null);
			vh.tv1 = (TextView) view.findViewById(R.id.textView1);
			vh.tv2 = (TextView) view.findViewById(R.id.Seeperson_intro);
			vh.iv1 = (SimpleDraweeView) view.findViewById(R.id.Seeperson_head);
			view.setTag(vh);
		} else {
			vh = (ViewHolder) view.getTag();
		}
		final TeamInfo info = section == 0 ? group1.get(position) : group2.get(position);

		vh.tv1.setText(info.getName());
		vh.tv2.setText(info.getIntro());
		FrescoUtils.Display(vh.iv1, AppConfig.Teanpic_path+info.getId(), 120, 120,false);

		view.setClickable(true);

		view.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub\
				Intent intent = new Intent(context, ChatActivity.class);
				MainActivity act = (MainActivity) context;
				GlobeScopeUtils.Put("conversation",
						((Main_frag4) act.getPagers().get(3)).getConvers().get(info.getGroupid()+""));
				intent.putExtra("team", info);
				((Activity) context).startActivityForResult(intent, 333);
			}
		});
		return view;
	}

	@Override
	public View getSectionHeaderView(int section, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub

		View view = LayoutInflater.from(context).inflate(R.layout.group_frag3, null);
		int size = section == 0 ? group1.size() : group2.size();
		((TextView) view.findViewById(R.id.textView1))
				.setText(section == 0 ? "我发起的组队(" + size + ")" : "我加入的组队(" + size + ")");
		return view;

	}

	public List<TeamInfo> getGroup1() {
		return group1;
	}

	public void setGroup1(List<TeamInfo> group1) {
		this.group1 = group1;
	}

	public List<TeamInfo> getGroup2() {
		return group2;
	}

	public void setGroup2(List<TeamInfo> group2) {
		this.group2 = group2;
	}

	private Context context;
	private List<TeamInfo> group2;

	class ViewHolder {
		TextView tv1, tv2;
		SimpleDraweeView iv1;
	}

}
