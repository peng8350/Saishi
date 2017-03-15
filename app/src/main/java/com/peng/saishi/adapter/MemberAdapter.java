package com.peng.saishi.adapter;

import java.util.ArrayList;
import java.util.List;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;
import com.facebook.drawee.view.SimpleDraweeView;
import com.peng.saishi.R;
import com.peng.saishi.activity.SeePersonActivity;
import com.peng.saishi.entity.User;
import com.peng.saishi.entity.config.AppConfig;
import com.peng.saishi.utils.FrescoUtils;
import com.peng.saishi.utils.GlobeScopeUtils;

@SuppressLint("InflateParams")
public class MemberAdapter extends BaseAdapter {

	private Context context;
	private List<User> list;
	private boolean isedit;
	private List<String> check_pos;

	public List<User> getList() {
		return list;
	}

	public void setList(List<User> list) {
		this.list = list;
	}

	public boolean isIsedit() {
		return isedit;
	}

	public void setIsedit(boolean isedit) {
		this.isedit = isedit;
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

	public MemberAdapter(Context context, List<User> list) {
		super();
		this.context = context;
		this.list = list;
		check_pos = new ArrayList<>();
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(final int position, View view, ViewGroup parent) {
		// TODO Auto-generated method stub
		final ViewHolder vh;
		if (view == null) {
			vh = new ViewHolder();
			view = LayoutInflater.from(context).inflate(R.layout.item_member,
					null);
			vh.tv = (TextView) view.findViewById(R.id.textView1);
			vh.imageView = (SimpleDraweeView) view
					.findViewById(R.id.imageView1);
			vh.box = (CheckBox) view.findViewById(R.id.checkBox1);
			view.setTag(vh);
		} else {
			vh = (ViewHolder) view.getTag();
		}
		final User info = list.get(position);
		vh.box.setVisibility(GlobeScopeUtils.getMeId() != info.getId()
				&& isedit ? View.VISIBLE : View.GONE);
		if (isedit) {
			vh.box.setChecked(false);
			vh.box.setOnCheckedChangeListener(new OnCheckedChangeListener() {

				@Override
				public void onCheckedChanged(CompoundButton buttonView,
						boolean isChecked) {
					// TODO Auto-generated method stub
					if (isChecked) {
						check_pos.add(position + "");
					} else {
						check_pos.remove(position + "");
					}
				}
			});
		}
		// 根据数据源设置数据
		vh.tv.setText(info.getUsername());
		view.setClickable(true);
		FrescoUtils.Display(vh.imageView, AppConfig.Userpic_path + info.getId(), 130, 130,true);
		view.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (isedit && GlobeScopeUtils.getMeId() != info.getId()) {
					vh.box.setChecked(!vh.box.isChecked());
				} else {
					Intent intent = new Intent(context, SeePersonActivity.class);
					intent.putExtra("isme", false);
					intent.putExtra("user", info);
					context.startActivity(intent);
				}
			}
		});
		return view;
	}

	public List<String> getCheck_pos() {
		return check_pos;
	}

	public void setCheck_pos(List<String> check_pos) {
		this.check_pos = check_pos;
	}

	public void ClearState() {
		check_pos.clear();
	}

	class ViewHolder {
		TextView tv;
		CheckBox box;
		SimpleDraweeView imageView;
	}

}
