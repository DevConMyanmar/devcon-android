package org.devconmyanmar.apps.devcon.ui;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.ButterKnife;
import butterknife.InjectView;
import org.devconmyanmar.apps.devcon.R;
import org.devconmyanmar.apps.devcon.adapter.SlidingTabAdapter;
import org.devconmyanmar.apps.devcon.ui.widget.SlidingTabLayout;

/**
 * Created by Ye Lin Aung on 14/11/10.
 */
public class MyScheduleFragment extends BaseFragment {

  @InjectView(R.id.sliding_tabs) SlidingTabLayout mSlidingTabLayout;
  @InjectView(R.id.view_pager) ViewPager mViewPager;
  @InjectView(R.id.toolbar) Toolbar mToolbar;

  public MyScheduleFragment() {
  }

  public static MyScheduleFragment getInstance() {
    return new MyScheduleFragment();
  }

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View rootView = inflater.inflate(R.layout.fragment_my_schedule, container, false);
    ButterKnife.inject(this, rootView);

    ((BaseActivity) getActivity()).setSupportActionBar(mToolbar);

    ((BaseActivity) getActivity()).setSupportActionBar(mToolbar);
    mToolbar.setNavigationIcon(R.drawable.ic_drawer);
    mToolbar.setTitleTextColor(getActivity().getResources().getColor(android.R.color.white));
    mSlidingTabLayout.setCustomTabView(R.layout.tab_indicator, android.R.id.text1);

    int mPrimaryColor = getResources().getColor(R.color.theme_primary);
    mSlidingTabLayout.setSelectedIndicatorColors(Color.WHITE);
    mSlidingTabLayout.setBackgroundColor(mPrimaryColor);

    mViewPager.setAdapter(
        new SlidingTabAdapter(getActivity().getSupportFragmentManager(), mContext));

    mSlidingTabLayout.setDistributeEvenly(true);
    mSlidingTabLayout.setViewPager(mViewPager);

    return rootView;
  }

  @Override public void onAttach(Activity activity) {
    super.onAttach(activity);
    activity.setTitle(getString(R.string.my_schedule_title));
  }
}
