package org.devconmyanmar.apps.devcon.ui;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import org.devconmyanmar.apps.devcon.R;
import org.devconmyanmar.apps.devcon.utils.DataUtils;
import org.devconmyanmar.apps.devcon.utils.SharePref;

import static org.devconmyanmar.apps.devcon.utils.LogUtils.LOGD;
import static org.devconmyanmar.apps.devcon.utils.LogUtils.makeLogTag;

/**
 * Created by Ye Lin Aung on 14/10/05.
 */
public class ScheduleListActivity extends BaseActivity
    implements NavigationDrawerFragment.NavigationDrawerCallbacks {

  private static final String TAG = makeLogTag(ScheduleListActivity.class);

  private NavigationDrawerFragment mNavigationDrawerFragment;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_schedule_list);

    mNavigationDrawerFragment =
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
    Fragment fragment = null;
    switch (position) {
      case 0:
        fragment = ScheduleFragment.getInstance();
        break;
      case 1:
        fragment = SpeakerFragment.getInstance();
        break;
      case 2:
        fragment = UpdateFragment.getInstance();
        break;
    }

    final Fragment finalFragment = fragment;
    new Handler().postDelayed(new Runnable() {
      @Override
      public void run() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.container, finalFragment).commit();
      }
    }, 200);
  }

  public void restoreActionBar() {
    android.support.v7.app.ActionBar actionBar = getSupportActionBar();
    CharSequence title = getTitle();
    if (actionBar != null) {
      actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
      actionBar.setDisplayShowTitleEnabled(true);
      actionBar.setTitle(title);
    }
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    if (mNavigationDrawerFragment.isDrawerOpen()) {
      // Show App Label When drawer is Open
      ActionBar actionBar = getSupportActionBar();
      if (actionBar != null) {
        actionBar.setTitle(getString(R.string.app_name));
      }
    }
    if (!mNavigationDrawerFragment.isDrawerOpen()) {
      // Only show items in the action bar relevant to this screen
      // if the drawer is not showing. Otherwise, let the drawer
      // decide what to show in the action bar.
      restoreActionBar();
      getMenuInflater().inflate(R.menu.schedule_menu, menu);
      return true;
    }
    return super.onCreateOptionsMenu(menu);
  }
}
