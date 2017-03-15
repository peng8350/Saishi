package com.peng.saishi.adapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.peng.saishi.R;
import com.peng.saishi.activity.SeePersonActivity;
import com.peng.saishi.entity.AnswerInfo;
import com.peng.saishi.entity.User;
import com.peng.saishi.entity.config.AppConfig;
import com.peng.saishi.interfaces.service.MyStringCallBack;
import com.peng.saishi.utils.ActChangeUtils;
import com.peng.saishi.utils.FrescoUtils;
import com.peng.saishi.utils.GlobeScopeUtils;
import com.peng.saishi.utils.JsonParser;
import com.peng.saishi.utils.ToastUtils;
import com.zhy.http.okhttp.OkHttpUtils;

public class AnswerAdapter extends BaseAdapter {
	// 来源的消息
	public static int FROM_ANSWER = 0;
	// 发送的消息
	public static int TO_ANSWER = 1;
	private Context context;
	private List<AnswerInfo> list;

	public AnswerAdapter(Context context, List<AnswerInfo> list) {
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
	public int getViewTypeCount() {
		// TODO Auto-generated method stub
		return 2;
	}

	@Override
	public int getItemViewType(int position) {
		// TODO Auto-generated method stub
		AnswerInfo info = list.get(position);
		if (info.getUserid() != GlobeScopeUtils.getMeId()) {
			System.out.println("对方");
			return FROM_ANSWER;
		} else {
			System.out.println("自己");
			return TO_ANSWER;
		}
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder;
		final AnswerInfo info = list.get(position);
		if (view == null) {
			holder = new ViewHolder();
			if (info.getUserid() != GlobeScopeUtils.getMeId()) {
				view = LayoutInflater.from(context).inflate(
						R.layout.item_answer_from, null);
				System.out.println("对方view");
			} else {
				System.out.println("自己view");
				view = LayoutInflater.from(context).inflate(
						R.layout.item_answer_to, null);
			}
			ViewUtils.inject(holder, view);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		holder.name_tView.setText(info.getUsername());
		holder.time_tv.setText("发表于:\t" + info.getTime());
		FrescoUtils.Display(holder.head,
				AppConfig.Userpic_path + info.getUserid(), 100, 100,false);
		holder.content_tv.setText(info.getContent());
		holder.head.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Map<String, String> params = new HashMap<>();
				params.put("id", info.getUserid() + "");
				OkHttpUtils.post().url(AppConfig.getUser).params(params)
						.build().execute(new MyStringCallBack() {

							@Override
							public void onResponse(String response, int id) {
								// TODO Auto-generated method stub
								if (id!=0) {
									ToastUtils.ShowNetError();
									return ;
								}
								try {
									User user = JsonParser.getBeanFromJson(
											new JSONObject(response),
											User.class);

									ActChangeUtils.ChangeActivity(
											(Activity) context,
											SeePersonActivity.class, "user",
											user);
								} catch (JSONException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}

							@Override
							public void onError(Call call, Exception e, int id) {
								// TODO Auto-generated method stub
								ToastUtils.ShowNetError();
							}
						});
			}
		});
		return view;
	}

	class ViewHolder {
		@ViewInject(R.id.item_answer_name)
		TextView name_tView;
		@ViewInject(R.id.item_answer_time)
		TextView time_tv;
		@ViewInject(R.id.item_answer_content)
		TextView content_tv;
		@ViewInject(R.id.item_answer_head)
		SimpleDraweeView head;
	}

}
