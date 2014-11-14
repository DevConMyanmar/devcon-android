
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
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import butterknife.ButterKnife;
import butterknife.InjectView;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.devconmyanmar.apps.devcon.R;
import org.devconmyanmar.apps.devcon.adapter.MyPagerAdapter;
import org.devconmyanmar.apps.devcon.model.Speaker;
import org.devconmyanmar.apps.devcon.transformer.StackTransformer;
import org.devconmyanmar.apps.devcon.utils.HelpUtils;

import static org.devconmyanmar.apps.devcon.Config.POSITION;

public class SpeakerDetailActivity extends BaseActivity {

  @InjectView(R.id.speaker_pager) ViewPager mSpeakerViewPager;
  @InjectView(R.id.toolbar) Toolbar mToolbar;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_speaker_detail);

    ButterKnife.inject(this);
    setSupportActionBar(mToolbar);
    ActionBar actionBar = getSupportActionBar();
    actionBar.setTitle(R.string.speaker_profile);
    actionBar.setHomeAsUpIndicator(R.drawable.ic_ab_back_mtrl_am_alpha);
    actionBar.setDisplayHomeAsUpEnabled(true);

    Intent intent = getIntent();
    int position = intent.getIntExtra(POSITION, 0) - 1;

    List<Fragment> fragments = getTalkFragments();
    MyPagerAdapter mAdapter = new MyPagerAdapter(getSupportFragmentManager(), fragments);

    mSpeakerViewPager.setPageTransformer(true, new StackTransformer());
    mSpeakerViewPager.setAdapter(mAdapter);
    mSpeakerViewPager.setCurrentItem(position);
  }

  private List<Fragment> getTalkFragments() {
    List<Fragment> fList = new ArrayList<Fragment>();

    try {
      List<Speaker> speakers = speakerDao.getAll();
      for (Speaker speaker : speakers) {
        int speakerId = speaker.getId();
        fList.add(SpeakerDetailFragment.newInstance(String.valueOf(speakerId)));
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }

    return fList;
  }

  @Override public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.schedule_menu,menu);
    return super.onCreateOptionsMenu(menu);
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()){
      case R.id.action_about:
        HelpUtils.showAbout(this);
        return true;
      case android.R.id.home:
        finish();
        return true;
      default:
        return super.onOptionsItemSelected(item);
    }
  }
}
