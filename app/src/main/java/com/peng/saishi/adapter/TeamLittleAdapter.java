package com.peng.saishi.adapter;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.facebook.drawee.view.SimpleDraweeView;
import com.peng.saishi.R;
import com.peng.saishi.entity.User;
import com.peng.saishi.entity.config.AppConfig;
import com.peng.saishi.utils.FrescoUtils;

//比赛页面的水平成员界面
@SuppressLint("InflateParams")
public class TeamLittleAdapter extends BaseAdapter {
	private Context context;
	private List<User> list;

	public TeamLittleAdapter(Context context, List<User> list) {
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

	@SuppressLint({ "ResourceAsColor", "ViewHolder" })
	@Override
	public View getView(int position, View view, ViewGroup parent) {
		// TODO Auto-generated method stub
		User info = list.get(position);
		ViewHolder holder;
		if (view==null) {
			holder= new ViewHolder();
			view = LayoutInflater.from(context).inflate(R.layout.item_teamlittle,null);
			holder.draweeView = (SimpleDraweeView) view.findViewById(R.id.item_teamlittle_image);
			view.setTag(holder);
		}
		else{
			holder = (ViewHolder) view.getTag();
		}
		FrescoUtils.Display(holder.draweeView, AppConfig.Userpic_path + info.getId(), 100,
				100,false);
		return view;
	}
	
	static class ViewHolder{
		SimpleDraweeView draweeView;
	}

}
