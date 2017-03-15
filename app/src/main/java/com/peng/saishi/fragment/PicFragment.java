package com.peng.saishi.fragment;

import android.view.View;
import android.view.View.OnClickListener;

import com.facebook.drawee.view.SimpleDraweeView;
import com.peng.saishi.R;
import com.peng.saishi.activity.MatchActivity;
import com.peng.saishi.entity.MatchInfo;
import com.peng.saishi.entity.config.AppConfig;
import com.peng.saishi.fragment.base.BaseFragment;
import com.peng.saishi.utils.ActChangeUtils;

public class PicFragment extends BaseFragment implements OnClickListener {
	SimpleDraweeView imageView;
	private MatchInfo info;
	public PicFragment(MatchInfo info) {
		super(R.layout.frag_pic);
		this.info = info;
		// TODO Auto-generated constructor stub
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		imageView = (SimpleDraweeView) layout.findViewById(R.id.frag_pic_image);
		imageView.setImageURI(AppConfig.Matchpic_path+info.getId());
		imageView.setClickable(true);
		imageView.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.frag_pic_image:
			// TODO Auto-generated method stub
			ActChangeUtils.ChangeActivity(getActivity(), MatchActivity.class,"match",info);
			break;

		default:
			break;
		}
	}

}
