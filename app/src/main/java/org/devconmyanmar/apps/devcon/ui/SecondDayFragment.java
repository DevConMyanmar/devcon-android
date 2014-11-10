package org.devconmyanmar.apps.devcon.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnItemClick;
import java.util.ArrayList;
import java.util.List;
import org.devconmyanmar.apps.devcon.R;
import org.devconmyanmar.apps.devcon.adapter.ScheduleAdapter;
import org.devconmyanmar.apps.devcon.model.Talk;

import static org.devconmyanmar.apps.devcon.Config.POSITION;
import static org.devconmyanmar.apps.devcon.utils.LogUtils.LOGD;
import static org.devconmyanmar.apps.devcon.utils.LogUtils.makeLogTag;

/**
 * Created by Ye Lin Aung on 14/10/05.
 */
public class SecondDayFragment extends BaseFragment {

  private static final String TAG = makeLogTag(SecondDayFragment.class);
  private static final String SECOND_DAY = "2014-11-16";

  @InjectView(R.id.second_day_list) ListView secondDayList;
  private List<Talk> mTalks = new ArrayList<Talk>();

  public SecondDayFragment() {
  }

  public static SecondDayFragment getInstance() {
    return new SecondDayFragment();
  }

  @Override public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View rootView = inflater.inflate(R.layout.fragment_second_day, container, false);
    ButterKnife.inject(this, rootView);

    mTalks = talkDao.getTalkByDay(SECOND_DAY);
    LOGD(TAG, "second day : " + mTalks.size());

    ScheduleAdapter mScheduleAdapter = new ScheduleAdapter(mContext);
    mScheduleAdapter.replaceWith(mTalks);

    secondDayList.setAdapter(mScheduleAdapter);

    return rootView;
  }

  @OnItemClick(R.id.second_day_list) void listItemClick(int position) {
    int id = mTalks.get(position).getId();
    Intent i = new Intent(getActivity(), TalkDetailActivity.class);
    i.putExtra(POSITION, id);
    startActivity(i);
  }
}
