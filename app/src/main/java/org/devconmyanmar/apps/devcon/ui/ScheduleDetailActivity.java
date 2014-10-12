package org.devconmyanmar.apps.devcon.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import butterknife.ButterKnife;
import butterknife.InjectView;
import io.realm.Realm;
import io.realm.RealmQuery;
import java.util.ArrayList;
import java.util.List;
import org.devconmyanmar.apps.devcon.R;
import org.devconmyanmar.apps.devcon.model.Talk;
import org.devconmyanmar.apps.devcon.transformer.StackTransformer;

import static org.devconmyanmar.apps.devcon.Config.POSITION;

public class ScheduleDetailActivity extends BaseActivity {

  @InjectView(R.id.schedule_pager) ViewPager mScheduleViewPager;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_schedule_detail);
    ButterKnife.inject(this);

    Intent intent = getIntent();
    int position = intent.getIntExtra(POSITION, 0);

    List<Fragment> fragments = getTalkFragments();
    SchedulePagerAdapter mAdapter =
        new SchedulePagerAdapter(getSupportFragmentManager(), fragments);

    mScheduleViewPager.setPageTransformer(true, new StackTransformer());
    mScheduleViewPager.setAdapter(mAdapter);
    mScheduleViewPager.setCurrentItem(position);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu_schedule_detail, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();

    //noinspection SimplifiableIfStatement
    if (id == R.id.action_settings) {
      return true;
    }

    return super.onOptionsItemSelected(item);
  }

  private List<Fragment> getTalkFragments() {
    List<Fragment> fList = new ArrayList<Fragment>();
    Realm realm = Realm.getInstance(this);
    RealmQuery<Talk> query = realm.where(Talk.class);
    List<Talk> talks = query.findAll();

    for (Talk talk : talks) {
      int talkId = talk.getId();
      fList.add(ScheduleDetailFragment.newInstance(String.valueOf(talkId)));
    }

    return fList;
  }

  public class SchedulePagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> fragments;

    public SchedulePagerAdapter(FragmentManager fm, List<Fragment> fragments) {
      super(fm);
      this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
      return this.fragments.get(position);
    }

    @Override
    public int getCount() {
      return fragments.size();
    }
  }
}
