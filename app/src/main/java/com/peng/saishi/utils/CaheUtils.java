package com.peng.saishi.utils;

import java.io.File;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.text.TextUtils;

import com.facebook.binaryresource.FileBinaryResource;
import com.facebook.cache.common.CacheKey;
import com.facebook.cache.common.SimpleCacheKey;
import com.facebook.common.references.CloseableReference;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.bitmaps.SimpleBitmapReleaser;
import com.facebook.imagepipeline.cache.DefaultCacheKeyFactory;
import com.facebook.imagepipeline.common.ImageDecodeOptions;
import com.facebook.imagepipeline.common.ImageDecodeOptionsBuilder;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.facebook.imagepipeline.image.CloseableImage;
import com.facebook.imagepipeline.image.CloseableStaticBitmap;
import com.facebook.imagepipeline.image.ImmutableQualityInfo;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

public class CaheUtils {

	public static void ClearOneCahe(String uri) {
		//
		ImagePipeline pipeline = Fresco.getImagePipeline();
		pipeline.evictFromMemoryCache(Uri.parse(uri));
		pipeline.evictFromDiskCache(Uri.parse(uri));
		pipeline.evictFromCache(Uri.parse(uri));
	}

	/**
	 * 从缓存中获取图片
	 */
	@SuppressWarnings("deprecation")
	public static Bitmap getBitfromCahe(String uri) {
		FileBinaryResource resource = (FileBinaryResource) Fresco
				.getImagePipelineFactory().getMainDiskStorageCache()
				.getResource(new SimpleCacheKey(uri.toString()));
		File file = resource.getFile();
		return BitmapFactory.decodeFile(file.getPath());

	}

	/**
	 * 主动放入缓存里面
	 * 
	 * @param:Bitmap 缓存进去的bitmap
	 * @param path
	 *            缓存进去的uri
	 */
	public static void PushBitmap(Bitmap b, String path) {
		ImageDecodeOptions deocdeoptions = new ImageDecodeOptionsBuilder()
				.setDecodePreviewFrame(true).build();
		ResizeOptions options = new ResizeOptions(250, 250);
		ImageRequest request = ImageRequestBuilder
				.newBuilderWithSource(Uri.parse(path))
				.setImageDecodeOptions(deocdeoptions).setResizeOptions(options)
				.build();
		CacheKey cacheKey = DefaultCacheKeyFactory.getInstance()
				.getBitmapCacheKey(request, null);

		// 获得 closeableReference
		CloseableReference<CloseableImage> closeableReference = CloseableReference
				.<CloseableImage> of(new CloseableStaticBitmap(b,
						SimpleBitmapReleaser.getInstance(),
						ImmutableQualityInfo.FULL_QUALITY, 0));
		// 存入 Fresco
		Fresco.getImagePipelineFactory().getBitmapMemoryCache()
				.cache(cacheKey, closeableReference);
	}

	/**
	 * 预加载图片
	 */
	/**
	 * 预加载图片
	 * 
	 * @param context
	 * @param url
	 */
	public static void preLoadImg(Context context, String url,int width,int height) {
		
		if (context == null || TextUtils.isEmpty(url))
			return;
		ImagePipeline imagePipeline = Fresco.getImagePipeline();
		ResizeOptions options = new ResizeOptions(width, height);
		ImageRequest request = ImageRequestBuilder
				.newBuilderWithSource(Uri.parse(url))
				.setProgressiveRenderingEnabled(true)
				.setResizeOptions(options).build();
		imagePipeline.prefetchToBitmapCache(request, null);
		imagePipeline.prefetchToDiskCache(request, null);

	}

}
