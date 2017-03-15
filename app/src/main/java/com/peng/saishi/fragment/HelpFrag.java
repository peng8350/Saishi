package com.peng.saishi.fragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupClickListener;

import com.peng.saishi.R;
import com.peng.saishi.adapter.HelpAdapter;
import com.peng.saishi.entity.HelpInfo;
import com.peng.saishi.fragment.base.BaseFragment;

public class HelpFrag extends BaseFragment implements OnGroupClickListener {

	private ExpandableListView listview;

	public HelpFrag() {
		super(R.layout.frag_help);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		listview = (ExpandableListView) layout.findViewById(R.id.frag_Help_expandableListView1);
		 listview.setAdapter(new HelpAdapter(getActivity(), getData1(),
		 getData2()));
		int count = listview.getCount();
		for (int i = 0; i < count; i++) {
			listview.expandGroup(i);
		}
		listview.setOnGroupClickListener(this);
	}

	private List<HelpInfo> getData2() {
		// TODO Auto-generated method stub
		List<HelpInfo> list = new ArrayList<>();

		list.add(new HelpInfo("队伍讨论功能",
				Arrays.asList(Arrays.asList("help8"), Arrays.asList("在队伍的聊天界面里面,用户可以发表情文字,图片和语音这三种."))));
		list.add(new HelpInfo("队伍二维码", Arrays.asList(Arrays.asList("help9"),
				Arrays.asList("队伍的二维码是拿来提供拖用户进群,以便拉队员的朋友进入队伍里面,通过二维码扫描进入不需要经过队长的同意与否就能进入群里面"))));
		return list;
	}

	private List<HelpInfo> getData1() {
		// TODO Auto-generated method stub
		List<HelpInfo> list = new ArrayList<>();
		list.add(new HelpInfo("为什么组队会被人回绝?", Arrays.asList(Arrays.asList("help1"),
				Arrays.asList("因为大多数人都在乎加入者的这些信息,会挑一些可靠的队友,所以填充好这些信息,展示个人特长,才有人会同意和你组队"))));
		list.add(new HelpInfo("如何和别人组队?",
				Arrays.asList(Arrays.asList("help2", "help3", "help4", "help5"),
						Arrays.asList("首先点击某个队伍,然后点击右上角的按钮,再点击加入队伍(当然,你加入了不会显示)!", "接着,填写备注信息,这点认真填写,以便别人可以明白",
								"队长会收到通知", "如果队长点接受,你就会加入"))));
		list.add(new HelpInfo("扫一扫能实现本App什么功能?",
				Arrays.asList(Arrays.asList("null"), Arrays.asList("扫一扫可以扫描队伍的二维码来加入队伍,还可以扫描其他二维码打开网页"))));
		list.add(new HelpInfo("设置界面背景随头像变化是什么意思?", Arrays.asList(Arrays.asList("help6", ""),
				Arrays.asList("意思就是说当用户开启了这个选项后,如图的背景就会变成头像,并且背景是模糊化的", "队伍介绍界面也是如此的道理"))));
		list.add(new HelpInfo("比赛的信息来源自哪里?",
				Arrays.asList(Arrays.asList("null"), Arrays.asList("本app的比赛信息大多数来源于大学生赛事网"))));
		return list;

	}

	@Override
	public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
		// TODO Auto-generated method stub
		return true;
	}

}
