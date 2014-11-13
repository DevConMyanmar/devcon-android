package org.devconmyanmar.apps.devcon.adapter;

import android.content.Context;
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
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.List;
import org.devconmyanmar.apps.devcon.R;
import org.devconmyanmar.apps.devcon.db.MyScheduleDao;
import org.devconmyanmar.apps.devcon.db.SpeakerDao;
import org.devconmyanmar.apps.devcon.model.MySchedule;
import org.devconmyanmar.apps.devcon.model.Speaker;
import org.devconmyanmar.apps.devcon.model.Talk;
import org.devconmyanmar.apps.devcon.utils.TimeUtils;

import static org.devconmyanmar.apps.devcon.utils.LogUtils.makeLogTag;

/**
 * Created by yemyatthu on 11/11/14.
 */
public class MyScheduleAdapter extends BaseAdapter {
  private static final int VIEW_TYPE_FAVORITE = 1;
  private static final int VIEW_TYPE_BROWSE = 2;

  private static final String TAG = makeLogTag(MyScheduleAdapter.class);

  private Context mContext;
  private LayoutInflater mLayoutInflater;
  private List<MySchedule> mMySchedules = new ArrayList<MySchedule>();
  private MyScheduleDao myScheduleDao;
  private SpeakerDao speakerDao;

  public MyScheduleAdapter(Context context) {
    this.mContext = context;
    this.mLayoutInflater = LayoutInflater.from(mContext);
    this.myScheduleDao = new MyScheduleDao(context);
    this.speakerDao = new SpeakerDao(context);
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
    MySchedule mySchedule = (MySchedule) getItem(i);
    int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4,
        mContext.getResources().getDisplayMetrics());
    if (view != null) {
      holder = (ViewHolder) view.getTag();
    } else {
      view = mLayoutInflater.inflate(R.layout.my_schedule_items, viewGroup, false);
      holder = new ViewHolder(view);
      view.setTag(holder);
    }
    if (Build.VERSION.SDK_INT >= 21) {
      holder.mFavoriteCardContainer.setPadding(0, px, 0, px);
    }
    String lFormattedFrom = TimeUtils.parseFromToString(mySchedule.getStart());
    holder.mFavoriteScheduleTitle.setText(mySchedule.getTitle());
    holder.mFavoriteScheduleFromTime.setText(lFormattedFrom);
    holder.mFavoriteScheduleSpeakers.setText(mySchedule.getSubTitle());

    List<Talk> favTalks = myScheduleDao.favedTalk(mySchedule);
    if (favTalks.size() == 1) {
      Talk talk = favTalks.get(0);
      holder.mFavoriteScheduleTitle.setText(talk.getTitle());
      ArrayList<Speaker> speakers = flatternSpeakers(talk.getSpeakers());
      StringBuilder stringBuilder = new StringBuilder();
      for (int j = 0; j < speakers.size(); j++) {
        stringBuilder.append(speakers.get(j).getName());
        // Do not append comma at the end of last element
        if (j < (speakers.size() - 1)) {
          stringBuilder.append(", ");
        }

        holder.mFavoriteScheduleSpeakers.setText(stringBuilder.toString());
      }
    }

    return view;
  }

  private ArrayList<Speaker> flatternSpeakers(String speakers) {
    ArrayList<Speaker> mSpeakers = new ArrayList<Speaker>();
    String id[] = new Gson().fromJson(speakers, String[].class);
    for (String anId : id) {
      mSpeakers.add(speakerDao.getSpeakerById(anId));
    }

    return mSpeakers;
  }

  /**
   * This class contains all butterknife-injected Views & Layouts from layout file
   * 'my_schedule_items.xml'
   * for easy to all layout elements.
   *
   * @author ButterKnifeZelezny, plugin for Android Studio by Inmite Developers
   *         (http://inmite.github.io)
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
