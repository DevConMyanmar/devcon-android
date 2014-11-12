package org.devconmyanmar.apps.devcon.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import static org.devconmyanmar.apps.devcon.utils.LogUtils.makeLogTag;

/**
 * Created by Ye Lin Aung on 14/08/12.
 */
public class SharePref {

  /** Boolean to check if the app is launched for the first time * */
  private static final String PREF_FIRST_TIME_CHECK = "pref_first_time_check";
  private static final String PREF_MENU_CHECK = "pref_menu_check";

  private static final String TAG = makeLogTag(SharePref.class);
  private static SharePref pref;
  protected SharedPreferences mSharedPreferences;
  protected SharedPreferences.Editor mEditor;
  protected Context mContext;

  public SharePref(Context context) {
    mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    mEditor = mSharedPreferences.edit();

    this.mContext = context;
  }

  public static synchronized SharePref getInstance(Context mContext) {
    if (pref == null) {
      pref = new SharePref(mContext);
    }
    return pref;
  }

  public boolean isFirstTime() {
    return mSharedPreferences.getBoolean(PREF_FIRST_TIME_CHECK, true);
  }

  public void noLongerFirstTime() {
    mEditor.putBoolean(PREF_FIRST_TIME_CHECK, false).apply();
  }
}
