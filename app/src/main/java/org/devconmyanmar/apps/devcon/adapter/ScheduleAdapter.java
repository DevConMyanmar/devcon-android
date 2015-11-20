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

import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import mm.technomation.tmmtextutilities.mmtext;
import org.devconmyanmar.apps.devcon.R;
import org.devconmyanmar.apps.devcon.db.SpeakerDao;
import org.devconmyanmar.apps.devcon.model.Talk;
import org.devconmyanmar.apps.devcon.ui.TalkClickListener;
import org.devconmyanmar.apps.devcon.ui.widget.ForegroundImageView;
import org.devconmyanmar.apps.devcon.utils.Phrase;
import org.devconmyanmar.apps.devcon.utils.TimeUtils;

import static org.devconmyanmar.apps.devcon.utils.LogUtils.makeLogTag;

/**
 * Created by Ye Lin Aung on 14/10/05.
 */

public class ScheduleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

  private static final int VIEW_TYPE_KEYNOTE = 1;
  private static final int VIEW_TYPE_NORMAL = 2;
  private static final int VIEW_TYPE_WORKSHOP = 4;

  private static final String TAG = makeLogTag(ScheduleAdapter.class);

  private static final int VIEW_TYPE_COUNT = 4;

  private List<Talk> mTalks = new ArrayList<Talk>();
  private Context mContext;

  private LayoutInflater mInflater;
  private SpeakerDao speakerDao;
  private TalkClickListener talkClickListener;

  public ScheduleAdapter(Context mContext, TalkClickListener talkClickListener) {
    this.mContext = mContext;
    this.mInflater = LayoutInflater.from(mContext);
    this.speakerDao = new SpeakerDao(mContext);
    this.talkClickListener = talkClickListener;
  }

  public void replaceWith(List<Talk> talks) {
    this.mTalks = talks;
    notifyDataSetChanged();
  }

  @Override public long getItemId(int position) {
    return mTalks.get(position).getId();
  }

  @Override public int getItemCount() {
    return mTalks.size();
  }

  @Override public int getItemViewType(int position) {
    return mTalks.get(position).getTalk_type() % VIEW_TYPE_COUNT;
  }

  @Override public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
    switch (viewType) {
      case VIEW_TYPE_KEYNOTE:
        View keynoteView = mInflater.inflate(R.layout.row_keynote, viewGroup, false);
        return new KeynoteViewHolder(keynoteView, talkClickListener);
      case VIEW_TYPE_NORMAL:
        View scheduleView = mInflater.inflate(R.layout.row_normal_schedule, viewGroup, false);
        return new NormalViewHolder(scheduleView, talkClickListener);
      case VIEW_TYPE_WORKSHOP:
        View workshopView = mInflater.inflate(R.layout.row_workshop, viewGroup, false);
        return new WorkshopViewHolder(workshopView, talkClickListener);
      default:
        View defaultView = mInflater.inflate(R.layout.row_normal_schedule, viewGroup, false);
        return new NormalViewHolder(defaultView, talkClickListener);
    }
  }

  @Override public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
    Talk mTalk = mTalks.get(position);
    Resources r = mContext.getResources();
    int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, r.getDisplayMetrics());
    switch (viewHolder.getItemViewType()) {
      case VIEW_TYPE_KEYNOTE:
        KeynoteViewHolder keynoteViewHolder = (KeynoteViewHolder) viewHolder;
        keynoteViewHolder.mKeynoteTitle.setText(mTalk.getTitle());

        String dateString = mTalk.getDate();

        String formattedDate = TimeUtils.parseDateString(dateString);
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
        break;
      case VIEW_TYPE_NORMAL:
        NormalViewHolder normalViewHolder = (NormalViewHolder) viewHolder;

        if (Build.VERSION.SDK_INT >= 21) {
          normalViewHolder.mNormalContainer.setPadding(0, px, 0, 0);
        }

        String normalFormattedFrom = TimeUtils.parseFromToString(mTalk.getFrom_time());
        String normalFormattedTo = TimeUtils.parseFromToString(mTalk.getTo_time());

        String talkTitle = mTalk.getTitle();
        normalViewHolder.mScheduleTitle.setText(talkTitle);
        if (isMyanmarText(talkTitle)) {
          mmtext.prepareView(mContext, normalViewHolder.mScheduleTitle, mmtext.TEXT_UNICODE, true,
              true);
        }

        normalViewHolder.mFromTime.setText(normalFormattedFrom);
        normalViewHolder.mToTime.setText(normalFormattedTo);

        String speakers = flatternSpeakerNames(mTalk.getSpeakers());
        normalViewHolder.mScheduleSpeakers.setText(speakers);
        break;
      case VIEW_TYPE_WORKSHOP:
        WorkshopViewHolder workshopViewHolder = (WorkshopViewHolder) viewHolder;
        workshopViewHolder.mWorkshopBackground.setBackgroundColor(
            ContextCompat.getColor(mContext, R.color.fb_button_background));
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
        break;
    }
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

  static class KeynoteViewHolder extends RecyclerView.ViewHolder {
    @Bind(R.id.keynote_row) FrameLayout mKeynoteWrapper;
    @Bind(R.id.keynote_background) ForegroundImageView mKeynoteBackground;
    @Bind(R.id.keynote_title) TextView mKeynoteTitle;
    @Bind(R.id.keynote_time_and_place) TextView mKeyNoteTime;

    public KeynoteViewHolder(View view, final TalkClickListener talkClickListener) {
      super(view);
      ButterKnife.bind(this, view);
      mKeynoteWrapper.setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
          talkClickListener.onTalkClick(v, getAdapterPosition());
        }
      });
    }
  }

  static class NormalViewHolder extends RecyclerView.ViewHolder {
    @Bind(R.id.normal_card_container) FrameLayout mNormalContainer;
    @Bind(R.id.normal_schedule_from_time) TextView mFromTime;
    @Bind(R.id.normal_schedule_to_time) TextView mToTime;
    @Bind(R.id.normal_schedule_title) TextView mScheduleTitle;
    @Bind(R.id.normal_schedule_speakers) TextView mScheduleSpeakers;

    public NormalViewHolder(View view, final TalkClickListener talkClickListener) {
      super(view);
      ButterKnife.bind(this, view);
      mNormalContainer.setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
          talkClickListener.onTalkClick(v, getAdapterPosition());
        }
      });
    }
  }

  static class WorkshopViewHolder extends RecyclerView.ViewHolder {
    @Bind(R.id.workshop_wrapper) FrameLayout workshopWrapper;
    @Bind(R.id.workshop_background) ForegroundImageView mWorkshopBackground;
    @Bind(R.id.workshop_title) TextView mWorkshopTitle;
    @Bind(R.id.workshop_time_and_place) TextView mWorkshopTime;

    public WorkshopViewHolder(View view, final TalkClickListener talkClickListenerk) {
      super(view);
      ButterKnife.bind(this, view);
      workshopWrapper.setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
          talkClickListenerk.onTalkClick(v, getAdapterPosition());
        }
      });
    }
  }

  public static boolean isMyanmarText(String str) {
    Pattern normalRange = Pattern.compile("^[\\u1000-\\u109f]");
    Pattern extendedRange = Pattern.compile("^[\\uaa60-\\uaa7f]");
    Matcher normalMatcher = normalRange.matcher(str);
    Matcher extendedMatcher = extendedRange.matcher(str);

    return normalMatcher.find() || extendedMatcher.find();
  }
}
