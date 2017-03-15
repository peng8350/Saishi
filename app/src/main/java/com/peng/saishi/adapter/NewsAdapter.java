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
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.peng.saishi.R;
import com.peng.saishi.entity.NewInfo;
import com.peng.saishi.entity.config.AppConfig;
import com.peng.saishi.utils.FrescoUtils;

@SuppressLint("InflateParams")
public class NewsAdapter extends BaseAdapter {

	private Context context;
	private List<NewInfo> list;
	private boolean nodata;
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list==null?0:list.size();
	}

	public NewsAdapter(Context context, List<NewInfo> list) {
		super();

	}

	public NewsAdapter(Context context, List<NewInfo> list,
			boolean nodata) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.list = list;
		this.nodata =nodata;
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
		ViewHolder holder;
		final NewInfo newInfo = list.get(position);
		if (view == null) {
			holder = new ViewHolder();
			view = LayoutInflater.from(context).inflate(R.layout.item_news,
					null);

			ViewUtils.inject(holder, view);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		// 初始化数据源
		if (!nodata) {
			
			FrescoUtils.Display(holder.image, AppConfig.New_Path+newInfo.getId(), 150, 150,true);
		}
		else{
			int res_id=0;
			try {
				res_id = Integer.parseInt(R.drawable.class
						.getField("new" + newInfo.getId())
						.get(null).toString());
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoSuchFieldException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			FrescoUtils.Display(holder.image,"res://com.peng.saishi/"+res_id,
					150, 150,true);
		}
		holder.title.setText(newInfo.getTitle());
		
		return view;

	}

	class ViewHolder {
		@ViewInject(R.id.textView1)
		TextView title;
		@ViewInject(R.id.imageView1)
		SimpleDraweeView image;
	}

}
