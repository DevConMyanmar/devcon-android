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

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.Bind;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import org.devconmyanmar.apps.devcon.R;
import org.devconmyanmar.apps.devcon.adapter.DrawerListAdapter;

/**
 * Created by Ye Lin Aung on 14/10/06.
 */
public class NavigationDrawerFragment extends BaseFragment {

  private static final String PREF_USER_LEARNED_DRAWER = "navigation_drawer_learned";
  private static final String STATE_SELECTED_POSITION = "selected_navigation_drawer_position";
  @Bind(R.id.drawer_list) ListView mDrawerListView;
  @Bind(R.id.drawer_title) TextView mDrawerTitle;
  private int mCurrentSelectedPosition = 1;
  private boolean mFromSavedInstanceState;
  private boolean mUserLearnedDrawer;
  private View mFragmentContainerView;
  private ActionBarDrawerToggle mDrawerToggle;
  private NavigationDrawerCallbacks mCallbacks;
  private DrawerLayout mDrawerLayout;
  private Activity mActivity;

  public NavigationDrawerFragment() {
  }

  public static NavigationDrawerFragment getInstance() {
    return new NavigationDrawerFragment();
  }

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
    mUserLearnedDrawer = sp.getBoolean(PREF_USER_LEARNED_DRAWER, false);
    mActivity = (ActionBarActivity) getActivity();

    if (savedInstanceState != null) {
      mCurrentSelectedPosition = savedInstanceState.getInt(STATE_SELECTED_POSITION);
      mFromSavedInstanceState = true;
    }

    selectItem(mCurrentSelectedPosition);
  }

  @Override public void onActivityCreated(Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    // Indicate that this fragment would like to influence the set of actions in the action bar.
    setHasOptionsMenu(true);
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View v = inflater.inflate(R.layout.fragment_navigation_drawer, container, false);
    ButterKnife.bind(this, v);
    mDrawerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        selectItem(position);
        ((DrawerListAdapter) mDrawerListView.getAdapter()).setChecked(mCurrentSelectedPosition);
        ((DrawerListAdapter) mDrawerListView.getAdapter()).notifyDataSetChanged();
      }
    });

    mDrawerTitle.setPadding(0, getStatusBarHeight(), 0, 0);
    DrawerListAdapter adapter = new DrawerListAdapter(getActivity());
    adapter.setChecked(mCurrentSelectedPosition);
    mDrawerListView.setAdapter(adapter);
    mDrawerListView.setItemChecked(mCurrentSelectedPosition, true);

    if (Build.VERSION.SDK_INT >= 19) {

      SystemBarTintManager tintManager = new SystemBarTintManager(getActivity());
      tintManager.setStatusBarTintEnabled(true);
      tintManager.setNavigationBarTintEnabled(false);
      tintManager.setTintColor(getResources().getColor(R.color.translucent_actionbar_background));
    }

    return v;
  }

  public boolean isDrawerOpen() {
    return mDrawerLayout != null && mDrawerLayout.isDrawerOpen(mFragmentContainerView);
  }

  public void setUp(int fragmentId, DrawerLayout drawerLayout) {
    mFragmentContainerView = getActivity().findViewById(fragmentId);
    mDrawerLayout = drawerLayout;

    // set a custom shadow that overlays the main content when the drawer opens
    mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
    // set up the drawer's list view with items and click listener

    // ActionBarDrawerToggle ties together the the proper interactions
    // between the navigation drawer and the action bar app icon.
    mDrawerToggle = new ActionBarDrawerToggle(getActivity(), mDrawerLayout, R.drawable.ic_drawer,
        R.string.drawer_open, R.string.drawer_close) {
      @Override
      public void onDrawerClosed(View drawerView) {
        super.onDrawerClosed(drawerView);
        if (!isAdded()) {
          return;
        }

        getActivity().supportInvalidateOptionsMenu(); // calls onPrepareOptionsMenu()
      }

      @Override
      public void onDrawerOpened(View drawerView) {
        super.onDrawerOpened(drawerView);
        if (!isAdded()) {
          return;
        }

        if (!mUserLearnedDrawer) {
          // The user manually opened the drawer; store this flag to prevent auto-showing
          // the navigation drawer automatically in the future.
          mUserLearnedDrawer = true;
          SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
          sp.edit().putBoolean(PREF_USER_LEARNED_DRAWER, true).commit();
        }

        getActivity().supportInvalidateOptionsMenu(); // calls onPrepareOptionsMenu()
      }
    };

    // If the user hasn't 'learned' about the drawer, open it to introduce them to the drawer,
    // per the navigation drawer design guidelines.
    if (!mUserLearnedDrawer && !mFromSavedInstanceState) {
      mDrawerLayout.openDrawer(mFragmentContainerView);
    }

    // Defer code dependent on restoration of previous instance state.
    mDrawerLayout.post(new Runnable() {
      @Override
      public void run() {
        mDrawerToggle.syncState();
      }
    });

    mDrawerLayout.setDrawerListener(mDrawerToggle);
  }

  @Override public void onAttach(Activity activity) {
    super.onAttach(activity);
    try {
      mCallbacks = (NavigationDrawerCallbacks) activity;
    } catch (ClassCastException e) {
      throw new ClassCastException("Activity must implement NavigationDrawerCallbacks.");
    }
  }

  @Override public void onDetach() {
    super.onDetach();
    mCallbacks = null;
  }

  @Override public void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    outState.putInt(STATE_SELECTED_POSITION, mCurrentSelectedPosition);
  }

  @Override public void onConfigurationChanged(Configuration newConfig) {
    super.onConfigurationChanged(newConfig);
    // Forward the new configuration the drawer toggle component.
    mDrawerToggle.onConfigurationChanged(newConfig);
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

  private void selectItem(int position) {
    mCurrentSelectedPosition = position;
    if (mDrawerListView != null) {
      mDrawerListView.setItemChecked(position, true);
    }
    if (mDrawerLayout != null) {
      mDrawerLayout.closeDrawer(mFragmentContainerView);
    }
    if (mCallbacks != null) {
      mCallbacks.onNavigationDrawerItemSelected(position);
    }
  }

  public int getStatusBarHeight() {
    int result = 0;
    int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
    if (resourceId > 0) {
      result = getResources().getDimensionPixelSize(resourceId);
    }
    return result;
  }

  public static interface NavigationDrawerCallbacks {
    /**
     * Called when an item in the navigation drawer is selected.
     */
    void onNavigationDrawerItemSelected(int position);
  }
}
