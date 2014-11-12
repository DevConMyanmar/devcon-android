package org.devconmyanmar.apps.devcon.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import java.util.ArrayList;
import java.util.List;
import org.devconmyanmar.apps.devcon.R;
import org.devconmyanmar.apps.devcon.model.MySchedule;
import org.devconmyanmar.apps.devcon.ui.TalkChooserActivity;
import org.devconmyanmar.apps.devcon.utils.TimeUtils;

/**
 * Created by yemyatthu on 11/11/14.
 */
public class MyScheduleAdapter extends BaseAdapter {
  private Context mContext;
  private LayoutInflater mLayoutInflater;
  private List<MySchedule> mMySchedules = new ArrayList<MySchedule>();
  private static final int VIEW_TYPE_FAVORITE = 1;
  private static final int VIEW_TYPE_BROWSE = 2;

  public MyScheduleAdapter(Context context) {
    mContext = context;
    mLayoutInflater = LayoutInflater.from(mContext);
  }

  public void replaceWith(List<MySchedule> mySchedules) {
    mMySchedules = mySchedules;
    notifyDataSetChanged();
  }

  @Override public int getCount() {
    return mMySchedules.size();
  }

  @Override public Object getItem(int i) {
    return mMySchedules.get(i);
  }

  @Override public long getItemId(int i) {
    return i;
  }

  @Override public View getView(int i, View view, ViewGroup viewGroup) {
    ViewHolder holder;
    final MySchedule mySchedule = (MySchedule) getItem(i);
    int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4,
        mContext.getResources().getDisplayMetrics());
    if (view != null) {
      holder = (ViewHolder) view.getTag();
    } else {
      view = mLayoutInflater.inflate(R.layout.my_schedule_items, viewGroup, false);
      holder = new ViewHolder(view);
      view.setTag(holder);
    }
    String lFormattedFrom = TimeUtils.parseFromToString(mySchedule.getStart());
    System.out.println(lFormattedFrom);
      holder.mFavoriteScheduleTitle.setText(mySchedule.getTitle());
      holder.mFavoriteScheduleFromTime.setText(lFormattedFrom);
      holder.mFavoriteScheduleSpeakers.setText(mySchedule.getSubTitle());
    if (Build.VERSION.SDK_INT >= 21) {
      holder.mFavoriteCardContainer.setPadding(0, px, 0, px);

    }
    holder.mFavoriteCardContainer.setOnClickListener(new View.OnClickListener() {

      @Override public void onClick(View view) {
        Intent i = new Intent(mContext, TalkChooserActivity.class);
        i.putExtra("START_TIME",mySchedule.getStart());
        i.putExtra("END_TIME",mySchedule.getEnd());
        mContext.startActivity(i);

      }
    });
    holder.mFavoriteCardContainer.setBackgroundColor(android.R.attr.selectableItemBackground);
    if(mySchedule.getTitle().equals("Lunch Break")){
      RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
          RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
      holder.mFavoriteCardContainer.setLayoutParams(layoutParams);
      holder.mFavoriteCardContainer.setClickable(false);
      holder.mFavoriteCardContainer.setEnabled(false);
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
    @InjectView(R.id.favorite_schedule_title) TextView mFavoriteScheduleTitle;
    @InjectView(R.id.favorite_schedule_speakers) TextView mFavoriteScheduleSpeakers;
    @InjectView(R.id.favorite_card_container) RelativeLayout mFavoriteCardContainer;

    ViewHolder(View view) {
      ButterKnife.inject(this, view);
    }
  }
}
