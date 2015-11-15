
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
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import butterknife.ButterKnife;
import butterknife.Bind;
import com.google.gson.Gson;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import java.util.ArrayList;
import java.util.List;
import org.devconmyanmar.apps.devcon.R;
import org.devconmyanmar.apps.devcon.adapter.ScheduleAdapter;
import org.devconmyanmar.apps.devcon.model.Talk;
import org.devconmyanmar.apps.devcon.utils.HelpUtils;

import static org.devconmyanmar.apps.devcon.Config.POSITION;

/**
 * Created by yemyatthu on 11/13/14.
 */
public class TalkChooserFragment extends BaseFragment {
  private List<Talk> lists;
  private BaseActivity mBaseActivity;
  @Bind(R.id.toolbar) Toolbar mToolbar;
  @Bind(R.id.my_list) ListView mMyList;

  public static TalkChooserFragment getInstance(String start, String end,String talkId){
    Bundle bundle = new Bundle();
    bundle.putString("START_TIME",start);
    bundle.putString("END_TIME",end);
    bundle.putString("TALK_ID",talkId);
    TalkChooserFragment talkChooserFragment = new TalkChooserFragment();
    talkChooserFragment.setArguments(bundle);
    return talkChooserFragment;
  }

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setHasOptionsMenu(true);
  }

  @Override public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View v = inflater.inflate(R.layout.fragment_speaker, container, false);
    ButterKnife.bind(this, v);
    mBaseActivity = (BaseActivity)getActivity();
    ScheduleAdapter scheduleAdapter = new ScheduleAdapter(getActivity());
    mBaseActivity.setSupportActionBar(mToolbar);
    ActionBar actionBar = mBaseActivity.getSupportActionBar();
    actionBar.setTitle(getArguments().getString("START_TIME")+"-"+getArguments().getString("END_TIME"));
    actionBar.setDisplayHomeAsUpEnabled(true);

    lists = flattenTalks(getArguments().getString("TALK_ID"));

    scheduleAdapter.replaceWith(lists);
    mMyList.setAdapter(scheduleAdapter);
    mMyList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        int id = lists.get(i).getId();

        Intent intent = new Intent(getActivity(), TalkChooserDetailActivity.class);
        intent.putExtra(POSITION, id);
        startActivity(intent);
      }
    });

    if (Build.VERSION.SDK_INT >= 19) {

      SystemBarTintManager tintManager = new SystemBarTintManager(getActivity());
      tintManager.setStatusBarTintEnabled(true);
      tintManager.setNavigationBarTintEnabled(false);
      tintManager.setTintColor(getResources().getColor(R.color.translucent_actionbar_background));
    }
    return v;
  }
  @Override public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    super.onCreateOptionsMenu(menu, inflater);
    inflater.inflate(R.menu.schedule_menu, menu);
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.action_about:
        HelpUtils.showAbout(mBaseActivity);
        return true;
      case android.R.id.home:
        mBaseActivity.finish();
        return true;
      default:
        return super.onOptionsItemSelected(item);
    }
  }
  private ArrayList<Talk> flattenTalks(String talks) {
    ArrayList<Talk> mTalks = new ArrayList<Talk>();
    String id[] = new Gson().fromJson(talks, String[].class);
    for (String anId : id) {
      mTalks.add(talkDao.getTalkById(Integer.valueOf(anId)));
    }

    return mTalks;
  }

}
