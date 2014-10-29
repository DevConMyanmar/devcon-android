package org.devconmyanmar.apps.devcon.ui;

import android.app.ActionBar;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import io.realm.Realm;
import java.util.List;
import org.devconmyanmar.apps.devcon.R;
import org.devconmyanmar.apps.devcon.adapter.SpeakerAdapter;
import org.devconmyanmar.apps.devcon.model.Speaker;
import org.devconmyanmar.apps.devcon.model.Talk;
import org.devconmyanmar.apps.devcon.ui.widget.CheckableFrameLayout;
import org.devconmyanmar.apps.devcon.ui.widget.StickyScrollView;
import org.devconmyanmar.apps.devcon.utils.LUtils;
import org.devconmyanmar.apps.devcon.utils.Phrase;

import static org.devconmyanmar.apps.devcon.utils.LogUtils.LOGD;
import static org.devconmyanmar.apps.devcon.utils.LogUtils.makeLogTag;

public class TalkDetailFragment extends BaseFragment {
  private static final String ARG_TALK_ID = "param1";
  private static final String TAG = makeLogTag(TalkDetailFragment.class);

  @InjectView(R.id.talk_title) TextView mTalkTitle;
  @InjectView(R.id.talk_detail_scroll_view) StickyScrollView talkDetailScrollView;
  @InjectView(R.id.talk_time_and_room) TextView talkTimeAndRoom;
  @InjectView(R.id.talk_description) TextView talkDescription;
  @InjectView(R.id.include_speaker_list) ListView mSpeakerList;
  @InjectView(R.id.add_schedule_button) CheckableFrameLayout mAddToFav;

  private LUtils mLUtils;
  private int mTalkId;
  private ActionBar mActionBar;
  private Realm realm;
  private Talk talk;
  private boolean isFavourite;

  public TalkDetailFragment() {
    // Required empty public constructor
  }

  public static TalkDetailFragment newInstance(String talk_id) {
    TalkDetailFragment fragment = new TalkDetailFragment();
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

    realm = Realm.getInstance(mContext);

    mActionBar = getActivity().getActionBar();

    mLUtils = LUtils.getInstance(mContext);
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    View rootView = inflater.inflate(R.layout.fragment_talk_detail, container, false);

    ButterKnife.inject(this, rootView);

    talk = realm.where(Talk.class).equalTo("id", mTalkId).findFirst();
    mTalkTitle.setText(talk.getTitle());

    isFavourite = talk.isFavourite();
    LOGD(TAG, "favourite ? " + isFavourite);

    if (isFavourite) {
      mAddToFav.setChecked(true);
    } else {
      mAddToFav.setChecked(false);
    }

    if (mActionBar != null) {
      mActionBar.setTitle(getString(R.string.title_activity_schedule_detail));
      mActionBar.setIcon(android.R.color.transparent);
    }

    talkDetailScrollView.hideActionBarOnScroll(true);

    CharSequence timeAndRoom = Phrase.from(mContext, R.string.talk_detail_time_and_place)
        .put("day", talk.getDate())
        .put("from_time", talk.getFrom_time())
        .put("to_time", talk.getTo_time())
        .put("room", talk.getRoom())
        .format();

    talkTimeAndRoom.setText(timeAndRoom);

    talkDescription.setText(talk.getDescription());

    List<Speaker> speakers = talk.getSpeakers();
    SpeakerAdapter speakerAdapter = new SpeakerAdapter(mContext);
    speakerAdapter.replaceWith(speakers);
    mSpeakerList.setAdapter(speakerAdapter);

    return rootView;
  }

  @OnClick(R.id.add_schedule_button) void addToFav() {

    realm.beginTransaction();

    if (!isFavourite && !mAddToFav.isChecked()) {
      mAddToFav.setChecked(true);
      talk.setFavourite(true);
    } else {
      mAddToFav.setChecked(false);
      talk.setFavourite(false);
    }

    realm.commitTransaction();

    ImageView iconView = (ImageView) mAddToFav.findViewById(R.id.add_to_fav_icon);
    mLUtils.setOrAnimatePlusCheckIcon(iconView, true, true);
  }
}
