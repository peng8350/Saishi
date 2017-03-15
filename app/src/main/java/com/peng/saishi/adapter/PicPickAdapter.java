package com.peng.saishi.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import cn.finalteam.galleryfinal.GalleryFinal.OnHanlderResultCallback;
import cn.finalteam.galleryfinal.model.PhotoInfo;

import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.view.SimpleDraweeView;
import com.peng.saishi.R;
import com.peng.saishi.manager.PickManager;
import com.peng.saishi.utils.FrescoUtils;
import com.peng.saishi.utils.SystemUtils;

public class PicPickAdapter extends BaseAdapter implements OnHanlderResultCallback{
	private Context context;
	private List<String> paths;

	public PicPickAdapter(Context context) {
		super();
		this.context = context;
		paths=new ArrayList<>();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if (paths.size()>=9) {
			return 9;
		}
		return paths.size() + 1;
		
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
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		SimpleDraweeView imageView = new SimpleDraweeView(context);
		imageView.setLayoutParams(new AbsListView.LayoutParams(SystemUtils
				.getAppWidth() / 3 - 20, SystemUtils.getAppWidth() / 3 - 20));
		GenericDraweeHierarchy hierarchy =imageView.getHierarchy();
		hierarchy.setPlaceholderImage(R.drawable.loading2,com.facebook.drawee.drawable.ScalingUtils.ScaleType.CENTER_CROP);
		if (position == paths.size()) {
			FrescoUtils.Display(imageView, "res://com.peng.saishi/"
					+ R.drawable.icon_addpic_unfocused,
					SystemUtils.getAppWidth() / 3 - 20,
					SystemUtils.getAppWidth() / 3 - 20, false);
			imageView.setClickable(true);
			imageView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					PickManager.getAlmera(context, PicPickAdapter.this, false);
				}
			});
		} else {
			
			FrescoUtils.Display(imageView, "file://"+paths.get(position),SystemUtils.getAppWidth() / 3 - 20,
					SystemUtils.getAppWidth() / 3 - 20, false);
		}
		return imageView;
	}


	public List<String> getPaths() {
		return paths;
	}

	public void setPaths(List<String> paths) {
		this.paths = paths;
	}

	@Override
	public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {
		// TODO Auto-generated method stub
		paths.add(resultList.get(0).getPhotoPath());
		notifyDataSetChanged();
	}

	@Override
	public void onHanlderFailure(int requestCode, String errorMsg) {
		// TODO Auto-generated method stub
		
	}

}
