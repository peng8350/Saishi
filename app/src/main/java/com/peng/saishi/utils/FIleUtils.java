package com.peng.saishi.utils;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import net.bither.util.NativeUtil;
import okhttp3.Call;

import org.json.JSONObject;

import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;

import com.peng.saishi.entity.config.AppConfig;
import com.peng.saishi.interfaces.observer.SaveLIstener;
import com.peng.saishi.interfaces.service.MyStringCallBack;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.zhy.http.okhttp.OkHttpUtils;

public class FIleUtils {

	/*
	 * 
	 * 上传语音到服务器,传入id
	 */
	public static void UploadRadio(final String path, String id) {

		Map<String, String> params = new HashMap<>();
		params.put("id", id + "");
		OkHttpUtils.post().addFile("imageFile", "audio/mpeg", new File(path))
				.addParams("id", id).url(AppConfig.RadioUpload).build()
				.execute(new MyStringCallBack() {

					@Override
					public void onError(Call call, Exception e, int id) {
						Log.e("1", "上传失败：");
						new File(path).delete();
					}

					@Override
					public void onResponse(String response, int id) {
						if (id != 0) {
							ToastUtils.ShowNetError();
							return;
						}
						String resultStr = response;
						Log.e("1", "上传成功：" + resultStr);
						new File(path).delete();

					}

				});

	}

	/*
	 * 
	 * 上传到服务器,传入id
	 */
	public static void UploadPic(final String path, int id,
			final SaveLIstener listener) {

		Map<String, String> params = new HashMap<>();
		params.put("id", id + "");
		OkHttpUtils.post().addFile("imageFile", "image/jpeg", new File(path))
				.params(params).url(AppConfig.Pic_Upload).build()
				.execute(new MyStringCallBack() {

					@Override
					public void onResponse(String response, int id) {
						String resultStr = response;
						Log.e("1", "上传成功：" + resultStr);
						if (id != 0) {
							ToastUtils.ShowNetError();
							return;
						}
						if (listener != null) {

							listener.OnSaveSuccess();
							new File(path).delete();
						}
					}

					@Override
					public void onError(Call call, Exception e, int id) {
						Log.e("1", "上传失败：");
						if (listener != null) {
							listener.OnSaveFailed();
						}
					}
				});

	}

	/*
	 * 
	 * 上传到聊天图片服务器,传入
	 */
	public static void UploadChatPic(final String path, String id,
			UpCompletionHandler handler) {
		Map<String, File> filemaps = new HashMap<>();
		filemaps.put("file2",
				new File(Environment.getExternalStorageDirectory()
						+ "/saiban/blurtemp.png"));
		filemaps.put("file1", new File(path));
		QiniuUtils.Upload(path, id, handler);
		QiniuUtils.Upload(AppConfig.blur_temp, "blur"+id, new UpCompletionHandler() {
			
			
			@Override
			public void complete(String arg0, ResponseInfo arg1, JSONObject arg2) {
				// TODO Auto-generated method stub
				new File(AppConfig.blur_temp).delete();
			}
			
		});
	}

	/** 保存botmap到某个路径方法 */
	public static void saveBitmap(final Bitmap bitmap, final String path,
			final SaveLIstener lIstener) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				if (!new File(path.substring(0, path.lastIndexOf("/")))
						.exists()) {
					new File(path.substring(0, path.lastIndexOf("/"))).mkdirs();
				}
				NativeUtil.compressBitmap(bitmap, 60, path,
						true);
				if (lIstener != null) {
					lIstener.OnSaveSuccess();
				}
			}
		}).start();

	}

}
