package org.devconmyanmar.apps.devcon.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
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
import org.devconmyanmar.apps.devcon.R;
import org.devconmyanmar.apps.devcon.db.SpeakerDao;
import org.devconmyanmar.apps.devcon.model.Talk;

/**
 * Created by yemyatthu on 11/15/14.
 */
public class LighteningTalkAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
  private List<Talk> mTalks = new ArrayList<>();
  private Context mContext;

  public LighteningTalkAdapter(Context context) {
    mContext = context;
  }

  public void replaceWith(List<Talk> talks) {
    mTalks = talks;
    notifyDataSetChanged();
  }

  @Override public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    LayoutInflater mInflater = LayoutInflater.from(mContext);
    View keynoteView = mInflater.inflate(R.layout.row_keynote, parent, false);
    return new ViewHolder(keynoteView);
  }

  @Override public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
    Talk mTalk = mTalks.get(position);
    Resources r = mContext.getResources();
    int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, r.getDisplayMetrics());
    ViewHolder mViewHolder = (ViewHolder) holder;
    mViewHolder.mLightningScheduleTitle.setText(mTalk.getTitle());
    String speakers = flatternSpeakerNames(mTalk.getSpeakers());
    mViewHolder.mLightningScheduleSpeaker.setText(speakers);
    if (Build.VERSION.SDK_INT >= 21) {
      mViewHolder.mLightningCardContainer.setPadding(0, px, 0, px);
    }
  }

  @Override public long getItemId(int i) {
    return mTalks.get(i).getId();
  }

  @Override public int getItemCount() {
    return 0;
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

  static class ViewHolder extends RecyclerView.ViewHolder {
    @Bind(R.id.lightning_schedule_title) TextView mLightningScheduleTitle;
    @Bind(R.id.lightning_schedule_speaker) TextView mLightningScheduleSpeaker;
    @Bind(R.id.lightning_card_container) FrameLayout mLightningCardContainer;

    ViewHolder(View view) {
      super(view);
      ButterKnife.bind(this, view);
    }
  }
}