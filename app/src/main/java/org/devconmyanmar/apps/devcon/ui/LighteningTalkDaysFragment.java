package org.devconmyanmar.apps.devcon.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.squareup.otto.Subscribe;
import java.util.ArrayList;
import java.util.List;
import org.devconmyanmar.apps.devcon.R;
import org.devconmyanmar.apps.devcon.adapter.LighteningTalkAdapter;
import org.devconmyanmar.apps.devcon.event.SyncSuccessEvent;
import org.devconmyanmar.apps.devcon.model.Talk;
import org.devconmyanmar.apps.devcon.utils.AnalyticsManager;
import org.devconmyanmar.apps.devcon.utils.ConnectionUtils;

/**
 * Created by yemyatthu on 11/15/14.
 */
public class LighteningTalkDaysFragment extends BaseFragment {
  private static final String FIRST_DAY = "2015-11-15";
  private static final String SECOND_DAY = "2015-11-16";
  private final static String SCREEN_LABEL = "Explore First Day";
  private List<Talk> mTalks = new ArrayList<Talk>();
  private List<Talk> mLighteningTalks = new ArrayList<Talk>();
  private SwipeRefreshLayout exploreSwipeRefreshView;
  private LighteningTalkAdapter lighteningTalkAdapter;

  @Bind(R.id.explore_list_view) RecyclerView talkRecyclerView;
  @Bind(R.id.empty_text) TextView emptyTextView;

  public LighteningTalkDaysFragment() {
  }

  public static LighteningTalkDaysFragment getInstance(int position) {
    Bundle bundle = new Bundle();
    bundle.putInt("Day", position);
    LighteningTalkDaysFragment lighteningTalkDaysFragment = new LighteningTalkDaysFragment();
    lighteningTalkDaysFragment.setArguments(bundle);
    return lighteningTalkDaysFragment;
  }

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    lighteningTalkAdapter = new LighteningTalkAdapter(mContext);
    setHasOptionsMenu(true);

    AnalyticsManager.sendScreenView(SCREEN_LABEL);
  }

  @Override public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {

    View rootView = inflater.inflate(R.layout.fragment_explore_list, container, false);
    ButterKnife.bind(this, rootView);

    exploreSwipeRefreshView =
        (SwipeRefreshLayout) rootView.findViewById(R.id.explore_swipe_refresh_view);

    exploreSwipeRefreshView.setColorSchemeResources(R.color.color1, R.color.color2, R.color.color3,
        R.color.color4);

    exploreSwipeRefreshView.setRefreshing(false);

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

    if (getArguments().getInt("Day") == 1) {
      mTalks = talkDao.getTalkByDay(FIRST_DAY);
    } else if (getArguments().getInt("Day") == 2) {
      mTalks = talkDao.getTalkByDay(SECOND_DAY);
    }

    for (Talk talk : mTalks) {
      if (talk.getTalk_type() == 3) {
        mLighteningTalks.add(talk);
      }
    }

    if (mLighteningTalks.size() > 0) {
      talkRecyclerView.setVisibility(View.VISIBLE);
      emptyTextView.setVisibility(View.GONE);
      lighteningTalkAdapter.replaceWith(mLighteningTalks);
      talkRecyclerView.setAdapter(lighteningTalkAdapter);
    } else {
      setEmptyView();
    }

    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
    linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
    talkRecyclerView.setLayoutManager(linearLayoutManager);

    return rootView;
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.action_refresh:
        if (ConnectionUtils.isOnline(mContext)) {
          syncSchedules(exploreSwipeRefreshView);
        } else {
          hideRefreshProgress(exploreSwipeRefreshView);
          Toast.makeText(mContext, R.string.no_connection_cannot_connect, Toast.LENGTH_SHORT)
              .show();
        }
        return true;

      default:
        return super.onOptionsItemSelected(item);
    }
  }

  @Subscribe public void syncSuccess(SyncSuccessEvent event) {
    if (getArguments().getInt("Day") == 1) {
      mTalks = talkDao.getTalkByDay(FIRST_DAY);
    } else if (getArguments().getInt("Day") == 2) {
      mTalks = talkDao.getTalkByDay(SECOND_DAY);
    }
    for (Talk talk : mTalks) {
      if (talk.getTalk_type() == 3) {
        mLighteningTalks.add(talk);
      }
    }

    if (mLighteningTalks.size() > 0) {
      talkRecyclerView.setVisibility(View.VISIBLE);
      emptyTextView.setVisibility(View.GONE);
      lighteningTalkAdapter.replaceWith(mLighteningTalks);
      talkRecyclerView.setAdapter(lighteningTalkAdapter);
    } else {
      setEmptyView();
    }
  }

  private void setEmptyView() {
    talkRecyclerView.setVisibility(View.GONE);
    emptyTextView.setVisibility(View.VISIBLE);
    emptyTextView.setText(getString(R.string.error_no_lightening_talk_try_again));
    emptyTextView.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        syncSchedules(exploreSwipeRefreshView);
      }
    });
  }
}
