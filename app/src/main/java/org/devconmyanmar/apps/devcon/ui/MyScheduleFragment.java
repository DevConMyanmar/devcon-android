package org.devconmyanmar.apps.devcon.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
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
  private BaseActivity mActivity;

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
    mActivity = (BaseActivity)getActivity();
    setHasOptionsMenu(true);
  }

  @Override public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View rootView = inflater.inflate(R.layout.fragment_my_schedule, container, false);
    ButterKnife.inject(this, rootView);

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
        new SlidingTabAdapter(getActivity().getSupportFragmentManager(), mContext));

    mSlidingTabLayout.setDistributeEvenly(true);
    mSlidingTabLayout.setViewPager(mViewPager);

    return rootView;
  }

}
