package org.devconmyanmar.apps.devcon.ui;

import android.app.ActionBar;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnItemClick;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import org.devconmyanmar.apps.devcon.R;
import org.devconmyanmar.apps.devcon.ScheduleFragment;

/**
 * Created by Ye Lin Aung on 14/10/05.
 */
public class ScheduleListActivity extends BaseActivity {

  @InjectView(R.id.drawer_layout) DrawerLayout mDrawerLayout;
  @InjectView(R.id.left_drawer) ListView mDrawerList;

  private ActionBarDrawerToggle mDrawerToggle;
  private String[] mNavDrawerItems;
  private ActionBar mActionBar;
  private CharSequence mTitle;
  private CharSequence mDrawerTitle;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_schedule_list);
    ButterKnife.inject(this);

    mTitle = mDrawerTitle = getTitle();

    mNavDrawerItems = getResources().getStringArray(R.array.nav_drawer_items);

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
        mActionBar.setTitle(getString(R.string.app_name));
        invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
      }
    };

    mDrawerToggle.syncState(); // this is shit
    mDrawerLayout.setDrawerListener(mDrawerToggle);

    mDrawerList.setAdapter(
        new ArrayAdapter<String>(this, R.layout.drawer_list_item, mNavDrawerItems));

    mActionBar = getActionBar();
    if (mActionBar != null) {
      mActionBar.setIcon(android.R.color.transparent);
      mActionBar.setDisplayHomeAsUpEnabled(true);
      mActionBar.setHomeButtonEnabled(true);
    }

    SystemBarTintManager tintManager = new SystemBarTintManager(this);
    tintManager.setStatusBarTintEnabled(true);
    tintManager.setNavigationBarTintEnabled(false);
    tintManager.setTintColor(getResources().getColor(R.color.translucent_actionbar_background));

    if (Build.VERSION.SDK_INT >= 19) {
      SystemBarTintManager.SystemBarConfig config = tintManager.getConfig();
      mDrawerList.setPadding(0, config.getPixelInsetTop(true), config.getPixelInsetRight(),
          config.getPixelInsetBottom());
    }

    if (savedInstanceState == null) {
      selectItem(0);
    }

    FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
    tx.replace(R.id.content_frame, ScheduleFragment.getInstance());
    tx.commit();
  }

  @Override
  public void setTitle(CharSequence title) {
    mTitle = title;
    mActionBar.setTitle(mTitle);
  }

  @Override public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.schedule_menu, menu);
    return super.onCreateOptionsMenu(menu);
  }

  @Override public boolean onPrepareOptionsMenu(Menu menu) {
    // If the nav drawer is open, hide action items related to the content view
    boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
    menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
    return super.onPrepareOptionsMenu(menu);
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
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

  @OnItemClick(R.id.left_drawer) void selectItem(int position) {
    // update the main content by replacing fragments
    Fragment fragment;
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
      default:
        fragment = ScheduleFragment.getInstance();
        break;
    }

    FragmentManager fragmentManager = getSupportFragmentManager();
    fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();

    // update selected item and title, then close the drawer
    mDrawerList.setItemChecked(position, true);
    setTitle(mNavDrawerItems[position]);
    mDrawerLayout.closeDrawer(mDrawerList);
  }
}
