package org.devconmyanmar.apps.devcon.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import org.devconmyanmar.apps.devcon.R;
import org.devconmyanmar.apps.devcon.ui.FirstDayFragment;
import org.devconmyanmar.apps.devcon.ui.SecondDayFragment;

/**
 * Created by Ye Lin Aung on 14/11/12.
 */
public class SlidingTabAdapter extends FragmentStatePagerAdapter {

  private Context mContext;

  public SlidingTabAdapter(FragmentManager fm, Context context) {
    super(fm);
    this.mContext = context;
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
        return mContext.getResources().getString(R.string.first_day);
      case 1:
        return mContext.getResources().getString(R.string.second_day);
    }
    return super.getPageTitle(position);
  }
}
