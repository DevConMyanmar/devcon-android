package org.devconmyanmar.apps.devcon.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import butterknife.ButterKnife;
import butterknife.InjectView;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.devconmyanmar.apps.devcon.R;
import org.devconmyanmar.apps.devcon.adapter.MyPagerAdapter;
import org.devconmyanmar.apps.devcon.model.Speaker;
import org.devconmyanmar.apps.devcon.transformer.StackTransformer;

import static org.devconmyanmar.apps.devcon.Config.POSITION;

public class SpeakerDetailActivity extends BaseActivity {

  @InjectView(R.id.speaker_pager) ViewPager mSpeakerViewPager;
  @InjectView(R.id.toolbar) Toolbar mToolbar;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_speaker_detail);

    ButterKnife.inject(this);
    mToolbar.setTitle(R.string.speaker_profile);
    mToolbar.setTitleTextColor(getResources().getColor(android.R.color.white));

    //Inflate Menu
    mToolbar.inflateMenu(R.menu.schedule_menu);

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
}
