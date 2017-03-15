package com.peng.saishi.utils;

import java.io.File;
import java.io.IOException;

import com.peng.saishi.entity.config.AppConfig;
import com.qiniu.android.storage.Configuration;
import com.qiniu.android.storage.KeyGenerator;
import com.qiniu.android.storage.Recorder;
import com.qiniu.android.storage.UpCancellationSignal;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UpProgressHandler;
import com.qiniu.android.storage.UploadManager;
import com.qiniu.android.storage.UploadOptions;
import com.qiniu.android.storage.persistent.FileRecorder;

public class QiniuUtils {

	// 上传图片
	public static final void Upload(String uploadPath, String id,
			UpCompletionHandler handler) {
		// TODO Auto-generated method stub
		// 重用uploadManager。一般地，只需要创建一个uploadManager对象
		UploadManager uploadManager = new UploadManager();
		uploadManager.put(uploadPath, id, AppConfig.qiniu_taken, handler, null);
	}

	//断电续传
	public static final void UploadFile(String uploadPath,String id,
			UpCompletionHandler complete_hanlder,
			UpProgressHandler progress_handler,UpCancellationSignal signal) {
		String dirPath = AppConfig.Chat_Save;
		Recorder recorder = null;
		try {
			recorder = new FileRecorder(dirPath);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		//默认使用key的url_safe_base64编码字符串作为断点记录文件的文件名
		//避免记录文件冲突（特别是key指定为null时），也可自定义文件名(下方为默认实现)：
		KeyGenerator keyGen = new KeyGenerator(){
		    public String gen(String key, File file){
		        // 不必使用url_safe_base64转换，uploadManager内部会处理
		        // 该返回值可替换为基于key、文件内容、上下文的其它信息生成的文件名
		        return key + "_._" + new StringBuffer(file.getAbsolutePath()).reverse();
		    }
		};
		// 重用uploadManager。一般地，只需要创建一个uploadManager对象
		//UploadManager uploadManager = new UploadManager(recorder);  // 1
		//UploadManager uploadManager = new UploadManager(recorder, keyGen); // 2
		// 或在初始化时指定：
		Configuration config = new Configuration.Builder()
		                    // recorder分片上传时，已上传片记录器
		                    // keyGen分片上传时，生成标识符，用于片记录器区分是哪个文件的上传记录
		                    .recorder(recorder, keyGen)  
		                    .build();
		UploadManager uploadManager = new UploadManager(config);
		uploadManager.put(uploadPath, uploadPath.substring(
				uploadPath.lastIndexOf("/") + 1, uploadPath.length()),
				AppConfig.qiniu_taken, complete_hanlder, new UploadOptions(
						null, null, false, progress_handler, signal));
	}
	

	
	
}
