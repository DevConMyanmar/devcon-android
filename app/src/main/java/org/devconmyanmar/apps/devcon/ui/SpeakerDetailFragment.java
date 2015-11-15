/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014 Devcon Contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package org.devconmyanmar.apps.devcon.ui;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.Bind;
import com.bumptech.glide.Glide;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import org.devconmyanmar.apps.devcon.R;
import org.devconmyanmar.apps.devcon.model.Speaker;
import org.devconmyanmar.apps.devcon.transformer.CircleTransformer;
import org.devconmyanmar.apps.devcon.utils.AnalyticsManager;

import static org.devconmyanmar.apps.devcon.utils.LogUtils.LOGD;
import static org.devconmyanmar.apps.devcon.utils.LogUtils.makeLogTag;

public class SpeakerDetailFragment extends BaseFragment {
  private static final String ARG_TALK_ID = "param1";
  private static final String TAG = makeLogTag(SpeakerDetailFragment.class);

  private static final String SCREEN_LABEL = "Speaker Detail";

  @Bind(R.id.speaker_detail_name) TextView mSpeakerName;
  @Bind(R.id.speaker_detail_title) TextView mSpeakerTitle;
  @Bind(R.id.speaker_detail_profile_image) ImageView mSpeakerProfileImage;
  @Bind(R.id.speaker_detail_description) TextView mSpeakerDescription;

  private String mSpeakerId;

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
      LOGD(TAG, "speaker id in detail : " + mSpeakerId);
    }
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    View rootView = inflater.inflate(R.layout.fragment_speaker_detail, container, false);

    ButterKnife.bind(this, rootView);
    if (Build.VERSION.SDK_INT >= 19) {

      SystemBarTintManager tintManager = new SystemBarTintManager(getActivity());
      tintManager.setStatusBarTintEnabled(true);
      tintManager.setNavigationBarTintEnabled(false);
      tintManager.setTintColor(getResources().getColor(R.color.translucent_actionbar_background));
    }

    Speaker speaker = speakerDao.getSpeakerById(mSpeakerId);

    if (speaker != null) {
      AnalyticsManager.sendScreenView(SCREEN_LABEL + " : " + speaker.getName());

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
