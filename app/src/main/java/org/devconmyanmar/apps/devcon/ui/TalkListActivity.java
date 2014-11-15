
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

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import org.devconmyanmar.apps.devcon.R;
import org.devconmyanmar.apps.devcon.adapter.DrawerListAdapter;

/**
 * Created by Ye Lin Aung on 14/10/05.
 */
public class TalkListActivity extends BaseActivity
    implements NavigationDrawerFragment.NavigationDrawerCallbacks {

  private Fragment fragment = null;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_schedule_list);

    NavigationDrawerFragment mNavigationDrawerFragment =
        (NavigationDrawerFragment) getSupportFragmentManager().findFragmentById(
            R.id.navigation_drawer);

    mNavigationDrawerFragment.setUp(R.id.navigation_drawer,
        (DrawerLayout) findViewById(R.id.drawer_layout));

    int fragmentNo = getIntent().getIntExtra("Fragment", 2);
    switch (fragmentNo) {
      case 0:
        fragment = MyScheduleFragment.getInstance();
        ((DrawerListAdapter) mNavigationDrawerFragment.mDrawerListView.getAdapter()).setChecked(0);
        break;
      case 1:
        fragment = LighteningTalksFragment.getInstance();
        ((DrawerListAdapter) mNavigationDrawerFragment.mDrawerListView.getAdapter()).setChecked(2);
        break;
      case 2:
        fragment = ExploreFragment.getInstance();
        ((DrawerListAdapter) mNavigationDrawerFragment.mDrawerListView.getAdapter()).setChecked(2);
        break;
      case 3:
        fragment = SpeakerFragment.getInstance();
        ((DrawerListAdapter) mNavigationDrawerFragment.mDrawerListView.getAdapter()).setChecked(3);
        break;
      default:
        break;
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
        fragment = LighteningTalksFragment.getInstance();
        break;
      case 2:
        fragment = ExploreFragment.getInstance();
        break;
      case 3:
        fragment = SpeakerFragment.getInstance();
        break;
    }

    new Handler().postDelayed(new Runnable() {
      @Override
      public void run() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();
      }
    }, 300);
  }
}
