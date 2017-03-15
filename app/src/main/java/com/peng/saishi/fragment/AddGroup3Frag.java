package com.peng.saishi.fragment;

import java.util.ArrayList;
import java.util.List;

import android.view.View;
import android.widget.TextView;

import com.peng.saishi.R;
import com.peng.saishi.fragment.base.BaseFragment;
import com.peng.saishi.widget.TagCloudView;
import com.peng.saishi.widget.TagCloudView.OnTagClickListener;

public class AddGroup3Frag extends BaseFragment {

	private TagCloudView tagcloud1, tagcloud2;
	private List<String> datas1, datas2;
	private TextView finish_btn;

	public AddGroup3Frag(TextView finish_btn, List<String> tags) {
		super(R.layout.frag_addgroup3);
		this.finish_btn = finish_btn;
		this.datas1 = tags;
		// TODO Auto-generated constructor stub
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		datas2 = new ArrayList<>();
		tagcloud1 = (TagCloudView) layout.findViewById(R.id.Addgroup3_tagcloudview1);
		tagcloud2 = (TagCloudView) layout.findViewById(R.id.Addgroup3_tagcloudview2);
		datas2.add("高技术");
		datas2.add("985 211");
		datas2.add("211以上");
		datas2.add("本科学校");
		datas2.add("大专学校");
		datas2.add("硕士");
		datas2.add("技术牛人");
		datas2.add("半年该方面经验");
		datas2.add("C9院校");
		datas2.add("会配合");
		datas2.add("PS");
		datas2.add("Java");
		datas2.add("Android");
		datas2.add("IOS");
		datas2.add("Asp.net");
		datas2.add("Jsp");
		datas2.add("Php");
		datas2.add("算法");
		datas2.add("寻求同道");
		datas2.add("效率");
		datas2.add("高目标");
		datas2.add("长远梦想");
		datas2.add("寻找队友");
		datas2.add("长期合作");

		tagcloud1.setOnTagClickListener(new OnTagClickListener() {

			@Override
			public void onTagClick(int position) {
				// TODO Auto-generated method stub
				datas2.add(datas1.get(position));
				datas1.remove(position);
				tagcloud1.setTags(datas1);
				tagcloud2.setTags(datas2);
				finish_btn.setVisibility(datas1.size() >= 3 ? View.VISIBLE : View.GONE);
			}
		});
		tagcloud2.setOnTagClickListener(new OnTagClickListener() {

			@Override
			public void onTagClick(int position) {
				// TODO Auto-generated method stub
				datas1.add(datas2.get(position));
				datas2.remove(position);
				finish_btn.setVisibility(datas1.size() >= 3 ? View.VISIBLE : View.GONE);
				tagcloud1.setTags(datas1);
				tagcloud2.setTags(datas2);
			}
		});
		tagcloud1.setTags(datas1);
		tagcloud2.setTags(datas2);
	}

}
