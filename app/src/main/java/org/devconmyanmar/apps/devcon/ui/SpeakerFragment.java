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
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.squareup.otto.Subscribe;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.devconmyanmar.apps.devcon.R;
import org.devconmyanmar.apps.devcon.adapter.SpeakerAdapter;
import org.devconmyanmar.apps.devcon.event.SyncSuccessEvent;
import org.devconmyanmar.apps.devcon.model.Speaker;
import org.devconmyanmar.apps.devcon.ui.widget.CustomSwipeRefreshLayout;
import org.devconmyanmar.apps.devcon.utils.AnalyticsManager;
import org.devconmyanmar.apps.devcon.utils.ConnectionUtils;
import org.devconmyanmar.apps.devcon.utils.HelpUtils;

import static org.devconmyanmar.apps.devcon.Config.POSITION;
import static org.devconmyanmar.apps.devcon.utils.LogUtils.LOGD;

/**
 * Created by Ye Lin Aung on 14/10/05.
 */
public class SpeakerFragment extends BaseFragment implements SpeakerClickListener {

  private final static String SCREEN_LABEL = "Speaker List";

  @Bind(R.id.speaker_swipe_refresh_view) CustomSwipeRefreshLayout speakerSRView;
  @Bind(R.id.my_list) RecyclerView speakerList;
  @Bind(R.id.toolbar) Toolbar mToolbar;

  private List<Speaker> mSpeakers = new ArrayList<Speaker>();
  private BaseActivity mActivity;

  public SpeakerFragment() {
  }

  public static SpeakerFragment getInstance() {
    return new SpeakerFragment();
  }

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mActivity = (BaseActivity) getActivity();
    setHasOptionsMenu(true);

    AnalyticsManager.sendScreenView(SCREEN_LABEL);
  }

  @Override public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View rootView = inflater.inflate(R.layout.fragment_speaker, container, false);
    ButterKnife.bind(this, rootView);
    mActivity.setSupportActionBar(mToolbar);
    ActionBar actionBar = mActivity.getSupportActionBar();
    if (actionBar != null) {
      actionBar.setDisplayHomeAsUpEnabled(true);
      actionBar.setHomeAsUpIndicator(R.drawable.ic_drawer);
      actionBar.setTitle(R.string.speakers);
    }

    speakerSRView.setColorSchemeResources(R.color.color1, R.color.color2, R.color.color3,
        R.color.color4);

    speakerSRView.setRefreshing(false);

    speakerSRView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
      @Override public void onRefresh() {
        if (ConnectionUtils.isOnline(mContext)) {
          syncSchedules(speakerSRView);
        } else {
          hideRefreshProgress(speakerSRView);
          Toast.makeText(mContext, R.string.no_connection_cannot_connect, Toast.LENGTH_SHORT)
              .show();
        }
      }
    });

    try {
      mSpeakers = speakerDao.getAll();
      SpeakerAdapter speakerAdapter = new SpeakerAdapter(mContext, this);
      speakerAdapter.replaceWith(mSpeakers);
      speakerList.setAdapter(speakerAdapter);
    } catch (SQLException e) {
      e.printStackTrace();
    }

    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
    linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
    speakerList.setLayoutManager(linearLayoutManager);

    return rootView;
  }

  @Override public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    super.onCreateOptionsMenu(menu, inflater);
    inflater.inflate(R.menu.schedule_menu, menu);
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.action_about:
        HelpUtils.showAbout(mActivity);
        return true;
      case android.R.id.home:
        mActivity.finish();
        return true;
      default:
        return super.onOptionsItemSelected(item);
    }
  }

  @Subscribe public void syncSuccess(SyncSuccessEvent event) {
    try {
      mSpeakers = speakerDao.getAll();
      SpeakerAdapter speakerAdapter = new SpeakerAdapter(mContext, this);
      speakerAdapter.replaceWith(mSpeakers);
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  @Override public void onSpeakerClick(Speaker speaker, View v, int position) {
    int id = mSpeakers.get(position).getId();
    LOGD("speakers ", "id " + id);
    Intent i = new Intent(getActivity(), SpeakerDetailActivity.class);

    AnalyticsManager.sendEvent("Speaker List", "selectspeaker", mSpeakers.get(position).getTitle());

    i.putExtra(POSITION, id);
    startActivity(i);
  }
}
