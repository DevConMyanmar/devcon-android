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
import butterknife.InjectView;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import java.sql.SQLException;
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
  @InjectView(R.id.toolbar) Toolbar mToolbar;
  @InjectView(R.id.my_list) ListView mMyList;

  public static TalkChooserFragment getInstance(String start, String end){
    Bundle bundle = new Bundle();
    bundle.putString("START_TIME",start);
    bundle.putString("END_TIME",end);
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
    ButterKnife.inject(this, v);
    mBaseActivity = (BaseActivity)getActivity();
    ScheduleAdapter scheduleAdapter = new ScheduleAdapter(getActivity());
    mBaseActivity.setSupportActionBar(mToolbar);
    ActionBar actionBar = mBaseActivity.getSupportActionBar();
    actionBar.setTitle(getArguments().getString("START_TIME")+"-"+getArguments().getString("END_TIME"));
    actionBar.setDisplayHomeAsUpEnabled(true);
    actionBar.setHomeAsUpIndicator(R.drawable.ic_ab_back_mtrl_am_alpha);

    lists = new ArrayList<Talk>();
    try {
      lists = talkDao.getAll();
    } catch (SQLException e) {
      e.printStackTrace();
    }
    scheduleAdapter.replaceWith(lists);
    mMyList.setAdapter(scheduleAdapter);
    mMyList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        int id = lists.get(i).getId();

        Intent intent = new Intent(getActivity(), TalkDetailActivity.class);
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
}
