
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

package org.devconmyanmar.apps.devcon.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import org.devconmyanmar.apps.devcon.db.MyScheduleDao;
import org.devconmyanmar.apps.devcon.db.SpeakerDao;
import org.devconmyanmar.apps.devcon.db.TalkDao;
import org.devconmyanmar.apps.devcon.event.BusProvider;
import org.devconmyanmar.apps.devcon.utils.AnalyticsManager;

/**
 * Created by Ye Lin Aung on 14/10/05.
 */
public abstract class BaseActivity extends ActionBarActivity {

  protected SpeakerDao speakerDao;
  protected TalkDao talkDao;
  protected MyScheduleDao favDao;
  protected Context mContext;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mContext = getApplicationContext();

    speakerDao = new SpeakerDao(mContext);
    talkDao = new TalkDao(mContext);
    favDao = new MyScheduleDao(mContext);

    AnalyticsManager.initializeAnalyticsTracker(getApplicationContext());
  }

  @Override public void onResume() {
    super.onResume();
    BusProvider.getInstance().register(this);
  }

  @Override public void onPause() {
    super.onPause();
    BusProvider.getInstance().unregister(this);
  }

  public void showProgress(final boolean show, final View listView, final View progressView) {
    // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
    // for very easy animations. If available, use these APIs to fade-in
    // the progress spinner.
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
      int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

      listView.setVisibility(show ? View.GONE : View.VISIBLE);
      listView.animate()
          .setDuration(shortAnimTime)
          .alpha(show ? 0 : 1)
          .setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
              listView.setVisibility(show ? View.GONE : View.VISIBLE);
            }
          });

      progressView.setVisibility(show ? View.VISIBLE : View.GONE);
      progressView.animate()
          .setDuration(shortAnimTime)
          .alpha(show ? 1 : 0)
          .setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
              progressView.setVisibility(show ? View.VISIBLE : View.GONE);
            }
          });
    } else {
      // The ViewPropertyAnimator APIs are not available, so simply show
      // and hide the relevant UI components.
      progressView.setVisibility(show ? View.VISIBLE : View.GONE);
      listView.setVisibility(show ? View.GONE : View.VISIBLE);
    }
  }
}
