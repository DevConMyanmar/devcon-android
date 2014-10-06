package org.devconmyanmar.apps.devcon.ui;

import android.app.ActionBar;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
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
import io.realm.Realm;
import org.devconmyanmar.apps.devcon.R;
import org.devconmyanmar.apps.devcon.ScheduleFragment;
import org.devconmyanmar.apps.devcon.model.Speaker;
import org.devconmyanmar.apps.devcon.model.Talk;

import static org.devconmyanmar.apps.devcon.utils.LogUtils.makeLogTag;

/**
 * Created by Ye Lin Aung on 14/10/05.
 */
public class ScheduleListActivity extends BaseActivity {

  private static final String TAG = makeLogTag(ScheduleListActivity.class);
  @InjectView(R.id.drawer_layout) DrawerLayout mDrawerLayout;
  @InjectView(R.id.left_drawer) ListView mDrawerList;
  private ActionBarDrawerToggle mDrawerToggle;
  private String[] mNavDrawerItems;
  private ActionBar mActionBar;
  private CharSequence mTitle;
  private CharSequence mDrawerTitle;
  private Bundle mInstanceState;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_schedule_list);
    ButterKnife.inject(this);

    this.mInstanceState = savedInstanceState;

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

    //if (mInstanceState == null) {
    //  selectItem(0);
    //}

    //FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
    //tx.replace(R.id.content_frame, ScheduleFragment.getInstance());
    //tx.commit();

    createFake();
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

    if (mInstanceState == null) {
      getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragment).commit();
    }

    // update selected item and title, then close the drawer
    mDrawerList.setItemChecked(position, true);
    setTitle(mNavDrawerItems[position]);
    mDrawerLayout.closeDrawer(mDrawerList);
  }

  private void createFake() {

    // Empty first
    Realm.deleteRealmFile(this);

    String[] myFakeSpeakers = new String[] {
        "John Graham-Cumming", "Brad Fitzpatrick", "Jeremy Saenz", "Andrew Gerrand", "Keith Rarick",
        "Blake Mizerany"
    };

    String[] myFakeSpeakersTitle = new String[] {
        "Programmer at CloudFlare, Author of The Geek Atlas",
        "Creator of memcached, OpenID & LiveJournal\n" + "Member of the Go team",
        "Creator of Martini and Negroni", "Member of the Go team",
        "Creator of godep, Beanstalkd and Doozer\n" + "Former programmer at Heroku",
        "Creator of Sinatra, co-creator of Doozer"
    };

    String[] myFakeTalkTitles = new String[] {
        "The greatest machine that never was", "Software I'm excited about",
        "Go, Martini and Gophercasts", "Writing Web Apps in Go", "Go Dependency Management",
        "Inside the Gophers Studio"
    };

    String[] talkPhotos = new String[] {
        "http://www.dotgo.eu/images/speakers/john-graham-cumming.png",
        "http://www.dotgo.eu/images/speakers/brad-fitzpatrick.png",
        "http://www.dotgo.eu/images/speakers/jeremy-saenz.png",
        "http://www.dotgo.eu/images/speakers/andrew-gerrand.png",
        "http://www.dotgo.eu/images/speakers/keith-rarick.png",
        "http://www.dotgo.eu/images/speakers/blake-mizerany.png"
    };

    String[] talkDates = new String[] {
        "Oct 15", "Oct 15", "Oct 15", "Oct 16", "Oct 16", "Oct 16"
    };

    int[] talkTypes = new int[] {
        0, 1, 2, 0, 1, 2
    };

    Realm realm = Realm.getInstance(this);
    realm.beginTransaction();

    for (int i = 0; i < myFakeSpeakers.length; i++) {
      Talk t = realm.createObject(Talk.class);
      t.setId(i);
      t.setTitle(myFakeTalkTitles[i]);
      t.setDate(talkDates[i]);
      t.setPhoto(talkPhotos[i]);
      t.setTalk_type(talkTypes[i]);

      Speaker s = realm.createObject(Speaker.class);
      s.setId(i);
      s.setName(myFakeSpeakers[i]);
      s.setTitle(myFakeSpeakersTitle[i]);
      s.setDescription("hello " + i);
    }

    realm.commitTransaction();
  }
}
