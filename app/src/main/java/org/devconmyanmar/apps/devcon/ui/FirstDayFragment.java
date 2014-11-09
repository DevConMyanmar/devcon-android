package org.devconmyanmar.apps.devcon.ui;

import android.app.Activity;
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
import io.realm.Realm;
import io.realm.RealmQuery;
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
public class FirstDayFragment extends BaseFragment {

  private static final String TAG = makeLogTag(FirstDayFragment.class);
  @InjectView(R.id.first_day_list) ListView firstDayList;
  private List<Talk> mTalks = new ArrayList<Talk>();

  public FirstDayFragment() {
  }

  public static FirstDayFragment getInstance() {
    return new FirstDayFragment();
  }

  @Override public void onAttach(Activity activity) {
    super.onAttach(activity);
  }

  @Override public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {

    View rootView = inflater.inflate(R.layout.fragment_first_day, container, false);
    ButterKnife.inject(this, rootView);

    Realm realm = Realm.getInstance(mContext);

    RealmQuery<Talk> query = realm.where(Talk.class);
    query.equalTo("date", "151114");
    mTalks = query.findAll();

    ScheduleAdapter mScheduleAdapter = new ScheduleAdapter(mContext);
    mScheduleAdapter.replaceWith(mTalks);

    firstDayList.setAdapter(mScheduleAdapter);

    return rootView;
  }

  @SuppressWarnings("unused") @OnItemClick(R.id.first_day_list) void listItemClick(int position) {
    int id = mTalks.get(position).getId();
    LOGD(TAG, "Talk Type -> " + mTalks.get(position).getTalk_type());
    Intent i = new Intent(getActivity(), TalkDetailActivity.class);
    i.putExtra(POSITION, id);
    startActivity(i);
  }
}
