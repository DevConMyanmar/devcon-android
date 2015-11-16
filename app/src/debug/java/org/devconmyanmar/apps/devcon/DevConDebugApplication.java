package org.devconmyanmar.apps.devcon;

import android.app.Application;
import android.content.Context;
import android.os.SystemClock;
import com.facebook.stetho.Stetho;
import com.facebook.stetho.okhttp.StethoInterceptor;
import com.squareup.okhttp.OkHttpClient;

import static org.devconmyanmar.apps.devcon.utils.LogUtils.LOGI;
import static org.devconmyanmar.apps.devcon.utils.LogUtils.makeLogTag;

/**
 * Created by yelinaung on 11/16/15.
 */
public class DevConDebugApplication extends Application {

  public static OkHttpClient okHttpClient;
  private static final String TAG = makeLogTag(DevConDebugApplication.class);

  @Override public void onCreate() {
    super.onCreate();
    okHttpClient = new OkHttpClient();
    LOGI(TAG, "on create from " + getClass().getCanonicalName());

    long startTime = SystemClock.elapsedRealtime();
    initializeStetho(this);
    long elapsed = SystemClock.elapsedRealtime() - startTime;
    LOGI(TAG, "Stetho initialized in " + elapsed + " ms");
  }

  private void initializeStetho(final Context context) {

    Stetho.initialize(Stetho.newInitializerBuilder(context).
        enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this)).build());
    okHttpClient.networkInterceptors().add(new StethoInterceptor());
  }
}
