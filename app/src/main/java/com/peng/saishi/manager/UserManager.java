package com.peng.saishi.manager;

import java.io.File;
import java.util.Map;

import okhttp3.Call;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.peng.saishi.entity.config.AppConfig;
import com.peng.saishi.interfaces.observer.UserRegListener;
import com.peng.saishi.interfaces.service.MyStringCallBack;
import com.peng.saishi.utils.ToastUtils;
import com.zhy.http.okhttp.OkHttpUtils;

/**
 * 用户的管理者
 * 
 * @author peng 登陆,注册
 */
public class UserManager {

	// 注册
	public static void Reg(Map<String, String> params, String path,
			final UserRegListener listener) {
		OkHttpUtils.post().url(AppConfig.REG).params(params)
				.addFile("imageFile", "image/jpeg", new File(path)).build()
				.execute(new MyStringCallBack() {

					@Override
					public void onResponse(String response, int id) {
						String resultStr = response;
						Log.e("1", "上传成功：" + resultStr);
						if (id!=0) {
							ToastUtils.ShowNetError();
							return ;
						}
						if (listener != null) {
							try {
								listener.onSuccess(new JSONObject(
										response));
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}

					@Override
					public void onError(Call call, Exception e, int id) {
						if (listener != null) {
							listener.onFailed("失败");
						}
					}
				});
	}

}
