package org.devconmyanmar.apps.devcon;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.ButterKnife;
import butterknife.InjectView;
import org.devconmyanmar.apps.devcon.ui.BaseFragment;
import org.devconmyanmar.apps.devcon.ui.FirstDayFragment;
import org.devconmyanmar.apps.devcon.ui.SecondDayFragment;
import org.devconmyanmar.apps.devcon.ui.widget.SlidingTabLayout;

/**
 * Created by Ye Lin Aung on 14/10/05.
 */
public class ScheduleFragment extends BaseFragment {

  @InjectView(R.id.sliding_tabs) SlidingTabLayout mSlidingTabLayout;
  @InjectView(R.id.view_pager) ViewPager mViewPager;

  public ScheduleFragment() {
  }

  public static ScheduleFragment getInstance() {
    return new ScheduleFragment();
  }

  @Override public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_schedule, container, false);
    ButterKnife.inject(this, view);

    mSlidingTabLayout.setCustomTabView(R.layout.tab_indicator, android.R.id.text1);

    int mPrimaryColor = getResources().getColor(R.color.theme_primary);
    mSlidingTabLayout.setSelectedIndicatorColors(Color.WHITE);
    mSlidingTabLayout.setBackgroundColor(mPrimaryColor);

    mViewPager.setAdapter(new SlidingTabAdapter(getActivity().getSupportFragmentManager()));

    mSlidingTabLayout.setDistributeEvenly(true);
    mSlidingTabLayout.setViewPager(mViewPager);

    return view;
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
