package org.devconmyanmar.apps.devcon.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.Toast;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnItemClick;
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

/**
 * Created by Ye Lin Aung on 14/10/05.
 */
public class SpeakerFragment extends BaseFragment {

  private final static String SCREEN_LABEL = "Speaker List";

  @InjectView(R.id.speaker_swipe_refresh_view) CustomSwipeRefreshLayout speakerSRView;
  @InjectView(R.id.my_list) ListView speakerList;
  @InjectView(R.id.toolbar) Toolbar mToolbar;

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
    ButterKnife.inject(this, rootView);
    mActivity.setSupportActionBar(mToolbar);
    ActionBar actionBar = mActivity.getSupportActionBar();
    actionBar.setDisplayHomeAsUpEnabled(true);
    actionBar.setHomeAsUpIndicator(R.drawable.ic_drawer);
    actionBar.setTitle(R.string.speakers);

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
      SpeakerAdapter speakerAdapter = new SpeakerAdapter(mContext);
      speakerAdapter.replaceWith(mSpeakers);
      speakerList.setAdapter(speakerAdapter);
    } catch (SQLException e) {
      e.printStackTrace();
    }

    speakerList.setOnScrollListener(new AbsListView.OnScrollListener() {
      @Override
      public void onScrollStateChanged(AbsListView view, int scrollState) {
      }

      @Override
      public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
          int totalItemCount) {
        if (firstVisibleItem == 0) {
          speakerSRView.setEnabled(true);
        } else {
          speakerSRView.setEnabled(false);
        }
      }
    });

    return rootView;
  }

  @SuppressWarnings("unused") @OnItemClick(R.id.my_list) void speakerListItemClick(int position) {
    int id = mSpeakers.get(position).getId();
    Intent i = new Intent(getActivity(), SpeakerDetailActivity.class);

    AnalyticsManager.sendEvent("Speaker List", "selectspeaker", mSpeakers.get(position).getTitle());

    i.putExtra(POSITION, id);
    startActivity(i);
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
      SpeakerAdapter speakerAdapter = new SpeakerAdapter(mContext);
      speakerAdapter.replaceWith(mSpeakers);
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
}
