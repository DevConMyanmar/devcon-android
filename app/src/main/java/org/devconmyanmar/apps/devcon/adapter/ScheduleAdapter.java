package org.devconmyanmar.apps.devcon.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import java.util.ArrayList;
import java.util.List;
import org.devconmyanmar.apps.devcon.R;
import org.devconmyanmar.apps.devcon.model.Speaker;
import org.devconmyanmar.apps.devcon.model.Talk;
import org.devconmyanmar.apps.devcon.ui.widget.ForegroundImageView;
import org.devconmyanmar.apps.devcon.utils.Phrase;

import static org.devconmyanmar.apps.devcon.utils.LogUtils.makeLogTag;

/**
 * Created by Ye Lin Aung on 14/10/05.
 */
public class ScheduleAdapter extends BaseAdapter {

  private static final int VIEW_TYPE_KEYNOTE = 0;
  private static final int VIEW_TYPE_NORMAL = 1;
  private static final int VIEW_TYPE_LIGHTNING = 2;
  private static final String TAG = makeLogTag(ScheduleAdapter.class);

  private static final int VIEW_TYPE_COUNT = 3;

  private List<Talk> mTalks = new ArrayList<Talk>();
  private Context mContext;

  public ScheduleAdapter(Context mContext) {
    this.mContext = mContext;
  }

  public void replaceWith(List<Talk> talks) {
    this.mTalks = talks;
    notifyDataSetChanged();
  }

  @Override public int getCount() {
    return mTalks.size();
  }

  @Override public Talk getItem(int position) {
    return mTalks.get(position);
  }

  @Override public long getItemId(int position) {
    return mTalks.get(position).getId();
  }

  @Override public int getViewTypeCount() {
    return VIEW_TYPE_COUNT;
  }

  @Override public int getItemViewType(int position) {
    // return position % VIEW_TYPE_COUNT;
    return mTalks.get(position).getTalk_type() % VIEW_TYPE_COUNT;
  }

  @Override public View getView(int position, View view, ViewGroup parent) {

    Talk mTalk = getItem(position);
    List<Speaker> speakers = mTalk.getSpeakers();

    KeynoteViewHolder keynoteViewHolder;
    NormalViewHolder normalViewHolder;
    LightningViewHolder lightningViewHolder;

    LayoutInflater mInflater =
        (LayoutInflater) mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

    View rootView = view;

    switch (mTalk.getTalk_type()) {
      case VIEW_TYPE_KEYNOTE:
        if (rootView != null) {
          keynoteViewHolder = (KeynoteViewHolder) rootView.getTag();
        } else {
          rootView = mInflater.inflate(R.layout.row_keynote, parent, false);
          keynoteViewHolder = new KeynoteViewHolder(rootView);
          rootView.setTag(keynoteViewHolder);
        }

        keynoteViewHolder.mKeynoteTitle.setText(mTalk.getTitle());
        // Phrase yo!
        CharSequence keyNoteTimeAndPlace =
            Phrase.from(mContext, R.string.talk_detail_time_and_place)
                .put("day", mTalk.getDate())
                .put("from_time", mTalk.getFrom_time())
                .put("to_time", mTalk.getTo_time())
                .put("room", mTalk.getRoom())
                .format();
        keynoteViewHolder.mKeyNoteTime.setText(keyNoteTimeAndPlace);

        return rootView;
      case VIEW_TYPE_NORMAL:
        if (rootView != null) {
          normalViewHolder = (NormalViewHolder) rootView.getTag();
        } else {
          rootView = mInflater.inflate(R.layout.row_normal_schedule, parent, false);
          normalViewHolder = new NormalViewHolder(rootView);
          rootView.setTag(normalViewHolder);
        }
        normalViewHolder.mScheduleTitle.setText(mTalk.getTitle());
        normalViewHolder.mFromTime.setText(mTalk.getFrom_time());
        normalViewHolder.mToTime.setText(mTalk.getTo_time());

        normalViewHolder.mScheduleSpeakers.setText(speakers.get(0).getName());

        return rootView;
      case VIEW_TYPE_LIGHTNING:
        if (rootView != null) {
          lightningViewHolder = (LightningViewHolder) rootView.getTag();
        } else {
          rootView = mInflater.inflate(R.layout.row_lightning_schedule, parent, false);
          lightningViewHolder = new LightningViewHolder(rootView);
          rootView.setTag(lightningViewHolder);
        }
        lightningViewHolder.mLightFromTime.setText(mTalk.getFrom_time());
        lightningViewHolder.mLightToTime.setText(mTalk.getTo_time());
        lightningViewHolder.mLightScheduleTitle.setText(mTalk.getTitle());
        lightningViewHolder.mLightSpeaker.setText(speakers.get(0).getName());

        return rootView;
      default:
    }

    return rootView;
  }

  static class KeynoteViewHolder {
    @InjectView(R.id.keynote_background) ForegroundImageView mKeynoteBackground;
    @InjectView(R.id.keynote_title) TextView mKeynoteTitle;
    @InjectView(R.id.keynote_time_and_place) TextView mKeyNoteTime;

    public KeynoteViewHolder(View view) {
      ButterKnife.inject(this, view);
    }
  }

  static class NormalViewHolder {

    @InjectView(R.id.normal_schedule_from_time) TextView mFromTime;
    @InjectView(R.id.normal_schedule_to_time) TextView mToTime;
    @InjectView(R.id.normal_schedule_title) TextView mScheduleTitle;
    @InjectView(R.id.normal_schedule_speakers) TextView mScheduleSpeakers;

    public NormalViewHolder(View view) {
      ButterKnife.inject(this, view);
    }
  }

  static class LightningViewHolder {
    @InjectView(R.id.lightning_schedule_from_time) TextView mLightFromTime;
    @InjectView(R.id.lightning_schedule_to_time) TextView mLightToTime;
    @InjectView(R.id.lightning_schedule_title) TextView mLightScheduleTitle;
    @InjectView(R.id.lightning_schedule_speaker) TextView mLightSpeaker;

    public LightningViewHolder(View view) {
      ButterKnife.inject(this, view);
    }
  }
}
