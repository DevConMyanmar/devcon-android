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
 * OUT OF OR IN CONNECTION WpeITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

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
import butterknife.Bind;
import butterknife.ButterKnife;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.List;
import org.devconmyanmar.apps.devcon.R;
import org.devconmyanmar.apps.devcon.db.SpeakerDao;
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
  private static final int VIEW_TYPE_WORKSHOP = 4;

  private static final String TAG = makeLogTag(ScheduleAdapter.class);

  private static final int VIEW_TYPE_COUNT = 4;

  private List<Talk> mTalks = new ArrayList<Talk>();
  private Context mContext;

  private LayoutInflater mInflater;
  private SpeakerDao speakerDao;

  private String formattedDate;

  public ScheduleAdapter(Context mContext) {
    this.mContext = mContext;
    this.mInflater = LayoutInflater.from(mContext);
    this.speakerDao = new SpeakerDao(mContext);
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
    WorkshopViewHolder workshopViewHolder;

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

        if (Build.VERSION.SDK_INT >= 21) {
          normalViewHolder.mNormalContainer.setPadding(0, px, 0, 0);
        }
        String normalFormattedFrom = TimeUtils.parseFromToString(mTalk.getFrom_time());
        String normalFormattedTo = TimeUtils.parseFromToString(mTalk.getTo_time());

        normalViewHolder.mScheduleTitle.setText(mTalk.getTitle());
        normalViewHolder.mFromTime.setText(normalFormattedFrom);
        normalViewHolder.mToTime.setText(normalFormattedTo);

        String speakers = flatternSpeakerNames(mTalk.getSpeakers());
        normalViewHolder.mScheduleSpeakers.setText(speakers);

        return rootView;
      case VIEW_TYPE_WORKSHOP:
        if (rootView != null) {
          workshopViewHolder = (WorkshopViewHolder) rootView.getTag();
        } else {
          rootView = mInflater.inflate(R.layout.row_workshop, parent, false);
          workshopViewHolder = new WorkshopViewHolder(rootView);
          rootView.setTag(workshopViewHolder);
        }

        workshopViewHolder.mWorkshopBackground.setBackgroundColor(
            mContext.getResources().getColor(R.color.fb_button_background));
        workshopViewHolder.mWorkshopTitle.setText(mTalk.getTitle());

        String workshopDate = mTalk.getDate();

        formattedDate = TimeUtils.parseDateString(workshopDate);
        String wsFormattedFrom = TimeUtils.parseFromToString(mTalk.getFrom_time());
        String wsFormattedTo = TimeUtils.parseFromToString(mTalk.getTo_time());
        String wsRoom = TimeUtils.getProperRoomName(mTalk.getRoom());
        // Phrase yo!
        CharSequence wsNoteTimeAndPlace = Phrase.from(mContext, R.string.talk_detail_time_and_place)
            .put("day", formattedDate)
            .put("from_time", wsFormattedFrom)
            .put("to_time", wsFormattedTo)
            .put("room", wsRoom)
            .format();
        workshopViewHolder.mWorkshopTime.setText(wsNoteTimeAndPlace);

        return rootView;

      default:
        return rootView;
    }
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

  private String flatternSpeakerNames(String speakers) {
    String id[] = new Gson().fromJson(speakers, String[].class);
    String s = "";
    for (int i = 0; i < id.length; i++) {
      s = s + speakerDao.getSpeakerNameById(id[i]);
      // Do not append comma at the end of last element
      if (i < id.length - 1) {
        s = s + ", ";
      }
    }

    return s;
  }

  static class KeynoteViewHolder {
    @Bind(R.id.keynote_row) FrameLayout mKeynoteWrapper;
    @Bind(R.id.keynote_background) ForegroundImageView mKeynoteBackground;
    @Bind(R.id.keynote_title) TextView mKeynoteTitle;
    @Bind(R.id.keynote_time_and_place) TextView mKeyNoteTime;

    public KeynoteViewHolder(View view) {
      ButterKnife.bind(this, view);
    }
  }

  static class NormalViewHolder {
    @Bind(R.id.normal_card_container) FrameLayout mNormalContainer;
    @Bind(R.id.normal_schedule_from_time) TextView mFromTime;
    @Bind(R.id.normal_schedule_to_time) TextView mToTime;
    @Bind(R.id.normal_schedule_title) TextView mScheduleTitle;
    @Bind(R.id.normal_schedule_speakers) TextView mScheduleSpeakers;

    public NormalViewHolder(View view) {
      ButterKnife.bind(this, view);
    }
  }

  static class WorkshopViewHolder {
    @Bind(R.id.workshop_background) ForegroundImageView mWorkshopBackground;
    @Bind(R.id.workshop_title) TextView mWorkshopTitle;
    @Bind(R.id.workshop_time_and_place) TextView mWorkshopTime;

    public WorkshopViewHolder(View view) {
      ButterKnife.bind(this, view);
    }
  }

  static class HeaderViewHolder {
    TextView header;
  }
}
