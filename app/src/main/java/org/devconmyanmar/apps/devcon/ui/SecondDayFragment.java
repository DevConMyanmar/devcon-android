package org.devconmyanmar.apps.devcon.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnItemClick;
import io.realm.Realm;
import io.realm.RealmQuery;
import java.util.ArrayList;
import java.util.List;
import org.devconmyanmar.apps.devcon.R;
import org.devconmyanmar.apps.devcon.adapter.ScheduleAdapter;
import org.devconmyanmar.apps.devcon.model.Talk;

import static org.devconmyanmar.apps.devcon.utils.LogUtils.LOGD;
import static org.devconmyanmar.apps.devcon.utils.LogUtils.makeLogTag;

/**
 * Created by Ye Lin Aung on 14/10/05.
 */
public class SecondDayFragment extends BaseFragment {

  private static final String TAG = makeLogTag(SecondDayFragment.class);
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

    Realm realm = Realm.getInstance(mContext);
    RealmQuery<Talk> query = realm.where(Talk.class);
    query.equalTo("date", "Oct 16");
    mTalks = query.findAll();

    ScheduleAdapter mScheduleAdapter = new ScheduleAdapter(mContext);
    mScheduleAdapter.replaceWith(mTalks);

    secondDayList.setAdapter(mScheduleAdapter);

    return rootView;
  }

  @OnItemClick(R.id.second_day_list) void listItemClick(int position) {
    LOGD(TAG, "photo -> " + mTalks.get(position).getPhoto());
  }
}
