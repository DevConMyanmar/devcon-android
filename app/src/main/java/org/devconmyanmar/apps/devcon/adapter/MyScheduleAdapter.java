package org.devconmyanmar.apps.devcon.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import java.util.ArrayList;
import java.util.List;
import org.devconmyanmar.apps.devcon.R;
import org.devconmyanmar.apps.devcon.model.Talk;

/**
 * Created by yemyatthu on 11/11/14.
 */
public class MyScheduleAdapter extends BaseAdapter {
  Context mContext;
  LayoutInflater mLayoutInflater;
  List<Talk> mTalks = new ArrayList<Talk>();

  public MyScheduleAdapter(Context context) {
    mContext = context;
    mLayoutInflater = LayoutInflater.from(mContext);
  }

  public void replaceWith(List<Talk> talks) {
    mTalks = talks;
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
    if (view != null) {
      holder = (ViewHolder) view.getTag();
    } else {
      view =mLayoutInflater.inflate(R.layout.my_schedule_items, viewGroup, false);
      holder = new ViewHolder(view);
      view.setTag(holder);
    }
    return view;
  }

  /**
   * This class contains all butterknife-injected Views & Layouts from layout file 'my_schedule_items.xml'
   * for easy to all layout elements.
   *
   * @author ButterKnifeZelezny, plugin for Android Studio by Inmite Developers (http://inmite.github.io)
   */
  static class ViewHolder {
    @InjectView(R.id.lightning_schedule_from_time) TextView mLightningScheduleFromTime;
    @InjectView(R.id.lightning_schedule_to_time) TextView mLightningScheduleToTime;
    @InjectView(R.id.lightning_schedule_title) TextView mLightningScheduleTitle;
    @InjectView(R.id.lightning_card_container) LinearLayout mLightningCardContainer;

    ViewHolder(View view) {
      ButterKnife.inject(this, view);
    }
  }
}
