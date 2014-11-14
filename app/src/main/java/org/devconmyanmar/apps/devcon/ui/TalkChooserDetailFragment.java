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

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import com.google.gson.Gson;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import java.util.ArrayList;
import org.devconmyanmar.apps.devcon.R;
import org.devconmyanmar.apps.devcon.adapter.SpeakerAdapter;
import org.devconmyanmar.apps.devcon.model.Speaker;
import org.devconmyanmar.apps.devcon.model.Talk;
import org.devconmyanmar.apps.devcon.ui.widget.CheckableFrameLayout;
import org.devconmyanmar.apps.devcon.ui.widget.SpeakerItemView;
import org.devconmyanmar.apps.devcon.ui.widget.StickyScrollView;
import org.devconmyanmar.apps.devcon.utils.AnalyticsManager;
import org.devconmyanmar.apps.devcon.utils.HelpUtils;
import org.devconmyanmar.apps.devcon.utils.LUtils;
import org.devconmyanmar.apps.devcon.utils.Phrase;
import org.devconmyanmar.apps.devcon.utils.TimeUtils;

import static org.devconmyanmar.apps.devcon.utils.LogUtils.LOGD;
import static org.devconmyanmar.apps.devcon.utils.LogUtils.makeLogTag;

public class TalkChooserDetailFragment extends BaseFragment {
  private static final String ARG_TALK_ID = "param1";
  private static final String TAG = makeLogTag(TalkDetailFragment.class);
  private static final String SCREEN_LABEL = "Talk Detail";

  @InjectView(R.id.talk_title) TextView mTalkTitle;
  @InjectView(R.id.talk_detail_scroll_view) StickyScrollView talkDetailScrollView;
  @InjectView(R.id.talk_time_and_room) TextView talkTimeAndRoom;
  @InjectView(R.id.talk_description) TextView talkDescription;
  @InjectView(R.id.add_schedule_button) CheckableFrameLayout mAddToFav;
  @InjectView(R.id.related_speaker_wrapper) LinearLayout realatedSpeakerWrapper;
  @InjectView(R.id.toolbar) Toolbar mToolbar;

  private LUtils mLUtils;
  private int mTalkId;
  private Talk mTalk;
  private boolean isFavourite;
  private BaseActivity mActivity;

  public TalkChooserDetailFragment() {
    // Required empty public constructor
  }

  public static TalkChooserDetailFragment newInstance(String talk_id) {
    TalkChooserDetailFragment fragment = new TalkChooserDetailFragment();
    Bundle args = new Bundle();
    args.putString(ARG_TALK_ID, talk_id);
    fragment.setArguments(args);
    return fragment;
  }

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (getArguments() != null) {
      mTalkId = Integer.valueOf(getArguments().getString(ARG_TALK_ID));
    }
    setHasOptionsMenu(true);
    mActivity = (BaseActivity) getActivity();
    mLUtils = LUtils.getInstance(getActivity());
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    View rootView = inflater.inflate(R.layout.fragment_talk_chooser_detail, container, false);

    ButterKnife.inject(this, rootView);
    mActivity.setSupportActionBar(mToolbar);
    mActivity.getSupportActionBar().setTitle(R.string.title_activity_schedule_detail);
    mActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    mActivity.getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_ab_back_mtrl_am_alpha);
    if (Build.VERSION.SDK_INT >= 19) {
      SystemBarTintManager tintManager = new SystemBarTintManager(getActivity());
      tintManager.setStatusBarTintEnabled(true);
      tintManager.setNavigationBarTintEnabled(false);
      tintManager.setTintColor(getResources().getColor(R.color.translucent_actionbar_background));
    }

    mTalk = talkDao.getTalkById(mTalkId);
    mTalkTitle.setText(mTalk.getTitle());

    AnalyticsManager.sendScreenView(SCREEN_LABEL + " : " + mTalk.getTitle());

    isFavourite = mTalk.isFavourite();
    LOGD(TAG, "favourite ? " + isFavourite);
    showStarred(isFavourite, true);

    talkDetailScrollView.hideActionBarOnScroll(true);

    // If talk type is keynote, don't show the add to fav button
    showAddToFav(mTalk);

    String dateString = mTalk.getDate();
    String formattedDate = TimeUtils.parseDateString(dateString);
    String formattedFrom = TimeUtils.parseFromToString(mTalk.getFrom_time());
    String formattedTo = TimeUtils.parseFromToString(mTalk.getTo_time());
    String room = TimeUtils.getProperRoomName(mTalk.getRoom());

    CharSequence timeAndRoom = Phrase.from(mContext, R.string.talk_detail_time_and_place)
        .put("day", formattedDate)
        .put("from_time", formattedFrom)
        .put("to_time", formattedTo)
        .put("room", room)
        .format();

    talkTimeAndRoom.setText(timeAndRoom);

    talkDescription.setText(mTalk.getDescription());

    SpeakerAdapter speakerAdapter = new SpeakerAdapter(mContext);
    speakerAdapter.replaceWith(flatternSpeakers(mTalk.getSpeakers()));

    ArrayList<Speaker> mSpeakers = flatternSpeakers(mTalk.getSpeakers());

    for (Speaker s : mSpeakers) {
      final SpeakerItemView speakerItemView =
          (SpeakerItemView) inflater.inflate(R.layout.speaker_layout, null, false);

      speakerItemView.setId(s.getId());
      speakerItemView.setBackgroundColor(Color.WHITE);
      TextView speakerName = (TextView) speakerItemView.findViewById(R.id.speaker_title);
      TextView speakerAbstract = (TextView) speakerItemView.findViewById(R.id.speaker_abstract);

      speakerName.setText(s.getName());
      speakerAbstract.setText(s.getTitle());

      realatedSpeakerWrapper.addView(speakerItemView);
    }

    return rootView;
  }

  @SuppressWarnings("unused") @OnClick(R.id.add_schedule_button) void addToFav() {
    boolean starred = !isFavourite;
    if (!isFavourite) {
      AnalyticsManager.sendEvent("Talk Detail", "favourite", mTalk.getTitle());
      mTalk.setFavourite(true);
      showStarred(starred, true);
      mTalk.setId(mTalkId);
      talkDao.createOrUpdate(mTalk);
    } else {
      AnalyticsManager.sendEvent("Talk Detail", "un-favourite", mTalk.getTitle());
      mTalk.setFavourite(false);
      showStarred(starred, true);
      mTalk.setId(mTalkId);
      talkDao.createOrUpdate(mTalk);
    }
  }

  private void showStarred(boolean starred, boolean allowAnimate) {
    isFavourite = starred;

    mAddToFav.setChecked(isFavourite, allowAnimate);

    ImageView iconView = (ImageView) mAddToFav.findViewById(R.id.add_to_fav_icon);
    mLUtils.setOrAnimatePlusCheckIcon(iconView, starred, allowAnimate);
  }

  private void showAddToFav(Talk mTalk) {
    int talkType = mTalk.getTalk_type();
    switch (talkType) {
      case 1:
        mAddToFav.setVisibility(View.GONE);
      default:
        mAddToFav.setVisibility(View.VISIBLE);
    }
  }

  private ArrayList<Speaker> flatternSpeakers(String speakers) {
    ArrayList<Speaker> mSpeakers = new ArrayList<Speaker>();
    String id[] = new Gson().fromJson(speakers, String[].class);
    for (String anId : id) {
      mSpeakers.add(speakerDao.getSpeakerById(anId));
    }

    return mSpeakers;
  }

  @Override public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    super.onCreateOptionsMenu(menu, inflater);
    inflater.inflate(R.menu.schedule_menu, menu);
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.action_about:
        HelpUtils.showAbout(mActivity);
        return true;
      case android.R.id.home:
        Intent i = new Intent(mContext, TalkListActivity.class);
        i.putExtra("Fragment", 0);
        startActivity(i);
        return true;
      default:
        return super.onOptionsItemSelected(item);
    }
  }
}
