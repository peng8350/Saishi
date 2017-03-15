package com.peng.saishi.adapter;

import java.util.List;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.facebook.drawee.view.SimpleDraweeView;
import com.peng.saishi.R;
import com.peng.saishi.entity.MatchInfo;
import com.peng.saishi.entity.config.AppConfig;
import com.peng.saishi.utils.FrescoUtils;

@SuppressLint("InflateParams")
public class MainFrag1bottomAdapter extends BaseAdapter {
	private Context context;
	private List<MatchInfo> list;

	public MainFrag1bottomAdapter(Context context, List<MatchInfo> list) {
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
			view = LayoutInflater.from(context).inflate(R.layout.item_frag1li, null);
			vh.title = (TextView) view.findViewById(R.id.textView1);
			vh.image = (SimpleDraweeView) view.findViewById(R.id.imageView1);
			view.setTag(vh);
		} else {
			vh = (ViewHolder) view.getTag();
		}
		MatchInfo info = list.get(position);
		
		FrescoUtils.Display(vh.image, AppConfig.Matchpic_path + info.getId(), 120, 80,true);
		vh.title.setText(info.getName());
		vh.title.setSelected(true);
		return view;
	}
	
	class ViewHolder{
		SimpleDraweeView image;
		TextView title;
		
	}

}
