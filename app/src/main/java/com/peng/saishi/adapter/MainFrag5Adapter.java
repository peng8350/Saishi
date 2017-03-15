package com.peng.saishi.adapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import android.app.Activity;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.peng.saishi.R;
import com.peng.saishi.activity.JoinGroupAct;
import com.peng.saishi.activity.MyFIleActivity;
import com.peng.saishi.activity.MyQuestionAct;
import com.peng.saishi.activity.SeePersonActivity;
import com.peng.saishi.activity.SettingActivity;
import com.peng.saishi.activity.ShouActivity;
import com.peng.saishi.activity.UpdatePswdActivity;
import com.peng.saishi.entity.StringIconInfo;
import com.peng.saishi.entity.config.AppConfig;
import com.peng.saishi.fragment.Main_frag5;
import com.peng.saishi.interfaces.service.MyStringCallBack;
import com.peng.saishi.utils.ActChangeUtils;
import com.peng.saishi.utils.GlobeScopeUtils;
import com.peng.saishi.utils.ToastUtils;
import com.peng.saishi.widget.BadgeView;
import com.zhy.http.okhttp.OkHttpUtils;

public class MainFrag5Adapter extends BaseAdapter {

	private Context context;
	private List<StringIconInfo> list;
	private int unread;

	public MainFrag5Adapter() {
		// TODO Auto-generated constructor stub
	}

	public MainFrag5Adapter(Context context, List<StringIconInfo> list,
			int unread) {
		super();
		this.context = context;
		this.list = list;
		this.unread = unread;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	public int getUnread() {
		return unread;
	}

	public void setUnread(int unread) {
		this.unread = unread;
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
			view = LayoutInflater.from(context).inflate(R.layout.item_frag5,
					null);
			vh.title = (TextView) view.findViewById(R.id.textView1);
			vh.badge = (BadgeView) view.findViewById(R.id.badgeView1);
			vh.image = (ImageView) view.findViewById(R.id.imageView1);
			view.setTag(vh);
		} else {
			vh = (ViewHolder) view.getTag();
		}
		StringIconInfo info = list.get(position);
		vh.title.setText(info.getName());
		vh.badge.setVisibility(position ==5 ? View.VISIBLE : View.GONE);
		if (position == 5) {
			vh.badge.setText(unread + "");
			vh.badge.setVisibility(unread == 0 ? View.GONE : View.VISIBLE);
		}

		vh.image.setImageBitmap(BitmapFactory.decodeResource(
				context.getResources(), info.getIcon()));

		view.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (position == 6) {
					// 设置界面
					ActChangeUtils.ChangeActivityForResult((Activity) context,
							SettingActivity.class, Main_frag5.UPDATEICON);
				} else if (position == 5) {
					// 组队历史
					Map<String, String> params = new HashMap<>();
					params.put("id", GlobeScopeUtils.getMeId()
							+ "");
					OkHttpUtils.post().url(AppConfig.ClearUnRead).params(params).build().execute(new MyStringCallBack() {
						
						@Override
						public void onResponse(String response, int id) {
							// TODO Auto-generated method stub
							if (id!=0) {
								ToastUtils.ShowNetError();
								return ;
							}
						}
						
						@Override
						public void onError(Call call, Exception e, int id) {
							// TODO Auto-generated method stub
							
						}
					});
					ActChangeUtils.ChangeActivity((Activity) context,
							JoinGroupAct.class);
				} 
				else if(position==4){
					//我的文件
					ActChangeUtils.ChangeActivity((Activity) context, MyFIleActivity.class);
				}
				else if(position==3){
					//我的问题
					ActChangeUtils.ChangeActivity((Activity) context, MyQuestionAct.class);
				}
				else if (position == 2) {
					// 我的收藏
					ActChangeUtils.ChangeActivity((Activity) context,
							ShouActivity.class);
				} else if (position == 1) {
					// 修改密码
					ActChangeUtils.ChangeActivity((Activity) context,
							UpdatePswdActivity.class);
				} else {
					// 个人资料
					ActChangeUtils.ChangeActivity((Activity) context,
							SeePersonActivity.class, "isme", true);
				}
			}
		});
		return view;
	}

	static class ViewHolder {
		ImageView image;
		TextView title;
		BadgeView badge;
	}
}
