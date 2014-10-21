package org.devconmyanmar.apps.devcon.ui;

import android.app.ActionBar;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.ButterKnife;
import io.realm.Realm;
import org.devconmyanmar.apps.devcon.R;
import org.devconmyanmar.apps.devcon.model.Speaker;

import static org.devconmyanmar.apps.devcon.utils.LogUtils.makeLogTag;

public class SpeakerDetailFragment extends BaseFragment {
  private static final String ARG_TALK_ID = "param1";
  private static final String TAG = makeLogTag(SpeakerDetailFragment.class);

  private int mSpeakerId;
  private ActionBar mActionBar;

  public static SpeakerDetailFragment newInstance(String speakerId) {
    SpeakerDetailFragment fragment = new SpeakerDetailFragment();
    Bundle args = new Bundle();
    args.putString(ARG_TALK_ID, speakerId);
    fragment.setArguments(args);
    return fragment;
  }

  public SpeakerDetailFragment() {
    // Required empty public constructor
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (getArguments() != null) {
      mSpeakerId = Integer.valueOf(getArguments().getString(ARG_TALK_ID));
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
    Speaker speaker = realm.where(Speaker.class).equalTo("id", mSpeakerId).findFirst();

    if (mActionBar != null && speaker != null) {
      mActionBar.setTitle(speaker.getName());
      mActionBar.setIcon(android.R.color.transparent);
    }

    return rootView;
  }
}
