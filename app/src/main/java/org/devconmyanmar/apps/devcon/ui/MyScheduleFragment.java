
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

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import org.devconmyanmar.apps.devcon.R;
import org.devconmyanmar.apps.devcon.adapter.MyScheduleSlidingAdapter;
import org.devconmyanmar.apps.devcon.ui.widget.SlidingTabLayout;
import org.devconmyanmar.apps.devcon.utils.AnalyticsManager;
import org.devconmyanmar.apps.devcon.utils.HelpUtils;

import static org.devconmyanmar.apps.devcon.utils.LogUtils.makeLogTag;

/**
 * Created by Ye Lin Aung on 14/10/05.
 */
public class MyScheduleFragment extends BaseFragment {
  private static final String TAG = makeLogTag(ExploreFragment.class);
  private final static String SCREEN_LABEL = "My Schedule";
  @InjectView(R.id.sliding_tabs) SlidingTabLayout mSlidingTabLayout;
  @InjectView(R.id.favorite_view_pager) ViewPager mViewPager;
  @InjectView(R.id.toolbar) Toolbar mToolbar;
  private BaseActivity mActivity;

  public MyScheduleFragment() {
  }

  public static MyScheduleFragment getInstance() {
    return new MyScheduleFragment();
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setHasOptionsMenu(true);
    mActivity = (BaseActivity) getActivity();

    AnalyticsManager.sendScreenView(SCREEN_LABEL);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_my_schedule, container, false);
    ButterKnife.inject(this, view);
    mActivity.setSupportActionBar(mToolbar);
    ActionBar actionBar = mActivity.getSupportActionBar();
    actionBar.setTitle(R.string.my_schedule_title);
    actionBar.setDisplayHomeAsUpEnabled(true);
    actionBar.setHomeAsUpIndicator(R.drawable.ic_drawer);
    mSlidingTabLayout.setCustomTabView(R.layout.tab_indicator, android.R.id.text1);
    int mPrimaryColor = getResources().getColor(R.color.theme_primary);
    mSlidingTabLayout.setSelectedIndicatorColors(Color.WHITE);
    mSlidingTabLayout.setBackgroundColor(mPrimaryColor);

    mViewPager.setAdapter(
        new MyScheduleSlidingAdapter(getActivity().getSupportFragmentManager(), mContext));

    mSlidingTabLayout.setDistributeEvenly(true);
    mSlidingTabLayout.setViewPager(mViewPager);

    if (Build.VERSION.SDK_INT >= 19) {
      SystemBarTintManager tintManager = new SystemBarTintManager(getActivity());
      tintManager.setStatusBarTintEnabled(true);
      tintManager.setNavigationBarTintEnabled(false);
      tintManager.setTintColor(getResources().getColor(R.color.translucent_actionbar_background));
    }

    return view;
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
  }

  @Override public void onResume() {
    super.onResume();
  }

  @Override public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    inflater.inflate(R.menu.schedule_menu, menu);
    super.onCreateOptionsMenu(menu, inflater);
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.action_about:
        HelpUtils.showAbout(getActivity());
        return true;
      case android.R.id.home:
        getActivity().finish();
        return true;
      default:
        return super.onOptionsItemSelected(item);
    }
  }
}

