package com.peng.saishi.adapter;

import java.util.List;

import com.peng.saishi.R;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class HelpArticleAdapter extends BaseAdapter {
	private Context context;
	private List<List<String>> contents;

	public HelpArticleAdapter(Context context, List<List<String>> contents) {
		super();
		this.context = context;
		this.contents = contents;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return contents==null?0:contents.get(0).size();
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
			view = LayoutInflater.from(context).inflate(R.layout.item_helparticle, null);
			vh.tv = (TextView) view.findViewById(R.id.textView1);
			vh.image = (ImageView) view.findViewById(R.id.imageView1);
			view.setTag(vh);
		} else {
			vh = (ViewHolder) view.getTag();
		}
		vh.tv.setText(contents.get(1).get(position));
		if (!contents.get(0).get(position).equals("null")) {
			int id=0;
			try {
				id = Integer
						.parseInt(R.drawable.class.getField( contents.get(0).get(position)).get(null).toString());
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
			vh.image.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), id));
		}

		return view;
	}

	class ViewHolder {
		TextView tv;
		ImageView image;
	}
}
