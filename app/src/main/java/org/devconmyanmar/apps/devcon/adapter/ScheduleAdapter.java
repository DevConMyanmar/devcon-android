package org.devconmyanmar.apps.devcon.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import java.util.ArrayList;
import java.util.List;
import org.devconmyanmar.apps.devcon.R;
import org.devconmyanmar.apps.devcon.model.Talk;
import org.devconmyanmar.apps.devcon.ui.widget.ForegroundImageView;
import org.devconmyanmar.apps.devcon.utils.Phrase;
import org.devconmyanmar.apps.devcon.utils.TimeUtils;
import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;

import static org.devconmyanmar.apps.devcon.utils.LogUtils.makeLogTag;

/**
 * Created by Ye Lin Aung on 14/10/05.
 */
public class ScheduleAdapter extends BaseAdapter implements StickyListHeadersAdapter {

  private static final int VIEW_TYPE_KEYNOTE = 1;
  private static final int VIEW_TYPE_NORMAL = 2;
  private static final int VIEW_TYPE_LIGHTNING = 3;
  private static final String TAG = makeLogTag(ScheduleAdapter.class);

  private static final int VIEW_TYPE_COUNT = 3;

  private List<Talk> mTalks = new ArrayList<Talk>();
  private Context mContext;

  private LayoutInflater mInflater;

  private String formattedDate;

  public ScheduleAdapter(Context mContext) {
    this.mContext = mContext;
    this.mInflater = LayoutInflater.from(mContext);
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

    KeynoteViewHolder keynoteViewHolder;
    NormalViewHolder normalViewHolder;
    LightningViewHolder lightningViewHolder;

    LayoutInflater mInflater =
        (LayoutInflater) mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

    View rootView = view;
    Resources r = mContext.getResources();
    int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, r.getDisplayMetrics());

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

        String dateString = mTalk.getDate();

        formattedDate = TimeUtils.parseDateString(dateString);
        String keynoteFormattedFrom = TimeUtils.parseFromToString(mTalk.getFrom_time());
        String keynoteFormattedTo = TimeUtils.parseFromToString(mTalk.getTo_time());
        String keynoteRoom = TimeUtils.getProperRoomName(mTalk.getRoom());
        // Phrase yo!
        CharSequence keyNoteTimeAndPlace =
            Phrase.from(mContext, R.string.talk_detail_time_and_place)
                .put("day", formattedDate)
                .put("from_time", keynoteFormattedFrom)
                .put("to_time", keynoteFormattedTo)
                .put("room", keynoteRoom)
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

        if (Build.VERSION.SDK_INT >= 21){
        normalViewHolder.mNormalContainer.setPadding(0,px,0,0);}
        String normalFormattedFrom = TimeUtils.parseFromToString(mTalk.getFrom_time());
        String normalFormattedTo = TimeUtils.parseFromToString(mTalk.getTo_time());

        normalViewHolder.mScheduleTitle.setText(mTalk.getTitle());
        normalViewHolder.mFromTime.setText(normalFormattedFrom);
        normalViewHolder.mToTime.setText(normalFormattedTo);

        // FIXME load speaker by id
        //normalViewHolder.mScheduleSpeakers.setText(speakers.get(0).getName());

        return rootView;
      case VIEW_TYPE_LIGHTNING:
        if (rootView != null) {
          lightningViewHolder = (LightningViewHolder) rootView.getTag();
        } else {
          rootView = mInflater.inflate(R.layout.row_lightning_schedule, parent, false);
          lightningViewHolder = new LightningViewHolder(rootView);
          rootView.setTag(lightningViewHolder);
        }

        String lFormattedFrom = TimeUtils.parseFromToString(mTalk.getFrom_time());
        String lFormattedTo = TimeUtils.parseFromToString(mTalk.getTo_time());
        if (Build.VERSION.SDK_INT >= 21){
          lightningViewHolder.mLightContainer.setPadding(0,px,0,0);}
        lightningViewHolder.mLightFromTime.setText(lFormattedFrom);
        lightningViewHolder.mLightToTime.setText(lFormattedTo);
        lightningViewHolder.mLightScheduleTitle.setText(mTalk.getTitle());
        //lightningViewHolder.mLightSpeaker.setText(speakers.get(0).getName());

        return rootView;
      default:
    }

    return rootView;
  }

  @Override public View getHeaderView(int i, View view, ViewGroup viewGroup) {
    HeaderViewHolder holder;
    if (view == null) {
      holder = new HeaderViewHolder();
      view = mInflater.inflate(R.layout.room_header, viewGroup, false);
      assert view != null;
      holder.header = (TextView) view.findViewById(R.id.room_name);
      view.setTag(holder);
    } else {
      holder = (HeaderViewHolder) view.getTag();
    }

    CharSequence headerChar = TimeUtils.getProperRoomName(mTalks.get(i).getRoom());
    holder.header.setText(headerChar);
    holder.header.invalidate();

    return view;
  }

  @Override public long getHeaderId(int i) {
    return mTalks.get(i).getRoom().subSequence(0, 1).charAt(0);
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
    @InjectView(R.id.normal_card_container) FrameLayout mNormalContainer;
    @InjectView(R.id.normal_schedule_from_time) TextView mFromTime;
    @InjectView(R.id.normal_schedule_to_time) TextView mToTime;
    @InjectView(R.id.normal_schedule_title) TextView mScheduleTitle;
    @InjectView(R.id.normal_schedule_speakers) TextView mScheduleSpeakers;

    public NormalViewHolder(View view) {
      ButterKnife.inject(this, view);
    }
  }

  static class LightningViewHolder {
    @InjectView(R.id.lightning_card_container) FrameLayout mLightContainer;
    @InjectView(R.id.lightning_schedule_from_time) TextView mLightFromTime;
    @InjectView(R.id.lightning_schedule_to_time) TextView mLightToTime;
    @InjectView(R.id.lightning_schedule_title) TextView mLightScheduleTitle;
    @InjectView(R.id.lightning_schedule_speaker) TextView mLightSpeaker;

    public LightningViewHolder(View view) {
      ButterKnife.inject(this, view);
    }
  }

  private static class HeaderViewHolder {
    TextView header;
  }
}
