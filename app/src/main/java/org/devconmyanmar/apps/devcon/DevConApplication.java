package org.devconmyanmar.apps.devcon;

import android.app.Application;
import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.integration.okhttp.OkHttpUrlLoader;
import com.bumptech.glide.load.engine.cache.DiskLruCacheWrapper;
import com.bumptech.glide.load.model.GlideUrl;
import com.squareup.okhttp.OkHttpClient;
import com.sromku.simple.fb.Permission;
import com.sromku.simple.fb.SimpleFacebook;
import com.sromku.simple.fb.SimpleFacebookConfiguration;
import java.io.InputStream;

/**
 * Created by Ye Lin Aung on 14/10/04.
 */
public class DevConApplication extends Application {

  public static GlideBuilder builder;
  public static OkHttpClient okHttpClient;

  @Override public void onCreate() {
    super.onCreate();

    Permission[] permissions = new Permission[] {
        Permission.EMAIL,
        Permission.PUBLISH_ACTION
    };

    SimpleFacebookConfiguration configuration = new SimpleFacebookConfiguration.Builder()
        .setAppId("719288351499456")
        .setNamespace("devcon")
        .setPermissions(permissions)
        .build();

    SimpleFacebook.setConfiguration(configuration);

    okHttpClient = new OkHttpClient();

    builder = new GlideBuilder(this).setDiskCache(
        DiskLruCacheWrapper.get(Glide.getPhotoCacheDir(this), 50 * 1024 * 1024));
    Glide.get(this)
        .register(GlideUrl.class, InputStream.class, new OkHttpUrlLoader.Factory(okHttpClient));
    if (!Glide.isSetup()) {
      Glide.setup(builder);
    }
  }
}
