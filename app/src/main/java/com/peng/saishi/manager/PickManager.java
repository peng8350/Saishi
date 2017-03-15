package com.peng.saishi.manager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import cn.finalteam.galleryfinal.CoreConfig;
import cn.finalteam.galleryfinal.FunctionConfig;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.GalleryFinal.OnHanlderResultCallback;
import cn.finalteam.galleryfinal.ImageLoader;
import cn.finalteam.galleryfinal.ThemeConfig;

import com.peng.saishi.utils.FrescoImageLoader;
import com.peng.saishi.widget.actionsheet.ActionSheet;
import com.peng.saishi.widget.actionsheet.ActionSheet.MenuItemClickListener;

public class PickManager {
	public static final int REQUEST_CROP = 127;
	public static final int REQUEST_CAMERA = 128;
	public static final int REQUEST_PICK = 129;

	// 聊天界面
	/**
	 * 处理拍照和那个,不弹出actionsheet
	 */
	public static void getCamera(Context context, OnHanlderResultCallback Callback,boolean edit) {
		FunctionConfig config = getFunction(edit);
		GalleryFinal.openCamera(REQUEST_CAMERA, config,Callback);
	}

	/**
	 * 处理拍照和那个,不弹出actionsheet
	 */
	public static void getAlmera(Context context,
			OnHanlderResultCallback Callback,boolean edit) {
		FunctionConfig config = getFunction(edit);
		GalleryFinal.openGallerySingle(REQUEST_CROP, config,Callback);
	}

	/**
	 * 裁剪图片用到的管理者 进行两个Actionsheet的选择
	 */

	public static void ShowSheetPick(final Context context,
			final OnHanlderResultCallback Callback) {
		// 选择图片

		ActionSheet sheet = new ActionSheet(context);
		sheet.setCancelButtonTitle("关闭");
		sheet.setTitle("更换头像");
		sheet.addItems("从相册中更换头像", "拍照更换头像");
		sheet.setCancelableOnTouchMenuOutside(true);
		sheet.setItemClickListener(new MenuItemClickListener() {

			@Override
			public void onItemClick(int itemPosition) {
				// TODO Auto-generated method stub

				switch (itemPosition) {
				case 0:
					getAlmera(context, Callback,true);

					break;
				case 1:
					getCamera(context, Callback,true);
					break;

				default:
					break;
				}
			}
		});
		sheet.showMenu();
	}

	// 初始化galleyfinal
	public static void initGalley(Context context) {
		ImageLoader loader = new FrescoImageLoader(context);
		CoreConfig coreConfig = new CoreConfig.Builder(context, loader,
				setTheme()).setFunctionConfig(getFunction(true))
				.build();
		GalleryFinal.init(coreConfig);
	}

	// 得到主题的参数
	@SuppressLint("ResourceAsColor")
	public static ThemeConfig setTheme() {
		ThemeConfig config = new ThemeConfig.Builder()
				.setTitleBarBgColor(Color.rgb(89, 200, 204))
				.setTitleBarTextColor(Color.rgb(0xff, 0xff, 0xff)).build();
		return config;
	}

	// 功能参数
	public static FunctionConfig getFunction(boolean edit) {
		FunctionConfig functionConfig = new FunctionConfig.Builder()
				.setEnableEdit(true).setEnableCrop(edit)
				.setEnableRotate(true).setCropSquare(true)
				.setEnablePreview(true).build();
		return functionConfig;
	}
}
