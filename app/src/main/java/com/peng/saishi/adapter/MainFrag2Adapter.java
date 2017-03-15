package com.peng.saishi.adapter;

import java.util.List;
import android.annotation.SuppressLint;
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
import com.peng.saishi.activity.TeamInfoActivity;
import com.peng.saishi.entity.TeamInfo;
import com.peng.saishi.entity.config.AppConfig;
import com.peng.saishi.utils.FrescoUtils;

@SuppressLint("InflateParams")
public class MainFrag2Adapter extends BaseAdapter {

	public List<TeamInfo> getList() {
		return list;
	}

	public void setList(List<TeamInfo> list) {
		this.list = list;
	}

	private List<TeamInfo> list;
	private Context context;

	@Override
	public int getCount() {
		// TODO Auto-genesysorated method stub
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

	public MainFrag2Adapter(List<TeamInfo> list, Context context) {
		super();
		this.list = list;
		this.context = context;
		System.out.println(list.size() + " asd");
	}

	// 获取最后一个
	public int getLastId() {
		if (list == null || list.size() == 0) {
			return 0;
		}
		return list.get(list.size() - 1).getId();
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		// TODO Auto-generated method stub
		final ViewHolder vh;
		System.out.println("getview");
		if (view == null) {
			vh = new ViewHolder();
			view = LayoutInflater.from(context).inflate(R.layout.item_frag2,
					null);
			vh.tv_groupname = (TextView) view.findViewById(R.id.textView1);
			vh.tv_matchname = (TextView) view
					.findViewById(R.id.Seeperson_xueli);
			vh.tv_needperson = (TextView) view.findViewById(R.id.textView3);
			vh.iv_groupicon = (SimpleDraweeView) view
					.findViewById(R.id.imageView1);
			vh.tv_type = (TextView) view.findViewById(R.id.TextView01);
			vh.tv_time = (TextView) view.findViewById(R.id.textView4);
			view.setTag(vh);
		} else {
			vh = (ViewHolder) view.getTag();
		}
		final TeamInfo info = list.get(position);
		// 设置数据源
		// 在这里，要设置一个图片给vh.groupicon
		FrescoUtils.Display(vh.iv_groupicon,
				AppConfig.Teanpic_path + info.getId(), 150, 150,true);
		vh.tv_type.setText("类型:\t" + info.getType());
		vh.tv_groupname.setText(info.getName());
		vh.tv_matchname.setText(info.getMatchName());
		vh.tv_needperson.setText(info.getNowperson() + "/"
				+ info.getNeedperson());
		vh.tv_time.setText(info.getTime());
		view.setClickable(true);
		view.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Intent intent = new Intent(context, TeamInfoActivity.class);
				intent.putExtra("showqr", false);
				intent.putExtra("team", info);
				context.startActivity(intent);

			}
		});
		return view;
	}

	static class ViewHolder {
		TextView tv_matchname, tv_groupname, tv_time, tv_needperson, tv_type;
		SimpleDraweeView iv_groupicon;
	}

}
