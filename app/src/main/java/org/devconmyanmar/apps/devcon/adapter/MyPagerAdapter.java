package org.devconmyanmar.apps.devcon.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import java.util.List;

/**
 * Created by Ye Lin Aung on 14/10/21.
 */
public class MyPagerAdapter extends FragmentPagerAdapter {

  private List<Fragment> fragments;

  public MyPagerAdapter(FragmentManager fm, List<Fragment> fragments) {
    super(fm);
    this.fragments = fragments;
  }

  @Override public Fragment getItem(int position) {
    return this.fragments.get(position);
  }

  @Override public int getCount() {
    return fragments.size();
  }
}
