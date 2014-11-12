/*
 * Copyright 2014 Google Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.devconmyanmar.apps.devcon.utils;

import android.content.Context;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import org.devconmyanmar.apps.devcon.R;

import static org.devconmyanmar.apps.devcon.utils.LogUtils.LOGD;

public class AnalyticsManager {
  private final static String TAG = LogUtils.makeLogTag(AnalyticsManager.class);
  private static Context sAppContext = null;
  private static Tracker mTracker;

  private static boolean canSend() {
    // We can only send Google Analytics when ALL the following conditions are true:
    //    1. This module has been initialized.
    //    2. The user has accepted the ToS.
    //    3. Analytics is enabled in Settings.
    return sAppContext != null && mTracker != null;
  }

  public static void sendScreenView(String screenName) {
    if (canSend()) {
      mTracker.setScreenName(screenName);
      mTracker.send(new HitBuilders.AppViewBuilder().build());
      LOGD(TAG, "Screen View recorded: " + screenName);
    } else {
      LOGD(TAG, "Screen View NOT recorded (analytics disabled or not ready).");
    }
  }

  public static void sendEvent(String category, String action, String label, long value) {
    if (canSend()) {
      mTracker.send(new HitBuilders.EventBuilder().setCategory(category)
          .setAction(action)
          .setLabel(label)
          .setValue(value)
          .build());

      LOGD(TAG, "Event recorded:");
      LOGD(TAG, "\tCategory: " + category);
      LOGD(TAG, "\tAction: " + action);
      LOGD(TAG, "\tLabel: " + label);
      LOGD(TAG, "\tValue: " + value);
    } else {
      LOGD(TAG, "Analytics event ignored (analytics disabled or not ready).");
    }
  }

  public static void sendEvent(String category, String action, String label) {
    sendEvent(category, action, label, 0);
  }

  public static synchronized void initializeAnalyticsTracker(Context context) {
    sAppContext = context;
    if (mTracker == null) {
      int useProfile;
      useProfile = R.xml.analytics_release;
      mTracker = GoogleAnalytics.getInstance(context).newTracker(useProfile);
    }
  }

  public Tracker getTracker() {
    return mTracker;
  }

  public static synchronized void setTracker(Tracker tracker) {
    mTracker = tracker;
  }
}
