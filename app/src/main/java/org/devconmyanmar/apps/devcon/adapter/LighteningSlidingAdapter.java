package org.devconmyanmar.apps.devcon.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import org.devconmyanmar.apps.devcon.R;
import org.devconmyanmar.apps.devcon.ui.LighteningTalkDaysFragment;

/**
 * Created by yemyatthu on 11/15/14.
 */
public class LighteningSlidingAdapter extends FragmentStatePagerAdapter {
  private Context mContext;

  public LighteningSlidingAdapter(FragmentManager fm, Context context) {
    super(fm);
    this.mContext = context;
  }

  @Override public Fragment getItem(int position) {
    return LighteningTalkDaysFragment.getInstance(position+1);

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
