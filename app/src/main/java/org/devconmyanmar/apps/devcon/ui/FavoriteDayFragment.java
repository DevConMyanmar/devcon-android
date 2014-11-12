package org.devconmyanmar.apps.devcon.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import org.devconmyanmar.apps.devcon.R;

/**
 * Created by yemyatthu on 11/12/14.
 */
public class FavoriteDayFragment extends BaseFragment {
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
    return v;
  }
}

