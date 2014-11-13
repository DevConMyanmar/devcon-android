package org.devconmyanmar.apps.devcon.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.devconmyanmar.apps.devcon.R;
import org.devconmyanmar.apps.devcon.adapter.MyScheduleAdapter;
import org.devconmyanmar.apps.devcon.db.MyScheduleDao;
import org.devconmyanmar.apps.devcon.model.MySchedule;

/**
 * Created by yemyatthu on 11/12/14.
 */
public class FavoriteDayFragment extends BaseFragment {
  private List<MySchedule> mMySchedules = new ArrayList<MySchedule>();
  private MyScheduleDao mMyScheduleDao;
  private static final String POSITION_ARGS = "position args";
  @InjectView(R.id.favorite_list) ListView mFavoriteList;
  public FavoriteDayFragment(){

  }
  public static FavoriteDayFragment getInstance(int position){
    Bundle bundle = new Bundle();
    bundle.putInt(POSITION_ARGS,position);
    FavoriteDayFragment favoriteDayFragment = new FavoriteDayFragment();
    favoriteDayFragment.setArguments(bundle);
    return favoriteDayFragment;
  }
  @Override public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View v = inflater.inflate(R.layout.fragment_favorite, container, false);
    ButterKnife.inject(this, v);
    mMyScheduleDao = new MyScheduleDao(getActivity());
    try {
      mMySchedules = mMyScheduleDao.getAll();
    } catch (SQLException e) {
      e.printStackTrace();
    }

    MyScheduleAdapter adapter = new MyScheduleAdapter(getActivity());
    ArrayList<MySchedule> firstDay = new ArrayList<MySchedule>();
    ArrayList<MySchedule> secondDay = new ArrayList<MySchedule>();
    for(MySchedule mySchedule:mMySchedules){
      if(mySchedule.getDate() == 0){
        firstDay.add(mySchedule);
      }
      if(mySchedule.getDate() == 1){
        secondDay.add(mySchedule);
      }
    }

    if(getArguments().getInt(POSITION_ARGS) == 0){
      adapter.replaceWith(firstDay);
    }
    if(getArguments().getInt(POSITION_ARGS)==1){
      adapter.replaceWith(secondDay);
    }
    mFavoriteList.setAdapter(adapter);
    return v;
  }
}

