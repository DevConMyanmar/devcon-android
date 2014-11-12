package org.devconmyanmar.apps.devcon.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import org.devconmyanmar.apps.devcon.db.SpeakerDao;
import org.devconmyanmar.apps.devcon.db.TalkDao;
import org.devconmyanmar.apps.devcon.event.BusProvider;

/**
 * Created by Ye Lin Aung on 14/10/05.
 */
public abstract class BaseActivity extends ActionBarActivity {

  protected SpeakerDao speakerDao;
  protected TalkDao talkDao;
  private Context mContext;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mContext = getApplicationContext();

    speakerDao = new SpeakerDao(mContext);
    talkDao = new TalkDao(mContext);
  }

  @Override public void onResume() {
    super.onResume();
    BusProvider.getInstance().register(this);
  }

  @Override public void onPause() {
    super.onPause();
    BusProvider.getInstance().unregister(this);
  }

  public void showProgress(final boolean show, final View firstView, final View secondView) {
    // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
    // for very easy animations. If available, use these APIs to fade-in
    // the progress spinner.
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
      int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

      firstView.setVisibility(show ? View.GONE : View.VISIBLE);
      firstView.animate()
          .setDuration(shortAnimTime)
          .alpha(show ? 0 : 1)
          .setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
              firstView.setVisibility(show ? View.GONE : View.VISIBLE);
            }
          });

      secondView.setVisibility(show ? View.VISIBLE : View.GONE);
      secondView.animate()
          .setDuration(shortAnimTime)
          .alpha(show ? 1 : 0)
          .setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
              secondView.setVisibility(show ? View.VISIBLE : View.GONE);
            }
          });
    } else {
      // The ViewPropertyAnimator APIs are not available, so simply show
      // and hide the relevant UI components.
      secondView.setVisibility(show ? View.VISIBLE : View.GONE);
      firstView.setVisibility(show ? View.GONE : View.VISIBLE);
    }
  }
}
