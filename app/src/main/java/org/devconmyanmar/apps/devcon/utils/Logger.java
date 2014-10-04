package org.devconmyanmar.apps.devcon.utils;

import android.util.Log;
import org.devconmyanmar.apps.devcon.BuildConfig;

/**
 * Created by Ye Lin Aung on 14/07/08.
 */
public class Logger {
  private static final String LOG_PREFIX = "ys_";
  private static final int LOG_PREFIX_LENGTH = LOG_PREFIX.length();
  private static final int MAX_LOG_TAG_LENGTH = 23;

  public Logger() {
  }

  public static String makeLogTag(String str) {
    if (str.length() > MAX_LOG_TAG_LENGTH - LOG_PREFIX_LENGTH) {
      return LOG_PREFIX + str.substring(0, MAX_LOG_TAG_LENGTH - LOG_PREFIX_LENGTH - 1);
    }
    return LOG_PREFIX + str;
  }

  public static String makeLogTag(Class<?> cls) {
    return makeLogTag(cls.getSimpleName());
  }

  public static void LOGI(final String tag, String message) {
    Throwable throwable = new Throwable();
    StackTraceElement[] e = throwable.getStackTrace();
    String c_name = e[1].getMethodName();

    if (BuildConfig.DEBUG) {
      Log.i(tag, "[" + c_name + "] " + message);
    }
  }

  public static void LOGE(String tag, String message) {
    Throwable throwable = new Throwable();
    StackTraceElement[] e = throwable.getStackTrace();
    String c_name = e[1].getMethodName();

    if (BuildConfig.DEBUG) {
      Log.e(tag, "[" + c_name + "] " + message);
    }
  }

  public static void LOGE(final String tag, String message, Throwable throwable) {
    StackTraceElement[] e = throwable.getStackTrace();
    String c_name = e[1].getMethodName();
    if (BuildConfig.DEBUG) {
      Log.e(tag, "[" + c_name + "] " + message);
    }
  }

  public static void LOGD(String tag, String message) {
    Throwable throwable = new Throwable();
    StackTraceElement[] e = throwable.getStackTrace();
    String c_name = e[1].getMethodName();

    if (BuildConfig.DEBUG) {
      Log.d(tag, "[" + c_name + "] " + message);
    }
  }

  public static void LOGV(String tag, String message) {
    Throwable throwable = new Throwable();
    StackTraceElement[] e = throwable.getStackTrace();
    String c_name = e[1].getMethodName();

    if (BuildConfig.DEBUG) {
      Log.v(tag, "[" + c_name + "] " + message);
    }
  }

  public static void WTF(String tag, String message) {
    Throwable throwable = new Throwable();
    StackTraceElement[] e = throwable.getStackTrace();
    String c_name = e[1].getMethodName();

    if (BuildConfig.DEBUG) {
      Log.wtf(tag, "[" + c_name + "] " + message);
    }
  }
}
