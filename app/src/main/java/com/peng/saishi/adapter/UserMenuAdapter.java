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
import android.widget.PopupWindow;
import android.widget.TextView;
import com.facebook.drawee.view.SimpleDraweeView;
import com.peng.saishi.R;
import com.peng.saishi.activity.LoginActivity;
import com.peng.saishi.entity.User;
import com.peng.saishi.entity.config.AppConfig;
import com.peng.saishi.utils.FrescoUtils;

@SuppressLint("InflateParams")
public class UserMenuAdapter extends BaseAdapter {
	private Context context;
	private List<User> users;
	private PopupWindow window;

	public UserMenuAdapter(Context context, List<User> users, PopupWindow window) {
		super();
		this.context = context;
		this.window = window;
		this.users = users;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if (users == null) {
			return 0;
		}
		return users.size();
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
	public View getView(final int position, View view, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder vh;
		if (view == null) {
			vh = new ViewHolder();
			view = LayoutInflater.from(context).inflate(R.layout.item_usershow,
					null);
			vh.head = (SimpleDraweeView) view.findViewById(R.id.imageView1);
			vh.name = (TextView) view.findViewById(R.id.textView1);
			vh.close = (ImageView) view.findViewById(R.id.imageView2);

			view.setTag(vh);
		} else {
			vh = (ViewHolder) view.getTag();
		}
		final User info = users.get(position);
		vh.name.setText(info.getUsername());
		FrescoUtils.Display(vh.head, AppConfig.Userpic_path + info.getId(), 90,
				90, false);
		vh.close.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				info.delete();
				users.remove(position);
				notifyDataSetChanged();

			}
		});
		view.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				LoginActivity act = (LoginActivity) context;
				act.setUserIntoEt(info);
				window.dismiss();
			}
		});
		return view;
	}

	class ViewHolder {
		TextView name;
		SimpleDraweeView head;
		ImageView close;

	}

}
