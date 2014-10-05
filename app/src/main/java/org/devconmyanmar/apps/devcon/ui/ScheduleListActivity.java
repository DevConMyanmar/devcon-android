package org.devconmyanmar.apps.devcon.ui;

import android.app.ActionBar;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import org.devconmyanmar.apps.devcon.R;
import org.devconmyanmar.apps.devcon.ui.widget.SlidingTabLayout;

/**
 * Created by Ye Lin Aung on 14/10/05.
 */
public class ScheduleListActivity extends BaseActivity {

  @InjectView(R.id.sliding_tabs) SlidingTabLayout mSlidingTabLayout;
  @InjectView(R.id.view_pager) ViewPager mViewPager;
  @InjectView(R.id.drawer_layout) DrawerLayout mDrawerLayout;
  @InjectView(R.id.left_drawer) ListView mDrawerList;

  private ActionBarDrawerToggle mDrawerToggle;
  private String[] mPlanetTitles;
  private ActionBar mActionBar;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_schedule_list);
    ButterKnife.inject(this);

    mSlidingTabLayout.setCustomTabView(R.layout.tab_indicator, android.R.id.text1);

    int mPrimaryColor = getResources().getColor(R.color.theme_primary);
    mSlidingTabLayout.setSelectedIndicatorColors(Color.WHITE);
    mSlidingTabLayout.setBackgroundColor(mPrimaryColor);

    mViewPager.setAdapter(new SlidingTabAdapter(getSupportFragmentManager()));

    mSlidingTabLayout.setDistributeEvenly(true);
    mSlidingTabLayout.setViewPager(mViewPager);

    final String mTitle = getString(R.string.schedule_activity);
    final String mDrawerTitle = getString(R.string.app_name);

    mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, Gravity.START);
    mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.drawable.ic_navigation_drawer,
        R.string.drawer_open, R.string.drawer_close) {

      public void onDrawerClosed(View view) {
        super.onDrawerClosed(view);
        mActionBar.setTitle(mTitle);
        invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
      }

      public void onDrawerOpened(View drawerView) {
        super.onDrawerOpened(drawerView);
        mActionBar.setTitle(mDrawerTitle);
        invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
      }
    };

    mDrawerToggle.syncState(); // this is shit
    mDrawerLayout.setDrawerListener(mDrawerToggle);

    mActionBar = getActionBar();
    if (mActionBar != null) {
      mActionBar.setIcon(android.R.color.transparent);
      //mActionBar.setTitle(getString(R.string.schedule_activity));
      mActionBar.setDisplayHomeAsUpEnabled(true);
      mActionBar.setHomeButtonEnabled(true);
    }

    SystemBarTintManager tintManager = new SystemBarTintManager(this);
    tintManager.setStatusBarTintEnabled(true);
    tintManager.setNavigationBarTintEnabled(false);
    tintManager.setTintColor(getResources().getColor(R.color.translucent_actionbar_background));
  }

  @Override public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.schedule_menu, menu);
    return super.onCreateOptionsMenu(menu);
  }

  /* Called whenever we call invalidateOptionsMenu() */
  @Override
  public boolean onPrepareOptionsMenu(Menu menu) {
    // If the nav drawer is open, hide action items related to the content view
    boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
    menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
    return super.onPrepareOptionsMenu(menu);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // The action bar home/up action should open or close the drawer.
    // ActionBarDrawerToggle will take care of this.
    if (mDrawerToggle.onOptionsItemSelected(item)) {
      return true;
    }
    // Handle action buttons
    switch (item.getItemId()) {
      default:
        return super.onOptionsItemSelected(item);
    }
  }

  private class SlidingTabAdapter extends FragmentPagerAdapter {

    public SlidingTabAdapter(FragmentManager fm) {
      super(fm);
    }

    @Override public Fragment getItem(int position) {
      switch (position) {
        case 0:
          return FirstDayFragment.getInstance();
        case 1:
          return SecondDayFragment.getInstance();
      }
      return null;
    }

    @Override public int getCount() {
      return 2;
    }

    @Override public CharSequence getPageTitle(int position) {
      switch (position) {
        case 0:
          return getString(R.string.first_day);
        case 1:
          return getString(R.string.second_day);
      }
      return super.getPageTitle(position);
    }
  }
}
