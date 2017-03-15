package com.peng.saishi.manager;

import java.io.File;

import org.json.JSONObject;

import cn.jpush.im.android.api.JMessageClient;

import com.peng.saishi.adapter.ChatAdapter;
import com.peng.saishi.entity.ChatEntity;
import com.peng.saishi.entity.FileChatEntity;
import com.peng.saishi.utils.QiniuUtils;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCancellationSignal;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UpProgressHandler;

public class QiniuManager {

	// 断点续传
	public static void PrepartUpload(final String path, final FileChatEntity entity,
			final ChatAdapter adapter) {
		UpProgressHandler progressHandler = new UpProgressHandler() {
			@Override
			public void progress(String arg0, double arg1) {
				// TODO Auto-generated method stub
				entity.setCurrent(arg1);
				if (adapter != null) {

					adapter.notifyDataSetChanged();
				}
			}
		};
		UpCompletionHandler completionHandler = new UpCompletionHandler() {

			@Override
			public void complete(String arg0, ResponseInfo arg1, JSONObject arg2) {
				// TODO Auto-generated method stub
				// 这里,如果完毕之后,就直接把信息发出去
				if (arg1.isOK()) {
					adapter.getConversation().updateMessageExtra(entity.getMsg(),
							"status", "1");
					adapter.notifyDataSetChanged();
					// 发送消息给对方
					new Thread(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							JMessageClient.sendMessage(entity.getMsg());
						}
					}).start();
				}

			}
		};
		UpCancellationSignal signal = new UpCancellationSignal() {

			@Override
			public boolean isCancelled() {
				// TODO Auto-generated method stub
				
				return entity==null?false:entity.isCanceled();
			}
		};
		// 上传
		QiniuUtils.UploadFile(path,
				path.substring(path.lastIndexOf("/") + 1, path.length()),
				completionHandler, progressHandler, signal);
		
	}

}
