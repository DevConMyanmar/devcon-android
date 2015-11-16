package org.devconmyanmar.apps.devcon.adapter;

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

/**
 * Created by yemyatthu on 11/15/14.
 */
public class LighteningTalkAdapter extends BaseAdapter {
  private List<Talk> mTalks = new ArrayList<Talk>();
  private Context mContext;
  private LayoutInflater mLayoutInflater;

  public LighteningTalkAdapter(Context context) {
    mContext = context;
    mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
  }

  public void replaceWith(List<Talk> talks) {
    mTalks = talks;
    notifyDataSetChanged();
  }

  @Override public int getCount() {
    return mTalks.size();
  }

  @Override public Object getItem(int i) {
    return mTalks.get(i);
  }

  @Override public long getItemId(int i) {
    return mTalks.get(i).getId();
  }

  @Override public View getView(int i, View view, ViewGroup viewGroup) {
    ViewHolder viewHolder;
    Talk talk = (Talk) getItem(i);
    Resources r = mContext.getResources();
    int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, r.getDisplayMetrics());
    if (view != null) {
      viewHolder = (ViewHolder) view.getTag();
    } else {
      view = mLayoutInflater.inflate(R.layout.row_lightning_schedule, viewGroup, false);
      viewHolder = new ViewHolder(view);
      view.setTag(viewHolder);
    }
    viewHolder.mLightningScheduleTitle.setText(talk.getTitle());
    String speakers = flatternSpeakerNames(talk.getSpeakers());
    viewHolder.mLightningScheduleSpeaker.setText(speakers);
    if (Build.VERSION.SDK_INT >= 21) {
      viewHolder.mLightningCardContainer.setPadding(0, px, 0, px);
    }
    return view;
  }

  //@Override public View getHeaderView(int i, View view, ViewGroup viewGroup) {
  //  HeaderViewHolder holder;
  //  if (view == null) {
  //
  //    view = mLayoutInflater.inflate(R.layout.room_header, viewGroup, false);
  //    holder = new HeaderViewHolder(view);
  //    view.setTag(holder);
  //  } else {
  //    holder = (HeaderViewHolder) view.getTag();
  //  }
  //
  //  CharSequence headerChar = TimeUtils.getProperRoomName(mTalks.get(i).getRoom());
  //  holder.header.setText(headerChar);
  //  String keynoteFormattedFrom = TimeUtils.parseFromToString(mTalks.get(i).getFrom_time());
  //  String keynoteFormattedTo = TimeUtils.parseFromToString(mTalks.get(i).getTo_time());
  //  holder.time.setText(keynoteFormattedFrom + " to " + keynoteFormattedTo);
  //  holder.time.invalidate();
  //  holder.header.invalidate();
  //
  //  return view;
  //}
  //
  //@Override public long getHeaderId(int i) {
  //  return mTalks.get(i).getRoom().subSequence(0, 1).charAt(0);
  //}
  //
  private String flatternSpeakerNames(String speakers) {
    SpeakerDao speakerDao = new SpeakerDao(mContext);
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

  /**
   * This class contains all butterknife-binded Views & Layouts from layout file
   * 'row_lightning_schedule.xml'
   * for easy to all layout elements.
   *
   * @author ButterKnifeZelezny, plugin for Android Studio by Inmite Developers
   *         (http://inmite.github.io)
   */

  static class ViewHolder {
    @Bind(R.id.lightning_schedule_title) TextView mLightningScheduleTitle;
    @Bind(R.id.lightning_schedule_speaker) TextView mLightningScheduleSpeaker;
    @Bind(R.id.lightning_card_container) FrameLayout mLightningCardContainer;

    ViewHolder(View view) {
      ButterKnife.bind(this, view);
    }
  }
}