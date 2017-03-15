package com.peng.saishi.adapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.peng.saishi.R;
import com.peng.saishi.entity.Pingjia;
import com.peng.saishi.entity.User;
import com.peng.saishi.entity.config.AppConfig;
import com.peng.saishi.interfaces.service.MyStringCallBack;
import com.peng.saishi.utils.FrescoUtils;
import com.peng.saishi.utils.GlobeScopeUtils;
import com.peng.saishi.utils.ToastUtils;
import com.zhy.http.okhttp.OkHttpUtils;

public class ReplyListAdapter extends BaseAdapter {
	private Context context;
	private List<Pingjia> list;
	private int myid = 0;

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
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

	public ReplyListAdapter(Context context, List<Pingjia> list) {
		super();
		this.context = context;
		this.list = list;
		myid = GlobeScopeUtils.getMeId();
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder vh;
		if (view == null) {
			vh = new ViewHolder();
			view = LayoutInflater.from(context).inflate(R.layout.item_replylist,null);
			vh.name = (TextView) view.findViewById(R.id.textView1);
			vh.content = (TextView) view.findViewById(R.id.textView2);
			vh.time = (TextView) view.findViewById(R.id.textView3);
			vh.bad = (TextView) view.findViewById(R.id.textView4);
			vh.good = (TextView) view.findViewById(R.id.textView5);
			vh.head = (SimpleDraweeView) view.findViewById(R.id.Seeperson_head);
			view.setTag(vh);
		} else {
			vh = (ViewHolder) view.getTag();
		}
		final Pingjia info = list.get(position);
		//对应数据源设置进去
		FrescoUtils.Display(vh.head, AppConfig.Userpic_path+info.getPicUrl(), 80, 80,false);
		vh.name.setText(info.getName());
		vh.content.setText(info.getContent());
		vh.time.setText(info.getTime());
		vh.good.setText(info.getGood().equals("")?0+"":(info.getGood().split(" ").length+""));
		vh.bad.setText(info.getBad().equals("")?0+"":info.getBad().split(" ").length+"");
		String[] clickgoods= info.getGood().split(" ");
		String[] clickbads = info.getBad().split(" ");
		for(String id:clickgoods){
			if (id.equals(String.valueOf(myid))) {
				vh.good.setEnabled(false);
				break;
			}
		}
		for(String id:clickbads){
			if (id.equals(String.valueOf(myid))) {
				vh.bad.setEnabled(false);
				break;
			}
		}
		vh.good.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Map<String, String> params = new HashMap<>();
				params.put("id",info.getId()+"");
				params.put("userid",myid+"");
				OkHttpUtils.post().url(AppConfig.ClickGood).params(params).build().execute(new MyStringCallBack() {
					
					@Override
					public void onResponse(String response, int id) {
						// TODO Auto-generated method stub

						// TODO Auto-generated method stub
						try {
							if (id!=0) {
								ToastUtils.ShowNetError();
								return ;
							}
							JSONObject res_obj =  new JSONObject(response);
							if (res_obj.get("result").equals("ok")) {
								info.setGood(info.getGood()+((User)GlobeScopeUtils.Get("ME")).getId()+" ");
								notifyDataSetChanged();
							}
							else{
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					
					}
					
					@Override
					public void onError(Call call, Exception e, int id) {
						// TODO Auto-generated method stub
						
					}
				});
			}
		});
		vh.bad.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// TODO Auto-generated method stub
				Map<String, String> params = new HashMap<>();
				params.put("id",info.getId()+"");
				params.put("userid",myid+"");
				OkHttpUtils.post().url(AppConfig.ClickBad).params(params).build().execute(new MyStringCallBack() {
					
					@Override
					public void onResponse(String response, int id) {
						// TODO Auto-generated method stub

						// TODO Auto-generated method stub
						try {
							if (id!=0) {
								ToastUtils.ShowNetError();
								return ;
							}
							JSONObject res_obj =  new JSONObject(response);
							if (res_obj.get("result").equals("ok")) {
								info.setBad(info.getBad()+((User)GlobeScopeUtils.Get("ME")).getId()+" ");
								notifyDataSetChanged();
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					
					}
					
					@Override
					public void onError(Call call, Exception e, int id) {
						// TODO Auto-generated method stub
						
					}
				});
			}
		});
		return view;
	}

	public List<Pingjia> getList() {
		return list;
	}

	public void setList(List<Pingjia> list) {
		this.list = list;
	}

	class ViewHolder {
		TextView time, content, name, good, bad;
		SimpleDraweeView head;
	}

}
