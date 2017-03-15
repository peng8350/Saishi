package com.peng.saishi.manager;

import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import cn.jpush.im.android.api.JMessageClient;

import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationClientOption.AMapLocationMode;
import com.amap.api.location.AMapLocationListener;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.facebook.imagepipeline.decoder.SimpleProgressiveJpegConfig;
import com.orm.SugarContext;
import com.peng.saishi.R;
import com.peng.saishi.entity.config.AppConfig;
import com.peng.saishi.utils.FIleUtils;
import com.peng.saishi.utils.OkhttpNetworkFecther;
import com.peng.saishi.utils.ToastUtils;
import com.qiniu.android.common.Zone;
import com.qiniu.android.storage.Configuration;
import com.qiniu.android.storage.UploadManager;
import com.uuzuche.lib_zxing.activity.ZXingLibrary;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

/**
 * 
 * 全局ＡＰＰ类，用来得到上下文，等等多个功能,查询数据库等单利对象
 * 
 * @author peng
 * 
 */
public class App extends Application {

	// 得到app
	public static Context Appcontext;

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub

		super.onCreate();
		FIleUtils.saveBitmap(BitmapFactory.decodeResource(getResources(),
				R.drawable.lauchicon), AppConfig.app_icon, null);
		Appcontext = getApplicationContext();
		OkHttpClient okHttpClient = new OkHttpClient.Builder()
				// .addInterceptor(new LoggerInterceptor("TAG"))
				.connectTimeout(10000L, TimeUnit.MILLISECONDS)
				.readTimeout(10000L, TimeUnit.MILLISECONDS)
				// 其他配置
				.build();
		OkHttpUtils.initClient(okHttpClient);
		SugarContext.init(getInstance());
		OkhttpNetworkFecther myNetworkFetcher = new OkhttpNetworkFecther(
				okHttpClient);

		ImagePipelineConfig config = ImagePipelineConfig.newBuilder(this)
				.setNetworkFetcher(myNetworkFetcher)
				.setBitmapsConfig(Config.RGB_565)
				.setResizeAndRotateEnabledForNetwork(true)
				.setProgressiveJpegConfig(new SimpleProgressiveJpegConfig())
				.build();
		PickManager.initGalley(this);
		Fresco.initialize(this, config);
		ZXingLibrary.initDisplayOpinion(this);
		JMessageClient.init(this);
		initQiniu();
	}

	
	



	// 初始化七牛的api
	@SuppressWarnings("unused")
	private void initQiniu() {
		// TODO Auto-generated method stub
		Configuration config = new Configuration.Builder()
				.chunkSize(256 * 1024) // 分片上传时，每片的大小。 默认256K
				.putThreshhold(512 * 1024) // 启用分片上传阀值。默认512K
				.connectTimeout(10) // 链接超时。默认10秒
				.responseTimeout(60) // 服务器响应超时。默认60秒
				.zone(Zone.zone0) // 设置区域，指定不同区域的上传域名、备用域名、备用IP。默认 Zone.zone0
				.build();
		UploadManager uploadManager = new UploadManager(config);



	}

	public static Context getInstance() {
		return Appcontext;
	}

}
