package org.devconmyanmar.apps.devcon.utils;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

/**
 * Created by Ye Lin Aung on 14/08/27.
 */
public class ConnectionUtils {
  public static boolean isOnline(Context c) {
    NetworkInfo netInfo = null;
    try {
      ConnectivityManager cm =
          (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);
      netInfo = cm.getActiveNetworkInfo();
    } catch (SecurityException e) {
      e.printStackTrace();
    }
    return netInfo != null && netInfo.isConnectedOrConnecting();
  }

  public static String getUUID(Activity a) {
    TelephonyManager tManager = (TelephonyManager) a.getSystemService(Context.TELEPHONY_SERVICE);
    return tManager.getDeviceId();
  }
}
