package org.devconmyanmar.apps.devcon.ui;

import android.app.ActionBar;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import io.realm.Realm;
import org.devconmyanmar.apps.devcon.R;
import org.devconmyanmar.apps.devcon.model.Talk;

public class ScheduleDetailFragment extends BaseFragment {
  private static final String ARG_TALK_ID = "param1";

  @InjectView(R.id.talk_title) TextView mTalkTitle;

  private int mTalkId;
  private ActionBar mActionBar;

  public static ScheduleDetailFragment newInstance(String talk_id) {
    ScheduleDetailFragment fragment = new ScheduleDetailFragment();
    Bundle args = new Bundle();
    args.putString(ARG_TALK_ID, talk_id);
    fragment.setArguments(args);
    return fragment;
  }

  public ScheduleDetailFragment() {
    // Required empty public constructor
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (getArguments() != null) {
      mTalkId = Integer.valueOf(getArguments().getString(ARG_TALK_ID));
    }

    mActionBar = getActivity().getActionBar();
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    View rootView = inflater.inflate(R.layout.fragment_talk_detail, container, false);

    ButterKnife.inject(this, rootView);

    Realm realm = Realm.getInstance(mContext);
    Talk talk = realm.where(Talk.class).equalTo("id", mTalkId).findFirst();
    mTalkTitle.setText(talk.getTitle());

    if (mActionBar != null) {
      mActionBar.setTitle(getString(R.string.title_activity_schedule_detail));
    }

    return rootView;
  }
}
