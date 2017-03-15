package com.peng.saishi.adapter;

import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.peng.saishi.R;
import com.peng.saishi.activity.ChatActivity;
import com.peng.saishi.entity.JoinGroup;
import com.peng.saishi.entity.MessageInfo;
import com.peng.saishi.entity.TeamInfo;
import com.peng.saishi.entity.config.AppConfig;
import com.peng.saishi.utils.FrescoUtils;
import com.peng.saishi.utils.GlobeScopeUtils;
import com.peng.saishi.widget.BadgeView;

public class MainFrag4Adapter extends BaseAdapter {
	private Context context;
	private List<MessageInfo> list;
	private List<JoinGroup> joins;

	public MainFrag4Adapter(Context context, List<MessageInfo> list) {
		super();
		this.context = context;
		this.list = list;
	}

	public List<MessageInfo> getList() {
		return list;
	}

	public void setList(List<MessageInfo> list) {
		this.list = list;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if (list == null) {
			return 0;
		}
		return list.size();
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
	
	public TeamInfo getTarget(int teamid){
		for(TeamInfo t:GlobeScopeUtils.getUserTeam1()){
			if (teamid==t.getId()) {
				return t;
			}
		}
		for(TeamInfo t1:GlobeScopeUtils.getUserTeam2()){
			if (teamid==t1.getId()) {
				return t1;
			}
		}
		return null;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		// TODO Auto-generated method stub
		final ViewHolder vh;
		if (view == null) {
			vh = new ViewHolder();
			view = LayoutInflater.from(context).inflate(R.layout.item_frag4,
					null);
			vh.content = (TextView) view.findViewById(R.id.Seeperson_intro);
			vh.name = (TextView) view.findViewById(R.id.textView1);
			vh.time = (TextView) view.findViewById(R.id.Seeperson_age);
			vh.head = (SimpleDraweeView) view
					.findViewById(R.id.item_frag4_image);
			vh.badgeView = (BadgeView) view.findViewById(R.id.badgeView1);
			view.setTag(vh);
		} else {
			vh = (ViewHolder) view.getTag();
		}
		final MessageInfo info = list.get(position);
		FrescoUtils.Display(vh.head, AppConfig.Teanpic_path + info.getTeamid(), 120, 120,true);
		if (info.getType() == 1) {

			vh.content.setText(info.getContent());
		} else if (info.getType() == 2) {
			vh.content.setText("[图片]");
		} else if(info.getType()==3){
			vh.content.setText("[语音]''");
		}
		else{
			vh.content.setText("[文件]''");
		}
		vh.name.setText(info.getName());
		vh.time.setText(com.peng.saishi.utils.TimeUtils.dateToStrLong(new Date(
				info.getTime())));
		if (info.getWeidu() > 0) {
			vh.badgeView.setText(info.getWeidu() + "");
			vh.badgeView.setVisibility(View.VISIBLE);
		} else {
			vh.badgeView.setVisibility(View.GONE);
		}
		view.setClickable(true);
		view.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				TeamInfo target = getTarget(info.getTeamid());
				Intent intent = new Intent(context, ChatActivity.class);
				intent.putExtra("team", target);
				GlobeScopeUtils.Put("conversation", info.getConver());
				((Activity) context).startActivityForResult(intent, 333);
				info.getConver().resetUnreadCount();

			}
		});
		return view;
	}

	public List<JoinGroup> getJoins() {
		return joins;
	}

	public void setJoins(List<JoinGroup> joins) {
		this.joins = joins;
	}

	class ViewHolder {
		TextView content, name, time;
		SimpleDraweeView head;
		BadgeView badgeView;
	}

}
