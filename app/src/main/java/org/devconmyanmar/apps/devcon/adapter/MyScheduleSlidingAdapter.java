package org.devconmyanmar.apps.devcon.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import org.devconmyanmar.apps.devcon.R;
import org.devconmyanmar.apps.devcon.ui.FavoriteDayFragment;

/**
 * Created by Ye Lin Aung on 14/11/12.
 */
public class MyScheduleSlidingAdapter extends FragmentStatePagerAdapter {

  private Context mContext;

  public MyScheduleSlidingAdapter(FragmentManager fm, Context context) {
    super(fm);
    this.mContext = context;
  }

  @Override public Fragment getItem(int position) {
    return FavoriteDayFragment.getInstance(position);
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
