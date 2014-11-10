package org.devconmyanmar.apps.devcon.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.ButterKnife;
import butterknife.InjectView;
import org.devconmyanmar.apps.devcon.R;
import org.devconmyanmar.apps.devcon.ui.widget.SlidingTabLayout;

import static org.devconmyanmar.apps.devcon.utils.LogUtils.makeLogTag;

/**
 * Created by Ye Lin Aung on 14/10/05.
 */
public class ScheduleFragment extends BaseFragment {

  private static final String TAG = makeLogTag(ScheduleFragment.class);
  @InjectView(R.id.sliding_tabs) SlidingTabLayout mSlidingTabLayout;
  @InjectView(R.id.view_pager) ViewPager mViewPager;
  @InjectView(R.id.toolbar) Toolbar mToolbar;
  public ScheduleFragment() {
  }

  public static ScheduleFragment getInstance() {
    return new ScheduleFragment();
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    getActivity().setTitle(getString(R.string.schedule_activity));
  }

  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
    @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_schedule, container, false);
    ButterKnife.inject(this, view);
    ((BaseActivity)getActivity()).setSupportActionBar(mToolbar);
    mToolbar.setNavigationIcon(R.drawable.ic_drawer);
    mToolbar.setTitleTextColor(getActivity().getResources().getColor(android.R.color.white));
    mSlidingTabLayout.setCustomTabView(R.layout.tab_indicator, android.R.id.text1);

    int mPrimaryColor = getResources().getColor(R.color.theme_primary);
    mSlidingTabLayout.setSelectedIndicatorColors(Color.WHITE);
    mSlidingTabLayout.setBackgroundColor(mPrimaryColor);

    mViewPager.setAdapter(new SlidingTabAdapter(getActivity().getSupportFragmentManager()));

    mSlidingTabLayout.setDistributeEvenly(true);
    mSlidingTabLayout.setViewPager(mViewPager);

    return view;
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
  }

  private class SlidingTabAdapter extends FragmentStatePagerAdapter {

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
