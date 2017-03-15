package com.peng.saishi.activity;

import java.io.File;

import android.net.Uri;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnLongClickListener;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.peng.saishi.R;
import com.peng.saishi.activity.base.BaseActivity;
import com.peng.saishi.entity.config.AppConfig;
import com.peng.saishi.utils.ToastUtils;
import com.peng.saishi.widget.CircleProgressDrawable;
import com.peng.saishi.widget.actionsheet.ActionSheet;
import com.peng.saishi.widget.actionsheet.ActionSheet.MenuItemClickListener;
import com.peng.saishi.widget.zoomable.ZoomableDraweeView;

public class PhotoActivity extends BaseActivity implements MenuItemClickListener {

	private ZoomableDraweeView photoview;

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub

	}

	String id;
	String path;

	@Override
	public void init() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_photo);
		photoview = (ZoomableDraweeView) findViewById(R.id.Photo_image);
		int type = getIntent().getIntExtra("type", 0);
		id = getIntent().getStringExtra("id");
		path = type == 5 ? AppConfig.New_Path : type == 4 ? AppConfig.qiniu_Url
				: type == 1 ? AppConfig.Matchpic_path
						: type == 2 ? AppConfig.Teanpic_path
								: type == 3 ? AppConfig.Userpic_path
										: AppConfig.Code_Path;

		GenericDraweeHierarchy hierarchy = photoview.getHierarchy();
		hierarchy.setProgressBarImage(new CircleProgressDrawable());
		photoview.setHierarchy(hierarchy);
		ImageRequest request = ImageRequestBuilder
				.newBuilderWithSource(Uri.parse(path + id))
				.setProgressiveRenderingEnabled(true).build();
		DraweeController controller = Fresco.newDraweeControllerBuilder()
				.setImageRequest(request).setAutoPlayAnimations(true).build();

		photoview.setController(controller);
		// photoview.setPhotoUri(Uri.parse(path+id));
		setTheme(R.style.ActionSheetStyleIOS7);

	}
	ActionSheet sheet;
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode==KeyEvent.KEYCODE_MENU) {
			sheet=  new ActionSheet(this);
			sheet.setCancelButtonTitle("关闭");
			sheet.setTitle("可选项");
			sheet.addItems("下载图片到本地");
			sheet.setItemClickListener(this);
			sheet.showMenu();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onItemClick(int itemPosition) {
		// TODO Auto-generated method stub

		// TODO Auto-generated method stub
		new HttpUtils().download(path + id,
				AppConfig.Chat_Save + System.currentTimeMillis() + ".jpg",
				true, new RequestCallBack<File>() {
					public void onFailure(HttpException arg0, String arg1) {
						ToastUtils.ShowNetError();
					};

					public void onSuccess(
							com.lidroid.xutils.http.ResponseInfo<File> arg0) {
						ToastUtils.ShowShortToast("保存成功!存放在:"
								+ AppConfig.Chat_Save);
					};
				});
	
	}

}
