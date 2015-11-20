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

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.DisplayMetrics;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.squareup.okhttp.OkHttpClient;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.devconmyanmar.apps.devcon.Config;
import org.devconmyanmar.apps.devcon.db.MyScheduleDao;
import org.devconmyanmar.apps.devcon.db.SpeakerDao;
import org.devconmyanmar.apps.devcon.db.SponsorDao;
import org.devconmyanmar.apps.devcon.db.TalkDao;
import org.devconmyanmar.apps.devcon.event.BusProvider;
import org.devconmyanmar.apps.devcon.event.SyncSuccessEvent;
import org.devconmyanmar.apps.devcon.model.Speaker;
import org.devconmyanmar.apps.devcon.model.Talk;
import org.devconmyanmar.apps.devcon.sync.SyncScheduleService;
import org.devconmyanmar.apps.devcon.sync.SyncSpeakerService;
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
  protected SponsorDao sponsorDao;
  protected Context mContext;
  protected OkHttpClient okHttpClient = new OkHttpClient();

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    this.mContext = getActivity();

    speakerDao = new SpeakerDao(mContext);
    talkDao = new TalkDao(mContext);
    favDao = new MyScheduleDao(mContext);
    sponsorDao = new SponsorDao(mContext);
  }

  public void showRefreshProgress(SwipeRefreshLayout mSwipeRefreshWidget) {
    mSwipeRefreshWidget.setRefreshing(true);
  }

  public void hideRefreshProgress(SwipeRefreshLayout mSwipeRefreshWidget) {
    mSwipeRefreshWidget.setRefreshing(false);
  }

  public void enableSwipe(SwipeRefreshLayout mSwipeRefreshWidget) {
    mSwipeRefreshWidget.setEnabled(true);
  }

  public void disableSwipe(SwipeRefreshLayout mSwipeRefreshWidget) {
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

  protected void syncSchedules(final SwipeRefreshLayout exploreSwipeRefreshView) {

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
        //Toast.makeText(getActivity(), getString(R.string.oops), Toast.LENGTH_SHORT).show();
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

        List<Talk> talks = new ArrayList<Talk>();
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
          talks.add(talk);
          talkDao.create(talk);
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

  public ArrayList<Talk> flattenFav(String talkIds) {
    ArrayList<Talk> mTalks = new ArrayList<Talk>();
    String id[] = new Gson().fromJson(talkIds, String[].class);
    for (String anId : id) {
      mTalks.add(talkDao.getTalkById(Integer.valueOf(anId)));
    }

    return mTalks;
  }

  public int dpToPx(int dp) {
    DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
    return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
  }

  public static boolean isMyanmarText(String str) {
    Pattern normalRange = Pattern.compile("^[\\u1000-\\u109f]");
    Pattern extendedRange = Pattern.compile("^[\\uaa60-\\uaa7f]");
    Matcher normalMatcher = normalRange.matcher(str);
    Matcher extendedMatcher = extendedRange.matcher(str);

    return normalMatcher.find() || extendedMatcher.find();
  }
}
