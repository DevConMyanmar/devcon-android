package org.devconmyanmar.apps.devcon.ui;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import org.devconmyanmar.apps.devcon.R;
import org.devconmyanmar.apps.devcon.utils.DataUtils;
import org.devconmyanmar.apps.devcon.utils.SharePref;

import static org.devconmyanmar.apps.devcon.utils.LogUtils.LOGD;
import static org.devconmyanmar.apps.devcon.utils.LogUtils.makeLogTag;

/**
 * Created by Ye Lin Aung on 14/10/05.
 */
public class TalkListActivity extends BaseActivity
    implements NavigationDrawerFragment.NavigationDrawerCallbacks {

  private static final String TAG = makeLogTag(TalkListActivity.class);
  Fragment fragment = null;
  private Bundle mBundle;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_schedule_list);

    NavigationDrawerFragment mNavigationDrawerFragment =
        (NavigationDrawerFragment) getSupportFragmentManager().findFragmentById(
            R.id.navigation_drawer);

    mNavigationDrawerFragment.setUp(R.id.navigation_drawer,
        (DrawerLayout) findViewById(R.id.drawer_layout));

    if (SharePref.getInstance(this).isFirstTime()) {
      //new DataUtils(this).createFake();
      new DataUtils(this).loadFromAssets();
      SharePref.getInstance(this).noLongerFirstTime();
    } else {
      LOGD(TAG, "no longer first time");
    }
  }

  @Override
  public void onNavigationDrawerItemSelected(int position) {
    // update the main content by replacing fragments

    switch (position) {
      case 0:
        fragment = MyScheduleFragment.getInstance();
        break;
      case 1:
        fragment = TalkFragment.getInstance();
        break;
      case 2:
        fragment = SpeakerFragment.getInstance();
        break;
    }

    new Handler().postDelayed(new Runnable() {
      @Override
      public void run() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();
      }
    }, 200);
  }
}
