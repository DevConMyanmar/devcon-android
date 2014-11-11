package org.devconmyanmar.apps.devcon.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import java.util.ArrayList;
import java.util.List;
import org.devconmyanmar.apps.devcon.R;
import org.devconmyanmar.apps.devcon.adapter.ScheduleAdapter;
import org.devconmyanmar.apps.devcon.model.Talk;
import org.devconmyanmar.apps.devcon.ui.widget.CustomSwipeRefreshLayout;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

import static org.devconmyanmar.apps.devcon.Config.POSITION;
import static org.devconmyanmar.apps.devcon.utils.LogUtils.LOGD;
import static org.devconmyanmar.apps.devcon.utils.LogUtils.makeLogTag;

/**
 * Created by Ye Lin Aung on 14/10/05.
 */
public class FirstDayFragment extends BaseFragment {

  private static final String TAG = makeLogTag(FirstDayFragment.class);
  private static final String FIRST_DAY = "2014-11-15";
  //@InjectView(R.id.first_day_list) StickyListHeadersListView firstDayList;
  private List<Talk> mTalks = new ArrayList<Talk>();

  public FirstDayFragment() {
  }

  public static FirstDayFragment getInstance() {
    return new FirstDayFragment();
  }

  @Override public void onAttach(Activity activity) {
    super.onAttach(activity);
  }

  @Override public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {

    View rootView = inflater.inflate(R.layout.fragment_explore_list, container, false);
    StickyListHeadersListView firstDayList =
        (StickyListHeadersListView) rootView.findViewById(R.id.explore_list_view);

    CustomSwipeRefreshLayout exploreSwipeRefreshView =
        (CustomSwipeRefreshLayout) rootView.findViewById(R.id.explore_swipe_refresh_view);

    exploreSwipeRefreshView.setColorSchemeResources(R.color.color1, R.color.color2, R.color.color3,
        R.color.color4);

    firstDayList.setDivider(null);

    mTalks = talkDao.getTalkByDay(FIRST_DAY);
    LOGD(TAG, "first day : " + mTalks.size());
    ScheduleAdapter mScheduleAdapter = new ScheduleAdapter(mContext);
    mScheduleAdapter.replaceWith(mTalks);

    firstDayList.setAdapter(mScheduleAdapter);

    firstDayList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override public void onItemClick(AdapterView<?> adapterView, View view, int position,
          long l) {
        int id = mTalks.get(position).getId();
        LOGD(TAG, "Talk Type -> " + mTalks.get(position).getTalk_type());
        Intent i = new Intent(getActivity(), TalkDetailActivity.class);
        i.putExtra(POSITION, id);
        startActivity(i);
      }
    });

    return rootView;
  }
}
