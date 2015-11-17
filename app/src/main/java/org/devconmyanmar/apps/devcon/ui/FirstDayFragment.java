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
import java.util.ArrayList;
import java.util.List;
import org.devconmyanmar.apps.devcon.Config;
import org.devconmyanmar.apps.devcon.R;
import org.devconmyanmar.apps.devcon.adapter.ScheduleAdapter;
import org.devconmyanmar.apps.devcon.adapter.SectionAdapter;
import org.devconmyanmar.apps.devcon.model.Talk;
import org.devconmyanmar.apps.devcon.utils.AnalyticsManager;
import org.devconmyanmar.apps.devcon.utils.ConnectionUtils;

import static org.devconmyanmar.apps.devcon.utils.LogUtils.LOGD;
import static org.devconmyanmar.apps.devcon.utils.LogUtils.makeLogTag;

/**
 * Created by Ye Lin Aung on 14/10/05.
 */
public class FirstDayFragment extends BaseFragment implements TalkClickListener {

  private static final String TAG = makeLogTag(FirstDayFragment.class);
  private static final String FIRST_DAY = "2015-11-21";
  private final static String SCREEN_LABEL = "Explore First Day";
  private List<Talk> mTalks = new ArrayList<>();
  private ScheduleAdapter mScheduleAdapter;

  private SectionAdapter mSectionedAdapter;

  @Bind(R.id.explore_swipe_refresh_view) SwipeRefreshLayout exploreSwipeRefreshView;
  @Bind(R.id.explore_list_view) RecyclerView firstDayList;

  public FirstDayFragment() {
  }

  public static FirstDayFragment getInstance() {
    return new FirstDayFragment();
  }

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mScheduleAdapter = new ScheduleAdapter(mContext, this);
    setHasOptionsMenu(true);

    AnalyticsManager.sendScreenView(SCREEN_LABEL);
  }

  @Override public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View rootView = inflater.inflate(R.layout.fragment_explore_list, container, false);
    ButterKnife.bind(this, rootView);

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

    mTalks = talkDao.getTalkByDay(FIRST_DAY);
    ArrayList<Talk> tempTalks = new ArrayList<Talk>();
    for (Talk talk : mTalks) {
      if (talk.getTalk_type() == 3) {
        tempTalks.add(talk);
      }
    }

    mTalks.removeAll(tempTalks);

    LOGD(TAG, "mTalks -> " + mTalks.size());
    mScheduleAdapter.replaceWith(mTalks);

    //List<SectionAdapter.Section> sections = new ArrayList<>();
    //for (int i = 0; i < mTalks.size(); i++) {
    //  String roomType = mTalks.get(i).getRoom();
    //  if (sections.size() > 0) {
    //    if (!checkSection(sections, translateRoomType(roomType))) {
    //      sections.add(new SectionAdapter.Section(i, translateRoomType(roomType)));
    //    }
    //  } else {
    //    sections.add(new SectionAdapter.Section(0, translateRoomType(roomType)));
    //  }
    //}
    //
    //SectionAdapter.Section[] dummy = new SectionAdapter.Section[sections.size()];
    //mSectionedAdapter =
    //    new SectionAdapter(mContext, R.layout.room_header, R.id.room_name, mScheduleAdapter);
    //mSectionedAdapter.setSections(sections.toArray(dummy));
    //firstDayList.setAdapter(mSectionedAdapter);

    firstDayList.setAdapter(mScheduleAdapter);

    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
    linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
    firstDayList.setLayoutManager(linearLayoutManager);

    //firstDayList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
    // @Override public void onItemClick(AdapterView<?> adapterView, View view,
    // int position, long l) {
    //    int id = mTalks.get(position).getId();
    //    LOGD(TAG, "Talk Type -> " + mTalks.get(position).getTalk_type());
    //
    //    // GA
    //    AnalyticsManager.sendEvent("Explore First Day", "selecttalk",
    //        mTalks.get(position).getTitle());
    //
    //    Intent i = new Intent(getActivity(), TalkDetailActivity.class);
    //    i.putExtra(POSITION, id);
    //    startActivity(i);
    //  }
    //});

    return rootView;
  }

  private boolean checkSection(List<SectionAdapter.Section> sections, String s) {
    for (SectionAdapter.Section section : sections) {
      if (s.equalsIgnoreCase(section.getTitle().toString())) return true;
    }
    return false;
  }

  private String translateRoomType(String roomType) {
    switch (roomType) {
      case "conference":
        return "Conference Room";
      case "205":
        return "Room 205";
      case "102":
        return "Room 102";
      case "mcf":
        return "MCF Meeting room";
      default:
        return "Room 205";
    }
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

  @Override public void onTalkClick(View v, int position) {
    int id = mTalks.get(position).getId();
    LOGD(TAG, "Talk Type -> " + mTalks.get(position).getTalk_type());
    Intent i = new Intent(getActivity(), TalkDetailActivity.class);
    i.putExtra(Config.POSITION, id);
    startActivity(i);

    // GA
    AnalyticsManager.sendEvent("Explore First Day", "selecttalk", mTalks.get(position).getTitle());
  }
}
