package org.devconmyanmar.apps.devcon.ui;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import io.realm.Realm;
import org.devconmyanmar.apps.devcon.R;
import org.devconmyanmar.apps.devcon.model.Speaker;
import org.devconmyanmar.apps.devcon.model.Talk;

import static org.devconmyanmar.apps.devcon.utils.LogUtils.makeLogTag;

/**
 * Created by Ye Lin Aung on 14/10/05.
 */
public class ScheduleListActivity extends BaseActivity
    implements NavigationDrawerFragment.NavigationDrawerCallbacks {

  private static final String TAG = makeLogTag(ScheduleListActivity.class);

  private CharSequence mTitle;

  private NavigationDrawerFragment mNavigationDrawerFragment;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_schedule_list);

    mNavigationDrawerFragment =
        (NavigationDrawerFragment) getSupportFragmentManager().findFragmentById(
            R.id.navigation_drawer);

    mTitle = getTitle();

    mNavigationDrawerFragment.setUp(R.id.navigation_drawer,
        (DrawerLayout) findViewById(R.id.drawer_layout));

    createFake();
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

    FragmentManager fragmentManager = getSupportFragmentManager();
    fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();
  }

  public void restoreActionBar() {
    ActionBar actionBar = getActionBar();
    if (actionBar != null) {
      actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
      actionBar.setDisplayShowTitleEnabled(true);
      actionBar.setTitle(mTitle);
    }
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
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
        //"https://dl.dropboxusercontent.com/u/2709123/default.png",
        //"https://dl.dropboxusercontent.com/u/2709123/default.png",
        //"https://dl.dropboxusercontent.com/u/2709123/default.png",
        //"https://dl.dropboxusercontent.com/u/2709123/default.png",
        //"https://dl.dropboxusercontent.com/u/2709123/default.png",
        //"https://dl.dropboxusercontent.com/u/2709123/default.png"
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
