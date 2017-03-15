package com.peng.saishi.utils;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeController;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.peng.saishi.widget.CircleProgressDrawable;

public class FrescoUtils {

	// 展示图片,截取图片大小
	public static void Display(SimpleDraweeView target, String uri, int width,
			int height,boolean progress) {
		
		ResizeOptions options = new ResizeOptions(width, height);
		if(progress){
			GenericDraweeHierarchy hierarchy = target.getHierarchy();
			hierarchy.setProgressBarImage(new CircleProgressDrawable());
			target.setHierarchy(hierarchy);
		}
		ImageRequest request = ImageRequestBuilder
				.newBuilderWithSource(Uri.parse(uri))
				.setProgressiveRenderingEnabled(true)
				.setResizeOptions(options).build();
		PipelineDraweeController controller = (PipelineDraweeController) Fresco
				.newDraweeControllerBuilder()
				.setOldController(target.getController())
				.setImageRequest(request).build();

		target.setController(controller);

	}

	/**
	 * 预加载图片
	 * 
	 * @param context
	 * @param url
	 */
	public static void preLoadImg(Context context, String url) {
		if (context == null || TextUtils.isEmpty(url))
			return;
		ImagePipeline imagePipeline = Fresco.getImagePipeline();
		ImageRequest imageRequest = ImageRequestBuilder
				.newBuilderWithSource(Uri.parse(url))
				.setLowestPermittedRequestLevel(
						ImageRequest.RequestLevel.FULL_FETCH).build();
		imagePipeline.prefetchToBitmapCache(imageRequest, null);
		imagePipeline.prefetchToDiskCache(imageRequest, null);
		System.out.println("预先加载");

	}

}
