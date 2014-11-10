package org.devconmyanmar.apps.devcon.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnItemClick;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.devconmyanmar.apps.devcon.R;
import org.devconmyanmar.apps.devcon.adapter.SpeakerAdapter;
import org.devconmyanmar.apps.devcon.model.Speaker;

import static org.devconmyanmar.apps.devcon.Config.POSITION;

/**
 * Created by Ye Lin Aung on 14/10/05.
 */
public class SpeakerFragment extends BaseFragment {

  @InjectView(R.id.my_list) ListView speakerList;
  @InjectView(R.id.toolbar) Toolbar mToolbar;
  private List<Speaker> mSpeakers = new ArrayList<Speaker>();

  public SpeakerFragment() {
  }

  public static SpeakerFragment getInstance() {
    return new SpeakerFragment();
  }

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    getActivity().setTitle(getString(R.string.speaker_list));
  }

  @Override public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View rootView = inflater.inflate(R.layout.fragment_speaker, container, false);
    ButterKnife.inject(this, rootView);
    ((BaseActivity) getActivity()).setSupportActionBar(mToolbar);
    mToolbar.setTitleTextColor(getActivity().getResources().getColor(android.R.color.white));
    mToolbar.setNavigationIcon(R.drawable.ic_drawer);
    try {
      mSpeakers = speakerDao.getAll();
      SpeakerAdapter speakerAdapter = new SpeakerAdapter(mContext);
      speakerAdapter.replaceWith(mSpeakers);
      speakerList.setAdapter(speakerAdapter);
    } catch (SQLException e) {
      e.printStackTrace();
    }

    return rootView;
  }

  @SuppressWarnings("unused") @OnItemClick(R.id.my_list) void speakerListItemClick(
      int position) {
    int id = mSpeakers.get(position).getId();
    Intent i = new Intent(getActivity(), SpeakerDetailActivity.class);
    i.putExtra(POSITION, id);
    startActivity(i);
  }
}
