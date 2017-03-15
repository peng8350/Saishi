package com.peng.saishi.adapter;

import java.io.File;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.MainThread;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.peng.saishi.R;
import com.peng.saishi.activity.FileMoreAct;
import com.peng.saishi.activity.MyFIleActivity;
import com.peng.saishi.entity.MyFIleInfo;
import com.peng.saishi.utils.FrescoUtils;

public class MyFileAdapter extends BaseAdapter {
	private Context context;
	private List<MyFIleInfo> list;
	private boolean needupdate;
	

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list == null ? 0 : list.size();
	}

	public boolean isNeedupdate() {
		return needupdate;
	}

	public void setNeedupdate(boolean needupdate) {
		this.needupdate = needupdate;
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

	@SuppressLint("InflateParams")
	@Override
	public View getView(final int position, View view, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder;
		if (view == null) {
			holder = new ViewHolder();
			view = LayoutInflater.from(context).inflate(R.layout.item_myfile,
					null);
			ViewUtils.inject(holder, view);

			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		final MyFIleInfo info = list.get(position);
		holder.tv_name.setText(info.getName());
		holder.tv_size.setText(info.getSize());
		holder.tv_time.setText(info.getCreate_time());

		// 根据end来判断,得到resid
		String end = info.getEnd();

		final int resid = end.equals("avi") || end.equals("mp4")
				|| end.equals("3gp") || end.equals("rmvb") ? R.drawable.filetype1
				: end.equals("doc") ? R.drawable.filetype2 : end.equals("m4a")
						|| end.equals("mp3") || end.equals("mid")
						|| end.equals("xmf") || end.equals("ogg")
						|| end.equals("wav") ? R.drawable.filetype3 : end
						.equals("apk") ? R.drawable.filetype4 : end
						.equals("jpg")
						|| end.equals("gif")
						|| end.equals("png")
						|| end.equals("jpeg")
						|| end.equals("bmp") ? R.drawable.filetype5 : end
						.equals("txt") ? R.drawable.filetype6
						: R.drawable.filetype7;
		FrescoUtils.Display(holder.icon, "res://com.peng.saishi/" + resid, 80,
				80, false);
		holder.btn_view.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// 查看详情
				Intent intent = new Intent(context, FileMoreAct.class);
				intent.putExtra("info", info);
				intent.putExtra("resid", resid);
				context.startActivity(intent);
				
			}
		});
		
		holder.btn_del.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// 删除这个文件
				
				new File(info.getParent()+"/"+info.getName()).delete();
				list.remove(position);
				needupdate = true;
			}
		});
		return view;
	}

	public MyFileAdapter(Context context, List<MyFIleInfo> list) {
		super();
		this.context = context;
		this.list = list;
	}

	class ViewHolder {
		@ViewInject(R.id.item_myfile_name)
		TextView tv_name;
		@ViewInject(R.id.item_myfile_size)
		TextView tv_size;
		@ViewInject(R.id.item_myfile_riv)
		SimpleDraweeView icon;
		@ViewInject(R.id.item_myfile_time)
		TextView tv_time;
		@ViewInject(R.id.item_myfileback_btn1)
		Button btn_del;
		@ViewInject(R.id.item_myfileback_btn2)
		Button btn_view;
	}

}
