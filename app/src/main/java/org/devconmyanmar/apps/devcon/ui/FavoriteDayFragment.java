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

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import butterknife.ButterKnife;
import butterknife.Bind;
import butterknife.OnItemClick;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.devconmyanmar.apps.devcon.Config;
import org.devconmyanmar.apps.devcon.R;
import org.devconmyanmar.apps.devcon.adapter.MyScheduleAdapter;
import org.devconmyanmar.apps.devcon.db.MyScheduleDao;
import org.devconmyanmar.apps.devcon.model.MySchedule;

import static org.devconmyanmar.apps.devcon.utils.LogUtils.LOGD;
import static org.devconmyanmar.apps.devcon.utils.LogUtils.makeLogTag;

/**
 * Created by yemyatthu on 11/12/14.
 */
public class FavoriteDayFragment extends BaseFragment {
  private static final String POSITION_ARGS = "position args";
  private static final String TAG = makeLogTag(FavoriteDayFragment.class);

  @Bind(R.id.favorite_list) ListView mFavoriteList;
  @Bind(R.id.loading_progress_bar) ProgressBar mLoadingProgress;

  private List<MySchedule> mMySchedules = new ArrayList<MySchedule>();
  private MyScheduleDao mMyScheduleDao;
  private MyScheduleAdapter myScheduleAdapter;

  private LazyLoadData lazyLoadTask;

  public FavoriteDayFragment() {
  }

  public static FavoriteDayFragment getInstance(int position) {
    Bundle bundle = new Bundle();
    bundle.putInt(POSITION_ARGS, position);
    FavoriteDayFragment favoriteDayFragment = new FavoriteDayFragment();
    favoriteDayFragment.setArguments(bundle);
    return favoriteDayFragment;
  }

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mMyScheduleDao = new MyScheduleDao(mContext);
    myScheduleAdapter = new MyScheduleAdapter(mContext);

    LOGD(TAG, "I am on onCreate");
    lazyLoadTask = new LazyLoadData(getArguments());
  }

  @Override public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View v = inflater.inflate(R.layout.fragment_favorite, container, false);
    ButterKnife.bind(this, v);

    LOGD(TAG, "I am on onCreateView");
    if (!(lazyLoadTask.getStatus() == AsyncTask.Status.RUNNING)) {
      lazyLoadTask.execute();
    }

    return v;
  }

  @Override public void onPause() {
    super.onPause();
    if (lazyLoadTask != null) {
      lazyLoadTask.cancel(true);
    }
  }

  @Override public void onResume() {
    super.onResume();
  }

  @SuppressWarnings("unused") @OnItemClick(R.id.favorite_list) void onItemClick(int position) {
    MySchedule mySchedule = (MySchedule) myScheduleAdapter.getItem(position);
    if (!mySchedule.isClickBlock()) {
      Intent intent = new Intent(mContext, TalkChooserActivity.class);
      intent.putExtra("START_TIME", mySchedule.getStart());
      intent.putExtra("END_TIME", mySchedule.getEnd());
      intent.putExtra("TALK_ID", mySchedule.getAssociatedTalkId());
      mContext.startActivity(intent);
    }

    if (mySchedule.isHasFavorite()) {
      if (mySchedule.getFavoriteTalkId() != 0) {
        Intent intent = new Intent(mContext, TalkChooserDetailActivity.class);
        intent.putExtra(Config.POSITION, mySchedule.getFavoriteTalkId());
        mContext.startActivity(intent);
      }
    }
  }

  private class LazyLoadData extends AsyncTask<Void, Void, MyScheduleAdapter> {

    private Bundle arg;

    private LazyLoadData(Bundle arg) {
      this.arg = arg;
    }

    @Override protected void onPreExecute() {
      super.onPreExecute();
      showProgress(true, mFavoriteList, mLoadingProgress);
    }

    @Override protected MyScheduleAdapter doInBackground(Void... voids) {

      MyScheduleAdapter adapter = new MyScheduleAdapter(mContext);
      try {
        mMySchedules = mMyScheduleDao.getAll();
      } catch (SQLException e) {
        e.printStackTrace();
      }

      ArrayList<MySchedule> firstDay = new ArrayList<MySchedule>();
      ArrayList<MySchedule> secondDay = new ArrayList<MySchedule>();
      for (MySchedule mySchedule : mMySchedules) {
        if (mySchedule.getDate() == 0) {
          firstDay.add(mySchedule);
        }
        if (mySchedule.getDate() == 1) {
          secondDay.add(mySchedule);
        }
      }

      if (arg.getInt(POSITION_ARGS) == 0) {
        adapter.replaceWith(firstDay);
      }
      if (arg.getInt(POSITION_ARGS) == 1) {
        adapter.replaceWith(secondDay);
      }

      return adapter;
    }

    @Override protected void onPostExecute(MyScheduleAdapter adapter) {
      super.onPostExecute(adapter);
      showProgress(false, mFavoriteList, mLoadingProgress);
      myScheduleAdapter = adapter;
      mFavoriteList.setAdapter(adapter);
    }

    private void showProgress(final boolean show, final View listView, final View progressView) {
      // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
      // for very easy animations. If available, use these APIs to fade-in
      // the progress spinner.
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

        listView.setVisibility(show ? View.GONE : View.VISIBLE);
        listView.animate()
            .setDuration(shortAnimTime)
            .alpha(show ? 0 : 1)
            .setListener(new AnimatorListenerAdapter() {
              @Override
              public void onAnimationEnd(Animator animation) {
                listView.setVisibility(show ? View.GONE : View.VISIBLE);
              }
            });

        progressView.setVisibility(show ? View.VISIBLE : View.GONE);
        progressView.animate()
            .setDuration(shortAnimTime)
            .alpha(show ? 1 : 0)
            .setListener(new AnimatorListenerAdapter() {
              @Override
              public void onAnimationEnd(Animator animation) {
                progressView.setVisibility(show ? View.VISIBLE : View.GONE);
              }
            });
      } else {
        // The ViewPropertyAnimator APIs are not available, so simply show
        // and hide the relevant UI components.
        progressView.setVisibility(show ? View.VISIBLE : View.GONE);
        listView.setVisibility(show ? View.GONE : View.VISIBLE);
      }
    }
  }
}

