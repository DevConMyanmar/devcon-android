package org.devconmyanmar.apps.devcon.ui;

import android.app.ActionBar;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import butterknife.ButterKnife;
import butterknife.InjectView;
import org.devconmyanmar.apps.devcon.R;
import org.devconmyanmar.apps.devcon.ui.widget.SlidingTabLayout;

/**
 * Created by Ye Lin Aung on 14/10/05.
 */
public class ScheduleListActivity extends BaseActivity {

  @InjectView(R.id.sliding_tabs) SlidingTabLayout mSlidingTabLayout;
  @InjectView(R.id.view_pager) ViewPager mViewPager;

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

    ActionBar mActionBar = getActionBar();
    if (mActionBar != null) {
      mActionBar.setIcon(android.R.color.transparent);
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
