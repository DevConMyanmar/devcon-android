
/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014 Devcon Contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package org.devconmyanmar.apps.devcon;

import android.app.Application;
import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.integration.okhttp.OkHttpUrlLoader;
import com.bumptech.glide.load.engine.cache.DiskLruCacheWrapper;
import com.bumptech.glide.load.model.GlideUrl;
import com.squareup.okhttp.OkHttpClient;
import java.io.InputStream;

/**
 * Created by Ye Lin Aung on 14/10/04.
 */
public class DevConApplication extends Application {

  public static GlideBuilder builder;
  public static OkHttpClient okHttpClient;

  @Override public void onCreate() {
    super.onCreate();
    if (!BuildConfig.DEBUG) {
      //Fabric.with(this, new Crashlytics());
    }

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
