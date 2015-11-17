/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014 Devcon Contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package org.devconmyanmar.apps.devcon.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.squareup.otto.Subscribe;
import java.util.ArrayList;
import java.util.List;
import org.devconmyanmar.apps.devcon.Config;
import org.devconmyanmar.apps.devcon.R;
import org.devconmyanmar.apps.devcon.adapter.ScheduleAdapter;
import org.devconmyanmar.apps.devcon.event.SyncSuccessEvent;
import org.devconmyanmar.apps.devcon.model.Talk;
import org.devconmyanmar.apps.devcon.utils.AnalyticsManager;
import org.devconmyanmar.apps.devcon.utils.ConnectionUtils;

import static org.devconmyanmar.apps.devcon.utils.LogUtils.makeLogTag;

/**
 * Created by Ye Lin Aung on 14/10/05.
 */
public class SecondDayFragment extends BaseFragment implements TalkClickListener {

  private static final String TAG = makeLogTag(SecondDayFragment.class);
  private static final String SECOND_DAY = "2015-11-22";

  private final static String SCREEN_LABEL = "Explore Second Day";

  @Bind(R.id.explore_swipe_refresh_view) SwipeRefreshLayout exploreSwipeRefreshView;
  @Bind(R.id.explore_list_view) RecyclerView secondDayList;
  private ScheduleAdapter mScheduleAdapter;

  private List<Talk> mTalks = new ArrayList<Talk>();

  public SecondDayFragment() {
  }

  public static SecondDayFragment getInstance() {
    return new SecondDayFragment();
  }

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mScheduleAdapter = new ScheduleAdapter(mContext, this);
    AnalyticsManager.sendScreenView(SCREEN_LABEL);
  }

  @Override public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View rootView = inflater.inflate(R.layout.fragment_explore_list, container, false);
    ButterKnife.bind(this, rootView);

    exploreSwipeRefreshView.setColorSchemeResources(R.color.color1, R.color.color2, R.color.color3,
        R.color.color4);

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

    mTalks = talkDao.getTalkByDay(SECOND_DAY);
    ArrayList<Talk> tempTalks = new ArrayList<Talk>();
    for (Talk talk : mTalks) {
      if (talk.getTalk_type() == 3) {
        tempTalks.add(talk);
      }
    }
    mTalks.removeAll(tempTalks);
    mScheduleAdapter.replaceWith(mTalks);

    secondDayList.setAdapter(mScheduleAdapter);

    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
    linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
    secondDayList.setLayoutManager(linearLayoutManager);

    return rootView;
  }

  @Subscribe public void syncSuccess(SyncSuccessEvent event) {
    mTalks = talkDao.getTalkByDay(SECOND_DAY);
    mScheduleAdapter.replaceWith(mTalks);
    secondDayList.setAdapter(mScheduleAdapter);
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

  @Override public void onTalkClick(Talk talk, View v, int position) {
    int id = mTalks.get(position).getId();
    Intent i = new Intent(getActivity(), TalkDetailActivity.class);
    AnalyticsManager.sendEvent("Explore Second Day", "selecttalk", mTalks.get(position).getTitle());

    i.putExtra(Config.POSITION, id);
    startActivity(i);
  }
}
