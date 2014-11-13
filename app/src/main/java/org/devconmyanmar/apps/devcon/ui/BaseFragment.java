package org.devconmyanmar.apps.devcon.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.widget.Toast;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.squareup.okhttp.OkHttpClient;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.devconmyanmar.apps.devcon.Config;
import org.devconmyanmar.apps.devcon.R;
import org.devconmyanmar.apps.devcon.db.MyScheduleDao;
import org.devconmyanmar.apps.devcon.db.SpeakerDao;
import org.devconmyanmar.apps.devcon.db.TalkDao;
import org.devconmyanmar.apps.devcon.event.BusProvider;
import org.devconmyanmar.apps.devcon.event.SyncSuccessEvent;
import org.devconmyanmar.apps.devcon.model.Speaker;
import org.devconmyanmar.apps.devcon.model.Talk;
import org.devconmyanmar.apps.devcon.sync.SyncScheduleService;
import org.devconmyanmar.apps.devcon.sync.SyncSpeakerService;
import org.devconmyanmar.apps.devcon.ui.widget.CustomSwipeRefreshLayout;
import org.devconmyanmar.apps.devcon.utils.SharePref;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;
import retrofit.client.Response;

import static org.devconmyanmar.apps.devcon.utils.LogUtils.makeLogTag;

/**
 * Created by Ye Lin Aung on 14/10/05.
 */
public abstract class BaseFragment extends Fragment {

  private static final String TAG = makeLogTag(BaseFragment.class);
  protected SpeakerDao speakerDao;
  protected TalkDao talkDao;
  protected MyScheduleDao favDao;
  protected Context mContext;
  protected OkHttpClient okHttpClient = new OkHttpClient();

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    this.mContext = getActivity();

    speakerDao = new SpeakerDao(mContext);
    talkDao = new TalkDao(mContext);
    favDao = new MyScheduleDao(mContext);
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

    RestAdapter speakerRestAdapter = new RestAdapter.Builder().setEndpoint(Config.BASE_URL)
        .setLogLevel(RestAdapter.LogLevel.BASIC)
        .setClient(new OkClient(okHttpClient))
        .build();

    SyncSpeakerService syncSpeakerService = speakerRestAdapter.create(SyncSpeakerService.class);
    syncSpeakerService.getSpeakers(new Callback<List<Speaker>>() {
      @Override public void success(List<Speaker> speakers, Response response) {
        try {
          speakerDao.deleteAll();
          for (Speaker s : speakers) {
            speakerDao.create(s);
          }
        } catch (SQLException e) {
          e.printStackTrace();
        }
      }

      @Override public void failure(RetrofitError error) {
        Toast.makeText(getActivity(), getString(R.string.oops), Toast.LENGTH_SHORT).show();
        hideRefreshProgress(exploreSwipeRefreshView);
      }
    });

    List<Talk> favTalk = talkDao.getFavTalks();
    ArrayList<Integer> talkIds = new ArrayList<Integer>();
    for (Talk talk : favTalk) {
      talkIds.add(talk.getId());
    }
    SharePref.getInstance(mContext).saveFavIds(talkIds.toString());

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
        List<Talk> talks = new ArrayList<Talk>();
        try {
          talks = talkDao.getAll();
        } catch (SQLException e) {
          e.printStackTrace();
        }
        ArrayList<Talk> favTalk = flattenFav(SharePref.getInstance(mContext).geFavIds());
        ArrayList<Integer> talkIds = new ArrayList<Integer>();
        for (Talk talk : favTalk) {
          talkIds.add(talk.getId());
        }

        for (Talk talk : talks) {
          if (talkIds.contains(talk.getId())) {
            talk.setFavourite(true);
            talk.setId(talk.getId());
            talkDao.createOrUpdate(talk);
          }
        }
        hideRefreshProgress(exploreSwipeRefreshView);
        BusProvider.getInstance().post(new SyncSuccessEvent());
      }

      @Override public void failure(RetrofitError error) {
        hideRefreshProgress(exploreSwipeRefreshView);
      }
    });
  }

  private ArrayList<Talk> flattenFav(String talkIds) {
    ArrayList<Talk> mTalks = new ArrayList<Talk>();
    String id[] = new Gson().fromJson(talkIds, String[].class);
    for (String anId : id) {
      mTalks.add(talkDao.getTalkById(Integer.valueOf(anId)));
    }

    return mTalks;
  }
}
