package com.peng.saishi.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;
import com.facebook.drawee.view.SimpleDraweeView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.peng.saishi.R;
import com.peng.saishi.entity.TeamInfo;
import com.peng.saishi.entity.config.AppConfig;
import com.peng.saishi.utils.FrescoUtils;

public class CodeShowActivity extends Activity implements View.OnClickListener{
	@ViewInject(R.id.codeshow_image)
	private SimpleDraweeView team_image;
	@ViewInject(R.id.code_show_name)
	private TextView name_tv;
	@ViewInject(R.id.code_show_desci)
	private TextView descri_tv;// 描述人数w
	@ViewInject(R.id.Codeshow_codeimage)
	private SimpleDraweeView code_image;
	private TeamInfo teamInfo;


	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_codeshow);
		ViewUtils.inject(this);
		teamInfo = getIntent().getParcelableExtra("info");
		descri_tv.setText(teamInfo.getNowperson() + " / "
				+ teamInfo.getNeedperson());
		name_tv.setText(teamInfo.getName());
		FrescoUtils.Display(code_image, AppConfig.Code_Path + teamInfo.getGroupid(),
				300, 300,false);
		FrescoUtils.Display(team_image,
				AppConfig.Teanpic_path + teamInfo.getId(), 120, 120,false);
	}


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}

}
