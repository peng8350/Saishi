package com.peng.saishi.adapter;

import java.util.ArrayList;
import java.util.List;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.peng.saishi.R;
import com.peng.saishi.activity.ReplyActivity;
import com.peng.saishi.entity.Pingjia;

@SuppressLint("InflateParams")
public class ReplyAdapter extends BaseAdapter {
	private Context context;
	private List<Pingjia> list;
	private int match_id;
	public ReplyAdapter(Context context, List<Pingjia> list,int match_id) {
		super();
		this.context = context;
		this.list = list;
		this.match_id = match_id;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if (list.size()>=16) {
			return 15;
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

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder vh;
		if (view == null) {
			vh = new ViewHolder();
			view = LayoutInflater.from(context).inflate(R.layout.item_reply, null);
			vh.name = (TextView) view.findViewById(R.id.item_reply_textView1);
			vh.content = (TextView) view.findViewById(R.id.item_reply_textView2);
			vh.time = (TextView) view.findViewById(R.id.item_reply_TextView01);
			view.setTag(vh);
		} else {
			vh = (ViewHolder) view.getTag();
		}
		Pingjia info = list.get(position);

		vh.name.setText(info.getName());
		vh.content.setText(info.getContent());
		vh.time.setText(info.getTime());
		view.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(context, ReplyActivity.class);
				intent.putParcelableArrayListExtra("replys", (ArrayList<? extends Parcelable>) list);
				intent.putExtra("match_id", match_id);
				context.startActivity(intent);
			}
		});
		return view;
	}

	class ViewHolder {
		TextView name, content, time;
	}

}
