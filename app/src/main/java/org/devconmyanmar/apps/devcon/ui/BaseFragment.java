package org.devconmyanmar.apps.devcon.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.squareup.okhttp.OkHttpClient;
import java.sql.SQLException;
import org.devconmyanmar.apps.devcon.Config;
import org.devconmyanmar.apps.devcon.db.SpeakerDao;
import org.devconmyanmar.apps.devcon.db.TalkDao;
import org.devconmyanmar.apps.devcon.event.BusProvider;
import org.devconmyanmar.apps.devcon.event.SyncSuccessEvent;
import org.devconmyanmar.apps.devcon.model.Talk;
import org.devconmyanmar.apps.devcon.sync.SyncScheduleService;
import org.devconmyanmar.apps.devcon.ui.widget.CustomSwipeRefreshLayout;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;
import retrofit.client.Response;

/**
 * Created by Ye Lin Aung on 14/10/05.
 */
public abstract class BaseFragment extends Fragment {

  protected SpeakerDao speakerDao;
  protected TalkDao talkDao;
  protected Context mContext;
  protected OkHttpClient okHttpClient = new OkHttpClient();

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    this.mContext = getActivity();

    speakerDao = new SpeakerDao(mContext);
    talkDao = new TalkDao(mContext);
  }

  public void showRefreshProgress(CustomSwipeRefreshLayout mSwipeRefreshWidget) {
    mSwipeRefreshWidget.setRefreshing(true);
  }

  public void hideRefreshProgress(CustomSwipeRefreshLayout mSwipeRefreshWidget) {
    mSwipeRefreshWidget.setRefreshing(false);
  }

  public void enableSwipe(CustomSwipeRefreshLayout mSwipeRefreshWidget) {
    mSwipeRefreshWidget.setEnabled(true);
  }

  public void disableSwipe(CustomSwipeRefreshLayout mSwipeRefreshWidget) {
    mSwipeRefreshWidget.setEnabled(false);
  }

  @Override public void onResume() {
    super.onResume();
    BusProvider.getInstance().register(this);
  }

  @Override public void onPause() {
    super.onPause();
    BusProvider.getInstance().unregister(this);
  }

  protected void syncSchedules(final CustomSwipeRefreshLayout exploreSwipeRefreshView) {
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
        BusProvider.getInstance().post(new SyncSuccessEvent());
      }

      @Override public void failure(RetrofitError error) {
        hideRefreshProgress(exploreSwipeRefreshView);
      }
    });
  }
}
