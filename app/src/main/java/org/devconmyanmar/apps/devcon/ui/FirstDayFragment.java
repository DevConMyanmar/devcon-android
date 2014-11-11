package org.devconmyanmar.apps.devcon.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.devconmyanmar.apps.devcon.Config;
import org.devconmyanmar.apps.devcon.R;
import org.devconmyanmar.apps.devcon.adapter.ScheduleAdapter;
import org.devconmyanmar.apps.devcon.model.Talk;
import org.devconmyanmar.apps.devcon.sync.SyncScheduleService;
import org.devconmyanmar.apps.devcon.ui.widget.CustomSwipeRefreshLayout;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;
import retrofit.client.Response;
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
  private CustomSwipeRefreshLayout exploreSwipeRefreshView;

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

    exploreSwipeRefreshView =
        (CustomSwipeRefreshLayout) rootView.findViewById(R.id.explore_swipe_refresh_view);

    exploreSwipeRefreshView.setColorSchemeResources(R.color.color1, R.color.color2, R.color.color3,
        R.color.color4);

    exploreSwipeRefreshView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
      @Override public void onRefresh() {
        syncSchedules();
      }
    });

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

  private void syncSchedules() {
    showRefreshProgress(exploreSwipeRefreshView);

    RestAdapter scheduleRestAdapter = new RestAdapter.Builder().setEndpoint(Config.BASE_URL)
        .setLogLevel(RestAdapter.LogLevel.BASIC)
        .setClient(new OkClient(okHttpClient))
        .build();

    SyncScheduleService syncScheduleService = scheduleRestAdapter.create(SyncScheduleService.class);
    syncScheduleService.getSchedules(new Callback<JsonObject>() {
      @Override public void success(JsonObject jsonObj, Response response) {
        JsonArray scheduleArray = jsonObj.getAsJsonArray("sessions");
        try {
          talkDao.deleteAll();
        } catch (SQLException e) {
          e.printStackTrace();
        }
        for (JsonElement j : scheduleArray) {
          Talk talk = new Talk();
          talk.setId(j.getAsJsonObject().get("id").getAsInt());
          talk.setTitle(j.getAsJsonObject().get("title").getAsString());
          talk.setDescription(j.getAsJsonObject().get("description").getAsString());
          talk.setPhoto(j.getAsJsonObject().get("photo").getAsString());
          talk.setDate(j.getAsJsonObject().get("date").getAsString());
          talk.setFavourite(j.getAsJsonObject().get("favourite").getAsBoolean());
          talk.setTalk_type(j.getAsJsonObject().get("talk_type").getAsInt());
          talk.setRoom(j.getAsJsonObject().get("room").getAsString());
          talk.setFrom_time(j.getAsJsonObject().get("from_time").getAsString());
          talk.setTo_time(j.getAsJsonObject().get("to_time").getAsString());
          JsonArray speakers = j.getAsJsonObject().getAsJsonArray("speakers");
          talk.setSpeakers(speakers.toString());
          talkDao.create(talk);
        }
        hideRefreshProgress(exploreSwipeRefreshView);
      }

      @Override public void failure(RetrofitError error) {
        hideRefreshProgress(exploreSwipeRefreshView);
      }
    });
  }
}
