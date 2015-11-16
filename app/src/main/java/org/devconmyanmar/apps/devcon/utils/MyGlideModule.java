package org.devconmyanmar.apps.devcon.utils;

import android.content.Context;
import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.integration.okhttp.OkHttpUrlLoader;
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.module.GlideModule;
import com.squareup.okhttp.OkHttpClient;
import java.io.InputStream;

/**
 * Created by Ye Lin Aung on 15/06/22.
 */
public class MyGlideModule implements GlideModule {

  public static OkHttpClient okHttpClient;

  public static GlideBuilder builder;

  @Override public void applyOptions(Context context, GlideBuilder builder) {
  }

  @Override public void registerComponents(Context context, Glide glide) {
    okHttpClient = new OkHttpClient();
    builder = new GlideBuilder(context).
        setDiskCache(new InternalCacheDiskCacheFactory(context, 50 * 1024 * 1024));
    glide.register(GlideUrl.class, InputStream.class, new OkHttpUrlLoader.Factory(okHttpClient));
  }
}
