package com.peng.saishi.activity;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Animatable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import cn.finalteam.galleryfinal.GalleryFinal.OnHanlderResultCallback;
import cn.finalteam.galleryfinal.model.PhotoInfo;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.image.ImageInfo;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.peng.saishi.R;
import com.peng.saishi.activity.base.BaseBackActivity;
import com.peng.saishi.entity.TeamInfo;
import com.peng.saishi.entity.config.AppConfig;
import com.peng.saishi.interfaces.service.MyStringCallBack;
import com.peng.saishi.manager.PickManager;
import com.peng.saishi.utils.CaheUtils;
import com.peng.saishi.utils.FIleUtils;
import com.peng.saishi.utils.FrescoUtils;
import com.peng.saishi.utils.ToastUtils;
import com.peng.saishi.widget.CustomDialog;
import com.zhy.http.okhttp.OkHttpUtils;

public class UpdateTeamAct extends BaseBackActivity implements TextWatcher,
		OnHanlderResultCallback {
	@ViewInject(R.id.UpdateTeam_add)
	private Button add;
	@ViewInject(R.id.UpdateTeam_plus)
	private Button plus;
	@ViewInject(R.id.UpdateTeam_editText1)
	private EditText name_et;
	@ViewInject(R.id.UpdateTeam_editText2)
	private EditText intro_et;
	@ViewInject(R.id.UpdateTeam_editText3)
	private EditText yaoqiu_et;
	@ViewInject(R.id.UpdateTeam_editText4)
	private EditText plan_et;
	@ViewInject(R.id.UpdateTeam_editText5)
	private EditText target_et;
	@ViewInject(R.id.UpdateTeam_editText6)
	private EditText totle_et;
	@ViewInject(R.id.UpdateTeam_rectImageVIew1)
	private SimpleDraweeView head_iv;
	@ViewInject(R.id.UpdateTeam_Btn)
	private Button sumbit_btn;

	private TeamInfo info;


	private boolean save = true;

	public boolean isSave() {
		return save;
	}

	public void setSave(boolean save) {
		this.save = save;
		sumbit_btn.setEnabled(!save);
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		setTheme(R.style.ActionSheetStyleIOS7);
		setContentView(R.layout.activity_updateteam);
		super.init();

		info = getIntent().getParcelableExtra("team");
		ViewUtils.inject(this);
		name_et.setText(info.getName());
		intro_et.setText(info.getIntro());
		target_et.setText(info.getTarget());
		totle_et.setText(info.getNeedperson() + "");
		yaoqiu_et.setText(info.getYaoqiu());
		plan_et.setText(info.getPlan());
		name_et.addTextChangedListener(this);
		intro_et.addTextChangedListener(this);
		@SuppressWarnings("rawtypes")
		ControllerListener listener = new BaseControllerListener<ImageInfo>() {
			@Override
			public void onFinalImageSet(String id, ImageInfo imageInfo,
					Animatable animatable) {
				// TODO Auto-generated method stub
				Bitmap bitmap = CaheUtils.getBitfromCahe(AppConfig.Teanpic_path
						+ info.getId());
				FIleUtils.saveBitmap(bitmap, AppConfig.pic_temp, null);
				
				super.onFinalImageSet(id, imageInfo, animatable);
			}
		};
		@SuppressWarnings("unchecked")
		DraweeController controller = Fresco.newDraweeControllerBuilder()
				.setUri(AppConfig.Teanpic_path + info.getId())
				.setControllerListener(listener).build();
		head_iv.setController(controller);
		target_et.addTextChangedListener(this);
		totle_et.addTextChangedListener(this);
		yaoqiu_et.addTextChangedListener(this);
		plan_et.addTextChangedListener(this);
	}

	// 选取图片
	@OnClick(R.id.UpdateTeam_rectImageVIew1)
	public void SelectPic(View v) {
		PickManager.ShowSheetPick(this, this);
	}

	// 显示确定退出的按钮
	private void ShowExitSure() {
		// TODO Auto-generated method stub
		CustomDialog.Builder builder = new CustomDialog.Builder(this);
		builder.setTitle("放弃保存");
		builder.setPositiveButton("确定", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				finish();
				dialog.dismiss();
			}
		});
		builder.setNegativeButton("取消", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub

				dialog.dismiss();

			}
		});
		builder.setMessage("等等,你刚所修改的信息尚未保存~~~你要确定退出不保存吗?");
		builder.create().show();
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub

		if (save) {
			// 保存了
			finish();
		} else {
			ShowExitSure();
		}

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == R.id.back_tv) {
			if (save) {
				// 保存了
				finish();
			} else {
				ShowExitSure();
			}
		}
	}

	@OnClick(R.id.UpdateTeam_Btn)
	public void submit(View v) {
		// 提交数据到服务端
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
		Map<String, String> params = new HashMap<>();
		params.put("id", info.getId() + "");
		params.put("intro", intro_et.getText().toString());
		params.put("name", name_et.getText().toString());
		params.put("yaoqiu", yaoqiu_et.getText().toString());
		params.put("target", target_et.getText().toString());
		params.put("plan", plan_et.getText().toString());
		params.put("need", totle_et.getText().toString());
		OkHttpUtils
				.post()
				.url(AppConfig.UpdateTeam)
				.addFile("imageFile", "images/png",
						new File(AppConfig.pic_temp)).params(params).build()
				.execute(new MyStringCallBack() {

					@Override
					public void onResponse(String response, int id) {
						// TODO Auto-generated method stub
						if (id!=0) {
							ToastUtils.ShowNetError();
							return ;
						}
						try {
							JSONObject res_obj = new JSONObject(response);
							if (res_obj.getString("result").equals("ok")) {
								// 成功了
								OkHttpUtils.post().url(AppConfig.ClearTeamCahe)
										.addParams("id", info.getId() + "")
										.build().execute(null);
									
								setResult(RESULT_OK);
								finish();
							} else {
								ToastUtils.ShowShortToast("Sever存在异常,原因不明");
							}
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

	@OnClick(R.id.UpdateTeam_add)
	public void Add(View v) {
		// 加
		totle_et.setText(Integer.parseInt(totle_et.getText().toString()) + 1
				+ "");
	}

	@OnClick(R.id.UpdateTeam_plus)
	public void Plus(View v) {
		// 减
		totle_et.setText(Integer.parseInt(totle_et.getText().toString()) - 1
				+ "");
	}

	@Override
	public void afterTextChanged(Editable s) {
		// TODO Auto-generated method stub
		if (s == totle_et.getText()) {
			if (s.toString().equals("")) {
				totle_et.setText("2");
				return;
			}
			int number = Integer.parseInt(s.toString());

			if (number > 15) {
				totle_et.setText(15 + "");
			} else if (number < 2) {
				totle_et.setText(2 + "");
			}
		}
		setSave(false);
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
		FrescoUtils.Display(head_iv, "file://" + path, 150, 150,false);
		setSave(false);
		FIleUtils.saveBitmap(BitmapFactory.decodeFile(path),
				AppConfig.pic_temp, null);
	}

	@Override
	public void onHanlderFailure(int requestCode, String errorMsg) {
		// TODO Auto-generated method stub

	}

}
