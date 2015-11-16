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

package org.devconmyanmar.apps.devcon.utils;

import android.util.Log;
import org.devconmyanmar.apps.devcon.BuildConfig;

/**
 * Created by Ye Lin Aung on 14/07/08.
 */
public class LogUtils {
  private static final String LOG_PREFIX = "dc_";
  private static final int LOG_PREFIX_LENGTH = LOG_PREFIX.length();
  private static final int MAX_LOG_TAG_LENGTH = 23;

  public LogUtils() {
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
