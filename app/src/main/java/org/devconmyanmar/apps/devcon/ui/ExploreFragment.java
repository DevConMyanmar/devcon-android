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
import org.devconmyanmar.apps.devcon.adapter.SlidingTabAdapter;
import org.devconmyanmar.apps.devcon.ui.widget.SlidingTabLayout;
import org.devconmyanmar.apps.devcon.utils.AnalyticsManager;
import org.devconmyanmar.apps.devcon.utils.HelpUtils;

import static org.devconmyanmar.apps.devcon.utils.LogUtils.makeLogTag;

/**
 * Created by Ye Lin Aung on 14/10/05.
 */
public class ExploreFragment extends BaseFragment {
  private static final String TAG = makeLogTag(ExploreFragment.class);
  @InjectView(R.id.sliding_tabs) SlidingTabLayout mSlidingTabLayout;
  @InjectView(R.id.view_pager) ViewPager mViewPager;
  @InjectView(R.id.toolbar) Toolbar mToolbar;
  private BaseActivity mActivity;

  private final static String SCREEN_LABEL = "Explore";

  public ExploreFragment() {
  }

  public static ExploreFragment getInstance() {
    return new ExploreFragment();
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
    View view = inflater.inflate(R.layout.fragment_schedule, container, false);
    ButterKnife.inject(this, view);
    mActivity.setSupportActionBar(mToolbar);
    ActionBar actionBar = mActivity.getSupportActionBar();
    actionBar.setTitle(R.string.explore_title);
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

  @Override public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    inflater.inflate(R.menu.refresh_menu, menu);
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
