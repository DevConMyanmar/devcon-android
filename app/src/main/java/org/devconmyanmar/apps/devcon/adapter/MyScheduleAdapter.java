package org.devconmyanmar.apps.devcon.adapter;

import android.content.Context;
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
import org.devconmyanmar.apps.devcon.utils.TimeUtils;

/**
 * Created by yemyatthu on 11/11/14.
 */
public class MyScheduleAdapter extends BaseAdapter {
  private Context mContext;
  private LayoutInflater mLayoutInflater;
  private List<Talk> mTalks = new ArrayList<Talk>();
  private static final int VIEW_TYPE_FAVORITE = 1;
  private static final int VIEW_TYPE_BROWSE = 2;

  public MyScheduleAdapter(Context context) {
    mContext = context;
    mLayoutInflater = LayoutInflater.from(mContext);
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
    return i;
  }

  @Override public View getView(int i, View view, ViewGroup viewGroup) {
    ViewHolder holder;
    Talk mTalk = (Talk) getItem(i);
    if (view != null) {
      holder = (ViewHolder) view.getTag();
    } else {
      view = mLayoutInflater.inflate(R.layout.my_schedule_items, viewGroup, false);
      holder = new ViewHolder(view);
      view.setTag(holder);
    }
    switch(mTalk.getTalk_type()) {
      case VIEW_TYPE_FAVORITE:
        String normalFormattedFrom = TimeUtils.parseFromToString(mTalk.getFrom_time());
        String normalFormattedTo = TimeUtils.parseFromToString(mTalk.getTo_time());
        holder.mFavoriteScheduleTitle.setText(mTalk.getTitle());
        holder.mFavoriteScheduleFromTime.setText(normalFormattedFrom);
        holder.mFavoriteScheduleToTime.setText(normalFormattedTo);
        holder.mFavoriteScheduleSpeakers.setText(mTalk.getSpeakers());

        
    }

    return view;
  }

  /**
   * This class contains all butterknife-injected Views & Layouts from layout file 'my_schedule_items.xml'
   * for easy to all layout elements.
   *
   * @author ButterKnifeZelezny, plugin for Android Studio by Inmite Developers (http://inmite.github.io)
   */

  class ViewHolder {
    @InjectView(R.id.favorite_schedule_from_time) TextView mFavoriteScheduleFromTime;
    @InjectView(R.id.favorite_schedule_to_time) TextView mFavoriteScheduleToTime;
    @InjectView(R.id.favorite_schedule_title) TextView mFavoriteScheduleTitle;
    @InjectView(R.id.favorite_schedule_speakers) TextView mFavoriteScheduleSpeakers;
    @InjectView(R.id.favorite_card_container) FrameLayout mFavoriteCardContainer;

    ViewHolder(View view) {
      ButterKnife.inject(this, view);
    }
  }
}
