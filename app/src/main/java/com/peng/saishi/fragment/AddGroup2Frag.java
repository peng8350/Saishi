package com.peng.saishi.fragment;

import java.util.List;

import android.graphics.BitmapFactory;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import cn.finalteam.galleryfinal.GalleryFinal.OnHanlderResultCallback;
import cn.finalteam.galleryfinal.model.PhotoInfo;

import com.facebook.drawee.view.SimpleDraweeView;
import com.peng.saishi.R;
import com.peng.saishi.activity.AddGroupActivity;
import com.peng.saishi.entity.config.AppConfig;
import com.peng.saishi.fragment.base.BaseFragment;
import com.peng.saishi.manager.PickManager;
import com.peng.saishi.utils.FIleUtils;
import com.peng.saishi.utils.FrescoUtils;
import com.peng.saishi.utils.GlobeScopeUtils;
import com.peng.saishi.utils.ToastUtils;

public class AddGroup2Frag extends BaseFragment implements OnClickListener,
		TextWatcher,  OnHanlderResultCallback {

	private EditText name_et, intro_et, yaoqiu_et, plan_et, target_et,
			person_et;
	private SimpleDraweeView head;

	public AddGroup2Frag() {
		super(R.layout.frag_addgroup2);
		// TODO Auto-generated constructor stub
	}


	@Override
	public void init() {
		// TODO Auto-generated method stub
		getActivity().setTheme(R.style.ActionSheetStyleIOS7);
		name_et = (EditText) layout.findViewById(R.id.frag_addgroup2_editText1);
		intro_et = (EditText) layout
				.findViewById(R.id.frag_addgroup2_editText2);
		yaoqiu_et = (EditText) layout
				.findViewById(R.id.frag_addgroup2_editText3);
		plan_et = (EditText) layout.findViewById(R.id.frag_addgroup2_editText4);
		target_et = (EditText) layout
				.findViewById(R.id.frag_addgroup2_editText5);
		person_et = (EditText) layout
				.findViewById(R.id.frag_addgroup2_editText6);
		layout.findViewById(R.id.frag_addgroup2_add).setOnClickListener(this);// 加
		layout.findViewById(R.id.frag_addgroup2_plus).setOnClickListener(this);// 减
		head = (SimpleDraweeView) layout
				.findViewById(R.id.frag_addgroup2_rectImageVIew1);
		layout.findViewById(R.id.frag_addgroup2_Apply).setOnClickListener(this);
		person_et.addTextChangedListener(this);
		head.setClickable(true);
		head.setOnClickListener(this);
		FrescoUtils.Display(head, "res://com.peng.saishi/"+R.drawable.pic3, 120, 120,false);
		FIleUtils.saveBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.pic3), AppConfig.pic_temp, null);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.frag_addgroup2_Apply:
			AddGroupActivity activity = (AddGroupActivity) getActivity();
			// {"id","name","matchname","need","intro","yaoqiu","plan","target","type","tags"};
			if (name_et.getText().toString().equals("")) {
				ToastUtils.ShowShortToast("名字一定要填写");
				return;
			}
			if (intro_et.getText().toString().equals("")) {
				ToastUtils.ShowShortToast("队伍介绍一定要填写");
				return;
			}
			if (plan_et.getText().toString().length() < 5) {
				ToastUtils.ShowShortToast("队的计划至少大于5个字");
				return;
			}
			activity.getValues()[0] = GlobeScopeUtils.getMeId() + "";
			activity.getValues()[1] = name_et.getText().toString();
			activity.getValues()[3] = person_et.getText().toString();
			activity.getValues()[4] = intro_et.getText().toString();
			activity.getValues()[5] = yaoqiu_et.getText().toString();
			activity.getValues()[6] = plan_et.getText().toString();
			activity.getValues()[7] = target_et.getText().toString();
			activity.next();
			break;

		case R.id.frag_addgroup2_add:
			// 加
			person_et.setText(Integer.parseInt(person_et.getText().toString())
					+ 1 + "");
			break;
		case R.id.frag_addgroup2_plus:
			// 减
			person_et.setText(Integer.parseInt(person_et.getText().toString())
					- 1 + "");
			break;
		case R.id.frag_addgroup2_rectImageVIew1:
			PickManager.ShowSheetPick(getActivity(), this);
			break;
		default:
			break;
		}

	}


	@Override
	public void afterTextChanged(Editable s) {
		// TODO Auto-generated method stub
		if (s.toString().equals("")) {
			person_et.setText("2");
			return;
		}
		int number = Integer.parseInt(s.toString());

		if (number > 15) {
			person_et.setText(15 + "");
		} else if (number < 2) {
			person_et.setText(2 + "");
		}
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		// TODO Auto-generated method stub

	}



	@Override
	public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {
		// TODO Auto-generated method stub
		String path = resultList.get(0).getPhotoPath();
		FrescoUtils.Display(head,"file://"+ path, 120, 120, false);
		FIleUtils.saveBitmap(BitmapFactory.decodeFile(path), AppConfig.pic_temp, null);
	}

	@Override
	public void onHanlderFailure(int requestCode, String errorMsg) {
		// TODO Auto-generated method stub
		
	}

}
