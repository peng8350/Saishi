package com.peng.saishi.activity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import cn.finalteam.galleryfinal.GalleryFinal.OnHanlderResultCallback;
import cn.finalteam.galleryfinal.model.PhotoInfo;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.api.BasicCallback;

import com.daimajia.androidanimations.library.YoYo;
import com.daimajia.androidanimations.library.attention.ShakeAnimator;
import com.facebook.drawee.view.SimpleDraweeView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.peng.saishi.R;
import com.peng.saishi.activity.base.BaseBackActivity;
import com.peng.saishi.entity.config.AppConfig;
import com.peng.saishi.interfaces.observer.SaveLIstener;
import com.peng.saishi.interfaces.observer.UserRegListener;
import com.peng.saishi.manager.PickManager;
import com.peng.saishi.manager.UserManager;
import com.peng.saishi.utils.DialogUtils;
import com.peng.saishi.utils.FIleUtils;
import com.peng.saishi.utils.FrescoUtils;
import com.peng.saishi.utils.ToastUtils;

public class RegMoreActivity extends BaseBackActivity implements
		OnHanlderResultCallback, UserRegListener {
	// 头像
	@ViewInject(R.id.RegMore_rectImageVIew1)
	private SimpleDraweeView draweeView;
	// 选择性别
	@ViewInject(R.id.Regmore_radioGroup1)
	private RadioGroup sex_group;
	// 输入名字的edittext
	@ViewInject(R.id.Regmore_editText1)
	private EditText et_name;
	// 介绍的ediitext
	@ViewInject(R.id.RegMore_editText2)
	private EditText et_intro;
	private String user;
	private String pswd;
	// 文件地址
	public String PicPath;

	// 加载我的包资源文件的地址
	@Override
	public void init() {
		// TODO Auto-generated mlocatiethod stub
		setContentView(R.layout.activity_regmore);
		super.init();
		ViewUtils.inject(this);
		setTheme(R.style.ActionSheetStyleIOS7);
		user = getIntent().getStringExtra("user");
		pswd = getIntent().getStringExtra("pswd");
		FrescoUtils.Display(draweeView, "res://com.peng.saishi/"
				+ R.drawable.user, 150, 150, false);
		PicPath = AppConfig.pic_temp;
		FIleUtils.saveBitmap(
				BitmapFactory.decodeResource(getResources(), R.drawable.user),
				PicPath, null);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
	}

	// 点击完成之后
	@OnClick(R.id.Regmore_button1)
	public void Finish(View v) {
		// 点击了完成支护,提交给服务器端修改代码,引用修改
		// 对用户输入的信息进行一个合法的判断
		final String name = et_name.getText().toString();
		final String intro = et_intro.getText().toString();
		final String sex = sex_group.getCheckedRadioButtonId() == R.id.radio0 ? "男"
				: "女";
		if (name.equals("")) {
			// 假如名字为空
			YoYo.with(new ShakeAnimator()).duration(700).playOn(et_name);
			return;
		}
		if (intro.equals("")) {
			YoYo.with(new ShakeAnimator()).duration(700).playOn(et_intro);
			return;
		}
		if (intro.length() < 10) {
			// 少于10个字
			DialogUtils.ShowTips("自我介绍按钮不能少于10个字", this);
			return;
		}
		wait_dialog = DialogUtils.createLoadingDialog(this, "注册中...");
		wait_dialog.show();
		JMessageClient.register(user, pswd, new BasicCallback() {

			@Override
			public void gotResult(int arg0, String arg1) {
				// TODO Auto-generated method stub
				if (arg0 == 0) {
					final Map<String, String> params = new HashMap<>();
					params.put("user", user);
					params.put("pswd", pswd);
					params.put("name", name);
					params.put("intro", intro);
					params.put("sex", sex);
					FIleUtils.saveBitmap(BitmapFactory.decodeFile(PicPath),
							AppConfig.pic_temp, new SaveLIstener() {

								@Override
								public void OnSaveSuccess() {
									// TODO Auto-generated method stub
									UserManager.Reg(params, AppConfig.pic_temp,
											RegMoreActivity.this);
								}

								@Override
								public void OnSaveFailed() {
									// TODO Auto-generated method stub
									wait_dialog.dismiss();
								}
							});

				} else if (arg0 == 863002) {
					ToastUtils.ShowShortToast("无效的密码");
					wait_dialog.dismiss();
				} else if (arg0 == 898001) {
					ToastUtils.ShowLongToast("用户已经存在");
					wait_dialog.dismiss();

				} else {
					ToastUtils.ShowLongToast(arg1);
					wait_dialog.dismiss();
				}
			}
		});

	}

	Dialog wait_dialog;

	// 点击头像之后
	@OnClick(R.id.RegMore_rectImageVIew1)
	public void SelectHead(View v) {
		PickManager.ShowSheetPick(this, this);
	}

	@Override
	public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {
		// TODO Auto-generated method stub
		String path = resultList.get(0).getPhotoPath();

		FrescoUtils.Display(draweeView, "file://" + path, 150, 150, false);
		PicPath = path;
		FIleUtils.saveBitmap(BitmapFactory.decodeFile(path),
				AppConfig.pic_temp, null);

	}

	@Override
	public void onHanlderFailure(int requestCode, String errorMsg) {
		// TODO Auto-generated method stub

		ToastUtils.ShowShortToast("文件选取失败,错误信息:" + errorMsg);
	}

	@Override
	public void onSuccess(JSONObject obj) {
		// TODO Auto-generated method stub
		wait_dialog.dismiss();
		try {
			if (obj.getString("result").equals("ok")) {
				ToastUtils.ShowShortToast("注册成功!!!");
				finish();
			} else {
				ToastUtils.ShowShortToast("注册失败,服务端关闭!");
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void onFailed(String msg) {
		// TODO Auto-generated method stub
		wait_dialog.dismiss();
		ToastUtils.ShowShortToast(msg);
	}

}
