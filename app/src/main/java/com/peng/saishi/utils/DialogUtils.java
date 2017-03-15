package com.peng.saishi.utils;

import java.io.File;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.lidroid.xutils.http.HttpHandler;
import com.peng.saishi.R;
import com.peng.saishi.manager.AppManager;
import com.peng.saishi.widget.CustomDialog;

public class DialogUtils {
	/**
	 * 得到自定义的progressDialog
	 * 
	 * @param context
	 * @param msg
	 * @return
	 */
	@SuppressLint("InflateParams")
	public static Dialog createLoadingDialog(Context context, String msg) {

		LayoutInflater inflater = LayoutInflater.from(context);
		View v = inflater.inflate(R.layout.loading_dialog, null);// 得到加载view
		LinearLayout layout = (LinearLayout) v.findViewById(R.id.dialog_view);// 加载布局
		// main.xml中的ImageView
		ImageView spaceshipImage = (ImageView) v.findViewById(R.id.img);
		TextView tipTextView = (TextView) v.findViewById(R.id.tipTextView);// 提示文字
		// 加载动画
		Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(
				context, R.anim.loading_animation);
		// 使用ImageView显示动画
		spaceshipImage.startAnimation(hyperspaceJumpAnimation);
		tipTextView.setText(msg);// 设置加载信息

		Dialog loadingDialog = new Dialog(context, R.style.loading_dialog);// 创建自定义样式dialog

		loadingDialog.setCancelable(false);// 不可以用“返回键”取消
		loadingDialog.setContentView(layout, new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.MATCH_PARENT));// 设置布局
		return loadingDialog;

	}

	// 确定退出码
	public static void ExitApp(Context context) {
		CustomDialog.Builder dialog = new CustomDialog.Builder(context);
		dialog.setTitle("退出");
		dialog.setMessage("你确定要退出赛伴吗?");
		dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}

		});
		dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				AppManager.ExitApp();

			}
		});
		dialog.create().show();
	}

	// 显示下载的对话
	@SuppressWarnings("deprecation")
	public static ProgressDialog openDialog(Context context,
			final HttpHandler<File> handler) {
		final ProgressDialog wait_dialog = new ProgressDialog(context);
		wait_dialog.setIcon(R.drawable.lauchicon);
		wait_dialog.setTitle("更新");
		wait_dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		wait_dialog.setCancelable(false);
		wait_dialog.setButton("取消下载", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				handler.cancel();

				dialog.dismiss();
				dialog = null;
			}
		});
		wait_dialog.show();
		return wait_dialog;
	}

	// 弹出提示框
	public static void ShowTips(String msg, Context context) {
		CustomDialog.Builder builder = new CustomDialog.Builder(context);
		builder.setTitle("提示");
		builder.setMessage(msg);
		builder.setPositiveButton("关闭", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();

			}
		});
		builder.create().show();
	}
}
