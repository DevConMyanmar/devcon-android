package org.devconmyanmar.apps.devcon.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import org.devconmyanmar.apps.devcon.R;

/**
 * Created by Ye Lin Aung on 14/11/10.
 */
public class MyScheduleFragment extends BaseFragment {

  @InjectView(R.id.my_list) ListView mScheduleList;
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
    mToolbar.setTitleTextColor(getActivity().getResources().getColor(android.R.color.white));
    mToolbar.setNavigationIcon(R.drawable.ic_drawer);
    return rootView;
  }

  @Override public void onAttach(Activity activity) {
    super.onAttach(activity);
    activity.setTitle(getString(R.string.my_schedule_title));
  }
}
