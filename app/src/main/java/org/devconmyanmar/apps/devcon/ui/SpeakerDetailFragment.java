package org.devconmyanmar.apps.devcon.ui;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.bumptech.glide.Glide;
import org.devconmyanmar.apps.devcon.R;
import org.devconmyanmar.apps.devcon.model.Speaker;
import org.devconmyanmar.apps.devcon.transformer.CircleTransformer;

import static org.devconmyanmar.apps.devcon.utils.LogUtils.makeLogTag;

public class SpeakerDetailFragment extends BaseFragment {
  private static final String ARG_TALK_ID = "param1";
  private static final String TAG = makeLogTag(SpeakerDetailFragment.class);

  @InjectView(R.id.speaker_detail_name) TextView mSpeakerName;
  @InjectView(R.id.speaker_detail_title) TextView mSpeakerTitle;
  @InjectView(R.id.speaker_detail_profile_image) ImageView mSpeakerProfileImage;
  @InjectView(R.id.speaker_detail_description) TextView mSpeakerDescription;

  private String mSpeakerId;
  private ActionBar mActionBar;

  public SpeakerDetailFragment() {
    // Required empty public constructor
  }

  public static SpeakerDetailFragment newInstance(String speakerId) {
    SpeakerDetailFragment fragment = new SpeakerDetailFragment();
    Bundle args = new Bundle();
    args.putString(ARG_TALK_ID, speakerId);
    fragment.setArguments(args);
    return fragment;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (getArguments() != null) {
      mSpeakerId = getArguments().getString(ARG_TALK_ID);
    }

    mActionBar = ((BaseActivity) getActivity()).getSupportActionBar();
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    View rootView = inflater.inflate(R.layout.fragment_speaker_detail, container, false);

    ButterKnife.inject(this, rootView);

    Speaker speaker = speakerDao.getSpeakerById(mSpeakerId);

    if (speaker != null) {
      if (mActionBar != null) {
        mActionBar.setTitle(getString(R.string.speakers));
        mActionBar.setIcon(android.R.color.transparent);
      }

      mSpeakerName.setText(speaker.getName());
      mSpeakerTitle.setText(speaker.getTitle());
      mSpeakerDescription.setText(speaker.getDescription());

      Glide.with(mContext)
          .load(speaker.getPhoto())
          .centerCrop()
          .error(R.drawable.person_image_empty)
          .placeholder(R.drawable.person_image_empty)
          .crossFade()
          .transform(new CircleTransformer(mContext))
          .into(mSpeakerProfileImage);
    }

    return rootView;
  }
}
