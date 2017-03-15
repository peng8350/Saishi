package com.peng.saishi.utils;

/**
 * Copyright (C) 2015 Wasabeef
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import net.qiujuer.genius.blur.StackBlur;
import android.content.Context;
import android.graphics.Bitmap;

import com.facebook.cache.common.CacheKey;
import com.facebook.cache.common.SimpleCacheKey;
import com.facebook.imagepipeline.request.BasePostprocessor;

public class BlurPostprocessor extends BasePostprocessor {

  private static int MAX_RADIUS = 25;
  private static int DEFAULT_DOWN_SAMPLING = 1;

  private int radius;
  private int sampling;

  public BlurPostprocessor(Context context) {
    this(context, MAX_RADIUS, DEFAULT_DOWN_SAMPLING);
  }

  public BlurPostprocessor(Context context, int radius) {
    this(context, radius, DEFAULT_DOWN_SAMPLING);
  }

  public BlurPostprocessor(Context context, int radius, int sampling) {
    this.radius = radius;
    this.sampling = sampling;
  }
  
  @Override
	public void process(Bitmap bitmap) {
		// TODO Auto-generated method stub
	  bitmap =StackBlur.blurNatively(bitmap, 60, true);
	}


  @Override public String getName() {
    return getClass().getSimpleName();
  }

  @Override public CacheKey getPostprocessorCacheKey() {
    return new SimpleCacheKey("radius=" + radius + ",sampling=" + sampling);
  }
}