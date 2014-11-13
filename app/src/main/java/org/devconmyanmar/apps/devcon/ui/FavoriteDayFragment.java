package org.devconmyanmar.apps.devcon.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import butterknife.ButterKnife;
import butterknife.InjectView;
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

  @InjectView(R.id.favorite_list) ListView mFavoriteList;
  private List<MySchedule> mMySchedules = new ArrayList<MySchedule>();
  private MyScheduleDao mMyScheduleDao;

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
  }
  @Override public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View v = inflater.inflate(R.layout.fragment_favorite, container, false);
    ButterKnife.inject(this, v);

    try {
      mMySchedules = mMyScheduleDao.getAll();
    } catch (SQLException e) {
      e.printStackTrace();
    }

    final MyScheduleAdapter adapter = new MyScheduleAdapter(getActivity());
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

    if (getArguments().getInt(POSITION_ARGS) == 0) {
      adapter.replaceWith(firstDay);
    }
    if (getArguments().getInt(POSITION_ARGS) == 1) {
      adapter.replaceWith(secondDay);
    }

    mFavoriteList.setAdapter(adapter);
    mFavoriteList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        System.out.println(((MySchedule) adapter.getItem(i)).isHasFavorite());
        if (!(((MySchedule) adapter.getItem(i)).isClickBlock())) {
          Intent intent = new Intent(mContext, TalkChooserActivity.class);
          intent.putExtra("START_TIME", ((MySchedule) adapter.getItem(i)).getStart());
          intent.putExtra("END_TIME", ((MySchedule) adapter.getItem(i)).getEnd());
          intent.putExtra("TALK_ID", ((MySchedule) adapter.getItem(i)).getAssociatedTalkId());
          mContext.startActivity(intent);
        }

        if(((MySchedule) adapter.getItem(i)).isHasFavorite()){
          if(((MySchedule) adapter.getItem(i)).getFavoriteTalkId()!=0){
            Intent intent = new Intent(mContext,TalkChooserDetailActivity.class);
            intent.putExtra(Config.POSITION,((MySchedule) adapter.getItem(i)).getFavoriteTalkId());
            mContext.startActivity(intent);
          }
        }
      }
    });
    return v;
  }

  @Override public void onResume() {
    super.onResume();
    LOGD(TAG, "reloading data");
    try {
      mMySchedules = mMyScheduleDao.getAll();
    } catch (SQLException e) {
      e.printStackTrace();
    }

    final MyScheduleAdapter adapter = new MyScheduleAdapter(getActivity());
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

    if (getArguments().getInt(POSITION_ARGS) == 0) {
      adapter.replaceWith(firstDay);
    }
    if (getArguments().getInt(POSITION_ARGS) == 1) {
      adapter.replaceWith(secondDay);
    }

    mFavoriteList.setAdapter(adapter);
  }
}

