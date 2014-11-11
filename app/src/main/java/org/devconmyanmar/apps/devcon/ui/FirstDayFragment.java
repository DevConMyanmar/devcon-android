package org.devconmyanmar.apps.devcon.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;
import com.squareup.otto.Subscribe;
import java.util.ArrayList;
import java.util.List;
import org.devconmyanmar.apps.devcon.R;
import org.devconmyanmar.apps.devcon.adapter.ScheduleAdapter;
import org.devconmyanmar.apps.devcon.event.SyncSuccessEvent;
import org.devconmyanmar.apps.devcon.model.Talk;
import org.devconmyanmar.apps.devcon.ui.widget.CustomSwipeRefreshLayout;
import org.devconmyanmar.apps.devcon.utils.ConnectionUtils;
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
  private List<Talk> mTalks = new ArrayList<Talk>();
  private CustomSwipeRefreshLayout exploreSwipeRefreshView;
  private ScheduleAdapter mScheduleAdapter;
  private StickyListHeadersListView firstDayList;

  public FirstDayFragment() {
  }

  public static FirstDayFragment getInstance() {
    return new FirstDayFragment();
  }

  @Override public void onAttach(Activity activity) {
    super.onAttach(activity);
  }

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mScheduleAdapter = new ScheduleAdapter(mContext);
    setHasOptionsMenu(true);
  }

  @Override public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {

    View rootView = inflater.inflate(R.layout.fragment_explore_list, container, false);
    firstDayList = (StickyListHeadersListView) rootView.findViewById(R.id.explore_list_view);

    exploreSwipeRefreshView =
        (CustomSwipeRefreshLayout) rootView.findViewById(R.id.explore_swipe_refresh_view);

    exploreSwipeRefreshView.setColorSchemeResources(R.color.color1, R.color.color2, R.color.color3,
        R.color.color4);

    exploreSwipeRefreshView.setStickyListHeadersListView(firstDayList);

    exploreSwipeRefreshView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
      @Override public void onRefresh() {
        if (ConnectionUtils.isOnline(mContext)) {
          syncSchedules(exploreSwipeRefreshView);
        } else {
          hideRefreshProgress(exploreSwipeRefreshView);
          Toast.makeText(mContext, R.string.no_connection_cannot_connect, Toast.LENGTH_SHORT)
              .show();
        }
      }
    });

    firstDayList.setDivider(null);

    mTalks = talkDao.getTalkByDay(FIRST_DAY);
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

  @Override public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    super.onCreateOptionsMenu(menu, inflater);
    inflater.inflate(R.menu.refresh_menu, menu);
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.refresh:
        if (ConnectionUtils.isOnline(mContext)) {
          syncSchedules(exploreSwipeRefreshView);
        } else {
          hideRefreshProgress(exploreSwipeRefreshView);
          Toast.makeText(mContext, R.string.no_connection_cannot_connect, Toast.LENGTH_SHORT)
              .show();
        }
        return true;
    }
    return super.onOptionsItemSelected(item);
  }

  @Subscribe public void syncSuccess(SyncSuccessEvent event) {
    mTalks = talkDao.getTalkByDay(FIRST_DAY);
    mScheduleAdapter.replaceWith(mTalks);
    firstDayList.setAdapter(mScheduleAdapter);
  }
}
