
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
  private static final String PREF_ID = "ids";

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

  public void saveFavIds(String ids){
    mEditor.putString(PREF_ID,ids).apply();
  }

  public String geFavIds(){
    return mSharedPreferences.getString(PREF_ID,"");
  }
}
